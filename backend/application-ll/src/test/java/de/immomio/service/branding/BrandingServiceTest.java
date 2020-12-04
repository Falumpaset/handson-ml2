package de.immomio.service.branding;

import de.immomio.constants.exceptions.TokenValidationException;
import de.immomio.data.base.type.addon.AddonType;
import de.immomio.data.landlord.bean.customer.settings.theme.LandlordCustomerBranding;
import de.immomio.data.landlord.bean.customer.settings.theme.LandlordCustomerBrandingTheme;
import de.immomio.data.landlord.bean.customer.settings.theme.LandlordCustomerBrandingThemes;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.settings.LandlordCustomerSettings;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.landlord.service.branding.BrandingService;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.model.abstractrepository.customer.CustomerRepositoryCustom;
import de.immomio.security.common.bean.BrandingUrlToken;
import de.immomio.security.common.bean.BrandingUrlTokenData;
import de.immomio.security.common.bean.PreviewBrandingUrlToken;
import de.immomio.security.service.JWTTokenService;
import de.immomio.service.AbstractTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import utils.TestHelper;

import javax.validation.ValidationException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class BrandingServiceTest extends AbstractTest {

    private static final String TOKEN = "TOKEN";

    private static final String THEME_NAME = "THEME_NAME";

    private static final String THEME_COLOR = "#ddccaa";

    private static final String BRANDING_URL_COMPARE =
            "/de/auth/register/propertyLocation?utm_source=Customer_Homepage&utm_medium=Whitelabel_Tenant_Pool&" +
                    "utm_campaign=2&token=null";

    @Mock
    private UserSecurityService userSecurityService;

    @Spy
    @InjectMocks
    private BrandingService brandingService;

    @Mock
    private JWTTokenService jwtTokenService;

    @Mock
    private CustomerRepositoryCustom<LandlordCustomer> landlordCustomerRepositoryImpl;

    private LandlordUser landlordUser;

    private LandlordCustomer landlordCustomer;

    @Before
    public void createLandlordUser() {
        landlordCustomer = TestHelper.generateLandlordCustomer(1L);
        landlordUser = TestHelper.generateLandlordUser(landlordCustomer, LandlordUsertype.COMPANYADMIN, 1L);
    }

    @Test(expected = ValidationException.class)
    public void generateBrandingUrlCustomerNotAllowedForBranding() throws Exception {
        when(userSecurityService.getPrincipalUser()).thenReturn(landlordUser);

        brandingService.validateAndGenerateBrandingUrl();
    }

    @Test(expected = ValidationException.class)
    public void generateBrandingUrlThemesNotExists() throws Exception {
        LandlordCustomer customerWithAllowedBranding =
                TestHelper.generateLandlordCustomerWithAddon(AddonType.BRANDING, 2L);
        landlordUser.setCustomer(customerWithAllowedBranding);

        when(userSecurityService.getPrincipalUser()).thenReturn(landlordUser);

        brandingService.validateAndGenerateBrandingUrl();
    }

    @Test(expected = ValidationException.class)
    public void generateBrandingUrlThemeNotFound() throws Exception {
        LandlordCustomer customerWithAllowedBranding =
                TestHelper.generateLandlordCustomerWithAddon(AddonType.BRANDING, 2L);

        LandlordCustomerSettings settings = new LandlordCustomerSettings();
        customerWithAllowedBranding.setCustomerSettings(settings);

        LandlordCustomerBrandingThemes themes = new LandlordCustomerBrandingThemes();
        themes.add(createBrandingTheme(THEME_NAME + THEME_NAME, THEME_COLOR, false));

        settings.setBrandingThemes(themes);
        landlordUser.setCustomer(customerWithAllowedBranding);

        when(userSecurityService.getPrincipalUser()).thenReturn(landlordUser);

        brandingService.validateAndGenerateBrandingUrl();
    }

    @Test
    public void generateBrandingUrl() throws Exception {
        LandlordCustomer customerWithAllowedBranding =
                TestHelper.generateLandlordCustomerWithAddon(AddonType.BRANDING, 2L);

        LandlordCustomerSettings settings = new LandlordCustomerSettings();
        customerWithAllowedBranding.setCustomerSettings(settings);

        LandlordCustomerBrandingThemes themes = new LandlordCustomerBrandingThemes();
        themes.add(createBrandingTheme(THEME_NAME, THEME_COLOR, true));

        settings.setBrandingThemes(themes);
        landlordUser.setCustomer(customerWithAllowedBranding);

        when(userSecurityService.getPrincipalUser()).thenReturn(landlordUser);

        String brandingUrl = brandingService.validateAndGenerateBrandingUrl();
        Assert.assertTrue(brandingUrl.contains(BRANDING_URL_COMPARE));
    }

    @Test
    public void validateBrandingUrlToken() {
        BrandingUrlToken brandingUrlToken = new BrandingUrlToken(landlordCustomer.getId());
        LandlordCustomer customerWithAllowedBranding =
                TestHelper.generateLandlordCustomerWithAddon(AddonType.BRANDING, 2L);

        LandlordCustomerSettings settings = new LandlordCustomerSettings();
        customerWithAllowedBranding.setCustomerSettings(settings);

        LandlordCustomerBrandingThemes themes = new LandlordCustomerBrandingThemes();
        LandlordCustomerBrandingTheme theme = createBrandingTheme(THEME_NAME, THEME_COLOR, true);
        themes.add(theme);

        settings.setBrandingThemes(themes);

        when(landlordCustomerRepositoryImpl.customFindOne(anyLong())).thenReturn(customerWithAllowedBranding);
        when(jwtTokenService.validateBrandingUrlToken(anyString())).thenReturn(brandingUrlToken);

        BrandingUrlTokenData validatedTokenData = brandingService.validateAndExtractBrandingUrlToken(TOKEN);

        assertThemeAndLogo(theme, validatedTokenData, settings.getLogo());
    }

    @Test(expected = TokenValidationException.class)
    public void validateBrandingUrlTokenWithInvalidToken() {
        when(jwtTokenService.validateBrandingUrlToken(anyString())).thenThrow(new TokenValidationException());

        brandingService.validateAndExtractBrandingUrlToken(TOKEN);
    }

    @Test(expected = TokenValidationException.class)
    public void validatePreviewBrandingUrlTokenWithInvalidToken() {
        when(jwtTokenService.validatePreviewBrandingUrlToken(anyString())).thenThrow(new TokenValidationException());

        brandingService.validateAndExtractPreviewBrandingUrlToken(TOKEN);
    }

    @Test(expected = ValidationException.class)
    public void generatePreviewBrandingUrlCustomerNotAllowedForBranding() throws Exception {
        when(userSecurityService.getPrincipalUser()).thenReturn(landlordUser);

        brandingService.generatePreviewBrandingUrl(new LandlordCustomerBranding());
    }

    @Test
    public void validatePreviewBrandingUrlToken() {
        LandlordCustomer customerWithAllowedBranding =
                TestHelper.generateLandlordCustomerWithAddon(AddonType.BRANDING, 2L);

        LandlordCustomerSettings settings = new LandlordCustomerSettings();
        customerWithAllowedBranding.setCustomerSettings(settings);

        LandlordCustomerBrandingThemes themes = new LandlordCustomerBrandingThemes();
        LandlordCustomerBrandingTheme theme = createBrandingTheme(THEME_NAME, THEME_COLOR, true);
        themes.add(theme);

        S3File logo = TestHelper.createS3File("test.png");
        PreviewBrandingUrlToken previewBrandingUrlToken = new PreviewBrandingUrlToken(landlordCustomer, theme, logo, Collections.emptyList());
        settings.setBrandingThemes(themes);

        when(landlordCustomerRepositoryImpl.customFindOne(anyLong())).thenReturn(customerWithAllowedBranding);
        when(jwtTokenService.validatePreviewBrandingUrlToken(anyString())).thenReturn(previewBrandingUrlToken);

        PreviewBrandingUrlToken validatedToken = brandingService.validateAndExtractPreviewBrandingUrlToken(TOKEN);

        assertThemeAndLogo(theme, validatedToken.getData(), logo);
    }

    private LandlordCustomerBrandingTheme createBrandingTheme(String themeName, String color, boolean active) {
        LandlordCustomerBrandingTheme theme = new LandlordCustomerBrandingTheme();
        theme.setName(themeName);
        theme.setBackgroundColor(color);
        theme.setButtonTextColor(color);
        theme.setCardBackgroundColor(color);
        theme.setPrimaryColor(color);
        theme.setPrimaryTextColor(color);
        theme.setSecondaryColor(color);
        theme.setSecondaryTextColor(color);
        theme.setActive(active);

        return theme;
    }

    private void assertThemeAndLogo(LandlordCustomerBrandingTheme theme, BrandingUrlTokenData tokenData, S3File logo) {
        assertEquals(theme.getName(), tokenData.getName());
        assertEquals(theme.getPrimaryColor(), tokenData.getPrimaryColor());
        assertEquals(theme.getSecondaryColor(), tokenData.getSecondaryColor());
        assertEquals(theme.getPrimaryTextColor(), tokenData.getPrimaryTextColor());
        assertEquals(theme.getSecondaryTextColor(), tokenData.getSecondaryTextColor());
        assertEquals(theme.getButtonTextColor(), tokenData.getButtonTextColor());
        assertEquals(theme.getBackgroundColor(), tokenData.getBackgroundColor());
        assertEquals(theme.getCardBackgroundColor(), tokenData.getCardBackgroundColor());
        assertEquals(logo, tokenData.getLogo());
    }

}
