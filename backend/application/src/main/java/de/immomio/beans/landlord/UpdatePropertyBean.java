package de.immomio.beans.landlord;

import de.immomio.beans.shared.conversation.ConversationConfigBean;
import de.immomio.data.base.type.property.PropertyType;
import de.immomio.data.base.type.property.PropertyWriteProtection;
import de.immomio.data.landlord.bean.prioset.ChangePriosetBean;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
public class UpdatePropertyBean implements Serializable {

    private static final long serialVersionUID = 5078288650127221778L;

    private PropertyData data;

    private String externalId;

    private PropertyType type;

    private PropertyWriteProtection writeProtection;

    private Long userId;

    private Long propertyManagerId;

    private Double autoOfferThreshold;

    private boolean autoOfferEnabled;

    private boolean showSelfDisclosureQuestions;

    private ConversationConfigBean conversationConfigs;

    private ChangePriosetBean prioset;

    private List<Long> credentialIds = new ArrayList<>();
}
