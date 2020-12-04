package de.immomio.config;

import de.immomio.model.repository.landlord.customer.user.LandlordExternalApiUserRepository;
import de.immomio.security.SecurityEvaluationContextExtension;
import de.immomio.security.common.handler.CommonCorsAuthenticationEntryPoint;
import de.immomio.security.openid.filter.OpenIDRelyingPartyFilter;
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

@Order(2)
@Configuration
public class CustomOpenIdConfiguration extends AbstractCustomOpenIdConfiguration<OpenIDRelyingPartyUserDetailsService> {

    @Autowired
    private LandlordExternalApiUserRepository userRepository;


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
                .antMatchers(HttpMethod.POST, "/api/auth/**")
                .antMatchers(HttpMethod.POST, "/api/mock/**")
                .antMatchers(HttpMethod.GET, "/api/mock/**");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        OpenIDRelyingPartyFilter authFilter = new OpenIDRelyingPartyFilter(authenticationManager(), allowParameter);
        authFilter.setSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> {

        });
        authFilter.setFailureHandler(new OpenIDRelyingPartyAuthenticationFailureHandler());
        http.addFilterBefore(authFilter, LogoutFilter.class)
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(new CommonCorsAuthenticationEntryPoint())
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/api/contract/**", "/api/tenant/**").authenticated()
                .antMatchers(HttpMethod.POST, "/api/contract/**", "/api/tenant/**").authenticated().antMatchers(HttpMethod.PUT, "/api/contract/**", "/api/tenant/**").authenticated()
                .antMatchers(HttpMethod.PATCH, "/api/contract/**", "/api/tenant/**").authenticated().antMatchers(HttpMethod.DELETE, "/api/contract/**", "/api/tenant/**")
                .authenticated();
    }

}
