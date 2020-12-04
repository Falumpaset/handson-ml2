package de.immomio.controller.product;

import de.immomio.controller.BaseController;
import de.immomio.data.landlord.bean.product.LandlordProductOverviewBean;
import de.immomio.data.landlord.entity.product.LandlordCustomerProduct;
import de.immomio.data.landlord.entity.product.LandlordProduct;
import de.immomio.landlord.service.product.LandlordPlanHandler;
import de.immomio.landlord.service.product.LandlordProductService;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.service.landlord.product.LandlordProductOverviewCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/products")
public class ProductController extends BaseController {

    private static final String CUSTOMER_HAS_NO_ACTIVE_PRODUCT = "CUSTOMER_HAS_NO_ACTIVE_PRODUCT_L";

    private final UserSecurityService userSecurityService;

    private final LandlordProductOverviewCollector overviewCollector;
    private final LandlordPlanHandler planHandler;
    private LandlordProductService landlordProductService;

    @Autowired
    public ProductController(
            UserSecurityService userSecurityService,
            LandlordProductOverviewCollector overviewCollector,
            LandlordProductService landlordProductService,
            LandlordPlanHandler planHandler) {

        this.userSecurityService = userSecurityService;
        this.overviewCollector = overviewCollector;
        this.landlordProductService = landlordProductService;
        this.planHandler = planHandler;
    }

    @GetMapping("/overview")
    public ResponseEntity<LandlordProductOverviewBean> getProductOverview() {
        return ResponseEntity.ok(overviewCollector.getProductOverview(userSecurityService.getPrincipalUser().getCustomer()));
    }

    @PostMapping(value = "/updateNextProduct")
    public ResponseEntity<?> updateNextProduct(@RequestParam("nextProduct") LandlordProduct nextProduct) {
        LandlordCustomerProduct activeProduct = userSecurityService.getPrincipalUser().getCustomer().getActiveProduct();
        if (activeProduct == null) {
            return ResponseEntity.badRequest().body(CUSTOMER_HAS_NO_ACTIVE_PRODUCT);
        }
        LandlordCustomerProduct savedProduct = planHandler.changeNextProduct(activeProduct, nextProduct);

        return ResponseEntity.ok(savedProduct);
    }

}
