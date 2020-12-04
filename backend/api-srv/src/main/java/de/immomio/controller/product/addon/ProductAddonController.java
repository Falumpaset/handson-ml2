package de.immomio.controller.product.addon;

import de.immomio.beans.ProductAddonPriceAndTypeBean;
import de.immomio.controller.product.CalculatePriceBean;
import de.immomio.data.landlord.bean.product.LandlordProductAddonBean;
import de.immomio.data.landlord.bean.product.LandlordProductAddonDiscountOverviewBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.product.LandlordProduct;
import de.immomio.data.landlord.entity.product.productaddon.LandlordProductAddon;
import de.immomio.service.addon.AddonDiscountPriceCalculationService;
import de.immomio.service.landlord.addon.LandlordAddonService;
import de.immomio.service.landlord.product.price.PriceCalculationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Map;
import java.util.Set;

import static org.springframework.http.HttpStatus.OK;

/**
 * @author Maik Kingma
 */

@Slf4j
@Controller
@RequestMapping(value = "/ll-productAddons")
public class ProductAddonController {

    private final PriceCalculationService priceCalculationService;
    private final AddonDiscountPriceCalculationService addonDiscountPriceCalculationService;

    private final LandlordAddonService addonService;

    public ProductAddonController(PriceCalculationService priceCalculationService,
            AddonDiscountPriceCalculationService addonDiscountPriceCalculationService,
            LandlordAddonService addonService) {
        this.priceCalculationService = priceCalculationService;
        this.addonDiscountPriceCalculationService = addonDiscountPriceCalculationService;
        this.addonService = addonService;
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

    @PostMapping(value = "/{addon}/calculatePrice")
    public ResponseEntity calculatePrice(
            @PathVariable("addon") @Valid LandlordProductAddon addon,
            @RequestBody @Valid CalculatePriceBean calculatePrice,
            BindingResult result
    ) {

        if (result.hasErrors()) {
            return new ResponseEntity<>(new CollectionModel<>(result.getAllErrors()), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(priceCalculationService.calcAddonPrice(addon, calculatePrice.getLocation(),
                calculatePrice.getPriceMultiplier(), calculatePrice.getDiscount(), calculatePrice.getAddonDiscount()), OK);
    }

    @GetMapping(value = "/search/findAvailableByProduct")
    public ResponseEntity<Set<LandlordProductAddonBean>> getAvailableAddons(
            @RequestParam("product") LandlordProduct landlordProduct
    ) {
        return ResponseEntity.ok(addonService.getAllAddonsByProduct(landlordProduct));
    }

    @PostMapping(value = "/listAddonPrices")
    public ResponseEntity<Set<LandlordProductAddonBean>> getAvailableAddons(
            @RequestBody LandlordProductAddonDiscountOverviewBean discountOverviewBean
    ) {
        return ResponseEntity.ok(addonDiscountPriceCalculationService.getAllAddonsWithDiscount(discountOverviewBean));
    }
}
