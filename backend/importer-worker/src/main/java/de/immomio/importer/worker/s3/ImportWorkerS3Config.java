package de.immomio.importer.worker.s3;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import de.immomio.common.amazon.s3.config.AbstractS3Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ImportWorkerS3Config extends AbstractS3Config {

    @Value("${s3.token-template.access.key}")
    private String awsTokenTemplateAccess;

    @Value("${s3.token-template.secret.key}")
    private String awsTokenTemplateSecret;

    public AWSCredentialsProvider getTemporalTokenClient() {

        return new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(awsTokenTemplateAccess, awsTokenTemplateSecret));
    }
}
