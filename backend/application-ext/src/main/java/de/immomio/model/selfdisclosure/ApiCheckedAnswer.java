package de.immomio.model.selfdisclosure;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "ConfirmedAnswer")
public class ApiCheckedAnswer {
    @Schema(description = "the question", example = "do you accept our license agreement?")
    private String question;
    @Schema(description= "the answer")
    private Boolean answer;
    @Schema(description = "the comment provided by the tenant (only if needed)")
    private String comment = "";
}
