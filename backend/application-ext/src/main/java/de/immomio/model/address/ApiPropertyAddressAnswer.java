package de.immomio.model.address;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "PropertyAddressInformation")
public class ApiPropertyAddressAnswer extends ApiAddressAnswer {

    private static final long serialVersionUID = -1935226296210227624L;

    @NotBlank
    @Schema(description = "the federal province")
    private String federalProvince;

    public ApiPropertyAddressAnswer(String city, String street, String houseNumber, String zipCode, String federalProvince) {
        super(city, street, houseNumber, zipCode, federalProvince);
        setFederalProvince(federalProvince);
    }
}
