package de.immomio.controller.customer;

import de.immomio.beans.RegisterResultBean;
import de.immomio.beans.landlord.LandlordRegisterBean;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.landlord.bean.product.RegisterAboBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.service.landlord.LandlordAboService;
import de.immomio.service.landlord.LandlordOnboardingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Date;

@RepositoryRestController
@RequestMapping(value = "/customers")
public class CustomerController {

    private final LandlordOnboardingService landlordOnboardingservice;

    private final LandlordAboService aboService;

    public static final String CUSTOMER_ALREADY_HAS_PRODUCT_L = "CUSTOMER_ALREADY_HAS_PRODUCT_L";

    @Autowired
    public CustomerController(
            LandlordOnboardingService landlordOnboardingservice,
            LandlordAboService aboService) {
        this.landlordOnboardingservice = landlordOnboardingservice;
        this.aboService = aboService;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Object> registerCustomer(PersistentEntityResourceAssembler assembler,
                                                   @RequestBody @Valid LandlordRegisterBean landlordRegisterBean,
                                                   BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(new CollectionModel<>(result.getAllErrors()), HttpStatus.BAD_REQUEST);
        }
        RegisterResultBean<LandlordUser> registerResultBean = landlordOnboardingservice.register(landlordRegisterBean);
        if (registerResultBean.hasError()) {
            result.addError(registerResultBean.getError());
            return new ResponseEntity<>(new CollectionModel<>(result.getAllErrors()), HttpStatus.BAD_REQUEST);
        }
        LandlordCustomer customer = registerResultBean.getRegisteredUser().getCustomer();
        aboService.setAbo(customer, landlordRegisterBean.getCustomer().getAbo());

        return new ResponseEntity<>(assembler.toModel(registerResultBean.getRegisteredUser()), HttpStatus.OK);
    }

    @PostMapping("/{customer}/setAbo")
    public ResponseEntity setAbo(@PathVariable("customer") LandlordCustomer landlordCustomer, @RequestBody RegisterAboBean aboBean) {
        if (landlordCustomer.getActiveProduct() != null) {
            throw new ApiValidationException(CUSTOMER_ALREADY_HAS_PRODUCT_L);
        }
        aboService.setAbo(landlordCustomer, aboBean);
        return ResponseEntity.ok().build();
    }


    @PostMapping(value = "/deactivate/{customer}")
    public ResponseEntity deactivate(
            @PathVariable("customer") LandlordCustomer customer,
            @RequestParam(value = "dueDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy")  Date dueDate
    ) {
        landlordOnboardingservice.deactivateActiveProduct(customer, dueDate);

        return ResponseEntity.ok().build();
    }
}
