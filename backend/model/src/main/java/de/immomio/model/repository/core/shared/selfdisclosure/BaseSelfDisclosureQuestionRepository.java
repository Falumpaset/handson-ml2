package de.immomio.model.repository.core.shared.selfdisclosure;

import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosureQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface BaseSelfDisclosureQuestionRepository extends JpaRepository<SelfDisclosureQuestion, Long> {

    @Override
    @Query("SELECT q FROM SelfDisclosureQuestion q order by orderNumber")
    List<SelfDisclosureQuestion> findAll();
}
