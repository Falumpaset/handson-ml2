package de.immomio;

import de.immomio.config.DefaultCorsConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;


/**
 * @author Johannes Hiemer, Maik Kingma
 */
@EnableJpaRepositories(basePackages = {
        "de.immomio.model.repository.landlord",
        "de.immomio.model.repository.unsecured",
        "de.immomio.model.repository.shared",
})
@EnableAspectJAutoProxy
@EnableAsync
@EnableScheduling
@EnableJpaAuditing
@EnableConfigurationProperties
@EnableSpringDataWebSupport
@EnableCaching
@EnableHypermediaSupport(type = HypermediaType.HAL)
@SpringBootApplication(scanBasePackages = {
        "de.immomio.api.handlers",
        "de.immomio.caching",
        "de.immomio.cloud",
        "de.immomio.common",
        "de.immomio.config",
        "de.immomio.controller",
        "de.immomio.exporter",
        "de.immomio.landlord",
        "de.immomio.mail",
        "de.immomio.message",
        "de.immomio.messages",
        "de.immomio.messaging",
        "de.immomio.model.entity",
        "de.immomio.pdf",
        "de.immomio.phraseApp",
        "de.immomio.service",
        "de.immomio.security",
        "de.immomio.utils",
        "de.immomio.schufa",
        "de.immomio.docusign"
})
public class Application implements EnvironmentAware {

    private static final int CORE_POOL_SIZE = 3;
    private static final int MAX_POOL_SIZE = 10;
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

    @Bean
    @Primary
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(CORE_POOL_SIZE);
        threadPoolTaskExecutor.setMaxPoolSize(MAX_POOL_SIZE);
        threadPoolTaskExecutor.setThreadNamePrefix("ApiLlTaskExecutor");
        return threadPoolTaskExecutor;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}
