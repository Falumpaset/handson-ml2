package de.immomio.reporting.model.event.customer;

import de.immomio.reporting.model.beans.ReportingEditorBean;
import de.immomio.reporting.model.event.AbstractEvent;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Fabian Beck
 */

@Getter
@Setter
public abstract class AbstractCustomerEvent extends AbstractEvent {

    private static final long serialVersionUID = - 4507856951790394646L;

    protected ReportingEditorBean editor;
    private Long id;
}
