package de.immomio.docusign.service.beans;

import com.docusign.esign.client.auth.OAuth;
import lombok.Data;

@Data
public class DocuSignApiAccountBean {

    private OAuth.OAuthToken oAuthToken = null;
    private long expiresIn;
    private String accountBasePath;
    private boolean refetchAccountBasePath = true;

}
