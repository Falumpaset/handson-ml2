package de.immomio.landlord.listener.product;

import de.immomio.data.base.type.addon.AddonType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.product.addonproduct.LandlordCustomerAddonProduct;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.landlord.service.product.LandlordCheckoutNotificationService;
import de.immomio.landlord.service.security.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler(LandlordCustomerAddonProduct.class)
public class LandlordCustomerAddonProductListener {

    private final LandlordCheckoutNotificationService landlordCheckoutNotificationService;

    private final UserSecurityService userSecurityService;

    @Autowired
    public LandlordCustomerAddonProductListener(LandlordCheckoutNotificationService landlordCheckoutNotificationService,
                                                UserSecurityService userSecurityService) {
        this.landlordCheckoutNotificationService = landlordCheckoutNotificationService;
        this.userSecurityService = userSecurityService;
    }

    @HandleAfterSave
    public void notifyAfterCustomerAddonProductUpdated(LandlordCustomerAddonProduct addonProduct) {
        if (addonProduct.getAddonProduct().getAddonType() == AddonType.HPMODULE && !addonProduct.isRenew()) {
            LandlordUser user = userSecurityService.getPrincipalUser();
            LandlordCustomer customer = addonProduct.getCustomerProduct().getCustomer();
            landlordCheckoutNotificationService.homepageModuleRemoved(customer, user);
        }
    }
}
