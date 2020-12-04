package de.immomio.controller.filer.landlord.file.upload;

import de.immomio.cloud.amazon.s3.LandlordS3FileManager;
import de.immomio.cloud.service.s3.AbstractImageService;
import de.immomio.common.upload.FileStoreObject;
import de.immomio.controller.AbstractFilerController;
import de.immomio.data.base.type.common.FileType;
import de.immomio.utils.ImageStorageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 */
@Controller
@Slf4j
public class UploadController extends AbstractFilerController<LandlordS3FileManager> {

    private static final String FILE_PARAM = "file";

    private static final String FILE_TYPE_PARAM = "fileType";

    private static final String ENCRYPTED_PARAM = "encrypted";

    private static final String ROTATE_PARAM = "rotate";

    private static final String MEDIA_TYPE_JSON = "application/json; charset=utf-8";

    @Autowired
    private LandlordS3FileManager s3FileManager;

    @Autowired
    private AbstractImageService imageService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MEDIA_TYPE_JSON)
    public ResponseEntity<FileStoreObject> upload(@RequestParam(FILE_PARAM) MultipartFile multipartFile,
            @RequestParam(value = FILE_TYPE_PARAM) FileType fileType,
            @RequestParam(value = ENCRYPTED_PARAM, required = false) boolean encrypted) {

        return ResponseEntity.ok(ImageStorageUtils.uploadFile(multipartFile, fileType, encrypted,
                getEncryptionKeyByFileType(s3FileManager, fileType), s3FileManager, imageService));
    }

    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST, produces = MEDIA_TYPE_JSON)
    public ResponseEntity<?> upload(@RequestParam(FILE_PARAM) MultipartFile multipartFile,
            @RequestParam(value = ROTATE_PARAM, required = false) Double rotate) {

        return ImageStorageUtils.uploadImage(multipartFile, s3FileManager, imageService, rotate);
    }

}
