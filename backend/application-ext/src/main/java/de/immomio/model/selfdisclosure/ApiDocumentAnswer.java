package de.immomio.model.selfdisclosure;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "DocumentInformation")
public class ApiDocumentAnswer implements Serializable {
    private static final long serialVersionUID = -7290338287002709208L;
    @Schema(description = "the description of the document", example = "WBS Schein")
    private String description;
    @Schema(description = "the filename of the document", example = "wbs_schein.pdf")
    private String fileName;
}
