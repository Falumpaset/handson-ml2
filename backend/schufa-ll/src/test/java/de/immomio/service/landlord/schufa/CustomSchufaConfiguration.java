package de.immomio.service.landlord.schufa;

import de.immomio.schufa.SchufaConfig;
import de.immomio.schufa.SchufaConnector;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Johannes Hiemer.
 */
@Configuration
@Import({SchufaConnector.class, SchufaConfig.class})
public class CustomSchufaConfiguration {

    @Configuration
    @Profile({"test","development"})
    static class PropertyResourceLoader {

        @Bean
        public PropertySourcesPlaceholderConfigurer schufaPropertiesLoader() {
            PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer
                    = new PropertySourcesPlaceholderConfigurer();

            YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
            yamlPropertiesFactoryBean.setResources(
                    new ClassPathResource("application-schufa-development.yml"));
            propertySourcesPlaceholderConfigurer.setProperties(yamlPropertiesFactoryBean.getObject());

            return propertySourcesPlaceholderConfigurer;
        }
    }
}