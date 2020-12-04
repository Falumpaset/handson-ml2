package de.immomio.config;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilderFactory;

@Configuration
public class CustomRestMvcConfiguration extends DefaultRestMvcConfiguration {

    @Primary
    @Bean(autowire = Autowire.BY_TYPE)
    public WebMvcLinkBuilderFactory controllerLinkBuilderFactory() {
        return new WebMvcLinkBuilderFactory();
    }

}
