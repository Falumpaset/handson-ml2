package de.immomio.schufa.client;

import de.immomio.data.propertysearcher.entity.schufa.SchufaKoopOrderRequest;
import de.immomio.data.propertysearcher.entity.schufa.SchufaKoopOrderResponse;
import de.immomio.data.propertysearcher.entity.schufa.delivery.DeliveryResponse;
import de.immomio.data.propertysearcher.entity.schufa.delivery.IdentDataWrapper;
import de.immomio.data.propertysearcher.entity.schufa.delivery.WrittenDelivery;
import de.immomio.data.propertysearcher.entity.schufa.order.LiableToPayCostsOrder;
import de.immomio.data.propertysearcher.entity.schufa.payment.SepaOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author Niklas Lindemann
 */

@Component
public class SchufaClient {

    private static final String ORDER_URL = "bestellungen";
    private static final String PAYMENT_URL = "bestellungen/%s/zahldaten/sepaLev";
    private static final String ORDERED_URL = "bestellungen/%s/kostenpflichtigBestellt";
    private static final String DIGITAL_DELIVERY_URL = "lieferungen/%s/digitaleLieferung";
    private static final String WRITTEN_DELIVERY_URL = "lieferungen/%s/schriftlicheLieferung";

    private SchufaRestConnector connector;

    @Autowired
    public SchufaClient(SchufaRestConnector connector) {
        this.connector = connector;
    }

    public ResponseEntity createOrder(SchufaKoopOrderRequest request) {
        return connector.runRequest(ORDER_URL, request, SchufaKoopOrderResponse.class);
    }

    public ResponseEntity createPaymentDetails(SepaOrder sepaOrder, String orderNumber) {
        return connector.runRequest(
                String.format(PAYMENT_URL, orderNumber),
                sepaOrder,
                SchufaKoopOrderResponse.class
        );
    }

    public ResponseEntity liablePayOrder(String orderNumber) {
        LiableToPayCostsOrder liableToPayCostsOrder = new LiableToPayCostsOrder(true);
        return connector.runRequest(
                String.format(ORDERED_URL, orderNumber),
                liableToPayCostsOrder,
                DeliveryResponse.class
        );
    }

    public ResponseEntity deliverDigital(IdentDataWrapper identData, String orderNumber) {
        return connector.runRequest(
                String.format(DIGITAL_DELIVERY_URL, orderNumber),
                identData,
                DeliveryResponse.class);
    }

    public ResponseEntity deliverWritten(String orderNumber) {
        WrittenDelivery writtenDelivery = new WrittenDelivery(true);
        return connector.runRequest(
                String.format(WRITTEN_DELIVERY_URL, orderNumber),
                writtenDelivery,
                DeliveryResponse.class);
    }
}
