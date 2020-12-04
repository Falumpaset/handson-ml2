package de.immomio.cloud.service.s3;

import de.immomio.common.amazon.s3.AbstractS3FileManager;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.data.shared.bean.common.S3FileDeleteStatus;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */

@Slf4j
public abstract class AbstractAsyncDeleteFileService
        <AIS extends AbstractImageService, ASM extends AbstractS3FileManager> {

    protected abstract AIS getImageService();

    protected abstract ASM getFileManager();

    protected abstract CompletableFuture<S3FileDeleteStatus> deleteFileInternal(S3File file);

    protected S3FileDeleteStatus deleteFromS3Bucket(S3File file) {
        S3FileDeleteStatus deleteStatus = new S3FileDeleteStatus();
        deleteStatus.setSuccess(true);
        deleteStatus.setFile(file);
        try {
            getImageService().deleteSizes(file.getType(), file.getIdentifier(), file.getExtension());
            getFileManager().deleteImage(file.getType(), file.getIdentifier(), file.getExtension());
        } catch (Exception e) {
            log.error("error deleting files ", e);
            deleteStatus.setCause(e.getMessage());
            deleteStatus.setSuccess(false);

            return deleteStatus;
        }

        return deleteStatus;
    }

    protected List<S3FileDeleteStatus> processS3FileDeletion(List<S3File> files) {
        List<CompletableFuture<S3FileDeleteStatus>> deletedFileFutures = files
                .stream()
                .map(this::deleteFileInternal)
                .collect(Collectors.toList());
        CompletableFuture<Void> all = CompletableFuture.allOf(deletedFileFutures.toArray(new CompletableFuture[0]));
        CompletableFuture<List<S3FileDeleteStatus>> allFuturesContent = all
                .thenApply(v -> deletedFileFutures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));
        List<S3FileDeleteStatus> statusList = new ArrayList<>();
        try {
            statusList = allFuturesContent.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
        }
        statusList.removeIf(Objects::isNull);

        return statusList;
    }
}
