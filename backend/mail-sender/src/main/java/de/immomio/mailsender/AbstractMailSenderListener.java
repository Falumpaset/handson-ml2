package de.immomio.mailsender;

import de.immomio.config.ApplicationMessageSource;
import de.immomio.data.base.entity.customer.AbstractCustomer;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.shared.entity.email.MessageBean;
import de.immomio.mail.MailMessage;
import de.immomio.mail.ModelParams;
import de.immomio.mail.configurator.AbstractMailConfigurator;
import de.immomio.mail.sender.SenderMessageBean;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.messaging.converter.BaseJsonConverter;
import de.immomio.sendgrid.SendgridTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Locale;

/**
 * @author Bastian Bliemeister
 */

@Slf4j
public abstract class AbstractMailSenderListener<CU extends AbstractCustomer<?, ?>,
        MB extends SenderMessageBean,
        ES extends AbstractEmailService,
        MC extends AbstractMailConfigurator>
        implements MessageListener {

    protected final BaseJsonConverter baseJsonConverter;
    private final ApplicationMessageSource messageSource;
    @Value("${email.footer.customer.default}")
    private String footerPlaceholder;
    private SendgridTemplateService templateService;

    protected AbstractMailSenderListener(BaseJsonConverter baseJsonConverter, ApplicationMessageSource messageSource) {
        this.baseJsonConverter = baseJsonConverter;
        this.messageSource = messageSource;
    }

    @Autowired
    public void setSendgridTemplateService(SendgridTemplateService sendgridTemplateService) {
        this.templateService = sendgridTemplateService;
    }

    protected abstract MC getCustomMailConfigurator();

    protected abstract String getToEmail(MB bean);

    protected abstract ES getEmailService();

    protected abstract void saveEmail(MB bean, MailMessage mailMessage);

    protected abstract CU getCustomer(Long id);

    protected void onMessage(MB bean) {
        String toEmail = getToEmail(bean);

        if (toEmail == null || toEmail.isEmpty()) {
            throw new IllegalArgumentException("toEmail is null or empty ...");
        }

        LandlordCustomer customizer = null;
        if (bean.getCustomizerCustomerId() != null) {
            customizer = getCustomizerCustomer(bean.getCustomizerCustomerId());
        }
        Locale locale = getCustomMailConfigurator().getLocale(customizer);
        MailTemplate template = bean.getTemplate();
        getCustomMailConfigurator().emailCustomization(
                bean.getData(),
                template,
                customizer,
                this.getMessage(footerPlaceholder, locale));

        String subject;
        if (StringUtils.isNotBlank(template.getSendGridTemplate())) {
            subject = templateService.getSubject(template, customizer, locale);
        } else {
            subject = this.getMessage(bean.getSubject(), locale);
        }

        if (bean.getSubjectFormat() != null && bean.getSubjectFormat().length > 0) {
            subject = String.format(subject, bean.getSubjectFormat());
        }

        if (bean.getData().containsKey(ModelParams.SUBJECT_PLACEHOLDER) && bean.getData().get(ModelParams.SUBJECT_PLACEHOLDER) != null) {
            subject = subject.replace("%s", bean.getData().get(ModelParams.SUBJECT_PLACEHOLDER).toString());
        }

        MailMessage mailMessage = getEmailService().create(toEmail, bean.getFromEmail(), null, template,
                subject, bean.getData(), bean.getAttachments(), customizer);

        mailMessage.setSendAttachmentsInBase64(bean.isSendAttachmentsInBase64());
        getEmailService().send(mailMessage);

        logSendEmail(mailMessage, template, customizer);

        saveEmail(bean, mailMessage);
    }

    private void logSendEmail(MailMessage mailMessage, MailTemplate template, LandlordCustomer landlordCustomer) {
        String logMessage = "Successfully send Email with " +
                "Template: " +
                template +
                " From: " +
                mailMessage.getFromEmail() +
                " To: " +
                mailMessage.getToEmail();
        if (landlordCustomer != null) {
            logMessage += " Landlord ID: " + landlordCustomer.getId();
        }

        log.info(logMessage);
    }

    protected abstract LandlordCustomer getCustomizerCustomer(Long id);

    protected MessageBean createMessageBean(MB bean, MailMessage mailMessage) {
        MessageBean messageBean = new MessageBean();
        messageBean.setAttachments(bean.getAttachments());
        messageBean.setFromEmail(mailMessage.getFromEmail());
        messageBean.setMessageBody(mailMessage.getMessageBody());
        messageBean.setSubject(mailMessage.getSubject());
        messageBean.setToBcc(mailMessage.getToBcc());
        messageBean.setToCc(mailMessage.getToCc());
        messageBean.setToEmail(mailMessage.getToEmail());

        return messageBean;
    }

    private String getMessage(String key, Locale locale) {
        return messageSource.resolveCodeString(key, locale);
    }
}
