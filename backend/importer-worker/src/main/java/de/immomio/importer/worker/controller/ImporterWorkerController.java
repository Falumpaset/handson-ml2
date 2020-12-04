package de.immomio.importer.worker.controller;

import com.amazonaws.services.securitytoken.model.Credentials;
import de.immomio.data.landlord.entity.property.importer.ImportObject;
import de.immomio.importer.worker.service.AdminUserService;
import de.immomio.importer.worker.service.S3TemporalTokenService;
import de.immomio.messaging.config.QueueConfigUtils;
import de.immomio.messaging.converter.MessagingDefaultJackson2Serializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

/**
 * @author Johannes Hiemer.
 */

@Slf4j
@Controller
public class ImporterWorkerController {

    private static final String IMPORT_OBJECT_IS_NULL = "importObject is null ...";
    private static final String IMPORT_STARTED = "Import started ...";
    private static final String IMPORT_QUEUED = "Import queued ...";
    private static final String EXPOSING_TEMPORAL_AWS_ACCESS = "Exposing temporal aws-access";

    private final RabbitTemplate rabbitTemplate;

    private final AdminUserService adminUserService;

    private final S3TemporalTokenService s3TemporalTokenService;

    @Autowired
    public ImporterWorkerController(
            RabbitTemplate rabbitTemplate,
            AdminUserService adminUserService,
            S3TemporalTokenService s3TemporalTokenService
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.adminUserService = adminUserService;
        this.s3TemporalTokenService = s3TemporalTokenService;
    }

    @PostMapping(value = "/import")
    public ResponseEntity<EntityModel<Object>> importObject(@RequestBody ImportObject importObject) {
        log.info(IMPORT_STARTED);

        if (importObject == null) {
            log.error(IMPORT_OBJECT_IS_NULL);
            return new ResponseEntity<>(new EntityModel<>(IMPORT_OBJECT_IS_NULL), HttpStatus.BAD_REQUEST);
        }

        this.queueImport(importObject);
        log.info(IMPORT_QUEUED);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/updateLastLogin")
    public ResponseEntity updateLastLogin(Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return adminUserService.updateLastLogin(principal);
    }

    @GetMapping("/aws-access")
    public ResponseEntity<EntityModel<Credentials>> getTemporalAwsCredentials() {
        log.info(EXPOSING_TEMPORAL_AWS_ACCESS);
        Credentials credentials = s3TemporalTokenService.getTemporalToken();

        return ResponseEntity.ok(new EntityModel<>(credentials));
    }

    private void queueImport(ImportObject importObject) {
        rabbitTemplate.setMessageConverter(MessagingDefaultJackson2Serializer.jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(
                QueueConfigUtils.ImporterConfig.EXCHANGE_NAME,
                QueueConfigUtils.ImporterConfig.IMPORTER_WORKER_ROUTING_KEY,
                importObject
        );
    }
}
