package de.immomio.reporting.model.event.customer;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Niklas Lindemann
 */
@Getter
@Setter
public class PropertyNotificationEvent extends PropertyEvent {

    private static final long serialVersionUID = 1989589037026188186L;

    private String recipient;
    private String message;
}
