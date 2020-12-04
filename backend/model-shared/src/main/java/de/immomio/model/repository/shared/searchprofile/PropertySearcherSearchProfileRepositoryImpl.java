package de.immomio.model.repository.shared.searchprofile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherSearchProfile;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Maik Kingma
 */
@Slf4j
public class PropertySearcherSearchProfileRepositoryImpl implements PropertySearcherSearchProfileRepositoryCustom {

    private final EntityManager entityManager;

    @Autowired
    public PropertySearcherSearchProfileRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(PropertySearcherSearchProfile.class);
    }

    @Override
    @Transactional
    public void customSave(PropertySearcherSearchProfile profile) {

        String dataJson = mapProfileToJson(profile);
        if (dataJson != null) {
            if (profile.isNew()) {
                Query query = entityManager.createNativeQuery("INSERT INTO propertysearcher.searchprofile (" +
                        "id, user_profile_id, property_id, data, created, location, deleted)" +
                        " VALUES (nextval('dictionary_seq'), ?, ?, " +
                        "cast(? AS jsonb), NOW(), " +
                        "ST_SetSRID(ST_MakePoint(?, ?), 4326), false);");
                setQueryParameters(profile, dataJson, query);
                query.executeUpdate();
            } else {
                Query query = entityManager.createNativeQuery("UPDATE propertysearcher.searchprofile " +
                        "SET user_profile_id = ?, property_id = ?, data = ?, " +
                        "updated = NOW(), " +
                        "location = ST_SetSRID(ST_MakePoint(?, ?), 4326)) " +
                        "WHERE id = ?;");
                setQueryParameters(profile, dataJson, query);
                query.setParameter(6, profile.getId());
            }
        }
    }

    private void setQueryParameters(PropertySearcherSearchProfile userProfile, String dataJson, Query query) {
        query.setParameter(1, userProfile.getId());
        query.setParameter(2, userProfile.getProperty().getId());
        query.setParameter(3, dataJson);
        query.setParameter(4, userProfile.getData().getAddress().getCoordinates().getLongitude());
        query.setParameter(5, userProfile.getData().getAddress().getCoordinates().getLatitude());
    }

    @Override
    @Transactional
    public void customManualEntryRadius(PropertySearcherSearchProfile profile) {
        String dataJson = mapProfileToJson(profile);
        if (profile.isNew()) {
            Query query = entityManager.createNativeQuery("INSERT INTO propertysearcher.searchprofile (" +
                    "id, user_profile_id, data, created, updated, location, " +
                    "manuallycreated, deleted, type) " +
                    "VALUES (nextval('dictionary_seq'), ?, " +
                    "cast(? AS jsonb), NOW(), now(), " +
                    "ST_SetSRID(ST_MakePoint(?, ?), 4326), ?, false, 'RADIUS');");
            setQueryParametersForManualEntry(profile, dataJson, query);
            query.executeUpdate();
        } else {
            Query query = entityManager.createNativeQuery("UPDATE propertysearcher.searchprofile " +
                    "SET user_profile_id = ?, data = ?, " +
                    "updated = NOW(), " +
                    "location = ST_SetSRID(ST_MakePoint(?, ?), 4326)), " +
                    "manuallycreated = ? " +
                    "WHERE id = ?;");
            setQueryParametersForManualEntry(profile, dataJson, query);
            query.setParameter(6, profile.getId());
            query.executeUpdate();
        }
    }

    private void setQueryParametersForManualEntry(PropertySearcherSearchProfile profile, String dataJson, Query query) {
        query.setParameter(1, profile.getUserProfile().getId());
        query.setParameter(2, dataJson);
        query.setParameter(3, profile.getData().getAddress().getCoordinates().getLongitude());
        query.setParameter(4, profile.getData().getAddress().getCoordinates().getLatitude());
        query.setParameter(5, profile.getManuallyCreated());
    }

    @Override
    public List<PropertySearcherSearchProfile> customFindByUserProfileAndProperty(PropertySearcherUserProfile userProfile,
                                                                           Property property) {
        Query query = queryForFindByUserProfileAndProperty(userProfile, property, false);
        return query.getResultList();
    }

    @Override
    public PropertySearcherSearchProfile customFindLastByUserProfileAndProperty(PropertySearcherUserProfile userProfile, Property property) {
        Query query = queryForFindByUserProfileAndProperty(userProfile, property, true);
        return (PropertySearcherSearchProfile) query.getSingleResult();
    }

    @Override
    public List<PropertySearcherSearchProfile> customFindByUserProfile(PropertySearcherUserProfile userProfile) {
        Query query = queryForFindLastByUserProfile(userProfile, false);
        return query.getResultList();
    }

    @Override
    public PropertySearcherSearchProfile customFindLastByUserProfile(PropertySearcherUserProfile userProfile) {
        Query query = queryForFindLastByUserProfile(userProfile, true);
        return (PropertySearcherSearchProfile) query.getSingleResult();
    }

    @Override
    public void customDeleteAllByUserProfile(PropertySearcherUserProfile userProfile) {
        Query q = entityManager.createNativeQuery("DELETE FROM propertysearcher.searchprofile " +
                        "WHERE user_profile_id = ?;",
                PropertySearcherSearchProfile.class);
        q.setParameter(1, userProfile.getId());
        q.executeUpdate();
    }

    private Query queryForFindByUserProfileAndProperty(PropertySearcherUserProfile userProfile, Property property, boolean singleResult) {
        String limit = singleResult ? "LIMIT 1;" : ";";
        Query query = entityManager.createNativeQuery("SELECT * FROM propertysearcher.searchprofile " +
                        "WHERE user_profile_id = ? AND property_id = ? ORDER BY created DESC " + limit,
                PropertySearcherSearchProfile.class);
        query.setParameter(1, userProfile.getId());
        query.setParameter(2, property.getId());
        return query;
    }

    private String mapProfileToJson(PropertySearcherSearchProfile profile) {
        ObjectMapper mapper = new ObjectMapper();
        String dataJson = null;
        try {
            dataJson = mapper.writeValueAsString(profile.getData());
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return dataJson;
    }

    private Query queryForFindLastByUserProfile(PropertySearcherUserProfile userProfile, boolean singleResult) {
        String limit = singleResult ? "LIMIT 1;" : ";";
        Query query = entityManager.createNativeQuery(
                "SELECT * FROM propertysearcher.searchprofile WHERE user_profile_id = ?" +
                        " ORDER BY created DESC " + limit,
                PropertySearcherSearchProfile.class);
        query.setParameter(1, userProfile.getId());
        return query;
    }
}
