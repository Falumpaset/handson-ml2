/**
 *
 */
package de.immomio.service.calculator;

import de.immomio.data.base.bean.score.CustomQuestionScoreBean;
import de.immomio.data.base.json.JsonForm;
import de.immomio.data.base.json.JsonModel;
import de.immomio.data.base.type.customQuestion.CustomQuestionType;
import de.immomio.data.landlord.bean.prioset.BoundaryValue;
import de.immomio.data.landlord.bean.prioset.PriosetData;
import de.immomio.data.landlord.entity.customquestion.CustomQuestion;
import de.immomio.data.landlord.entity.customquestion.PriosetCustomQuestionAssociation;
import de.immomio.data.landlord.entity.prioset.Prioset;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileData;
import de.immomio.data.propertysearcher.entity.user.profile.details.Profession;
import de.immomio.data.shared.entity.common.customQuestion.CustomQuestionResponse;
import de.immomio.service.calculator.implementation.CustomBooleanCalculator;
import de.immomio.service.calculator.implementation.DigitalPositiveCalculator;
import de.immomio.service.calculator.implementation.MathematicalCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Johannes Hiemer, Maik Kingma
 */


@Slf4j
@Service
public class Calculator {

    private static final String CHILDREN_SCORE = "childrenScore";
    private static final String INCOME_SCORE = "incomeScore";
    private static final String ANIMAL_SCORE = "animalScore";
    private static final String HOUSEHOLD_TYPE_SCORE = "householdTypeScore";
    private static final String RESIDENTS_SCORE = "residentsScore";
    private static final String JOB_SCORE = "jobScore";
    private static final String AGE_SCORE = "ageScore";
    private static final String PRODUCTION = "production";
    private static final String RANGED_OPTIONS = "rangedOptions";
    private static final String MAX = "max";
    private static final String MIN = "min";
    private static final String KEY = "key";
    private static final String TYPE = "type";
    private static final String RANGE = "range";
    private static final String DESIRED_RANGE = "desiredRange";

    private final Environment env;

    private final DigitalPositiveCalculator digitalPositiveCalculator;

    private final MathematicalCalculator mathematicalCalculator;

    private final CustomBooleanCalculator customBooleanCalculator;

    @Autowired
    public Calculator(
            Environment env,
            DigitalPositiveCalculator digitalPositiveCalculator,
            MathematicalCalculator mathematicalCalculator,
            CustomBooleanCalculator customBooleanCalculator
    ) {
        this.env = env;
        this.digitalPositiveCalculator = digitalPositiveCalculator;
        this.mathematicalCalculator = mathematicalCalculator;
        this.customBooleanCalculator = customBooleanCalculator;
    }

    BigDecimal calculate(Prioset prioset, PropertySearcherUserProfileData userProfile, Property property, int priosetScore) {
        Map<String, Double> resultMap = new HashMap<>();
        double result = 0;

        /*
         * Digital positive
         */
        PriosetData priosetData = prioset.getData();
        if (priosetData.getAge() != null) {
            double ageScore = digitalPositiveCalculator
                    .calculateAge(prioset, userProfile) * priosetData.getAge().getValue();
            resultMap.put(AGE_SCORE, ageScore);
        }

        if (priosetData.getEmploymentType() != null) {
            double jobScore = digitalPositiveCalculator
                    .calculateJob(prioset, userProfile) * priosetData.getEmploymentType().getValue();
            resultMap.put(JOB_SCORE, jobScore);
        }

        if (priosetData.getResidents() != null) {
            double residentsScore = digitalPositiveCalculator
                    .calculateResidents(prioset, userProfile) * priosetData.getResidents().getValue();
            resultMap.put(RESIDENTS_SCORE, residentsScore);
        }

        if (priosetData.getHouseholdType() != null) {
            double householdTypeScore = digitalPositiveCalculator
                    .calculateHouseholdType(prioset, userProfile) * priosetData.getHouseholdType().getValue();
            resultMap.put(HOUSEHOLD_TYPE_SCORE, householdTypeScore);
        }

        if (priosetData.getAnimals() != null) {
            double animalScore = digitalPositiveCalculator
                    .calculateAnimal(prioset, userProfile) * getPriosetDataValue(priosetData.getAnimals());
            resultMap.put(ANIMAL_SCORE, animalScore);
        }

        if (priosetData.getChildren() != null) {
            double childrenScore = digitalPositiveCalculator
                    .calculateChildren(prioset, userProfile) * getPriosetDataValue(priosetData.getChildren());
            resultMap.put(CHILDREN_SCORE, childrenScore);
        }

        /*
         * Income
         */
        if (priosetData.getMonthlyIncome() != null) {
            resultMap.put(INCOME_SCORE, calculateMonthlyIncomeScore(prioset, userProfile, property));
        }

        for (Entry<String, Double> detailResult : resultMap.entrySet()) {
            Double value = detailResult.getValue();
            if (!value.isNaN()) {
                result += value;
            } else {
                log.error("Calculated Score for " + detailResult.getKey() + " of prioset " + prioset.getId() + " " +
                        "resulted in NaN!");
            }
        }

        if (priosetData.getWbs() != null && priosetData.getWbs()) {
            double wbsScore = customBooleanCalculator.calculateWbs(userProfile.getAdditionalInformation());
            result = result * wbsScore;
        }


        return BigDecimal.valueOf(result);
    }

    private double getIncomeFactor(
            PropertySearcherUserProfileData userProfile,
            double totalRentGross,
            BoundaryValue rentBoundaries
    ) {
        double income = 0;
        Profession profession = userProfile.getProfession();
        if (profession != null && profession.getIncome() != null) {
            income = profession.getIncome();
        }
        int lowerBound = rentBoundaries.getLowerBound();
        int upperBound = rentBoundaries.getUpperBound();

        if (upperBound <= lowerBound) {
            return 0.0;
        }

        return mathematicalCalculator.calculateNewIncomeFactor(income, totalRentGross, lowerBound, upperBound);
    }

    public double calculateMonthlyIncomeScore(
            Prioset prioset,
            PropertySearcherUserProfileData userProfile,
            Property property
    ) {
        BoundaryValue monthlyIncomePrio = prioset.getData().getMonthlyIncome();
        double incomeFactor = getIncomeFactor(userProfile, property.getData().getTotalRentGross(), monthlyIncomePrio);

        return incomeFactor * monthlyIncomePrio.getValue();
    }

    private Integer getPriosetDataValue(Integer value) {
        return value != null ? value : 0;
    }

    public CustomQuestionScoreBean evaluateCustomQuestions(List<CustomQuestionResponse> responses, Property property) {
        AtomicReference<Double> scoreIncludingRange = new AtomicReference<>(0.0);
        AtomicReference<Double> scoreExcludingRange = new AtomicReference<>(0.0);

        CustomQuestionScoreBean.CustomQuestionScoreBeanBuilder scoreBeanBuilder = CustomQuestionScoreBean.builder();
        if (responses == null || responses.isEmpty()) {
            return null;
        }

        responses.forEach(response -> {
            JsonModel responseData = response.getData();
            responseData.keySet().forEach(key -> {
                Object responseObject = response.getData().get(key);
                CustomQuestion customQuestion = response.getCustomQuestion();

                Object desiredResponseObject = customQuestion.getDesiredResponses().get(key);

                if (responseObject != null && desiredResponseObject != null) {
                    List<Object> responseDesired = createResponseListFromObject(desiredResponseObject);
                    List<Object> responseGiven = createResponseListFromObject(responseObject);
                    if (responseDesired.containsAll(responseGiven)) {
                        setScoreExcludingRange(property, scoreExcludingRange, customQuestion);
                        if (isRangedQuestion(customQuestion, key)) {
                            BigDecimal rangeScore = getRangeScore(response, key, responseObject, customQuestion);
                            setScoreIncludingRange(property, scoreIncludingRange, customQuestion, rangeScore);
                        } else {
                            setScoreIncludingRange(property, scoreIncludingRange, customQuestion, BigDecimal.ONE);
                        }
                    }

                }
            });
        });
        return scoreBeanBuilder
                .scoreExcludingRange(BigDecimal.valueOf(scoreExcludingRange.get()))
                .scoreIncludingRange(BigDecimal.valueOf(scoreIncludingRange.get()))
                .build();

    }

    private void setScoreIncludingRange(Property property, AtomicReference<Double> scoreIncludingRange, CustomQuestion customQuestion, BigDecimal rangeScore) {
        if (customQuestion.getType() == CustomQuestionType.PROPERTY) {
            scoreIncludingRange.updateAndGet(item ->
                    customQuestion.getPriosets()
                            .stream()
                            .filter(association -> priosetEquals(property.getPrioset(), association))
                            .findFirst()
                            .map(association -> applyRangeScoreToCustomQuestionScore(rangeScore, item, association.getImportance()))
                            .orElse(item));
        } else {
            scoreIncludingRange.updateAndGet(item -> applyRangeScoreToCustomQuestionScore(rangeScore, item, customQuestion.getImportance()));
        }
    }

    private void setScoreExcludingRange(Property property, AtomicReference<Double> scoreExcludingRange, CustomQuestion customQuestion) {
        if (customQuestion.getType() == CustomQuestionType.PROPERTY) {
            scoreExcludingRange.updateAndGet(item ->
                    customQuestion.getPriosets()
                            .stream()
                            .filter(association -> priosetEquals(property.getPrioset(), association))
                            .findFirst()
                            .map(association -> (item + association.getImportance()))
                            .orElse(item));
        } else {
            scoreExcludingRange.updateAndGet(item -> (double) customQuestion.getImportance() + item);
        }
    }

    private double applyRangeScoreToCustomQuestionScore(BigDecimal rangeScore, Double item, Integer importance) {
        return BigDecimal.valueOf(item + importance).multiply(rangeScore).doubleValue();
    }

    private boolean priosetEquals(Prioset prioset, PriosetCustomQuestionAssociation association) {
        return association.getPrioset().equals(prioset);
    }

    private BigDecimal getRangeScore(
            CustomQuestionResponse response,
            String key,
            Object responseObject, CustomQuestion customQuestion
    ) {
        String desiredRange = getDesiredRange(key, responseObject, customQuestion);
        Map<String, Object> formParams = getFormParams(customQuestion, key);
        Map rangedOptions = (HashMap) formParams.get(RANGED_OPTIONS);
        Long max = getRange(rangedOptions, MAX);
        Long min = getRange(rangedOptions, MIN);
        BigDecimal rangeScore;

        if (response.getSelectedRange() == null) {
            return BigDecimal.ZERO;
        }

        if (desiredRange.equals(MAX)) {
            if (response.getSelectedRange() > max) {
                return BigDecimal.valueOf(1);
            }
            if (response.getSelectedRange() < min) {
                return BigDecimal.valueOf(0);
            }
            rangeScore = calculateCustomQuestionRangeScore(min, max, response.getSelectedRange());
        } else {
            if (response.getSelectedRange() < min) {
                return BigDecimal.valueOf(1);
            }
            if (response.getSelectedRange() > max) {
                return BigDecimal.valueOf(0);
            }
            rangeScore = calculateCustomQuestionRangeScore(max, min, response.getSelectedRange());
        }

        return rangeScore;
    }

    private Long getRange(Map rangedOptions, String rangeKey) {
        try {
            return Long.valueOf(String.valueOf(rangedOptions.get(rangeKey)));
        } catch (Exception e) {
            return 0L;
        }
    }

    private String getDesiredRange(String key, Object responseObject, CustomQuestion customQuestion) {
        Map<String, Object> desiredResponses = customQuestion.getDesiredResponses();
        Map responseMap = (HashMap) desiredResponses.getOrDefault(key, new HashMap<>());
        Map responseParams = (HashMap) responseMap.getOrDefault(responseObject, new HashMap<>());

        return (String) responseParams.getOrDefault(DESIRED_RANGE, MIN);
    }

    private boolean isRangedQuestion(CustomQuestion customQuestion, String key) {
        Map<String, Object> formParams = getFormParams(customQuestion, key);

        return formParams.getOrDefault(TYPE, "").equals(RANGE);
    }

    private Map<String, Object> getFormParams(CustomQuestion customQuestion, String key) {
        JsonForm jsonForm = customQuestion.getForm();

        return jsonForm.stream()
                .map(formParam -> (HashMap<String, Object>) formParam)
                .filter(map -> map.get(KEY).equals(key))
                .findFirst()
                .orElse(new LinkedHashMap<>());
    }

    public BigDecimal calculateCustomQuestionRangeScore(Long worst, Long best, Long answer) {
        if (worst == null || best == null || answer == null) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(answer)
                .subtract(BigDecimal.valueOf(worst))
                .divide(BigDecimal.valueOf(best)
                        .subtract(BigDecimal.valueOf(worst)), 2, RoundingMode.HALF_UP);
    }

    private List<Object> createResponseListFromObject(Object responseObject) {
        List<Object> responseDesired;
        if (responseObject instanceof HashMap) {
            Map map = (HashMap) responseObject;
            return new ArrayList<>(map.keySet());
        }
        try {
            responseDesired = (List<Object>) responseObject;
        } catch (ClassCastException e) {
            responseDesired = Arrays.asList(responseObject);
        }
        return responseDesired;
    }
}
