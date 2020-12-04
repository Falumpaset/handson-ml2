package de.immomio.broker.service.property.tenant;

import de.immomio.data.base.type.property.TenantSelectType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.settings.LandlordCustomerSettings;
import de.immomio.data.shared.entity.property.tenant.PropertyTenant;
import de.immomio.exporter.openimmo.feedback.ImmomioToOpenimmoFeedback;
import de.immomio.exporter.openimmo.feedback.OpenimmoFeedback;
import de.immomio.mail.sender.LandlordMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AareonTenantSelect extends AbstractTenantSelect {

    private static final String FEEDBACK = "feedback";
    private static final String DOT_XML = ".xml";
    private static final String APPLICATION_FEEDBACK_SUBJECT = "application.feedback.subject";
    private static final String FEEDBACK_FILE = "feedback.xml";

    private final ImmomioToOpenimmoFeedback immomioToOpenimmoFeedback;

    private final LandlordMailSender landlordMailSender;

    @Autowired
    public AareonTenantSelect(
            ImmomioToOpenimmoFeedback immomioToOpenimmoFeedback,
            LandlordMailSender landlordMailSender
    ) {
        super(TenantSelectType.AAREON);

        this.immomioToOpenimmoFeedback = immomioToOpenimmoFeedback;
        this.landlordMailSender = landlordMailSender;
    }

    @Override
    public boolean run(PropertyTenant propertyTenant) {
        if (!validate(propertyTenant)) {
            return false;
        }

        try {
            OpenimmoFeedback openimmoFeedback = immomioToOpenimmoFeedback.convert(propertyTenant);
            File feedbackFile = File.createTempFile(FEEDBACK, DOT_XML);
            LandlordCustomer landlordCustomer = propertyTenant.getProperty().getCustomer();

            try (OutputStream os = new FileOutputStream(feedbackFile)) {
                Marshaller marshaller = initializeMarshaller();
                marshaller.marshal(openimmoFeedback, os);
                sendMessage(landlordCustomer.getCustomerSettings().getAareonEmail(), feedbackFile);
            } catch (Exception e) {
                log.error("Error sending feedback mail ...", e);
                return false;
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return false;
        }

        return true;
    }

    private boolean validate(PropertyTenant propertyTenant) {
        if (propertyTenant == null) {
            log.error("Error sending aareon-feedbackmail because propertyTenant is null");
            return false;
        } else if (propertyTenant.getProperty() == null) {
            log.error("Error sending aareon-feedbackmail because property is null [" + propertyTenant.getId() + "]");
            return false;
        } else if (propertyTenant.getUserProfile() == null) {
            log.error("Error sending aareon-feedbackmail because user is null [" + propertyTenant.getId() + "]");
            return false;
        }

        LandlordCustomerSettings customerSettings = propertyTenant.getProperty().getCustomer().getCustomerSettings();
        if (customerSettings == null || StringUtils.isEmpty(customerSettings.getAareonEmail())) {
            log.error("Error sending aareon-feedbackmail because email is null [" + propertyTenant.getId() + "]");
            return false;
        }

        return true;
    }

    private void sendMessage(String aareonEmail, File feedbackFile) {
        Map<String, Object> data = new HashMap<>();
        Map<String, String> attachments = new HashMap<>();
        attachments.put(FEEDBACK_FILE, feedbackFile.getAbsolutePath());

        landlordMailSender.send(aareonEmail, MailTemplate.APPLICATION_FEEDBACK, APPLICATION_FEEDBACK_SUBJECT, data,
                attachments, true);
    }

    private Marshaller initializeMarshaller() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(OpenimmoFeedback.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        return marshaller;
    }
}
