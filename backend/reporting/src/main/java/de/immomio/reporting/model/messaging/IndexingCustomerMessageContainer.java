package de.immomio.reporting.model.messaging;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
@NoArgsConstructor
public class IndexingCustomerMessageContainer extends AbstractIndexingMessageContainer<CustomerIndexingEvent> {

    private static final long serialVersionUID = 3290648850342756608L;

    private Long customerId;

    public IndexingCustomerMessageContainer(List<CustomerIndexingEvent> events, String indexPrefix, Long customerId) {
        super(events, indexPrefix);
        this.customerId = customerId;
    }
}
