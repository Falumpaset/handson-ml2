package de.immomio.landlord.service.customquestion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.immomio.beans.landlord.CustomQuestionCreateBean;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.constants.exceptions.CustomQuestionValidationException;
import de.immomio.data.base.json.JsonSchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.Validator;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static de.immomio.landlord.service.customquestion.LandlordCustomQuestionService.DESIRED_RESPONSE_KEY_DOES_NOT_MATCH_QUESTION_KEY;
import static de.immomio.landlord.service.customquestion.LandlordCustomQuestionService.MORE_THAN_ONE_QUESTION_IN_SCHEMA;
import static de.immomio.landlord.service.customquestion.LandlordCustomQuestionService.MORE_THAN_ONE_RESPONSE_KEY;
import static de.immomio.landlord.service.customquestion.LandlordCustomQuestionService.RESPONSE_NOT_CONTAINED_IN_GIVEN_RESPONSE_SELECTION;
import static de.immomio.landlord.service.customquestion.LandlordCustomQuestionService.SCORING_TURNED_ON_BUT_DESIRED_ANSWERS_UNDEFINED;

/**
 * @author Niklas Lindemann, Maik Kingma
 */

@Service
public class LandlordCustomQuestionValidator {

    public static final String IMPORTANCE_NOT_ALLOWED_AT_PROPERTY_QUESTIONS_L = "IMPORTANCE_NOT_ALLOWED_AT_PROPERTY_QUESTIONS_L";
    private ObjectMapper objectMapper = new ObjectMapper();

    public void validateParameters(JsonSchema jsonSchema, Map<String, Object> input)
            throws JsonProcessingException, JSONException, ValidationException {
        SchemaLoader loader = SchemaLoader.builder().schemaJson(serializeObjectToJSONObject(jsonSchema)).build();
        Schema schema = loader.load().build();

        Validator validator = Validator.builder().build();
        validator.performValidation(schema, serializeObjectToJSONObject(input));
    }

    public void validateScoringModel(CustomQuestionCreateBean questionBean) throws CustomQuestionValidationException {
        if (questionBean.isScoring() && questionBean.getDesiredResponses().isEmpty()) {
            throw new CustomQuestionValidationException(SCORING_TURNED_ON_BUT_DESIRED_ANSWERS_UNDEFINED);
        }
    }

    public void validateQuestionSchema(JsonSchema schema) throws CustomQuestionValidationException {
        if (schema.getProperties().size() > 1) {
            throw new CustomQuestionValidationException(MORE_THAN_ONE_QUESTION_IN_SCHEMA);
        }
    }

    public void validateGlobalQuestion(CustomQuestionCreateBean questionCreateBean) {
        if(!questionCreateBean.isGlobal() && questionCreateBean.getImportance() > 0) {
            throw new ApiValidationException(IMPORTANCE_NOT_ALLOWED_AT_PROPERTY_QUESTIONS_L);
        }
    }

    public void validateQuestionResponseModel(
            JsonSchema schema,
            Map<String, Object> desiredResponses
    ) throws CustomQuestionValidationException {
        if (desiredResponses.size() > 1) {
            throw new CustomQuestionValidationException(MORE_THAN_ONE_RESPONSE_KEY);
        }
        Map<String, JsonSchema> properties = schema.getProperties();
        Set<String> keys = properties.keySet();

        if (!desiredResponses.keySet().containsAll(keys)) {
            throw new CustomQuestionValidationException(DESIRED_RESPONSE_KEY_DOES_NOT_MATCH_QUESTION_KEY);
        }

        for (String key : keys) {
            JsonSchema question = properties.get(key);
            List<Object> desiredResponsesValues;
            try {
                desiredResponsesValues = (List<Object>) desiredResponses.get(key);
            } catch (ClassCastException e) {
                desiredResponsesValues = Arrays.asList(desiredResponses.get(key));
            }
            isValidDesiredResponseModel(desiredResponsesValues, question);
        }
    }

    private void isValidDesiredResponseModel(
            List<Object> desiredResponsesValues,
            JsonSchema question
    ) throws CustomQuestionValidationException {
        List<Object> keys = getKeysFromDesiredResponses(desiredResponsesValues);
        if (!question.getEnums().containsAll(keys)) {
            throw new CustomQuestionValidationException(RESPONSE_NOT_CONTAINED_IN_GIVEN_RESPONSE_SELECTION);
        }
    }

    private List<Object> getKeysFromDesiredResponses(List<Object> desiredResponsesValues) {
        List<Object> flattedList = desiredResponsesValues.stream()
                .map(desiredResponse -> {
                    if (hasAttributes(desiredResponse)) {
                        return ((HashMap<Object, Object>) desiredResponse).keySet();
                    } else {
                        return Collections.singleton(desiredResponse);
                    }
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return flattedList;
    }

    private boolean hasAttributes(Object desiredResponse) {
        return desiredResponse instanceof HashMap;
    }

    private JSONObject serializeObjectToJSONObject(Object value)
            throws JsonProcessingException, JSONException {
        return new JSONObject(objectMapper.writeValueAsString(value));
    }
}
