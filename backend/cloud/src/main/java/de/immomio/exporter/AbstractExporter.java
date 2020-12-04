package de.immomio.exporter;

import com.google.common.io.Files;
import de.immomio.cloud.service.url.UrlService;
import de.immomio.common.amazon.s3.AbstractS3FileManager;
import de.immomio.config.ApplicationMessageSource;
import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.landlord.entity.credential.Credential;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.exporter.exception.ExporterException;
import de.immomio.service.shortUrl.ShortUrlService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Johannes Hiemer, Bastian Bliemeister
 */

@Slf4j
@Scope("prototype")
public abstract class AbstractExporter {

    private static final String EXPORT_SHORT_DESCRIPTION_DISCLAIMER = "export.shortDescription.disclaimer";
    private static final String EXPORT_SHORT_DESCRIPTION_EXTERNAL_ID_LABEL = "export.shortDescription.externalId.label";
    private static final String EXPORT_SHORT_DESCRIPTION_TEMPLATE = "export.shortDescription.template";
    private static final String EXPORT_MISCELLANEOUS_TEXT_TEMPLATE = "export.miscellaneousText.template";

    private final Portal PORTAL;

    protected Locale locale = Locale.GERMAN;

    @Autowired
    protected ApplicationMessageSource messageSource;

    @Value("${email.contact}")
    private String contactEmail;

    @Autowired
    private ShortUrlService shortUrlService;

    @Autowired
    private UrlService urlService;

    protected AbstractExporter(Portal portal) {
        PORTAL = portal;
    }

    public abstract boolean activate(
            Property property,
            Credential credential
    ) throws ExporterException;

    public abstract boolean update(
            Property property,
            Credential credential
    ) throws ExporterException;

    public abstract boolean deactivate(
            Property property,
            Credential credential
    ) throws ExporterException;

    public abstract boolean delete(
            Property property,
            Credential credential
    ) throws ExporterException;

    public abstract boolean checkConnection(Credential credential);

    public List<Portal> getReleasedPortals() {
        List<Portal> list = new ArrayList<>(1);

        list.add(PORTAL);

        return list;
    }

    public Portal getPortal() {
        return PORTAL;
    }

    public boolean isApplicable(Portal portal) {
        log.debug("compare exporter " + PORTAL + " and " + portal);
        return PORTAL == portal;
    }

    public URL getApplicationUrl(Property property) {
        Map<String, String> properties = new HashMap<>();
        properties.put("portal", getPortal().name());
        properties.put("flatId", property.getId().toString());
        properties.put("customerId", property.getCustomer().getId() + "");

        Date expires = new DateTime().plusMonths(3).toDate();

        return shortUrlService.createTenantUrl(urlService.getApplicationLink(property.getId()), properties, expires,
                true);
    }

    public String getObjectDescriptionWithDisclaimer(Property property) {
        String externalId = property.getExternalId();

        String description = property.getData().getObjectDescription();

        if (!StringUtils.isBlank(externalId)) {
            String externalIdLabel = messageSource.getMessage(EXPORT_SHORT_DESCRIPTION_EXTERNAL_ID_LABEL, new Object[]{externalId}, locale);
            description = externalIdLabel + StringUtils.trimToEmpty(description);
        }

        return messageSource.getMessage(EXPORT_SHORT_DESCRIPTION_TEMPLATE, new Object[] {description != null ? description : ""}, locale);
    }

    public String getObjectMiscellaneousTextWithApplicationLink(Property property) {
        String miscellaneousText = property.getData().getObjectMiscellaneousText();
        String disclaimer = messageSource.getMessage(EXPORT_SHORT_DESCRIPTION_DISCLAIMER, new Object[0], locale);
        URL url = getApplicationUrl(property);
        String link = url.toString();

        // IVD24, IMMONET_DE portals cut all links from the text
        if (PORTAL == Portal.IVD || PORTAL == Portal.IMMONET_DE) {
            link = link.replace("http://", "").replace("https://", "");
        }

        return messageSource.getMessage(EXPORT_MISCELLANEOUS_TEXT_TEMPLATE, new Object[] {miscellaneousText, disclaimer, link}, locale);
    }


    protected abstract <T extends AbstractS3FileManager> T getS3FileManager();

    protected void downloadAttachments(Property property, File folder) {
        log.info("Downloading attachments ");

        List<S3File> attachments = new ArrayList<>();
        if (property.getData().getAttachments() != null && !property.getData().getAttachments().isEmpty()) {
            attachments.addAll(property.getData().getAttachments());
        }

        if (property.getData().getDocuments() != null && !property.getData().getDocuments().isEmpty()) {
            attachments.addAll(property.getData().getDocuments());
        }

        getS3FileManager().downloadFiles(attachments, folder);
    }

    protected File getTempDir() {
        return Files.createTempDir();
    }

    protected String getDefaultContactEmail() {
        return contactEmail;
    }
}
