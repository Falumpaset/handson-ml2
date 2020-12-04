package de.immomio.reporting.model.event.customer;

import de.immomio.data.landlord.entity.user.reporting.enums.RatingEventType;
import de.immomio.reporting.model.beans.propertysearcher.ReportingPropertySearcherBean;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
public class RatingEvent extends AbstractCustomerEvent implements Serializable {

    private static final long serialVersionUID = - 7959302394206944442L;

    private RatingEventType eventType;
    private ReportingPropertySearcherBean user;
    private Double rating;
}
