package de.immomio.model.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Niklas Lindemann
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "TokenData")
public class TokenResponseBean implements Serializable {
    private static final long serialVersionUID = -3735658841242885584L;
    private String token;
}
