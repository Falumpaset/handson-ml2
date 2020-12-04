package de.immomio.model.selfdisclosure;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
@Schema(name = "TenantInformation")
public class ApiSdResponseTenant {
    @Schema(description = "personal information")
    private ApiPersonAnswer personDetails;
    @Schema(description = "address information")
    private ApiTenantAddress addressDetails;
    @Schema(description = "employment information")
    private ApiEmploymentAnswer employmentDetails;
    @Schema(description = "list of confirmed answers")
    private List<ApiCheckedAnswer> checkedAnswers = new ArrayList<>();

}
