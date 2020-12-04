package de.immomio.controller.product;

import de.immomio.data.landlord.bean.product.LandlordProductOverviewBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.product.LandlordCustomerProduct;
import de.immomio.data.landlord.entity.product.LandlordProduct;
import de.immomio.service.landlord.product.AdminLandlordPlanHandler;
import de.immomio.service.landlord.product.LandlordProductOverviewCollector;
import de.immomio.service.landlord.product.price.PriceCalculationService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
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

import static org.springframework.http.HttpStatus.OK;

/**
 * @author Maik Kingma
 */

@Controller
@RequestMapping(value = "/ll-products")
public class ProductController {

    private final PriceCalculationService priceCalculationService;

    private LandlordProductOverviewCollector overviewCollector;
    private final AdminLandlordPlanHandler planHandler;

    private static final String CUSTOMER_HAS_NO_ACTIVE_PRODUCT_MSG = "CUSTOMER_HAS_NO_ACTIVE_PRODUCT_L";

    @Autowired
    public ProductController(PriceCalculationService priceCalculationService,
            LandlordProductOverviewCollector overviewCalculationService,
            AdminLandlordPlanHandler planHandler) {
        this.priceCalculationService = priceCalculationService;
        this.overviewCollector = overviewCalculationService;
        this.planHandler = planHandler;
    }

    @GetMapping(value = "/{product}/customerPrice/{customer}")
    public ResponseEntity customerPrice(@ModelAttribute("product") @Valid LandlordProduct product,
                                        @ModelAttribute("customer") @Valid LandlordCustomer customer,
                                        BindingResult result) {

        if (result.hasErrors()) {
            return new ResponseEntity<>(new CollectionModel<>(result.getAllErrors()), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(priceCalculationService.calcPrice(product, customer), OK);
    }

    @PostMapping(value = "/{product}/calculatePrice")
    public ResponseEntity calculatePrice(@PathVariable("product") @Valid LandlordProduct product,
                                         @RequestBody @Valid CalculatePriceBean calculatePrice,
                                         BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(new CollectionModel<>(result.getAllErrors()), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(priceCalculationService.calcPrice(product, calculatePrice.getLocation(),
                calculatePrice.getPriceMultiplier()), OK);
    }

    @PostMapping(value = "/updateNextProduct")
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "customer", schema = @Schema(type = "Long"), required = true),
            @Parameter(in = ParameterIn.QUERY, name = "nextProduct", schema = @Schema(type = "Long"), required = true)})
    public ResponseEntity<?> updateNextProduct(
            @RequestParam("nextProduct") LandlordProduct nextProduct,
            @RequestParam("customer") LandlordCustomer customer
    ) {
        LandlordCustomerProduct activeProduct = customer.getActiveProduct();
        if (activeProduct == null) {
            return ResponseEntity.badRequest().body(CUSTOMER_HAS_NO_ACTIVE_PRODUCT_MSG);
        }
        LandlordCustomerProduct savedProduct = planHandler.changeNextProduct(activeProduct, nextProduct);

        return ResponseEntity.ok(savedProduct);
    }

    @GetMapping("/overview")
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "customer", schema = @Schema(type = "Long"), required = true)})
    public ResponseEntity<LandlordProductOverviewBean> getProductOverview(@RequestParam(value = "customer", required = false) LandlordCustomer customer) {
        return ResponseEntity.ok(overviewCollector.getProductOverview(customer));
    }
}
