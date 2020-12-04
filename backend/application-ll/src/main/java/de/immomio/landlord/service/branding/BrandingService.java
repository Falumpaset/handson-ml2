package de.immomio.landlord.service.branding;

import de.immomio.config.DefaultBrandingThemeConfiguration;
import de.immomio.data.landlord.bean.customer.settings.theme.LandlordCustomerBranding;
import de.immomio.data.landlord.bean.customer.settings.theme.LandlordCustomerBrandingTheme;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.settings.LandlordCustomerSettings;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.shared.bean.customQuestion.CustomQuestionBean;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.model.abstractrepository.customer.CustomerRepositoryCustom;
import de.immomio.security.common.bean.BrandingUrlToken;
import de.immomio.security.common.bean.BrandingUrlTokenData;
import de.immomio.security.common.bean.PreviewBrandingUrlToken;
import de.immomio.security.service.JWTTokenService;
import de.immomio.service.customQuestion.CustomQuestionConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static de.immomio.constants.Constants.BASE_BRANDING_URL;

@Slf4j
@Service
public class BrandingService {

    private static final String VALIDATION_BRANDING_NOT_ALLOWED = "VALIDATION_BRANDING_NOT_ALLOWED_L";

    private static final String VALIDATION_BRANDING_THEME_NOT_FOUND = "VALIDATION_BRANDING_THEME_NOT_FOUND_L";

    private static final String VALIDATION_BRANDING_THEMES_NOT_EXISTS = "VALIDATION_BRANDING_THEMES_NOT_EXISTS_L";

    private static final String BASE_PREVIEW_BRANDING_URL = BASE_BRANDING_URL + "&preview=true";

    private final JWTTokenService jwtTokenService;

    private final UserSecurityService userSecurityService;

    private final CustomerRepositoryCustom<LandlordCustomer> landlordCustomerRepositoryImpl;

    private final DefaultBrandingThemeConfiguration defaultBrandingThemeConfiguration;

    private final CustomQuestionConverter customQuestionConverter;

    @Value("${base.propertysearcher.url}")
    private String tenantUrl;

    @Autowired
    public BrandingService(
            JWTTokenService jwtTokenService,
            UserSecurityService userSecurityService,
            CustomerRepositoryCustom<LandlordCustomer> landlordCustomerRepositoryImpl,
            DefaultBrandingThemeConfiguration defaultBrandingThemeConfiguration,
            CustomQuestionConverter customQuestionConverter) {
        this.jwtTokenService = jwtTokenService;
        this.userSecurityService = userSecurityService;
        this.landlordCustomerRepositoryImpl = landlordCustomerRepositoryImpl;
        this.defaultBrandingThemeConfiguration = defaultBrandingThemeConfiguration;
        this.customQuestionConverter = customQuestionConverter;
    }

    public String validateAndGenerateBrandingUrl() throws Exception {
        LandlordUser landlordUser = userSecurityService.getPrincipalUser();
        LandlordCustomer customer = landlordUser.getCustomer();

        if (!customer.isBrandingAllowed()) {
            throw new ValidationException(VALIDATION_BRANDING_NOT_ALLOWED);
        }

        return generateBrandingUrl(customer.getId());
    }

    public String generateBrandingUrl(Long customerId) throws IOException {
        String token = jwtTokenService.generateBrandingUrlToken(customerId);

        return String.format(BASE_BRANDING_URL, tenantUrl, customerId, token);
    }

    public String generatePreviewBrandingUrl(LandlordCustomerBranding branding) throws Exception {
        LandlordUser landlordUser = userSecurityService.getPrincipalUser();
        LandlordCustomer customer = landlordUser.getCustomer();

        if (!customer.isBrandingAllowed()) {
            throw new ValidationException(VALIDATION_BRANDING_NOT_ALLOWED);
        }

        LandlordCustomerBrandingTheme theme = branding.getTheme();
        List<CustomQuestionBean> globalQuestions = customer.getGlobalQuestions().stream().map(customQuestionConverter::getCustomQuestionBean).collect(Collectors.toList());
        String token = jwtTokenService.generatePreviewBrandingUrlToken(customer, theme, branding.getLogo(), globalQuestions);

        return String.format(BASE_PREVIEW_BRANDING_URL, tenantUrl, customer.getId(), token);
    }

    public BrandingUrlTokenData validateAndExtractBrandingUrlToken(String token) {
        BrandingUrlToken brandingUrlToken = jwtTokenService.validateBrandingUrlToken(token);
        LandlordCustomer customer = landlordCustomerRepositoryImpl.customFindOne(brandingUrlToken.getCustomerId());

        if (!customer.isBrandingAllowed()) {
            throw new ValidationException(VALIDATION_BRANDING_NOT_ALLOWED);
        }

        LandlordCustomerSettings settings = customer.getCustomerSettings();

        Optional<LandlordCustomerBrandingTheme> activeBrandingTheme = getActiveBrandingTheme(settings);

        LandlordCustomerBrandingTheme brandingTheme = activeBrandingTheme.orElse(defaultBrandingThemeConfiguration);

        List<CustomQuestionBean> globalQuestions = customer.getGlobalQuestions().stream().map(customQuestionConverter::getCustomQuestionBean).collect(Collectors.toList());
        return new BrandingUrlTokenData(customer, brandingTheme, settings.getLogo(), settings.getLogoRedirectUrl(), globalQuestions);
    }

    public PreviewBrandingUrlToken validateAndExtractPreviewBrandingUrlToken(String token) {
        PreviewBrandingUrlToken previewBrandingUrlToken = jwtTokenService.validatePreviewBrandingUrlToken(token);
        Long customerId = previewBrandingUrlToken.getData().getCustomerId();
        LandlordCustomer customer = landlordCustomerRepositoryImpl.customFindOne(customerId);

        if (!customer.isBrandingAllowed()) {
            throw new ValidationException(VALIDATION_BRANDING_NOT_ALLOWED);
        }

        return previewBrandingUrlToken;
    }

    private Optional<LandlordCustomerBrandingTheme> getActiveBrandingTheme(LandlordCustomerSettings settings) {
        return settings.getBrandingThemes().stream().filter(LandlordCustomerBrandingTheme::isActive).findFirst();
    }

}
