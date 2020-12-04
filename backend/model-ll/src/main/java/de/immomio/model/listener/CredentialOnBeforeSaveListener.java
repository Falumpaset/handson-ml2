package de.immomio.model.listener;

import de.immomio.common.encryption.CryptoUtils;
import de.immomio.common.encryption.exception.CryptoException;
import de.immomio.data.landlord.entity.credential.Credential;
import de.immomio.model.repository.landlord.customer.credential.LandlordCredentialRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import java.util.Map.Entry;
import java.util.Optional;

/**
 * @author Bastian Bliemeister.
 */
@Component
@Slf4j
@RepositoryEventHandler(Credential.class)
public class CredentialOnBeforeSaveListener {

    @Autowired
    private LandlordCredentialRepository credentialRepository;

    @Value("${encryption.credentials.key}")
    private String encryptionKey;

    @HandleBeforeSave
    @HandleBeforeCreate
    public void handleBeforeSave(Credential credential) {
        log.debug("credentialOnBeforeSave-event");

        boolean updated;
        try {
            updated = credential.isNew() ? createCredential(credential) : updateCredential(credential);
        } catch (CryptoException e) {
            log.error("Exception saving credentials ...", e);

            updated = false;
        }

        credential.setEncrypted(Boolean.TRUE);

        if (!updated) {
            log.error("Problem saving Credential -> " + credential);
        }
    }

    private boolean updateCredential(Credential credential) throws CryptoException {
        Optional<Credential> oldCredential = credentialRepository.findById(credential.getId());

        if (oldCredential.isPresent()) {
            Credential data = oldCredential.get();
            for (Entry<String, String> entry : credential.getProperties().entrySet()) {
                String oldValue = data.getProperties().get(entry.getKey());

                if (oldValue != null && data.getEncrypted() && oldValue.equals(entry.getValue())) {
                    continue;
                }

                entry.setValue(CryptoUtils.encryptString(encryptionKey, entry.getValue()));
            }
        } else {
            return false;
        }

        return true;
    }

    private boolean createCredential(Credential credential) throws CryptoException {
        for (Entry<String, String> entry : credential.getProperties().entrySet()) {
            entry.setValue(CryptoUtils.encryptString(encryptionKey, entry.getValue()));
        }

        return true;
    }
}
