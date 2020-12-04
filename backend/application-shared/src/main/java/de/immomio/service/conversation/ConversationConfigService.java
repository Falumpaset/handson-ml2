package de.immomio.service.conversation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.immomio.beans.shared.conversation.ConversationConfigBean;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.base.type.property.PropertyType;
import de.immomio.data.shared.entity.conversation.Conversation;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.model.repository.shared.application.PropertyApplicationRepository;
import de.immomio.model.repository.shared.conversation.ConversationRepository;
import de.immomio.service.shared.conversation.AbstractConversationConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Freddy Sawma
 */

@Slf4j
@Service
public class ConversationConfigService extends AbstractConversationConfigService {

    private final ConversationRepository conversationRepository;
    private final PropertyApplicationRepository applicationRepository;
    private static final String PREFERENCE_VALIDATION_FAILED = "PREFERENCE_VALIDATION_FAILED_L";

    @Autowired
    public ConversationConfigService(
            ConversationRepository conversationRepository,
            PropertyApplicationRepository applicationRepository
    ) {
        this.conversationRepository = conversationRepository;
        this.applicationRepository = applicationRepository;
    }

    public boolean isAllowedToSendMessage(Long applicationId) {
        Optional<PropertyApplication> applicationOpt = applicationRepository.findById(applicationId);

        if (applicationOpt.isPresent()) {
            PropertyApplication application = applicationOpt.get();
            if (application.getProperty().getType() != PropertyType.FLAT) {
                return false;
            }

            ConversationConfigBean config = extractConfigs(application);
            if (config.isForbidden()) {
                return false;
            } else if (config.isAllowed()) {
                return true;
            }

            if (config.isWhenInvited() && (ACTIVE_STATUSES.contains(application.getStatus()) || application.isAskedForIntent())) {
                return true;
            }

            if (config.isReplyOnly()) {
                Optional<Conversation> conversationOptional = conversationRepository.findById(applicationId);
                return conversationOptional.isPresent() && conversationRepository.hasMessagesByLandlord(conversationOptional.get());
            }
        }

        return false;
    }

    public String serializeConfigs(ConversationConfigBean configBean) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(configBean);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return "{}";
        }
    }

    public void validateConversationConfigBean(ConversationConfigBean configBean) {

        boolean allowedAndForbidden = configBean.isAllowed() && configBean.isForbidden();

        if (allowedAndForbidden || ((configBean.isAllowed() || configBean.isForbidden()) && (configBean.isReplyOnly() || configBean.isWhenInvited()))) {
            throw new ApiValidationException(PREFERENCE_VALIDATION_FAILED);
        }
    }




}
