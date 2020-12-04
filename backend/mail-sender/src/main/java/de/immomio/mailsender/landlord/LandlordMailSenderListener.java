package de.immomio.mailsender.landlord;

import de.immomio.config.ApplicationMessageSource;
import de.immomio.data.base.entity.customer.user.AbstractUser;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.email.LandlordEmail;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.mail.MailMessage;
import de.immomio.mail.configurator.LandlordMailConfigurator;
import de.immomio.mail.sender.LandlordMessageBean;
import de.immomio.mailsender.AbstractMailSenderListener;
import de.immomio.mailsender.MailSenderErrorHandler;
import de.immomio.messaging.converter.BaseJsonConverter;
import de.immomio.model.repository.core.landlord.customer.BaseLandlordCustomerRepository;
import de.immomio.model.repository.core.landlord.customer.user.BaseLandlordUserRepository;
import de.immomio.model.repository.core.landlord.email.BaseLandlordEmailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 */

@Slf4j
@Component
public class LandlordMailSenderListener extends AbstractMailSenderListener<LandlordCustomer,
        LandlordMessageBean,
        LandlordEmailService,
        LandlordMailConfigurator> {

    private final BaseLandlordEmailRepository emailRepository;

    private final LandlordEmailService landlordEmailService;

    private final BaseLandlordCustomerRepository customerRepository;

    private final BaseLandlordUserRepository userRepository;

    private final LandlordMailConfigurator landlordMailConfigurator;

    @Autowired
    public LandlordMailSenderListener(
            BaseLandlordEmailRepository emailRepository,
            LandlordEmailService landlordEmailService,
            BaseLandlordCustomerRepository customerRepository,
            BaseLandlordUserRepository userRepository,
            LandlordMailConfigurator landlordMailConfigurator,
            BaseJsonConverter baseJsonConverter,
            MailSenderErrorHandler executeErrorHandler,
            ApplicationMessageSource messageSource
    ) {
        super(baseJsonConverter, messageSource);
        this.emailRepository = emailRepository;
        this.landlordEmailService = landlordEmailService;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.landlordMailConfigurator = landlordMailConfigurator;
    }

    /* (non-Javadoc)
     * @see org.springframework.amqp.core.MessageListener#onMessage(org.springframework.amqp.core.Message)
     */
    @Override
    public void onMessage(Message message) {
        LandlordMessageBean messageBean = (LandlordMessageBean) baseJsonConverter.fromMessage(message);

        try {
            super.onMessage(messageBean);
        } catch (Exception e) {
            log.error("onMessage-Error for Email {}", messageBean.getToEmail());
            log.error("onMessage-Error ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected LandlordMailConfigurator getCustomMailConfigurator() {
        return landlordMailConfigurator;
    }

    protected String getToEmail(LandlordMessageBean bean) {
        if (bean == null) {
            return null;
        } else if (bean.getToEmail() != null && !bean.getToEmail().isEmpty()) {
            return bean.getToEmail();
        } else if (bean.getCustomerId() != null) {
            LandlordCustomer customer = getCustomer(bean.getCustomerId());
            if (customer.getInvoiceEmail() != null && !customer.getInvoiceEmail().isEmpty()) {
                return customer.getInvoiceEmail();
            }
        } else if (bean.getUserId() != null) {
            Optional<LandlordUser> user = userRepository.findById(bean.getUserId());
            return user.map(AbstractUser::getEmail).orElse(null);
        }

        return null;
    }

    @Override
    protected LandlordEmailService getEmailService() {
        return landlordEmailService;
    }

    @Override
    protected void saveEmail(LandlordMessageBean bean, MailMessage mailMessage) {
        LandlordEmail email = new LandlordEmail();

        if (bean.getUserId() != null) {

            userRepository.findById(bean.getUserId()).ifPresentOrElse(user -> {
                        email.setUser(user);

                        LandlordCustomer customer = getCustomer(bean.getCustomerId());
                        if (customer != null) {
                            email.setCustomer(customer);
                        } else if (user != null) {
                            email.setCustomer(user.getCustomer());
                        }

                        email.setTemplate(bean.getTemplate());

                        emailRepository.save(email);
                    },
                    () -> log.error("User with ID {} not found", bean.getUserId())
            );
        }
    }

    @Override
    protected LandlordCustomer getCustomer(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    protected LandlordCustomer getCustomizerCustomer(Long id) {
        return getCustomer(id);
    }
}
