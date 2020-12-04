package de.immomio.beans.landlord.email;

import de.immomio.mail.sender.templates.MailTemplate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TemplateResponseBean {

    @NotNull
    private MailTemplate template;

    @NotNull
    private String templateText;

    private String templateSubject;

}
