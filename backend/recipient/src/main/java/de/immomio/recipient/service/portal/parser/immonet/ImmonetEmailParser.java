package de.immomio.recipient.service.portal.parser.immonet;

import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.landlord.entity.property.portal.PortalBean;
import de.immomio.data.shared.entity.email.ListingDetail;
import de.immomio.mail.ReceivedEmail;
import de.immomio.recipient.service.portal.parser.EmailParser;
import org.apache.commons.io.IOUtils;
import org.springframework.mail.MailParseException;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

/**
 * @author Johannes Hiemer.
 */
@Component
public class ImmonetEmailParser extends EmailParser {

    private static final Portal PORTAL = Portal.IMMONET_DE;

    public ImmonetEmailParser() {
        super(PORTAL);
    }

    /* (non-Javadoc)
     * @see de.immomio.mail.parser.AbstractEmailParser#parse(java.lang.String)
     */
    @Override
    public ListingDetail parse(Message message) throws MailParseException {
        String subject;
        String objectId;
        ReceivedEmail receivedEmail = new ReceivedEmail();

        ListingDetail listingDetail = new ListingDetail();
        listingDetail.setListingPortal(new PortalBean(PORTAL));

        try {
            subject = message.getSubject();
            receivedEmail = getContent(message.getContent(), receivedEmail);
        } catch (Exception e) {
            throw new MailParseException(e);
        }
        if (subject != null && subject.contains("Kontaktanfrage") && subject.contains("Referenznummer")) {
            objectId = subject.substring(subject.indexOf("Referenznummer: ") + 16, subject.length());
            listingDetail.setListingId(objectId);
        }
        if (receivedEmail.getPlain() == null) {
            throw new MailParseException("No Plain-Content avaiable [" + PORTAL.name() + "] ...");
        }
        try {
            List<String> lines = IOUtils.readLines(new StringReader(receivedEmail.getPlain()));
            for (String line : lines) {
                if (line.contains("Anbieter-Objekt-ID:")) {
                    String listingId = line.substring("Anbieter-Objekt-ID:".length() + 1);
                    if (listingId.contains("-")) {
                        listingId = listingId.split("-")[0];
                        listingDetail.setListingId(listingId);
                    } else {
                        listingDetail.setListingId(listingId);
                    }
                } else if (line.contains("Vorname:")) {
                    listingDetail.setFirstName(line.substring("Vorname:".length() + 1));
                } else if (line.contains("Nachname:")) {
                    listingDetail.setName(line.substring("Nachname:".length() + 1));
                } else if (line.contains("Telefon-Nummer:")) {
                    listingDetail.setTelephone(line.substring("Telefon-Nummer:".length() + 1));
                } else if (line.contains("E-Mail:")) {
                    listingDetail.setEmail(line.substring("E-Mail:".length() + 1));
                } else if (line.contains("Anmerkungen:")) {
                    listingDetail.setText(line.substring("Anmerkungen:".length() + 1));
                }
            }
        } catch (IOException e) {
            throw new MailParseException(e);
        }
        return listingDetail;
    }
}
