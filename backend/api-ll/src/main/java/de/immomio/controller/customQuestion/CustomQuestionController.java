package de.immomio.controller.customQuestion;

import de.immomio.beans.landlord.CustomQuestionCreateBean;
import de.immomio.constants.exceptions.CustomQuestionValidationException;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.customquestion.CustomQuestion;
import de.immomio.data.shared.bean.customQuestion.CustomQuestionBean;
import de.immomio.landlord.service.customquestion.LandlordCustomQuestionService;
import de.immomio.landlord.service.security.UserSecurityService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Maik Kingma
 */

@Slf4j
@RepositoryRestController
@RequestMapping(value = "/customQuestions")
public class CustomQuestionController {

    private final LandlordCustomQuestionService customQuestionService;

    private final UserSecurityService userSecurityService;

    @Autowired
    public CustomQuestionController(
            LandlordCustomQuestionService customQuestionService,
            UserSecurityService userSecurityService) {
        this.customQuestionService = customQuestionService;
        this.userSecurityService = userSecurityService;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<Object> create(
            @RequestBody CustomQuestionCreateBean questionBean,
            @Parameter(hidden = true) PersistentEntityResourceAssembler assembler
    ) {
        LandlordCustomer customer = userSecurityService.getPrincipalUser().getCustomer();
        try {
            CustomQuestion customQuestion = customQuestionService.createCustomQuestion(questionBean, customer);
            return new ResponseEntity<>(assembler.toModel(customQuestion), HttpStatus.CREATED);
        } catch (CustomQuestionValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("#customQuestion.customer.id == principal?.customer.id")
    @DeleteMapping("/delete/{customQuestion}")
    public ResponseEntity delete(@PathVariable("customQuestion") CustomQuestion customQuestion) {
        customQuestionService.delete(customQuestion);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity findAll(@RequestParam("global") boolean global,  @Parameter(hidden = true) PagedResourcesAssembler<CustomQuestionBean> pagedResourcesAssembler
            , Pageable pageable) {
        Page<CustomQuestionBean> customQuestions = customQuestionService.findAll(userSecurityService.getPrincipalUser().getCustomer(), global, pageable);
        return new ResponseEntity(pagedResourcesAssembler.toModel(customQuestions), HttpStatus.OK);
    }
}
