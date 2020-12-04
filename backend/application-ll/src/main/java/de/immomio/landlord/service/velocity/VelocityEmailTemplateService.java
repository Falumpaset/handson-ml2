package de.immomio.landlord.service.velocity;

import de.immomio.beans.landlord.email.TemplateResponseBean;
import de.immomio.mail.sender.templates.MailTemplate;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.tools.generic.DateTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 */
@Service
public class VelocityEmailTemplateService {

    VelocityEngine velocityEngine = new VelocityEngine();
    @Value("${timezone.europe}")
    private String ZONE_ID;

    @PostConstruct
    public void init() {
        velocityEngine.setProperty("resource.loader", "class");
        velocityEngine.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
    }

    public TemplateResponseBean readAndMergeTemplate(MailTemplate mailTemplate, Map<String, Object> data) {
        if (data == null) {
            data = new HashMap<>();
        }

        data.put("timezone", TimeZone.getTimeZone(ZONE_ID));
        data.put("date", new DateTool());
        data.put("messages", new MessageSourceFaker());

        VelocityContext context = new VelocityContext();
        for (Entry<String, Object> entry : data.entrySet()) {
            context.put(entry.getKey(), entry.getValue());
        }

        StringWriter sw = new StringWriter();
        boolean bool = velocityEngine.mergeTemplate(mailTemplate.getTemplateFile(), "UTF-8", context, sw);

        if (!bool) {
            throw new VelocityException("Error merging template -> " + mailTemplate);
        }

        return new TemplateResponseBean(mailTemplate, sw.toString(), null);
    }
}
