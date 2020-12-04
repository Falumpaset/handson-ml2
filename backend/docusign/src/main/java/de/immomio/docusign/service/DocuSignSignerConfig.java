package de.immomio.docusign.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Andreas Hansen.
 */
@Configuration
@Getter
@Setter
public class DocuSignSignerConfig {

    @Value("${docusign.envelope.document.viewingDateCombined.name}")
    private String documentViewingDateCombinedName;

    @Value("${docusign.envelope.document.viewingDateCombined.extension}")
    private String documentViewingDateCombinedExtension;

    @Value("${docusign.envelope.document.viewingDateCombined.path}")
    private String documentViewingDateCombinedPath;

    @Value("${docusign.envelope.document.viewingDateCombined.docuSignTemplateId}")
    private String documentViewingDateCombinedDocuSignTemplateId;

    @Value("${docusign.envelope.document.identityConfirmation.name}")
    private String documentIdentityConfirmationName;

    @Value("${docusign.envelope.document.identityConfirmation.extension}")
    private String documentIdentityConfirmationExtension;

    @Value("${docusign.envelope.document.identityConfirmation.path}")
    private String documentIdentityConfirmationPath;

    @Value("${docusign.envelope.document.identityConfirmation.docuSignTemplateId}")
    private String documentIdentityConfirmationDocuSignTemplateId;

}
