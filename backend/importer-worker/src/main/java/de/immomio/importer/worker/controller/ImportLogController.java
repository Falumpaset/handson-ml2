package de.immomio.importer.worker.controller;

import de.immomio.data.base.type.common.ImportStatus;
import de.immomio.data.landlord.entity.importlog.ImportLog;
import de.immomio.importer.worker.service.ImportLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Maik Kingma.
 */

@Slf4j
@RepositoryRestController
@RequestMapping(value = "/ll-importLogs")
public class ImportLogController {

    private final ImportLogService importLogService;

    @Autowired
    public ImportLogController(ImportLogService importLogService) {
        this.importLogService = importLogService;
    }

    @PostMapping(value = "/incrementLogSize/{importLog}")
    public ResponseEntity<Object> incrementLogSize(@PathVariable("importLog") ImportLog importLog) {
        importLog.setTotal(importLog.getTotal() + 1);
        return updateImportLog(importLog);
    }

    @PostMapping(value = "/reportError/{importLog}")
    public ResponseEntity<Object> reportError(
            @PathVariable("importLog") ImportLog importLog,
            @RequestBody Exception ex) {
        importLog.setStatusMessage(ex.getMessage());
        importLog.setStatus(ImportStatus.ERROR);
        return updateImportLog(importLog);
    }

    private ResponseEntity<Object> updateImportLog(ImportLog importLog) {
        try {
            importLogService.save(importLog);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
