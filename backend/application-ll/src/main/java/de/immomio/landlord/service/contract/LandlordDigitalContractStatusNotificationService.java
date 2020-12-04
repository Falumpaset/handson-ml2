package de.immomio.landlord.service.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.immomio.mail.configurator.LandlordMailConfigurator;
import de.immomio.security.service.JWTTokenService;
import de.immomio.service.shared.EmailModelProvider;
import de.immomio.service.shared.contract.status.AbstractDigitalContractStatusNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Service
public class LandlordDigitalContractStatusNotificationService extends AbstractDigitalContractStatusNotificationService {

    private LandlordMailConfigurator mailConfigurator;

    private JWTTokenService jwtTokenService;

    @Autowired
    public LandlordDigitalContractStatusNotificationService(
            LandlordMailConfigurator landlordMailConfigurator,
            JWTTokenService jwtTokenService, RabbitTemplate rabbitTemplate, EmailModelProvider emailModelProvider, LandlordMailConfigurator mailConfigurator) {
        super(rabbitTemplate, emailModelProvider);
        this.mailConfigurator = mailConfigurator;
        this.mailConfigurator = landlordMailConfigurator;
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
        return mailConfigurator.buildPsAppUrl();
    }

    @Override
    protected String getLandlordAppUrl() {
        return mailConfigurator.buildAppUrl();
    }
}
