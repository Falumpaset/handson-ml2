package de.immomio.service;

import de.immomio.cloud.amazon.s3.LandlordS3FileManager;
import de.immomio.cloud.service.LandlordImageService;
import de.immomio.cloud.service.s3.AbstractAsyncDeleteFileService;
import de.immomio.cloud.service.s3.AbstractImageService;
import de.immomio.common.amazon.s3.AbstractS3FileManager;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.shared.bean.common.S3DeleteResponse;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.data.shared.bean.common.S3FileDeleteStatus;
import de.immomio.model.repository.shared.property.PropertyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Service
public class LandlordAsyncDeleteFileService extends AbstractAsyncDeleteFileService {

    private final LandlordImageService landlordImageService;

    private final LandlordS3FileManager landlordS3FileManager;

    private final PropertyRepository propertyRepository;

    @Autowired
    public LandlordAsyncDeleteFileService(
            LandlordImageService landlordImageService,
            LandlordS3FileManager landlordS3FileManager,
            PropertyRepository propertyRepository
    ) {
        this.landlordImageService = landlordImageService;
        this.landlordS3FileManager = landlordS3FileManager;
        this.propertyRepository = propertyRepository;
    }

    @Override
    protected AbstractImageService getImageService() {
        return landlordImageService;
    }

    @Override
    protected AbstractS3FileManager getFileManager() {
        return landlordS3FileManager;
    }

    public S3DeleteResponse deleteFiles(Property property, List<S3File> files) {
        S3DeleteResponse response = new S3DeleteResponse();
        List<S3File> filteredFiles = files.stream()
                .filter(file -> property.getData().getAttachments().contains(file))
                .collect(Collectors.toList());
        List<S3FileDeleteStatus> statusList = processS3FileDeletion(filteredFiles);
        deleteFileFromDb(property, statusList);
        response.getStatusList().addAll(statusList);

        return response;
    }

    protected CompletableFuture<S3FileDeleteStatus> deleteFileInternal(S3File file) {
        return CompletableFuture.supplyAsync(() -> deleteFromS3Bucket(file));
    }

    private void deleteFileFromDb(Property property, List<S3FileDeleteStatus> statusList) {
        List<S3File> filesToDelete = statusList.stream().map(S3FileDeleteStatus::getFile).collect(Collectors.toList());
        property.getData().getAttachments().removeAll(filesToDelete);
        propertyRepository.save(property);
    }
}
