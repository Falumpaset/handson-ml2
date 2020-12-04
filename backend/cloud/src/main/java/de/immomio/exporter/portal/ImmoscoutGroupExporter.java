package de.immomio.exporter.portal;

import de.immomio.data.base.type.credential.CredentialProperty;
import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.landlord.entity.credential.Credential;
import de.immomio.exporter.amazon.s3.BrokerS3FileManager;
import de.immomio.exporter.exception.CredentialNotFoundException;
import de.immomio.exporter.immoscout.Immoscout24;
import de.immomio.exporter.immoscout.ImmoscoutExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImmoscoutGroupExporter extends ImmoscoutExporter {

    private static long channel = 10000; // Nur default ... wird ersetzt

    private final BrokerS3FileManager brokerS3FileManager;

    @Autowired
    public ImmoscoutGroupExporter(BrokerS3FileManager brokerS3FileManager) {
        super(Portal.IMMOBILIENSCOUT24_GC_DE);
        this.brokerS3FileManager = brokerS3FileManager;
    }

    @Override
    protected long getChannel() {
        return channel;
    }

    @Override
    protected Immoscout24 immoscout(Credential credential) throws CredentialNotFoundException {
        if (credential == null) {
            throw new CredentialNotFoundException("No credentials provided for endpoint");
        }

        Long parsedChannel;

        try {
            parsedChannel = Long.parseLong(credential.getProperties().get(CredentialProperty.CHANNEL.name()));
        } catch (NumberFormatException e) {
            throw new CredentialNotFoundException("No channel provided for endpoint");
        }

        ImmoscoutGroupExporter.channel = parsedChannel;

        return super.immoscout(credential);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected BrokerS3FileManager getS3FileManager() {
        return brokerS3FileManager;
    }
}
