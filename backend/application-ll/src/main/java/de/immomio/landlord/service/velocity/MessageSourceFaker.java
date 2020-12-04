package de.immomio.landlord.service.velocity;

import java.util.Locale;

/**
 * @author Johannes Hiemer.
 */
public class MessageSourceFaker {

    public String getMessage(String key, Object[] args, Locale locale) {
        return "{{" + key + "}}";
    }
}
