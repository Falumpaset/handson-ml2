package de.immomio.sendgrid;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import de.immomio.constants.exceptions.SendGridException;
import de.immomio.sendgrid.model.SendgridTemplate;
import de.immomio.sendgrid.model.SendgridTemplateVersion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Component
public class SendgridTemplateClient {

    @Value("${sendgrid.api_key}")
    private String apiKey;

    private static final int ACTIVE_VERSION = 1;

    private SendGrid sendGrid;

    private ObjectMapper objectMapper;

    private static final String MAILTEMPLATE_CACHE_KEY = "mail-templates";

    @PostConstruct
    public void init() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper = objectMapper;
        this.sendGrid = new SendGrid(apiKey);
    }

    @Cacheable(value = MAILTEMPLATE_CACHE_KEY, key = "#templateId", cacheManager = "cacheManager")
    public SendgridTemplateVersion getTemplate(String templateId) {
        try {
            SendgridTemplate sendgridTemplate = requestSendgridTemplate(templateId);
            return getActiveTemplate(sendgridTemplate);
        } catch (IOException e) {
            log.error("error getting sendgrid template", e);
            throw new SendGridException(e.getMessage(), e);
        }
    }

    private SendgridTemplateVersion getActiveTemplate(SendgridTemplate sendgridTemplate) {
        return sendgridTemplate.getVersions()
                .stream()
                .filter(version -> version.getActive() == ACTIVE_VERSION).findFirst()
                .orElseThrow(() -> new SendGridException("no active template found"));
    }

    private SendgridTemplate requestSendgridTemplate(String templateId) throws IOException {
        Request request = new Request();
        request.setMethod(Method.GET);
        request.setEndpoint(String.format("templates/%s", templateId));
        Response response = sendGrid.api(request);

        return objectMapper.readValue(response.getBody(), SendgridTemplate.class);
    }
}
