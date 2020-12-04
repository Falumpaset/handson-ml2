package de.immomio.service.calculator;

import de.immomio.data.base.bean.score.CustomQuestionScoreBean;
import de.immomio.data.base.json.JsonForm;
import de.immomio.data.base.json.JsonModel;
import de.immomio.data.base.type.customQuestion.CustomQuestionType;
import de.immomio.data.base.type.property.EmploymentType;
import de.immomio.data.base.type.property.HouseholdType;
import de.immomio.data.landlord.bean.prioset.BoundaryValue;
import de.immomio.data.landlord.bean.prioset.ListValue;
import de.immomio.data.landlord.bean.prioset.PriosetData;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.customquestion.CustomQuestion;
import de.immomio.data.landlord.entity.prioset.Prioset;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileData;
import de.immomio.data.propertysearcher.entity.user.profile.details.AdditionalInformation;
import de.immomio.data.propertysearcher.entity.user.profile.details.Profession;
import de.immomio.data.shared.entity.common.customQuestion.CustomQuestionResponse;
import de.immomio.service.calculator.implementation.CustomBooleanCalculator;
import de.immomio.service.calculator.implementation.DigitalPositiveCalculator;
import de.immomio.service.calculator.implementation.MathematicalCalculator;
import de.immomio.service.calculator.implementation.PreferencePriosetCalculator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * @author Maik Kingma
 */

@RunWith(SpringRunner.class)
public class CalculatorTest {

    private static final String UNIT_TEST = "unit-test";

    //Test data
    private int[] ages = {25, 44, 69, 19, 36};
    private EmploymentType[] job = {
            EmploymentType.STUDENT,
            EmploymentType.EMPLOYED_UNLIMITED,
            EmploymentType.RETIRED,
            EmploymentType.LOOKING_FOR_WORK,
            EmploymentType.SELF_EMPLOYED};
    private int[] residents = {1, 4, 2, 2, 2};
    private HouseholdType[] household = {
            HouseholdType.SINGLE,
            HouseholdType.FAMILY,
            HouseholdType.COUPLE_WITHOUT_CHILDREN,
            HouseholdType.FLATSHARE,
            HouseholdType.SINGLE_WITH_CHILDREN};
    private boolean[] animals = {false, true, true, false, false};
    private double[] income = {1500.00, 5500.00, 2350.00, 800.00, 9000.00};
    private boolean[] wbs = {true, false, false, true, false};

    //Expected Test Results
    private double[] scoreResults = {5.882352941, 4.117647059, 3.87254902, 4.705882353, 8.529411765};
    private int[] ageScores = {3, 0, 0, 0, 3};
    private int[] jobScores = {0, 4, 0, 0, 4};
    private int[] residentScores = {7, 0, 7, 7, 7};
    private int[] householdScores = {1, 0, 1, 0, 0};
    private int[] animalScores = {5, 0, 0, 5, 5};
    private int[] childrenScores = {4, 0, 4, 4, 0};
    private double[] incomeScores = {0.0, 10.0, 1.166666667, 0.0, 10.0};

    private Property testProperty;

    private LinkedList<PropertySearcherUserProfile> testUsers;

    @Autowired
    private PreferencePriosetCalculator preferencePriosetCalculator;

    @Autowired
    private CalculationExecutor calculationExecutor;

    @Autowired
    private DigitalPositiveCalculator digitalPositiveCalculator;

    @Autowired
    private Calculator calculator;

    @Autowired
    private Environment environment;

    @TestConfiguration
    static class CalculatorTestConfiguration {

        @MockBean
        public Environment environment;

        @Bean
        public PreferencePriosetCalculator preferencePriosetCalculator() {
            return new PreferencePriosetCalculator();
        }

        @Bean
        public DigitalPositiveCalculator digitalPositiveCalculator() {
            return new DigitalPositiveCalculator();
        }

        @Bean
        public MathematicalCalculator mathematicalCalculator() {
            return new MathematicalCalculator();
        }

        @Bean
        public CustomBooleanCalculator customBooleanCalculator() {
            return new CustomBooleanCalculator();
        }

        @Bean
        public Calculator calculator() {
            return new Calculator(environment, digitalPositiveCalculator(),
                    mathematicalCalculator(), customBooleanCalculator());
        }

        @Bean
        public CalculationExecutor calculationExecutor() {
            return new CalculationExecutor(calculator(), preferencePriosetCalculator());
        }
    }

    @Before
    public void populateTestData() {
        this.testProperty = new Property();
        testProperty.setData(generateTestPropertyData());
        testProperty.setPrioset(generateTestPrioset());

        this.testUsers = new LinkedList<>();
        IntStream.range(0, 5).forEach(i -> testUsers.addLast(generateTestUser(i)));

        //PreCheck test input data
        Assert.assertEquals((Double) 1000.00, testProperty.getData().getTotalRentGross());
        Assert.assertEquals(34,
                preferencePriosetCalculator.calculatePreferencePrioset(testProperty.getPrioset()));

    }

    @Test
    public void evaluateCustomQuestions() {
        List<CustomQuestionResponse> responses = new ArrayList<>();
        responses.add(generateSimplePositivGlobalCustomQuestionResponse());
        responses.add(generateSimplePositivGlobalCustomQuestionResponse());

        CustomQuestionScoreBean customQuestionScoreBean = calculator.evaluateCustomQuestions(responses, null);

        Assert.assertEquals(20, customQuestionScoreBean.getScoreExcludingRange().intValue());
    }


    @Test
    public void calculateCustomQuestionRangeScore() {
        BigDecimal bigDecimal = calculator.calculateCustomQuestionRangeScore(1L, 20L, 5L);
        Assert.assertEquals(0.21, bigDecimal.doubleValue(), 0);

        bigDecimal = calculator.calculateCustomQuestionRangeScore(20L, 1L,  5L);
        Assert.assertEquals(0.79, bigDecimal.doubleValue(), 0);

        bigDecimal = calculator.calculateCustomQuestionRangeScore(20L, 1L, 1L);
        Assert.assertEquals(1.0, bigDecimal.doubleValue(), 0);

        bigDecimal = calculator.calculateCustomQuestionRangeScore(1L, 20L, 1L);
        Assert.assertEquals(0.0, bigDecimal.doubleValue(), 0);
    }

    @Test
    public void mainScoreShouldBeEqual() {
        Mockito.when(environment.getActiveProfiles()).thenReturn(new String[]{UNIT_TEST});
        IntStream.range(0, 5).forEach(i -> {
            Assert.assertEquals(getRoundedDouble(scoreResults[i]),
                    calculationExecutor.calculateScore(
                            testProperty,
                            testUsers.get(i),
                            Collections.emptyList(),
                            Collections.emptyList(),
                            Collections.emptyList()));
        });
    }

    @Test
    public void incomeScoreShouldBeEqual() {
        Prioset testPrioset = testProperty.getPrioset();

        IntStream.range(0, 5).forEach(i -> {
            PropertySearcherUserProfileData testProfile = testUsers.get(i).getData();
            Assert.assertEquals(incomeScores[i],
                    calculator.calculateMonthlyIncomeScore(testPrioset, testProfile, testProperty), 0.0000001);
        });
    }

    @Test
    public void digitalPositiveScoresShouldBeEqual() {
        Prioset testPrioset = testProperty.getPrioset();
        PriosetData data = testPrioset.getData();

        IntStream.range(0, 5).forEach(i -> {
            PropertySearcherUserProfileData testProfile = testUsers.get(i).getData();
            int agePrio = data.getAge().getValue();
            int jobPrio = data.getEmploymentType().getValue();
            int residentsPrio = data.getResidents().getValue();
            int householdPrio = data.getHouseholdType().getValue();

            Assert.assertEquals(ageScores[i],
                    digitalPositiveCalculator.calculateAge(testPrioset, testProfile) * agePrio);
            Assert.assertEquals(jobScores[i],
                    digitalPositiveCalculator.calculateJob(testPrioset, testProfile) * jobPrio);
            Assert.assertEquals(residentScores[i],
                    digitalPositiveCalculator.calculateResidents(testPrioset, testProfile) * residentsPrio);
            Assert.assertEquals(householdScores[i],
                    digitalPositiveCalculator.calculateHouseholdType(testPrioset, testProfile) * householdPrio);
            Assert.assertEquals(animalScores[i],
                    digitalPositiveCalculator.calculateAnimal(testPrioset, testProfile) * data.getAnimals());
            Assert.assertEquals(childrenScores[i],
                    digitalPositiveCalculator.calculateChildren(testPrioset, testProfile) * data.getChildren());
        });
    }

    private Double getRoundedDouble(double expectedValue) {
        return BigDecimal.valueOf(expectedValue).setScale(1, RoundingMode.HALF_UP).doubleValue();
    }

    private PropertySearcherUserProfile generateTestUser(int i) {
        PropertySearcherUserProfile userProfile = new PropertySearcherUserProfile();

        userProfile.setData(generateUserProfile(i));

        return userProfile;
    }

    private PropertySearcherUserProfileData generateUserProfile(int i) {
        PropertySearcherUserProfileData profile = new PropertySearcherUserProfileData();
        profile.setProfession(generateProfession(i));
        profile.setHouseholdType(household[i]);
        profile.setResidents(residents[i]);
        profile.setDateOfBirth(Date.from(LocalDate.now()
                .minusYears(ages[i])
                .atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant()));
        profile.setAdditionalInformation(generateAdditionalInformation(i));
        return profile;
    }

    private AdditionalInformation generateAdditionalInformation(int i) {
        AdditionalInformation info = new AdditionalInformation();
        info.setAnimals(animals[i]);
        info.setWbs(wbs[i]);
        return info;
    }

    private Profession generateProfession(int i) {
        Profession profession = new Profession();
        profession.setType(job[i]);
        profession.setIncome(income[i]);

        return profession;
    }

    private Prioset generateTestPrioset() {
        Prioset testPrioset = new Prioset();
        testPrioset.setData(generateTestPriosetData());

        return testPrioset;
    }

    private PriosetData generateTestPriosetData() {
        PriosetData testPriosetData = new PriosetData();
        testPriosetData.setAge(new BoundaryValue(3, 25, 40));
        testPriosetData.setEmploymentType(new ListValue<>(4, Arrays.asList(
                EmploymentType.SELF_EMPLOYED,
                EmploymentType.EMPLOYED_UNLIMITED,
                EmploymentType.EMPLOYED_LIMITED)));
        testPriosetData.setResidents(new BoundaryValue(7, 0, 2));
        testPriosetData.setHouseholdType(new ListValue<>(1, Arrays.asList(
                HouseholdType.SINGLE,
                HouseholdType.COUPLE_WITHOUT_CHILDREN)));
        testPriosetData.setAnimals(5);
        testPriosetData.setChildren(4);
        testPriosetData.setMonthlyIncome(new BoundaryValue(10, 2, 5));
        testPriosetData.setWbs(false);

        return testPriosetData;
    }

    private PropertyData generateTestPropertyData() {
        PropertyData testData = new PropertyData();
        testData.setBasePrice(850.00);
        testData.setHeatingCost(100.00);
        testData.setServiceCharge(50.00);
        testData.setHeatingCostIncluded(false);

        return testData;
    }

    private CustomQuestionResponse generateSimplePositivGlobalCustomQuestionResponse() {
        CustomQuestion question = new CustomQuestion();
        question.setImportance(10);
        question.setDesiredResponses(Map.of("question", "true"));
        question.setType(CustomQuestionType.GLOBAL);
        JsonForm form = new JsonForm();
        Map<String, Object> formMap = new HashMap();
        formMap.put("key", "question");
        form.add(formMap);
        question.setForm(form);
        CustomQuestionResponse response = new CustomQuestionResponse();
        response.setCustomQuestion(question);
        JsonModel model = new JsonModel();
        model.put("question", "true");
        response.setData(model);

        return response;
    }
}
