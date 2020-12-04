package de.immomio.model.abstractrepository.product;

import de.immomio.data.base.entity.product.AbstractProduct;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public interface AbstractProductRepository<P extends AbstractProduct<?, ?, ?, ?, ?>> extends
        BaseAbstractProductRepository<P> {

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @RestResource(exported = false)
    void delete(@Param("product") P product);

    @Override
    @RestResource(exported = false) <S extends P> S save(@Param("product") S product);

}
