package de.immomio.service.landlord.product;

import de.immomio.billing.landlord.AbstractLandlordBasketHandler;
import de.immomio.data.base.type.addon.AddonType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.product.basket.LandlordProductBasket;
import de.immomio.data.landlord.entity.product.basket.LandlordProductBasketProductAddon;
import de.immomio.model.repository.service.landlord.customer.LandlordCustomerRepository;
import de.immomio.model.repository.service.landlord.product.LandlordProductRepository;
import de.immomio.model.repository.service.landlord.product.basket.LandlordProductBasketRepository;
import de.immomio.model.repository.service.landlord.product.productaddon.LandlordProductAddonRepository;
import de.immomio.service.contract.AdminDigitalContractUserService;
import de.immomio.service.landlord.selfdisclosure.SelfDisclosureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Component
public class AdminBasketHandler extends AbstractLandlordBasketHandler<LandlordProductBasketRepository,
        AdminLandlordCheckoutService,
        AdminLandlordCacheEvictService,
        LandlordProductAddonRepository,
        LandlordProductRepository,
        LandlordCustomerRepository> {

    private final SelfDisclosureService selfDisclosureService;

    private final AdminDigitalContractUserService digitalContractUserService;

    @Autowired
    public AdminBasketHandler(
            AdminLandlordCheckoutService checkoutService,
            AdminLandlordCacheEvictService cacheEvictService,
            SelfDisclosureService selfDisclosureService,
            LandlordProductBasketRepository productBasketRepository,
            AdminDigitalContractUserService digitalContractUserService,
            LandlordProductAddonRepository productAddonRepository,
            LandlordProductRepository productRepository,
            LandlordCustomerRepository customerRepository
    ) {
        super(productBasketRepository, checkoutService, cacheEvictService, customerRepository, productAddonRepository, productRepository);
        this.selfDisclosureService = selfDisclosureService;
        this.digitalContractUserService = digitalContractUserService;
    }

    @Override
    public void initializeAddons(LandlordProductBasket basket) {
        LandlordCustomer customer = basket.getCustomer();

        Optional<LandlordProductBasketProductAddon> addedSelfDisclosureModule = basket.getProductAddons()
                .stream()
                .filter(item -> item.getProductAddon().getAddonProduct().getAddonType() == AddonType.SELF_DISCLOSURE)
                .findFirst();

        addedSelfDisclosureModule.ifPresent(productAddon -> selfDisclosureService.create(customer));

        Optional<LandlordProductBasketProductAddon> addedDigitalContractModule = basket.getProductAddons()
                .stream()
                .filter(item -> item.getProductAddon().getAddonProduct().getAddonType() == AddonType.DIGITAL_CONTRACT)
                .findFirst();

        addedDigitalContractModule.ifPresent(productAddon -> digitalContractUserService.createDigitalContractUserIfNotExists(customer));
    }
}
