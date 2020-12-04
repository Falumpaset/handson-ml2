package de.immomio.service.customQuestion;

import de.immomio.beans.landlord.CustomQuestionCreateBean;
import de.immomio.constants.exceptions.CustomQuestionValidationException;

import de.immomio.landlord.service.customquestion.LandlordCustomQuestionService;
import de.immomio.landlord.service.customquestion.LandlordCustomQuestionValidator;

import de.immomio.service.AbstractTest;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import utils.TestHelper;

import static de.immomio.landlord.service.customquestion.LandlordCustomQuestionService.*;

/**
 * @author Maik Kingma
 */

public class LandlordCustomQuestionValidatorTest extends AbstractTest {

    private static final String JSON_PAYLOAD_FAIL_SCHEMA_AMOUNT_QUESTIONS =
            "/jsonPayload_Fail_Schema_Amount_Questions.json";
    private static final String TEST_FAILED_NO_EXCEPTION_THROWN_DURING_TEST_EXECUTION =
            "TEST_FAILED_NO_EXCEPTION_THROWN_DURING_TEST_EXECUTION";
    private static final String JSON_PAYLOAD_FAIL_MORE_THAN_ONE_RESPONSE =
            "/jsonPayload_Fail_more_than_one_response.json";
    private static final String JSON_PAYLOAD_FAIL_NOT_MATCHING_QUESTION_RESPONSE_KEY =
            "/jsonPayload_Fail_not_matching_question_response_key.json";
    private static final String JSON_PAYLOAD_FAIL_RESPONSE_NOT_CONTAINED_IN_PREDEFINED_SELECTION =
            "/jsonPayload_Fail_response_not_contained_in_predefined_selection.json";
    private static final String JSON_PAYLOAD_FAIL_SCORING_BUT_NO_DESIRED_ANSWERS_PROVIDED =
            "/jsonPayload_Fail_scoring_but_no_desired_answers_provided.json";

    @InjectMocks
    @Spy
    private LandlordCustomQuestionValidator customQuestionValidator;

    @Test
    public void creatingQuestionFailsWithTooManyInSchemaPayload() throws Exception {
        try {
            CustomQuestionCreateBean bean = TestHelper.createCustomQuestionBeaFromJsonFile(
                    JSON_PAYLOAD_FAIL_SCHEMA_AMOUNT_QUESTIONS,
                    LandlordCustomQuestionService.class);
            customQuestionValidator.validateQuestionSchema(bean.getJsonPayload().getSchema());
            Assert.fail(TEST_FAILED_NO_EXCEPTION_THROWN_DURING_TEST_EXECUTION);
        } catch (CustomQuestionValidationException e) {
            Assert.assertEquals(e.getMessage(), MORE_THAN_ONE_QUESTION_IN_SCHEMA);
        }
    }

    @Test
    public void createQuestionFailsWithMoreThanOneResponse() throws Exception {
        try {
            CustomQuestionCreateBean bean = TestHelper.createCustomQuestionBeaFromJsonFile(
                    JSON_PAYLOAD_FAIL_MORE_THAN_ONE_RESPONSE,
                    LandlordCustomQuestionService.class);
            customQuestionValidator.validateQuestionResponseModel(
                    bean.getJsonPayload().getSchema(),
                    bean.getDesiredResponses());
            Assert.fail(TEST_FAILED_NO_EXCEPTION_THROWN_DURING_TEST_EXECUTION);
        } catch (CustomQuestionValidationException e) {
            Assert.assertEquals(e.getMessage(), MORE_THAN_ONE_RESPONSE_KEY);
        }
    }

    @Test
    public void createQuestionFailsWithResponseNotContainedInPredefinedSelection() throws Exception {
        try {
            CustomQuestionCreateBean bean = TestHelper.createCustomQuestionBeaFromJsonFile(
                    JSON_PAYLOAD_FAIL_RESPONSE_NOT_CONTAINED_IN_PREDEFINED_SELECTION,
                    LandlordCustomQuestionService.class);
            customQuestionValidator.validateQuestionResponseModel(
                    bean.getJsonPayload().getSchema(),
                    bean.getDesiredResponses());
            Assert.fail(TEST_FAILED_NO_EXCEPTION_THROWN_DURING_TEST_EXECUTION);
        } catch (CustomQuestionValidationException e) {
            Assert.assertEquals(e.getMessage(), RESPONSE_NOT_CONTAINED_IN_GIVEN_RESPONSE_SELECTION);
        }
    }

    @Test
    public void createQuestionFailsWithNotMatchingQuestionResponseKey() throws Exception {
        CustomQuestionCreateBean bean = TestHelper.createCustomQuestionBeaFromJsonFile(
                JSON_PAYLOAD_FAIL_NOT_MATCHING_QUESTION_RESPONSE_KEY,
                LandlordCustomQuestionService.class);
        try {
            customQuestionValidator.validateQuestionResponseModel(
                    bean.getJsonPayload().getSchema(),
                    bean.getDesiredResponses());
            Assert.fail(TEST_FAILED_NO_EXCEPTION_THROWN_DURING_TEST_EXECUTION);
        } catch (CustomQuestionValidationException e) {
            Assert.assertEquals(
                    e.getMessage(),
                    DESIRED_RESPONSE_KEY_DOES_NOT_MATCH_QUESTION_KEY);
        }
    }

    @Test
    public void createQuestionFailsWithScoringButNoDesiredAnswersProvided() throws Exception {
        CustomQuestionCreateBean bean = TestHelper.createCustomQuestionBeaFromJsonFile(
                JSON_PAYLOAD_FAIL_SCORING_BUT_NO_DESIRED_ANSWERS_PROVIDED,
                LandlordCustomQuestionService.class);
        try {
            customQuestionValidator.validateScoringModel(bean);
            Assert.fail(TEST_FAILED_NO_EXCEPTION_THROWN_DURING_TEST_EXECUTION);
        } catch (CustomQuestionValidationException e) {
            Assert.assertEquals(
                    e.getMessage(),
                    SCORING_TURNED_ON_BUT_DESIRED_ANSWERS_UNDEFINED);
        }
    }
}