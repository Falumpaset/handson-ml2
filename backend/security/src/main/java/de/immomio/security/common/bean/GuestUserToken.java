package de.immomio.security.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestUserToken extends AbstractToken {

    private static final long serialVersionUID = 781690587567808765L;

    private Long applicationId;
}
