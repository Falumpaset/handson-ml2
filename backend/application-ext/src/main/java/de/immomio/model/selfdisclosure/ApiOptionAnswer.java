package de.immomio.model.selfdisclosure;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
public class ApiOptionAnswer implements Serializable {

    private static final long serialVersionUID = 7004365033347187166L;

    private String name;

    private Object value;
}
