package de.immomio.model.abstractrepository.discount;

import de.immomio.data.base.entity.discount.AbstractDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface BaseAbstractDiscountRepository<D extends AbstractDiscount> extends JpaRepository<D, Long> {

    @Override
    void deleteById(@Param("id") Long id);

    @Override
    void delete(@Param("discount") D discount);

    @Override <S extends D> S save(@Param("discount") S discount);

}
