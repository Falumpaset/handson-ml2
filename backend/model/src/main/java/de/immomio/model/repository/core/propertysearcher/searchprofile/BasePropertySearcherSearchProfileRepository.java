package de.immomio.model.repository.core.propertysearcher.searchprofile;

import de.immomio.data.propertysearcher.entity.user.PropertySearcherSearchProfile;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * @author Niklas Lindemann
 */

@RepositoryRestResource(path = "searchProfiles")
public interface BasePropertySearcherSearchProfileRepository
        extends JpaRepository<PropertySearcherSearchProfile, Long> {

    @RestResource(exported = false)
    @Query(value = "SELECT DISTINCT ON " +
                        "(sp.id) sp.*, " +
                        "ST_Distance(sp.location, ST_SetSRID(ST_MakePoint" +
                        "(:long, :lat), 4326)) " +
                    "FROM propertysearcher.searchprofile sp INNER JOIN propertysearcher.user_profile u on u.id = sp.user_profile_id and u.search_until > now() " +
                    "WHERE sp.type = 'RADIUS' AND ST_DWithin(sp.location, cast(ST_SetSRID" +
                        "(ST_MakePoint(:long, :lat), 4326) as geography), cast(sp.data ->> 'radius' as double precision)) AND sp.deleted = false " +
                    "ORDER BY sp.id, ST_Distance(sp.location, " +
                        "ST_SetSRID(ST_MakePoint(:long, :lat), 4326));", nativeQuery = true)
    List<PropertySearcherSearchProfile> customFindNearestToPoint(@Param("lat") Double lat, @Param("long") Double lng);

    @RestResource(exported = false)
    @Query(value = "SELECT ST_DWithin(" +
                        "CAST(ST_SetSRID(ST_MakePoint(:long1, :lat1), 4326) as geography), " +
                        "CAST(ST_SetSRID(ST_MakePoint(:long2, :lat2), 4326) as geography)," +
                        ":radius)",
            nativeQuery = true)
    boolean isWithin(@Param("lat1") double lat1,
                     @Param("long1") double long1,
                     @Param("lat2") double lat2,
                     @Param("long2") double long2,
                     @Param("radius") Long radius);

    @RestResource(exported = false)
    List<PropertySearcherSearchProfile> findAllByUserProfile(PropertySearcherUserProfile userProfile);

    @RestResource(exported = false)
    @Query(value = "SELECT COUNT(p) FROM PropertySearcherSearchProfile p where p.userProfile.searchUntil > CURRENT_TIMESTAMP AND p.deleted = false ")
    Long countExcludingInactiveUsers();

    @RestResource(exported = false)
    @Query(value = "SELECT COUNT(p) FROM PropertySearcherSearchProfile p where p.proposals is not empty AND p.userProfile.searchUntil > CURRENT_TIMESTAMP AND p.deleted = false ")
    Long countProfilesWithMatches();

    @RestResource(exported = false)
    @Query(value = "SELECT p FROM PropertySearcherSearchProfile p where p.userProfile.searchUntil > CURRENT_TIMESTAMP AND p.deleted = false ")
    List<PropertySearcherSearchProfile> findAllActive();

    @RestResource(exported = false)
    List<PropertySearcherSearchProfile> findAllByUserProfileIn(List<PropertySearcherUserProfile> userProfiles);

    @Query("select distinct p from PropertySearcherSearchProfile p " +
            "inner join p.districts d inner join d.zipCodes z where z.zipCode= :zipCode and p.type = 'DISTRICT' AND p.deleted = false and p.userProfile.searchUntil > CURRENT_TIMESTAMP ")
    List<PropertySearcherSearchProfile> findAllByZip(@Param("zipCode") String zipCode);
}
