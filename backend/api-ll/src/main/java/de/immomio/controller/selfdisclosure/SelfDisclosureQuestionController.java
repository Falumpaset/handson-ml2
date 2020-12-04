package de.immomio.controller.selfdisclosure;

import de.immomio.beans.shared.selfdisclosure.SelfDisclosureQuestionBean;
import de.immomio.landlord.service.selfdisclosure.LandlordSelfDisclosureQuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@RepositoryRestController
@RequestMapping(value = "/selfDisclosureQuestions")
public class SelfDisclosureQuestionController {

    private final LandlordSelfDisclosureQuestionService questionService;

    public SelfDisclosureQuestionController(LandlordSelfDisclosureQuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public ResponseEntity<List<SelfDisclosureQuestionBean>> get() {
        return ResponseEntity.ok(questionService.getQuestionBeans());
    }
}
