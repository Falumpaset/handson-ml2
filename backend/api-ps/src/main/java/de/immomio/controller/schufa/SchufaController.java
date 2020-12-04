package de.immomio.controller.schufa;

import de.immomio.data.propertysearcher.entity.schufa.SchufaKoopOrderRequest;
import de.immomio.data.propertysearcher.entity.schufa.delivery.IdentDataWrapper;
import de.immomio.data.propertysearcher.entity.schufa.payment.SepaOrder;
import de.immomio.schufa.client.SchufaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Niklas Lindemann
 */

@RequestMapping("/schufa")
@RestController
public class SchufaController {

    private SchufaClient schufaClient;

    private String IDENT_DATA_REQUIRED = "IDENT_DATA_REQUIRED_L";

    @Autowired
    public SchufaController(SchufaClient schufaClient) {
        this.schufaClient = schufaClient;
    }

    @PostMapping("/createOrder")
    public ResponseEntity createOrder(@RequestBody SchufaKoopOrderRequest request) {
        return schufaClient.createOrder(request);
    }

    @PostMapping("/createPaymentDetails")
    public ResponseEntity createPaymentDetails(@RequestBody SepaOrder sepaOrder, @RequestParam("orderNumber") String orderNumber) {
        return schufaClient.createPaymentDetails(sepaOrder, orderNumber);
    }

    @PostMapping("/liablePayOrder")
    public ResponseEntity liablePayOrder(@RequestParam("orderNumber") String orderNumber) {
        return schufaClient.liablePayOrder(orderNumber);
    }

    @PostMapping("/deliver")
    public ResponseEntity deliver(
            @RequestParam("deliverNumber") String deliverNumber,
            @RequestParam("digital") Boolean digital,
            @RequestBody(required = false) IdentDataWrapper identDataWrapper
    ) {
        if (digital && (identDataWrapper == null || identDataWrapper.getIdentData() == null)) {
            return ResponseEntity.badRequest().body(IDENT_DATA_REQUIRED);
        }
        return digital ? schufaClient.deliverDigital(identDataWrapper, deliverNumber) : schufaClient.deliverWritten(deliverNumber);
    }
}
