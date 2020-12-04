package de.immomio.model.abstractrepository.price;

import de.immomio.data.base.entity.price.AbstractPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@RepositoryRestResource(path = "prices")
public interface AbstractPriceRepository<P extends AbstractPrice> extends JpaRepository<P, Long> {

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @RestResource(exported = false)
    void delete(@Param("price") P price);

    @Override
    @RestResource(exported = false) <T extends P> T save(@Param("price") T price);

}
