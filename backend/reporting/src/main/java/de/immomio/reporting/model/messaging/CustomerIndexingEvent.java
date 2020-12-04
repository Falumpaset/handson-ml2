package de.immomio.reporting.model.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
@NoArgsConstructor
public class CustomerIndexingEvent extends IndexingEvent {
    private static final long serialVersionUID = 8623000319430116786L;

    private Long id;

    public CustomerIndexingEvent(String content, Long id) {
        super(content);
        this.id = id;
    }
}
