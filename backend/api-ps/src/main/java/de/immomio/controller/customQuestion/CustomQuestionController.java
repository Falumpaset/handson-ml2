package de.immomio.controller.customQuestion;

import de.immomio.beans.propertysearcher.SharedPropertyData;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.shared.bean.customQuestion.CustomQuestionBean;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import de.immomio.service.customQuestion.PropertySearcherCustomQuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
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
@RequestMapping(value = "/customQuestions")
public class CustomQuestionController {
    private final PropertySearcherCustomQuestionService customQuestionService;

    @Autowired
    public CustomQuestionController(PropertySearcherCustomQuestionService customQuestionService) {
        this.customQuestionService = customQuestionService;
    }

    @GetMapping("/search/findByApplication/{application}")
    @PreAuthorize("#application.userProfile.user.id == principal.id")
    public ResponseEntity<List<CustomQuestionBean>> findByApplication(@PathVariable("application") PropertyApplication application) {
        return ResponseEntity.ok(customQuestionService.findForUserProfileAndProperty(application.getUserProfile(), application.getProperty()));
    }

    @GetMapping("/search/findByProposal/{proposal}")
    @PreAuthorize("#proposal.userProfile.user.id == principal.id")
    public ResponseEntity<List<CustomQuestionBean>> findByProposal(@PathVariable("proposal") PropertyProposal proposal) {
        return ResponseEntity.ok(customQuestionService.findForUserProfileAndProperty(proposal.getUserProfile(), proposal.getProperty()));
    }

    @GetMapping(value = "/shared/search/findByProperty/{property}")
    public ResponseEntity<List<CustomQuestionBean>> sharedFindByProperty(@PathVariable("property") Long property) {
        return ResponseEntity.ok(customQuestionService.findForProperty(property));
    }
}
