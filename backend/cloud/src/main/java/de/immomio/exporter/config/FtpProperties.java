package de.immomio.exporter.config;

import de.immomio.data.landlord.entity.credential.Credential;
import de.immomio.exporter.utils.credential.CredentialUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class FtpProperties implements Serializable {

    private static final long serialVersionUID = -4275005895850196967L;

    private String host;

    private Integer port = 21;

    private String username;

    private String password;

    private FtpType type = FtpType.FTP;

    public FtpProperties(Credential credential, String encryptionKey) {
        this.host = CredentialUtils.getHost(credential, encryptionKey);
        this.port = CredentialUtils.getPort(credential, encryptionKey);
        this.username = CredentialUtils.getUsername(credential, encryptionKey);
        this.password = CredentialUtils.getPassword(credential, encryptionKey);
        this.type = convertType(credential, encryptionKey);
    }

    public FtpProperties(Credential credential, String encryptionKey, String host, int port) {
        this.host = host;
        this.port = port;
        this.username = CredentialUtils.getUsername(credential, encryptionKey);
        this.password = CredentialUtils.getPassword(credential, encryptionKey);
        this.type = convertType(credential, encryptionKey);
    }

    private FtpType convertType(Credential credential, String encryptionKey) {
        String ftpType = CredentialUtils.getType(credential, encryptionKey);
        return ftpType != null ? FtpType.getByName(ftpType) : FtpType.FTP;
    }
}
