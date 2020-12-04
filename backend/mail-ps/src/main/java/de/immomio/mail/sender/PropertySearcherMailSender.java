package de.immomio.mail.sender;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.propertysearcher.entity.customer.PropertySearcherCustomer;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.messaging.config.QueueConfigUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PropertySearcherMailSender extends AbstractMailSender {

    private final PropertySearcherAttachmentHandler propertySearcherAttachmentHandler;

    @Autowired
    public PropertySearcherMailSender(PropertySearcherAttachmentHandler propertySearcherAttachmentHandler, RabbitTemplate rabbitTemplate) {
        super(rabbitTemplate);
        this.propertySearcherAttachmentHandler = propertySearcherAttachmentHandler;
    }

    public void send(PropertySearcherUserProfile userProfile, MailTemplate template, String subject, Map<String, Object> data, Long customizerCustomerId) {
        PropertySearcherMessageBean messageBean = createMessageBean(userProfile.getEmail(), null, userProfile.getUser().getCustomer().getId(), userProfile.getId(), customizerCustomerId, data, subject,
                null, template);

        sendMessage(messageBean);
    }

    public void send(PropertySearcherUserProfile userProfile,
            MailTemplate template,
            String subject,
            Object[] subjectFormat,
            Map<String, Object> data,
            Long customizerCustomerId,
            Map<String, String> attachments) {
        PropertySearcherMessageBean messageBean = createMessageBeanWithAttachments(userProfile.getEmail(), userProfile.getUser().getCustomer().getId(), userProfile.getId(), customizerCustomerId, data,
                subject, subjectFormat, template, attachments);

        sendMessage(messageBean);
    }

    public void send(String email,
            MailTemplate template,
            String subject,
            Object[] subjectFormat,
            Map<String, Object> data,
            Long customizerCustomerId,
            Map<String, String> attachments) {
        PropertySearcherMessageBean messageBean = createMessageBeanWithAttachments(email, null, null, customizerCustomerId, data, subject, subjectFormat, template, attachments);

        sendMessage(messageBean);
    }

    public void send(PropertySearcherUserProfile userProfile, MailTemplate template, String subject, Map<String, Object> model) {
        send(userProfile.getEmail(), userProfile, userProfile.getUser().getCustomer(), template, subject, null, model, null, null, false);
    }

    public void send(PropertySearcherUserProfile userProfile, MailTemplate template, String subject, Map<String, Object> model, LandlordCustomer customizer) {
        PropertySearcherMessageBean messageBean = createMessageBeanWithAttachments(userProfile.getEmail(),
                userProfile.getUser().getCustomer().getId(), userProfile.getId(), customizer.getId(), model, subject, null, template, null);
        sendMessage(messageBean);
    }

    public void send(PropertySearcherUserProfile userProfile, MailTemplate template, String subject, Object[] subjectFormat, Map<String, Object> model, Map<String, String> attachments) {
        send(userProfile.getEmail(), userProfile, userProfile.getUser().getCustomer(), template, subject, subjectFormat, model, attachments, null, false);
    }

    public void send(String toEmail, MailTemplate template, String subject, Object[] subjectFormat, Map<String, Object> model, Map<String, String> attachments) {
        send(toEmail, null, null, template, subject, subjectFormat, model, attachments, null, false);
    }

    public void send(String toEmail, PropertySearcherUserProfile userProfile, MailTemplate template, String subject, Map<String, Object> model) {
        send(toEmail, userProfile, userProfile.getUser().getCustomer(), template, subject, null, model, null, null, false);
    }

    public void send(String toEmail,
            String fromEmail,
            MailTemplate template,
            String subject,
            Map<String, Object> model,
            Map<String, String> attachments,
            boolean sendAttachmentsInBase64) {
        send(toEmail, null, null, template, subject, null, model, attachments, fromEmail, sendAttachmentsInBase64);
    }

    public void send(PropertySearcherUserProfile userProfile, MailTemplate mailTemplate, String subject, Object[] subjectFormat, Map<String, Object> data, Long customerId) {
        PropertySearcherMessageBean messageBean = createMessageBean(userProfile.getEmail(), null, userProfile.getUser().getCustomer().getId(), userProfile.getId(), customerId, data, subject,
                subjectFormat, mailTemplate);

        sendMessage(messageBean);
    }

    protected void send(String toEmail,
            PropertySearcherUserProfile userProfile,
            PropertySearcherCustomer customer,
            MailTemplate template,
            String subject,
            Object[] subjectFormat,
            Map<String, Object> data,
            Map<String, String> attachments,
            String fromEmail,
            boolean sendAttachmentsInBase64) {
        Long customerId = null;
        if (customer != null) {
            customerId = customer.getId();
        }

        Long userProfileId = null;
        if (userProfile != null) {
            userProfileId = userProfile.getId();
        }

        PropertySearcherMessageBean messageBean = createMessageBean(toEmail, fromEmail, customerId, userProfileId, null, data, subject, subjectFormat, template);
        messageBean.setAttachments(propertySearcherAttachmentHandler.uploadAttachments(attachments));
        messageBean.setSendAttachmentsInBase64(sendAttachmentsInBase64);

        sendMessage(messageBean);
    }

    private PropertySearcherMessageBean createMessageBean(String email,
            String fromEmail,
            Long customerId,
            Long userProfileId,
            Long customizerCustomerId,
            Map<String, Object> data,
            String subject,
            Object[] subjectFormat,
            MailTemplate template) {
        PropertySearcherMessageBean messageBean = new PropertySearcherMessageBean();

        messageBean.setToEmail(email);
        messageBean.setCustomerId(customerId);
        messageBean.setUserProfileId(userProfileId);
        messageBean.setCustomizerCustomerId(customizerCustomerId);

        messageBean.setData(data);
        messageBean.setSubject(subject);
        messageBean.setFromEmail(fromEmail);
        messageBean.setSubjectFormat(subjectFormat);
        messageBean.setTemplate(template);

        return messageBean;
    }

    private PropertySearcherMessageBean createMessageBeanWithAttachments(String email,
            Long customerId,
            Long userProfileId,
            Long customizerCustomerId,
            Map<String, Object> data,
            String subject,
            Object[] subjectFormat,
            MailTemplate template,
            Map<String, String> attachments) {
        PropertySearcherMessageBean messageBean = createMessageBean(email, null, customerId, userProfileId, customizerCustomerId, data, subject, subjectFormat, template);
        messageBean.setAttachments(propertySearcherAttachmentHandler.uploadAttachments(attachments));
        messageBean.setSendAttachmentsInBase64(false);

        return messageBean;
    }

    private void sendMessage(SenderMessageBean senderMessageBean) {
        sendMessage(QueueConfigUtils.MailConfig.PROPERTY_SEARCHER_EXCHANGE_NAME, QueueConfigUtils.MailConfig.ROUTING_KEY, senderMessageBean);
    }

}
