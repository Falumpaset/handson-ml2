package de.immomio.recipient.handler;

import de.immomio.cloud.service.url.UrlService;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.shared.entity.email.ListingDetail;
import de.immomio.mail.sender.PropertySearcherMailSender;
import de.immomio.model.repository.core.landlord.customer.property.BasePropertyRepository;
import de.immomio.model.repository.core.propertysearcher.user.BasePropertySearcherUserRepository;
import de.immomio.recipient.application.ApplicationHandler;
import de.immomio.recipient.service.portal.parser.EmailParser;
import de.immomio.recipient.service.portal.parser.ebay.EbayEmailParser;
import de.immomio.recipient.service.portal.parser.immonet.ImmonetEmailParser;
import de.immomio.recipient.service.portal.parser.immoscout.ImmoscoutEmailParser;
import de.immomio.recipient.service.portal.parser.immowelt.ImmoweltEmailParser;
import de.immomio.recipient.service.portal.parser.ivd24.Ivd24EmailParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailParseException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Johannes Hiemer.
 */
@Slf4j
@Component
public class PortalEmailHandler implements MessageHandler {

    private final BasePropertyRepository propertyRepository;
    private final ApplicationHandler applicationHandler;
    private final UrlService urlService;
    @Value("${email.renter}")
    public String fromEmail;
    private final List<EmailParser> emailParsers = new ArrayList<>();

    @Autowired
    public PortalEmailHandler(
            BasePropertyRepository propertyRepository,
            ApplicationHandler applicationHandler,
            UrlService urlService
    ) {
        emailParsers.add(new ImmoscoutEmailParser());
        emailParsers.add(new EbayEmailParser());
        emailParsers.add(new ImmonetEmailParser());
        emailParsers.add(new ImmoweltEmailParser());
        emailParsers.add(new Ivd24EmailParser());
        this.propertyRepository = propertyRepository;
        this.applicationHandler = applicationHandler;
        this.urlService = urlService;
    }

    @Override
    public void handleMessage(Message message) {
        try {
            receive((MimeMessage) message.getPayload());
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void receive(MimeMessage message) throws MessagingException {
        String senderAddress = getSenderAddress(message);
        if (senderAddress == null) {
            log.error("No sender email address found");
            return;
        }

        EmailParser foundParser = emailParsers.stream().filter(emailParser -> emailParser.isApplicableSender(senderAddress)).findFirst().orElse(null);
        if (foundParser == null) {
            log.error("No valid parser found for sender address: {}.", senderAddress);
            return;
        }

        ListingDetail listingDetails = parseListingDetail(message, foundParser);
        if (listingDetails == null) {
            log.error("No listing details found");
            return;
        }
        if (listingDetails.getEmail() == null) {
            log.error("No email in listing details: {}.", listingDetails);
        }

        log.debug("Found listing details: " + listingDetails.toString());

        Property property = getProperty(listingDetails);
        if (property == null) {
            log.error(String.format("Could not find Property. Details were: %s.", listingDetails));
        }

        applicationHandler.applyUser(property, listingDetails, foundParser.getPortal());
    }

    private ListingDetail parseListingDetail(MimeMessage message, EmailParser parser) {
        try {
            return parser.parse(message);
        } catch (MailParseException e) {
            log.error("Error parsing E-Mail", e);
        }
        return null;
    }

    private String getSenderAddress(MimeMessage message) throws MessagingException {
        Address[] addresses = message.getFrom();
        InternetAddress sender = null;
        if (addresses.length > 0) {
            sender = (InternetAddress) addresses[0];
        }

        if (sender == null) {
            log.error("No sender address found in message {}", message);
            return null;
        }

        log.info("Received e-mail from:" + sender);
        return sender.getAddress();
    }

    private Property getProperty(ListingDetail listingDetails) {
        Long propertyId;
        Property property;
        String listingId = listingDetails.getListingId();
        try {
            propertyId = Long.parseLong(listingId);
            property = propertyRepository.findById(propertyId).orElse(null);
            if (property == null) {
                return getPropertyByExternalId(listingId);
            }
        } catch (NumberFormatException e) {
            return getPropertyByExternalId(listingId);
        }

        return property;
    }

    private Property getPropertyByExternalId(String listingId) {
        List<Property> properties = propertyRepository.findByExternalId(listingId.toLowerCase().trim());
        if (properties.size() == 1) {
            return properties.get(0);
        } else {
            log.error(String.format("no unique property with id %s found. " +
                            "Found %s properties",
                    listingId,
                    properties.size()));
            return null;
        }
    }
}
