package de.immomio.landlord.service.user;

import de.immomio.beans.landlord.LandlordUserSearchBean;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUserOverviewBean;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.utils.criteria.CriteriaQueryUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static de.immomio.utils.criteria.CriteriaQueryUtils.addEnumListToPredicates;

/**
 * @author Niklas Lindemann
 */

@Service
public class LandlordUserSearchService {

    public static final String QUERY_FIELD_ENABLED = "enabled";
    public static final String QUERY_FIELD_USERTYPE = "usertype";
    public static final String QUERY_FIELD_CUSTOMER = "customer";
    public static final String QUERY_FIELD_PROFILE = "profile";
    public static final String FIRSTNAME = "firstname";
    public static final String NAME = "name";
    public static final char PERCENT = '%';
    public static final String SPACE = " ";
    public static final String EMPTY = "";

    private final EntityManager entityManager;
    private UserSecurityService userSecurityService;

    private LandlordUserConverter landlordUserConverter;

    public LandlordUserSearchService(
            EntityManager entityManager,
            UserSecurityService userSecurityService,
            LandlordUserConverter landlordUserConverter) {
        this.entityManager = entityManager;
        this.userSecurityService = userSecurityService;
        this.landlordUserConverter = landlordUserConverter;
    }

    public PageImpl<LandlordUserOverviewBean> search(LandlordUserSearchBean searchBean) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        PageRequest pageRequest = PageRequest.of(searchBean.getPage(), searchBean.getSize(), searchBean.getSort());

        CriteriaQuery<LandlordUser> query = builder.createQuery(LandlordUser.class);
        Root<LandlordUser> root = query.from(LandlordUser.class);
        Predicate predicate = createPredicate(searchBean, builder, root);

        List<Order> orders = CriteriaQueryUtils.generateSortOrders(builder, root, pageRequest);

        Long count = getCount(builder, searchBean);
        int offset = Long.valueOf(pageRequest.getOffset()).intValue();

        List<LandlordUser> users = entityManager.createQuery(query.where(predicate).orderBy(orders))
                .setFirstResult(offset)
                .setMaxResults(pageRequest.getPageSize())
                .getResultList();

        return new PageImpl<>(users.stream().map(landlordUserConverter::convertUserToUserOverviewBean).collect(Collectors.toList()), pageRequest, count);
    }

    public Map<String, Long> getCountOfUsers(Map<String, LandlordUserSearchBean> userSearchBean) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        builder.createQuery(Long.class);

        return userSearchBean.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> getCount(builder, entry.getValue())));
    }

    private Predicate createPredicate(LandlordUserSearchBean searchBean, CriteriaBuilder builder, Root root) {
        List<Predicate> predicates = new ArrayList<>();

        if (searchBean.getEnabled() != null) {
            predicates.add(builder.equal(root.get(QUERY_FIELD_ENABLED), searchBean.getEnabled()));
        }

        if (!searchBean.getTypes().isEmpty()) {
            addEnumListToPredicates(searchBean.getTypes(), builder, root.get(QUERY_FIELD_USERTYPE), predicates);
        }

        String searchTerm = searchBean.getSearchTerm();
        if (StringUtils.isNotBlank(searchTerm)) {
            searchTerm = searchTerm.replace(SPACE, EMPTY);
            Predicate concat = builder.like(builder.concat(
                    builder.lower(CriteriaQueryUtils.jsonFunction(builder, root, QUERY_FIELD_PROFILE, FIRSTNAME)),
                    builder.lower(CriteriaQueryUtils.jsonFunction(builder,  root, QUERY_FIELD_PROFILE, NAME))), PERCENT + searchTerm.toLowerCase() + PERCENT);

            predicates.add(concat);
        }

        predicates.add(builder.equal(root.get(QUERY_FIELD_CUSTOMER), userSecurityService.getPrincipalUser().getCustomer()));
        return builder.and(predicates.toArray(new Predicate[0]));
    }

    private Long getCount(CriteriaBuilder builder, LandlordUserSearchBean searchBean) {
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<LandlordUser> root = query.from(LandlordUser.class);

        Predicate predicate = createPredicate(searchBean, builder, root);
        query.select(builder.countDistinct(root));
        return entityManager.createQuery(query.where(predicate)).getSingleResult();
    }
}
