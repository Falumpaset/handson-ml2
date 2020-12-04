package de.immomio.controller.propertyProposal;

import de.immomio.beans.landlord.proposal.ProposalSearchBean;
import de.immomio.data.shared.bean.proposal.LandlordPropertyProposalBean;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposalState;
import de.immomio.data.shared.entity.property.search.PropertySearcherSearchBean;
import de.immomio.landlord.service.property.notification.PropertyProposalNotificationService;
import de.immomio.landlord.service.proposal.PropertyProposalSearchService;
import de.immomio.landlord.service.reporting.indexing.delegate.LandlordPropertySearcherIndexingDelegate;
import de.immomio.model.repository.shared.propertyProposal.PropertyProposalRepository;
import de.immomio.service.property.PropertyProposalService;
import de.immomio.service.property.cache.PropertyCountRefreshCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

/**
 * @author Maik Kingma
 */
@Slf4j
@RepositoryRestController
@RequestMapping(value = "/propertyProposals")
public class PropertyProposalController {

    private static final String PARAM_SEARCH = "search";

    private final PropertyProposalRepository propertyProposalRepository;

    private final PropertyProposalService propertyProposalService;

    private final PropertyProposalSearchService propertyProposalSearchService;

    private final PropertyProposalNotificationService propertyProposalNotificationService;

    private final PagedResourcesAssembler<LandlordPropertyProposalBean> pagedResourcesAssembler;

    private final PropertyCountRefreshCacheService refreshPropertyCountsCache;

    private final LandlordPropertySearcherIndexingDelegate propertySearcherIndexingDelegate;

    @Value("${property.new.for.prospect.notification}")
    private boolean propertyNewForProspectNotification;

    @Autowired
    public PropertyProposalController(
            PropertyProposalRepository propertyProposalRepository,
            PropertyProposalService propertyProposalService,
            PropertyProposalSearchService propertyProposalSearchService,
            PropertyProposalNotificationService propertyProposalNotificationService,
            PagedResourcesAssembler<LandlordPropertyProposalBean> pagedResourcesAssembler,
            PropertyCountRefreshCacheService refreshPropertyCountsCache,
            LandlordPropertySearcherIndexingDelegate propertySearcherIndexingDelegate
    ) {
        this.propertyProposalRepository = propertyProposalRepository;
        this.propertyProposalService = propertyProposalService;
        this.propertyProposalSearchService = propertyProposalSearchService;
        this.propertyProposalNotificationService = propertyProposalNotificationService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.refreshPropertyCountsCache = refreshPropertyCountsCache;
        this.propertySearcherIndexingDelegate = propertySearcherIndexingDelegate;
    }

    @PostMapping(value = "/offer/{id}")
    public ResponseEntity invite(PersistentEntityResourceAssembler assembler, @PathVariable Long id) {
        try {
            Optional<PropertyProposal> proposalOpt = propertyProposalRepository.findById(id);

            if (proposalOpt.isPresent()) {
                PropertyProposal proposal = proposalOpt.get();
                propertyProposalService.updateState(proposal, PropertyProposalState.OFFERED);

                if (propertyNewForProspectNotification) {
                    propertyProposalNotificationService.invitedForProposal(proposal);
                }
                refreshPropertyCountsCache.refreshProposalCache(proposal.getProperty());
                propertySearcherIndexingDelegate.proposalOffered(proposal);
                return new ResponseEntity<>(assembler.toModel(proposal), HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("No proposal matches the requested Id.", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/deny/{id}")
    public ResponseEntity deny(PersistentEntityResourceAssembler assembler, @PathVariable Long id) {
        try {
            Optional<PropertyProposal> proposalOpt = propertyProposalRepository.findById(id);

            if (proposalOpt.isPresent()) {
                PropertyProposal proposal = proposalOpt.get();
                propertyProposalService.updateState(proposal, PropertyProposalState.DENIEDBYLL);
                refreshPropertyCountsCache.refreshProposalCache(proposal.getProperty());

                return new ResponseEntity<>(assembler.toModel(proposal), HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("No proposal matches the requested Id.", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search/findByUser")
    public ResponseEntity<List<PropertySearcherSearchBean>> searchByUser(@RequestParam(PARAM_SEARCH) String searchValue) {
        List<PropertySearcherSearchBean> userBeans = propertyProposalSearchService.findByNameAndEmail(searchValue);

        return ResponseEntity.ok(userBeans);
    }

    @PostMapping("/search/list")
    @PreAuthorize("@userSecurityService.allowUserToReadProperty(#searchBean?.propertyId)")
    public ResponseEntity<PagedModel<EntityModel<LandlordPropertyProposalBean>>> find(
            @RequestBody ProposalSearchBean searchBean
    ) {
        PageRequest pageRequest = PageRequest.of(searchBean.getPage(), searchBean.getSize(), searchBean.getSort());
        Page<LandlordPropertyProposalBean> result = propertyProposalSearchService.search(searchBean, pageRequest);

        PagedModel<EntityModel<LandlordPropertyProposalBean>> pagedModel = pagedResourcesAssembler.toModel(result);
        return ResponseEntity.ok(pagedModel);
    }

    @PostMapping("search/list/count")
    @PreAuthorize("@userSecurityService.allowUserToReadProperty(#searchBean?.propertyId)")
    public ResponseEntity<?> findProposalCount(
            @RequestBody ProposalSearchBean searchBean
    ) {
        return ResponseEntity.ok(propertyProposalSearchService.getCountOfProposals(searchBean));
    }

}
