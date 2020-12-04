/**
 *
 */
package de.immomio.exporter.immoscout;

import de.immobilienscout24.rest.schema.common._1.Attachments;
import de.immobilienscout24.rest.schema.common._1.PublishChannel;
import de.immobilienscout24.rest.schema.common._1.RealtorContactDetails;
import de.immobilienscout24.rest.schema.offer.realestates._1.RealEstate;
import de.immomio.common.amazon.s3.AbstractS3;
import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.landlord.bean.property.Contact;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.credential.Credential;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.profile.LandlordUserProfile;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.exporter.AbstractExporter;
import de.immomio.exporter.exception.CredentialNotFoundException;
import de.immomio.exporter.exception.ExporterException;
import de.immomio.exporter.immoscout.converter.ImmomioToImmoscoutConverter;
import de.immomio.exporter.utils.credential.CredentialUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister.
 */

@Slf4j
public abstract class ImmoscoutExporter extends AbstractExporter {

    @Autowired
    private Immoscout24 immoscout24;

    @Value("${encryption.credentials.key}")
    private String encryptionKey;

    protected ImmoscoutExporter(Portal portal) {
        super(portal);
    }

    private static boolean isPublished(long channel, RealEstate realEstate) {
        boolean published = false;

        log.info("Checking published channel: " + channel);
        for (PublishChannel pc : realEstate.getPublishChannels().getPublishChannel()) {
            log.info("Found channel: " + pc.getId());
            if (pc.getId() != null && pc.getId().equals(channel)) {
                published = true;
                break;
            }
        }

        if (published) {
            log.info("Published on channel: " + channel);
        } else {
            log.info("Not published on channel: " + channel);
        }

        return published;
    }

    @Override
    public boolean activate(Property property, Credential credential) throws ExporterException {
        log.info("Starting export to immoscout for: " + property);
        try {
            immoscout24 = immoscout(credential);

            RealEstate realEstate = convertRealEstate(property);
            realEstate.setContact(setRealEstateContact(property, property.getData().getContact()));
            RealEstate existingRealEstate = immoscout24.getRealEstate(property.getId());

            String result;
            if (existingRealEstate != null) {
                this.removeAttachments(existingRealEstate);

                boolean published = isPublished(getChannel(), existingRealEstate);
                immoscout24.updateRealEstate(realEstate);
                result = existingRealEstate.getId().toString();

                if (!published) {
                    immoscout24.publishRealEstate(existingRealEstate);
                }
            } else {
                result = immoscout24.createRealEstate(realEstate);
            }

            Assert.notNull(result, "Result of real estate creation may not be null");
            log.info("Proceeding with IS24-ID: " + result);

            this.addAttachments(result, property);
            log.info("Export result for immoscout: " + result);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ExporterException(getPortal().name() + " -> threw Exception during export", e);
        }

        return true;
    }

    @Override
    public boolean update(Property property, Credential credential) throws ExporterException {
        log.info("Starting update to immoscout for: " + property);

        try {
            immoscout24 = immoscout(credential);

            Assert.notNull(immoscout24, "Immoscout24 client may not be null");

            RealEstate realEstate = convertRealEstate(property);
            realEstate.setContact(setRealEstateContact(property, property.getData().getContact()));

            RealEstate existingRealEstate = immoscout24.getRealEstate(property.getId());
            String result;
            if (existingRealEstate != null) {
                this.removeAttachments(existingRealEstate);

                boolean published = isPublished(getChannel(), existingRealEstate);

                immoscout24.updateRealEstate(realEstate);
                result = existingRealEstate.getId().toString();

                if (!published) {
                    immoscout24.publishRealEstate(existingRealEstate);
                }
            } else {
                result = immoscout24.createRealEstate(realEstate);
            }

            Assert.notNull(result, "Result of real estate creation may not be null");
            log.info("Proceeding with IS24-ID: " + result);

            this.addAttachments(result, property);

            log.info("Export result for immoscout: " + result);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ExporterException(getPortal().name() + " -> threw Exception during export", e);
        }

        return true;
    }

    @Override
    public boolean deactivate(Property property, Credential credential) throws ExporterException {
        log.info("Starting deactivation to immoscout for: " + property);

        try {
            immoscout24 = immoscout(credential);
            RealEstate realEstate = convertRealEstate(property);
            RealEstate existingApartmentRent = immoscout24.getRealEstate(property.getId());

            if (existingApartmentRent != null) {
                this.removeAttachments(existingApartmentRent);
                immoscout24.deactivateRealEstate(realEstate);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ExporterException(getPortal().name() + " -> threw Exception during export", e);
        }

        return true;
    }

    @Override
    public boolean delete(Property property, Credential credential)
            throws ExporterException {
        log.info("Starting deletion to immoscout for: " + property);

        return deactivate(property, credential);
    }

    private RealEstate convertRealEstate(Property property) {
        PropertyData propertyData = property.getData();
        String description = getObjectDescriptionWithDisclaimer(property);
        String miscellaneousText = getObjectMiscellaneousTextWithApplicationLink(property);

        if (propertyData.getObjectType() != null) {
            switch (propertyData.getObjectType()) {
                case HOUSE:
                    return ImmomioToImmoscoutConverter.convertHouseRent(property, description, miscellaneousText);
                case GARAGE:
                    return ImmomioToImmoscoutConverter.convertGarageRent(property);
                case OFFICE:
                    return ImmomioToImmoscoutConverter.convertOffice(property);
            }
        }

        if (propertyData.isShortTermAccommodation()) {
            return ImmomioToImmoscoutConverter.convertShortTermAccommodation(property, description, miscellaneousText);
        }

        // default objecttype = flat
        return ImmomioToImmoscoutConverter.convertApartmentRent(property, description, miscellaneousText);
    }

    private void removeAttachments(RealEstate realEstate) {
        log.info("[Immoscout] Removing attachments from real estate");

        Attachments attachments = realEstate.getAttachments();
        if (attachments != null) {
            log.info("[Immoscout] Number of attachments to remove: " + attachments.getAttachment().size());
            for (de.immobilienscout24.rest.schema.common._1.Attachment attachment : attachments.getAttachment()) {
                immoscout24.deleteAttachment(realEstate, attachment);
            }
        }
    }

    private void addAttachments(String result, Property property) {
        log.info("[Immoscout] Adding attachment to apartment");

        File folder = this.getTempDir();

        this.downloadAttachments(property, folder);

        List<S3File> attachments = new ArrayList<>();
        if (property.getData().getAttachments() != null && property.getData().getAttachments().size() > 0) {
            attachments.addAll(property.getData().getAttachments());
        }

        if (property.getData().getDocuments() != null && property.getData().getDocuments().size() > 0) {
            attachments.addAll(property.getData().getDocuments());
        }

        final boolean[] isPrimary = {true};
        attachments.forEach(attachment -> {
            try {
                String title = attachment.getTitle();
                String name = attachment.getName();
                if (StringUtils.isEmpty(title) && StringUtils.isEmpty(name)) {
                    title = name;
                }
                immoscout24.addAttachment(
                        result,
                        title,
                        attachment.getType(),
                        new File(
                                folder.getAbsolutePath() + "/" +
                                        AbstractS3.concatIdentifier(
                                                attachment.getType(),
                                                attachment.getIdentifier(),
                                                attachment.getExtension())
                        ),
                        isPrimary[0]);
            } catch (Exception e) {
                log.error("Error adding file " + attachment.getUrl() + "from flat " + property, e);
                return;
            }
            isPrimary[0] = false;
        });

        try {
            FileUtils.deleteDirectory(folder);
        } catch (Exception e) {
            log.error("Error deleting folder [" + folder.getAbsolutePath() + "]" + "while exporting flat ["
                    + property + "]", e);
        }
    }

    protected Immoscout24 immoscout(Credential credential) throws CredentialNotFoundException {
        if (credential == null) {
            throw new CredentialNotFoundException("No credentials provided for endpoint");
        }

        String token = CredentialUtils.getToken(credential, encryptionKey);
        String tokenSecret = CredentialUtils.getTokenSecret(credential, encryptionKey);

        immoscout24.init(getChannel());
        immoscout24.login(token, tokenSecret);

        return immoscout24;
    }

    private RealEstate.Contact setRealEstateContact(Property property, Contact contact) {
        LandlordUserProfile userProfile = property.getUser().getProfile();

        if (checkForNameInContact(contact)) {
            return createIS24Contact(contact);
        }

        if (userProfile.getName() != null && userProfile.getFirstname() != null) {
            contact = new Contact(userProfile.getFirstname(), userProfile.getName(), getDefaultContactEmail());
            return createIS24Contact(contact);
        }

        contact = createDefaultContact();
        return createIS24Contact(contact);
    }

    private Contact createDefaultContact() {
        return new Contact("Immomio", "GmbH", getDefaultContactEmail());
    }

    private boolean checkForNameInContact(Contact contact) {
        return contact != null && contact.getName() != null && contact.getFirstName() != null;
    }

    private RealEstate.Contact createIS24Contact(Contact contact) {
        RealtorContactDetails realtorContactDetails = immoscout24.findByFirstNameAndLastNameAndEmail(
                contact.getFirstName(), contact.getName(), getDefaultContactEmail());

        if (realtorContactDetails == null) {
            immoscout24.createContact(ImmomioToImmoscoutConverter.convertContact(contact, getDefaultContactEmail()));
            realtorContactDetails = immoscout24.findByFirstNameAndLastNameAndEmail(
                    contact.getFirstName(),
                    contact.getName(),
                    getDefaultContactEmail());

            log.info("Contact did not exist, created a new one");
        }

        RealEstate.Contact is24Contact = new RealEstate.Contact();

        is24Contact.setId(realtorContactDetails.getId());
        return is24Contact;
    }

    protected abstract long getChannel();

    public boolean checkConnection(Credential credential) {
        if (credential == null || credential.getPortal() != getPortal()) {
            return false;
        }

        try {
            immoscout(credential);

            return true;
        } catch (CredentialNotFoundException e) {
            return false;
        }
    }
}
