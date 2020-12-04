package de.immomio.landlord.service.propertyuserprofile;

import de.immomio.data.propertysearcher.entity.user.PropertySearcherSearchProfile;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.note.Note;
import de.immomio.data.shared.entity.property.PropertyUserProfileAssignment;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.landlord.service.security.UserSecurityService;
import org.hibernate.query.criteria.internal.OrderImpl;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.Date;
import java.util.List;

/**
 * @author Niklas Lindemann
 */
public abstract class PropertyUserProfileAssignmentSearchService<T extends PropertyUserProfileAssignment> {

    public static final String FIELD_NAME_CREATED = "created";
    public static final String FIELD_NAME_DELETED = "deleted";
    public static final String FIELD_NAME_SEARCH_PROFILES = "searchProfiles";
    private static final String FIELD_NAME_RATING = "rating";
    private static final String FIELD_NAME_NOTES = "notes";
    private static final String FIELD_NAME_CUSTOMER = "customer";
    private static final String FIELD_NAME_USER_PROFILE = "userProfile";
    private static final String SORT_NAME_SP_CREATED = "searchprofile.created";
    private static final String SORT_NAME_RATING = "rating";

    private final UserSecurityService userSecurityService;

    protected PropertyUserProfileAssignmentSearchService(UserSecurityService userSecurityService) {
        this.userSecurityService = userSecurityService;
    }

    protected void addOrder(CriteriaBuilder builder, Root<T> root, List<Order> orders, Sort.Order sortOrder) {
        if (isRatingOrder(sortOrder)) {
            addRatingOrder(builder, root, orders, sortOrder.isAscending());
        }
        if (isSearchProfileOrder(sortOrder)) {
            addSearchProfileCreatedOrder(builder, root, orders, sortOrder.isAscending());
        } else {
            addOrder(root, orders, sortOrder);
        }
    }

    protected void addSearchProfileCreatedOrder(CriteriaBuilder builder, Root<T> root, List<Order> orders, boolean asc) {
        Join<T, PropertySearcherUserProfile> userJoin = root.join(FIELD_NAME_USER_PROFILE);

        CriteriaQuery<PropertySearcherSearchProfile> searchProfileCriteriaQuery = builder.createQuery(PropertySearcherSearchProfile.class);

        Subquery<Date> createdSubQuery = searchProfileCriteriaQuery.subquery(Date.class);

        Root<PropertySearcherSearchProfile> subQueryRoot = createdSubQuery.from(PropertySearcherSearchProfile.class);

        Predicate deletedPredicate = builder.equal(subQueryRoot.get(FIELD_NAME_DELETED), false);
        Predicate userProfilePredicate = builder.equal(subQueryRoot.get(FIELD_NAME_USER_PROFILE), root.get(FIELD_NAME_USER_PROFILE));

        Path createdPath = subQueryRoot.get(FIELD_NAME_CREATED);

        createdSubQuery.where(deletedPredicate, userProfilePredicate).select(builder.min(createdPath));

        Join<Object, Object> searchProfiles = userJoin.join(FIELD_NAME_SEARCH_PROFILES, JoinType.LEFT);
        searchProfiles.on(builder.equal(searchProfiles.get(FIELD_NAME_DELETED), false),
                builder.equal(searchProfiles.get(FIELD_NAME_USER_PROFILE), root.get(FIELD_NAME_USER_PROFILE)));

        searchProfiles.on(searchProfiles.get(FIELD_NAME_CREATED).in(createdSubQuery));

        orders.add(new OrderImpl(builder.coalesce(searchProfiles.get(FIELD_NAME_CREATED), new Date(0L)), asc));
    }

    protected boolean isSearchProfileOrder(Sort.Order sortOrder) {
        return sortOrder.getProperty().equalsIgnoreCase(SORT_NAME_SP_CREATED);
    }

    private boolean isRatingOrder(Sort.Order sortOrder) {
        return sortOrder.getProperty().equalsIgnoreCase(SORT_NAME_RATING);
    }

    private void addRatingOrder(CriteriaBuilder builder, Root<T> root, List<Order> orders, boolean asc) {
        Join<PropertyApplication, PropertySearcherUserProfile> userJoin = root.join(FIELD_NAME_USER_PROFILE);
        Join<Join<PropertyApplication, PropertySearcherUserProfile>, List<Note>> notesJoin = userJoin.join(FIELD_NAME_NOTES, JoinType.LEFT);
        notesJoin.on(builder.equal(notesJoin.get(FIELD_NAME_CUSTOMER), userSecurityService.getPrincipalUser().getCustomer()));
        orders.add(new OrderImpl(builder.coalesce(notesJoin.get(FIELD_NAME_RATING), 0.0), asc));
    }

    private void addOrder(Root<T> root, List<Order> orders, Sort.Order sortOrder) {
        boolean ascending = sortOrder.getDirection() == Sort.Direction.ASC;

        orders.add(new OrderImpl(root.get(sortOrder.getProperty()), ascending));
    }

}
