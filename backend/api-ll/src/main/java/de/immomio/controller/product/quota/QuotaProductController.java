package de.immomio.controller.product.quota;

import de.immomio.landlord.service.product.quota.LandlordQuotaProductService;
import de.immomio.data.base.type.product.quota.QuotaProductType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Niklas Lindemann
 */

@RestController
@RequestMapping("/quotaProducts")
public class QuotaProductController {

    private final LandlordQuotaProductService quotaProductService;


    public QuotaProductController(LandlordQuotaProductService quotaProductService) {
        this.quotaProductService = quotaProductService;
    }

    @GetMapping("/details")
    public ResponseEntity getQuotaProduct(@RequestParam("type") QuotaProductType quotaProductType) {
        return ResponseEntity.ok(quotaProductService.getQuotaProductDetails(quotaProductType));
    }
}
