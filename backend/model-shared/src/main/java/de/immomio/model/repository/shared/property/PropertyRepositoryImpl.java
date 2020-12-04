package de.immomio.model.repository.shared.property;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.LandlordUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Bastian Bliemeister, Maik Kingma
 */
public class PropertyRepositoryImpl implements PropertyRepositoryCustom {

    private static final String USER_ID = "userId";
    private static final String SELECT_PROPERTY_BY_USER_ID = "SELECT * FROM landlord.property WHERE user_id = :userId";

    private final EntityManager entityManager;

    @Autowired
    public PropertyRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(Property.class);
    }

    @Override
    @Transactional
    public void customSave(Property property) {
        if (property.isNew()) {
            entityManager.persist(property);
        } else {
            entityManager.merge(property);
        }
    }

    @Override
    public List<Property> customFindByUser(LandlordUser user) {
        Query query = entityManager.createNativeQuery(SELECT_PROPERTY_BY_USER_ID, Property.class);
        query.setParameter(USER_ID, user.getId());
        return query.getResultList();
    }

    @Override
    public Property customFindOne(Long id) {
        return entityManager.find(Property.class, id);
    }

//    @Override
//    @Transactional(readOnly = true)
//    public List<Property> customFindNearestToPoint(Point point) {
//        Query query = entityManager.createNativeQuery("SELECT DISTINCT ON (p.id) p.*, " +
//                "ST_Distance(p.location, ST_SetSRID(ST_MakePoint(:long, :lat), 4326)) " +
//                "FROM landlord.property p " +
//                "WHERE ST_DWithin(p.location, cast(ST_SetSRID(ST_MakePoint(:long, :lat), 4326) as geography), 5000) " +
//                "ORDER BY p.id, ST_Distance(p.location, " +
//                "ST_SetSRID(ST_MakePoint(:long, :lat), 4326));", Property.class);
//        query.setParameter("long", point.getX());
//        query.setParameter("lat", point.getY());
//        List results = query.getResultList();
//        List<Property> properties = new LinkedList<>();
//        for (Object result : results) {
//            properties.add((Property) result);
//        }
//
//        return properties;
//    }
//
//    @Override
//    @Transactional
//    public void customUpdatePropertyLocation(Property property) {
//        Query query = entityManager.createNativeQuery("UPDATE landlord.property SET location = ST_SetSRID" +
//                "(ST_PointFromText('POINT(' || CAST(COALESCE(NULLIF(regexp_replace(" +
//                "data #>> '{address,coordinates,longitude}', '[^-0-9.]+', '', 'g'),''),'0.0') " +
//                "AS DOUBLE PRECISION ) || ' ' || CAST(COALESCE(NULLIF(regexp_replace(" +
//                "data #>> '{address,coordinates,latitude}', '[^-0-9.]+', '', 'g'),''),'0.0') " +
//                "AS DOUBLE PRECISION) || ')', 4326),4326) WHERE id = :id");
//        query.setParameter("id", property.getId());
//        query.executeUpdate();
//    }

    @Override
    public void customSave(List<Property> properties) {
        properties.forEach(this::customSave);
    }

    @Override
    @Transactional
    public void setApplicationsViewed(Long id) {
        Query query = entityManager.createNativeQuery("UPDATE landlord.property set applications_viewed = now() WHERE id = :propertyId");
        query.setParameter("propertyId", id);
        query.executeUpdate();
    }

}
