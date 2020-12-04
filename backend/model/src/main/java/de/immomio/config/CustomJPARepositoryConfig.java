package de.immomio.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.immomio.config.dialect.CustomPostgreSqlDialect;
import io.pivotal.cfenv.jdbc.CfJdbcEnv;
import io.pivotal.cfenv.jdbc.CfJdbcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 * @author Maik Kingma
 */

@Slf4j
@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@EnableConfigurationProperties
public class CustomJPARepositoryConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory());
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("de.immomio.model", "de.immomio.data");
        factory.setDataSource(dataSource);
        factory.getJpaPropertyMap().put("hibernate.dialect", CustomPostgreSqlDialect.class);
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }

    @Configuration
    @Profile({"default", "development"})
    static class Default {

        @Value("${database.url}")
        private String databaseUrl;

        @Value("${database.user}")
        private String databaseUser;

        @Value("${database.password}")
        private String databasePassword;

        @Value("${database.driver}")
        private String databaseDriver;

        @Bean
        public DataSource dataSource() {
            DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
            driverManagerDataSource.setDriverClassName(databaseDriver);
            driverManagerDataSource.setUrl(databaseUrl);
            driverManagerDataSource.setUsername(databaseUser);
            driverManagerDataSource.setPassword(databasePassword);

            return driverManagerDataSource;
        }

    }

    @Configuration
    @Profile({"test-api"})
    static class TestApiProfile {

        @Value("${database.test.url}")
        private String databaseUrl;

        @Value("${database.test.user}")
        private String databaseUser;

        @Value("${database.test.password}")
        private String databasePassword;

        @Value("${database.test.driver}")
        private String databaseDriver;

        @Bean
        public DataSource dataSource() {
            DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
            driverManagerDataSource.setDriverClassName(databaseDriver);
            driverManagerDataSource.setUrl(databaseUrl);
            driverManagerDataSource.setUsername(databaseUser);
            driverManagerDataSource.setPassword(databasePassword);

            return driverManagerDataSource;
        }
    }

    @Configuration
    @Profile("cloud")
    static class Cloud {

        public static final String JDBC_POSTGRESQL = "jdbc:postgresql://";
        public static final String POSTGRES_PORT = ":5432";
        public static final String SLASH = "/";

        @Value("${service.config.poolSize}")
        private int poolSize;

        @Value("${service.config.maxWaitTime}")
        private int maxWaitTime;

        @Value("${service.postgres.ip}")
        private String postgresIp;


        @Bean
        public DataSource dataSource() {
            CfJdbcEnv jdbcEnv =  new CfJdbcEnv();
            CfJdbcService jdbcService = jdbcEnv.findJdbcService();
            String jdbcUrl = new StringBuilder().append(JDBC_POSTGRESQL)
                    .append(postgresIp)
                    .append(POSTGRES_PORT)
                    .append(SLASH)
                    .append(jdbcService.getCredentials().getUriInfo().getPath()).toString();

            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setMaximumPoolSize(poolSize);
            hikariConfig.setJdbcUrl(jdbcUrl);
            hikariConfig.setUsername(jdbcService.getCredentials().getUsername());
            hikariConfig.setPassword(jdbcService.getCredentials().getPassword());
            hikariConfig.setConnectionTimeout(maxWaitTime);
            hikariConfig.setLeakDetectionThreshold(60000L);
            return new HikariDataSource(hikariConfig);
        }
    }
}