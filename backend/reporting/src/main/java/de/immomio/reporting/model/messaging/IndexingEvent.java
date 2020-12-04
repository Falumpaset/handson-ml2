package de.immomio.reporting.model.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Fabian Beck
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IndexingEvent implements Serializable {
    private static final long serialVersionUID = - 4472958547153819360L;

    private String content;
}
