package de.immomio.controller.impl;

import de.immomio.controller.base.BaseExternalAuthController;
import de.immomio.service.impl.auth.ExternalAuthenticationServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Niklas Lindemann
 */

@Tag(name = "Authorization")
@RestController
@RequestMapping("/api/auth")
public class ExternalAuthController extends BaseExternalAuthController<ExternalAuthenticationServiceImpl> {

    @Autowired
    public ExternalAuthController(ExternalAuthenticationServiceImpl authenticationService) {
        super(authenticationService);
    }

}
