package de.immomio.model.address;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author Niklas Lindemann
 */
@Getter
@Setter
@NoArgsConstructor
@Schema(name = "PersonAddressInformation")
public class ApiPersonAddressAnswer extends ApiAddressAnswer {
    private static final long serialVersionUID = 7346421101781644588L;

    @NotNull
    @Schema(description = "the email address")
    protected String email;

    public ApiPersonAddressAnswer(String city, String street, String houseNumber, String zipCode, String federalProvince, String email) {
        super(city, street, houseNumber, zipCode, federalProvince);
        this.email = email;
    }
}
