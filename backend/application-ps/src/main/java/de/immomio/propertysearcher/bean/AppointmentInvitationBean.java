package de.immomio.propertysearcher.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentInvitationBean {

    private static final long serialVersionUID = 5310888004397067001L;

    private Long appointmentId;

    private Long id;

}
