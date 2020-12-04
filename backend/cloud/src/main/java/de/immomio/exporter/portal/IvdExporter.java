package de.immomio.exporter.portal;

import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.landlord.entity.credential.Credential;
import de.immomio.exporter.config.FtpProperties;
import de.immomio.exporter.openimmo.OpenImmoBasedFtpExporter;
import de.immomio.exporter.openimmo.converter.OpenImmoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Johannes Hiemer.
 */
@Service
public class IvdExporter extends OpenImmoBasedFtpExporter {

    @Value("${ftp.ivd.server}")
    private String host;

    @Value("${ftp.ivd.port}")
    private int port;

    @Autowired
    public IvdExporter(OpenImmoConverter converter) {
        super(Portal.IVD, converter);
    }

    @Override
    protected FtpProperties getFtpProperties(Credential credential, String encryptionKey) {
        return new FtpProperties(credential, encryptionKey, host, port);
    }
}
