package de.immomio.exporter.portal;

import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.landlord.entity.credential.Credential;
import de.immomio.exporter.config.FtpProperties;
import de.immomio.exporter.openimmo.OpenImmoBasedFtpExporter;
import de.immomio.exporter.openimmo.converter.OpenImmoConverter;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Johannes Hiemer.
 */
public abstract class ImmoweltExporter extends OpenImmoBasedFtpExporter {

    @Value("${ftp.iw.server}")
    private String host;

    @Value("${ftp.iw.port}")
    private int port;

    protected ImmoweltExporter(Portal portal, OpenImmoConverter converter) {
        super(portal, converter);
    }

    @Override
    protected FtpProperties getFtpProperties(Credential credential, String encryptionKey) {
        return new FtpProperties(credential, encryptionKey, host, port);
    }
}
