package de.immomio.broker.service;

import de.immomio.data.landlord.bean.property.PropertyMailBean;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosure;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileEmailBean;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.selfdisclosure.SelfDisclosureResponse;
import de.immomio.exporter.openimmo.feedback.ImmomioToOpenimmoFeedback;
import de.immomio.exporter.openimmo.feedback.OpenimmoFeedback;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.LandlordMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.model.repository.core.shared.selfdisclosure.BaseSelfDisclosureResponseRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class SelfDisclosureService {

    private static final String FEEDBACK = "feedback";

    private static final String SELF_DISCLOSURE_RESPONSE_REPORT = "self-disclosure-response";

    private static final String DOT_XML = ".xml";

    private static final String DOT_PDF = ".pdf";

    private static final String FEEDBACK_FILE = "feedback.xml";

    private static final String SELF_DISCLOSURE_RESPONSE_REPORT_FILE = SELF_DISCLOSURE_RESPONSE_REPORT + DOT_PDF;

    private static final String SELF_DISCLOSURE_FEEDBACK_SUBJECT = "selfDisclosure.feedback.subject";

    private final ImmomioToOpenimmoFeedback immomioToOpenimmoFeedback;

    private final LandlordMailSender landlordMailSender;

    private final BaseSelfDisclosureResponseRepository selfDisclosureResponseRepository;

    private final SelfDisclosureReportService selfDisclosureReportService;

    @Value("${selfDisclosure.feedbackSenderEmail}")
    private String selfDisclosureFeedbackSenderEmail;

    @Autowired
    public SelfDisclosureService(ImmomioToOpenimmoFeedback immomioToOpenimmoFeedback,
            LandlordMailSender landlordMailSender,
            BaseSelfDisclosureResponseRepository selfDisclosureResponseRepository,
            SelfDisclosureReportService selfDisclosureReportService) {
        this.immomioToOpenimmoFeedback = immomioToOpenimmoFeedback;
        this.landlordMailSender = landlordMailSender;
        this.selfDisclosureResponseRepository = selfDisclosureResponseRepository;
        this.selfDisclosureReportService = selfDisclosureReportService;
    }

    public void sendResponseFeedbackEmail(Long responseId, String email) {
        selfDisclosureResponseRepository.findById(responseId).ifPresentOrElse(selfDisclosureResponse -> {
            PropertySearcherUserProfile userProfile = selfDisclosureResponse.getUserProfile();
            Property property = selfDisclosureResponse.getProperty();

            OpenimmoFeedback openimmoFeedback = immomioToOpenimmoFeedback.convert(property, userProfile);
            try {
                File feedbackFile = File.createTempFile(FEEDBACK, DOT_XML);

                try (OutputStream os = new FileOutputStream(feedbackFile)) {
                    Marshaller marshaller = initializeMarshaller();
                    marshaller.marshal(openimmoFeedback, os);

                    File selfDisclosureResponseReport = generateSelfDisclosureResponseReport(selfDisclosureResponse);
                    sendMessage(email, property, userProfile, feedbackFile, selfDisclosureResponseReport);
                } catch (Exception e) {
                    log.error("Error sending feedback mail ...", e);
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }, () -> log.error("selfDisclosureResponse with ID '{}' not found", responseId));
    }

    public void sendSelfDisclosureFeedbackEmail(Property property, PropertySearcherUserProfile userProfile) {
        selfDisclosureResponseRepository.findByPropertyIdAndUserProfileId(property.getId(), userProfile.getId()).ifPresent(this::sendSelfDisclosureResponseMessage);
    }

    private void sendSelfDisclosureResponseMessage(SelfDisclosureResponse response) {
        SelfDisclosure selfDisclosure = response.getSelfDisclosure();
        String feedbackEmail = selfDisclosure.getFeedbackEmail();
        if (StringUtils.isNotEmpty(feedbackEmail)) {
            sendResponseFeedbackEmail(response.getId(), feedbackEmail);
        } else {
            log.warn("Feedback email with XML file has not been sent because `feedbackEmail` is not provided.");
        }
    }

    private File generateSelfDisclosureResponseReport(SelfDisclosureResponse selfDisclosureResponse) {
        File reportFile = null;
        try (ByteArrayOutputStream outputStream = (ByteArrayOutputStream) selfDisclosureReportService.generateReport(selfDisclosureResponse)) {
            if (outputStream != null) {
                reportFile = File.createTempFile(SELF_DISCLOSURE_RESPONSE_REPORT, DOT_PDF);
                outputStream.writeTo(new FileOutputStream(reportFile));
            }
        } catch (Exception e) {
            log.error("Error generating self disclosure response report ...", e);
        }

        return reportFile;
    }

    private void sendMessage(String email, Property property, PropertySearcherUserProfile userProfile, File feedbackFile, File selfDisclosureReport) {
        Map<String, Object> data = new HashMap<>();
        data.put(ModelParams.MODEL_USER, new PropertySearcherUserProfileEmailBean(userProfile));
        data.put(ModelParams.MODEL_USER_PROFILE, userProfile.getData());
        data.put(ModelParams.MODEL_FLAT, new PropertyMailBean(property));

        Map<String, String> attachments = new HashMap<>();
        attachments.put(FEEDBACK_FILE, feedbackFile.getAbsolutePath());

        if (selfDisclosureReport != null) {
            attachments.put(SELF_DISCLOSURE_RESPONSE_REPORT_FILE, selfDisclosureReport.getAbsolutePath());
        }

        landlordMailSender.send(email, selfDisclosureFeedbackSenderEmail, MailTemplate.SELF_DISCLOSURE_FEEDBACK_EMAIL, SELF_DISCLOSURE_FEEDBACK_SUBJECT, data, attachments, true);
    }

    private Marshaller initializeMarshaller() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(OpenimmoFeedback.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        return marshaller;
    }
}
