package de.immomio.security.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyGuestUserToken extends AbstractToken {

    private static final long serialVersionUID = 781690587567808765L;

    private UUID applyId;
    private String email;
    private Long propertyId;

    public String getEmail() {
        return email != null ? email.toLowerCase() : null;
    }
}
