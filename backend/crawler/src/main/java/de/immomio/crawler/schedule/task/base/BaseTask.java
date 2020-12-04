package de.immomio.crawler.schedule.task.base;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.LandlordUser;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 */
public abstract class BaseTask implements SchedulerTask {

    protected LandlordUser getPropertyUser(Property property) {
        LandlordUser user = property.getUser();

        if (user == null) {
            user = property.getCustomer().getResposibleUser();
        }

        return user;
    }

}
