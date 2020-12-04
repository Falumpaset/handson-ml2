package de.immomio.security.config;

import de.immomio.config.AbstractCustomOpenIdConfiguration;
import de.immomio.model.repository.landlord.customer.user.LandlordUserRepository;
import de.immomio.security.SecurityEvaluationContextExtension;
import de.immomio.security.common.handler.CommonCorsAuthenticationEntryPoint;
import de.immomio.security.openid.filter.OpenIDRelyingPartyFilter;
import de.immomio.security.openid.handler.LandlordOpenIDRelyingPartyAuthenticationSuccessHandler;
import de.immomio.security.openid.handler.OpenIDRelyingPartyAuthenticationFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.spel.spi.EvaluationContextExtension;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * @author Johannes Hiemer.
 */
@Order(3)
@Configuration
public class CustomOpenIdConfiguration extends AbstractCustomOpenIdConfiguration<OpenIDRelyingPartyUserDetailsService> {

    @Autowired
    private LandlordUserRepository userRepository;

    @Autowired
    private LandlordOpenIDRelyingPartyAuthenticationSuccessHandler openIDRelyingPartyAuthenticationSuccessHandler;

    @Value("${authentication.token.allowParameter:false}")
    private boolean allowParameter;

    @Bean
    public EvaluationContextExtension securityExtension() {
        return new SecurityEvaluationContextExtension();
    }

    @Bean
    @Override
    public OpenIDRelyingPartyUserDetailsService openIDRelyingPartyUserDetailsService() {
        OpenIDRelyingPartyUserDetailsService openIDRelyingPartyUserDetailsService = new OpenIDRelyingPartyUserDetailsService();
        openIDRelyingPartyUserDetailsService.setUserRepository(userRepository);
        return openIDRelyingPartyUserDetailsService;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.authenticationProvider(openIDRelyingPartyAuthenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // add exceptions for endpoints that don't require auth

        // TODO: it's a somewhat bad idea to have this configuration in here
        // (authentication repository) even though it is
        // highly consumer specific (e.g. web and vw APIs).
        web.debug(false).ignoring()
                .antMatchers(HttpMethod.GET, "/")
                .antMatchers(HttpMethod.GET, "/error")
                .antMatchers(HttpMethod.GET, "/v2/api-docs")
                .antMatchers(HttpMethod.GET, "/swagger-ui.html")
                .antMatchers(HttpMethod.GET, "/swagger-resources/**")
                .antMatchers(HttpMethod.GET, "/webjars/**")
                .antMatchers(HttpMethod.GET, "/constants/**")
                .antMatchers(HttpMethod.POST, "/manualTrigger/proposalOfferEmails")
                .antMatchers(HttpMethod.GET, "/cloud/immoscout/oauth")
                .antMatchers(HttpMethod.GET, "/users/email/confirm")
                .antMatchers(HttpMethod.POST, "/users/auth/**")
                .antMatchers(HttpMethod.POST, "/users/register")
                .antMatchers(HttpMethod.POST, "/users/reset-password")
                .antMatchers(HttpMethod.POST, "/users/reset-password/confirm")
                .antMatchers(HttpMethod.POST, "/users/verify-email/**")
                .antMatchers(HttpMethod.POST, "/customers/branding/validate/token")
                .antMatchers(HttpMethod.POST, "/customers/branding/preview/validate/token")
                .antMatchers(HttpMethod.POST, "/dmv/signingUrl")
                .antMatchers(HttpMethod.POST, "/dmv/getContractData")
                .antMatchers(HttpMethod.GET, "/dmv/download");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        OpenIDRelyingPartyFilter authFilter = new OpenIDRelyingPartyFilter(authenticationManager(), allowParameter);
        authFilter.setSuccessHandler(openIDRelyingPartyAuthenticationSuccessHandler);
        authFilter.setFailureHandler(new OpenIDRelyingPartyAuthenticationFailureHandler());

        http.addFilterBefore(authFilter, LogoutFilter.class)

                .csrf().disable()

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()

                .exceptionHandling().authenticationEntryPoint(new CommonCorsAuthenticationEntryPoint())

                .and()

                .authorizeRequests()
                .antMatchers("/actuator/**").denyAll()
                .antMatchers("/swagger-ui/**").denyAll()
                .antMatchers(HttpMethod.GET, "/**").authenticated()
                .antMatchers(HttpMethod.POST, "/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/**").authenticated()
                .antMatchers(HttpMethod.PATCH, "/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/**")
                .authenticated();
    }

}
