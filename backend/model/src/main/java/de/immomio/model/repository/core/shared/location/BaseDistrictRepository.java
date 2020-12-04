package de.immomio.model.repository.core.shared.location;

import de.immomio.data.shared.entity.common.District;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Niklas Lindemann
 */
@Repository
public interface BaseDistrictRepository extends JpaRepository<District, Long> {

    Set<District> findAllByZipCodesZipCodeContains(String zipCode);

    Set<District> findAllByIdIn(List<Long> ids);

    @Query(value = "select * from constants.district d " +
            "inner join propertysearcher.searchprofile_district sd " +
            "on sd.district_id = d.id " +
            "where sd.searchprofile_id = :searchProfile", nativeQuery = true)
    List<District> findAllBySearchProfile(@Param("searchProfile") Long searchProfileId);

    List<District> findAllByCityId(Long id);

}
