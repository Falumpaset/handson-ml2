package de.immomio.controller.mock;

import de.immomio.controller.base.BaseExternalAuthController;
import de.immomio.service.mock.auth.ExternalAuthenticationServiceMock;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Niklas Lindemann
 */

@RestController
@RequestMapping("/api/mock/auth")
@Tag(name = "Authorization (Mock)")
public class ExternalAuthMockController extends BaseExternalAuthController<ExternalAuthenticationServiceMock> {

    public ExternalAuthMockController(ExternalAuthenticationServiceMock authenticationService) {
        super(authenticationService);
    }

}
