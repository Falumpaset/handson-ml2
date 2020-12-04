package de.immomio.controller.conversation;

import de.immomio.beans.shared.conversation.ConversationMessageCreateBean;
import de.immomio.data.base.type.conversation.ConversationMessageSender;
import de.immomio.data.landlord.bean.user.AgentInfo;
import de.immomio.data.shared.entity.conversation.Conversation;
import de.immomio.data.shared.entity.conversation.ConversationBean;
import de.immomio.data.shared.entity.conversation.ConversationMessageBean;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.service.conversation.ConversationConfigService;
import de.immomio.service.conversation.PropertysearcherConversationService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Freddy Sawma
 */

@RepositoryRestController
@RequestMapping(value = "/conversations")
public class ConversationController {
    private static final String USER_NOT_ALLOWED_TO_SEND_MESSAGE = "USER_NOT_ALLOWED_TO_SEND_MESSAGE_L";

    private final PropertysearcherConversationService conversationService;

    private ConversationConfigService conversationConfigService;

    private final PagedResourcesAssembler pagedResourcesAssembler;

    @Autowired
    public ConversationController(
            PropertysearcherConversationService conversationService,
            ConversationConfigService conversationConfigService,
            PagedResourcesAssembler pagedResourcesAssembler
    ) {
        this.conversationService = conversationService;
        this.conversationConfigService = conversationConfigService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @PostMapping(value = "/send")
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "application", schema = @Schema(type = "Long"), required = true)})
    @PreAuthorize("#application.userProfile.user.id == principal?.id")
    public ResponseEntity send(@RequestBody ConversationMessageCreateBean messageBean,
                               @RequestParam("application") PropertyApplication application
    ) {
        if (!conversationConfigService.isAllowedToSendMessage(application.getId())) {
            return ResponseEntity.badRequest().body(USER_NOT_ALLOWED_TO_SEND_MESSAGE);
        }

        conversationService.validateBlockedConversationFromApplication(application.getId());

        ConversationMessageBean message = conversationService.createMessage(
                messageBean,
                application,
                ConversationMessageSender.PROPERTYSEARCHER
        );

        return ResponseEntity.ok(message);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ConversationBean>> search(
            @RequestParam(value = "search", required = false) String search
    ) {
        List<ConversationBean> conversations = conversationService.search(search);
        return new ResponseEntity(pagedResourcesAssembler.toModel(new PageImpl(conversations)), HttpStatus.OK);
    }

    @GetMapping("/participatedAgents")
    @PreAuthorize("#conversation.application.userProfile.user.id == principal.id")
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "conversation", schema = @Schema(type = "Long"), required = true)})
    public ResponseEntity getParticipatedAgents(
            @RequestParam(value = "conversation") Conversation conversation
    ) {
        List<AgentInfo> participatedAgents = conversationService.getParticipatedAgents(conversation);
        return ResponseEntity.ok(participatedAgents);
    }
}
