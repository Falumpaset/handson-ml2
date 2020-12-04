package de.immomio.landlord.analytics;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Maik Kingma
 */

@Getter
@Setter
public class OneDayApplicationStatistics {

    private long total;

    private long unanswered;

    private long accepted;

    private long rejected;

    OneDayApplicationStatistics(long total, long unanswered, long accepted, long rejected) {
        this.total = total;
        this.unanswered = unanswered;
        this.accepted = accepted;
        this.rejected = rejected;
    }
}
