/**
 *
 */
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
public class EbayExporter extends OpenImmoBasedFtpExporter {

    @Value("${ftp.ebay.server}")
    private String host;

    @Value("${ftp.ebay.port}")
    private int port;

    @Autowired
    public EbayExporter(OpenImmoConverter converter) {
        super(Portal.EBAY, converter);
    }

    @Override
    protected FtpProperties getFtpProperties(Credential credential, String encryptionKey) {
        return new FtpProperties(credential, encryptionKey, host, port);
    }
}
