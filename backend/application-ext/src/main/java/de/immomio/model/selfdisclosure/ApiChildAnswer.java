package de.immomio.model.selfdisclosure;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "BasePersonalInformation")
public class ApiChildAnswer implements Serializable {

    private static final long serialVersionUID = 1785917369513816105L;

    @Schema(description = "the first name")
    protected String firstName;

    @Schema(description = "the last name")
    protected String lastName;

    @Schema(description = "birth date")
    protected Date birthDate;
}
