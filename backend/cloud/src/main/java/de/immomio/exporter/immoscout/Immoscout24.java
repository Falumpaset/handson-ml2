package de.immomio.exporter.immoscout;

import de.immobilienscout24.rest.schema.common._1.Attachment;
import de.immobilienscout24.rest.schema.common._1.RealtorContactDetails;
import de.immobilienscout24.rest.schema.offer.realestates._1.RealEstate;
import de.immomio.data.base.type.common.FileType;
import de.is24.rest.api.export.api.Is24Api;
import de.is24.rest.api.export.api.ObjectApi.PublishChannel;
import de.is24.rest.api.export.api.impl.FloorplanMultimediaObject;
import de.is24.rest.api.export.api.impl.IS24ApiImpl;
import de.is24.rest.api.export.api.impl.PdfMultimediaObject;
import de.is24.rest.api.export.api.impl.PictureMultimediaObject;
import de.is24.rest.api.export.api.impl.VideoMultimediaObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;
import java.util.List;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 */
@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Immoscout24 {

    private Is24Api isAIs24Api;

    @Value("${immoscout.endpoint}")
    private String ENDPOINT;

    @Value("${immoscout.consumer.key}")
    private String CONSUMER_KEY;

    @Value("${immoscout.secret.key}")
    private String CONSUMER_SECRET;

    private long channel;

    public static String[] parseId(String result) {
        return StringUtils.substringsBetween(result, "<id>", "</id>");
    }

    public void init(long channel) {
        this.channel = channel;

        String endpoint = ENDPOINT + "/restapi/api";
        isAIs24Api = new IS24ApiImpl();
        isAIs24Api.init(CONSUMER_KEY, CONSUMER_SECRET, endpoint);
    }

    public void login(String token, String tokenSecret) {
        isAIs24Api.signIn(token, tokenSecret);

        try {
            log.info("avaiable Channels: " + isAIs24Api.getPublishChannels().size());
            for (PublishChannel pc : isAIs24Api.getPublishChannels()) {
                log.info("avaiable Channel: " + pc.getTitle() + " (" + pc.getId() + ")");
            }
        } catch (Exception e) {
            log.info("getPublishChannels logging ...", e);
        }
    }

    public String createContact(RealtorContactDetails realtorContactDetails) {
        String result = isAIs24Api.createContact(realtorContactDetails);

        Assert.notNull(result, "result may not be null");

        return result;
    }

    public List<RealtorContactDetails> loadContact() {
        return isAIs24Api.getContacts();
    }

    public RealtorContactDetails findByFirstNameAndLastNameAndEmail(String firstName, String lastName, String email) {
        for (RealtorContactDetails contact : loadContact()) {
            if (contact.getFirstname() != null && contact.getFirstname().equals(firstName) &&
                    contact.getLastname() != null && contact.getLastname().equals(lastName) &&
                    contact.getEmail() != null && contact.getEmail().equals(email)) {
                return contact;
            }
        }
        return null;
    }

    public RealEstate getRealEstate(Long objectId) {
        ImmomioObjectId id = new ImmomioObjectId(objectId.toString());

        try {
            return isAIs24Api.getRealestate(id.getId());
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    public String createRealEstate(RealEstate realEstate) {
        String result = isAIs24Api.createRealestate(realEstate);

        isAIs24Api.publishRealestate(result, channel + "");

        Assert.notNull(result, "result may not be null");

        return result;
    }

    public void deactivateRealEstate(RealEstate realEstate) {
        realEstate = this.getRealEstate(Long.parseLong(realEstate.getExternalId()));

        String id = realEstate.getId().toString();

        try {
            isAIs24Api.deactivateRealestate(id, channel + "");
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();

            sb.append("Error deactivating realestate -> IS24 id: ");
            sb.append(id);
            sb.append(" / ");
            sb.append(realEstate.getExternalId());
            sb.append(" channel: ");
            sb.append(channel);
            sb.append("\n");

            log.error(sb.toString(), e);
        }
    }

    public void publishRealEstate(RealEstate realEstate) {
        try {
            isAIs24Api.publishRealestate(realEstate.getId().toString(), channel + "");
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();

            sb.append("Error publishing realestate -> IS24 id: ");
            sb.append(realEstate.getId().toString());
            sb.append(" channel: ");
            sb.append(channel);
            sb.append(" externalID: ");
            sb.append(realEstate.getExternalId());
            sb.append("\n");

            log.error(sb.toString(), e);
        }
    }

    public String updateRealEstate(RealEstate realEstate) {
        ImmomioObjectId id = new ImmomioObjectId(realEstate.getExternalId());

        return isAIs24Api.updateRealestate(id, realEstate);
    }

    public void deleteRealEstate(RealEstate realEstate) {
        Long realEstateProjectId = realEstate.getRealEstateProjectId();
        ImmomioObjectId id = new ImmomioObjectId(realEstate.getExternalId());

        isAIs24Api.deleteRealestateFromRealestateProject(String.valueOf(realEstateProjectId), id);
    }

    public void addAttachment(String realEstateId, String title, FileType type, File file, boolean isPrimary) {
        if (type == null) {
            return;
        }

        if (title != null && title.length() > 30) {
            title = title.substring(0, 29);
        }

        switch (type) {
            case IMG:
                PictureMultimediaObject pictureMultimediaObject = new PictureMultimediaObject(title, file, false,
                        isPrimary);
                isAIs24Api.addAttachmentToRealestate(realEstateId, pictureMultimediaObject);
                break;
            case FLOOR_PLAN:
                FloorplanMultimediaObject floorplanMultimediaObject = new PdfMultimediaObject(title, file, true);
                isAIs24Api.addAttachmentToRealestate(realEstateId, floorplanMultimediaObject);
                break;
            case VIDEO:
                VideoMultimediaObject videoMultimediaObject = new VideoMultimediaObject(title, file);
                isAIs24Api.addAttachmentToRealestate(realEstateId, videoMultimediaObject);
                break;
            case PDF:
            case INCOME_STATEMENT:
            case PROOF_OF_PAYMENT:
            case SELF_ASSESSMENT:
            case WB_CERTIFICATE:
            case CREDIT_REPORT:
            case ENERGY_CERTIFICATE:
                PdfMultimediaObject pdfMultimediaObject = new PdfMultimediaObject(title, file, false);
                isAIs24Api.addAttachmentToRealestate(realEstateId, pdfMultimediaObject);
            default:
                break;
        }
    }

    public void deleteAttachment(RealEstate realEstate, Attachment attachment) {
        isAIs24Api.deleteAttachment(realEstate.getId().toString(), attachment.getId().toString());
    }
}
