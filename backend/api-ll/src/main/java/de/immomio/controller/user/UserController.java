package de.immomio.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.immomio.beans.ChangeEmailBean;
import de.immomio.beans.ChangePasswordBean;
import de.immomio.beans.RegisterResultBean;
import de.immomio.beans.ResetPasswordBean;
import de.immomio.beans.ResetPasswordConfirmBean;
import de.immomio.beans.TokenBean;
import de.immomio.beans.landlord.AgentActivateErrorBean;
import de.immomio.beans.landlord.AgentEditBean;
import de.immomio.beans.landlord.CustomerUserBean;
import de.immomio.beans.landlord.LandlordRegisterBean;
import de.immomio.beans.landlord.LandlordUserSearchBean;
import de.immomio.beans.shared.conversation.ConversationConfigBean;
import de.immomio.config.CustomExposePdfProperties;
import de.immomio.constants.exceptions.UserNotFoundException;
import de.immomio.controller.BaseController;
import de.immomio.controller.search.property.SearchProperty;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUserOverviewBean;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.data.landlord.entity.user.changeemail.ChangeEmail;
import de.immomio.data.landlord.entity.user.changepassword.ChangePassword;
import de.immomio.data.landlord.entity.user.profile.LandlordUserProfile;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.landlord.service.user.LandlordChangeEmailService;
import de.immomio.landlord.service.user.LandlordChangePasswordService;
import de.immomio.landlord.service.user.LandlordOnboardingService;
import de.immomio.landlord.service.user.LandlordUserSearchService;
import de.immomio.landlord.service.user.LandlordUserService;
import de.immomio.model.repository.landlord.customer.user.changeemail.LandlordChangeEmailRepository;
import de.immomio.service.conversation.ConversationConfigService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RepositoryRestController
@RequestMapping(value = "/users")
public class UserController extends BaseController {

    private static final String USER_EXISTS_IN_KEYCLOAK = "USER_WITH_EMAIL_ALREADY_EXISTS_IN_KEYCLOAK_L";
    private static final String EMAIL_ALREADY_VERIFIED_MESSAGE = "EMAIL_ALREADY_VERIFIED_L";
    private static final String NO_USER_AUTHORIZED = "NO_USER_AUTHORIZED_L";
    private static final String SOMETHING_WENT_WRONG = "SOMETHING_WENT_WRONG_L";
    private static final String ERRORS_PASSWORD_RESET_NO_EMAIL_PROVIDED = "errors.password.reset.noEmailProvided";
    private static final String MISSING_ADMIN_AUTHORITY = "MISSING_ADMIN_AUTHORITY_L";
    private static final String ONLY_ADMIN_REMAINING_L = "UNABLE_TO_DELETE_ONLY_ADMIN_REMAINING_L";
    private static final String CONTAINS_OWN_ADMIN_ACCOUNT_L = "CANNOT_DELETE_OWN_ACCOUNT_L";
    private static final String AGENT_IS_COMPANY_ADMIN_NO_DOWNGRADE_POSSIBLE =
            "AGENT_IS_COMPANY_ADMIN_NO_DOWNGRADE_POSSIBLE_L";
    private static final String AGENT_ALREADY_ACTIVATED = "AGENT_ALREADY_ACTIVATED_L";
    private static final String NO_FREE_AGENT_SLOTS_LEFT = "NO_FREE_AGENT_SLOTS_LEFT_L";
    private static final String NOT_ENOUGH_AGENT_SLOTS_LEFT = "NOT_ENOUGH_AGENT_SLOTS_LEFT_L";
    private static final String USER_CANNOT_DEACTIVATE_HIMSELF = "USER_CANNOT_DEACTIVATE_HIMSELF_L";
    private static final String AGENT_ALREADY_DEACTIVATED = "AGENT_ALREADY_DEACTIVATED_L";

    private static final String CUSTOM_PDF_EXPOSE = "customPdfExpose";

    private final LandlordUserService landlordUserService;

    private final LandlordChangeEmailRepository changeEmailRepository;

    private final LandlordOnboardingService landlordOnboardingservice;

    private final LandlordChangePasswordService changePasswordService;

    private final UserSecurityService userSecurityService;

    private final LandlordChangeEmailService landlordChangeEmailService;

    private final ConversationConfigService conversationConfigService;

    private final LandlordUserSearchService userSearchService;

    @Value("${immomio.endpoints.ui.panel}")
    private String uiPanel;

    @Autowired
    private CustomExposePdfProperties customExposePdfProperties;

    @Autowired
    public UserController(
            LandlordChangeEmailRepository changeEmailRepository,
            LandlordUserService landlordUserService,
            LandlordOnboardingService landlordOnboardingservice,
            LandlordChangePasswordService changePasswordService,
            UserSecurityService userSecurityService,
            LandlordChangeEmailService landlordChangeEmailService,
            ConversationConfigService conversationConfigService,
            LandlordUserSearchService userSearchService) {
        this.landlordUserService = landlordUserService;
        this.changeEmailRepository = changeEmailRepository;
        this.landlordOnboardingservice = landlordOnboardingservice;
        this.changePasswordService = changePasswordService;
        this.userSecurityService = userSecurityService;
        this.landlordChangeEmailService = landlordChangeEmailService;
        this.conversationConfigService = conversationConfigService;
        this.userSearchService = userSearchService;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Object> register(
           @Parameter(hidden = true)  PersistentEntityResourceAssembler assembler,
            @RequestBody @Valid LandlordRegisterBean registerBean,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(new CollectionModel<>(result.getAllErrors()), HttpStatus.BAD_REQUEST);
        }

        RegisterResultBean<LandlordUser> rrb = landlordOnboardingservice.register(registerBean);

        if (rrb.hasError()) {
            result.addError(rrb.getError());
            return badRequest(result);
        }

        landlordUserService.sendEmailVerifyNotification(rrb.getRegisteredUser());
        return new ResponseEntity<>(assembler.toModel(rrb.getRegisteredUser()), HttpStatus.OK);
    }

    @GetMapping(value = "/me")
    public @ResponseBody
    PersistentEntityResource me(PersistentEntityResourceAssembler assembler) {
        LandlordUser user = userSecurityService.getPrincipal();
        Long customerId = user.getCustomer().getId();
        Map<Long, String> customExposePdfCustomers = customExposePdfProperties.getCustomers();

        String customPdfUrl = customExposePdfCustomers.get(customerId);
        if (StringUtils.isNotBlank(customPdfUrl)) {
            user.getPreferences().put(CUSTOM_PDF_EXPOSE, customPdfUrl);
        }

        return assembler.toModel(user);
    }

    @PostMapping(value = "/preferences/showRentedFlats")
    public ResponseEntity<Object> showRentedFlats(
            @RequestParam("show") boolean show,
           @Parameter(hidden = true)  PersistentEntityResourceAssembler assembler
    ) {
        LandlordUser user = userSecurityService.getPrincipalUser();
        user = landlordUserService.updateShowRentedFlatsPreference(show, user);

        return new ResponseEntity<>(assembler.toModel(user), HttpStatus.ACCEPTED);
    }

    @PutMapping(value = "/email")
    public ResponseEntity<Object> email(
            @RequestBody @Valid ChangeEmailBean email,
            BindingResult result,
            @Parameter(hidden = true)  PersistentEntityResourceAssembler assembler) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(new CollectionModel<>(result.getAllErrors()), HttpStatus.BAD_REQUEST);
        } else if (landlordUserService.findByEmail(email.getEmail()) != null) {
            return new ResponseEntity<>(
                    new CollectionModel<>(Collections.singletonList("The new email already exists in our system. " +
                            "Please select another email.")), HttpStatus.CONFLICT);
        }

        LandlordUser user = userSecurityService.getPrincipalUser();

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<ChangeEmail> existing = changeEmailRepository.findByUser(user);

        ChangeEmail changeEmail = new ChangeEmail();
        changeEmail.setUser(user);
        changeEmail.setEmail(email.getEmail());
        changeEmail.setToken(UUID.randomUUID().toString());

        try {
            changeEmailRepository.save(changeEmail);
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonList(e.getMessage()), HttpStatus.BAD_REQUEST);
        }

        changeEmailRepository.deleteAll(existing);
        landlordChangeEmailService.sendChangeEmailNotification(user, changeEmail);

        return new ResponseEntity<>(assembler.toModel(changeEmail), HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/email/confirm")
    public ResponseEntity<Object> confirmEmail(@RequestParam String token) {
        if (token == null || token.trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ChangeEmail changeEmail = changeEmailRepository.findByToken(token);
        if (changeEmail == null || changeEmail.getUser() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (landlordUserService.changeEmail(changeEmail.getUser(), changeEmail.getEmail())) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/password")
    public ResponseEntity<Object> password(@RequestBody @Valid ChangePasswordBean changePassword,
                                           BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(new CollectionModel<>(result.getAllErrors()), HttpStatus.BAD_REQUEST);
        }

        LandlordUser user = userSecurityService.getPrincipalUser();
        boolean bool = changePasswordService.changePassword(user, changePassword, result);

        if (result.hasErrors()) {
            return new ResponseEntity<>(new CollectionModel<>(result.getAllErrors()), HttpStatus.BAD_REQUEST);
        }
        if (!bool) {
            return new ResponseEntity<>(SOMETHING_WENT_WRONG, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/reset-password")
    public ResponseEntity<Object> resetPassword(
            @RequestBody @Valid ResetPasswordBean resetPassword,
            BindingResult result,
           @Parameter(hidden = true)  PersistentEntityResourceAssembler assembler
    ) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(new CollectionModel<>(result.getAllErrors()), HttpStatus.BAD_REQUEST);
        }

        if (resetPassword.getEmail() == null || resetPassword.getEmail().isEmpty()) {
            return new ResponseEntity<>(ERRORS_PASSWORD_RESET_NO_EMAIL_PROVIDED, HttpStatus.BAD_REQUEST);
        }

        ChangePassword cp;
        try {
            cp = changePasswordService.resetPassword(resetPassword.getEmail());
        } catch (UserNotFoundException e) {
            try {
                //some old users may have email addresses with uppercase letters. Due to that, we implemented this fallback
                cp = changePasswordService.resetPassword(resetPassword.getEmail().toLowerCase());
            } catch (UserNotFoundException ex) {
                log.error(e.getMessage());
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }

        if (cp == null) {
            return new ResponseEntity<>(new CollectionModel<>(result.getAllErrors()), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(assembler.toModel(cp), HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/reset-password/confirm")
    public ResponseEntity<Object> resetPassword(
            @RequestBody @Valid ResetPasswordConfirmBean changePassword,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(new CollectionModel<>(result.getAllErrors()), HttpStatus.BAD_REQUEST);
        }

        boolean passwordChangeSuccess = changePasswordService.changePassword(changePassword, result);
        if (result.hasErrors()) {
            return new ResponseEntity<>(new CollectionModel<>(result.getAllErrors()), HttpStatus.BAD_REQUEST);
        }
        if (!passwordChangeSuccess) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/addAgent")
    public ResponseEntity addAgent(@RequestBody CustomerUserBean customerUserBean) {
        if (!userSecurityService.isCompanyAdmin()) {
            return new ResponseEntity<>("MISSING_ADMIN_AUTHORITY_L", HttpStatus.UNAUTHORIZED);
        }
        if (landlordOnboardingservice.findByEmail(customerUserBean.getEmail()) != null) {
            return new ResponseEntity<>("USER_WITH_EMAIL_ALREADY_EXISTS_L", HttpStatus.BAD_REQUEST);
        }

        LandlordCustomer customer = userSecurityService.getPrincipal().getCustomer();

        return createAgentWithEnabled(customer, customerUserBean, landlordUserService.freeAgentSlotCheck(customer));
    }

    private ResponseEntity<Object> createAgentWithEnabled(
            LandlordCustomer customer,
            CustomerUserBean customerUserBean,
            boolean enabled
    ) {
        LandlordUser newAgent = new LandlordUser();
        LandlordUserProfile newAgentProfile = new LandlordUserProfile();

        newAgentProfile.setFirstname(customerUserBean.getFirstname());
        newAgentProfile.setName(customerUserBean.getName());
        newAgentProfile.setTitle(customerUserBean.getTitle());
        newAgentProfile.setGender(customerUserBean.getGender());
        newAgentProfile.setPortrait(customerUserBean.getPortrait());
        newAgentProfile.setPhone(customerUserBean.getPhone());

        newAgent.setCustomer(customer);
        newAgent.setUsertype(customerUserBean.getUsertype());
        newAgent.setEmail(customerUserBean.getEmail());
        newAgent.setEnabled(enabled);
        newAgent.setProfile(newAgentProfile);

        try {
            landlordUserService.customSave(newAgent);

            if (!landlordOnboardingservice.createInKeycloak(customerUserBean, enabled)) {
                landlordUserService.customDelete(newAgent);
                return new ResponseEntity<>(USER_EXISTS_IN_KEYCLOAK, HttpStatus.BAD_REQUEST);
            }

            if (enabled) {
                changePasswordService.newUser(newAgent);
            }

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/removeAgents")
    public ResponseEntity removeAgents(@RequestParam List<LandlordUser> agents) {
        if (!userSecurityService.isCompanyAdmin()) {
            return new ResponseEntity<>(MISSING_ADMIN_AUTHORITY, HttpStatus.UNAUTHORIZED);
        }

        if (landlordUserService.containsLoggedInUser(agents)) {
            return new ResponseEntity<>(CONTAINS_OWN_ADMIN_ACCOUNT_L, HttpStatus.BAD_REQUEST);
        }

        try {
            landlordUserService.removeAgents(agents);

            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/editAgent/{user}")
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "user", schema = @Schema(type = "Long"), required = true)})
    public ResponseEntity editAgent(@PathVariable LandlordUser user, @RequestBody AgentEditBean agentEditBean) {

        if (nonAdminTryingToEditOtherUser(user)) {
            return new ResponseEntity<>(MISSING_ADMIN_AUTHORITY, HttpStatus.UNAUTHORIZED);
        }

        if (isRemovingCompanyAdminRole(user, agentEditBean)) {
            return new ResponseEntity<>(AGENT_IS_COMPANY_ADMIN_NO_DOWNGRADE_POSSIBLE, HttpStatus.BAD_REQUEST);
        }
        if (agentEditBean.getUsertype() != null) {
            user.setUsertype(agentEditBean.getUsertype());
        }

        LandlordUserProfile profile = user.getProfile();
        profile.setGender(agentEditBean.getGender());
        profile.setTitle(agentEditBean.getTitle());
        profile.setFirstname(agentEditBean.getFirstname());
        profile.setName(agentEditBean.getName());
        profile.setPhone(agentEditBean.getPhone());
        profile.setPortrait(agentEditBean.getPortrait());

        try {
            landlordUserService.customSave(user);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isRemovingCompanyAdminRole(LandlordUser user, AgentEditBean agentEditBean) {
        return user.getUsertype().equals(LandlordUsertype.COMPANYADMIN)
                && agentEditBean.getUsertype() != null
                && !agentEditBean.getUsertype().equals(LandlordUsertype.COMPANYADMIN);
    }

    private boolean nonAdminTryingToEditOtherUser(LandlordUser user) {
        LandlordUser principal = userSecurityService.getPrincipalUser();
        return !user.equals(principal) && !principal.getUsertype().equals(LandlordUsertype.COMPANYADMIN);
    }

    @PostMapping(value = "/searchPreferences")
    public ResponseEntity saveSearchPreferences(@RequestBody SearchProperty searchProperty, String name) {
        LandlordUser principal = userSecurityService.getPrincipalUser();
        if (principal == null) {
            return new ResponseEntity<>(NO_USER_AUTHORIZED, HttpStatus.BAD_REQUEST);
        }
        landlordUserService.saveSearchPreferences(principal, searchProperty, name);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/searchPreferences")
    public ResponseEntity loadSearchPreferences() {
        LandlordUser principal = userSecurityService.getPrincipalUser();
        if (principal == null) {
            return new ResponseEntity<>(NO_USER_AUTHORIZED, HttpStatus.BAD_REQUEST);
        }
        Object searchPreferences = landlordUserService.loadSearchPreferences(principal);

        return new ResponseEntity<>(searchPreferences, HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/activateAgents")
    public ResponseEntity activateAgent(@RequestParam("user") List<LandlordUser> users) {
        if (!userSecurityService.isCompanyAdmin()) {
            return new ResponseEntity<>(MISSING_ADMIN_AUTHORITY, HttpStatus.UNAUTHORIZED);
        }

        if (!landlordUserService.freeAgentSlotCheck(userSecurityService.getPrincipal().getCustomer(), users.size())) {
            return new ResponseEntity<>(NOT_ENOUGH_AGENT_SLOTS_LEFT, HttpStatus.BAD_REQUEST);
        }

        List<AgentActivateErrorBean> errorBeans = landlordUserService.activateUser(users);
        if (errorBeans.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(errorBeans, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/deactivateAgent/{user}")
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "user", schema = @Schema(type = "Long"), required = true)})
    public ResponseEntity deactivateAgent(@PathVariable LandlordUser user) {

        if (!userSecurityService.isCompanyAdmin()) {
            return new ResponseEntity<>(MISSING_ADMIN_AUTHORITY, HttpStatus.UNAUTHORIZED);
        }

        if (userSecurityService.getPrincipalUser().equals(user)) {
            return new ResponseEntity<>(USER_CANNOT_DEACTIVATE_HIMSELF, HttpStatus.BAD_REQUEST);
        }

        if (!user.isEnabled()) {
            return new ResponseEntity<>(AGENT_ALREADY_DEACTIVATED, HttpStatus.BAD_REQUEST);
        }

        return landlordUserService.deactivateUser(user);
    }

    @PostMapping(value = "/resend-email-verification/{user}")
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "user", schema = @Schema(type = "Long"), required = true)})
    public ResponseEntity<Object> resendEmailVerification(@PathVariable LandlordUser user) {
        if (user.getEmailVerified() != null) {
            return new ResponseEntity<>(EMAIL_ALREADY_VERIFIED_MESSAGE, HttpStatus.BAD_REQUEST);
        }

        landlordUserService.sendEmailVerifyNotification(user);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/verify-email/{userID}")
    public ResponseEntity verifyEmail(@PathVariable Long userID,
                                      @RequestBody TokenBean emailVerification) {

        LandlordUser user = landlordUserService.customFindOne(userID);

        if (user.getEmailVerified() != null) {
            return new ResponseEntity<>(EMAIL_ALREADY_VERIFIED_MESSAGE, HttpStatus.BAD_REQUEST);
        }

        landlordUserService.verifyEmail(user, emailVerification.getToken());
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/updateLastLogin")
    public ResponseEntity updateLastLogin() {
        LandlordUser principal = userSecurityService.getPrincipalUser();
        if (principal == null) {
            return new ResponseEntity<>(NO_USER_AUTHORIZED, HttpStatus.BAD_REQUEST);
        }
        try {
            landlordUserService.updateLastLogin(principal);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/conversationConfigs")
    public ResponseEntity saveConversationConfigs(@RequestBody ConversationConfigBean configBean) {
        LandlordUser principal = userSecurityService.getPrincipalUser();
        conversationConfigService.validateConversationConfigBean(configBean);
        try {
            landlordUserService.saveconversationConfigs(principal, configBean);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/search/list")
    public ResponseEntity<PagedModel<EntityModel<LandlordUserOverviewBean>>> search(
            @RequestBody LandlordUserSearchBean searchBean,
            PagedResourcesAssembler<LandlordUserOverviewBean> pagedResourcesAssembler) {
        PageImpl<LandlordUserOverviewBean> userBeans = userSearchService.search(searchBean);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(userBeans));
    }

    @PostMapping("search/list/count")
    public ResponseEntity<?> findApplicationsCount(
            @RequestBody Map<String, LandlordUserSearchBean> userSearchBeanMap
    ) {
        return new ResponseEntity<>(userSearchService.getCountOfUsers(userSearchBeanMap), HttpStatus.OK);
    }
}
