package de.immomio.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author Niklas Lindemann
 */
@Configuration
public class OpenApiDocConfig extends BaseOpenApiDocConfig{

    public static final String LANDLORD_GROUP = "immomio-admin";
    public static final String CONTROLLER_PACKAGE = "de.immomio.controller";
    public static final String TITLE = "Immomio Admin API";
    public static final String DESCRIPTION = "Immomio Admin API Documentation";
    public static final String VERSION = "1.0";

    @PostConstruct
    public void init() {
        super.init();
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .setGroup(LANDLORD_GROUP).packagesToScan(CONTROLLER_PACKAGE)
                .build();
    }

    @Bean
    public OpenAPI immomioOpenApi() {
        return new OpenAPI()
                .info(new Info().title(TITLE)
                        .description(DESCRIPTION)
                        .version(VERSION));
    }
}
