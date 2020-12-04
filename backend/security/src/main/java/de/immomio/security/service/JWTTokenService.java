package de.immomio.security.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.constants.exceptions.TokenValidationException;
import de.immomio.data.base.entity.customer.user.AbstractUser;
import de.immomio.data.base.type.user.profile.PropertySearcherUserProfileType;
import de.immomio.data.landlord.bean.customer.settings.theme.LandlordCustomerBrandingTheme;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.data.shared.bean.customQuestion.CustomQuestionBean;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.security.common.bean.AbstractToken;
import de.immomio.security.common.bean.ApplicationIntentToken;
import de.immomio.security.common.bean.ApplyGuestUserToken;
import de.immomio.security.common.bean.BrandingUrlToken;
import de.immomio.security.common.bean.DigitalContractSignToken;
import de.immomio.security.common.bean.GuestUserToken;
import de.immomio.security.common.bean.PreviewBrandingUrlToken;
import de.immomio.security.common.bean.UserEmailToken;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.jwt.crypto.sign.SignerVerifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class JWTTokenService {

    private static final String ERROR_PARSING_JWT_TOKEN = "ERROR_PARSING_JWT_TOKEN_L";
    private static final String TOKEN_IS_EXPIRED = "TOKEN_IS_EXPIRED_L";
    private static final String USER_PROFILE_NOT_GUEST_L = "USER_PROFILE_NOT_GUEST_L";

    @Value("${email.token.secret.key}")
    private String emailTokenSecretKey;

    @Value("${application.intent.token.secret.key}")
    private String applicationIntentSecretKey;

    @Value("${branding.token.secret.key}")
    private String brandingTokenSecretKey;

    @Value("${branding.token.ttl}")
    private int brandingTokenSecretTtl;

    @Value("${digitalcontract.token.secret.key}")
    private String contractTokenKey;

    @Value("${digitalcontract.token.ttl}")
    private int contractTokenTtl;

    private ObjectMapper objectMapper;

    public <U extends AbstractUser> String generateEmailToken(U user, int expiredInDays) throws IOException {
        UserEmailToken userEmailToken = new UserEmailToken(user.getId(), user.getEmail());

        return getJwtToken(expiredInDays, userEmailToken, emailTokenSecretKey);
    }

    public String generateApplicationIntentToken(
            PropertyApplication application,
            int expiredInDays
    ) throws IOException {
        ApplicationIntentToken applicationIntentToken = new ApplicationIntentToken(application.getId());

        return getJwtToken(expiredInDays, applicationIntentToken, applicationIntentSecretKey);
    }

    public UserEmailToken validateEmailToken(String token) {
        SignerVerifier verifier = new MacSigner(emailTokenSecretKey);
        UserEmailToken userEmailToken = new UserEmailToken();
        userEmailToken = verifyToken(token, verifier, userEmailToken);

        return userEmailToken;
    }

    public ApplicationIntentToken validateApplicationIntentToken(String token) {
        SignerVerifier verifier = new MacSigner(applicationIntentSecretKey);
        ApplicationIntentToken applicationIntentToken = new ApplicationIntentToken();
        applicationIntentToken = verifyToken(token, verifier, applicationIntentToken);

        return applicationIntentToken;
    }

    public String generateBrandingUrlToken(Long customerId) throws IOException {
        BrandingUrlToken brandingUrlToken = new BrandingUrlToken(customerId);

        return getJwtToken(brandingTokenSecretTtl, brandingUrlToken, brandingTokenSecretKey);
    }

    public String generatePreviewBrandingUrlToken(
            LandlordCustomer customer,
            LandlordCustomerBrandingTheme theme,
            S3File logo, List<CustomQuestionBean> globalQuestions
    ) throws IOException {
        PreviewBrandingUrlToken brandingUrlToken = new PreviewBrandingUrlToken(customer, theme, logo, globalQuestions);

        return getJwtToken(brandingTokenSecretTtl, brandingUrlToken, brandingTokenSecretKey);
    }

    public String generateDigitalContractSignToken(Long signerId) throws JsonProcessingException {
        DigitalContractSignToken signToken = new DigitalContractSignToken(signerId);

        return getJwtToken(contractTokenTtl, signToken, contractTokenKey);
    }

    public String generateGuestUserToken(PropertyApplication application) throws IOException {
        if (application.getUserProfile().getType() != PropertySearcherUserProfileType.GUEST) {
            throw new ApiValidationException(USER_PROFILE_NOT_GUEST_L);
        }

        GuestUserToken guestUserToken = new GuestUserToken(application.getId());

        return getJwtToken(brandingTokenSecretTtl, guestUserToken, contractTokenKey);
    }

    public String generateApplyGuestUserToken(UUID applyID, String email, Long propertyId) throws IOException {
        ApplyGuestUserToken applyGuestUserToken = new ApplyGuestUserToken(applyID, email, propertyId);

        return getJwtToken(brandingTokenSecretTtl, applyGuestUserToken, contractTokenKey);
    }

    public ApplyGuestUserToken validateApplyGuestUserToken(String token) {
        SignerVerifier verifier = new MacSigner(contractTokenKey);
        ApplyGuestUserToken applyGuestUserToken = new ApplyGuestUserToken();

        return verifyToken(token, verifier, applyGuestUserToken);
    }

    public BrandingUrlToken validateBrandingUrlToken(String token) {
        SignerVerifier verifier = new MacSigner(brandingTokenSecretKey);
        BrandingUrlToken brandingUrlToken = new BrandingUrlToken();

        return verifyToken(token, verifier, brandingUrlToken);
    }

    public PreviewBrandingUrlToken validatePreviewBrandingUrlToken(String token) {
        SignerVerifier verifier = new MacSigner(brandingTokenSecretKey);
        PreviewBrandingUrlToken brandingUrlToken = new PreviewBrandingUrlToken();

        return verifyToken(token, verifier, brandingUrlToken);
    }

    public DigitalContractSignToken validateContractSignToken(String token) {
        SignerVerifier verifier = new MacSigner(contractTokenKey);
        DigitalContractSignToken contractSignToken = new DigitalContractSignToken();

        return verifyToken(token, verifier, contractSignToken);
    }

    public GuestUserToken validateGuestUserToken(String token) {
        SignerVerifier verifier = new MacSigner(contractTokenKey);
        GuestUserToken guestUserToken = new GuestUserToken();

        return verifyToken(token, verifier, guestUserToken);
    }

    private <T extends AbstractToken> String getJwtToken(
            int expiredInDays,
            T token,
            String secretKey
    ) throws JsonProcessingException {
        token.setExpired(DateTime.now().plusDays(expiredInDays).getMillis());

        String tokenString = getObjectMapper().writeValueAsString(token);
        SignerVerifier signer = new MacSigner(secretKey);
        Jwt jwt = JwtHelper.encode(tokenString, signer);

        return jwt.getEncoded();
    }

    private <T extends AbstractToken> T verifyToken(String token, SignerVerifier verifier, T returnValue) {
        try {
            Jwt jwt = JwtHelper.decodeAndVerify(token, verifier);

            if (jwt.getClaims() == null) {
                throw new TokenValidationException(ERROR_PARSING_JWT_TOKEN);
            }

            returnValue = (T) getObjectMapper().readValue(jwt.getClaims(), returnValue.getClass());
            if (returnValue.getExpired() < DateTime.now().getMillis()) {
                throw new TokenValidationException(TOKEN_IS_EXPIRED);
            }
            return returnValue;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new TokenValidationException(ERROR_PARSING_JWT_TOKEN);
        }
    }

    private ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        }
        return objectMapper;
    }
}
