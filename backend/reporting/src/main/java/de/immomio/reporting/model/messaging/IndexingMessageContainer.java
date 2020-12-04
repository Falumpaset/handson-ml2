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
public class IndexingMessageContainer extends AbstractIndexingMessageContainer<IndexingEvent> {

    private static final long serialVersionUID = - 3625942395718944390L;

    public IndexingMessageContainer(List<IndexingEvent> events, String indexPrefix) {
        super(events, indexPrefix);
    }
}