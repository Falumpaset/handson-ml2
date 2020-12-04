package de.immomio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.Assert;

/**
 * @author Bastian Bliemeister.
 */

@EnableJpaRepositories(basePackages = {
        "de.immomio.model.repository.core",
        "de.immomio.model.repository.unsecured",
})
@EnableAsync
@SpringBootApplication(scanBasePackages = {
        "de.immomio.config",
        "de.immomio.common",
        "de.immomio.service.shortUrl",
        "de.immomio.controller"
})
@EnableTransactionManagement
public class Application implements EnvironmentAware {

    private Environment environment;

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        Assert.notNull(ctx, "ApplicationContext is NULL");
    }

    @Bean
    public SpelAwareProxyProjectionFactory projectionFactory() {
        return new SpelAwareProxyProjectionFactory();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}