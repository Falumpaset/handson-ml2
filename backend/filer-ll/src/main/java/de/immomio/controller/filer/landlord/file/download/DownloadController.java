package de.immomio.controller.filer.landlord.file.download;

import de.immomio.cloud.amazon.s3.LandlordS3FileManager;
import de.immomio.controller.AbstractFilerController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 */
@Controller
public class DownloadController extends AbstractFilerController<LandlordS3FileManager> {

    private static final String FILE_NAME_PARAM = "filename";

    private static final String ENCRYPTED_PARAM = "encrypted";

    @Autowired
    private LandlordS3FileManager landlordS3FileManager;

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<?> download(@RequestParam(value = URL_PARAM, required = false) String url,
                                      @RequestParam(value = FILE_NAME_PARAM, required = false) String filename,
                                      @RequestParam(value = ENCRYPTED_PARAM, required = false) boolean encrypted,
                                      HttpServletRequest request) throws IOException {
        return download(landlordS3FileManager, url, filename, encrypted, request);
    }

    @RequestMapping(value = "/download/{filename:.+}", method = RequestMethod.GET)
    public ResponseEntity<?> downloadWithFilename(@RequestParam(value = URL_PARAM, required = false) String url,
                                                  @PathVariable(value = FILE_NAME_PARAM) String filename,
                                                  @RequestParam(value = ENCRYPTED_PARAM, required = false)
                                                          boolean encrypted,
                                                  HttpServletRequest request) throws IOException {
        return download(url, filename, encrypted, request);
    }

}
