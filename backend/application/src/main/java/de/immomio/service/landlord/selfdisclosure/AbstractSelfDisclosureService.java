package de.immomio.service.landlord.selfdisclosure;

import de.immomio.beans.shared.selfdisclosure.SelfDisclosureBean;
import de.immomio.beans.shared.selfdisclosure.SelfDisclosureQuestionBean;
import de.immomio.beans.shared.selfdisclosure.SelfDisclosureSubQuestionBean;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosure;
import de.immomio.model.repository.core.shared.selfdisclosure.BaseSelfDisclosureRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.stream.Collectors;

public abstract class AbstractSelfDisclosureService<SDR extends BaseSelfDisclosureRepository> {

    public static final String SELF_DISCLOSURE_NOT_FOUND_L = "SELF_DISCLOSURE_NOT_FOUND_L";
    protected SDR selfDisclosureRepository;

    private SelfDisclosureDataConverter selfDisclosureDataConverter;

    public AbstractSelfDisclosureService(SDR selfDisclosureRepository) {
        this.selfDisclosureRepository = selfDisclosureRepository;
    }

    @Autowired
    public void setSelfDisclosureConverter(SelfDisclosureDataConverter selfDisclosureDataConverter) {
        this.selfDisclosureDataConverter = selfDisclosureDataConverter;
    }

    public void create(LandlordCustomer customer) {
        if (customer.getSelfDisclosure() == null) {
            SelfDisclosure selfDisclosure = new SelfDisclosure();
            selfDisclosure.setCustomer(customer);

            selfDisclosureRepository.save(selfDisclosure);
        }
    }

    public SelfDisclosureBean mergeInSelfDisclosureBean(SelfDisclosure selfDisclosure) {
        if (selfDisclosure == null) {
            throw new ApiValidationException(SELF_DISCLOSURE_NOT_FOUND_L);
        }
        SelfDisclosureBean selfDisclosureBean = selfDisclosureDataConverter.selfDisclosureToBean(selfDisclosure);
        selfDisclosureBean.setQuestions(selfDisclosureBean.getQuestions()
                .stream()
                .sorted(Comparator.comparing(SelfDisclosureQuestionBean::getOrderNumber))
                .collect(Collectors.toList()));
        selfDisclosureBean.getQuestions().forEach(selfDisclosureQuestionBean -> {
            selfDisclosureQuestionBean.setSubQuestions(selfDisclosureQuestionBean.getSubQuestions()
                    .stream()
                    .sorted(Comparator.comparing(SelfDisclosureSubQuestionBean::getOrderNumber))
                    .collect(Collectors.toList()));
        });
        return selfDisclosureBean;
    }

}
