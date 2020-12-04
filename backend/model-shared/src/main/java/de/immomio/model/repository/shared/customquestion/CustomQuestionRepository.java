package de.immomio.model.repository.shared.customquestion;

import de.immomio.data.base.type.customQuestion.CustomQuestionType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.customquestion.CustomQuestion;
import de.immomio.model.repository.core.shared.customquestion.BaseCustomQuestionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

/**
 * @author Maik Kingma
 */

@RestResource(exported = false)
@RepositoryRestResource(path = "customQuestions")
public interface CustomQuestionRepository extends BaseCustomQuestionRepository {

    @Override
    @RestResource(exported = false)
    Optional<CustomQuestion> findById(Long aLong);

    @Override
    @RestResource(exported = false)
    @Query("SELECT o FROM #{#entityName} o WHERE o.customer.id = ?#{principal.customer.id}")
    Page<CustomQuestion> findAll(Pageable pageable);

    @RestResource(exported = false)
    Page<CustomQuestion> findByCustomerAndType(LandlordCustomer customer, CustomQuestionType type, Pageable pageable);

    /*@PreAuthorize("#application.userProfile.user.id == principal?.id")
    @Query(value = "select cq " +
            "from CustomQuestion cq " +
            "inner join cq.priosets p " +
            "inner join p.property.propertyApplications a where a = :application and cq.type = 'PROPERTY'")
    List<CustomQuestion> findByApplication(@Param("application") PropertyApplication application);

    @PreAuthorize("#application.userProfile.user.id == principal?.id")
    @Query(value = "select cq " +
            "from PropertyApplication pA " +
            "inner join pA.property pro " +
            "inner join pro.prioset pri " +
            "inner join pri.customQuestionAssociations cQA " +
            "inner join cQA.customQuestion cQ where pA = :application and cQ.type = 'PROPERTY'")
    List<CustomQuestion> findByApplication(@Param("application") PropertyApplication application);*/
}
