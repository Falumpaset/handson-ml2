package de.immomio.model.repository.propertysearcher.product.customer;

import de.immomio.data.propertysearcher.entity.customer.PropertySearcherCustomer;
import de.immomio.data.propertysearcher.entity.customer.product.PropertySearcherCustomerProduct;
import de.immomio.model.abstractrepository.product.customer.AbstractCustomerProductRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "customerProducts")
public interface PropertySearcherCustomerProductRepository
        extends AbstractCustomerProductRepository<PropertySearcherCustomerProduct, PropertySearcherCustomer> {

}
