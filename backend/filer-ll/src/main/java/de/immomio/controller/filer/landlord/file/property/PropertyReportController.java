package de.immomio.controller.filer.landlord.file.property;

import de.immomio.cloud.amazon.s3.LandlordS3FileManager;
import de.immomio.controller.BaseController;
import de.immomio.data.base.type.common.FileType;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.service.report.PropertyReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping(value = "/properties/pdf")
public class PropertyReportController extends BaseController {

    private final PropertyReportService propertyReportService;

    private final LandlordS3FileManager landlordS3FileManager;

    private static final String PDF_EXPOSE_NOT_CREATED = "PDF_EXPOSE_NOT_CREATED_L";

    @Autowired
    public PropertyReportController(
            PropertyReportService propertyReportService,
            LandlordS3FileManager landlordS3FileManager
    ) {
        this.propertyReportService = propertyReportService;
        this.landlordS3FileManager = landlordS3FileManager;
    }

    @PreAuthorize("#property.customer.id == principal.customer.id")
    @GetMapping(value = "/expose/{property}")
    public ResponseEntity<Object> exposeReport(@PathVariable("property") Property property) {
        return responseWithFile(propertyReportService.generatePdfExpose(property, landlordS3FileManager),
                                FileType.PDF,
                                PDF_EXPOSE_NOT_CREATED,
                                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
