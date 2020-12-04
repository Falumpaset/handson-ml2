package de.immomio.landlord.service.contract;

import de.immomio.beans.shared.contract.DigitalContractSimpleState;
import de.immomio.beans.shared.contract.overview.DigitalContractOverviewFilterBean;
import de.immomio.beans.shared.contract.overview.DigitalContractOverviewItemBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.shared.entity.contract.DigitalContract;
import de.immomio.data.shared.entity.contract.history.DigitalContractHistoryState;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.model.repository.shared.contract.DigitalContractRepository;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DigitalContractOverviewService {

    public static final String CUSTOMER = "customer";
    public static final String CURRENT_STATE = "currentState";
    public static final String AGENT_INFO = "agentInfo";
    public static final String ID = "id";
    private DigitalContractRepository digitalContractRepository;
    private DigitalContractOverviewConverter digitalContractOverviewConverter;
    private final EntityManager entityManager;
    private final UserSecurityService userSecurityService;

    @Autowired
    public DigitalContractOverviewService(
            DigitalContractRepository digitalContractRepository,
            DigitalContractOverviewConverter digitalContractOverviewConverter,
            EntityManager entityManager,
            UserSecurityService userSecurityService
    ) {
        this.digitalContractRepository = digitalContractRepository;
        this.digitalContractOverviewConverter = digitalContractOverviewConverter;
        this.entityManager = entityManager;
        this.userSecurityService = userSecurityService;
    }

    public Page<DigitalContractOverviewItemBean> getDigitalContracts(
            DigitalContractOverviewFilterBean filterBean,
            PageRequest pageRequest
    ) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DigitalContract> query = builder.createQuery(DigitalContract.class);
        Root<DigitalContract> root = query.from(DigitalContract.class);

        List<Order> orders = new ArrayList<>();
        pageRequest.getSort().iterator().forEachRemaining(sortOrder -> {
            CriteriaQueryUtils.addOrder(root, orders, sortOrder);
        });

        Predicate predicate = createPredicate(filterBean, builder, root);
        int offset = Long.valueOf(pageRequest.getOffset()).intValue();

        List<DigitalContract> contracts = entityManager.createQuery(query.where(predicate).orderBy(orders))
                .setFirstResult(offset)
                .setMaxResults(pageRequest.getPageSize())
                .getResultList();

        List<DigitalContractOverviewItemBean> result =
                contracts
                        .stream()
                        .map(digitalContract ->
                                digitalContractOverviewConverter.convertContractEntityToBean(digitalContract))
                        .collect(Collectors.toList());

        return new PageImpl<>(result, pageRequest, getCount(builder, filterBean, userSecurityService.getPrincipalUser().getCustomer()));
    }

    private Long getCount(CriteriaBuilder builder, DigitalContractOverviewFilterBean filterBean, LandlordCustomer customer) {
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<DigitalContract> root = query.from(DigitalContract.class);

        Predicate predicate = createPredicate(filterBean, builder, root);
        query.select(builder.countDistinct(root));
        return entityManager.createQuery(query.where(predicate)).getSingleResult();
    }

    private Predicate createPredicate(
            DigitalContractOverviewFilterBean filterBean,
            CriteriaBuilder builder,
            Root<DigitalContract> root
    ) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(root.get(CUSTOMER), userSecurityService.getPrincipalUser().getCustomer()));

        if (filterBean.getStates() != null && !filterBean.getStates().isEmpty()) {
            List<DigitalContractHistoryState> internalStates = filterBean.getStates()
                    .stream()
                    .map(DigitalContractSimpleState::getInternalStates)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            predicates.add(root.get(CURRENT_STATE).in(internalStates));
        }
        if (filterBean.getAgents() != null && !filterBean.getAgents().isEmpty()) {
            List<String> agentsString = filterBean.getAgents().stream().map(String::valueOf).collect(Collectors.toList());
            CriteriaQueryUtils.populateJsonbInPredicate(builder, root, AGENT_INFO, ID, agentsString, predicates);
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }


}
