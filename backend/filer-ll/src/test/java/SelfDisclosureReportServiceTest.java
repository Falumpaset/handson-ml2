import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.immomio.cloud.amazon.s3.LandlordS3FileManager;
import de.immomio.config.ApplicationMessageSource;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.shared.entity.selfdisclosure.SelfDisclosureResponse;
import de.immomio.data.shared.entity.selfdisclosure.json.SelfDisclosureResponseData;
import de.immomio.service.LandlordSelfDisclosureReportService;
import de.immomio.service.report.JasperReportService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Locale;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doReturn;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@ActiveProfiles(value = {"test", "development"})
@RunWith(SpringRunner.class)
public class SelfDisclosureReportServiceTest {

    @Mock
    private LandlordS3FileManager landlordS3FileManager;

    @Mock(answer = Answers.RETURNS_MOCKS)
    private ApplicationMessageSource messageSource;

    @Mock
    private JasperReportService jasperReportService;

    @Spy
    @InjectMocks
    private LandlordSelfDisclosureReportService selfDisclosureReportService;

    @Test
    public void generateReport() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SelfDisclosureResponseData data = objectMapper.readValue(getClass().getResourceAsStream("selfdisclosureresponse.json"), SelfDisclosureResponseData.class);

        PropertyData propertyData = objectMapper.readValue(getClass().getResourceAsStream("property.json"), PropertyData.class);

        Property property = new Property();
        property.setData(propertyData);

        doReturn("mocked").when(messageSource).getMessage(anyString(), any(), any(Locale.class));

        SelfDisclosureResponse response = new SelfDisclosureResponse();
        response.setData(data);
        response.setProperty(property);
        selfDisclosureReportService.generateReport(response);
    }
}
