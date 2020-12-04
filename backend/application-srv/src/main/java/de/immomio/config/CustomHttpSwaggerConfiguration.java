package de.immomio.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

/**
 * @author Niklas Lindemann
 */
@Configuration
@Order(-1)
public class CustomHttpSwaggerConfiguration extends AbstractCustomSwaggerSecurityConfiguration {

    @Value("${swagger.auth.username}")
    private String username;
    @Value("${swagger.auth.password}")
    private String password;

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser(username).password(password).roles("USER");
    }
}
