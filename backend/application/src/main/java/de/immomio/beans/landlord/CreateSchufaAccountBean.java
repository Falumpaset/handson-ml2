package de.immomio.beans.landlord;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
public class CreateSchufaAccountBean implements Serializable {

    private static final long serialVersionUID = -4348378623733657589L;

    private String username;
    private String password;
}
