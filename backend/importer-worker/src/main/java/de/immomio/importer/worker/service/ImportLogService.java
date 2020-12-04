package de.immomio.importer.worker.service;

import de.immomio.data.landlord.entity.importlog.ImportLog;
import de.immomio.model.repository.service.landlord.customer.importlog.ImportLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Maik Kingma.
 */

@Slf4j
@Service
public class ImportLogService {

    private final ImportLogRepository importLogRepository;

    @Autowired
    public ImportLogService(ImportLogRepository importLogRepository) {
        this.importLogRepository = importLogRepository;
    }

    public void save(ImportLog importLog) {
        importLogRepository.save(importLog);
    }

    public void reduceTotal(long id, String message) {
        findById(id).ifPresentOrElse(importLog -> {
                    importLog.reduceSize();
                    importLog.extendErrorMessage("REDUCING THE TOTAL OF IMPORTS DUE TO: " + message);
                    save(importLog);
                },
                () -> log.warn("ImportLog with ID {} not found", id)
        );
    }

    private Optional<ImportLog> findById(long importLogId) {
        return importLogRepository.findById(importLogId);
    }
}
