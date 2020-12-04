package de.immomio.controller.filer.landlord.file.selfdisclosure;

import de.immomio.controller.BaseController;
import de.immomio.data.base.type.common.FileType;
import de.immomio.data.shared.entity.selfdisclosure.SelfDisclosureResponse;
import de.immomio.service.LandlordSelfDisclosureReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Controller
@RequestMapping(value = "/self-disclosure/pdf")
public class SelfDisclosureReportController extends BaseController {

    private static final String PDF_SELF_DISCLOSURE_NOT_CREATED = "PDF_SELF_DISCLOSURE_NOT_CREATED_L";

    private static final String DOT_PDF = ".pdf";

    private LandlordSelfDisclosureReportService selfDisclosureReportService;

    @Autowired
    public SelfDisclosureReportController(LandlordSelfDisclosureReportService selfDisclosureReportService) {
        this.selfDisclosureReportService = selfDisclosureReportService;
    }

    @PreAuthorize("#selfDisclosureResponse.property.customer.id == principal.customer.id")
    @GetMapping("/generate/{response}")
    public ResponseEntity getSelfDisclosurePdf(
            @PathVariable("response") SelfDisclosureResponse selfDisclosureResponse
    ) {
        String reportName = selfDisclosureReportService.getResponseReportName(selfDisclosureResponse);

        try (ByteArrayOutputStream outputStream = (ByteArrayOutputStream) selfDisclosureReportService.generateReport(
                selfDisclosureResponse)) {
            File reportFile = File.createTempFile(reportName, DOT_PDF);
            outputStream.writeTo(new FileOutputStream(reportFile));

            return responseWithFile(reportFile,
                                    FileType.PDF,
                                    PDF_SELF_DISCLOSURE_NOT_CREATED,
                                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return internalServerError(e.getMessage(), new ArrayList<>());
        }
    }
}
