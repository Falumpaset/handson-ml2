package de.immomio.controller.credential;

import de.immomio.data.base.type.credential.CredentialProperty;
import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.landlord.entity.credential.Credential;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MapKeyClass;
import javax.persistence.MapKeyEnumerated;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
@Setter
public class CredentialCheckBean {

    @NotNull
    @Enumerated(EnumType.STRING)
    private Portal portal;

    @NotNull
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyClass(CredentialProperty.class)
    private Map<String, String> properties;

    public Credential toCredential() {
        Credential tmp = new Credential();

        tmp.setPortal(portal);
        tmp.setProperties(properties);
        tmp.setEncrypted(false);

        return tmp;
    }
}
