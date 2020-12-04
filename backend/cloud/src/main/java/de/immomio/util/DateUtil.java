package de.immomio.util;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Niklas Lindemann
 */
@Slf4j
public class DateUtil {
    public static XMLGregorianCalendar dateToGregorianCalendar(Date date) {
        if (date != null) {
            return DateUtil.instantToGregorianCalendar(date.toInstant());
        } else {
            return null;
        }

    }

    public static XMLGregorianCalendar localDateToGregorianCalender(LocalDate localDate) {
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(localDate.toString());
        } catch (DatatypeConfigurationException e) {
            log.error("Error converting Date to XMLGregorianCalendar");
            e.printStackTrace();
            return null;
        }
    }

    public static XMLGregorianCalendar instantToGregorianCalendar(Instant instant) {
        try {
            if (instant != null) {
                return DatatypeFactory.newInstance().newXMLGregorianCalendar(instant.toString());
            } else {
                return null;
            }
        } catch (DatatypeConfigurationException e) {
            return null;
        }
    }

    public static String dateToFormattedString(Date date) {
        if (date == null) {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        return formatter.format(date);
    }
}
