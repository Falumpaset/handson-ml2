package de.immomio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.Assert;

/**
 * @author Johannes Hiemer, Maik Kingma.
 */

@EnableAsync
@EnableSpringDataWebSupport
@SpringBootApplication(
        scanBasePackages = {
                "de.immomio.config.security",
                "de.immomio.importer",
                "de.immomio.common.zip",
                "de.immomio.model.entity",
        },
        exclude = {
                DataSourceAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class
        })
public class Application implements EnvironmentAware {

    private Environment environment;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Application.class);

        ConfigurableApplicationContext ctx = springApplication.run(args);
        Assert.notNull(ctx, "ConfigurableApplicationContext may not be null");
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}