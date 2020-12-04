package de.immomio.service.product;

import de.immomio.data.landlord.bean.customer.LandlordCustomerBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.price.LandlordPrice;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUserBean;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.landlord.service.product.LandlordCheckoutNotificationService;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.LandlordMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.service.AbstractTest;
import org.hamcrest.collection.IsMapContaining;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static utils.TestHelper.generateLandlordCustomer;
import static utils.TestHelper.generateLandlordPrice;
import static utils.TestHelper.generateLandlordUser;

/**
 * @author Niklas Lindemann
 */

public class LandlordCheckoutNotificationServiceTest extends AbstractTest {

    @Mock
    private LandlordMailSender landlordMailSender;

    @InjectMocks
    @Spy
    private LandlordCheckoutNotificationService checkoutNotificationService;

    @Test
    public void homepageModuleAdded() {
        LandlordCustomer landlordCustomer = generateLandlordCustomer(1L);
        LandlordUser landlordUser = generateLandlordUser(landlordCustomer, LandlordUsertype.COMPANYADMIN, 1L);
        LandlordPrice price = generateLandlordPrice(32);

        doAnswer(invocation ->  {
            Map<String, Object> provided = (HashMap<String, Object>) invocation.getArguments()[5];
            assertThat(provided.size(), is(3));
            assertThat(provided, IsMapContaining.hasEntry(
                    ModelParams.MODEL_CUSTOMER, new LandlordCustomerBean(landlordCustomer, landlordUser.fullName())));
            assertThat(provided, IsMapContaining.hasEntry(ModelParams.MODEL_USER, new LandlordUserBean(landlordUser)));
            assertThat(provided, IsMapContaining.hasEntry(ModelParams.MODEL_SETUP_PRICE, price.getSetup()));
            return null;
        }).when(landlordMailSender).send(
                any(),
                eq(landlordUser),
                eq(landlordCustomer),
                eq(MailTemplate.COMMERCIAL_ADD_HOMEPAGE_MODULE),
                anyString(),
                anyMap());

        checkoutNotificationService.homepageModuleAdded(landlordCustomer, landlordUser, price);

        verify(landlordMailSender, times(1)).send(
                any(),
                eq(landlordUser),
                eq(landlordCustomer),
                eq(MailTemplate.COMMERCIAL_ADD_HOMEPAGE_MODULE),
                anyString(),
                anyMap());
    }

    @Test
    public void homepageModuleRemoved() {
        LandlordCustomer landlordCustomer = generateLandlordCustomer(1L);
        LandlordUser landlordUser = generateLandlordUser(landlordCustomer, LandlordUsertype.COMPANYADMIN, 1L);

        doAnswer(invocation ->  {
            Map<String, Object> provided = (HashMap<String, Object>) invocation.getArguments()[6];
            assertThat(provided.size(), is(2));
            assertThat(provided, IsMapContaining.hasEntry(
                    ModelParams.MODEL_CUSTOMER, new LandlordCustomerBean(landlordCustomer, landlordUser.fullName())));
            assertThat(provided, IsMapContaining.hasEntry(ModelParams.MODEL_USER, new LandlordUserBean(landlordUser)));
            return null;
        }).when(landlordMailSender).send(
                any(),
                eq(landlordUser),
                eq(landlordCustomer),
                eq(MailTemplate.COMMERCIAL_REMOVE_HOMEPAGE_MODULE),
                anyString(),
                any(Object[].class),
                anyMap());

        checkoutNotificationService.homepageModuleRemoved(landlordCustomer, landlordUser);

        verify(landlordMailSender, times(1)).send(
                any(),
                eq(landlordUser),
                eq(landlordCustomer),
                eq(MailTemplate.COMMERCIAL_REMOVE_HOMEPAGE_MODULE),
                anyString(),
                any(Object[].class),
                anyMap());

    }
}