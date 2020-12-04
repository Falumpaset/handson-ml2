package de.immomio.model.repository.landlord.discount;

import de.immomio.data.landlord.entity.discount.LandlordCustomerProductAddonDiscount;
import de.immomio.model.repository.core.landlord.discount.BaseLandlordCustomerProductAddonDiscountRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

/**
 * @author Maik Kingma
 */

@RepositoryRestResource(path = "customerProductAddonDiscounts")
public interface LandlordCustomerProductAddonDiscountRepository
        extends BaseLandlordCustomerProductAddonDiscountRepository {

    @Override
    @RestResource(exported = false)
    <S extends LandlordCustomerProductAddonDiscount> List<S> saveAll(Iterable<S> entities);

    @Override
    @RestResource(exported = false)
    Page<LandlordCustomerProductAddonDiscount> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    <S extends LandlordCustomerProductAddonDiscount> S save(S entity);

    @Override
    @RestResource(exported = false)
    Optional<LandlordCustomerProductAddonDiscount> findById(Long id);

    @Override
    @RestResource(exported = false)
    void deleteById(Long id);

    @Override
    @RestResource(exported = false)
    void delete(LandlordCustomerProductAddonDiscount entity);
}
