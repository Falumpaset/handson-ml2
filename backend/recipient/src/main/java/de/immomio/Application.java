package de.immomio;

import de.immomio.config.DefaultCorsConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.Assert;

@EnableJpaRepositories(basePackages = {
        "de.immomio.model.repository.unsecured",
        "de.immomio.model.repository.core",
        "de.immomio.model.repository.unsecured",
})
@EnableAsync
@EnableJpaAuditing
@EnableSpringDataWebSupport
@EnableTransactionManagement
@EnableScheduling
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
@SpringBootApplication(scanBasePackages = {
        "de.immomio.caching",
        "de.immomio.config",
        "de.immomio.common",
        "de.immomio.model",
        "de.immomio.cloud",
        "de.immomio.mail",
        "de.immomio.utils",
        "de.immomio.recipient",
        "de.immomio.phraseApp.update",
        "de.immomio.security",
        "de.immomio.service",
        "de.immomio.reporting"
})
public class Application implements EnvironmentAware {

    private Environment environment;

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        Assert.notNull(ctx, "ApplicationContext is null");
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