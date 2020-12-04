package de.immomio.service.application;

import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.data.shared.entity.property.search.PropertySearcherSearchBean;
import de.immomio.data.shared.entity.property.search.PropertySearcherSearchItem;
import de.immomio.landlord.service.application.LandlordPropertyApplicationSearchService;
import de.immomio.model.repository.shared.application.PropertyApplicationRepository;
import de.immomio.service.AbstractTest;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import utils.TestHelper;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static utils.TestHelper.generateApplication;

/**
 * @author Niklas Lindemann
 */

public class LandlordPropertyApplicationSearchServiceTest extends AbstractTest {

    private static final long USER_ID_1 = 1L;

    private static final long USER_ID_2 = 2L;


    @Mock
    private PropertyApplicationRepository propertyApplicationRepository;


    @InjectMocks
    @Spy
    private LandlordPropertyApplicationSearchService applicationSearchService;


    @Test
    public void findByNameAndEmail() {
        PropertySearcherUserProfile user1 = TestHelper.generatePropertySearcherUser(
                "test1@mamil.de",
                "Test",
                "Person", USER_ID_1);
        PropertyApplication application1_1 = generateApplication(ApplicationStatus.ACCEPTED, user1, 1L);
        PropertyApplication application1_2 = generateApplication(ApplicationStatus.ACCEPTED, user1, 2L);

        PropertySearcherUserProfile user2 = TestHelper.generatePropertySearcherUser(
                "test2@mamil.de",
                "Test2",
                "Person2", USER_ID_2);
        PropertyApplication application2_1 = generateApplication(ApplicationStatus.ACCEPTED, user2, 3L);
        PropertyApplication application2_2 = generateApplication(ApplicationStatus.ACCEPTED, user2, 4L);
        PropertyApplication application2_3 = generateApplication(ApplicationStatus.ACCEPTED, user2, 5L);

        when(propertyApplicationRepository.findByPsNameOrEmail(anyString()))
                .thenReturn(Arrays.asList(
                        application1_1,
                        application1_2,
                        application2_1,
                        application2_2,
                        application2_3));
        List<PropertySearcherSearchBean> beans = applicationSearchService.findByNameAndEmail("name");

        Assert.assertEquals(beans.size(), 2);
        List<PropertySearcherSearchItem> searchBeans1 = Arrays.asList(
                new PropertySearcherSearchItem(application1_1.getProperty(), application1_1.getStatus().name(), application1_1.getScore(), application1_1.getId()),
                new PropertySearcherSearchItem(application1_2.getProperty(), application1_2.getStatus().name(),application1_2.getScore(), application1_2.getId()));

        List<PropertySearcherSearchItem> searchBeans2 = Arrays.asList(
                new PropertySearcherSearchItem(application2_1.getProperty(), application2_1.getStatus().name(), application2_1.getScore(), application2_1.getId()),
                new PropertySearcherSearchItem(application2_2.getProperty(), application2_2.getStatus().name(), application2_2.getScore(), application2_2.getId()),
                new PropertySearcherSearchItem(application2_3.getProperty(), application2_3.getStatus().name(), application2_3.getScore(), application2_3.getId()));

        PropertySearcherSearchBean bean1 = beans.stream()
                .filter(bean -> bean.getUserId().equals(USER_ID_1))
                .findFirst()
                .orElse(null);
        PropertySearcherSearchBean bean2 = beans.stream()
                .filter(bean -> bean.getUserId().equals(USER_ID_2))
                .findFirst()
                .orElse(null);

        Assert.assertTrue(bean1.getItems().containsAll(searchBeans1));
        Assert.assertEquals(2, bean1.getItems().size());
        Assert.assertTrue(bean2.getItems().containsAll(searchBeans2));
        Assert.assertEquals(3, bean2.getItems().size());
    }


}