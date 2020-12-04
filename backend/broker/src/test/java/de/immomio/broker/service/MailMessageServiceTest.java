package de.immomio.broker.service;

import de.immomio.broker.AbstractTest;
import de.immomio.broker.utils.TestHelper;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.mail.configurator.PropertySearcherMailConfigurator;
import de.immomio.mail.sender.LandlordMailSender;
import de.immomio.mail.sender.PropertySearcherMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.messaging.container.mail.LandlordMailBrokerContainer;
import de.immomio.messaging.container.mail.PropertySearcherProfileMailBrokerContainer;
import de.immomio.model.repository.core.landlord.customer.property.BasePropertyRepository;
import de.immomio.model.repository.core.landlord.customer.user.BaseLandlordUserRepository;
import de.immomio.model.repository.core.propertysearcher.user.BasePropertySearcherUserProfileRepository;
import de.immomio.model.repository.core.propertysearcher.user.BasePropertySearcherUserRepository;
import de.immomio.service.report.PropertyReportService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.Map;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MailMessageServiceTest extends AbstractTest {

    @Mock
    private LandlordMailSender landlordMailSender;

    @Mock
    private PropertySearcherMailSender propertySearcherMailSender;

    @Mock
    private BaseLandlordUserRepository baseLandlordUserRepository;

    @Mock
    private BasePropertySearcherUserProfileRepository basePropertySearcherUserProfileRepository;

    @Mock
    private BasePropertyRepository basePropertyRepository;

    @Mock
    private  PropertyReportService propertyReportService;

    @Mock
    private PropertySearcherMailConfigurator mailConfigurator;

    @InjectMocks
    @Spy
    private MailMessageService mailMessageService;

    @Test
    public void onMessageWhenUserIdIsNull() {
        LandlordMailBrokerContainer container = TestHelper.createLandlordMailBrokerContainer();
        container.setUserId(null);
        container.setTemplate(MailTemplate.NEW_EMAIL);
        mailMessageService.onMessage(container);

        verify(landlordMailSender, times(1)).send(nullable(String.class), any(), nullable(String.class), (Object[]) nullable(Object.class), nullable(Map.class), nullable(Map.class));
    }

    @Test
    public void onMessageWhenUserIdNotNull() {
        LandlordMailBrokerContainer container = TestHelper.createLandlordMailBrokerContainer();
        container.setTemplate(MailTemplate.NEW_EMAIL);
        when(baseLandlordUserRepository.findById(anyLong())).thenReturn(Optional.of(TestHelper.generateLandlordUser()));
        mailMessageService.onMessage(container);

        verify(baseLandlordUserRepository, times(1)).findById(anyLong());
        verify(landlordMailSender, times(1)).send(any(LandlordUser.class), any(), nullable(String.class), (Object[]) nullable(Object.class), nullable(Map.class), nullable(Map.class));
    }

    @Test
    public void onMessageWhenUserIdIsNullAndCustomerIdIsNull() {
        PropertySearcherProfileMailBrokerContainer container = TestHelper.createPropertySearcherMailBrokerContainer();
        container.setUserProfileId(null);
        container.setCustomerId(null);
        container.setTemplate(MailTemplate.NEW_EMAIL);

        mailMessageService.onMessage(container);

        verify(baseLandlordUserRepository, never()).findById(anyLong());
        verify(propertySearcherMailSender, never()).send(any(PropertySearcherUserProfile.class), any(), anyString(),
                any(), anyMap(), anyLong(), anyMap());
        verify(propertySearcherMailSender, never()).send(any(PropertySearcherUserProfile.class), any(), anyString(),
                any(), anyMap(), anyMap());
        verify(propertySearcherMailSender, never()).send(anyString(), any(), anyString(),
                any(), anyMap(), anyLong(), anyMap());
        verify(propertySearcherMailSender, times(1)).send(anyString(), any(), anyString(),
                any(), anyMap(), anyMap());
    }

    @Test
    public void onMessageWhenUserIdNotNullAndCustomerNotNullAndUserNotNull() {
        PropertySearcherProfileMailBrokerContainer container = TestHelper.createPropertySearcherMailBrokerContainer();
        when(basePropertySearcherUserProfileRepository.findById(anyLong())).thenReturn(Optional.of(TestHelper.generatePropertySearcherUser()));
        mailMessageService.onMessage(container);

        verify(basePropertySearcherUserProfileRepository, times(1)).findById(anyLong());
        verify(propertySearcherMailSender, times(1)).send(any(PropertySearcherUserProfile.class), any(), anyString(),
                any(), anyMap(), anyLong(), anyMap());
        verify(propertySearcherMailSender, never()).send(any(PropertySearcherUserProfile.class), any(), anyString(),
                any(), anyMap(),  anyMap());
        verify(propertySearcherMailSender, never()).send(anyString(), any(), anyString(),
                any(), anyMap(), anyLong(), anyMap());
        verify(propertySearcherMailSender, never()).send(anyString(), any(), anyString(),
                any(), anyMap(), anyMap());
    }

    @Test
    public void onMessageWhenUserIdNotNullAndUserNotNull() {
        PropertySearcherProfileMailBrokerContainer container = TestHelper.createPropertySearcherMailBrokerContainer();
        container.setCustomerId(null);

        when(basePropertySearcherUserProfileRepository.findById(anyLong())).thenReturn(Optional.of(TestHelper.generatePropertySearcherUser()));
        mailMessageService.onMessage(container);

        verify(basePropertySearcherUserProfileRepository, times(1)).findById(anyLong());
        verify(propertySearcherMailSender, never()).send(any(PropertySearcherUserProfile.class), any(), anyString(),
                any(), anyMap(), anyLong(), anyMap());
        verify(propertySearcherMailSender, times(1)).send(any(PropertySearcherUserProfile.class), any(), anyString(),
                any(), anyMap(), anyMap());
        verify(propertySearcherMailSender, never()).send(anyString(), any(), anyString(),
                any(), anyMap(), anyLong(), anyMap());
        verify(propertySearcherMailSender, never()).send(anyString(), any(), anyString(),
                any(), anyMap(), anyMap());
    }

    @Test
    public void onMessageWhenUserIdNotNullAndCustomerIdNotNull() {
        PropertySearcherProfileMailBrokerContainer container = TestHelper.createPropertySearcherMailBrokerContainer();

        when(basePropertySearcherUserProfileRepository.findById(anyLong())).thenReturn(Optional.empty());
        mailMessageService.onMessage(container);

        verify(basePropertySearcherUserProfileRepository, times(1)).findById(anyLong());
        verify(propertySearcherMailSender, never()).send(any(PropertySearcherUserProfile.class), any(), anyString(),
                any(), anyMap(), anyLong(), anyMap());
        verify(propertySearcherMailSender, never()).send(any(PropertySearcherUserProfile.class), any(), anyString(),
                any(), anyMap(), anyMap());
        verify(propertySearcherMailSender, times(1)).send(anyString(), any(), anyString(),
                any(), anyMap(), anyLong(), anyMap());
        verify(propertySearcherMailSender, never()).send(anyString(), any(), anyString(),
                any(), anyMap(), anyMap());
    }
}