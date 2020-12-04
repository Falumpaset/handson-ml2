package de.immomio.reporting.model.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Fabian Beck
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractIndexingMessageContainer<IE extends IndexingEvent> implements Serializable {
    private static final long serialVersionUID = 5771847599435024776L;

    private List<IE> events = new ArrayList<>();
    private String indexPrefix;
}
