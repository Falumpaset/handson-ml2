package de.immomio.model.repository.core.landlord.discount;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.discount.LandlordCustomerProductAddonDiscount;
import de.immomio.data.landlord.entity.product.addon.LandlordAddonProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Maik Kingma
 */
@Repository
public interface BaseLandlordCustomerProductAddonDiscountRepository
        extends JpaRepository<LandlordCustomerProductAddonDiscount, Long> {

    List<LandlordCustomerProductAddonDiscount> findAllByCustomer(LandlordCustomer customer);

    Optional<LandlordCustomerProductAddonDiscount> findFirstByCustomerAndProductAddonAddonProduct(
            LandlordCustomer customer,
            LandlordAddonProduct addonProduct);
}
