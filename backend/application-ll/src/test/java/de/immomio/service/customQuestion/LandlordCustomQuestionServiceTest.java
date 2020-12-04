package de.immomio.service.customQuestion;

import de.immomio.beans.landlord.CustomQuestionCreateBean;
import de.immomio.constants.exceptions.CustomQuestionValidationException;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.customquestion.CustomQuestion;
import de.immomio.landlord.service.customquestion.LandlordCustomQuestionService;
import de.immomio.landlord.service.customquestion.LandlordCustomQuestionValidator;
import de.immomio.model.repository.shared.customquestion.CustomQuestionRepository;
import de.immomio.service.AbstractTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import utils.TestHelper;

import java.io.IOException;

import static de.immomio.landlord.service.customquestion.LandlordCustomQuestionService.SCHEMA_OR_FORM_NOT_PROVIDED;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Maik Kingma
 */

@Slf4j
public class LandlordCustomQuestionServiceTest extends AbstractTest {

    private static final String JSON_PAYLOAD_SUCCESS = "/jsonPayload_Success.json";
    private static final String TEST_FAILED_NO_EXCEPTION_THROWN_DURING_TEST_EXECUTION =
            "TEST_FAILED_NO_EXCEPTION_THROWN_DURING_TEST_EXECUTION";
    private static final String JSON_PAYLOAD_FAIL_SCHEMA_NULL = "/jsonPayload_Fail_schema_null.json";
    private static final String JSON_PAYLOAD_FAIL_FORM_NULL = "/jsonPayload_Fail_form_null.json";
    private static final long CUSTOMER_ID = 1L;

    private LandlordCustomer customer;

    @Mock
    private CustomQuestionRepository customQuestionRepository;

    @InjectMocks
    @Spy
    private LandlordCustomQuestionService customQuestionService;

    @Mock
    private LandlordCustomQuestionValidator validator;

    @Before
    public void createCustomer() {
         customer = TestHelper.generateLandlordCustomer(CUSTOMER_ID);
    }

    @Test
    public void createQuestionSave() throws CustomQuestionValidationException, IOException {
        when(customQuestionRepository.save(any(CustomQuestion.class)))
                .thenAnswer(invocation -> invocation.getArguments()[0]);
        CustomQuestionCreateBean bean = TestHelper.createCustomQuestionBeaFromJsonFile(JSON_PAYLOAD_SUCCESS,
                LandlordCustomQuestionService.class);
        CustomQuestion question = customQuestionService.createCustomQuestion(bean, customer);
        verify(customQuestionService, times(1)).save(question);
    }

    @Test
    public void createQuestionFailsSchemaNull() throws Exception {
        CustomQuestionCreateBean bean = TestHelper.createCustomQuestionBeaFromJsonFile(
                JSON_PAYLOAD_FAIL_SCHEMA_NULL,
                LandlordCustomQuestionService.class);
        try {
            customQuestionService.createCustomQuestion(bean, customer);
            Assert.fail(TEST_FAILED_NO_EXCEPTION_THROWN_DURING_TEST_EXECUTION);
        } catch (CustomQuestionValidationException e) {
            Assert.assertEquals(
                    e.getMessage(),
                    SCHEMA_OR_FORM_NOT_PROVIDED);
        }
    }

    @Test
    public void createQuestionFailsFormNull() throws Exception {
        CustomQuestionCreateBean bean = TestHelper.createCustomQuestionBeaFromJsonFile(
                JSON_PAYLOAD_FAIL_FORM_NULL,
                LandlordCustomQuestionService.class);
        try {
            customQuestionService.createCustomQuestion(bean, customer);
            Assert.fail(TEST_FAILED_NO_EXCEPTION_THROWN_DURING_TEST_EXECUTION);
        } catch (CustomQuestionValidationException e) {
            Assert.assertEquals(
                    e.getMessage(),
                    SCHEMA_OR_FORM_NOT_PROVIDED);
        }
    }
}
