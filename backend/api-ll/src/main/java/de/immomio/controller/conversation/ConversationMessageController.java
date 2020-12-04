package de.immomio.controller.conversation;

import de.immomio.beans.shared.CommonCountBean;
import de.immomio.data.shared.entity.conversation.Conversation;
import de.immomio.data.shared.entity.conversation.ConversationMessageBean;
import de.immomio.landlord.service.conversation.LandlordConversationMessageService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@RepositoryRestController
@RequestMapping(value = "/conversationMessages")
public class ConversationMessageController {

    private final LandlordConversationMessageService conversationMessageService;

    private final PagedResourcesAssembler pagedResourcesAssembler;

    @Autowired
    public ConversationMessageController(
            LandlordConversationMessageService conversationMessageService,
            PagedResourcesAssembler pagedResourcesAssembler
    ) {
        this.conversationMessageService = conversationMessageService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("search/findByConversation")
    @PreAuthorize("#conversation.application.property.customer.id == principal.customer.id")
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "conversation", schema = @Schema(type = "Long"), required = true)})
    public ResponseEntity findByConversation(@RequestParam("conversation") Conversation conversation, Pageable pageable) {
        Page<ConversationMessageBean> conversationMessages = conversationMessageService.findByConversation(conversation, pageable);
        return new ResponseEntity(pagedResourcesAssembler.toModel(conversationMessages), HttpStatus.OK);
    }

    @GetMapping("search/findUnreadMessages")
    @PreAuthorize("#conversation.application.property.customer.id == principal.customer.id")
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "conversation", schema = @Schema(type = "Long"), required = true)})
    public ResponseEntity getUnreadMessages(@RequestParam("conversation") Conversation conversation) {
        List<ConversationMessageBean> conversationMessages = conversationMessageService.getUnreadMessages(conversation);
        return new ResponseEntity(pagedResourcesAssembler.toModel(new PageImpl(conversationMessages)), HttpStatus.OK);
    }

    @GetMapping("countUnread")
    public ResponseEntity countUnread(@RequestParam(value = "agents", required = false) List<String> agentIds) {
        Long unread = conversationMessageService.countUnread(agentIds);
        return ResponseEntity.ok(new CommonCountBean(unread));
    }

}
