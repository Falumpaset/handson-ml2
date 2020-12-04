package de.immomio.reporting.model.beans;

import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.base.type.property.PropertyTask;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.bean.user.AgentInfo;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.portal.PropertyPortal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
@NoArgsConstructor
public class ReportingPropertyBean implements Serializable {

    private static final long serialVersionUID = 1036126149928844219L;
    private Long id;
    private PropertyData data;
    private Date validUntil;
    private int runtimeInDays;
    private PropertyTask task;
    private boolean autoOfferEnabled;
    private Double autoOfferThreshold;
    private List<Portal> portals;
    private Date created;
    private AgentInfo agentInfo;

    public ReportingPropertyBean(Property property) {
        this.id = property.getId();
        this.data = property.getData();
        this.validUntil = property.getValidUntil();
        this.runtimeInDays = property.getRuntimeInDays();
        this.task = property.getTask();
        this.autoOfferEnabled = property.isAutoOfferEnabled();
        this.autoOfferThreshold = property.getAutoOfferThreshold();
        this.portals = property.getPortals().stream().map(PropertyPortal::getPortal).collect(Collectors.toList());
        this.created = property.getCreated();
        this.agentInfo = new AgentInfo(property.getUser());
    }
}
