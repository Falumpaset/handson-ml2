package de.immomio.landlord.service.conversation;

import de.immomio.data.landlord.bean.conversationTemplate.ConversationMessageTemplateBean;
import de.immomio.data.landlord.bean.conversationTemplate.ConversationMessageTemplateCreateBean;
import de.immomio.data.landlord.entity.conversation.ConversationMessageTemplate;
import de.immomio.data.landlord.entity.user.LandlordUserBean;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileEmailBean;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.model.repository.landlord.conversation.ConversationMessageTemplateRepository;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */
@Service
public class LandlordConversationMessageTemplateService {

    private final ConversationMessageTemplateRepository messageTemplateRepository;

    private final UserSecurityService userSecurityService;

    public LandlordConversationMessageTemplateService(ConversationMessageTemplateRepository messageTemplateRepository, UserSecurityService userSecurityService) {
        this.messageTemplateRepository = messageTemplateRepository;
        this.userSecurityService = userSecurityService;
    }

    public Page<ConversationMessageTemplateBean> search(Pageable pageable) {
        Page<ConversationMessageTemplate> messageTemplates = findForCustomer(pageable);
        List<ConversationMessageTemplateBean> messageTemplateBeans = messageTemplates.stream()
                .map(this::getTemplateBean)
                .collect(Collectors.toList());

        return new PageImpl<>(messageTemplateBeans, pageable, messageTemplates.getTotalElements());
    }

    public Page<ConversationMessageTemplateBean> getSubstitutedTemplates(Pageable pageable, PropertyApplication conversation) {
        Page<ConversationMessageTemplate> messageTemplates = findForCustomer(pageable);
        List<ConversationMessageTemplateBean> messageTemplateBeans = messageTemplates.stream()
                .map(template -> getSubstitutedBean(conversation, template))
                .collect(Collectors.toList());


        return new PageImpl<>(messageTemplateBeans, pageable, messageTemplates.getTotalElements());
    }

    public ConversationMessageTemplate create(ConversationMessageTemplateCreateBean templateBean) {
        ConversationMessageTemplate template = new ConversationMessageTemplate();
        template.setCustomer(userSecurityService.getPrincipalUser().getCustomer());
        template.setAttachments(templateBean.getAttachments());
        template.setContent(templateBean.getContent());
        template.setTitle(templateBean.getTitle());
        return messageTemplateRepository.save(template);
    }

    public ConversationMessageTemplate update(ConversationMessageTemplate template, ConversationMessageTemplateCreateBean templateBean) {
        template.setAttachments(templateBean.getAttachments());
        template.setContent(templateBean.getContent());
        template.setTitle(templateBean.getTitle());
        return messageTemplateRepository.save(template);
    }

    public void delete(ConversationMessageTemplate template) {
        messageTemplateRepository.delete(template);
    }

    private ConversationMessageTemplateBean getSubstitutedBean(PropertyApplication application, ConversationMessageTemplate template) {
        ConversationMessageTemplateBean templateListBean = new ConversationMessageTemplateBean();
        templateListBean.setId(template.getId());
        templateListBean.setAttachments(template.getAttachments());
        templateListBean.setContent(substituteTemplate(template.getContent(), application));
        templateListBean.setTitle(template.getTitle());
        return templateListBean;
    }

    private ConversationMessageTemplateBean getTemplateBean(ConversationMessageTemplate template) {
        ConversationMessageTemplateBean templateListBean = new ConversationMessageTemplateBean();
        templateListBean.setId(template.getId());
        templateListBean.setAttachments(template.getAttachments());
        templateListBean.setContent(template.getContent());
        templateListBean.setTitle(template.getTitle());
        return templateListBean;
    }

    private Page<ConversationMessageTemplate> findForCustomer(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        return messageTemplateRepository.findAllForCustomer(pageRequest);
    }

    private String substituteTemplate(String content, PropertyApplication application) {
        Map<String, Object> data = new HashMap<>();
        data.put("applicant", new PropertySearcherUserProfileEmailBean(application.getUserProfile()));
        data.put("property", application.getProperty().getData());
        data.put("user", new LandlordUserBean(userSecurityService.getPrincipalUser()));
        VelocityContext context = new VelocityContext(data);
        StringWriter stringWriter = new StringWriter();

        Velocity.evaluate(context, stringWriter, "content", content);
        return stringWriter.toString();
    }
}
