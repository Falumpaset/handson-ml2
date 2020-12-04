package de.immomio.mail.sender;

import de.immomio.data.landlord.bean.customer.LandlordCustomerBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.messaging.config.QueueConfigUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Bastian Bliemeister
 */

@Slf4j
@Service
public class LandlordMailSender extends AbstractMailSender {

    private final LandlordAttachmentHandler landlordAttachmentHandler;

    @Autowired
    public LandlordMailSender(RabbitTemplate rabbitTemplate, LandlordAttachmentHandler landlordAttachmentHandler) {
        super(rabbitTemplate);
        this.landlordAttachmentHandler = landlordAttachmentHandler;
    }

    //For invoice emailing to the customers
    public void send(LandlordCustomer customer, MailTemplate template, String subject, Map<String, Object> model, Map<String, String> attachments) {
        send(customer.getInvoiceEmail(), null, null, template, subject, null, model, attachments, null, false);
    }

    //Application Mailbox warning in CheckApplicationMailboxTask:94
    public void send(String toEmail, MailTemplate template, String subject, Map<String, Object> model) {
        send(toEmail, null, null, template, subject, null, model, null, null, false);
    }

    //PropertyInvitationReminderTask:84
    //LandlordOnboardingService:102
    public void send(LandlordUser user, MailTemplate template, String subject, Map<String, Object> model) {
        send(user.getEmail(), user, user.getCustomer(), template, subject, null, model, null, null, false);
    }

    //PropertyApplicationReminderTask:86
    public void send(LandlordUser user, MailTemplate template, String subject, Map<String, Object> model, String fromEmail) {
        send(user.getEmail(), user, user.getCustomer(), template, subject, null, model, null, fromEmail, false);
    }

    //MailMessageService:48
    public void send(LandlordUser user, MailTemplate template, String subject, Object[] subjectFormat, Map<String, Object> model, Map<String, String> attachments) {
        send(user.getEmail(), user, user.getCustomer(), template, subject, subjectFormat, model, attachments, null, false);
    }

    //MailMessageService:52
    public void send(String toEmail, MailTemplate template, String subject, Object[] subjectFormat, Map<String, Object> model, Map<String, String> attachments) {
        send(toEmail, null, null, template, subject, subjectFormat, model, attachments, null, false);
    }

    //MailMessageService:74
    public void send(LandlordUser user, MailTemplate template, String subject, Object[] subjectFormat, Map<String, Object> model) {
        send(user.getEmail(), user, user.getCustomer(), template, subject, subjectFormat, model, null, null, false);
    }

    //PropertySearcherChangeEmailService:59
    public void send(String toEmail, LandlordUser user, MailTemplate template, String subject, Map<String, Object> model) {
        send(toEmail, user, user.getCustomer(), template, subject, null, model, null, null, false);
    }

    //LandlordCheckoutNotificationService:40
    public void send(String toEmail, LandlordUser user, LandlordCustomer customer, MailTemplate template, String subject, Map<String, Object> model) {
        send(toEmail, user, customer, template, subject, null, model, null, null, false);
    }

    //LandlordCheckoutNotificationService:49
    public void send(String toEmail, LandlordUser user, LandlordCustomer customer, MailTemplate template, String subject, Object[] subjectFormat, Map<String, Object> model) {
        send(toEmail, user, customer, template, subject, subjectFormat, model, null, null, false);
    }

    //AareonTenantSelect
    public void send(String toEmail, MailTemplate template, String subject, Map<String, Object> model, Map<String, String> attachments, boolean sendAttachmentsInBase64) {
        send(toEmail, null, null, template, subject, null, model, attachments, null, sendAttachmentsInBase64);
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

    public void send(String toEmail, LandlordCustomer customer, MailTemplate template, String subject, Map<String, Object> model, Map<String, String> attachments) {
        send(toEmail, null, customer, template, subject, null, model, attachments, null, false);
    }

    private void send(String toEmail,
            LandlordUser user,
            LandlordCustomer customer,
            MailTemplate template,
            String subject,
            Object[] subjectFormat,
            Map<String, Object> data,
            Map<String, String> attachments,
            String fromEmail,
            boolean sendAttachmentsInBase64) {

        LandlordMessageBean messageBean = new LandlordMessageBean();

        if (customer != null && user != null) {
            data.put("customer", new LandlordCustomerBean(customer, user.fullName()));
        }

        messageBean.setToEmail(toEmail);
        messageBean.setCustomerId(customer != null ? customer.getId() : null);
        messageBean.setUserId(user != null ? user.getId() : null);

        Long customerId = null;
        if (customer != null) {
            customerId = customer.getId();
        } else if (user != null) {
            customerId = user.getCustomer().getId();
        }
        messageBean.setCustomizerCustomerId(customerId);

        messageBean.setData(data);
        messageBean.setSubject(subject);
        messageBean.setFromEmail(fromEmail);
        messageBean.setSubjectFormat(subjectFormat);
        messageBean.setTemplate(template);
        messageBean.setSendAttachmentsInBase64(sendAttachmentsInBase64);

        messageBean.setAttachments(landlordAttachmentHandler.uploadAttachments(attachments));

        sendMessage(messageBean);
    }

    private void sendMessage(SenderMessageBean senderMessageBean) {
        sendMessage(QueueConfigUtils.MailConfig.LANDLORD_EXCHANGE_NAME, QueueConfigUtils.MailConfig.ROUTING_KEY, senderMessageBean);
    }
}
