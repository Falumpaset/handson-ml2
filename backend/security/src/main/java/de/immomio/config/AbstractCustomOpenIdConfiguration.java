package de.immomio.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.immomio.security.SecurityEvaluationContextExtension;
import de.immomio.security.openid.provider.OpenIDRelyingPartyAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.spel.spi.EvaluationContextExtension;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public abstract class AbstractCustomOpenIdConfiguration<UDS extends UserDetailsService>
        extends WebSecurityConfigurerAdapter {

    @Value("${auth.openid.public_key}")
    private String publicKey;

    @Bean
    public EvaluationContextExtension securityExtension() {
        return new SecurityEvaluationContextExtension();
    }

    @Bean
    public abstract UDS openIDRelyingPartyUserDetailsService();

    @Bean
    public OpenIDRelyingPartyAuthenticationProvider openIDRelyingPartyAuthenticationProvider() {
        OpenIDRelyingPartyAuthenticationProvider openIDAuthenticationProvider = new OpenIDRelyingPartyAuthenticationProvider();
        openIDAuthenticationProvider.setUserDetailsService(openIDRelyingPartyUserDetailsService());
        openIDAuthenticationProvider.setPublicKey(publicKey);
        openIDAuthenticationProvider.setObjectMapper(new ObjectMapper());
        return openIDAuthenticationProvider;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.authenticationProvider(openIDRelyingPartyAuthenticationProvider());
    }

    @Override
    public abstract void configure(WebSecurity web) throws Exception;

    @Override
    protected abstract void configure(HttpSecurity http) throws Exception;
}
