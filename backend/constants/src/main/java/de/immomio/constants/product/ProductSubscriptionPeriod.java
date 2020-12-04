package de.immomio.constants.product;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.Date;

public enum ProductSubscriptionPeriod {
    ONCE(null),
    WEEKLY(Period.weeks(1)),
    MONTHLY(Period.months(1)),
    YEARLY(Period.years(1));

    private Period period;

    ProductSubscriptionPeriod(Period period) {
        this.period = period;
    }

    public static Date calculateDate(Date start, Period period) {
        if (period == null) {
            return null;
        } else if (start == null) {
            start = new Date();
        }

        DateTime tmp = new DateTime(start);

        tmp = tmp.plus(period);

        return tmp.toDate();
    }

    public Date calculateDate(Date start) {
        if (period == null) {
            return null;
        } else if (start == null) {
            start = new Date();
        }

        DateTime tmp = new DateTime(start);

        tmp = tmp.plus(period);

        return tmp.toDate();
    }
}
