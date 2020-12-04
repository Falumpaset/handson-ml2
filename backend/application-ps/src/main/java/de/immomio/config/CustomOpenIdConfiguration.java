package de.immomio.config;

import de.immomio.model.repository.propertysearcher.user.PropertySearcherUserRepository;
import de.immomio.security.SecurityEvaluationContextExtension;
import de.immomio.security.common.handler.CommonCorsAuthenticationEntryPoint;
import de.immomio.security.openid.filter.OpenIDRelyingPartyFilter;
import de.immomio.security.openid.handler.OpenIDRelyingPartyAuthenticationFailureHandler;
import de.immomio.security.openid.handler.PropertySearcherOpenIDRelyingPartyAuthenticationSuccessHandler;
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

@Order(3)
@Configuration
public class CustomOpenIdConfiguration extends AbstractCustomOpenIdConfiguration<OpenIDRelyingPartyUserDetailsService> {

    @Autowired
    private PropertySearcherUserRepository userRepository;

    @Autowired
    private PropertySearcherOpenIDRelyingPartyAuthenticationSuccessHandler successHandler;

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
        web.debug(false).ignoring()
                .antMatchers(HttpMethod.GET, "/")
                .antMatchers(HttpMethod.GET, "/error")
                .antMatchers(HttpMethod.GET, "/v2/api-docs")
                .antMatchers(HttpMethod.GET, "/swagger-ui.html")
                .antMatchers(HttpMethod.GET, "/swagger-resources/**")
                .antMatchers(HttpMethod.GET, "/webjars/**")
                .antMatchers(HttpMethod.GET, "/properties/shared/**")
                .antMatchers(HttpMethod.GET, "/customQuestions/shared/**")
                .antMatchers(HttpMethod.GET, "/constants/**")
                .antMatchers(HttpMethod.POST, "/users/auth/**")
                .antMatchers(HttpMethod.POST, "/users/register")
                .antMatchers(HttpMethod.POST, "/users/unlock")
                .antMatchers(HttpMethod.GET, "/users/email/confirm")
                .antMatchers(HttpMethod.POST, "/users/reset-password")
                .antMatchers(HttpMethod.POST, "/users/reset-password/confirm")
                .antMatchers(HttpMethod.POST, "/users/verify-email/**")
                .antMatchers(HttpMethod.POST, "/applications/declareIntentNoAuth")
                .antMatchers(HttpMethod.POST, "/users/extendSearchUntil")
                .antMatchers(HttpMethod.GET, "/users/firstSocialLogin")
                .antMatchers(HttpMethod.GET, "/locations/**")
                .antMatchers(HttpMethod.POST, "/schufa/**")
                .antMatchers(HttpMethod.POST, "/dmv/**")
                .antMatchers(HttpMethod.GET, "/dmv/**")
                .antMatchers(HttpMethod.GET, "/guest/**")
                .antMatchers(HttpMethod.POST, "/guest/**")
                .antMatchers(HttpMethod.DELETE, "/guest/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        OpenIDRelyingPartyFilter authFilter = new OpenIDRelyingPartyFilter(authenticationManager(), allowParameter);
        authFilter.setSuccessHandler(successHandler);
        authFilter.setFailureHandler(new OpenIDRelyingPartyAuthenticationFailureHandler());

        http.addFilterBefore(authFilter, LogoutFilter.class)
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(new CommonCorsAuthenticationEntryPoint())
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/**").authenticated()
                .antMatchers(HttpMethod.POST, "/**").authenticated().antMatchers(HttpMethod.PUT, "/**").authenticated()
                .antMatchers(HttpMethod.PATCH, "/**").authenticated().antMatchers(HttpMethod.DELETE, "/**")
                .authenticated();
    }

}
