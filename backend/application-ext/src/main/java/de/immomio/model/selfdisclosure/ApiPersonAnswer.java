package de.immomio.model.selfdisclosure;

import de.immomio.constants.GenderType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "ExtendedPersonalInformations", description = "contains birth name and birth place in addition to the base personal informations")
public class ApiPersonAnswer extends ApiChildAnswer {

    private static final long serialVersionUID = -6714494128068190954L;

    @Schema(description = "birth name")
    private String birthName;

    @Schema(description = "birth place")
    private String birthPlace;

    private GenderType gender;
}