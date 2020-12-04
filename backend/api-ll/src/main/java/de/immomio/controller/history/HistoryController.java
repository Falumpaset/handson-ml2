package de.immomio.controller.history;

import de.immomio.beans.shared.PropertySearcherHistoryBean;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import de.immomio.landlord.service.history.LandlordHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@RepositoryRestController
@RequestMapping(value = "/history")
public class HistoryController {

    private final LandlordHistoryService historyService;

    @Autowired
    public HistoryController(LandlordHistoryService historyService) {
        this.historyService = historyService;
    }

    // we pass an application to get the psuser from it. We dont have a repo on the ll app
    @GetMapping("/application/{application}")
    @PreAuthorize("#application.property.customer.id == principal.customer.id")
    public ResponseEntity<?> getApplicationHistory(@PathVariable("application") PropertyApplication application) {
        List<PropertySearcherHistoryBean> history = historyService.getHistory(application.getUserProfile());

        return ResponseEntity.ok(history);
    }

    // we pass an proposal to get the psuser from it. We dont have a repo on the ll app
    @GetMapping("/proposal/{proposal}")
    @PreAuthorize("#proposal.property.customer.id == principal.customer.id")
    public ResponseEntity<?> getProposalHistory(@PathVariable("proposal") PropertyProposal proposal) {
        List<PropertySearcherHistoryBean> history = historyService.getHistory(proposal.getUserProfile());

        return ResponseEntity.ok(history);
    }
}
