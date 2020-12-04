package de.immomio.model.address;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "AddressInformation")
public class ApiAddressAnswer implements Serializable {

    private static final long serialVersionUID = -7067664272468117679L;

    @NotBlank
    @Schema(description = "the city")
    private String city;
    @NotBlank
    @Schema(description = "the street")
    private String street;
    @NotBlank
    @Schema(description = "the house number")
    private String houseNumber;
    @NotBlank
    @Schema(description = "the zip code")
    private String zipCode;

    @Schema(description = "the federal province")
    private String federalProvince;
}
