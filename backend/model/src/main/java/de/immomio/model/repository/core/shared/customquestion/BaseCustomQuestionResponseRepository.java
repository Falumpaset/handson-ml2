package de.immomio.model.repository.core.shared.customquestion;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.customquestion.CustomQuestion;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.common.customQuestion.CustomQuestionResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

/**
 * @author Niklas Lindemann
 */

@RepositoryRestResource(path = "customQuestionResponses")
public interface BaseCustomQuestionResponseRepository extends JpaRepository<CustomQuestionResponse, Long> {

    @Query("SELECT o FROM #{#entityName} o " + "WHERE o.userProfile = :userProfile " + "AND o.customQuestion IN (:customQuestions)")
    @RestResource(exported = false)
    List<CustomQuestionResponse> findAllByUserProfileAndCustomQuestion(@Param("userProfile") PropertySearcherUserProfile userProfile,
            @Param("customQuestions") List<CustomQuestion> customQuestions);

    @RestResource(exported = false)
    @Query("SELECT r from CustomQuestionResponse r where r.customQuestion.type = 'GLOBAL' and r.customQuestion.customer = :customer and r.userProfile = :userProfile")
    List<CustomQuestionResponse> findGlobalByUserProfileAndCustomer(@Param("userProfile") PropertySearcherUserProfile userProfile, @Param("customer") LandlordCustomer customer);

    @RestResource(exported = false)
    List<CustomQuestionResponse> findAllByUserProfile(@Param("userProfile") PropertySearcherUserProfile userProfile);

    @Query("SELECT DISTINCT o FROM #{#entityName} o JOIN fetch o.customQuestion cq "
            + "join fetch cq.priosets pri "
            + "WHERE o.userProfile = :userProfile "
            + "AND o.customQuestion IN (:customQuestions) ")
    @RestResource(exported = false)
    List<CustomQuestionResponse> findAllByUserProfileAndCustomQuestionFetchPriosets(@Param("customQuestions") List<CustomQuestion> customQuestions,
            @Param("userProfile") PropertySearcherUserProfile userProfile);

    @RestResource(exported = false)
    Optional<CustomQuestionResponse> findByUserProfileAndCustomQuestion(PropertySearcherUserProfile userProfile, CustomQuestion customQuestion);
}