package de.immomio.cloud.service;

import de.immomio.cloud.service.google.GoogleMapsService;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.data.shared.bean.common.GeoCoordinates;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Maik Kingma
 */

@Slf4j
public abstract class AbstractGeoCodingService {

    public void generateAndSaveCoordinates(List<Property> properties, boolean forceGenerateCoordinates) {
        List<Property> propertiesToGenerate = properties.stream()
                .filter(property -> coordinatesAreNull(property) || forceGenerateCoordinates)
                .collect(Collectors.toList());
        saveGeoCoordinates(propertiesToGenerate);
        customSavePropertyLocation(propertiesToGenerate);
    }

    private boolean coordinatesAreNull(Property property) {
        GeoCoordinates coordinates = property.getData().getAddress().getCoordinates();
        return coordinates == null ||
                coordinates.getLatitude() == null ||
                coordinates.getLongitude() == null ||
                (coordinates.getLatitude() == 0d && coordinates.getLongitude() == 0d);
    }

    public abstract Property saveProperty(Property property);

    public abstract void customUpdatePropertyLocation(Long id);

    public abstract GoogleMapsService getGoogleMapsService();

    private void saveGeoCoordinates(List<Property> properties) {
        properties.forEach(property -> {
            Address address = property.getData().getAddress();
            if (address != null) {
                try {
                    GeoCoordinates coordinates = getGoogleMapsService().getGeoCoordinates(address.toGoogleString());
                    address.setCoordinates(coordinates);
                    saveProperty(property);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        });
    }

    private void customSavePropertyLocation(List<Property> properties) {
        properties.forEach(property -> {
            try {
                customUpdatePropertyLocation(property.getId());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }
}
