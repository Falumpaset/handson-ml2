package de.immomio.beans.shared.selfdisclosure;

import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosureQuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelfDisclosureQuestionBean implements Serializable {

    private static final long serialVersionUID = -8258705569708241891L;

    private Long id;

    private String title;

    private SelfDisclosureQuestionType type;

    private boolean mandatory = false;

    private boolean hidden = true;

    private List<SelfDisclosureSubQuestionBean> subQuestions = new ArrayList<>();

    private Boolean commentAllowed;

    private String commentHint;

    private int orderNumber;

    private Boolean uploadAllowed;

    private String uploadHint;

}
