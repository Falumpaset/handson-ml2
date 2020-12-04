package de.immomio.itp.client;

import de.immomio.data.propertysearcher.entity.itp.ItpCheckRequestBean;
import de.immomio.data.propertysearcher.entity.itp.ItpCheckResponseBean;
import de.immomio.itp.config.ItpWebClientConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author Andreas Hansen
 */
@Component
public class ItpClient {

    private static final String IDENT_CHECK = "/identchecks.json";
    private static final String IDENT_CHECK_STATUS = "/identcheck/status/%s.json";

    private ItpRestConnector connector;

    private ItpWebClientConfiguration itpWebClientConfiguration;

    @Autowired
    public ItpClient(ItpRestConnector connector, ItpWebClientConfiguration itpWebClientConfiguration) {
        this.connector = connector;
        this.itpWebClientConfiguration = itpWebClientConfiguration;
    }

    /**
     * Create a new identcheck instance.
     */
    public Mono<ItpCheckResponseBean> identCheck(ItpCheckRequestBean itpCheckRequestBean) {
        return connector.runPostRequest(
                itpWebClientConfiguration.getBasepath() + IDENT_CHECK,
                itpCheckRequestBean,
                ItpCheckResponseBean.class
        );
    }

    /**
     * Create a new identcheck instance.
     */
    public Mono<ItpCheckResponseBean> mockedIdentCheck(ItpCheckRequestBean itpCheckRequestBean) {
        return connector.runPostRequest(
                itpWebClientConfiguration.getMockbasepath() + IDENT_CHECK,
                itpCheckRequestBean,
                ItpCheckResponseBean.class
        );
    }

    public Mono<ItpCheckResponseBean> identCheckStatus(String identcheckUuid) {
        String statusUrl = itpWebClientConfiguration.getBasepath() + String.format(IDENT_CHECK_STATUS, identcheckUuid);

        return connector.runGetRequest(statusUrl, ItpCheckResponseBean.class);
    }

        public Mono<ItpCheckResponseBean> mockedIdentCheckStatus(String identcheckUuid) {
        String statusUrl = itpWebClientConfiguration.getMockbasepath() + String.format(IDENT_CHECK_STATUS, identcheckUuid);

        return connector.runGetRequest(statusUrl, ItpCheckResponseBean.class);
    }
}
