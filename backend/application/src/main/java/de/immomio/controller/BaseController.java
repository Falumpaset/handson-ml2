package de.immomio.controller;

import de.immomio.common.file.DeleteOnCloseFileInputStream;
import de.immomio.data.base.type.common.FileType;
import de.immomio.web.exceptions.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class BaseController {

    protected static final String CONTENT_DISPOSITION_HEADER = "Content-Disposition";

    protected static final String ATTACHMENT_NAME_PREFIX = "attachment; filename=";

    protected ResponseEntity<Object> badRequest(BindingResult result) {
        return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
    }

    protected ResponseEntity<Object> internalServerError(String errorMessage, List<String> errors) {
        ApiException exception = new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, errors);

        return new ResponseEntity<>(exception, new HttpHeaders(), exception.getStatus());
    }

    protected ResponseEntity<Object> badRequest(String errorMessage) {
        ApiException exception = new ApiException(HttpStatus.BAD_REQUEST, errorMessage, new ArrayList<>());

        return new ResponseEntity<>(exception, new HttpHeaders(), exception.getStatus());
    }

    protected ResponseEntity<Object> badRequest(String errorMessage, List<String> errors) {
        ApiException exception = new ApiException(HttpStatus.BAD_REQUEST, errorMessage, errors);

        return new ResponseEntity<>(exception, new HttpHeaders(), exception.getStatus());
    }

    protected ResponseEntity<Object> responseWithFile(
            File file,
            FileType fileType,
            String errorMessage,
            HttpStatus errorStatus
    ) {
        if (file != null) {
            long fileLength = file.length();
            try (InputStream inputStream = new DeleteOnCloseFileInputStream(file, true)) {
                String attachmentName = ATTACHMENT_NAME_PREFIX + file.getName();
                switch (fileType) {
                    case PDF:
                        return ResponseEntity.ok()
                                .header(CONTENT_DISPOSITION_HEADER, attachmentName)
                                .contentLength(fileLength)
                                .contentType(MediaType.APPLICATION_PDF)
                                .body(new InputStreamResource(inputStream));
                    case ZIP:
                    default:
                        return ResponseEntity.ok()
                                .header(CONTENT_DISPOSITION_HEADER, attachmentName)
                                .contentLength(fileLength)
                                .body(new InputStreamResource(inputStream));
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);

                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(errorMessage, errorStatus);
        }
    }

    protected ResponseEntity<Object> createFileToResponse(
            byte[] fileContent,
            HttpServletResponse response,
            String fileName,
            String errorMessage,
            String contentType,
            HttpStatus errorStatus) throws IOException {
        if (fileContent != null) {
            OutputStream outputResult = response.getOutputStream();

            response.setContentType(contentType);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + fileName + "\"");

            outputResult.write(fileContent);
            outputResult.flush();
            outputResult.close();

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(errorMessage, errorStatus);
        }
    }
}
