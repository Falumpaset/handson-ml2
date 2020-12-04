/**
 *
 */
package de.immomio.common.validator;

import org.apache.commons.validator.routines.UrlValidator;

/**
 * @author Johannes Hiemer.
 */
public class CommonValidator {

    public static boolean isUrlValid(String url) {
        String[] schemes = {"http", "https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        return urlValidator.isValid(url);
    }

}
