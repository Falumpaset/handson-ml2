package de.immomio.landlord.service.schufa;

import de.immomio.beans.landlord.schufa.SchufaReportSearchBean;
import de.immomio.data.base.bean.schufa.cbi.enums.CbiActionType;
import de.immomio.data.base.type.schufa.JobState;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.schufa.LandlordSchufaJob;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.model.entity.landlord.schufa.SchufaReportBean;
import de.immomio.utils.criteria.CriteriaQueryUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import java.util.stream.Collectors;

@Slf4j
@Service
public class SchufaJobService {

    private static final String CUSTOMER = "customer";

    private static final String USER = "user";

    private static final String AGENT_INFO = "agentInfo";

    private static final String STATE = "state";

    private static final String TYPE = "type";

    private static final String ID = "id";

    private final EntityManager entityManager;

    private final UserSecurityService userSecurityService;

    @Autowired
    public SchufaJobService(EntityManager entityManager, UserSecurityService userSecurityService) {
        this.entityManager = entityManager;
        this.userSecurityService = userSecurityService;
    }

    public Page<SchufaReportBean> search(SchufaReportSearchBean searchBean, PageRequest pageRequest) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<LandlordSchufaJob> query = builder.createQuery(LandlordSchufaJob.class);
        Root<LandlordSchufaJob> root = query.from(LandlordSchufaJob.class);

        List<Order> orders = CriteriaQueryUtils.generateSortOrders(builder, root, pageRequest);
        Predicate predicate = createPredicate(searchBean, builder, root);
        Long count = getCount(builder, searchBean);

        int offset = Long.valueOf(pageRequest.getOffset()).intValue();
        List<LandlordSchufaJob> schufaJobs = entityManager.createQuery(query.where(predicate)
                .orderBy(orders))
                .setFirstResult(offset)
                .setMaxResults(pageRequest.getPageSize())
                .getResultList();

        List<SchufaReportBean> schufaReportBeans = schufaJobs
                .stream()
                .map(SchufaReportBean::new)
                .collect(Collectors.toList());

        return new PageImpl<>(schufaReportBeans, pageRequest, count);
    }

    private Predicate createPredicate(
            SchufaReportSearchBean searchBean,
            CriteriaBuilder builder,
            Root<LandlordSchufaJob> root
    ) {
        LandlordUser landlordUser = userSecurityService.getPrincipalUser();
        LandlordCustomer landlordCustomer = landlordUser.getCustomer();
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(builder.equal(root.get(CUSTOMER).get(ID), landlordCustomer.getId()));

        Long userId = searchBean.getUserId();
        if (userId != null) {
            predicates.add(builder.equal(root.get(USER).get(ID), userId));
        }

        List<JobState> states = searchBean.getStates();
        if (states != null && !states.isEmpty()) {
            predicates.add(root.get(STATE).in(states));
        }

        List<CbiActionType> types = searchBean.getTypes();
        if (types != null && !types.isEmpty()) {
            predicates.add(root.get(TYPE).in(types));
        }

        populateAgentPredicate(builder, root, predicates, searchBean);

        return builder.and(predicates.toArray(new Predicate[0]));
    }

    private void populateAgentPredicate(
            CriteriaBuilder builder,
            Root<LandlordSchufaJob> root,
            List<Predicate> predicates,
            SchufaReportSearchBean searchBean
    ) {
        List<Long> agentIds = searchBean.getAgents();
        if (agentIds != null && !agentIds.isEmpty()) {
            List<String> convertedAgentIds = agentIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList());
            CriteriaQueryUtils.populateJsonbInPredicate(builder, root, AGENT_INFO, ID, convertedAgentIds, predicates);
        }
    }

    private Long getCount(CriteriaBuilder builder, SchufaReportSearchBean searchBean) {
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<LandlordSchufaJob> root = query.from(LandlordSchufaJob.class);

        Predicate predicate = createPredicate(searchBean, builder, root);
        query.select(builder.countDistinct(root));

        return entityManager.createQuery(query.where(predicate)).getSingleResult();
    }
}
