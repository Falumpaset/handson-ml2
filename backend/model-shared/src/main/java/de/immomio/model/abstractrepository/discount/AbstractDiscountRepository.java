package de.immomio.model.abstractrepository.discount;

import de.immomio.data.base.entity.discount.AbstractDiscount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

public interface AbstractDiscountRepository<D extends AbstractDiscount> extends BaseAbstractDiscountRepository<D> {

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @RestResource(exported = false)
    void delete(@Param("discount") D discount);

    @Override
    @RestResource(exported = false)
    <S extends D> S save(@Param("discount") S discount);

    @Override
    @RestResource(exported = false)
    D getOne(Long id);

    @Override
    @RestResource(exported = false)
    Page<D> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    Optional<D> findById(Long id);

}
