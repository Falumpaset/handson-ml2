package de.immomio.recipient.service.portal.parser.ivd24;

import de.immomio.data.base.type.portal.Portal;
import de.immomio.recipient.service.portal.parser.openimmo.OpenimmoEmailParser;
import org.springframework.stereotype.Component;

/**
 * @author Johannes Hiemer.
 */
@Component
public class Ivd24EmailParser extends OpenimmoEmailParser {

    private static final Portal PORTAL = Portal.IVD;

    public Ivd24EmailParser() {
        super(PORTAL);
    }

}
