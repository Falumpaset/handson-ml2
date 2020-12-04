package de.immomio.mailsender.propertysearcher;

import de.immomio.config.ApplicationMessageSource;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.propertysearcher.entity.customer.PropertySearcherCustomer;
import de.immomio.data.propertysearcher.entity.email.PropertySearcherEmail;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.mail.MailMessage;
import de.immomio.mail.configurator.PropertySearcherMailConfigurator;
import de.immomio.mail.sender.PropertySearcherMessageBean;
import de.immomio.mailsender.AbstractMailSenderListener;
import de.immomio.mailsender.MailSenderErrorHandler;
import de.immomio.messaging.converter.BaseJsonConverter;
import de.immomio.model.repository.core.landlord.customer.BaseLandlordCustomerRepository;
import de.immomio.model.repository.core.propertysearcher.customer.BasePropertySearcherCustomerRepository;
import de.immomio.model.repository.core.propertysearcher.email.BasePropertySearcherEmailRepository;
import de.immomio.model.repository.core.propertysearcher.user.BasePropertySearcherUserProfileRepository;
import de.immomio.model.repository.core.propertysearcher.user.BasePropertySearcherUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Johannes Hiemer, Bastian Bliemeister, Maik Kingma
 */

@Slf4j
@Component
public class PropertySearcherMailSenderListener
        extends AbstractMailSenderListener<PropertySearcherCustomer,
        PropertySearcherMessageBean,
        PropertySearcherEmailService,
        PropertySearcherMailConfigurator> {

    private final BasePropertySearcherEmailRepository emailRepository;

    private final PropertySearcherEmailService propertySearcherEmailService;

    private final BasePropertySearcherCustomerRepository customerRepository;

    private final BaseLandlordCustomerRepository baseLandlordCustomerRepository;

    private final BasePropertySearcherUserRepository userRepository;

    private final BasePropertySearcherUserProfileRepository userProfileRepository;

    private final PropertySearcherMailConfigurator propertySearcherMailConfigurator;

    @Autowired
    public PropertySearcherMailSenderListener(BasePropertySearcherEmailRepository emailRepository,
            PropertySearcherEmailService propertySearcherEmailService,
            BasePropertySearcherCustomerRepository customerRepository,
            BaseLandlordCustomerRepository baseLandlordCustomerRepository,
            BasePropertySearcherUserRepository userRepository,
            PropertySearcherMailConfigurator propertySearcherMailConfigurator,
            BaseJsonConverter baseJsonConverter,
            MailSenderErrorHandler executeErrorHandler,
            ApplicationMessageSource messageSource,
            BasePropertySearcherUserProfileRepository userProfileRepository) {
        super(baseJsonConverter, messageSource);
        this.emailRepository = emailRepository;
        this.propertySearcherEmailService = propertySearcherEmailService;
        this.customerRepository = customerRepository;
        this.baseLandlordCustomerRepository = baseLandlordCustomerRepository;
        this.userRepository = userRepository;
        this.propertySearcherMailConfigurator = propertySearcherMailConfigurator;
        this.userProfileRepository = userProfileRepository;
    }

    /* (non-Javadoc)
     * @see org.springframework.amqp.core.MessageListener#onMessage(org.springframework.amqp.core.Message)
     */
    @Override
    public void onMessage(Message message) {
        PropertySearcherMessageBean messageBean = (PropertySearcherMessageBean) baseJsonConverter.fromMessage(
                message);
        try {
            super.onMessage(messageBean);
        } catch (Exception e) {
            log.error("onMessage-Error for Email {}", messageBean.getToEmail());
            log.error("onMessage-Error ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected PropertySearcherMailConfigurator getCustomMailConfigurator() {
        return propertySearcherMailConfigurator;
    }

    protected String getToEmail(PropertySearcherMessageBean bean) {
        if (bean == null) {
            return null;
        } else if (bean.getToEmail() != null && !bean.getToEmail().isEmpty()) {
            return bean.getToEmail();
        } else if (bean.getCustomerId() != null) {
            PropertySearcherCustomer customer = getCustomer(bean.getCustomerId());
            if (customer.getInvoiceEmail() != null && !customer.getInvoiceEmail().isEmpty()) {
                return customer.getInvoiceEmail();
            }
        } else if (bean.getUserProfileId() != null) {
            Optional<PropertySearcherUserProfile> user = userProfileRepository.findById(bean.getUserProfileId());
            return user.map(PropertySearcherUserProfile::getEmail).orElse(null);
        } else if (bean.getUserId() != null) {
            Optional<PropertySearcherUser> user = userRepository.findById(bean.getUserId());
            return user.map(PropertySearcherUser::getEmail).orElse(null);
        }

        return null;
    }

    @Override
    protected PropertySearcherEmailService getEmailService() {
        return propertySearcherEmailService;
    }

    @Override
    protected void saveEmail(PropertySearcherMessageBean bean, MailMessage mailMessage) {
        if (bean.getUserId() == null) {
            PropertySearcherUser user = userRepository.findByEmail(bean.getToEmail());
            if (user != null) {
                saveEmail(bean, user);
            }
        } else {
            userRepository.findById(bean.getUserId()).ifPresentOrElse(user -> {
                        saveEmail(bean, user);
                    },
                    () -> log.error("User with ID {} not found", bean.getUserId())
            );
        }

    }

    @Override
    protected PropertySearcherCustomer getCustomer(Long id) {
        if (id != null) {
            return customerRepository.findById(id).orElse(null);
        }
        return null;
    }

    @Override
    protected LandlordCustomer getCustomizerCustomer(Long id) {
        if (id == null) {
            return null;
        }
        return baseLandlordCustomerRepository.findById(id).orElse(null);
    }

    private void saveEmail(PropertySearcherMessageBean bean, PropertySearcherUser user) {
        PropertySearcherEmail email = new PropertySearcherEmail();
        email.setUser(user);

        PropertySearcherCustomer customer = getCustomer(bean.getCustomerId());
        if (customer != null) {
            email.setCustomer(customer);
        } else if (user != null) {
            email.setCustomer(user.getCustomer());
        }

        email.setTemplate(bean.getTemplate());

        try {
            emailRepository.save(email);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
