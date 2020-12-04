package de.immomio.model.repository.core.propertysearcher.product.customer;

import de.immomio.data.propertysearcher.entity.customer.PropertySearcherCustomer;
import de.immomio.data.propertysearcher.entity.customer.product.PropertySearcherCustomerProduct;
import de.immomio.model.abstractrepository.product.BaseAbstractCustomerProductRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "ps-customerProducts")
public interface BasePropertySearcherCustomerProductRepository
        extends BaseAbstractCustomerProductRepository<PropertySearcherCustomerProduct,
        PropertySearcherCustomer> {

}
