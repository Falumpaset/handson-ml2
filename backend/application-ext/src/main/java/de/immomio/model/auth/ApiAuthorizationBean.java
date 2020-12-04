package de.immomio.model.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Niklas Lindemann
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiAuthorizationBean implements Serializable {
    private static final long serialVersionUID = 1005196806702363139L;

    @Schema(example = "testuser")
    private String username;
    @Schema(example = "abc123")
    private String password;
}
