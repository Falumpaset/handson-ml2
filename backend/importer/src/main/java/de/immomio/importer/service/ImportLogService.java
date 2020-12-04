package de.immomio.importer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.immomio.data.base.type.common.ImportStatus;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.importlog.ImportLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Maik Kingma.
 */

@Slf4j
@Service
public class ImportLogService {

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    private final IdExtractor idExtractor;

    @Value("${api.url}")
    private String apiUrl;

    @Value("${api.importlog.path}")
    private String importLogPath;

    @Value("${api.importlog.incrementLogSize}")
    private String incrementLogSize;

    @Value("${api.importlog.reportError}")
    private String reportError;

    @Autowired
    public ImportLogService(RestTemplate restTemplate, ObjectMapper objectMapper, IdExtractor idExtractor) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.idExtractor = idExtractor;
    }

    public EntityModel<ImportLog> initImportLog(EntityModel<LandlordCustomer> customer) {
        ImportLog importLog = new ImportLog();
        importLog.setStatus(ImportStatus.STARTED);
        importLog.setCurrent(0);
        importLog.setTotal(0);
        return createImportLog(importLog, customer);
    }

    private EntityModel<ImportLog> createImportLog(
            ImportLog importLog,
            EntityModel<LandlordCustomer> customer
    ) {
        ObjectNode importLogRequest = objectMapper.valueToTree(importLog);
        importLogRequest.put("customer", idExtractor.getId(customer).get().getHref());

        HttpEntity<ObjectNode> httpEntity = new HttpEntity<>(importLogRequest);
        ResponseEntity<EntityModel<ImportLog>> response = restTemplate.exchange(
                apiUrl + importLogPath,
                HttpMethod.POST,
                httpEntity,
                new ParameterizedTypeReference<EntityModel<ImportLog>>() {
                });

        return response.getBody();
    }

    public void incrementLogSize(EntityModel<ImportLog> importLogResource) {
        Map<String, Long> params = new HashMap<>();
        params.put("importLog", idExtractor.extractIDFromResource(importLogResource));

        try {
            restTemplate.exchange(
                    apiUrl + importLogPath + incrementLogSize,
                    HttpMethod.POST,
                    null,
                    new ParameterizedTypeReference<EntityModel<ImportLog>>() {
                    }, params);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }


    }

    private void reportErrorOnImport(EntityModel<ImportLog> importLogResource, Exception ex) {
        Map<String, Long> params = new HashMap<>();
        params.put("importLog", idExtractor.extractIDFromResource(importLogResource));

        ObjectNode reportErrorRequest = objectMapper.valueToTree(ex);

        HttpEntity<ObjectNode> httpEntity = new HttpEntity<>(reportErrorRequest);

        try {
            restTemplate.exchange(
                    apiUrl + importLogPath + reportError,
                    HttpMethod.POST,
                    httpEntity,
                    new ParameterizedTypeReference<EntityModel<ImportLog>>() {
                    }, params);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    public void importErrorLog(EntityModel<ImportLog> importLog, Exception ex) {
        log.error("Error converting flat -> ", ex);
        reportErrorOnImport(importLog, ex);
    }
}
