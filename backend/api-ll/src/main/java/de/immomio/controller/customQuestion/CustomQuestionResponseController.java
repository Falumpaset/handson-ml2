package de.immomio.controller.customQuestion;

import de.immomio.data.shared.bean.customQuestion.CustomQuestionResponseQuestionBean;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import de.immomio.service.customQuestion.CustomQuestionResponseService;
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
@RequestMapping(value = "/customQuestionResponses")
public class CustomQuestionResponseController {

    private CustomQuestionResponseService questionResponseService;

    @Autowired
    public CustomQuestionResponseController(CustomQuestionResponseService questionResponseService) {
        this.questionResponseService = questionResponseService;
    }

    @GetMapping("/search/findByApplication/{application}")
    @PreAuthorize("#application.property.customer.id == principal.customer.id")
    public ResponseEntity<List<CustomQuestionResponseQuestionBean>> findByApplication(@PathVariable("application") PropertyApplication application) {
        return ResponseEntity.ok(questionResponseService.getCustomQuestionResponses(application.getProperty(), application.getUserProfile()));
    }

    @GetMapping("/search/findByProposal/{proposal}")
    @PreAuthorize("#proposal.property.customer.id == principal.customer.id")
    public ResponseEntity<List<CustomQuestionResponseQuestionBean>> findByProposal(@PathVariable("proposal") PropertyProposal proposal) {
        return ResponseEntity.ok(questionResponseService.getCustomQuestionResponses(proposal.getProperty(), proposal.getUserProfile()));
    }
}
