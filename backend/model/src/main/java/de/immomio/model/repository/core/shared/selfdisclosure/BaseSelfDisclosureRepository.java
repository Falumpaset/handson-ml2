package de.immomio.model.repository.core.shared.selfdisclosure;

import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface BaseSelfDisclosureRepository extends JpaRepository<SelfDisclosure, Long> {

    @Query("SELECT o FROM #{#entityName} o WHERE o.customer.id IS NULL")
    SelfDisclosure findDefaultForCreation();

}
