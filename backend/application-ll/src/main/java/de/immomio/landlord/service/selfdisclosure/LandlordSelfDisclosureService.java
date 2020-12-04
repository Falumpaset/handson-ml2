package de.immomio.landlord.service.selfdisclosure;

import de.immomio.beans.shared.selfdisclosure.SelfDisclosureBean;
import de.immomio.beans.shared.selfdisclosure.SelfDisclosureQuestionBean;
import de.immomio.beans.shared.selfdisclosure.SelfDisclosureResponseBean;
import de.immomio.beans.shared.selfdisclosure.SelfDisclosureSubQuestionBean;
import de.immomio.constants.exceptions.ApiNotFoundException;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosure;
import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosureQuestionConfiguration;
import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosureSubQuestion;
import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosureSubQuestionConfiguration;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.landlord.service.property.PropertyService;
import de.immomio.model.repository.shared.selfdisclosure.SelfDisclosureQuestionConfigurationRepository;
import de.immomio.model.repository.shared.selfdisclosure.SelfDisclosureQuestionRepository;
import de.immomio.model.repository.shared.selfdisclosure.SelfDisclosureRepository;
import de.immomio.model.repository.shared.selfdisclosure.SelfDisclosureResponseRepository;
import de.immomio.model.repository.shared.selfdisclosure.SelfDisclosureSubQuestionConfigurationRepository;
import de.immomio.model.repository.shared.selfdisclosure.SelfDisclosureSubQuestionRepository;
import de.immomio.service.landlord.selfdisclosure.AbstractSelfDisclosureService;
import de.immomio.service.landlord.selfdisclosure.SelfDisclosureDataConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LandlordSelfDisclosureService extends AbstractSelfDisclosureService<SelfDisclosureRepository> {

    private final SelfDisclosureRepository selfDisclosureRepository;

    private final SelfDisclosureResponseRepository selfDisclosureResponseRepository;

    private final PropertyService propertyService;

    private final SelfDisclosureSubQuestionRepository subQuestionRepository;

    private final SelfDisclosureSubQuestionConfigurationRepository subQuestionConfigRepository;

    private final SelfDisclosureQuestionRepository questionRepository;

    private final SelfDisclosureQuestionConfigurationRepository questionConfigurationRepository;

    private final SelfDisclosureDataConverter selfDisclosureDataConverter;

    @Autowired
    public LandlordSelfDisclosureService(
            SelfDisclosureRepository selfDisclosureRepository,
            SelfDisclosureResponseRepository selfDisclosureResponseRepository,
            PropertyService propertyService,
            SelfDisclosureSubQuestionRepository subQuestionRepository,
            SelfDisclosureSubQuestionConfigurationRepository subQuestionConfigRepository,
            SelfDisclosureQuestionRepository questionRepository,
            SelfDisclosureQuestionConfigurationRepository questionConfigurationRepository,
            SelfDisclosureDataConverter selfDisclosureDataConverter) {
        super(selfDisclosureRepository);
        this.selfDisclosureRepository = selfDisclosureRepository;
        this.selfDisclosureResponseRepository = selfDisclosureResponseRepository;
        this.propertyService = propertyService;
        this.subQuestionRepository = subQuestionRepository;
        this.subQuestionConfigRepository = subQuestionConfigRepository;
        this.questionRepository = questionRepository;
        this.questionConfigurationRepository = questionConfigurationRepository;
        this.selfDisclosureDataConverter = selfDisclosureDataConverter;
    }

    public void create(LandlordCustomer customer) {
        super.create(customer);

        // auto associate all customer's properties to the module
        propertyService.setShowSelfDisclosureQuestionsFlag(customer);
    }

    public void delete(LandlordCustomer customer) {
        selfDisclosureRepository.delete(customer.getSelfDisclosure());
        propertyService.unsetShowSelfDisclosureQuestionsFlag(customer);
    }

    public SelfDisclosure mergeAndSave(SelfDisclosureBean selfDisclosureBean, SelfDisclosure selfDisclosure) {

        selfDisclosure.setFeedbackEmail(selfDisclosureBean.getFeedbackEmail());
        selfDisclosure.setDescription(selfDisclosureBean.getDescription());
        selfDisclosure.setDocuments(selfDisclosureBean.getDocuments());
        selfDisclosure.setConfirmations(selfDisclosureBean.getConfirmations());

        List<SelfDisclosureQuestionConfiguration> configs = selfDisclosureBean.getQuestions()
                .stream()
                .map(selfDisclosureQuestionBean -> {
                    SelfDisclosureQuestionConfiguration questionConfig = selfDisclosure.getQuestionsConfigs().stream()
                            .filter(selfDisclosureQuestionConfig -> selfDisclosureQuestionConfig.getSelfDisclosureQuestionId()
                                    .equals(selfDisclosureQuestionBean.getId()))
                            .findFirst()
                            .orElseGet(() -> newConfig(selfDisclosure, selfDisclosureQuestionBean));
                    questionConfig.setHidden(BooleanUtils.isTrue(selfDisclosureQuestionBean.isHidden()));
                    questionConfig.setMandatory(BooleanUtils.isTrue(selfDisclosureQuestionBean.isMandatory()));
                    setSubQuestions(questionConfig, selfDisclosureQuestionBean.getSubQuestions());
                    return questionConfigurationRepository.save(questionConfig);

                }).collect(Collectors.toList());

        selfDisclosure.getQuestionsConfigs().addAll(configs);

        return selfDisclosureRepository.save(selfDisclosure);
    }

    public SelfDisclosureResponseBean findResponse(Property property, PropertySearcherUserProfile userProfile) {
        return selfDisclosureResponseRepository.findByPropertyIdAndUserProfileId(property.getId(), userProfile.getId())
                .map(selfDisclosureDataConverter::selfDisclosureResponseToBean)
                .orElseThrow(ApiNotFoundException::new);
    }

    private SelfDisclosureQuestionConfiguration newConfig(SelfDisclosure selfDisclosure, SelfDisclosureQuestionBean selfDisclosureQuestionBean) {
        SelfDisclosureQuestionConfiguration newQuestionConfig = new SelfDisclosureQuestionConfiguration();
        newQuestionConfig.setSelfDisclosure(selfDisclosure);
        newQuestionConfig.setQuestion(questionRepository.findById(selfDisclosureQuestionBean.getId()).orElseThrow());
        return questionConfigurationRepository.save(newQuestionConfig);
    }

    private void setSubQuestions(SelfDisclosureQuestionConfiguration selfDisclosureQuestionConfiguration,
            List<SelfDisclosureSubQuestionBean> subQuestionBeans) {
        List<SelfDisclosureSubQuestionConfiguration> subQuestionConfigs = subQuestionBeans.stream().map(subQuestionBean -> {
            SelfDisclosureSubQuestion subQuestion = subQuestionRepository.findById(subQuestionBean.getId()).orElse(null);
            if (subQuestion == null) {
                return null;
            }

            SelfDisclosureSubQuestionConfiguration subQuestionConfiguration = subQuestionConfigRepository.findByQuestionConfigurationAndSubQuestion(
                    selfDisclosureQuestionConfiguration, subQuestion).orElseGet(() -> {
                SelfDisclosureSubQuestionConfiguration selfDisclosureSubQuestionConfiguration = new SelfDisclosureSubQuestionConfiguration();
                selfDisclosureSubQuestionConfiguration.setQuestionConfiguration(selfDisclosureQuestionConfiguration);
                selfDisclosureSubQuestionConfiguration.setSubQuestion(subQuestion);
                return subQuestionConfigRepository.save(selfDisclosureSubQuestionConfiguration);
            });

            subQuestionConfiguration.setHidden(BooleanUtils.isTrue(subQuestionBean.getHidden()));
            subQuestionConfiguration.setMandatory(BooleanUtils.isTrue(subQuestionBean.getMandatory()));
            return subQuestionConfiguration;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        selfDisclosureQuestionConfiguration.getSubQuestions().addAll(subQuestionConfigs);
    }

}
