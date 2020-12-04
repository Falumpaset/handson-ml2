package de.immomio.controller.conversation;

import de.immomio.beans.shared.conversation.ConversationMessageCreateBean;
import de.immomio.beans.shared.conversation.ConversationMessageCreateBulkBean;
import de.immomio.data.base.bean.MessengerFilterBean;
import de.immomio.data.base.type.conversation.ConversationMessageSender;
import de.immomio.data.landlord.bean.user.AgentInfo;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.shared.entity.conversation.Conversation;
import de.immomio.data.shared.entity.conversation.ConversationBean;
import de.immomio.data.shared.entity.conversation.ConversationMessageBean;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.landlord.service.conversation.LandlordConversationService;
import de.immomio.landlord.service.security.UserSecurityService;
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

    private final LandlordConversationService conversationService;

    private final UserSecurityService userSecurityService;

    private final PagedResourcesAssembler pagedResourcesAssembler;

    @Autowired
    public ConversationController(
            LandlordConversationService conversationService,
            UserSecurityService userSecurityService,
            PagedResourcesAssembler pagedResourcesAssembler
    ) {
        this.conversationService = conversationService;
        this.userSecurityService = userSecurityService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "application", schema = @Schema(type = "Long"), required = true)})
    @PreAuthorize("#application.property.customer.id == principal.customer.id")
    @PostMapping(value = "/send")
    public ResponseEntity<ConversationMessageBean> send(@RequestBody ConversationMessageCreateBean messageBean,
                               @RequestParam("application") PropertyApplication application) {
        ConversationMessageBean message = conversationService.createMessage(
                messageBean,
                application,
                ConversationMessageSender.LANDLORD,
                new AgentInfo(userSecurityService.getPrincipalUser())
        );

        return ResponseEntity.ok(message);
    }

    @PostMapping(value = "/bulk/send")
    public ResponseEntity<Void> send(@RequestBody ConversationMessageCreateBulkBean messageBean) {
        conversationService.createMessages(messageBean, userSecurityService.getPrincipalUser());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/search")
    public ResponseEntity<Page<ConversationBean>> search(@RequestBody MessengerFilterBean filterBean) {
        List<ConversationBean> conversations = conversationService.search(filterBean);
        return new ResponseEntity(pagedResourcesAssembler.toModel(new PageImpl(conversations)), HttpStatus.OK);
    }

    @GetMapping("/search/findByProperty")
    @PreAuthorize("#property.customer.id == principal?.customer?.id")
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "property", schema = @Schema(type = "Long"), required = true)})
    public ResponseEntity<Page<ConversationBean>> findByProperty(
            @RequestParam(value = "property") Property property
    ) {
        List<ConversationBean> conversations = conversationService.findByProperty(property);
        return new ResponseEntity(pagedResourcesAssembler.toModel(new PageImpl(conversations)), HttpStatus.OK);
    }

    @PostMapping("/block")
    @PreAuthorize("#conversation.application.property.customer.id == principal.customer.id")
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "conversation", schema = @Schema(type = "Long"), required = true)})
    public ResponseEntity search(
            @RequestParam(value = "conversation") Conversation conversation,
            @RequestParam(value = "blocked") boolean blocked
    ) {
        conversationService.block(conversation, blocked);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/participatedAgents")
    @PreAuthorize("#conversation.application.property.customer.id == principal.customer.id")
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "conversation", schema = @Schema(type = "Long"), required = true)})
    public ResponseEntity getParticipatedAgents(
            @RequestParam(value = "conversation") Conversation conversation
    ) {
        List<AgentInfo> participatedAgents = conversationService.getParticipatedAgents(conversation);
        return ResponseEntity.ok(participatedAgents);
    }
}
