package de.immomio.model.abstractrepository.product.limitation;

import de.immomio.data.base.entity.product.limitation.AbstractAddonProductLimitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.MappedSuperclass;

/**
 * @author Bastian Bliemeister
 */
@MappedSuperclass
@RepositoryRestResource(path = "addonProductLimitations")
public interface AbstractAddonProductLimitationRepository<APL extends AbstractAddonProductLimitation>
        extends JpaRepository<APL, Long> {

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @RestResource(exported = false)
    void delete(@Param("addonProductLimitation") APL addonProductLimitation);

}
