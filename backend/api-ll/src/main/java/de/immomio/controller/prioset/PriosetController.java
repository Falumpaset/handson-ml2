package de.immomio.controller.prioset;

import de.immomio.data.landlord.bean.prioset.ChangePriosetBean;
import de.immomio.data.landlord.bean.prioset.PriosetBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.prioset.Prioset;
import de.immomio.landlord.service.prioset.PriosetConverter;
import de.immomio.landlord.service.prioset.PriosetService;
import de.immomio.landlord.service.security.UserSecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @author Niklas Lindemann
 */

@Slf4j
@RepositoryRestController
@RequestMapping(value = "/priosets")
public class PriosetController {

    private final PriosetService priosetService;

    private final PriosetConverter priosetConverter;

    private final UserSecurityService userSecurityService;

    private final PagedResourcesAssembler<Object> pagedResourcesAssembler;

    @Autowired
    public PriosetController(PriosetService priosetService, PriosetConverter priosetConverter, UserSecurityService userSecurityService,
            PagedResourcesAssembler<Object> pagedResourcesAssembler) {
        this.priosetService = priosetService;
        this.priosetConverter = priosetConverter;
        this.userSecurityService = userSecurityService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @PostMapping()
    public ResponseEntity<PriosetBean> createPrioset(
            @RequestBody ChangePriosetBean newPriosetBean
    ) {
        LandlordCustomer customer = userSecurityService.getPrincipalUser().getCustomer();
        PriosetBean priosetBean = priosetConverter.priosetToBean(priosetService.create(newPriosetBean, customer));
        return ResponseEntity.ok(priosetBean);
    }

    @PreAuthorize("#prioset.customer.id == principal.customer.id")
    @PatchMapping(value = "/{prioset}")
    public ResponseEntity<PriosetBean> updatePrioset(
            @PathVariable("prioset") Prioset prioset,
            @RequestBody ChangePriosetBean updatePriosetBean
    ) {
        PriosetBean priosetBean = priosetConverter.priosetToBean(priosetService.update(prioset, updatePriosetBean));
        return ResponseEntity.ok(priosetBean);
    }

    @PreAuthorize("#prioset.customer.id == principal.customer.id")
    @GetMapping(value = "/{prioset}")
    public ResponseEntity<PriosetBean> getPrioset(
            @PathVariable("prioset") Prioset prioset
    ) {
        PriosetBean priosetBean = priosetConverter.priosetToBean(priosetService.get(prioset));
        return ResponseEntity.ok(priosetBean);
    }

    @DeleteMapping("/{prioset}")
    public ResponseEntity<?> deletePrioset(
            @PathVariable("prioset") Prioset prioset,
            @RequestParam(value = "newPrioset", required = false) Prioset priosetToReplace
    ) {
        LandlordCustomer customer = userSecurityService.getPrincipalUser().getCustomer();
        priosetService.delete(prioset, priosetToReplace, customer);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search/findByTemplate")
    public ResponseEntity<?> findByTemplate(@RequestParam("template") Boolean template, Pageable pageable) {
        Page priosets = priosetService.findByTemplate(template, pageable).map(priosetConverter::priosetToBean);
        if (!priosets.hasContent()) {
            return ResponseEntity.ok(pagedResourcesAssembler.toEmptyModel(priosets, PriosetBean.class));
        }

        return ResponseEntity.ok(pagedResourcesAssembler.toModel(priosets));
    }
}

