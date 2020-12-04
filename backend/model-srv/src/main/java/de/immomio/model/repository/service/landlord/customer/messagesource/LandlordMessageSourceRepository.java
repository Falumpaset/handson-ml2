package de.immomio.model.repository.service.landlord.customer.messagesource;

import de.immomio.model.repository.core.landlord.customer.messagesource.BaseLandlordMessageSourceRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "ll-messageSources")
public interface LandlordMessageSourceRepository extends BaseLandlordMessageSourceRepository {

}
