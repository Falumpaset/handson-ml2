package de.immomio.model.abstractrepository.product;

import de.immomio.data.base.entity.customer.AbstractCustomer;
import de.immomio.data.base.entity.customer.AbstractCustomerProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Date;
import java.util.List;
import java.util.Set;

@RepositoryRestResource(path = "customerProducts")
public interface BaseAbstractCustomerProductRepository<CP extends AbstractCustomerProduct, C extends AbstractCustomer>
        extends JpaRepository<CP, Long> {

    Set<CP> findByRenewAndDueDateIsBefore(Boolean renew, Date dueDate);

    List<CP> findByCustomerAndDueDateIsAfter(C customer, Date dueDate);
}
