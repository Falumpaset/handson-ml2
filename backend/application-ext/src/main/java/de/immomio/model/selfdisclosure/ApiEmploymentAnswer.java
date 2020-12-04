package de.immomio.model.selfdisclosure;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
@Schema(name = "EmploymentInformation")
public class ApiEmploymentAnswer implements Serializable {
    private static final long serialVersionUID = -6098934236199332868L;

    @Schema(description = "job title")
    private String job;

    @Schema(description = "name of the employer")
    private String employer;

    @Schema(description = "net income")
    private Double netIncome;
}
