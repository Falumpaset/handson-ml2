package de.immomio.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * @author Johannes Hiemer.
 */
@Configuration
@ComponentScan(basePackages = {"de.immomio.importer", "de.immomio.common.zip", "de.immomio.constants"})
public class CustomImporterConfiguration {

    @Configuration
    @Profile({"test"})
    static class PropertyResourceLoader {

        @Bean
        public PropertySourcesPlaceholderConfigurer mailPropertiesLoader() throws IOException {
            PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();

            YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
            yamlPropertiesFactoryBean.setResources(
                    new ClassPathResource("application-model.yml"),
                    new ClassPathResource("application-model-tests.yml"),
                    new ClassPathResource("application-messaging.yml"),
                    new ClassPathResource("application-messaging-tests.yml"),
                    new ClassPathResource("application-cloud.yml"),
                    new ClassPathResource("application-importer-tests.yml"));
            propertySourcesPlaceholderConfigurer.setProperties(yamlPropertiesFactoryBean.getObject());

            return propertySourcesPlaceholderConfigurer;
        }
    }
}