package de.immomio.service.mock.auth;

import de.immomio.constants.exceptions.NotAuthorizedException;
import de.immomio.model.auth.ApiAuthorizationBean;
import de.immomio.service.base.auth.ExternalAuthenticationService;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */
@Service
public class ExternalAuthenticationServiceMock implements ExternalAuthenticationService {

    private static final String TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI0ZWEwY0hLUHhzV3Q2clhBQy13a251UkNEaHRBQlJ0ZjRxSHJsNGdObDk0In0.eyJqdGkiOiI1MmFjZGFmNy1lZmU1LTQ1Y2QtYjliMC1kYzlkNjVmZmU4NzciLCJleHAiOjE3MzgwNDU1MjMsIm5iZiI6MCwiaWF0IjoxNTY1MjQ1NTIzLCJpc3MiOiJodHRwczovL3Nzby5pbnQuaW1tb21pby5jb20vYXV0aC9yZWFsbXMvaW1tb21pby1pbnQiLCJhdWQiOiJpbW1vbWlvLW9pZGMiLCJzdWIiOiI3MGJi";
    private final ApiAuthorizationBean VALID_AUTH_BEAN = new ApiAuthorizationBean("testuser", "abc123");

    @Override
    public String getAuthToken(String username, String password) throws NotAuthorizedException {
        if (!ApiAuthorizationBean.builder().username(username).password(password).build().equals(VALID_AUTH_BEAN)) {
            throw new NotAuthorizedException("credentials not found");
        }
        return TOKEN;
    }
}
