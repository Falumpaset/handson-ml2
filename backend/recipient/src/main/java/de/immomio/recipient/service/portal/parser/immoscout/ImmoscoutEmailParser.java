package de.immomio.recipient.service.portal.parser.immoscout;

import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.landlord.entity.property.portal.PortalBean;
import de.immomio.data.shared.entity.email.ListingDetail;
import de.immomio.mail.ReceivedEmail;
import de.immomio.recipient.service.portal.parser.openimmo.OpenimmoEmailParser;
import javax.mail.Message;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.mail.MailParseException;
import org.springframework.stereotype.Component;

/**
 * @author Johannes Hiemer.
 */
@Component
public class ImmoscoutEmailParser extends OpenimmoEmailParser {

    private static final Portal PORTAL = Portal.IMMOBILIENSCOUT24_DE;
    private static final String PERSONAL_INFORMATION = "Weitere Daten zum Interessenten";

    public ImmoscoutEmailParser() {
        super(PORTAL);
    }

    /* (non-Javadoc)
     * @see de.immomio.mail.parser.AbstractEmailParser#parse(java.lang.String)
     */
    @Override
    public ListingDetail parse(Message message) throws MailParseException {
        String subject;
        ReceivedEmail receivedEmail = new ReceivedEmail();
        ListingDetail listingDetails = new ListingDetail();

        try {
            subject = message.getSubject();
            receivedEmail = getContent(message.getContent(), receivedEmail);
        } catch (Exception e) {
            throw new MailParseException(e);
        }

        if (receivedEmail.getXml() != null) {
            return cutPersonalInformationFromListingDetail(super.parse(message));
        }

        String content = receivedEmail.getHtml();
        if (content == null && receivedEmail.getPlain() != null) {
            content = receivedEmail.getPlain();
        }
        if (content != null && content.length() > 0) {

            Document document = Jsoup.parse(content);

            setObjectId(subject, listingDetails);
            setFirstname(listingDetails, document);
            setLastname(listingDetails, document);
            setPhone(listingDetails, document);
            setMail(listingDetails, document);
            setApplicationText(listingDetails, document);
        }

        return listingDetails;
    }

    private void setObjectId(String subject, ListingDetail listingDetails) {
        String objectId;
        if (subject != null) {
            final String OBJECT_MARKER = "Objekt";
            if (subject.contains(OBJECT_MARKER)) {
                if (subject.contains("(") && subject.contains(")")) {
                    objectId = subject.substring(subject.indexOf(OBJECT_MARKER) + OBJECT_MARKER.length(),
                            subject.lastIndexOf(')'));
                } else {
                    objectId = subject.substring(subject.indexOf(OBJECT_MARKER) + OBJECT_MARKER.length()
                    );
                }
                listingDetails.setListingId(
                        objectId.trim()); // IM24 has two blanks between 'Object' and 'id' in their regular mails
                listingDetails.setListingPortal(new PortalBean(PORTAL));
            }
        }
    }

    private void setApplicationText(ListingDetail listingDetails, Document document) {
        if (document.select("td:contains(Anmerkungen/Fragen des Interessenten)").size() > 0) {
            Element tbody = document.select("td:contains(Anmerkungen/Fragen des Interessenten)")
                    .last().parents().get(3);
            if (tbody != null) {
                Element td = tbody.getElementsByTag("td").last();
                String value = td.text();
                listingDetails.setText(value);
            }
        }
    }

    private void setMail(ListingDetail listingDetails, Document document) {
        Element email;
        email = document.select("td:containsOwn(E-Mail:)").last().parent().select("td").last();
        if (email != null) {
            listingDetails.setEmail(email.text());
        }
    }

    private void setPhone(ListingDetail listingDetails, Document document) {
        Element telephone;
        telephone = document.select("td:containsOwn(Telefon:)").last().parent().select("td").last();
        if (telephone != null) {
            listingDetails.setTelephone(telephone.text());
        }
    }

    private void setLastname(ListingDetail listingDetails, Document document) {
        Element name;
        name = document.select("td:containsOwn(Nachname:)").last().parent().select("td").last();
        if (name != null) {
            listingDetails.setName(name.text());
        }
    }

    private void setFirstname(ListingDetail listingDetails, Document document) {
        Element firstname;
        firstname = document.select("td:containsOwn(Vorname:)").last().parent().select("td").last();
        if (firstname != null) {
            listingDetails.setFirstName(firstname.text());
        }
    }

    private ListingDetail cutPersonalInformationFromListingDetail(ListingDetail listingDetail) {
        String applicationText = listingDetail.getText();
        if (applicationText != null && applicationText.contains(PERSONAL_INFORMATION)) {
            listingDetail.setText(applicationText.substring(0, applicationText.indexOf(PERSONAL_INFORMATION)).trim());
        }
        return listingDetail;
    }
}