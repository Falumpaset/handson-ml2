package de.immomio.exporter.utils.credential;

import de.immomio.common.encryption.CryptoUtils;
import de.immomio.common.encryption.exception.CryptoException;
import de.immomio.data.base.type.credential.CredentialProperty;
import de.immomio.data.landlord.entity.credential.Credential;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CredentialUtils {

    private CredentialUtils() {
    }

    public static String getToken(Credential credential, String encryptionKey) {
        return getValueByCredentialKey(credential, CredentialProperty.TOKEN.name(), encryptionKey);
    }

    public static String getTokenSecret(Credential credential, String encryptionKey) {
        return getValueByCredentialKey(credential, CredentialProperty.TOKEN_SECRET.name(), encryptionKey);
    }

    public static String getUsername(Credential credential, String encryptionKey) {
        return getValueByCredentialKey(credential, CredentialProperty.USERNAME.name(), encryptionKey);
    }

    public static String getPassword(Credential credential, String encryptionKey) {
        return getValueByCredentialKey(credential, CredentialProperty.PASSWORD.name(), encryptionKey);
    }

    public static String getHost(Credential credential, String encryptionKey) {
        return getValueByCredentialKey(credential, CredentialProperty.HOST.name(), encryptionKey);
    }

    public static String getType(Credential credential, String encryptionKey) {
        return getValueByCredentialKey(credential, CredentialProperty.TYPE.name(), encryptionKey);
    }

    public static Integer getPort(Credential credential, String encryptionKey) {
        String port = getValueByCredentialKey(credential, CredentialProperty.PORT.name(), encryptionKey);
        return port != null ? Integer.valueOf(port) : null;
    }

    private static String getValueByCredentialKey(Credential credential, String credentialKey, String encryptionKey) {
        String value = credential.getProperties().get(credentialKey);
        return value != null && credential.getEncrypted() ? decryptValue(value, encryptionKey) : value;
    }

    private static String decryptValue(String value, String encryptionKey) {
        try {
            return CryptoUtils.decryptString(encryptionKey, value);
        } catch (CryptoException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
