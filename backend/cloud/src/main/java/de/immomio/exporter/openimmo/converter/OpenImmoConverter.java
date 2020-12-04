package de.immomio.exporter.openimmo.converter;

import de.immobilienscout24.rest.schema.common._1.CountryCode;
import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.openimmo.OpenImmoAktionTyp;
import de.immomio.exporter.exception.ExporterException;
import de.immomio.openimmo.Openimmo;
import de.immomio.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Instant;

/**
 * @author Fabian Beck
 */

@Slf4j
@Service
public class OpenImmoConverter {

    private final LocationService locationService;
    private final OpenImmoConverterFlat openimmoConverterFlat;
    private final OpenImmoConverterGarage openimmoConverterGarage;
    private final OpenImmoConverterCommercial openimmoConverterCommercial;

    @Autowired
    public OpenImmoConverter(LocationService locationService, OpenImmoConverterFlat openimmoConverterFlat,
            OpenImmoConverterGarage openimmoConverterGarage, OpenImmoConverterCommercial openimmoConverterCommercial) {
        this.locationService = locationService;
        this.openimmoConverterFlat = openimmoConverterFlat;
        this.openimmoConverterGarage = openimmoConverterGarage;
        this.openimmoConverterCommercial = openimmoConverterCommercial;
    }

    public Openimmo convertForUnprocess(Property property, String supplierNumber, Portal portal,
            URL applicationLink, String shortDescription, boolean userExternId) throws ExporterException {
        OpenImmoConverterData converterData = OpenImmoConverterData.builder().
                property(property).
                aktion(OpenImmoAktionTyp.DELETE).
                supplierNumber(supplierNumber).
                applicationLink(applicationLink).
                shortDescription(shortDescription).
                now(Instant.now()).
                portal(portal).
                defaultContactEmail(locationService.getInsertionEmail()).
                countryCode(CountryCode.DEU).
                useExternalId(userExternId).build();

        return convert(converterData);
    }

    public Openimmo convertForProcess(Property property, String supplierNumber, Portal portal, URL applicationLink,
            String shortDescription, String miscellaneousText, boolean userExternId) throws ExporterException {
        OpenImmoConverterData converterData = OpenImmoConverterData.builder().
                property(property).
                aktion(OpenImmoAktionTyp.CHANGE).
                supplierNumber(supplierNumber).
                applicationLink(applicationLink).
                shortDescription(shortDescription).
                miscellaneousText(miscellaneousText).
                now(Instant.now()).
                portal(portal).
                defaultContactEmail(locationService.getInsertionEmail()).
                countryCode(CountryCode.DEU).
                useExternalId(userExternId).build();

        return convert(converterData);
    }

    private Openimmo convert(OpenImmoConverterData data) throws ExporterException {
        if (data.getProperty().getType() == null) {
            log.error("Type for property " + data.getProperty().getId() + "is null");
            throw new ExporterException("Type for property " + data.getProperty().getId() + "is null");
        }

        switch (data.getProperty().getType()) {
            case GARAGE:
                return openimmoConverterGarage.createOpenimmo(data);
            case COMMERCIAL:
                return openimmoConverterCommercial.createOpenimmo(data);
            default:
                return openimmoConverterFlat.createOpenimmo(data);
        }
    }
}
