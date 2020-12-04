package de.immomio.controller;

import com.neovisionaries.i18n.LocaleCode;
import de.immomio.config.ApplicationMessageSource;
import de.immomio.data.landlord.bean.messagesource.MessageSourceBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.messagesource.MessageSource;
import de.immomio.model.repository.landlord.customer.LandlordCustomerRepository;
import de.immomio.model.repository.landlord.customer.messagesource.LandlordMessageSourceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * @author Johannes Hiemer.
 * @author Maik Baumbach
 */
@Slf4j
@Controller
@RequestMapping(value = "/messageSources")
public class MessageSourceController {

    private LandlordMessageSourceRepository messageSourceRepository;

    private LandlordCustomerRepository customerRepository;

    private ApplicationMessageSource messageSource;

    @Autowired
    public MessageSourceController(
            LandlordMessageSourceRepository messageSourceRepository,
            LandlordCustomerRepository customerRepository,
            ApplicationMessageSource messageSource
    ) {
        this.messageSourceRepository = messageSourceRepository;
        this.customerRepository = customerRepository;
        this.messageSource = messageSource;
    }

    @RequestMapping(value = {"/search/findByLanguageKeyAndMessageKeyAndCustomerId"}, method = RequestMethod.GET)
    public ResponseEntity<EntityModel<Object>> findByLanguageKeyAndMessageKeyAndCustomer(
            @RequestParam(value = "messageKey") String messageKey,
            @RequestParam(value = "customerId") long customerId,
            @RequestParam(value = "locale") LocaleCode locale
    ) {

        LandlordCustomer customer = customerRepository.findById(customerId).get();
        de.immomio.data.landlord.entity.messagesource.MessageSource message = messageSourceRepository.
                findByLanguageKeyAndMessageKeyAndCustomer(
                locale, messageKey, customer);

        EntityModel<Object> messageResource;
        if (message == null) {
            String value = messageSource.getMessage(messageKey, null, messageKey, locale.getLanguage().toLocale());
            message = new MessageSource(customer, messageKey, locale, value);

            MessageSourceBean messageSourceBean = new MessageSourceBean(message);
            messageResource = new EntityModel<>(messageSourceBean);
            WebMvcLinkBuilder selfLink = linkTo(WebMvcLinkBuilder.methodOn(MessageSourceController.class)
                    .findByLanguageKeyAndMessageKeyAndCustomer(messageKey, customerId, locale));
            messageResource.add(selfLink.withRel("self"));
            messageResource.add(selfLink.withRel("messageSource"));
        } else {
            messageResource = new EntityModel<>(new MessageSourceBean(message));
            WebMvcLinkBuilder selfLink = linkTo(MessageSourceController.class).slash(message.getId());
            messageResource.add(selfLink.withSelfRel());
            messageResource.add(selfLink.withRel("messageSource"));
        }

        return new ResponseEntity<>(messageResource, HttpStatus.OK);
    }

}
