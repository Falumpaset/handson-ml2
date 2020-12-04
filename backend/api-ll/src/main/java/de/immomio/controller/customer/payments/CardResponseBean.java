package de.immomio.controller.customer.payments;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardResponseBean {

    public String stripeId;

    public String cardToken;

    public CardResponseBean(String stripeId, String cardToken) {
        this.stripeId = stripeId;
        this.cardToken = cardToken;
    }
}
