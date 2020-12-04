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
 * @author Bastian Bliemeister.
 */
@Service
public class WordpressPluginExporter extends OpenImmoBasedFtpExporter {

    @Value("${ftp.wpp.server}")
    private String host;

    @Value("${ftp.wpp.port}")
    private int port;

    @Autowired
    protected WordpressPluginExporter(OpenImmoConverter converter) {
        super(Portal.WORDPRESS_PLUGIN, converter);
    }

    @Override
    protected FtpProperties getFtpProperties(Credential credential, String encryptionKey) {
        FtpProperties ftpProperties = new FtpProperties(credential, encryptionKey);

        if (ftpProperties.getHost() == null || ftpProperties.getPort() == null) {
            ftpProperties.setHost(host);
            ftpProperties.setPort(port);
        }
        return ftpProperties;
    }
}
