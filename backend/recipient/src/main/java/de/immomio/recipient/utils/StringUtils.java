package de.immomio.recipient.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author Johannes Hiemer.
 */
public class StringUtils {

    public static String removeSpecialChars(String email) {
        return email.replaceAll("([\\r\\n])", "");
    }

    public static String encodeValue(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }
}
