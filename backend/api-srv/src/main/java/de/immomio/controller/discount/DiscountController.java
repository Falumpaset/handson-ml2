package de.immomio.controller.discount;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.discount.LandlordDiscount;
import de.immomio.data.shared.bean.discount.LandlordAddonDiscountBean;
import de.immomio.data.shared.bean.discount.LandlordQuotaDiscountBean;
import de.immomio.service.landlord.LandlordDiscountService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@RepositoryRestController
@RequestMapping("/discount")
public class DiscountController {

    private final LandlordDiscountService landlordDiscountService;

    @Autowired
    public DiscountController(LandlordDiscountService landlordDiscountService) {
        this.landlordDiscountService = landlordDiscountService;
    }

    @PostMapping("/create/{customerId}")
    public ResponseEntity createDiscount(
            @PathVariable Long customerId,
            @RequestBody LandlordDiscount landlordDiscount
    ) {
        try {
            LandlordDiscount discountCreated = landlordDiscountService.createDiscount(customerId, landlordDiscount);
            return new ResponseEntity<>(discountCreated, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/update")
    public ResponseEntity updateDiscount(@RequestBody LandlordDiscount landlordDiscount) {
        try {
            LandlordDiscount discountUpdated = landlordDiscountService.updateDiscount(landlordDiscount);
            return new ResponseEntity<>(discountUpdated, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity deleteDiscount(@RequestBody LandlordDiscount landlordDiscount) {
        try {
            landlordDiscountService.deleteDiscount(landlordDiscount);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/create/landlordAddonDiscounts")
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "customer", schema = @Schema(type = "Long"), required = true)})
    public ResponseEntity createLandlordAddonDiscount(
            @RequestParam("customer") LandlordCustomer customer,
            @RequestBody LandlordAddonDiscountBean[] addonDiscountsBeans
    ) {
        landlordDiscountService.createLandlordAddonDiscounts(customer, Arrays.asList(addonDiscountsBeans));

        return ResponseEntity.ok().build();
    }

    @PostMapping("/create/landlordQuotaDiscounts")
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "customer", schema = @Schema(type = "Long"), required = true)})
    public ResponseEntity createLandlordQuotaDiscount(
            @RequestParam("customer") LandlordCustomer customer,
            @RequestBody LandlordQuotaDiscountBean[] quotaDiscountBeans
    ) {
        landlordDiscountService.createLandlordQuotaDiscounts(customer, Arrays.asList(quotaDiscountBeans));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/landlordAddonDiscounts")
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "customer", schema = @Schema(type = "Long"), required = true)})
    public ResponseEntity getLandlordAddonDiscounts(@RequestParam("customer") LandlordCustomer customer) {
        return ResponseEntity.ok(landlordDiscountService.getDiscountsByCustomer(customer));
    }

    @GetMapping("/landlordQuotaDiscounts")
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "customer", schema = @Schema(type = "Long"), required = true)})
    public ResponseEntity getLandlordQuotaDiscounts(@RequestParam("customer") LandlordCustomer customer) {
        return ResponseEntity.ok(landlordDiscountService.getQuotaDiscountsByCustomer(customer));
    }
}

