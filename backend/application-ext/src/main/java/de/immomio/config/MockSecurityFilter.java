package de.immomio.config;

import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Niklas Lindemann
 */
@Component
public class MockSecurityFilter implements Filter {

    public static final String TOKEN = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI0ZWEwY0hLUHhzV3Q2clhBQy13a251UkNEaHRBQlJ0ZjRxSHJsNGdObDk0In0.eyJqdGkiOiI1MmFjZGFmNy1lZmU1LTQ1Y2QtYjliMC1kYzlkNjVmZmU4NzciLCJleHAiOjE3MzgwNDU1MjMsIm5iZiI6MCwiaWF0IjoxNTY1MjQ1NTIzLCJpc3MiOiJodHRwczovL3Nzby5pbnQuaW1tb21pby5jb20vYXV0aC9yZWFsbXMvaW1tb21pby1pbnQiLCJhdWQiOiJpbW1vbWlvLW9pZGMiLCJzdWIiOiI3MGJi";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        String authorization = httpRequest.getHeader("Authorization");
        String servletPath = httpRequest.getServletPath();
        if (!servletPath.contains("/mock")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        if (!TOKEN.equals(authorization) && !(servletPath.contains("auth/token"))) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            httpServletResponse.setStatus(401);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}

