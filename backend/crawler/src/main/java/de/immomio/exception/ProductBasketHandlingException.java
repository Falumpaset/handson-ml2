package de.immomio.exception;

import de.immomio.data.landlord.entity.product.basket.LandlordProductBasket;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Maik Kingma
 */

@Getter
@Setter
public class ProductBasketHandlingException extends Exception {

    private static final long serialVersionUID = -3239878963652328173L;

    private LandlordProductBasket basket;

    public ProductBasketHandlingException(String message, Throwable cause, LandlordProductBasket basket) {
        super(message, cause);
        this.basket = basket;
    }
}
