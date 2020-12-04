package de.immomio;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import de.immomio.config.DefaultCorsConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaRepositories(basePackages = {
		"de.immomio.model.repository.landlord",
		"de.immomio.model.repository.unsecured",
		"de.immomio.model.repository.shared"
})
@SpringBootApplication(scanBasePackages = {
		"de.immomio.cloud",
		"de.immomio.config",
		"de.immomio.common",
		"de.immomio.model.entity",
		"de.immomio.service",
		"de.immomio.controller",
		"de.immomio.messaging",
		"de.immomio.caching",
		"de.immomio.utils",
		"de.immomio.message",
		"de.immomio.phraseApp",
		"de.immomio.docusign",
		"de.immomio.mail"
}, exclude = RepositoryRestMvcAutoConfiguration.class)
@EnableAsync
@EnableScheduling
@EnableJpaAuditing
@EnableConfigurationProperties
@EnableSpringDataWebSupport
@EnableCaching
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class Application implements EnvironmentAware {

	private Environment environment;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
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
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		return objectMapper;
	}
}
