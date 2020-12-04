package de.immomio.security.openid.handler;

import de.immomio.model.abstractrepository.customer.user.AbstractUserRepository;
import de.immomio.data.base.entity.customer.user.AbstractUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Maik Kingma.
 */

@Slf4j
@Service
public abstract class AbstractOpenIDRelyingPartyAuthenticationSuccessHandler<
        U extends AbstractUser,
        AIR extends AbstractUserRepository<U>> implements AuthenticationSuccessHandler {

    public abstract AIR getUserRepository();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) {
    }
}
