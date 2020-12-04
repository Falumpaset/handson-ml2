package de.immomio.landlord.service.product;

import de.immomio.billing.landlord.AbstractLandlordBasketHandler;
import de.immomio.data.base.type.addon.AddonType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.product.basket.LandlordProductBasket;
import de.immomio.data.landlord.entity.product.basket.LandlordProductBasketProductAddon;
import de.immomio.landlord.service.contract.LandlordDigitalContractUserService;
import de.immomio.landlord.service.selfdisclosure.LandlordSelfDisclosureService;
import de.immomio.model.repository.landlord.customer.LandlordCustomerRepository;
import de.immomio.model.repository.landlord.product.LandlordProductRepository;
import de.immomio.model.repository.landlord.product.basket.LandlordProductBasketRepository;
import de.immomio.model.repository.landlord.product.productaddon.LandlordProductAddonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Component
public class LandlordBasketHandler extends AbstractLandlordBasketHandler<LandlordProductBasketRepository,
        LandlordCheckoutService,
        LandlordCacheEvictService,
        LandlordProductAddonRepository,
        LandlordProductRepository,
        LandlordCustomerRepository> {

    private final LandlordSelfDisclosureService landlordSelfDisclosureService;

    private final LandlordDigitalContractUserService digitalContractUserService;

    @Autowired
    public LandlordBasketHandler(
            LandlordCheckoutService checkoutService,
            LandlordCacheEvictService cacheEvictService,
            LandlordSelfDisclosureService landlordSelfDisclosureService,
            LandlordProductBasketRepository productBasketRepository,
            LandlordDigitalContractUserService digitalContractUserService,
            LandlordCustomerRepository customerRepository,
            LandlordProductRepository productRepository,
            LandlordProductAddonRepository productAddonRepository
    ) {
        super(productBasketRepository, checkoutService, cacheEvictService, customerRepository, productAddonRepository, productRepository);
        this.landlordSelfDisclosureService = landlordSelfDisclosureService;
        this.digitalContractUserService = digitalContractUserService;
    }

    public void initializeAddons(LandlordProductBasket basket) {
        LandlordCustomer customer = basket.getCustomer();

        Optional<LandlordProductBasketProductAddon> addedSelfDisclosureModule = basket.getProductAddons()
                .stream()
                .filter(item -> item.getProductAddon().getAddonProduct().getAddonType() == AddonType.SELF_DISCLOSURE)
                .findFirst();

        addedSelfDisclosureModule.ifPresent(productAddon -> landlordSelfDisclosureService.create(customer));

        Optional<LandlordProductBasketProductAddon> addedDigitalContractModule = basket.getProductAddons()
                .stream()
                .filter(item -> item.getProductAddon().getAddonProduct().getAddonType() == AddonType.DIGITAL_CONTRACT)
                .findFirst();

        addedDigitalContractModule.ifPresent(productAddon -> digitalContractUserService.createDigitalContractUserIfNotExists(customer));
    }
}
