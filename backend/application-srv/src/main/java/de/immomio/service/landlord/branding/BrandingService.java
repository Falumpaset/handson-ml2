package de.immomio.service.landlord.branding;

import de.immomio.security.service.JWTTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static de.immomio.constants.Constants.BASE_BRANDING_URL;

@Slf4j
@Service
public class BrandingService {

    @Value("${base.propertysearcher.url}")
    private String tenantUrl;

    private final JWTTokenService jwtTokenService;

    @Autowired
    public BrandingService(JWTTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    public String generateBrandingUrl(Long customerId) throws IOException {
        String token = jwtTokenService.generateBrandingUrlToken(customerId);

        return String.format(BASE_BRANDING_URL, tenantUrl, customerId, token);
    }

}
