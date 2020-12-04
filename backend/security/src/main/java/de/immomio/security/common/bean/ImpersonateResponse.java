package de.immomio.security.common.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ImpersonateResponse implements Serializable {

    private static final long serialVersionUID = -5372723596986491895L;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    @JsonProperty("refresh_expires_in")
    private Integer refreshExpiresIn;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("requested_token_type")
    private String requestedTokenType;

    @JsonProperty("session_state")
    private String sessionState;
}
