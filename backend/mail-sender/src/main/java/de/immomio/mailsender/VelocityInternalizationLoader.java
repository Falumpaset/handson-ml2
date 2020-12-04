package de.immomio.mailsender;

import de.immomio.config.ApplicationMessageSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.tools.generic.DateTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 */

@Slf4j
@Service
public class VelocityInternalizationLoader {

    @Autowired
    private ApplicationMessageSource messageSource;

    @Autowired
    private VelocityEngine velocityEngine;

    public String merge(String templateLocation, String encoding, Map<String, Object> data) {
        if (data == null) {
            data = new HashMap<>();
        }

        data.put("date", new DateTool());
        data.put("messages", this.messageSource);

        VelocityContext context = new VelocityContext(data);
        StringWriter sw = new StringWriter();
        boolean bool = velocityEngine.mergeTemplate(templateLocation, "UTF-8", context, sw);

        if (!bool) {
            throw new VelocityException("Error merging template -> " + templateLocation);
        }

        return sw.toString();
    }
}
