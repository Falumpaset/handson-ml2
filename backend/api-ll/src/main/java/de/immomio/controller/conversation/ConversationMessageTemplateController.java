package de.immomio.controller.conversation;

import de.immomio.beans.IdBean;
import de.immomio.data.landlord.bean.conversationTemplate.ConversationMessageTemplateBean;
import de.immomio.data.landlord.bean.conversationTemplate.ConversationMessageTemplateCreateBean;
import de.immomio.data.landlord.entity.conversation.ConversationMessageTemplate;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.landlord.service.conversation.LandlordConversationMessageTemplateService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
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
 * @author Niklas Lindemann
 */
@RepositoryRestController
@RequestMapping(value = "/conversationMessageTemplates")
public class ConversationMessageTemplateController {

    private final LandlordConversationMessageTemplateService templateService;

    @Autowired
    public ConversationMessageTemplateController(LandlordConversationMessageTemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping("/search")
    public ResponseEntity<PagedModel<EntityModel<ConversationMessageTemplateBean>>> findAll(
            Pageable pageable,
            @Parameter(hidden = true) PagedResourcesAssembler<ConversationMessageTemplateBean> pagedResourcesAssembler
    ) {
        Page<ConversationMessageTemplateBean> messageTemplateBeans = templateService.search(pageable);
        PagedModel<EntityModel<ConversationMessageTemplateBean>> body = pagedResourcesAssembler.toModel(messageTemplateBeans);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/search/substituted")
    @PreAuthorize("#application.property.customer.id == principal.customer.id")
    public ResponseEntity<PagedModel<EntityModel<ConversationMessageTemplateBean>>> findSubstituted(
            @RequestParam("application") PropertyApplication application,
            Pageable pageable,
            @Parameter(hidden = true) PagedResourcesAssembler<ConversationMessageTemplateBean> pagedResourcesAssembler
    ) {
        Page<ConversationMessageTemplateBean> messageTemplateBeans = templateService.getSubstitutedTemplates(pageable, application);
        PagedModel<EntityModel<ConversationMessageTemplateBean>> body = pagedResourcesAssembler.toModel(messageTemplateBeans);
        return ResponseEntity.ok(body);
    }

    @PostMapping("/create")
    public ResponseEntity<IdBean> create(@RequestBody ConversationMessageTemplateCreateBean templateBean) {
        ConversationMessageTemplate template = templateService.create(templateBean);
        return ResponseEntity.ok(new IdBean(template.getId()));
    }

    @PostMapping("/update/{template}")
    @PreAuthorize("#template.customer.id == principal.customer.id")
    public ResponseEntity<?> update(
            @PathVariable("template") ConversationMessageTemplate template,
            @RequestBody ConversationMessageTemplateCreateBean templateBean
    ) {
        templateService.update(template, templateBean);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{template}")
    @PreAuthorize("#template.customer.id == principal.customer.id")
    public ResponseEntity<?> delete(@PathVariable("template") ConversationMessageTemplate template) {
        templateService.delete(template);
        return ResponseEntity.ok().build();
    }
}
