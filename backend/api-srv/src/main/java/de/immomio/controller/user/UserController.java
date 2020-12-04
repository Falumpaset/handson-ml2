package de.immomio.controller.user;

import de.immomio.beans.ImpersonateEmailBean;
import de.immomio.beans.landlord.AgentEditBean;
import de.immomio.beans.landlord.CustomerUserBean;
import de.immomio.controller.BaseController;
import de.immomio.data.base.entity.customer.user.AbstractUser;
import de.immomio.data.base.type.addon.AddonType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.data.landlord.entity.user.profile.LandlordUserProfile;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.model.repository.service.landlord.customer.property.PropertyRepository;
import de.immomio.model.repository.service.landlord.customer.user.LandlordUserRepository;
import de.immomio.model.repository.service.propertysearcher.customer.user.PropertySearcherUserRepository;
import de.immomio.security.common.bean.ImpersonateResponse;
import de.immomio.security.service.KeycloakService;
import de.immomio.service.landlord.LandlordChangePasswordService;
import de.immomio.service.landlord.LandlordOnboardingService;
import de.immomio.service.landlord.user.LandlordUserImportService;
import de.immomio.service.landlord.user.LandlordUserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.immomio.messaging.config.QueueConfigUtils.SchufaConfig;
import static de.immomio.messaging.config.QueueConfigUtils.TriggerJobConfig;

/**
 * @author Maik Kingma
 */

@RepositoryRestController
@RequestMapping(value = "/users")
@Slf4j
public class UserController extends BaseController {

    private static final String ONLY_ONE_USER_ASSIGNED_TO_CUSTOMER = "ONLY_ONE_USER_ASSIGNED_TO_CUSTOMER_L";
    private static final String CANNOT_DELETE_ONLY_COMPANY_ADMIN = "CANNOT_DELETE_ONLY_COMPANY_ADMIN_L";
    private final LandlordUserRepository userRepository;

    private final PropertyRepository propertyRepository;

    private final PropertySearcherUserRepository psUserRepository;

    private final KeycloakService keycloakService;

    private final LandlordOnboardingService landlordOnboardingservice;

    private final LandlordChangePasswordService changePasswordService;

    private final RabbitTemplate rabbitTemplate;

    private final LandlordUserImportService userImportService;

    private final LandlordUserService userService;

    private final PagedResourcesAssembler<Object> pagedResourcesAssembler;

    @Autowired
    public UserController(
            LandlordUserRepository userRepository,
            PropertyRepository propertyRepository,
            PropertySearcherUserRepository psUserRepository,
            KeycloakService keycloakService,
            LandlordOnboardingService landlordOnboardingservice,
            LandlordChangePasswordService changePasswordService,
            RabbitTemplate rabbitTemplate,
            LandlordUserImportService userImportService,
            LandlordUserService userService, PagedResourcesAssembler<Object> pagedResourcesAssembler) {
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
        this.psUserRepository = psUserRepository;
        this.keycloakService = keycloakService;
        this.landlordOnboardingservice = landlordOnboardingservice;
        this.changePasswordService = changePasswordService;
        this.rabbitTemplate = rabbitTemplate;
        this.userImportService = userImportService;
        this.userService = userService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @PostMapping(value = "/addAgent")
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "customer", schema = @Schema(type = "Long"), required = true)})
    public ResponseEntity addAgent(
            @RequestParam("customer") LandlordCustomer customer,
            @RequestBody CustomerUserBean customerUserBean
    ) {
        return createAgentWithEnabled(customer, customerUserBean, freeAgentSlotCheck(customer));
    }

    private ResponseEntity<Object> createAgentWithEnabled(LandlordCustomer customer,
                                                          CustomerUserBean customerUserBean, boolean enabled) {
        LandlordUser newAgent = new LandlordUser();
        LandlordUserProfile newAgentProfile = new LandlordUserProfile();

        newAgentProfile.setFirstname(customerUserBean.getFirstname());
        newAgentProfile.setName(customerUserBean.getName());
        newAgentProfile.setTitle(customerUserBean.getTitle());
        newAgentProfile.setGender(customerUserBean.getGender());
        newAgentProfile.setPortrait(customerUserBean.getPortrait());

        newAgent.setCustomer(customer);
        newAgent.setUsertype(customerUserBean.getUsertype());
        newAgent.setEmail(customerUserBean.getEmail());
        newAgent.setEnabled(enabled);
        newAgent.setProfile(newAgentProfile);

        try {
            userRepository.save(newAgent);

            if (!landlordOnboardingservice.createInKeycloak(customerUserBean, enabled)) {
                userRepository.delete(newAgent);
                return new ResponseEntity<>("USER_WITH_EMAIL_ALREADY_EXISTS_IN_KEYCLOAK_L",
                        HttpStatus.BAD_REQUEST);
            }

            if (enabled) {
                changePasswordService.resetPassword(newAgent.getEmail());
            }

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean freeAgentSlotCheck(LandlordCustomer customer) {
        long agentSlots = customer.getActiveProduct().getAddons()
                .stream()
                .filter(addonProduct ->
                        addonProduct.getAddonProduct().getAddonType().equals(AddonType.AGENT)
                ).count();
        return agentSlots > customer.getUsers().stream().filter(AbstractUser::isEnabled).count();
    }

    @DeleteMapping(value = "/removeAgent/{user}")
    public ResponseEntity removeAgent(@PathVariable LandlordUser user) {

        LandlordCustomer customer = user.getCustomer();

        if (customer.getUsers().size() == 1) {
            return new ResponseEntity<>(ONLY_ONE_USER_ASSIGNED_TO_CUSTOMER, HttpStatus.BAD_REQUEST);
        }

        List<LandlordUser> admins = customer.getCompanyAdmins();
        LandlordUser admin = admins.get(0);
        if (admin.equals(user)) {
            if (admins.size() == 1) {
                return new ResponseEntity<>(CANNOT_DELETE_ONLY_COMPANY_ADMIN, HttpStatus.BAD_REQUEST);
            }
            admin = customer.getCompanyAdmins().get(1);
        }

        try {
            List<Property> userProperties = propertyRepository.findByUsersReturnsList(new LandlordUser[]{user});
            LandlordUser finalAdmin = admin;
            userProperties.forEach(property -> property.setUser(finalAdmin));
            propertyRepository.saveAll(userProperties);
            userRepository.delete(user);
            keycloakService.removeUser(user.getEmail());
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/editAgent/{user}")
    public ResponseEntity editAgent(@PathVariable LandlordUser user, @RequestBody AgentEditBean agentEditBean) {

        if (agentEditBean.getUsertype() != null) {
            user.setUsertype(agentEditBean.getUsertype());
        }

        LandlordUserProfile profile = user.getProfile();
        if (agentEditBean.getGender() != null) {
            profile.setGender(agentEditBean.getGender());
        }
        if (agentEditBean.getTitle() != null) {
            profile.setTitle(agentEditBean.getTitle());
        }
        if (agentEditBean.getFirstname() != null) {
            profile.setFirstname(agentEditBean.getFirstname());
        }
        if (agentEditBean.getName() != null) {
            profile.setName(agentEditBean.getName());
        }
        if (agentEditBean.getPhone() != null) {
            profile.setPhone(agentEditBean.getPhone());
        }
        if (agentEditBean.getPortrait() != null) {
            profile.setPortrait(agentEditBean.getPortrait());
        }

        try {
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/activateAgent/{user}")
    public ResponseEntity activateAgent(@PathVariable LandlordUser user) {

        if (user.isEnabled()) {
            return new ResponseEntity<>("AGENT_ALREADY_ACTIVATED_L", HttpStatus.BAD_REQUEST);
        }

        if (!freeAgentSlotCheck(user.getCustomer())) {
            return new ResponseEntity<>("NO_FREE_AGENT_SLOTS_LEFT_L", HttpStatus.BAD_REQUEST);
        }

        user.setEnabled(true);
        try {
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/deactivateAgent/{user}")
    public ResponseEntity deactivateAgent(@PathVariable LandlordUser user) {

        if (!user.isEnabled()) {
            return new ResponseEntity<>("AGENT_ALREADY_DEACTIVATED_L", HttpStatus.BAD_REQUEST);
        }

        user.setEnabled(false);
        try {
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/activateps")
    public ResponseEntity acivateUsers(@RequestBody String[] mails) {
        Map<String, String> errorneousMails = new HashMap<>();
        Arrays.stream(mails).forEach(mail -> {
            try {
                PropertySearcherUser psUser = psUserRepository.findByEmail(mail);
                if (psUser != null) {
                    psUser.setEnabled(true);
                    if (psUser.getEmailVerified() == null) {
                        psUser.setEmailVerified(new Date());
                    }
                    psUserRepository.save(psUser);
                    keycloakService.activateUser(mail);
                } else {
                    errorneousMails.put(mail, "email not found in db");
                }
            } catch (Exception e) {
                errorneousMails.put(mail, e.getMessage());
            }
        });
        if (errorneousMails.isEmpty()) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(errorneousMails, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/triggerCronJob")
    public ResponseEntity triggerCronJob(@RequestParam("jobName") String jobName) {
        rabbitTemplate.convertAndSend(
                TriggerJobConfig.EXCHANGE_NAME,
                TriggerJobConfig.ROUTING_KEY,
                jobName);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/triggerSchufa")
    public ResponseEntity triggerSchufa() {
        rabbitTemplate.convertAndSend(
                SchufaConfig.EXCHANGE_NAME,
                SchufaConfig.ROUTING_KEY,
                "");
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/impersonate")
    public ResponseEntity impersonateUser(@RequestBody ImpersonateEmailBean impersonateEmailBean) {
        try {
            ImpersonateResponse impersonateData = keycloakService.impersonateUser(impersonateEmailBean.getEmail());
            return new ResponseEntity<>(impersonateData, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/importUsers")
    public ResponseEntity importUsers(
            @RequestParam("customer") LandlordCustomer customer,
            @RequestParam("type") LandlordUsertype usertype,
            @RequestParam("file") MultipartFile file,
            @RequestParam("enable") Boolean enableUsers) throws IOException {
        userImportService.importUsers(customer, file, usertype, enableUsers);

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "search/list")
    public ResponseEntity search(@RequestParam("searchTerm") String searchTerm, Pageable pageable,  PersistentEntityResourceAssembler assembler) {
        Page landlordUsers = userService.searchUsers(searchTerm, pageable);
        return new ResponseEntity(pagedResourcesAssembler.toModel(landlordUsers, assembler), HttpStatus.OK);
    }


    @GetMapping("/list")
    @Produces("text/csv")
    public void getUserList(@RequestParam("type") LandlordUsertype usertype, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain; charset=utf-8");
        response.getWriter().print(String.join(System.lineSeparator(), userService.getUsers(usertype)));
    }
}
