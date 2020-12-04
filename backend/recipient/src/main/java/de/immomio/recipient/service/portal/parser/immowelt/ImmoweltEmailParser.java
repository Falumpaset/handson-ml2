package de.immomio.recipient.service.portal.parser.immowelt;

import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.landlord.entity.property.portal.PortalBean;
import de.immomio.data.shared.entity.email.ListingDetail;
import de.immomio.mail.ReceivedEmail;
import de.immomio.recipient.service.portal.parser.immowelt.xml.Interessent;
import de.immomio.recipient.service.portal.parser.immowelt.xml.Objektanfrage;
import de.immomio.recipient.service.portal.parser.openimmo.OpenimmoEmailParser;
import de.immomio.recipient.service.portal.parser.openimmo.OpenimmoFeedback;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.mail.MailParseException;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Johannes Hiemer.
 */
@Component
@Slf4j
public class ImmoweltEmailParser extends OpenimmoEmailParser {

    private static final Portal PORTAL = Portal.IMMOWELT_DE;

    private static final String SUBJECT_REF_NR = "Ref.-Nr.";

    private static final String NAME = "Name";

    private static final String TELEFON = "Telefon";

    private static final String E_MAIL = "E-Mail";

    private static final String NACHRICHT = "Nachricht";

    public ImmoweltEmailParser()  {
        super(PORTAL);
    }

    /*
     * (non-Javadoc)
     *
     * @see de.immomio.mail.parser.AbstractEmailParser#parse(java.lang.String)
     */
    @Override
    public ListingDetail parse(Message message) throws MailParseException {
        ListingDetail listingDetail = new ListingDetail();
        listingDetail.setListingPortal(new PortalBean(PORTAL));

        try {
            String subject = message.getSubject();
            listingDetail.setListingId(getPropertyIdFromSubject(subject));
            List<ReceivedEmail> contents = getContents(message.getContent());

            // parse xml contents
            contents.stream()
                    .filter(content -> content.getXml() != null &&
                            parseXmlContent(content.getXml(), Objektanfrage.class) != null)
                    .findFirst()
                    .ifPresent(content -> populateListingDetail(listingDetail, parseXmlContent(content.getXml(),
                            Objektanfrage.class)));

            // try to parse using parent method
            if (listingDetail.getEmail() == null) {
                contents.stream()
                        .filter(content -> content.getXml() != null &&
                                parseXmlContent(content.getXml(), OpenimmoFeedback.class) != null)
                        .findFirst()
                        .ifPresent(content -> populateListingDetail(listingDetail,
                                parseXmlContent(content.getXml(), OpenimmoFeedback.class).getObjekt()
                                        .getInteressent()));
            }

            // parse html content if xml is not present
            if (listingDetail.getEmail() == null) {
                contents.stream()
                        .filter(content -> content.getHtml() != null)
                        .findFirst()
                        .ifPresent(content -> parseHtmlContent(listingDetail, content.getHtml()));
            }

            return listingDetail;
        } catch (Exception e1) {
            throw new MailParseException(e1);
        }
    }

    @Override
    protected String extractText(InputStream inputStream) throws IOException {
        return IOUtils.toString(inputStream, StandardCharsets.ISO_8859_1);
    }

    private String getPropertyIdFromSubject(String subject) {
        String propertyId = null;
        if (subject != null) {
            if (subject.contains(SUBJECT_REF_NR) && subject.contains(":")) {
                propertyId = subject.substring(subject.indexOf(SUBJECT_REF_NR) + SUBJECT_REF_NR.length() + 1,
                        subject.indexOf(":"));
            } else {
                log.error("unable to parse objectId from email subjet: " + subject);
            }
        }

        return propertyId;
    }

    private <T> T parseXmlContent(String content, Class<T> clazz) {
        try {
            JAXBContext jc = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            StringReader reader = new StringReader(content);
            return (T) unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            return null;
        }
    }

    private void parseHtmlContent(ListingDetail listingDetail, String htmlContent) {
        Document document = Jsoup.parse(htmlContent);
        document.charset(StandardCharsets.ISO_8859_1);
        String fullSalutation = getTextForReference(document, NAME);
        String firstName = "", name = "";
        if (fullSalutation != null) {
            final String[] salutation = fullSalutation.split(" ");
            if (salutation.length == 3) {
                firstName = salutation[1].trim();
                name = salutation[2].trim();
            } else {
                firstName = salutation[0].trim();
                name = salutation[1].trim();
            }
        }

        listingDetail.setFirstName(firstName);
        listingDetail.setName(name);
        listingDetail.setTelephone(getTextForReference(document, TELEFON));
        listingDetail.setEmail(getAnchorTextForReference(document));
        listingDetail.setText(getTextForReference(document, NACHRICHT));
    }

    private void populateListingDetail(ListingDetail listingDetail, Objektanfrage objektanfrage) {
        String brokerNumber = objektanfrage.getObjekt().getMaklerNummer();
        if (!StringUtils.isBlank(brokerNumber) && StringUtils.isBlank(listingDetail.getListingId())) {
            listingDetail.setListingId(brokerNumber);
        }

        populateListingDetail(listingDetail, objektanfrage.getInteressent());
    }

    private void populateListingDetail(ListingDetail listingDetail, Interessent interest) {
        listingDetail.setFirstName(interest.getVorname());
        listingDetail.setName(interest.getName());
        listingDetail.setTelephone(interest.getTel());
        listingDetail.setEmail(interest.getEmail());
        listingDetail.setText(interest.getInfo());
    }

    private void populateListingDetail(ListingDetail listingDetail,
                                       OpenimmoFeedback.OpenimmoObjekt.OpenimmoInteressent interest) {
        listingDetail.setFirstName(interest.getVorname());
        listingDetail.setName(interest.getNachname());
        listingDetail.setTelephone(interest.getTel());
        listingDetail.setEmail(interest.getEmail());
        listingDetail.setText(interest.getAnfrage());
    }

    private String getTextForReference(Document document, String ref) {
        return doGetText(document, ref, false);
    }

    private String getAnchorTextForReference(Document document) {
        return doGetText(document, E_MAIL, true);
    }

    private String doGetText(Document document, String ref, boolean isAnchor) {
        final Elements select = document.select("td:containsOwn(" + ref + ":)");
        if (!select.isEmpty() && select.get(0) != null) {
            final Element targetTd = select.get(0) // target field
                    .parent().parent().parent() // table element
                    .nextElementSibling() // next table
                    .getElementsByTag("td").get(2); // navigate to target td
            if (isAnchor) {
                return targetTd.getElementsByTag("a").text();
            }
            return targetTd.text();
        } else {
            return null;
        }
    }

}
