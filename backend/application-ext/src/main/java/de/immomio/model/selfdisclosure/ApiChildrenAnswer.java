package de.immomio.model.selfdisclosure;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
public class ApiChildrenAnswer implements Serializable {

    private static final long serialVersionUID = 21202683461302625L;

    private ApiChildAnswer answer;
}
