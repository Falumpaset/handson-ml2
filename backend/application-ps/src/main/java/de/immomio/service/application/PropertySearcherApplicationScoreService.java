package de.immomio.service.application;

import de.immomio.data.base.bean.score.ScoreBean;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.service.calculator.PropertySearcherCalculatorDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */
@Service
public class PropertySearcherApplicationScoreService {

    private PropertySearcherCalculatorDelegate calculatorDelegate;

    @Autowired
    public PropertySearcherApplicationScoreService(PropertySearcherCalculatorDelegate calculatorDelegate) {
        this.calculatorDelegate = calculatorDelegate;
    }

    public void setScoreForApplication(PropertyApplication application) {
        Property property = application.getProperty();
        PropertySearcherUserProfile userProfile = application.getUserProfile();
        ScoreBean scoreBean = calculatorDelegate.calculateScore(property, userProfile);

        application.setCustomQuestionScore(scoreBean.getCustomQuestionScore());
        application.setScore(scoreBean.getScore().doubleValue());
    }

}
