package de.immomio.controller.base;

import de.immomio.controller.BaseController;
import de.immomio.model.selfdisclosure.SdResponseApiBean;
import de.immomio.service.base.tenant.ExternalTenantInfoService;
import de.immomio.util.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author Niklas Lindemann
 */
@Slf4j
public abstract class BaseExternalTenantController<TS extends ExternalTenantInfoService> extends BaseController {
    public static final String DOCUMENTS_ZIP = "documents.zip";
    public static final String ZIP_CREATION_ERROR = "ZIP_CREATION_ERROR_L";
    public static final String APPLICATION_ZIP = "application/zip";
    private TS tenantInfoService;

    public BaseExternalTenantController(TS tenantInfoService) {
        this.tenantInfoService = tenantInfoService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Return tenant info by the tenant id ", security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<SdResponseApiBean> getTenantInfo(
            @Parameter(description = "the tenant id", required = true) @PathVariable("id") Long id) throws IOException {
        return ResponseEntity.ok(tenantInfoService.getTenantInfo(id));
    }

    @GetMapping(value = "/{id}/files", produces = APPLICATION_ZIP)
    @Operation(summary = "Return files of tenant info by the tenant id in a zip file", security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<Object> getTenantInfoFiles(@Parameter(description = "the tenant id", required = true) @PathVariable("id") String id,
                                                     HttpServletResponse response) throws IOException {
        byte[] zipFileContent = tenantInfoService.getZipFileContent(Long.valueOf(id));
        if (zipFileContent.length == 0) {
            return ResponseEntity.notFound().build();
        }

        return createFileToResponse(
                zipFileContent,
                response,
                DOCUMENTS_ZIP,
                ZIP_CREATION_ERROR,
                APPLICATION_ZIP,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @GetMapping(value = "/created", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns a list of ids with created tenants since the passed timestamp ", security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<List<Long>> getModifiedProperties(
            @Parameter(description = "the timestamp since the last request (ISO-Datetime) with timezone URL encoded. If not set, all ids will be returned", example = "2020-02-22T13:32:14.496+02:00")
            @RequestParam(value = "since", required = false) String since) {
        Date from = DateUtil.getUtcDateFromParam(since);
        List<Long> createdIds = tenantInfoService.getCreatedTenantIdsSince(from);
        return ResponseEntity.ok(createdIds);
    }
}
