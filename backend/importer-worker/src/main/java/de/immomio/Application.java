/**
 *
 */
package de.immomio;

import de.immomio.config.DefaultCorsConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.Assert;

/**
 * @author Johannes Hiemer, Maik Kingma.
 */

@EnableJpaRepositories(basePackages = {
        "de.immomio.model.repository.service.landlord",
        "de.immomio.model.repository.unsecured",
        "de.immomio.model.repository.service.propertysearcher",
        "de.immomio.model.repository.service.shared",
        "de.immomio.model.repository.service.admin",
})
@EnableJpaAuditing
@EnableAsync
@EnableConfigurationProperties
@EnableSpringDataWebSupport
@EnableTransactionManagement
@EnableHypermediaSupport(type = HypermediaType.HAL)
@SpringBootApplication(scanBasePackages = {
        "de.immomio.config",
        "de.immomio.common",
        "de.immomio.importer.worker",
        "de.immomio.mail.sender",
        "de.immomio.security",
        "de.immomio.phraseApp.update",
        "de.immomio.cloud.service",
        "de.immomio.reporting"
})

public class Application implements EnvironmentAware {

    private Environment environment;

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        Assert.notNull(ctx, "Application Context may not be null");
    }

    @Bean
    public FilterRegistrationBean filterBean() {
        return DefaultCorsConfiguration.corsConfiguration();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}