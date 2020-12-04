package de.immomio.model.repository.core.shared.location;

import de.immomio.data.shared.entity.common.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Niklas Lindemann
 */
@Repository
public interface BaseStateRepository extends JpaRepository<State, Long> {

    @Query("SELECT s from State s inner join s.cities c inner join c.zipCodes z where z.zipCode = :zip")
    Optional<State> findByZipCode(@Param("zip") String zip);
}
