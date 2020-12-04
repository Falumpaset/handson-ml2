package de.immomio.model.repository.core.landlord.product.basket;

import de.immomio.data.base.type.product.basket.ProductBasketStatus;
import de.immomio.data.landlord.entity.product.basket.LandlordProductBasket;
import de.immomio.model.abstractrepository.product.BaseAbstractProductBasketRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BaseLandlordProductBasketRepository extends
        BaseAbstractProductBasketRepository<LandlordProductBasket> {

    @Query("SELECT o FROM #{#entityName} o WHERE status IN :productBasketStatuses")
    List<LandlordProductBasket> findByStatuses(
            @Param("productBasketStatuses") List<ProductBasketStatus> productBasketStatuses, Pageable pageable);
}
