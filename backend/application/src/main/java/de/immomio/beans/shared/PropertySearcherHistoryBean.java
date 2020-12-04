package de.immomio.beans.shared;

import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.utils.compare.CompareBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropertySearcherHistoryBean implements Serializable {

    private static final long serialVersionUID = -5295387175462158595L;

    private Long propertyId;
    private String externalId;
    private Enum type;
    private Date created;
    private PropertyData propertyData;
    private Boolean propertyDeleted;
    private Portal portal;
    private List<CompareBean> differences;

    public PropertySearcherHistoryBean(
            Property property,
            Enum type,
            Date created,
            Boolean propertyDeleted,
            PropertyData propertyData,
            Portal portal
    ) {
        if (property != null) {
            this.propertyId = property.getId();
            this.externalId = property.getExternalId();
        }

        this.type = type;
        this.created = created;
        this.propertyDeleted = propertyDeleted;
        this.propertyData = propertyData;
        this.portal = portal;
    }
}