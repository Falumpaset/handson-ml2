package de.immomio.model.repository.core.propertysearcher.price;

import de.immomio.data.propertysearcher.entity.price.PropertySearcherPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface BasePropertySearcherPriceRepository extends JpaRepository<PropertySearcherPrice, Long> {

    @Override
    void deleteById(@Param("id") Long id);

    @Override
    void delete(@Param("price") PropertySearcherPrice price);

    @Override <S extends PropertySearcherPrice> S save(@Param("price") S price);

}
