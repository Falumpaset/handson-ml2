package de.immomio.exporter.openimmo.converter;

import de.immobilienscout24.rest.schema.common._1.CountryCode;
import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.openimmo.OpenImmoAktionTyp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URL;
import java.time.Instant;

/**
 * @author Fabian Beck
 */

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpenImmoConverterData {

    private Property property;
    private OpenImmoAktionTyp aktion;
    private String supplierNumber;
    private URL applicationLink;
    private String shortDescription;
    private String miscellaneousText;
    private Instant now;
    private Portal portal;

    private String defaultContactEmail;
    private CountryCode countryCode;

    private boolean useExternalId;
}
