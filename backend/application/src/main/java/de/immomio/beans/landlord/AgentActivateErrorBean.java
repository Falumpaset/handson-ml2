package de.immomio.beans.landlord;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author Niklas Lindemann
 */

@Getter
@AllArgsConstructor
public class AgentActivateErrorBean implements Serializable {
    private static final long serialVersionUID = -302201305786022527L;

    private CustomerUserBean user;
    private String error;
}
