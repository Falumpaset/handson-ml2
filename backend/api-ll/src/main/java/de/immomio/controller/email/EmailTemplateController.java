package de.immomio.controller.email;

import de.immomio.beans.landlord.email.TemplateRequestBean;
import de.immomio.beans.landlord.email.TemplateSendRequestBean;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.landlord.service.email.EmailTemplateService;
import de.immomio.landlord.service.user.LandlordUserService;
import de.immomio.mail.sender.LandlordMailSender;
import de.immomio.mail.sender.templates.MailGroup;
import de.immomio.mail.sender.templates.MailTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 */

@Slf4j
@Controller
@RequestMapping(path = "/email")
public class EmailTemplateController {

    private final LandlordMailSender landlordMailSender;

    private final EmailTemplateService emailTemplateService;

    private final LandlordUserService userService;

    @Autowired
    public EmailTemplateController(LandlordMailSender landlordMailSender, EmailTemplateService emailTemplateService, LandlordUserService userService) {
        this.landlordMailSender = landlordMailSender;
        this.emailTemplateService = emailTemplateService;
        this.userService = userService;
    }

    @PostMapping(path = "/templates/{template}/send")
    public ResponseEntity<EntityModel<Object>> send(@PathVariable(name = "template") MailTemplate template, @RequestBody TemplateSendRequestBean requestBean) {
        Optional<LandlordUser> optionalLandlordUser = userService.findById(requestBean.getUserId());

        if (optionalLandlordUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        landlordMailSender.send(optionalLandlordUser.get(), template, requestBean.getSubject(), new HashMap<>());

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/templates")
    public ResponseEntity<Object> templates() {
        List<MailTemplate> resolveTemplates = MailTemplate.getConfigurableTemplates();

        return new ResponseEntity<>(resolveTemplates, HttpStatus.OK);
    }

    @GetMapping(value = "/templates/{group}")
    public ResponseEntity<Object> templatesByGroup(@PathVariable("group") MailGroup mailGroup) {
        List<MailTemplate> resolveTemplates = MailTemplate.getConfigurableTemplates()
                .stream()
                .filter(mailTemplate -> mailGroup == mailTemplate.getGroup())
                .collect(Collectors.toList());

        return new ResponseEntity<>(resolveTemplates, HttpStatus.OK);
    }

    @PostMapping(value = "/templates/{template}")
    public ResponseEntity<Object> template(@PathVariable(name = "template") MailTemplate template, @RequestBody TemplateRequestBean requestBean) {
        return ResponseEntity.ok(new EntityModel<>(emailTemplateService.template(template, requestBean)));
    }
}
