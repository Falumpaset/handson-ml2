package de.immomio.controller.base;

import de.immomio.constants.exceptions.NotAuthorizedException;
import de.immomio.model.auth.ApiAuthorizationBean;
import de.immomio.model.auth.TokenResponseBean;
import de.immomio.service.base.auth.ExternalAuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Niklas Lindemann
 */
public abstract class BaseExternalAuthController<AS extends ExternalAuthenticationService> {

    private AS authenticationService;

    public BaseExternalAuthController(AS authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Provides an Bearer token for this user to authenticate", description = "the technical users to access this api can be created inside the immomio app. Currently, the token is valid unlimited. This may be changed in the future.")
    @ApiResponses({@ApiResponse(responseCode = "401", description = "credentials not found"), @ApiResponse(responseCode = "200")})
    public ResponseEntity<TokenResponseBean> getToken(@Parameter(name = "Credentials", description = "Credentials provided by immomio", required = true)  @RequestBody ApiAuthorizationBean authBean) throws NotAuthorizedException {
        String authToken = authenticationService.getAuthToken(authBean.getUsername(), authBean.getPassword());
        return ResponseEntity.ok(new TokenResponseBean(authToken));
    }
}
