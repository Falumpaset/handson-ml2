package de.immomio.service;

import de.immomio.constants.Location;
import de.immomio.data.base.type.price.CurrencyType;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.TimeZone;

@Service
public class LocationService {

    @Value("${email.error}")
    private String errorEmail;

    @Value("${email.renter}")
    private String renterEmail;

    @Value("${email.contact}")
    private String contactEmail;

    @Value("${email.system}")
    private String systemEmail;

    @Value("${email.support}")
    private String supportEmail;

    @Value("${email.bugs}")
    private String bugsEmail;

    @Value("${email.monitoring}")
    private String monitoringEmail;

    @Value("${timezone.europe}")
    private String ZONE_ID;

    @Getter
    @Value("${customer.default.logo}")
    private String logo;

    public Location getLocation() {
        // TODO implement
        return Location.DE;
    }

    public Locale getLocale() {
        // TODO implement
        return Locale.GERMAN;
    }

    public String getErrorEmail() {
        return errorEmail;
    }

    public String getRenterEmail() {
        return renterEmail;
    }

    public String getInsertionEmail() {
        return contactEmail;
    }

    public String getSystemEmail() {
        return systemEmail;
    }

    public String getSupportEmail() {
        return supportEmail;
    }

    public String getBugsEmail() {
        return bugsEmail;
    }

    public String getMonitoringEmail() {
        return monitoringEmail;
    }

    public Double getTaxrate() {
        // TODO implement
        return 1.19;
    }

    public CurrencyType getCurrency() {
        // TODO implement
        return CurrencyType.EUR;
    }

    public TimeZone getTimeZone() {
        // TODO implement
        return TimeZone.getTimeZone(ZONE_ID);
    }

}
