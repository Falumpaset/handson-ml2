package de.immomio.util;

import de.immomio.exception.ExternalApiValidationException;
import org.apache.commons.lang3.StringUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author Niklas Lindemann
 */
public class DateUtil {
    public static Date getUtcDateFromParam(String param) {
        if (StringUtils.isBlank(param)) {
            return new Date(0);
        }
        try {
            ZonedDateTime parsed = ZonedDateTime.parse(param, DateTimeFormatter.ISO_DATE_TIME);
            ZonedDateTime utc = parsed.withZoneSameInstant(ZoneId.systemDefault());
            return Date.from(utc.toInstant());
        } catch (Exception e) {
            throw new ExternalApiValidationException("DATE COULD NOT BE PARSED " + param);
        }
    }
}
