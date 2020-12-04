package de.immomio.controller.product.basket;

import de.immomio.billing.exception.InvoiceGenerationException;
import de.immomio.constants.CheckoutResult;
import de.immomio.constants.exceptions.BasketCheckoutException;
import de.immomio.data.base.bean.product.basket.BasketValueBean;
import de.immomio.data.base.bean.product.basket.CheckoutBean;
import de.immomio.data.base.bean.product.basket.quota.LandlordQuotaBasketBean;
import de.immomio.data.landlord.entity.product.basket.quota.LandlordQuotaBasket;
import de.immomio.landlord.service.product.quota.LandlordQuotaBasketHandler;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RepositoryRestController
@RequestMapping(path = "/productBaskets/quota")
public class ProductBasketQuotaController {

    private final LandlordQuotaBasketHandler basketHandler;

    @Autowired
    public ProductBasketQuotaController(LandlordQuotaBasketHandler basketHandler) {
        this.basketHandler = basketHandler;
    }

    @PostMapping(value = "/{id}/checkout")
    public ResponseEntity checkout(
            @PathVariable Long id, @RequestBody @Valid CheckoutBean checkout,
            BindingResult result
    ) {
        try {
            CheckoutResult checkoutResult = basketHandler.checkoutBasket(id, checkout);
        } catch (BasketCheckoutException e) {
            result.addError(e.getObjectError());

            return ResponseEntity.status(e.getStatus()).body(e.getObjectError().getDefaultMessage());
        }
        return ResponseEntity.accepted().build();
    }

    @PostMapping(value = "/basketValue")
    public ResponseEntity basketValue(@RequestBody @Valid LandlordQuotaBasketBean basketBean) {
        try {
            BasketValueBean basketValueBean = basketHandler.getBasketValue(basketBean);

            return new ResponseEntity<>(new EntityModel<>(basketValueBean), HttpStatus.OK);
        } catch (InvoiceGenerationException e) {
            log.error(e.getMessage(), e);

            return new ResponseEntity<>("ERROR_AT_CREATING_BASKET_L", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/create")
    public ResponseEntity createBasket(@RequestBody @Valid LandlordQuotaBasketBean basketBean, @Parameter(hidden = true)  PersistentEntityResourceAssembler assembler) {
        LandlordQuotaBasket savedBasket = basketHandler.createAndSaveQuotaBasket(basketBean);

        return ResponseEntity.ok(assembler.toModel(savedBasket));
    }
}
