package de.immomio.landlord.service.email;

import de.immomio.beans.landlord.email.TemplateResponseBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.sendgrid.SendgridTemplateClient;
import de.immomio.sendgrid.SendgridTemplateService;
import de.immomio.sendgrid.model.SendgridTemplateVersion;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

@Service
public class SendgridEmailTemplateService {

    private final SendgridTemplateClient sendgridTemplateClient;

    private final SendgridTemplateService sendgridTemplateService;

    @Autowired
    public SendgridEmailTemplateService(SendgridTemplateClient sendgridTemplateClient, SendgridTemplateService sendgridTemplateService) {
        this.sendgridTemplateClient = sendgridTemplateClient;
        this.sendgridTemplateService = sendgridTemplateService;
    }

    public TemplateResponseBean getTemplate(MailTemplate template, LandlordCustomer customer, Map<String, Object> data, boolean filled, Locale locale) {
        if (filled) {
            return getTemplateWithTranslationsAndData(template, customer, data, locale);
        } else {
            return getTemplateWithData(template, data);
        }
    }

    private TemplateResponseBean getTemplateWithTranslationsAndData(MailTemplate template, LandlordCustomer customer, Map<String, Object> data, Locale locale) {
        return new TemplateResponseBean(template, sendgridTemplateService.getSendGridHtml(template, customer, locale, data),
                sendgridTemplateService.getSubject(template, customer, locale));
    }

    private TemplateResponseBean getTemplateWithData(MailTemplate template, Map<String, Object> data) {
        String sendGridTemplateId = template.getSendGridTemplate();
        SendgridTemplateVersion sendGridTemplate = sendgridTemplateClient.getTemplate(sendGridTemplateId);
        String htmlContent = sendGridTemplate.getHtmlContent();
        VelocityContext context = new VelocityContext(data);
        StringWriter stringWriter = new StringWriter();
        Velocity.evaluate(context, stringWriter, sendGridTemplateId, htmlContent);

        return new TemplateResponseBean(template, stringWriter.toString(), sendGridTemplate.getSubject());
    }

}
