package de.immomio.model.contract;

import de.immomio.constants.GenderType;
import de.immomio.model.address.ApiPersonAddressAnswer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
@Schema(description = "the base informations of a signer")
public class SignerInfo implements Serializable {
    private static final long serialVersionUID = -4704977175590713605L;

    @NotNull
    @Schema(description = "the gender")
    private GenderType gender;
    @NotNull
    @Schema(description = "the address")
    private ApiPersonAddressAnswer address;
    @NotNull
    @Schema(description = "the firstname")
    private String firstname;
    @NotNull
    @Schema(description = "the lastname")
    private String lastname;

}
