package de.immomio.service.property;

import de.immomio.AbstractTest;
import de.immomio.constants.exceptions.NotAuthorizedException;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.model.repository.shared.property.PropertyRepository;
import de.immomio.service.security.UserSecurityService;
import de.immomio.utils.TestComparatorHelper;
import de.immomio.utils.TestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * @author Niklas Lindemann
 */

@ExtendWith(MockitoExtension.class)
public class PropertyServiceTest {

    private static final long CUSTOMER_ID_ONE = 1L;

    private static final long CUSTOMER_ID_TWO = 2L;

    private static final long PROPERTY_ID = 1L;
    @Mock
    private PropertyRepository propertyRepository;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private UserSecurityService userSecurityService;

    @InjectMocks
    private PropertyService propertyService;

    @Test
    public void findByIdNotNullNotAuthorized() throws Exception {
        Property property = TestHelper.generateProperty(de.immomio.utils.TestHelper.generateLandlordCustomer(), PROPERTY_ID);

        when(propertyRepository.findById(anyLong())).thenReturn(Optional.of(property));
        when(userSecurityService.allowUserToReadProperty(any(), anyLong())).thenReturn(false);

        Assertions.assertThrows(NotAuthorizedException.class, () -> propertyService.findById(1L));
    }

    @Test
    public void findByIdNotNullAuthorized() throws Exception {
        Property property = TestHelper.generateProperty(TestHelper.generateLandlordCustomer(), PROPERTY_ID);

        when(propertyRepository.findById(anyLong())).thenReturn(Optional.of(property));
        when(userSecurityService.allowUserToReadProperty(any(), anyLong())).thenReturn(true);

        Property propertyFound = propertyService.findById(1L);

        TestComparatorHelper.compareProperty(property, propertyFound);
    }
}