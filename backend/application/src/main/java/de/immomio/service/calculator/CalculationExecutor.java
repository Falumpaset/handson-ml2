package de.immomio.service.calculator;

import de.immomio.data.base.bean.score.CustomQuestionScoreBean;
import de.immomio.data.base.bean.score.ScoreBean;
import de.immomio.data.landlord.entity.customquestion.CustomQuestion;
import de.immomio.data.landlord.entity.customquestion.PriosetCustomQuestionAssociation;
import de.immomio.data.landlord.entity.prioset.Prioset;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.common.customQuestion.CustomQuestionResponse;
import de.immomio.service.calculator.implementation.PreferencePriosetCalculator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * @author Johannes Hiemer, Maik Kingma.
 */
@Slf4j
@Service
public class CalculationExecutor {

    private final Calculator calculator;

    private final PreferencePriosetCalculator preferencePriosetCalculator;

    @Autowired
    public CalculationExecutor(
            Calculator calculator,
            PreferencePriosetCalculator preferencePriosetCalculator
    ) {
        this.calculator = calculator;
        this.preferencePriosetCalculator = preferencePriosetCalculator;
    }

    public ScoreBean calculateScore(
            Property property,
            PropertySearcherUserProfile userProfile,
            List<CustomQuestionResponse> responses,
            List<PriosetCustomQuestionAssociation> customQuestionAssociations,
            List<CustomQuestion> globalQuestions
    ) {
        Prioset prioset = property.getPrioset();
        ScoreBean scoreBean = new ScoreBean();

        //TODO 20.07 new object type nullpointer exception fix -> should there be an error for prioset null?
        if (userProfile.isAnonymous() || prioset == null) {
            return scoreBean;
        }
        try {

            /*
             * normal score
             */
            BigDecimal calculatedCQScore = BigDecimal.ZERO;
            BigDecimal calculatedPSScore = BigDecimal.ZERO;
            int priosetScore = preferencePriosetCalculator.calculatePreferencePrioset(prioset);

            BigDecimal priosetScoreBD = BigDecimal.valueOf(priosetScore);
            if (priosetScore > 0) {
                BigDecimal result = calculator.calculate(
                        prioset,
                        userProfile.getData(),
                        property,
                        priosetScore);
                calculatedPSScore = calculateFinalPartialScore(priosetScoreBD, result);
            }

            /*
             * custom questions score
             */
            int sumImportances = getImportances(customQuestionAssociations, globalQuestions);

            if (sumImportances > 0 && wbsMatches(userProfile, prioset)) {
                CustomQuestionScoreBean customQuestionScoreBean = calculateCustomQuestionScore(property, responses, sumImportances);
                scoreBean.setCustomQuestionScore(customQuestionScoreBean);
                calculatedCQScore = customQuestionScoreBean.getScoreIncludingRange();
            }
            if (calculatedCQScore == null) {
                calculatedCQScore = BigDecimal.ZERO;
            }
            /*
             * combined score
             */

            if (sumImportances == 0 && priosetScore == 0 && wbsMatches(userProfile, prioset)) {
                scoreBean.setScore(BigDecimal.TEN);
                return scoreBean;
            }

            BigDecimal totalScoreDistribution = priosetScoreBD.add(BigDecimal.valueOf(sumImportances));

            BigDecimal percentileCQ;

            if (totalScoreDistribution.equals(BigDecimal.ZERO)) {
                percentileCQ = BigDecimal.ONE;
            } else {
                percentileCQ = BigDecimal.valueOf(sumImportances)
                        .divide(totalScoreDistribution, 2, RoundingMode.HALF_UP);
            }

            BigDecimal weightedCQScore = calculatedCQScore.multiply(percentileCQ);

            BigDecimal percentilePS = BigDecimal.ONE.subtract(percentileCQ);

            BigDecimal weightedPSScore = calculatedPSScore.multiply(percentilePS);

            scoreBean.setScore(weightedCQScore.add(weightedPSScore).setScale(2, RoundingMode.HALF_UP));
            return scoreBean;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new ScoreBean();
    }

    public int getImportances(List<PriosetCustomQuestionAssociation> customQuestionAssociations, List<CustomQuestion> globalQuestions) {
        int associationImportance = customQuestionAssociations.stream()
                .mapToInt(PriosetCustomQuestionAssociation::getImportance)
                .sum();
        int customQuestionImportance = globalQuestions.stream().mapToInt(CustomQuestion::getImportance).sum();
        return associationImportance + customQuestionImportance;
    }

    private BigDecimal calculateFinalPartialScore(BigDecimal divisor, BigDecimal result) {
        return result.divide(divisor, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.TEN);
    }

    public CustomQuestionScoreBean calculateCustomQuestionScore(
            Property property,
            List<CustomQuestionResponse> responses,
            int sumImportances
    ) {
        CustomQuestionScoreBean result = calculator.evaluateCustomQuestions(responses, property);
        if (result == null) {
            return CustomQuestionScoreBean.builder().build();
        }
        if (sumImportances > 0) {
            BigDecimal customQuestionScoreBD = BigDecimal.valueOf(sumImportances);
            result.setScoreExcludingRange(calculateFinalPartialScore(customQuestionScoreBD, result.getScoreExcludingRange()));
            result.setScoreIncludingRange(calculateFinalPartialScore(customQuestionScoreBD, result.getScoreIncludingRange()));
            return result;
        }
        return CustomQuestionScoreBean.builder().build();
    }

    private boolean wbsMatches(PropertySearcherUserProfile userProfile, Prioset prioset) {
        Boolean priosetWbs = prioset.getData().getWbs();
        if (BooleanUtils.isFalse(priosetWbs)) {
            return true;
        }

        return userProfile.getData() != null &&
                userProfile.getData().getAdditionalInformation() != null &&
                BooleanUtils.isTrue(userProfile.getData().getAdditionalInformation().getWbs());
    }
}
