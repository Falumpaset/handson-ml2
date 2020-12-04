package de.immomio.service.base.auth;

import de.immomio.constants.exceptions.NotAuthorizedException;

/**
 * @author Niklas Lindemann
 */
public interface ExternalAuthenticationService {
    public String getAuthToken(String username, String password) throws NotAuthorizedException;
}
