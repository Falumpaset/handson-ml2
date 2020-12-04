package de.immomio.security;

import de.immomio.model.abstractrepository.customer.user.AbstractUserRepository;
import de.immomio.data.base.entity.customer.user.AbstractUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public abstract class AbstractSecurityService<T extends AbstractUser, R extends AbstractUserRepository<T>> {

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Deprecated
    public boolean hasReadRight(T user) {

        if (user == null) {
            return false;
        }

        T principal = getPrincipal();

        if (principal == null) {
            return false;
        } else if (principal.hasAuthority("is_admin")) {
            return true;
        } else if (user.getCustomer() == null) {
            return false;
        } else if (principal.getCustomer() == null) {
            return false;
        } else if (principal.getId().equals(user.getId())) {
            return true;
        } else {
            return principal.getCustomer().getId().equals(user.getCustomer().getId());
        }

    }

    @Deprecated
    public boolean hasDeleteRight(T user) {
        if (user == null) {
            return false;
        }

        T principal = getPrincipal();

        if (principal == null) {
            return false;
        } else if (Objects.equals(user.getId(), principal.getId())) {
            return false;
        } else if (principal.hasAuthority("is_admin")) {
            return true;
        } else if (!user.getCustomer().getId().equals(principal.getCustomer().getId())) {
            return false;
        } else {
            return principal.hasAuthority("is_commercial");
        }

    }

    @Deprecated
    public boolean hasWriteRight(T user) {
        if (user == null) {
            return false;
        }

        T principal = getPrincipal();

        if (principal == null) {
            return false;
        } else if (user.getId().equals(principal.getId())) {
            return true;
        } else if (principal.hasAuthority("is_admin")) {
            return true;
        } else if (!user.getCustomer().getId().equals(principal.getCustomer().getId())) {
            return false;
        } else {
            return principal.hasAuthority("is_commercial");
        }

    }

    protected abstract R getUserRepository();

    protected abstract T getPrincipal();
}
