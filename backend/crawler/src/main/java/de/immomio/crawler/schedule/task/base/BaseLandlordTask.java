package de.immomio.crawler.schedule.task.base;

import de.immomio.model.repository.core.landlord.customer.BaseLandlordCustomerRepository;
import de.immomio.model.repository.core.landlord.customer.property.BasePropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Bastian Bliemeister
 */
public abstract class BaseLandlordTask extends BaseTask {

    @Autowired
    protected BaseLandlordCustomerRepository customerRepository;

    @Autowired
    protected BasePropertyRepository propertyRepository;

}
