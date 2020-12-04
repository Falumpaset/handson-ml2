package de.immomio.model.abstractrepository.product;

import de.immomio.data.base.entity.product.AbstractProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author Maik Baumbach
 */

public interface BaseAbstractProductRepository<P extends AbstractProduct<?, ?, ?, ?, ?>>
        extends JpaRepository<P, Long> {

    @Override
    void deleteById(@Param("id") Long id);

    @Override
    void delete(@Param("product") P product);

    @Override <S extends P> S save(@Param("product") S product);

}
