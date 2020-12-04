package de.immomio.service.landlord;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.model.repository.core.landlord.customer.BaseLandlordCustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author Maik Kingma
 * @author Freddy Sawma
 */

@Slf4j
@Service
public class CrawlerLandlordCustomerService {

    private final BaseLandlordCustomerRepository landlordCustomerRepository;

    public CrawlerLandlordCustomerService(BaseLandlordCustomerRepository landlordCustomerRepository) {
        this.landlordCustomerRepository = landlordCustomerRepository;
    }

    public List<LandlordCustomer> findAll() {
        return landlordCustomerRepository.findAll();
    }
}
