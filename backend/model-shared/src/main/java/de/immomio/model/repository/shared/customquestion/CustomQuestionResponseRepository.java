package de.immomio.model.repository.shared.customquestion;

import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.common.customQuestion.CustomQuestionResponse;
import de.immomio.model.repository.core.shared.customquestion.BaseCustomQuestionResponseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Niklas Lindemann, Maik Kingma
 */

@RestResource(exported = false)
@RepositoryRestResource(path = "customQuestionResponses")
public interface CustomQuestionResponseRepository extends BaseCustomQuestionResponseRepository {

    @Override
    @RestResource(exported = false)
    Page<CustomQuestionResponse> findAll(Pageable pageable);

    @Override
    @PostAuthorize("returnObject?.get().userProfile?.user?.id == principal?.id ||" +
            " returnObject?.get().customQuestion?.customer?.id == principal?.customer.id")
    Optional<CustomQuestionResponse> findById(Long id);

    @Override
    @RestResource(exported = false)
    <S extends CustomQuestionResponse> S save(S s);

    @Override
    @RestResource
    @PreAuthorize("#response.userProfile.user.id = principal.id")
    void delete(@P("response") CustomQuestionResponse entity);

    @RestResource(exported = false)
    @Query(value = "select cQR" +
            " from Property pro " +
            " inner join pro.prioset pri " +
            " inner join pri.customQuestionAssociations pQA " +
            " inner join CustomQuestionResponse cQR on cQR.customQuestion = pQA.customQuestion" +
            " and cQR.userProfile.user.id = :userId and pro.id = :propertyId ")
    List<CustomQuestionResponse> findByPropertyIdAndUserId(
            @Param("propertyId") Long propertyId, @Param("userId") Long userId);

    void deleteAllByUserProfileIn(List<PropertySearcherUserProfile> userProfiles);
}
