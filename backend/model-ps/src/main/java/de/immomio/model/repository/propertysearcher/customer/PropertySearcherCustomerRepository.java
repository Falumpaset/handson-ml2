package de.immomio.model.repository.propertysearcher.customer;

import de.immomio.data.propertysearcher.entity.customer.PropertySearcherCustomer;
import de.immomio.model.abstractrepository.customer.AbstractCustomerRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

public interface PropertySearcherCustomerRepository extends AbstractCustomerRepository<PropertySearcherCustomer> {

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @PreAuthorize("#customer.id == principal?.customer.id")
    void delete(@Param("customer") PropertySearcherCustomer customer);

}
