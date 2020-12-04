package de.immomio.security;

import org.springframework.data.spel.spi.EvaluationContextExtension;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Johannes Hiemer
 */
public class SecurityEvaluationContextExtension implements EvaluationContextExtension {

    @Override
    public String getExtensionId() {
        return "security_immomio";
    }

    @Override
    public SecurityExpressionRoot getRootObject() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityExpressionRoot securityExpressionRoot = new SecurityExpressionRoot(authentication) {
        };
        securityExpressionRoot.setDefaultRolePrefix("");

        return securityExpressionRoot;
    }

}
