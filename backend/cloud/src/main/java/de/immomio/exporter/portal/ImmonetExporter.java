package de.immomio.exporter.portal;

import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.landlord.entity.credential.Credential;
import de.immomio.exporter.config.FtpProperties;
import de.immomio.exporter.openimmo.OpenImmoBasedFtpExporter;
import de.immomio.exporter.openimmo.converter.OpenImmoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister.
 */
@Service
public class ImmonetExporter extends OpenImmoBasedFtpExporter {

    @Value("${ftp.im.server}")
    private String host;

    @Value("${ftp.im.port}")
    private int port;

    @Autowired
    public ImmonetExporter(OpenImmoConverter converter) {
        super(Portal.IMMONET_DE, converter);
    }

    @Override
    protected FtpProperties getFtpProperties(Credential credential, String encryptionKey) {
        return new FtpProperties(credential, encryptionKey, host, port);
    }

    @Override
    public List<Portal> getReleasedPortals() {
        List<Portal> list = super.getReleasedPortals();

        list.add(Portal.IMMOWELT_DE);

        return list;
    }
}
