package de.immomio.controller.filer.landlord.file.application.customdata;

import de.immomio.controller.BaseController;
import de.immomio.data.base.type.common.FileType;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataRequestBean;
import de.immomio.landlord.service.application.customData.LandlordPropertyApplicationCustomDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;

@Slf4j
@Controller
@RequestMapping(value = "/application/customData/excel")
public class PropertyApplicationCustomDataReportController extends BaseController {

    private static final String XLSX_CUSTOM_DATA_NOT_CREATED = "XLSX_CUSTOM_DATA_NOT_CREATED";

    private final LandlordPropertyApplicationCustomDataService customDataService;

    @Autowired
    public PropertyApplicationCustomDataReportController(LandlordPropertyApplicationCustomDataService customDataService) {
        this.customDataService = customDataService;
    }

    @PostMapping
    public ResponseEntity<Object> mapApplicationsToFields(@RequestBody ApplicationCustomDataRequestBean requestBean) {
        File file = customDataService.createDatafile(requestBean);
        return responseWithFile(file,
                FileType.XLSX,
                XLSX_CUSTOM_DATA_NOT_CREATED,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
