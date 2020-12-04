package de.immomio.landlord.service.proposal;

import de.immomio.beans.landlord.proposal.ProposalSearchBean;
import de.immomio.beans.shared.CommonCountBean;
import de.immomio.data.base.type.user.profile.PropertySearcherUserProfileType;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.bean.proposal.LandlordPropertyProposalBean;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposalState;
import de.immomio.data.shared.entity.property.search.PropertySearcherSearchBean;
import de.immomio.data.shared.entity.property.search.PropertySearcherSearchItem;
import de.immomio.landlord.service.propertyuserprofile.PropertyUserProfileAssignmentSearchService;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.model.repository.landlord.propertysearcher.searchprofile.LandlordPropertySearcherUserSearchProfileRepository;
import de.immomio.model.repository.shared.propertyProposal.PropertyProposalRepository;
import de.immomio.service.proposal.PropertyProposalConverter;
import de.immomio.utils.criteria.CriteriaQueryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PropertyProposalSearchService extends PropertyUserProfileAssignmentSearchService<PropertyProposal> {

    private static final String PROPERTY = "property";
    private static final String USER_PROFILE = "userProfile";
    private static final String STATE = "state";
    private static final String TYPE = "type";
    private static final String ID = "id";
    private static final String WBS = "additionalInformation.wbs";
    private static final String DATA = "data";
    private final PropertyProposalRepository proposalRepository;
    private final EntityManager entityManager;
    private final PropertyProposalConverter propertyProposalConverter;
    private final LandlordPropertySearcherUserSearchProfileRepository searchProfileRepository;

    @Autowired
    public PropertyProposalSearchService(PropertyProposalRepository proposalRepository,
            EntityManager entityManager,
            PropertyProposalConverter propertyProposalConverter,
            LandlordPropertySearcherUserSearchProfileRepository searchProfileRepository,
            UserSecurityService userSecurityService) {
        super(userSecurityService);
        this.proposalRepository = proposalRepository;
        this.entityManager = entityManager;
        this.propertyProposalConverter = propertyProposalConverter;
        this.searchProfileRepository = searchProfileRepository;
    }

    public List<PropertySearcherSearchBean> findByNameAndEmail(String searchValue) {
        searchValue = searchValue.replace(" ", "%");
        List<PropertyProposal> proposals = proposalRepository.findByPsNameOrEmail(searchValue);

        return proposals.stream()
                .collect(Collectors.groupingBy(PropertyProposal::getUserProfile))
                .entrySet()
                .stream()
                .map(this::getPropertySearcherSearchBean)
                .collect(Collectors.toList());
    }

    public Page<LandlordPropertyProposalBean> search(ProposalSearchBean searchBean, PageRequest pageRequest) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PropertyProposal> query = builder.createQuery(PropertyProposal.class);
        Root<PropertyProposal> root = query.from(PropertyProposal.class);

        List<Order> orders = new ArrayList<>();
        pageRequest.getSort().iterator().forEachRemaining(sortOrder -> {
            addOrder(builder, root, orders, sortOrder);
        });

        Predicate predicate = createPredicate(searchBean, builder, root);
        Long count = getCount(builder, searchBean);

        int offset = Long.valueOf(pageRequest.getOffset()).intValue();
        List<PropertyProposal> proposals = entityManager.createQuery(query.where(predicate).orderBy(orders))
                .setFirstResult(offset)
                .setMaxResults(pageRequest.getPageSize())
                .getResultList();

        return new PageImpl<>(proposals.stream().map(this::collectPropertyProposalBean).collect(Collectors.toList()), pageRequest, count);
    }

    public CommonCountBean getCountOfProposals(ProposalSearchBean searchBean) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        return CommonCountBean.builder().count(getCount(builder, searchBean)).build();
    }

    private LandlordPropertyProposalBean collectPropertyProposalBean(PropertyProposal proposal) {
        Date firstCreatedDate = searchProfileRepository.getFirstCreatedDate(proposal.getUserProfile());
        return propertyProposalConverter.convertToProposalBean(proposal, firstCreatedDate);
    }

    private Predicate createPredicate(ProposalSearchBean searchBean, CriteriaBuilder builder, Root<PropertyProposal> root) {
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(builder.equal(root.get(PROPERTY), searchBean.getPropertyId()));

        List<PropertyProposalState> states = searchBean.getStates();
        if (states != null && !states.isEmpty()) {
            predicates.add(root.get(STATE).in(states));
        }

        Long userId = searchBean.getUserId();
        if (userId != null) {
            predicates.add(builder.equal(root.get(USER_PROFILE).get(ID), userId));
        }

        List<PropertySearcherUserProfileType> userProfileTypes = searchBean.getUserProfileTypes();
        if (userProfileTypes != null && !userProfileTypes.isEmpty()) {
            predicates.add(root.get(USER_PROFILE).get(TYPE).in(userProfileTypes));
        }

        joinUserProfile(searchBean, builder, root, predicates);

        return builder.and(predicates.toArray(new Predicate[0]));
    }

    private void joinUserProfile(ProposalSearchBean searchBean, CriteriaBuilder builder, Root root, List<Predicate> predicates) {

        Join userJoin = root.join(USER_PROFILE);

        if (searchBean.getWbs() != null) {
            CriteriaQueryUtils.populateJsonbEqualPredicate(builder, userJoin, DATA, WBS, String.valueOf(searchBean.getWbs()), predicates);
        }
    }

    private Long getCount(CriteriaBuilder builder, ProposalSearchBean proposalSearchBean) {
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<PropertyProposal> root = query.from(PropertyProposal.class);

        Predicate predicate = createPredicate(proposalSearchBean, builder, root);
        query.select(builder.countDistinct(root));

        return entityManager.createQuery(query.where(predicate)).getSingleResult();
    }

    private PropertySearcherSearchBean getPropertySearcherSearchBean(Map.Entry<PropertySearcherUserProfile, List<PropertyProposal>> entry) {
        PropertySearcherUserProfile userProfile = entry.getKey();
        List<PropertyProposal> groupedProposals = entry.getValue();
        List<PropertySearcherSearchItem> applicationBeans = groupedProposals.stream()
                .map(proposal -> new PropertySearcherSearchItem(proposal.getProperty(), proposal.getState().name(), proposal.getScore(), proposal.getId()))
                .collect(Collectors.toList());
        return new PropertySearcherSearchBean(userProfile, applicationBeans);
    }

}
