package de.immomio.service.landlord;

import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.base.type.addon.AddonType;
import de.immomio.data.landlord.bean.product.RegisterAboBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.product.LandlordCustomerProduct;
import de.immomio.data.landlord.entity.product.LandlordProduct;
import de.immomio.data.landlord.entity.product.addon.LandlordAddonProduct;
import de.immomio.data.landlord.entity.product.addonproduct.LandlordCustomerAddonProduct;
import de.immomio.model.repository.service.landlord.product.LandlordProductRepository;
import de.immomio.model.repository.service.landlord.product.addon.AddonProductRepository;
import de.immomio.model.repository.service.landlord.product.customer.LandlordCustomerProductRepository;
import de.immomio.model.repository.service.landlord.product.customer.addon.LandlordCustomerAddonProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Niklas Lindemann
 */
@Service
public class LandlordAboService {

    private LandlordProductRepository productRepository;

    private LandlordCustomerProductRepository customerProductRepository;

    private AddonProductRepository addonProductRepository;

    private LandlordCustomerAddonProductRepository customerAddonProductRepository;

    private final LandlordInitializeAddonService initializeAddonService;


    @Autowired
    public LandlordAboService(LandlordProductRepository productRepository,
            LandlordCustomerProductRepository customerProductRepository,
            AddonProductRepository addonProductRepository,
            LandlordCustomerAddonProductRepository customerAddonProductRepository,
            LandlordInitializeAddonService initializeAddonService) {
        this.productRepository = productRepository;
        this.customerProductRepository = customerProductRepository;
        this.addonProductRepository = addonProductRepository;
        this.customerAddonProductRepository = customerAddonProductRepository;
        this.initializeAddonService = initializeAddonService;
    }

    public void setAbo(LandlordCustomer customer, RegisterAboBean aboBean) {
        LandlordCustomerProduct customerProduct = new LandlordCustomerProduct();
        Date dueDate = Date.from(ZonedDateTime.now().plusDays(aboBean.getTrialDays()).toInstant());
        customerProduct.setDueDate(dueDate);
        customerProduct.setRenew(true);
        customerProduct.setTrial(true);
        customerProduct.setCustomer(customer);
        LandlordProduct landlordProduct = productRepository.findById(aboBean.getProduct()).orElseThrow(ApiValidationException::new);
        customerProduct.setProduct(landlordProduct);
        LandlordCustomerProduct savedCustomerProduct = customerProductRepository.save(customerProduct);

        List<LandlordCustomerAddonProduct> addonsToSave = aboBean.getAddons().stream()
                .map(landlordProductAddonQuantity -> {
                    LandlordAddonProduct addonProduct = addonProductRepository.findById(landlordProductAddonQuantity.getProductAddonId()).orElseThrow(ApiValidationException::new);
                    List<LandlordCustomerAddonProduct> addonProducts = new ArrayList<>();
                    IntStream.range(0, landlordProductAddonQuantity.getQuantity()).forEach(value -> {
                        LandlordCustomerAddonProduct customerAddonProduct = new LandlordCustomerAddonProduct();
                        customerAddonProduct.setRenew(true);
                        customerAddonProduct.setAddonProduct(addonProduct);
                        customerAddonProduct.setCustomerProduct(savedCustomerProduct);
                        addonProducts.add(customerAddonProduct);
                    });
                    return addonProducts;
                }).flatMap(Collection::stream)
                .collect(Collectors.toList());

        customerAddonProductRepository.saveAll(addonsToSave);
        Set<AddonType> createdAddonTypes = addonsToSave.stream().map(addonProduct -> addonProduct.getAddonProduct().getAddonType()).collect(Collectors.toSet());

        initializeAddonService.initializeAddons(customer, createdAddonTypes);

    }
}
