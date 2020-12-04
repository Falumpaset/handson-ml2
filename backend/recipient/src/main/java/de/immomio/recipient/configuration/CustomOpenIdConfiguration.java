package de.immomio.recipient.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Johannes Hiemer.
 */
@Configuration
public class CustomOpenIdConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.debug(false).ignoring()
                .antMatchers(HttpMethod.GET, "/parse");
    }
}
