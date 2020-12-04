package de.immomio.common.amazon.s3.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;

public abstract class AbstractS3Config {

    @Value("${s3.access.key}")
    private String awsId;

    @Value("${s3.secret.key}")
    private String awsKey;

    @Value("${s3.region}")
    private String region;

    public AmazonS3 getClient() {

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsId, awsKey);

        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}
