package de.immomio.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Niklas Lindemann
 */
@Configuration
public class OpenApiDocConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .setGroup("Immomio API")
                .pathsToMatch("/api/**").packagesToExclude("de.immomio.controller.mock")
                .build();
    }

    @Bean
    public GroupedOpenApi mockApi() {
        return GroupedOpenApi.builder()
                .setGroup("Immomio Mock API")
                .pathsToMatch("/api/mock/**")
                .build();
    }

    @Bean
    public OpenAPI immomioOpenApi() {
        return new OpenAPI()
                .info(new Info().title("Immomio API")
                        .description("All endpoints are secured via oauth2 (except the auth/token endpoint). First, you need to call the api/auth/token endpoint to receive an access token. " +
                                "With this token, you can access all endpoints (Authorization: Bearer ey....). On the top right of this page you can switch between the Production and Mock Implementation of this API. " +
                                "You are able to authenticate inside this documentation and use the api. To do this, call the auth/token endpoint, click on 'Authorize' and provide the token.")
                        .version("1.0")).components(new Components().addSecuritySchemes("bearer-key", new SecurityScheme().type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
    }

}
