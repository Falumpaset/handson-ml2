package de.immomio;

import de.immomio.config.DefaultCorsConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.Assert;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 */

@EnableJpaRepositories(basePackages = {
        "de.immomio.model.repository.core",
        "de.immomio.model.repository.unsecured",
})
@EnableAsync
@EnableJpaAuditing
@EnableScheduling
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication(scanBasePackages = {
        "de.immomio.caching",
        "de.immomio.crawler",
        "de.immomio.config",
        "de.immomio.common",
        "de.immomio.service",
        "de.immomio.mail",
        "de.immomio.messaging",
        "de.immomio.cloud",
        "de.immomio.service.calculator",
        "de.immomio.messaging",
        "de.immomio.messages",
        "de.immomio.phraseApp.update",
        "de.immomio.security",
        "de.immomio.pdf",
        "de.immomio.schufa",
        "de.immomio.utils",
        "de.immomio.docusign",
        "de.immomio.itp"
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

    @Bean
    @Primary
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(10);
        threadPoolTaskScheduler.setThreadNamePrefix("CrawlerThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
    }
}
