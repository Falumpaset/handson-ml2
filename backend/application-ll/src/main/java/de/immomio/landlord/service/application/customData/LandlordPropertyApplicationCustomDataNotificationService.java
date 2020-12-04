package de.immomio.landlord.service.application.customData;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.mail.sender.LandlordMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.service.shared.EmailModelProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
public class LandlordPropertyApplicationCustomDataNotificationService {

    private static final String CUSTOM_DATA_MODAL_SUBJECT = "custom_data_modal.subject";

    private final LandlordMailSender landlordMailSender;
    private final EmailModelProvider modelProvider;

    @Autowired
    public LandlordPropertyApplicationCustomDataNotificationService(LandlordMailSender landlordMailSender,
            EmailModelProvider modelProvider) {
        this.landlordMailSender = landlordMailSender;
        this.modelProvider = modelProvider;
    }

    public void sendCustomDataFile(LandlordUser user, Property property, String email, File fileToSend) {
        Map<String, Object> model = new HashMap<>();
        modelProvider.appendUser(model, user);
        modelProvider.appendProperty(model, property);

        Map<String, String> attachments = new HashMap<>();
        attachments.put(fileToSend.getName(), fileToSend.getAbsolutePath());

        landlordMailSender.send(email, MailTemplate.CUSTOM_DATA_MODAL_EXCEL,
                CUSTOM_DATA_MODAL_SUBJECT, model, attachments, true);
    }
}
