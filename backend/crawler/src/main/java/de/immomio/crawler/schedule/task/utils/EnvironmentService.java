package de.immomio.crawler.schedule.task.utils;

import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author Freddy Sawma
 */

@Component
public class EnvironmentService {

    private final Environment env;

    private static final String PRODUCTION = "production";

    @Autowired
    public EnvironmentService(Environment env) {
        this.env = env;
    }

    public boolean isProduction() {
        return Arrays.asList(env.getActiveProfiles()).contains(PRODUCTION);
    }
}
