package de.immomio.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
@ComponentScan(basePackages = {"de.immomio"})
public class CustomAPIConfiguration {

    @Configuration
    @Profile({"test-api"})
    static class PropertyResourceLoader {

        @Bean
        public PropertySourcesPlaceholderConfigurer testApiPropertiesLoader() throws IOException {
            PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();

            YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
            yamlPropertiesFactoryBean.setResources(
                    new ClassPathResource("application-api-ps-tests.yml"),
                    new ClassPathResource("application-ps.yml"),
                    new ClassPathResource("application-billing.yml"),
                    new ClassPathResource("application-billing-tests.yml"),
                    new ClassPathResource("application-cloud.yml"),
                    new ClassPathResource("application-cloud-test.yml"),
                    new ClassPathResource("application-constants.yml"),
                    new ClassPathResource("application-constants-tests.yml"),
                    new ClassPathResource("application-mail.yml"),
                    new ClassPathResource("application-mail-tests.yml"),
                    new ClassPathResource("application-mail-ps.yml"),
                    new ClassPathResource("application-messaging-tests.yml"),
                    new ClassPathResource("application-model-ps-tests.yml"),
                    new ClassPathResource("application-model-tests.yml"),
                    new ClassPathResource("application-i18n.yml"),
                    new ClassPathResource("application-security-tests.yml")
            );
            propertySourcesPlaceholderConfigurer.setProperties(yamlPropertiesFactoryBean.getObject());

            return propertySourcesPlaceholderConfigurer;
        }
    }
}
