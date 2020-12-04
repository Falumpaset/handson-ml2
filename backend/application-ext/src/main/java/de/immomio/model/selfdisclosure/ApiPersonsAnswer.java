package de.immomio.model.selfdisclosure;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
public class ApiPersonsAnswer implements Serializable {
    private static final long serialVersionUID = -1984665821152924478L;

    private ApiPersonAnswer answer;

    private List<ApiSelfDisclosureSubQuestionAnswer> subQuestions;

    private List<ApiConfirmation> confirmations;

}
