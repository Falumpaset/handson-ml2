package de.immomio.controller.propertyProposal;

import de.immomio.data.shared.bean.proposal.PropertySearcherPropertyProposalBean;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposalState;
import de.immomio.model.repository.shared.propertyProposal.PropertyProposalRepository;
import de.immomio.service.propertyProposals.PropertySearcherPropertyProposalService;
import de.immomio.service.security.UserSecurityService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Maik Kingma
 */

@RepositoryRestController
@RequestMapping(value = "/propertyProposals")
@Slf4j
public class PropertyProposalController {

    private static final String PROPOSAL_IN_UNSUITABLE_STATE = "PROPOSAL_IN_UNSUITABLE_STATE_L";

    private final PropertyProposalRepository propertyProposalRepository;

    private final UserSecurityService userSecurityService;

    private final PropertySearcherPropertyProposalService proposalService;

    private final PagedResourcesAssembler<PropertySearcherPropertyProposalBean> pagedResourcesAssembler;

    @Autowired
    public PropertyProposalController(
            PropertyProposalRepository propertyProposalRepository,
            UserSecurityService userSecurityService,
            PropertySearcherPropertyProposalService proposalService,
            PagedResourcesAssembler<PropertySearcherPropertyProposalBean> pagedResourcesAssembler
    ) {
        this.propertyProposalRepository = propertyProposalRepository;
        this.userSecurityService = userSecurityService;
        this.proposalService = proposalService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @PostMapping(value = "/accept/{id}")
    public ResponseEntity accept(
            @Parameter(hidden = true)  PersistentEntityResourceAssembler assembler,
            @PathVariable Long id) {
        try {
            PropertyProposal proposal = propertyProposalRepository.customFindOne(id);
            if (!proposal.getUserProfile().getUser().getId().equals(userSecurityService.getPrincipalId())) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            if (proposal.getState() != PropertyProposalState.OFFERED) {
                return new ResponseEntity<>(PROPOSAL_IN_UNSUITABLE_STATE, HttpStatus.BAD_REQUEST);
            }
            proposalService.acceptProposal(proposal);

            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping(value = "/deny/{id}")
    public ResponseEntity deny(PersistentEntityResourceAssembler assembler, @PathVariable Long id) {
        PropertyProposal proposal;
        try {
            proposal = propertyProposalRepository.customFindOne(id);

            if (!proposal.getUserProfile().getUser().getId().equals(userSecurityService.getPrincipalId())) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            if (proposal.getState() != PropertyProposalState.OFFERED) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            PropertyProposal saved = proposalService.deny(proposal);
            return new ResponseEntity<>(assembler.toModel(saved), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("")
    public ResponseEntity<PagedModel<EntityModel<PropertySearcherPropertyProposalBean>>> getProposals(Pageable pageable) {
        Page<PropertySearcherPropertyProposalBean> proposals = proposalService.findAllForPs(userSecurityService.getPrincipalUser(), pageable);

        PagedModel<EntityModel<PropertySearcherPropertyProposalBean>> pagedModel = pagedResourcesAssembler.toModel(proposals);
        return ResponseEntity.ok(pagedModel);
    }
}
