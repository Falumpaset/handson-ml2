package de.immomio.model.selfdisclosure;

import de.immomio.model.ApiS3File;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
public class ApiSelfDisclosureSubQuestionAnswer implements Serializable {
    private static final long serialVersionUID = 4960240675720337074L;

    private Long id;

    private SelfDisclosureSubQuestionType type;

    private String title;

    private Object answer;

    private String comment;

    private String commentHint;

    private Boolean commentAllowed;

    private Boolean showSelfDisclosureQuestions;

    private ApiS3File upload;

}
