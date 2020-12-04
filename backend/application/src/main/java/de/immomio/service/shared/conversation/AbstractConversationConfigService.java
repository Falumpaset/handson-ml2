package de.immomio.service.shared.conversation;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.immomio.beans.shared.conversation.ConversationConfigBean;
import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.base.type.property.PropertyType;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;

public class AbstractConversationConfigService {

    public static final ConversationConfigBean DEFAULT_CONFIG_BEAN = new ConversationConfigBean(false, false, false, true);
    protected static final List<ApplicationStatus> ACTIVE_STATUSES = List.of(ApplicationStatus.ACCEPTED, ApplicationStatus.INTENT);
    private static final String CONVERSATION_PREFERENCES = "conversationConfigs";

    public boolean isPropertySearcherAbleToRespondViaMail(PropertyApplication application) {
        if (application.getProperty().getType() != PropertyType.FLAT) {
            return true;
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
            return true;
        }

        return false;
    }

    protected ConversationConfigBean extractConfigs(PropertyApplication application) {
        Property property = application.getProperty();
        LandlordUser user = property.getUser();

        String propertyConfigs = property.getPreferences().get(CONVERSATION_PREFERENCES);
        String userConfigs = (String) user.getPreferences().get(CONVERSATION_PREFERENCES);
        if (StringUtils.isNotBlank(propertyConfigs)) {
            ConversationConfigBean conversationPropertyConfigs = extractConfigsFromJson(propertyConfigs);
            if (conversationPropertyConfigs.nothingIsSet()) {
                if (StringUtils.isNotBlank(userConfigs)) {
                    return extractConfigsFromJson(userConfigs);
                } else {
                    return DEFAULT_CONFIG_BEAN;
                }
            }
            return conversationPropertyConfigs;
        } else if (StringUtils.isNotBlank(userConfigs)) {
            return extractConfigsFromJson(userConfigs);
        } else {
            return DEFAULT_CONFIG_BEAN;
        }
    }

    private ConversationConfigBean extractConfigsFromJson(String configs) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ConversationConfigBean conversationConfigBean = objectMapper.readValue(configs, ConversationConfigBean.class);
            return conversationConfigBean != null ? conversationConfigBean : DEFAULT_CONFIG_BEAN;
        } catch (IOException e) {
            return DEFAULT_CONFIG_BEAN;
        }
    }
}
