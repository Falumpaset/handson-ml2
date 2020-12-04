package de.immomio.recipient.service.portal.parser.openimmo;

import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.landlord.entity.property.portal.PortalBean;
import de.immomio.data.shared.entity.email.ListingDetail;
import de.immomio.mail.ReceivedEmail;
import de.immomio.recipient.service.portal.parser.EmailParser;
import org.springframework.mail.MailParseException;

import javax.mail.Message;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

public abstract class OpenimmoEmailParser extends EmailParser {

    protected OpenimmoEmailParser(Portal portal) {
        super(portal);
    }

    public ListingDetail parse(Message message) throws MailParseException {
        ListingDetail listingDetail = new ListingDetail();
        listingDetail.setListingPortal(new PortalBean(getPortal()));

        ReceivedEmail receivedEmail = new ReceivedEmail();
        try {
            receivedEmail = getContent(message.getContent(), receivedEmail);
        } catch (Exception e) {
            throw new MailParseException(e);
        }

        if (receivedEmail.getXml() == null) {
            throw new MailParseException("No XML-Content avaiable [" + getPortal().name() + "] ...");
        }

        try {
            JAXBContext jc = JAXBContext.newInstance(OpenimmoFeedback.class);

            Unmarshaller unmarshaller = jc.createUnmarshaller();
            StringReader reader = new StringReader(receivedEmail.getXml());
            OpenimmoFeedback openimmoFeedback = (OpenimmoFeedback) unmarshaller.unmarshal(reader);

            if (openimmoFeedback.getObjekt().getOobjId() != null
                    && openimmoFeedback.getObjekt().getOobjId().length() > 0) {
                listingDetail.setListingId(openimmoFeedback.getObjekt().getOobjId());
            }

            listingDetail.setFirstName(openimmoFeedback.getObjekt().getInteressent().getVorname());
            listingDetail.setName(openimmoFeedback.getObjekt().getInteressent().getNachname());
            listingDetail.setTelephone(openimmoFeedback.getObjekt().getInteressent().getTel());
            listingDetail.setEmail(openimmoFeedback.getObjekt().getInteressent().getEmail());
            listingDetail.setText(openimmoFeedback.getObjekt().getInteressent().getAnfrage());
        } catch (JAXBException e) {
            throw new MailParseException(e);
        }

        return listingDetail;
    }
}
