package de.immomio.cloud.amazon.s3;

import com.amazonaws.services.s3.AmazonS3;
import de.immomio.common.amazon.s3.AbstractS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 * <p>
 * Create a public bucket: { "Version": "2008-10-17", "Id": "Policy1410846366931", "Statement": [ { "Sid":
 * "Stmt1410846362554", "Effect": "Allow", "Principal": { "AWS": "*" }, "Action": [ "s3:DeleteObject", "s3:GetObject",
 * "s3:PutObject" ], "Resource": "arn:aws:s3:::whibs/*" } ] }
 */
@Service
public class LandlordS3 extends AbstractS3 {

    @Value("${s3.endpoint}")
    private String endpoint;

    @Autowired
    private LandlordS3Config s3Config;

    @Override
    public String getEndpoint() {
        return this.endpoint;
    }

    @Override
    public AmazonS3 getClient() {
        return s3Config.getClient();
    }

}
