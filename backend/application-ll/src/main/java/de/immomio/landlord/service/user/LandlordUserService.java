package de.immomio.landlord.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.immomio.beans.landlord.AgentActivateErrorBean;
import de.immomio.beans.landlord.CustomerUserBean;
import de.immomio.beans.shared.conversation.ConversationConfigBean;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.constants.exceptions.TokenValidationException;
import de.immomio.constants.exceptions.UserNotFoundException;
import de.immomio.controller.search.property.SearchProperty;
import de.immomio.data.base.entity.customer.user.AbstractUser;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUserBean;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.mail.ModelParams;
import de.immomio.mail.configurator.LandlordMailConfigurator;
import de.immomio.mail.sender.LandlordMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.model.repository.landlord.customer.user.LandlordUserRepository;
import de.immomio.model.repository.shared.property.PropertyRepository;
import de.immomio.security.common.bean.UserEmailToken;
import de.immomio.security.service.JWTTokenService;
import de.immomio.security.service.KeycloakService;
import de.immomio.service.conversation.ConversationConfigService;
import de.immomio.service.landlord.AbstractLandlordUserService;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LandlordUserService extends AbstractLandlordUserService {

    private static final String WRONG_USER_ID = "WRONG_USER_ID_L";
    private static final String EMAIL_VERIFICATION_SUBJECT_KEY = "email.verification.subject";
    private static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR_L";
    public static final String SHOW_RENTED_FLATS_KEY = "showRentedFlats";
    private static final String PROPERTY_FILTER = "propertyFilter";
    private static final String CONVERSATION_PREFERENCES = "conversationConfigs";
    public static final String NOT_ALLOWED_TO_ACCESS_USER_L = "NOT_ALLOWED_TO_ACCESS_USER_L";

    @Value("${email.period.verificationInDays}")
    private int emailVerificationPeriod;

    private final JWTTokenService jwtTokenService;

    private final LandlordUserRepository userRepository;

    private final LandlordMailSender landlordMailSender;

    private final LandlordMailConfigurator mailConfigurator;

    private final LandlordChangePasswordService changePasswordService;

    private final KeycloakService keycloakService;

    private final LandlordChangeEmailService landlordChangeEmailService;

    private final PropertyRepository propertyRepository;

    private final UserSecurityService userSecurityService;

    private final ConversationConfigService conversationConfigService;

    @Autowired
    public LandlordUserService(
            JWTTokenService jwtTokenService,
            LandlordUserRepository userRepository,
            LandlordMailSender landlordMailSender,
            LandlordMailConfigurator mailConfigurator,
            LandlordChangePasswordService changePasswordService,
            KeycloakService keycloakService,
            LandlordChangeEmailService landlordChangeEmailService,
            PropertyRepository propertyRepository,
            UserSecurityService userSecurityService,
            ConversationConfigService conversationConfigService
    ) {
        this.jwtTokenService = jwtTokenService;
        this.userRepository = userRepository;
        this.landlordMailSender = landlordMailSender;
        this.mailConfigurator = mailConfigurator;
        this.changePasswordService = changePasswordService;
        this.keycloakService = keycloakService;
        this.landlordChangeEmailService = landlordChangeEmailService;
        this.propertyRepository = propertyRepository;
        this.userSecurityService = userSecurityService;
        this.conversationConfigService = conversationConfigService;
    }

    public LandlordUser findByEmail(String email) {
        if (email == null || email.isEmpty()) {
            return null;
        }

        return userRepository.findByEmailIgnoreCase(email);
    }

    public boolean exists(String email) {
        return findByEmail(email) != null;
    }

    public List<AgentActivateErrorBean> activateUser(List<LandlordUser> users) {
        List<AgentActivateErrorBean> errorBeans = new ArrayList<>();
        users.forEach(user -> {
            try {
                activateUserInternal(user);
            } catch (UserNotFoundException e) {
                log.error(e.getMessage(), e);
                errorBeans.add(new AgentActivateErrorBean(new CustomerUserBean(user), KeycloakService.NO_USER_WITH_THAT_EMAIL_IN_SSO_L));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                errorBeans.add(new AgentActivateErrorBean(new CustomerUserBean(user), INTERNAL_SERVER_ERROR));
            }
        });

        return errorBeans;
    }

    public ResponseEntity<Object> activateUser(LandlordUser user) {
        user.setEnabled(true);
        try {
            activateUserInternal(user);

            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void activateUserInternal(LandlordUser user) throws Exception {
        user.setEnabled(true);
        userRepository.customSave(user);
        keycloakService.activateUser(user.getEmail());

        if (user.getLastLogin() == null) {
            changePasswordService.resetPassword(user.getEmail());
        }
    }

    public ResponseEntity<Object> deactivateUser(LandlordUser user) {
        user.setEnabled(false);
        try {
            userRepository.customSave(user);
            keycloakService.deactivateUser(user.getEmail());
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void sendEmailVerifyNotification(LandlordUser user) {
        try {
            String token = jwtTokenService.generateEmailToken(user, emailVerificationPeriod);

            Map<String, Object> model = new HashMap<>();
            model.put(ModelParams.MODEL_USER, new LandlordUserBean(user));
            model.put(ModelParams.MODEL_TOKEN, token);
            model.put(ModelParams.RETURN_URL, mailConfigurator.buildAppUrl());

            landlordMailSender.send(user, MailTemplate.EMAIL_VERIFICATION_LANDLORD, EMAIL_VERIFICATION_SUBJECT_KEY, model);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    public LandlordUser updateShowRentedFlatsPreference(boolean show, LandlordUser principalUser) {
        principalUser.getPreferences().put(SHOW_RENTED_FLATS_KEY, String.valueOf(show));

        return save(principalUser);
    }

    public void verifyEmail(LandlordUser user, String token) {
        UserEmailToken userEmailToken = jwtTokenService.validateEmailToken(token);
        if (userEmailToken.getId().equals(user.getId())) {
            user.setEmailVerified(new Date());
            userRepository.customSave(user);
        } else {
            throw new TokenValidationException(WRONG_USER_ID);
        }
    }

    public LandlordUser customSave(LandlordUser user) {
        return userRepository.customSave(user);
    }

    public LandlordUser updateLastLogin(LandlordUser principal) {
        principal.setLastLogin(new Date());
        return save(principal);
    }

    public boolean changeEmail(LandlordUser user, String newEmail) {
        List<UserRepresentation> users = keycloakService.searchUser(user.getEmail());
        if (users.size() != 1) {
            log.error("Found multiple/none user(s) for " + user.getEmail() + " [" + users.size() + "]");
            return false;
        } else {
            keycloakService.changeEmail(users.get(0), newEmail);

            String oldEmail = user.getEmail();
            user.setEmail(newEmail);
            customSave(user);

            landlordChangeEmailService.sendEmailChangedNotification(user, oldEmail);
            return true;
        }
    }

    public void customDelete(LandlordUser user) {
        userRepository.customDelete(user);
    }

    @Transactional
    public void removeAgents(List<LandlordUser> agents) {
        List<String> agentEmails = agents.stream().map(AbstractUser::getEmail).collect(Collectors.toList());
        LandlordUser user = userSecurityService.getPrincipal();

        propertyRepository.updateOwner(agents, user.getId());
        userRepository.deleteAll(agents);
        keycloakService.removeUsersByEmail(agentEmails);
    }

    public LandlordUser customFindOne(Long userId) {
        return userRepository.customFindOne(userId);
    }

    public LandlordUser save(LandlordUser principalUser) {
        return userRepository.save(principalUser);
    }

    public void saveSearchPreferences(LandlordUser user, SearchProperty searchProperty, String name) {
        Map<String, Object> userPreferences = user.getPreferences();
        userPreferences.putIfAbsent(PROPERTY_FILTER, new HashMap<>());
        Map<String, Object> propertyFilters = (HashMap<String, Object>) userPreferences.get(PROPERTY_FILTER);
        propertyFilters.put(name, searchProperty);

        userRepository.save(user);
    }

    public HashMap<String, Object> loadSearchPreferences(LandlordUser user) {
        Map<String, Object> userPreferences = user.getPreferences();

        return (HashMap<String, Object>) userPreferences.getOrDefault(PROPERTY_FILTER, new HashMap<>());
    }

    public boolean containsLoggedInUser(List<LandlordUser> agents) {
        return agents.stream()
                .anyMatch(landlordUser -> landlordUser.getEmail().equals(userSecurityService.getPrincipalEmail()));
    }

    public void saveconversationConfigs(LandlordUser user, ConversationConfigBean configBean) throws JsonProcessingException {
        Map<String, Object> userPreferences = user.getPreferences();
        String serializedConfigs = conversationConfigService.serializeConfigs(configBean);
        userPreferences.put(CONVERSATION_PREFERENCES, serializedConfigs);

        userRepository.save(user);
    }

    public Optional<LandlordUser> findById(Long userId) {
        if (userId == null) {
            return Optional.empty();
        }
        Optional<LandlordUser> landlordUserOptional = userRepository.findById(userId);
        if (landlordUserOptional.isPresent()) {
            LandlordUser landlordUser = landlordUserOptional.get();
            if (!landlordUser.getCustomer().equals(userSecurityService.getPrincipalUser().getCustomer())) {
                throw new ApiValidationException(NOT_ALLOWED_TO_ACCESS_USER_L);
            }
        }
        return landlordUserOptional;
    }
}
