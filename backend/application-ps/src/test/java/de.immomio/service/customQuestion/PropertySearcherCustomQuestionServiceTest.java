package de.immomio.service.customQuestion;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.immomio.beans.landlord.CustomQuestionCreateBean;
import de.immomio.constants.exceptions.ResponseValidationException;
import de.immomio.data.base.json.JsonModel;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.customquestion.CustomQuestion;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.bean.customQuestion.CustomQuestionResponseCreateBean;
import de.immomio.data.shared.entity.common.customQuestion.CustomQuestionResponse;
import de.immomio.model.repository.shared.customquestion.CustomQuestionResponseRepository;
import de.immomio.utils.TestHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Maik Kingma
 */

@Slf4j
@ExtendWith(MockitoExtension.class)
public class PropertySearcherCustomQuestionServiceTest {

    private static final String JSON_PAYLOAD_CUSTOM_QUESTION = "/jsonPayload_customQuestion.json";
    private static final long CUSTOMER_ID = 1L;
    private static final String RESPONSE_SELECT2_A = "{\"select2\": \"a\"}";
    private static final String RESPONSE_SELECT_A = "{\"select\": \"a\"}";
    private static final String RESPONSE_SELECT_MULIT_INVALID_ONE = "{\"select2\": [\"b\", \"de\"]}";
    private static final String RESPONSE_EMPTY = "{}";
    private static final String TEST_FAILED_NO_EXCEPTION_THROWN_DURING_TEST_EXECUTION =
            "TEST_FAILED_NO_EXCEPTION_THROWN_DURING_TEST_EXECUTION";

    private CustomQuestion question;
    private PropertySearcherUserProfile userProfile;

    @Mock
    private CustomQuestionResponseRepository customQuestionResponseRepository;

    @Mock
    private PropertySearcherCustomQuestionResponseMessageSender customQuestionResponseMessageSender;

    @InjectMocks
    @Spy
    private PropertySearcherCustomQuestionService customQuestionService;

    @BeforeEach
    public void createCustomer() throws IOException {
        CustomQuestionCreateBean bean = TestHelper.createCustomQuestionBeaFromJsonFile(
                JSON_PAYLOAD_CUSTOM_QUESTION,
                PropertySearcherCustomQuestionService.class);

        LandlordCustomer customer = TestHelper.generateLandlordCustomer();
        question = TestHelper.createCustomQuestion(bean, customer);
        userProfile = TestHelper.generatePropertySearcherUser(TestHelper.generateCustomer()).getMainProfile();
    }

    @Test
    public void createQuestionResponseSave() throws IOException, ResponseValidationException {
        when(customQuestionResponseRepository.save(any(CustomQuestionResponse.class)))
                .thenAnswer(invocation -> invocation.getArguments()[0]);

        CustomQuestionResponseCreateBean response = createResponseModelFromString(RESPONSE_SELECT2_A);

        when(customQuestionResponseRepository.findByUserProfileAndCustomQuestion(any(), any())).thenReturn(Optional.empty());

        CustomQuestionResponse questionResponse = customQuestionService.respond(response, userProfile, question, true);

        verify(customQuestionService, times(1)).save(questionResponse);
    }

    @Test
    public void createQuestionResponseFailsWithUnmatchingQuestionResposeKeyCombo() throws Exception {
        CustomQuestionResponseCreateBean response = createResponseModelFromString(RESPONSE_SELECT_A);
        try {
            customQuestionService.respond(response, userProfile, question, true);
            Assertions.fail(TEST_FAILED_NO_EXCEPTION_THROWN_DURING_TEST_EXECUTION);
        } catch (ResponseValidationException e) {
            Assertions.assertEquals(
                    e.getMessage(),
                    PropertySearcherCustomQuestionService.INVALID_RESPONSE_KEY_NOT_MATCHING_QUESTION_KEYS);
        }
    }

    @Test
    public void createQuestionResponseFailsWithInvalidResponseOptionsContained() throws Exception {
        CustomQuestionResponseCreateBean response = createResponseModelFromString(RESPONSE_SELECT_MULIT_INVALID_ONE);
        try {
            customQuestionService.respond(response, userProfile, question, true);
            Assertions.fail(TEST_FAILED_NO_EXCEPTION_THROWN_DURING_TEST_EXECUTION);
        } catch (ResponseValidationException e) {
            Assertions.assertEquals(
                    e.getMessage(),
                    PropertySearcherCustomQuestionService.INVALID_RESPONSE_OPTIONS_CONTAINED);
        }
    }

    @Test
    public void createQuestionResponseFailsWithEmptyJsonModel() throws Exception {
        CustomQuestionResponseCreateBean response = createResponseModelFromString(RESPONSE_EMPTY);
        try {
            customQuestionService.respond(response, userProfile, question, true);
            Assertions.fail(TEST_FAILED_NO_EXCEPTION_THROWN_DURING_TEST_EXECUTION);
        } catch (ResponseValidationException e) {
            Assertions.assertEquals(
                    e.getMessage(),
                    PropertySearcherCustomQuestionService.EMPTY_RESPONSE_MODEL);
        }
    }

    private CustomQuestionResponseCreateBean createResponseModelFromString(String model) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return new CustomQuestionResponseCreateBean(mapper.readValue(model, JsonModel.class), "", null);
    }
}
