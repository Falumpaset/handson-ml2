package de.immomio.service.landlord.selfdisclosure;

import de.immomio.common.amazon.s3.AbstractS3FileManager;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.common.file.FileUtilities;
import de.immomio.utils.FileStorageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PdfMergerService {

    public PDFMergerUtility initializeMergeUtility(OutputStream outputStream) {
        PDFMergerUtility mergerUtility = new PDFMergerUtility();

        mergerUtility.setDestinationStream(outputStream);
        ByteArrayOutputStream arrayOutputStream = (ByteArrayOutputStream) outputStream;
        InputStream inputStream = new ByteArrayInputStream(arrayOutputStream.toByteArray());
        mergerUtility.addSource(inputStream);

        return mergerUtility;
    }

    public void appendDocuments(List<S3File> s3Files, OutputStream outputStream, AbstractS3FileManager fileManager) {
        PDFMergerUtility mergerUtility = initializeMergeUtility(outputStream);
        List<File> downloaded = new ArrayList<>();

        try {
            s3Files.forEach(s3File -> {
                try {
                    File file = FileStorageUtils.downloadFile(s3File.getUrl(), fileManager, false, null);
                    downloaded.add(file);
                    mergerUtility.addSource(file);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            });
            mergerUtility.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            downloaded.forEach(FileUtilities::forceDelete);
        }
    }

}
