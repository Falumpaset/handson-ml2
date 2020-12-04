package de.immomio;

import de.immomio.config.DefaultCorsConfiguration;
import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 */
@EnableJpaRepositories(basePackages = {
        "de.immomio.model.repository.core",
        "de.immomio.model.repository.unsecured"
})
@EnableAsync
@EnableRetry
@EnableScheduling
@EnableJpaAuditing
@EnableSpringDataWebSupport
@EnableHypermediaSupport(type = HypermediaType.HAL)
@SpringBootApplication(scanBasePackages = {
        "de.immomio.broker",
        "de.immomio.caching",
        "de.immomio.reporting",
        "de.immomio.cloud",
        "de.immomio.config",
        "de.immomio.broker.config",
        "de.immomio.common",
        "de.immomio.exporter",
        "de.immomio.mail",
        "de.immomio.messages",
        "de.immomio.messaging",
        "de.immomio.model.entity",
        "de.immomio.phraseApp",
        "de.immomio.service",
        "de.immomio.service.calculator",
        "de.immomio.reporting",
        "de.immomio.utils"
})
public class Application implements EnvironmentAware {

    private Environment environment;

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        Assert.notNull(ctx, "ApplicationContext is null");
    }

    @Bean
    public SpelAwareProxyProjectionFactory projectionFactory() {
        return new SpelAwareProxyProjectionFactory();
    }

    @Bean
    public FilterRegistrationBean filterBean() {
        return DefaultCorsConfiguration.corsConfiguration();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


    @Bean
    @Primary
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(3);
        threadPoolTaskExecutor.setMaxPoolSize(7);
        threadPoolTaskExecutor.setThreadNamePrefix("BrokerTaskExecutor");
        return threadPoolTaskExecutor;
    }

}