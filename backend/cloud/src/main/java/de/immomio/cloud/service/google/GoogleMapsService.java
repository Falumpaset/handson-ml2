package de.immomio.cloud.service.google;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import de.immomio.data.shared.bean.common.GeoCoordinates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GoogleMapsService {

    private final GeoApiContext context;

    @Autowired
    public GoogleMapsService(@Value("${google.maps.api.key}") final String googleMapsApiKey) {
        this.context = new GeoApiContext.Builder().apiKey(googleMapsApiKey).build();
    }

    public GeoCoordinates getGeoCoordinates(String address) {

        GeocodingApiRequest req = GeocodingApi.newRequest(context).address(address);

        GeoCoordinates geoCoordinates = new GeoCoordinates(0.0, 0.0);
        try {
            GeocodingResult[] result = req.await();
            LatLng location = result[0].geometry.location;
            // Handle successful request.
            geoCoordinates = new GeoCoordinates(location.lng, location.lat);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            req.cancel();
        }

        return geoCoordinates;
    }
}
