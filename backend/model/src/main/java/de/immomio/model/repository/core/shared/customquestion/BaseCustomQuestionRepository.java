package de.immomio.model.repository.core.shared.customquestion;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.customquestion.CustomQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * @author Maik Kingma
 */

@RestResource(exported = false)
@RepositoryRestResource(path = "customQuestions")
public interface BaseCustomQuestionRepository extends JpaRepository<CustomQuestion, Long> {

    @Query("SELECT cq FROM CustomQuestion cq where cq.customer = :customer and cq.type = 'GLOBAL'")
    List<CustomQuestion> findGlobalForCustomer(@Param("customer") LandlordCustomer customer);
}
