package de.immomio.controller.base;

import de.immomio.model.contract.ContractStatusApiBean;
import de.immomio.model.contract.CreateContractApiBean;
import de.immomio.service.base.contract.DigitalContractService;
import de.immomio.util.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @author Niklas Lindemann
 */

public abstract class BaseExternalContractController<DS extends DigitalContractService> {

    private final DS digitalContractService;

    public BaseExternalContractController(DS digitalContractService) {
        this.digitalContractService = digitalContractService;
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Creates a rental contract in immomio", security = {@SecurityRequirement(name = "bearer-key")})
    @ApiResponses({@ApiResponse(responseCode = "400", description = "validation failed")})
    public ResponseEntity<?> createContract(
            @RequestBody @Valid CreateContractApiBean contractBean) {
        digitalContractService.createDigitalContract(contractBean);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/upload/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Uploads a pdf/docx to a rental contract. Must be called after the /create request. Can only be called once per contract", security = {@SecurityRequirement(name = "bearer-key")})
    @ApiResponses({@ApiResponse(responseCode = "404", description = "contract process with this id not found"), @ApiResponse(responseCode = "200", description = "the file has been uploaded"), @ApiResponse(responseCode = "401", description = "there is already a file for this contract uploaded")})
    public ResponseEntity<?> uploadPdf(
            @Parameter(description = "the files as multipart (must be in correct order)") @RequestParam("file") MultipartFile[] files,
            @Parameter(description = "unique identifier of the rental contract signing process", required = true) @PathVariable("id") String id) {

        digitalContractService.uploadDigitalContract(id, files);

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/finished", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns a list of signed contracts, finished after the timestamp", security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<List<String>> getFinishedContracts(
            @Parameter(description = "the timestamp since the last request (ISO-Datetime) with timezone, URL encoded", example = "2020-02-22T13:32:14.496+02:00", required = true) @RequestParam(value = "since", required = false) String since) {
        List<String> finishedContracts = digitalContractService.getFinishedContracts(DateUtil.getUtcDateFromParam(since));
        return ResponseEntity.ok(finishedContracts);
    }

    @GetMapping(value = "/modified", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns a list of modified contracts, modified after the timestamp", security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<List<String>> getModifiedContracts(
            @Parameter(description = "the timestamp since the last request (ISO-Datetime) with timezone, URL encoded", example = "2020-02-22T13:32:14.496+02:00", required = true) @RequestParam(value = "since", required = false) String since) {
        List<String> modifiedContracts = digitalContractService.getModifiedContracts(DateUtil.getUtcDateFromParam(since));
        return ResponseEntity.ok(modifiedContracts);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "returns the informations including the current status of a given contract", security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<ContractStatusApiBean> status(
            @Parameter(description = "unique identifier of the rental contract signing process", required = true) @PathVariable("id") String id,
            HttpServletResponse response) throws IOException {
        return ResponseEntity.ok(digitalContractService.getContractInfo(id));
    }

    @GetMapping(value = "/download/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Operation(summary = "downloads a signed contract as a zip archive", security = {@SecurityRequirement(name = "bearer-key")})
    @ApiResponses({@ApiResponse(responseCode = "404", description = "contract process with this id not found"), @ApiResponse(responseCode = "423", description = "the signing process is not finished yet")})
    public ResponseEntity<?> downloadPdf(
            @Parameter(description = "unique identifier of the rental contract signing process", required = true) @PathVariable("id") String id) throws IOException {
        return digitalContractService.downloadContract(id);
    }

    @ExceptionHandler
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

}
