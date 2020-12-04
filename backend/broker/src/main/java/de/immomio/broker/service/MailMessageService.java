package de.immomio.broker.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.immomio.common.ical.ICalEventHandler;
import de.immomio.data.landlord.bean.property.PropertyMailBean;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.data.shared.entity.appointment.AppointmentBean;
import de.immomio.mail.ModelParams;
import de.immomio.mail.configurator.PropertySearcherMailConfigurator;
import de.immomio.mail.s3.MailPropertySearcherS3FileManager;
import de.immomio.mail.sender.LandlordMailSender;
import de.immomio.mail.sender.PropertySearcherMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.messaging.container.mail.LandlordMailBrokerContainer;
import de.immomio.messaging.container.mail.PropertySearcherProfileMailBrokerContainer;
import de.immomio.model.repository.core.landlord.customer.user.BaseLandlordUserRepository;
import de.immomio.model.repository.core.propertysearcher.user.BasePropertySearcherUserProfileRepository;
import de.immomio.utils.FileStorageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MailMessageService {

    private final LandlordMailSender landlordMailSender;
    private final PropertySearcherMailSender propertySearcherMailSender;
    private final BaseLandlordUserRepository baseLandlordUserRepository;
    private final BasePropertySearcherUserProfileRepository basePropertySearcherUserProfileRepository;
    private final PropertySearcherMailConfigurator mailConfigurator;
    private final MailPropertySearcherS3FileManager mailPropertySearcherS3FileManager;
    @Value("${appointment.durationInMinutes}")
    private int durationInMinutes;

    @Autowired
    public MailMessageService(LandlordMailSender landlordMailSender,
            PropertySearcherMailSender propertySearcherMailSender,
            BaseLandlordUserRepository baseLandlordUserRepository,
            BasePropertySearcherUserProfileRepository basePropertySearcherUserProfileRepository,
            PropertySearcherMailConfigurator mailConfigurator,
            MailPropertySearcherS3FileManager mailPropertySearcherS3FileManager) {
        this.landlordMailSender = landlordMailSender;
        this.propertySearcherMailSender = propertySearcherMailSender;
        this.baseLandlordUserRepository = baseLandlordUserRepository;
        this.basePropertySearcherUserProfileRepository = basePropertySearcherUserProfileRepository;
        this.mailConfigurator = mailConfigurator;
        this.mailPropertySearcherS3FileManager = mailPropertySearcherS3FileManager;
    }

    public void onMessage(LandlordMailBrokerContainer container) {
        Map<String, String> attachments = populateAttachments(container);
        if (! attachments.isEmpty()) {
            container.getAttachments().putAll(attachments);
        }

        Long userId = container.getUserId();
        if (userId != null) {
            baseLandlordUserRepository.findById(userId).ifPresentOrElse(user -> {
                landlordMailSender.send(user, container.getTemplate(), container.getSubject(),
                        container.getSubjectFormat(), container.getData(), container.getAttachments());
            }, () -> log.warn("User with ID={} not found", userId));

        } else {
            landlordMailSender.send(container.getEmail(), container.getTemplate(), container.getSubject(),
                    container.getSubjectFormat(), container.getData(), container.getAttachments());
        }
    }

    public void onMessage(PropertySearcherProfileMailBrokerContainer container) {
        String subject = container.getSubject();
        Object[] subjectFormat = container.getSubjectFormat();
        MailTemplate template = container.getTemplate();
        PropertySearcherUserProfile userProfile = null;
        Long customizerCustomerId = container.getCustomerId();
        Map<String, S3File> s3FileAttachments = container.getAttachments();
        container.getData().put(ModelParams.RETURN_URL, mailConfigurator.buildAppUrl());
        String fromEmail = container.getFromEmail();

        if (container.getUserProfileId() != null) {
            userProfile = basePropertySearcherUserProfileRepository.findById(container.getUserProfileId()).orElse(null);
        }

        Map<String, String> attachments = downloadS3FileAttachments(s3FileAttachments);

        if (userProfile != null && customizerCustomerId != null && fromEmail != null) {
            propertySearcherMailSender.send(userProfile.getEmail(), fromEmail, template, subject, container.getData(),
                    attachments, true);
            return;
        }

        if (userProfile != null && customizerCustomerId != null) {
            propertySearcherMailSender.send(userProfile, template, subject, subjectFormat, container.getData(),
                    customizerCustomerId, attachments);
            return;
        }

        if (userProfile != null) {
            propertySearcherMailSender.send(userProfile, template, subject, subjectFormat, container.getData(), attachments);
            return;
        }

        if (customizerCustomerId != null) {
            propertySearcherMailSender.send(container.getEmail(), template, subject, subjectFormat, container.getData(),
                    customizerCustomerId, attachments);
            return;
        }

        propertySearcherMailSender.send(container.getEmail(), template, subject, subjectFormat, container.getData(),
                attachments);
    }

    private Map<String, String> populateAttachments(LandlordMailBrokerContainer container) {
        Map<String, String> attachments = new HashMap<>();

        try {
            switch (container.getTemplate()) {
                case INVITATION_ACCEPTANCE_NOTIFICATION:
                    populateIcsAttachment(container, attachments);
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return attachments;
    }

    private void populateIcsAttachment(LandlordMailBrokerContainer container, Map<String, String> attachments) throws
            IOException {
        ICalEventHandler iCalEventHandler = new ICalEventHandler();

        ObjectMapper objectMapper = createObjectMapper();
        Map<String, Object> data = container.getData();
        PropertyMailBean propertyMailBean = extractPropertyBean(data, objectMapper);
        AppointmentBean appointmentBean = extractAppointmentBean(data, objectMapper);

        File file = iCalEventHandler.createEvent(appointmentBean.getId().toString(), ICalEventHandler.ICAL_TITLE,
                propertyMailBean.getData().getAddress().toString(), ICalEventHandler.LANGUAGE, appointmentBean.getDate(),
                durationInMinutes);
        attachments.put(ICalEventHandler.INVITE_ICS_FILE_NAME, file.getAbsolutePath());
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        return objectMapper;
    }

    private PropertyMailBean extractPropertyBean(Map<String, Object> data, ObjectMapper objectMapper) {
        return objectMapper.convertValue(data.get(ModelParams.MODEL_FLAT), PropertyMailBean.class);
    }

    private AppointmentBean extractAppointmentBean(Map<String, Object> data, ObjectMapper objectMapper) {
        return objectMapper.convertValue(data.get(ModelParams.MODEL_APPOINTMENT), AppointmentBean.class);
    }

    private Map<String, String> downloadS3FileAttachments(Map<String, S3File> attachments) {
        return attachments.entrySet().stream().map((attachment) -> {
            try {
                return FileStorageUtils.downloadFile(attachment.getValue().getUrl(), mailPropertySearcherS3FileManager,
                        false, "");
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toMap(File::getName, File::getPath));
    }
}
