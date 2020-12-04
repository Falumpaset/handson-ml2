package de.immomio.model.repository.core.shared.location;

import de.immomio.data.shared.entity.common.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Niklas Lindemann
 */
@Repository
public interface BaseCityRepository extends JpaRepository<City, Long> {

    @Override
    @Query("SELECT DISTINCT o from City o join fetch o.zipCodes")
    List<City> findAll();

    @Query("SELECT c from City c inner join c.zipCodes z where z.zipCode = :zip")
    Optional<City> findByZipCode(@Param("zip") String zip);
}
