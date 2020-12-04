package de.immomio.controller.product.addon;

import de.immomio.beans.ProductAddonPriceAndTypeBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.product.LandlordProduct;
import de.immomio.service.landlord.product.price.PriceCalculationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Controller
@RequestMapping(value = "/productAddons")
public class ProductAddonController {

    private final PriceCalculationService priceCalculationService;

    public ProductAddonController(PriceCalculationService priceCalculationService) {
        this.priceCalculationService = priceCalculationService;
    }

    @GetMapping(value = "/customerPrice/{customer}/product/{product}")
    public ResponseEntity customerPrice(
            @ModelAttribute("customer") @Valid LandlordCustomer customer,
            @ModelAttribute("product") @Valid LandlordProduct landlordProduct,
            BindingResult result
    ) {

        if (result.hasErrors()) {
            return new ResponseEntity<>(new CollectionModel<>(result.getAllErrors()), HttpStatus.BAD_REQUEST);
        }
        Map<Long, ProductAddonPriceAndTypeBean> addonPrices =
                priceCalculationService.getAddonPrices(customer, landlordProduct);
        return new ResponseEntity<>(addonPrices, OK);
    }
}
