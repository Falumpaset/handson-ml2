package de.immomio.sendgrid;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.sendgrid.model.SendgridTemplateVersion;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Service
public class SendgridTemplateService {

    private final SendgridTemplateTranslationCacheService sendgridTemplateTranslationCacheService;
    private final SendgridTemplateClient sendgridTemplateClient;

    @Autowired
    public SendgridTemplateService(
            SendgridTemplateTranslationCacheService sendgridTemplateTranslationCacheService, SendgridTemplateClient sendgridTemplateClient
    ) {
        this.sendgridTemplateTranslationCacheService = sendgridTemplateTranslationCacheService;
        this.sendgridTemplateClient = sendgridTemplateClient;
    }

    public String getSendGridHtml(MailTemplate template, LandlordCustomer customer, Locale locale, Map<String, Object> data) {
        SendgridTemplateVersion sendgridTemplate = sendgridTemplateTranslationCacheService.getTranslatedSendgridHtml(template, customer, locale);
        return replaceObjectData(data, sendgridTemplate.getHtmlContent(), sendgridTemplate.getName());
    }

    public String getSubject(MailTemplate template, LandlordCustomer customer, Locale locale) {
        SendgridTemplateVersion sendgridTemplate = sendgridTemplateTranslationCacheService.getTranslatedSendgridHtml(template, customer, locale);
        return sendgridTemplate.getSubject();
    }

    private String replaceObjectData(Map<String, Object> data, String htmlContent, String templateName) {
        VelocityContext context = new VelocityContext(data);
        StringWriter stringWriter = new StringWriter();

        Velocity.evaluate(context, stringWriter, templateName, htmlContent);

        return stringWriter.toString();
    }
}
