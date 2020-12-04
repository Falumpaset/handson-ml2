package de.immomio.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.pivotal.cfenv.core.CfApplication;
import io.pivotal.cfenv.core.CfEnv;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("cloud")
public class CustomMicrometerConfig {

    @Bean
    MeterRegistryCustomizer<MeterRegistry> configurer() {
        CfApplication app = new CfEnv().getApp();
        return (registry) -> registry.config()
                .commonTags("space.name", app.getSpaceName())
                .commonTags("application.name", app.getApplicationName())
                .commonTags("application.id", app.getApplicationId());
    }
}
