package de.immomio.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author Johannes Hiemer.
 */
public class DefaultCorsConfiguration {

    public static FilterRegistrationBean corsConfiguration() {
        return customCorsConfiguration(null, null);
    }

    public static FilterRegistrationBean customCorsConfiguration(String[] extraAllowedHeaders,
                                                                 String[] extraExposedHeaders) {
        if (extraAllowedHeaders == null) {
            extraAllowedHeaders = new String[0];
        }

        if (extraExposedHeaders == null) {
            extraExposedHeaders = new String[0];
        }

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("Authorization");
        config.addAllowedHeader("Content-Type");
        config.addAllowedHeader("Accept");
        config.addAllowedHeader("X-Auth-Token");
        config.addAllowedHeader("Access-Control-Request-Headers");
        config.addAllowedHeader("Access-Control-Request-Method");

        for (String tmp : extraAllowedHeaders) {
            config.addAllowedHeader(tmp);
        }

        config.addExposedHeader("X-Auth-Token");
        config.addExposedHeader("WWW-Authenticate");

        for (String tmp : extraExposedHeaders) {
            config.addExposedHeader(tmp);
        }

        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");

        source.registerCorsConfiguration("/**", config);

        final FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(Ordered.LOWEST_PRECEDENCE);

        return bean;
    }
}
