package de.immomio.controller.customQuestion;

import de.immomio.constants.exceptions.ResponseValidationException;
import de.immomio.data.landlord.entity.customquestion.CustomQuestion;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.bean.customQuestion.CustomQuestionResponseCreateBean;
import de.immomio.data.shared.bean.customQuestion.CustomQuestionResponseQuestionBean;
import de.immomio.data.shared.entity.common.customQuestion.CustomQuestionResponse;
import de.immomio.service.customQuestion.CustomQuestionResponseService;
import de.immomio.service.customQuestion.PropertySearcherCustomQuestionService;
import de.immomio.service.security.UserSecurityService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Maik Kingma
 */

@Slf4j
@RepositoryRestController
@RequestMapping(value = "/customQuestionResponses")
public class CustomQuestionResponseController {

    private static final String QUESTION_NOT_FOUND = "QUESTION_NOT_FOUND_L";
    private static final String USER_HAS_ALREADY_ANSWERED_QUESTION = "USER_HAS_ALREADY_ANSWERED_QUESTION_L";
    private static final String UQ_USER_CUSTOM_QUESTION = "uq_user_custom_question";

    private final UserSecurityService userSecurityService;

    private final PropertySearcherCustomQuestionService customQuestionService;

    private final CustomQuestionResponseService customQuestionResponseService;

    @Autowired
    public CustomQuestionResponseController(
            UserSecurityService userSecurityService,
            PropertySearcherCustomQuestionService customQuestionService,
            CustomQuestionResponseService customQuestionResponseService) {
        this.userSecurityService = userSecurityService;
        this.customQuestionService = customQuestionService;
        this.customQuestionResponseService = customQuestionResponseService;
    }

    @PostMapping(value = "/respond")
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "question", schema = @Schema(type = "Long"), required = true)})
    public ResponseEntity<Object> create(
            @RequestParam("question") CustomQuestion question,
            @RequestBody @Valid CustomQuestionResponseCreateBean response,
            @Parameter(hidden = true)  PersistentEntityResourceAssembler assembler
    ) {
        if (question == null) {
            return ResponseEntity.badRequest().body(QUESTION_NOT_FOUND);
        }
        try {
            PropertySearcherUserProfile userProfile = userSecurityService.getPrincipalUserProfile();
            CustomQuestionResponse customQuestionResponse = customQuestionService.respond(response, userProfile, question, true);

            return ResponseEntity.accepted().body(assembler.toModel(customQuestionResponse));
        } catch (ResponseValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains(UQ_USER_CUSTOM_QUESTION)) {
                return ResponseEntity.badRequest().body(USER_HAS_ALREADY_ANSWERED_QUESTION);
            } else {
                log.error(e.getMessage(), e);

                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @GetMapping("")
    public ResponseEntity<List<CustomQuestionResponseQuestionBean>> findForUser() {
        return ResponseEntity.ok(customQuestionResponseService.getCustomQuestionResponses(userSecurityService.getPrincipalUserProfile()));
    }

}
