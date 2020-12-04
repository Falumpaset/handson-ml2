package de.immomio.cloud.service;

import de.immomio.cloud.amazon.s3.LandlordS3FileManager;
import de.immomio.cloud.service.s3.AbstractImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LandlordImageService extends AbstractImageService<LandlordS3FileManager> {

    @Autowired
    private LandlordS3FileManager landlordS3FileManager;

    @Override
    public LandlordS3FileManager getS3FileManager() {
        return landlordS3FileManager;
    }
}
