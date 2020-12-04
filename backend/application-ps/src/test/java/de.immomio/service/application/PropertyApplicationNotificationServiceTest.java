package de.immomio.service.application;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.PropertySearcherMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.utils.TestHelper;
import org.hamcrest.MatcherAssert;
import org.hamcrest.collection.IsMapContaining;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Freddy Sawma
 */

@ExtendWith(MockitoExtension.class)
public class PropertyApplicationNotificationServiceTest {

    @Mock
    private PropertySearcherMailSender mailSender;

    @Spy
    @InjectMocks
    private PropertyApplicationNotificationService applicationNotificationService;

    @Test
    public void applicationConfirmed() {
        PropertySearcherUserProfile mainProfile = TestHelper.generatePropertySearcherUser(TestHelper.generateCustomer()).getMainProfile();
        Property property = TestHelper.generateProperty(TestHelper.generateLandlordCustomer());

        doAnswer(invocation-> {
            Map<String, Object> map = (HashMap<String, Object>) invocation.getArguments()[3];
            Assertions.assertEquals(map.size(), 6);
            Assertions.assertTrue(map.containsKey(ModelParams.MODEL_USER));
            Assertions.assertTrue(map.containsKey(ModelParams.MODEL_USER_PROFILE));
            Assertions.assertTrue(map.containsKey(ModelParams.MODEL_FLAT));
            return null;
        }).when(mailSender).send(
                eq(mainProfile),
                any(MailTemplate.class),
                anyString(),
                anyMap(),
                nullable(Long.class));

        applicationNotificationService.applicationConfirmed(mainProfile, property);

        verify(mailSender, times(1)).send(eq(mainProfile),
                any(MailTemplate.class),
                anyString(),
                anyMap(),
                nullable(Long.class));
    }

    @Test
    public void applicationAccepted() {
        PropertySearcherUserProfile mainProfile = TestHelper.generatePropertySearcherUser(TestHelper.generateCustomer()).getMainProfile();
        Property property = TestHelper.generateProperty(TestHelper.generateLandlordCustomer());

        doAnswer(invocation-> {
            Map<String, Object> map = (HashMap<String, Object>) invocation.getArguments()[3];
            Assertions.assertEquals(map.size(),6);
            MatcherAssert.assertThat(map, IsMapContaining.hasKey(ModelParams.MODEL_USER));
            Assertions.assertTrue(map.containsKey(ModelParams.MODEL_USER_PROFILE));
            Assertions.assertTrue(map.containsKey(ModelParams.MODEL_FLAT));
            return null;
        }).when(mailSender).send(
                eq(mainProfile),
                any(MailTemplate.class),
                anyString(),
                anyMap());

        applicationNotificationService.applicationAccepted(mainProfile, property);

        verify(mailSender, times(1)).send(eq(mainProfile),
                any(MailTemplate.class),
                anyString(),
                anyMap());
    }
}