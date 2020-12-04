package de.immomio.service.landlord;

import de.immomio.data.base.entity.customer.user.AbstractUser;
import de.immomio.data.base.type.addon.AddonType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;

/**
 * @author Niklas Lindemann
 */
public abstract class AbstractLandlordUserService {

    public boolean freeAgentSlotCheck(LandlordCustomer customer) {
        long agentSlots = getAgentSlots(customer);
        return agentSlots > customer.getUsers().stream().filter(AbstractUser::isEnabled).count();
    }

    public boolean freeAgentSlotCheck(LandlordCustomer customer, Integer countToEnable) {
        long agentSlots = getAgentSlots(customer);
        long freeSlots = agentSlots - customer.getUsers().stream().filter(AbstractUser::isEnabled).count();
        return freeSlots >= countToEnable;
    }

    private long getAgentSlots(LandlordCustomer customer) {
        return customer.getActiveProduct().getAddons()
                .stream()
                .filter(addonProduct ->
                        addonProduct.getAddonProduct().getAddonType() == AddonType.AGENT
                ).count();
    }
}
