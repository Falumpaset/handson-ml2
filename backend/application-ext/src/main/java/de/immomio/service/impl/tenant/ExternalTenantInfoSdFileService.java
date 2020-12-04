package de.immomio.service.impl.tenant;

import de.immomio.cloud.amazon.s3.LandlordS3FileManager;
import de.immomio.common.file.FileUtilities;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.data.shared.entity.selfdisclosure.SelfDisclosureResponse;
import de.immomio.data.shared.entity.selfdisclosure.json.SelfDisclosureAnswers;
import de.immomio.data.shared.entity.selfdisclosure.json.SelfDisclosureQuestionAnswer;
import de.immomio.data.shared.entity.selfdisclosure.json.SelfDisclosureResponseData;
import de.immomio.data.shared.entity.selfdisclosure.json.SelfDisclosureSubQuestionAnswer;
import de.immomio.utils.FileStorageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Niklas Lindemann
 */
@Service
public class ExternalTenantInfoSdFileService {
    private LandlordS3FileManager fileManager;

    @Autowired
    public ExternalTenantInfoSdFileService(LandlordS3FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public Optional<ByteArrayOutputStream> zipFilesToResponse(SelfDisclosureResponse selfDisclosureResponse) throws IOException {
        List<S3File> documents = getTotalDocuments(selfDisclosureResponse.getData());
        if (documents.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(s3FilesToZip(documents));
    }

    public Optional<ByteArrayOutputStream> zipFilesToResponse(PropertySearcherUserProfile userProfile) throws IOException {
        List<S3File> documents = userProfile.getData().getAttachments();
        if (documents.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(s3FilesToZip(documents));
    }

    private ByteArrayOutputStream s3FilesToZip(List<S3File> documents) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(bos);
        for (S3File document : documents) {
            File file = FileStorageUtils.downloadFile(document.getUrl(), fileManager, false, null);
            if (file == null) {
                continue;
            }
            ZipEntry zipEntry = new ZipEntry(document.getFilename());
            zipOut.putNextEntry(zipEntry);
            zipOut.write(Files.readAllBytes(file.toPath()));
            FileUtilities.forceDelete(file);
        }
        zipOut.close();
        return bos;
    }

    private List<S3File> getTotalDocuments(SelfDisclosureResponseData response) {
        SelfDisclosureAnswers selfDisclosureAnswers = response.getAnswers();
        List<S3File> uploadedDocuments = selfDisclosureAnswers.getUploadedDocuments();
        List<S3File> documents = new ArrayList<>(uploadedDocuments);

        List<SelfDisclosureQuestionAnswer> questions = selfDisclosureAnswers.getQuestions();

        if (questions != null) {
            questions.forEach(selfDisclosureQuestionAnswer -> {
                if (selfDisclosureQuestionAnswer.getUpload() != null) {
                    documents.add(selfDisclosureQuestionAnswer.getUpload());
                }

                List<SelfDisclosureSubQuestionAnswer> subQuestions = selfDisclosureQuestionAnswer.getSubQuestions();
                if (subQuestions != null) {
                    subQuestions.stream()
                            .filter(item -> item.getUpload() != null)
                            .forEach(subQuestionAnswer -> documents.add(subQuestionAnswer.getUpload()));

                }
            });
        }
        return documents;

    }
}
