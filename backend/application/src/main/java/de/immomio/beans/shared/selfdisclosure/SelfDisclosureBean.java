package de.immomio.beans.shared.selfdisclosure;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelfDisclosureBean implements Serializable {

    private static final long serialVersionUID = -1394234513909370554L;

    private Long id;

    private String feedbackEmail;

    private String description;

    private List<SelfDisclosureQuestionBean> questions;

    private List<String> documents;

    private List<String> confirmations;

}
