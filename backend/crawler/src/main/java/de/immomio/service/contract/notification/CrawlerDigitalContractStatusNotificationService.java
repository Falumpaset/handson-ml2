package de.immomio.service.contract.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.immomio.mail.configurator.LandlordMailConfigurator;
import de.immomio.mail.configurator.PropertySearcherMailConfigurator;
import de.immomio.security.service.JWTTokenService;
import de.immomio.service.shared.EmailModelProvider;
import de.immomio.service.shared.contract.status.AbstractDigitalContractStatusNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CrawlerDigitalContractStatusNotificationService extends AbstractDigitalContractStatusNotificationService {

    private final PropertySearcherMailConfigurator propertySearcherMailConfigurator;
    private final LandlordMailConfigurator landlordMailConfigurator;
    private final JWTTokenService jwtTokenService;

    @Autowired
    public CrawlerDigitalContractStatusNotificationService(
            PropertySearcherMailConfigurator searcherMailConfigurator,
            LandlordMailConfigurator landlordMailConfigurator,
            JWTTokenService jwtTokenService, RabbitTemplate rabbitTemplate, EmailModelProvider emailModelProvider) {
        super(rabbitTemplate, emailModelProvider);
        this.propertySearcherMailConfigurator = searcherMailConfigurator;
        this.landlordMailConfigurator = landlordMailConfigurator;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected String generateDigitalContractSignToken(Long signerId) {
        try {
            return jwtTokenService.generateDigitalContractSignToken(signerId);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    protected String getPropertySearcherAppUrl() {
        return propertySearcherMailConfigurator.buildAppUrl();
    }

    @Override
    protected String getLandlordAppUrl() {
        return landlordMailConfigurator.buildAppUrl();
    }

}
