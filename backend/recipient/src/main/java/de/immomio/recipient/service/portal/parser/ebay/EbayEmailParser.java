package de.immomio.recipient.service.portal.parser.ebay;

import de.immomio.data.base.type.portal.Portal;
import de.immomio.recipient.service.portal.parser.openimmo.OpenimmoEmailParser;
import org.springframework.stereotype.Component;

/**
 * @author Johannes Hiemer.
 */
@Component
public class EbayEmailParser extends OpenimmoEmailParser {

    private static final Portal PORTAL = Portal.EBAY;

    private static final String EBAY_SENDER_ADDRESS = "noreply-immobilien@ebay-kleinanzeigen.de";

    public EbayEmailParser() {
        super(PORTAL);
    }

    @Override
    public boolean isApplicableSender(String sender) {
        if (sender == null || sender.isEmpty()) {
            return false;
        }

        return sender.contains(EBAY_SENDER_ADDRESS);
    }
}
