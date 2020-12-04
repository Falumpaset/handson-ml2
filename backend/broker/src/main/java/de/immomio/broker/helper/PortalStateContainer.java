package de.immomio.broker.helper;

import de.immomio.data.landlord.entity.property.portal.PropertyPortalState;
import de.immomio.exporter.exception.ExporterException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PortalStateContainer {

    private PropertyPortalState state;

    private ExporterException exporterException;

    public PortalStateContainer(PropertyPortalState state, ExporterException exception) {
        this.state = state;
        this.exporterException = exception;
    }
}
