package de.immomio.reporting.model.event;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Fabian Beck
 */

@Getter
@Setter
public class EmailRecipientEvent extends AbstractEvent implements Serializable {
    private static final long serialVersionUID = - 4623181024295149251L;

    private String sender;
    private List<String> recipients;
    private String subject;
    private Date sentDate;
    private String htmlContent;
    private String plainContent;
}
