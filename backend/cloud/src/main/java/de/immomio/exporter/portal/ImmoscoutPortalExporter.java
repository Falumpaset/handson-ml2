package de.immomio.exporter.portal;

import de.immomio.data.base.type.portal.Portal;
import de.immomio.exporter.amazon.s3.BrokerS3FileManager;
import de.immomio.exporter.immoscout.ImmoscoutExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImmoscoutPortalExporter extends ImmoscoutExporter {

    private static final long CHANNEL = 10000;

    private final BrokerS3FileManager brokerS3FileManager;

    @Autowired
    public ImmoscoutPortalExporter(BrokerS3FileManager brokerS3FileManager) {
        super(Portal.IMMOBILIENSCOUT24_DE);
        this.brokerS3FileManager = brokerS3FileManager;
    }

    @Override
    protected long getChannel() {
        return CHANNEL;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected BrokerS3FileManager getS3FileManager() {
        return brokerS3FileManager;
    }
}
