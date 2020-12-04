package de.immomio.beans.landlord.email;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TemplateSendRequestBean extends TemplateRequestBean {

    @NotNull
    private Long userId;

    @NotNull
    private String subject;

}
