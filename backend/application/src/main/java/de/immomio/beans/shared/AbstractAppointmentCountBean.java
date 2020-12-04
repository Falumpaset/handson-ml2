package de.immomio.beans.shared;

import lombok.Getter;

/**
 * @author Niklas Lindemann
 */

@Getter
public abstract class AbstractAppointmentCountBean {
    private Long past;
    private Long upcoming;

    protected AbstractAppointmentCountBean(Long past, Long upcoming) {
        this.past = past;
        this.upcoming = upcoming;
    }
}
