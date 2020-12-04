package de.immomio.controller.product.basket;

import de.immomio.beans.IdBean;
import de.immomio.billing.exception.InvoiceGenerationException;
import de.immomio.constants.CheckoutResult;
import de.immomio.constants.exceptions.BasketCheckoutException;
import de.immomio.data.base.bean.product.basket.BasketValueBean;
import de.immomio.data.base.bean.product.basket.CheckoutBean;
import de.immomio.data.base.bean.product.basket.LandlordProductBasketBean;
import de.immomio.data.landlord.entity.product.basket.LandlordProductBasket;
import de.immomio.landlord.service.product.LandlordBasketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * @author Bastian Bliemeister, Maik Kingma.
 */

@Slf4j
@Controller
@RequestMapping(value = "/productBaskets")
public class ProductBasketController {

    public static final String PAYMENT_METHOD_NOT_SET = "PAYMENT_METHOD_NOT_SET_L";
    private final LandlordBasketHandler basketHandler;

    @Autowired public ProductBasketController(LandlordBasketHandler basketHandler) {
        this.basketHandler = basketHandler;
    }

    @PostMapping(value = "/{id}/checkout")
    public ResponseEntity checkout(
            @PathVariable Long id, @RequestBody @Valid CheckoutBean checkout,
            BindingResult result
    ) {
        try {
            if (checkout.getPaymentMethod() == null) {
                return ResponseEntity.badRequest().body(PAYMENT_METHOD_NOT_SET);
            }
            CheckoutResult checkoutResult = basketHandler.checkoutBasket(id, checkout);
        } catch (BasketCheckoutException e) {
            result.addError(e.getObjectError());
            return ResponseEntity.status(e.getStatus()).body(e.getObjectError().getDefaultMessage());
        }
        return ResponseEntity.accepted().build();

    }

    @PostMapping(value = "/{id}/checkPaymentRequired")
    public ResponseEntity<EntityModel<Boolean>> checkPaymentRequired(
            @PathVariable Long id,
            @RequestBody @Valid CheckoutBean checkout,
            BindingResult result
    ) {
        try {
            return new ResponseEntity<>(
                    new EntityModel<>(basketHandler.paymentRequired(id, checkout)), HttpStatus.OK);
        } catch (InvoiceGenerationException e) {
            log.error("ERROR checkPaymentRequired", e);
            return new ResponseEntity(new EntityModel<>(result), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/create")
    public ResponseEntity<IdBean> createBasket(@RequestBody @Valid LandlordProductBasketBean basketBean) {
        LandlordProductBasket savedBasket = basketHandler.createAndSaveBasket(basketBean);

        return ResponseEntity.ok(new IdBean(savedBasket.getId()));
    }

    @PostMapping(value = "/basketValue")
    public ResponseEntity<EntityModel<BasketValueBean>> basketValue(
            @RequestBody @Valid LandlordProductBasketBean basketBean,
            BindingResult result
    ) throws InvoiceGenerationException {
        BasketValueBean basketValueBean = basketHandler.basketValue(basketBean);
        return new ResponseEntity<>(new EntityModel<>(basketValueBean), HttpStatus.OK);
    }

}
