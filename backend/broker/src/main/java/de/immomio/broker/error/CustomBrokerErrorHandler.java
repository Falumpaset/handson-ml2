package de.immomio.broker.error;

import de.immomio.broker.helper.PortalStateContainer;
import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.portal.PropertyPortalState;
import de.immomio.exporter.exception.ExporterException;
import de.immomio.messages.InternalCommunicationErrorHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CustomBrokerErrorHandler extends InternalCommunicationErrorHandler {


    /* (non-Javadoc)
     * @see org.springframework.util.ErrorHandler#handleError(java.lang.Throwable)
     */
    public String handleError(Property property, Map<Portal, PortalStateContainer> result) {
        StringBuilder desc = new StringBuilder();
        desc.append("Exception thrown with: ");
        desc.append("[property (");
        desc.append(property == null ? "null" : property.getId().toString());
        desc.append(") usage (");
        desc.append(") for the portals: \n");
        for (Map.Entry<Portal, PortalStateContainer> entry : result.entrySet()) {

            String exporterMessage = getExporterMessage(entry);
            if (entry.getValue().getState().equals(PropertyPortalState.ERROR)) {
                desc.append(entry.getKey()
                                    + ": failed with exception cause: "
                                    + exporterMessage + "\n");
            }
            log.error(exporterMessage,
                    entry.getValue().getExporterException());
            desc.append("---------------------------------------------------------------");
        }
        desc.append(")]");

        log.error(desc.toString());


        return desc.toString();
    }

    private String getExporterMessage(Map.Entry<Portal, PortalStateContainer> entry) {
        ExporterException exporterException = entry.getValue().getExporterException();
        if (exporterException == null) {
            return "";
        }

        return exporterException.getMessage() +
                "----" +
                System.lineSeparator() +
                exporterException.getCause().getMessage();
    }

    @Override
    public void handleError(Throwable t) {
        log.error("Exception thrown with: {}", exceptionAsString(t));
    }

}