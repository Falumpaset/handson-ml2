package de.immomio.mail.configurator;

import de.immomio.mail.ModelParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author Fabian Beck
 */

@Slf4j
@Service
public class MailLogoConfigurator {
    @Value("${customer.default.logo}")
    protected String logo;

    public void applyDefaultLogo(Map<String, Object> data) {
        data.put(ModelParams.MODEL_LOGO, logo);
    }

    public void applyBrandingLogoIfExist(Map<String, Object> data) {
        String logoToApply = logo;

        if (data.containsKey(ModelParams.MODEL_ALLOW_BRANDING) && Boolean.parseBoolean(String.valueOf(data.get(
                ModelParams.MODEL_ALLOW_BRANDING))) && data.containsKey(ModelParams.MODEL_BRANDING_LOGO)) {
            String brandingLogo = String.valueOf(data.get(ModelParams.MODEL_BRANDING_LOGO));
            if (! StringUtils.isEmpty(brandingLogo)) {
                logoToApply = brandingLogo;
            }
        }
        applyLogo(data, logoToApply);
    }

    private void applyLogo(Map<String, Object> data, String logo) {
        data.put(ModelParams.MODEL_LOGO, logo);
    }
}
