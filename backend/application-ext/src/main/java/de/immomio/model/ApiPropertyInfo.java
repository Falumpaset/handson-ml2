package de.immomio.model;

import de.immomio.model.address.ApiPropertyAddressAnswer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "PropertyInformation")
public class ApiPropertyInfo implements Serializable {
    private static final long serialVersionUID = 7757978980320716706L;

    @NotBlank
    @Schema(description = "the id of the property (the openimmo internalid)")
    private String id;
    @Schema(description = "the name of the property")
    private String name;
    @NotNull
    @Schema(description = "the address of the property")
    private ApiPropertyAddressAnswer address;
    @Schema(description = "the floor of the property")
    private Integer floor;
    @NotNull
    @Schema(description = "the amount of rooms")
    private Double rooms;
    @NotNull
    @Schema(description = "the size in square meters")
    private Double size;
    @NotNull
    @Schema(description = "the base rent of the property")
    private Double rent;
    @Schema(description = "the heating cost of the property")
    private Double heatingCost;
    @Schema(description = "the additional costs of the property")
    private Double additionalCosts;
    @NotNull
    @Schema(description = "the deposit costs of the property")
    private Double deposit;
}
