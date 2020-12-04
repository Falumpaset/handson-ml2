package de.immomio.exception;

import de.immomio.data.landlord.entity.product.LandlordCustomerProduct;
import de.immomio.data.landlord.entity.product.basket.LandlordProductBasket;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Maik Kingma
 */

@Getter
@Setter
public class CustomerProductHandlingException extends Exception {

    private static final long serialVersionUID = -3239878963652328173L;

    private List<LandlordCustomerProduct> customerProduct;

    private LandlordProductBasket basket;

    public CustomerProductHandlingException(String message, Throwable cause,
                                            List<LandlordCustomerProduct> customerProduct,
                                            LandlordProductBasket basket) {
        super(message, cause);
        this.customerProduct = customerProduct;
        this.basket = basket;
    }
}
