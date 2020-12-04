package de.immomio.model.selfdisclosure;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
public class ApiConfirmation implements Serializable {
    private static final long serialVersionUID = -9171958438856701212L;

    private String text;

    private Boolean checked;
}
