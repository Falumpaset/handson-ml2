package de.immomio.importer.worker.service;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.securitytoken.model.GetSessionTokenRequest;
import com.amazonaws.services.securitytoken.model.GetSessionTokenResult;
import de.immomio.importer.worker.s3.ImportWorkerS3Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class S3TemporalTokenService {

    private ImportWorkerS3Config config;

    @Value("${s3.region}")
    private String region;

    @Value("${s3.token-template.duration}")
    private int tokenDuration;

    @Autowired
    public S3TemporalTokenService(ImportWorkerS3Config config) {
        this.config = config;
    }

    public Credentials getTemporalToken() {
        try {
            AWSCredentialsProvider client = config.getTemporalTokenClient();
            AWSSecurityTokenService securityTokenService = AWSSecurityTokenServiceClientBuilder
                    .standard()
                    .withRegion(region)
                    .withCredentials(client)
                    .build();
            GetSessionTokenRequest request = new GetSessionTokenRequest();
            request.setDurationSeconds(tokenDuration);
            GetSessionTokenResult result = securityTokenService.getSessionToken(request);

            return result.getCredentials();
        } catch (Exception e) {
            log.error("Failed to generate Temporal Access Token", e);
            return null;
        }
    }
}
