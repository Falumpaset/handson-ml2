package de.immomio.controller.product.quota;

import de.immomio.beans.landlord.product.quota.QuotaPackageBean;
import de.immomio.landlord.service.product.quota.LandlordQuotaPackageService;
import de.immomio.data.base.type.product.quota.QuotaProductType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Niklas Lindemann
 */
@RestController
@RequestMapping("/quotaPackages")
public class QuotaPackageController {

    private LandlordQuotaPackageService quotaPackageService;

    @Autowired
    public QuotaPackageController(LandlordQuotaPackageService quotaPackageService) {
        this.quotaPackageService = quotaPackageService;
    }

    @GetMapping("/search/findByType")
    public ResponseEntity getQuotaPackages(@RequestParam("type") QuotaProductType type) {
        List<QuotaPackageBean> quotaPackageBeans = quotaPackageService.getQuotaPackagesByType(type);
        return ResponseEntity.ok(quotaPackageBeans);
    }
}
