package de.immomio.docusign.service;

import com.docusign.esign.api.UsersApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.ForgottenPasswordInformation;
import com.docusign.esign.model.NewUser;
import com.docusign.esign.model.NewUsersDefinition;
import com.docusign.esign.model.NewUsersSummary;
import com.docusign.esign.model.UserInformation;
import com.docusign.esign.model.UserSettingsInformation;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.constants.exceptions.DocuSignApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DocuSignUserService {

    private static final String DOCUSIGN_NAME = "docusign+";
    private static final String EMAIL_DOMAIN = "@immomio.de";
    private static final String USER_STATUS_ACTIVE = "Active";
    private static final String QUESTION_1 = "What is the nickname of our CEO?";
    private static final String ANSWER_1 = "Bobo";

    private final DocuSignApiClient docuSignApiClient;
    private final DocuSignUserConfig docuSignUserConfig;

    @Autowired
    public DocuSignUserService(
            DocuSignApiClient docuSignApiClient,
            DocuSignUserConfig docuSignUserConfig
    ) {
        this.docuSignApiClient = docuSignApiClient;
        this.docuSignUserConfig = docuSignUserConfig;
    }

    public NewUser createApiUser(long customerId) throws DocuSignApiException {
        ApiClient apiClient = docuSignApiClient.getApiClient(docuSignUserConfig.getAdminUserId());
        String accountId = docuSignApiClient.getAccountId();
        UsersApi usersApi = new UsersApi(apiClient);

        try {
            UserInformation userInformation = new UserInformation();
            String username = DOCUSIGN_NAME + customerId;
            userInformation.setEmail(username + EMAIL_DOMAIN);
            userInformation.setUserName(username);
            userInformation.setPermissionProfileId(docuSignUserConfig.getPermissionProfileId());
            userInformation.setUserStatus(USER_STATUS_ACTIVE);
            userInformation.setPassword(generateRandomPassword());
            UserSettingsInformation settings = new UserSettingsInformation();

            settings.setAllowRecipientLanguageSelection(Boolean.TRUE.toString());
            settings.setAllowSupplementalDocuments(Boolean.TRUE.toString());
            settings.setCanManageTemplates("use");
            settings.setCanSendEnvelope(Boolean.TRUE.toString());
            settings.setEnableSequentialSigningAPI(Boolean.TRUE.toString());
            settings.setEnableSequentialSigningUI(Boolean.TRUE.toString());
            settings.setEnableSignerAttachments(Boolean.TRUE.toString());
            settings.setEnableSignOnPaperOverride(Boolean.TRUE.toString());
            settings.setPowerFormMode("user");
            settings.setRecipientViewedNotification(Boolean.TRUE.toString());
            settings.setSupplementalDocumentsMustAccept(Boolean.TRUE.toString());
            settings.setSupplementalDocumentsMustRead(Boolean.TRUE.toString());
            settings.setSupplementalDocumentsMustView(Boolean.TRUE.toString());
            userInformation.setUserSettings(settings);
            ForgottenPasswordInformation forgotten = new ForgottenPasswordInformation();
            forgotten.setForgottenPasswordQuestion1(QUESTION_1);
            forgotten.setForgottenPasswordAnswer1(ANSWER_1);
            userInformation.setForgottenPasswordInfo(forgotten);
            userInformation.setSendActivationEmail(Boolean.FALSE.toString());
            NewUsersDefinition newUsersDefinition = new NewUsersDefinition();
            newUsersDefinition.addNewUsersItem(userInformation);
            NewUsersSummary usersSummary = usersApi.create(accountId, newUsersDefinition);

            /*
            Create and send envelopes. Obtain links for starting signing sessions.
            This application will be permitted to request access to your account without you being present.
            https://account-d.docusign.com/oauth/auth?response_type=code&scope=signature%20impersonation&client_id=ee3263ab-4cc9-4741-b89b-df574c174786&redirect_uri=https://www.docusign.com#/challenge/appconsent
             */

            log.info(usersSummary.toString());
            return usersSummary.getNewUsers().stream()
                    .filter(newUser -> newUser.getUserId() != null)
                    .findFirst()
                    .orElseThrow(() -> new ApiValidationException("FAILED_TO_CREATE_DS_USER_L"));
        } catch (ApiException e) {
            log.error(e.getMessage(), e);

            throw new DocuSignApiException(e.getMessage(), e.getResponseBody());
        }
    }

    private String generateRandomPassword() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .selectFrom("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+".toCharArray())
                .build();

        return generator.generate(16);
    }

}
