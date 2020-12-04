package de.immomio.landlord.service.selfdisclosure;

import de.immomio.beans.shared.selfdisclosure.SelfDisclosureQuestionBean;
import de.immomio.model.repository.shared.selfdisclosure.SelfDisclosureQuestionRepository;
import de.immomio.service.landlord.selfdisclosure.SelfDisclosureDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */

@Service
public class LandlordSelfDisclosureQuestionService {

    private final SelfDisclosureQuestionRepository questionRepository;

    private final SelfDisclosureDataConverter selfDisclosureDataConverter;

    @Autowired
    public LandlordSelfDisclosureQuestionService(SelfDisclosureQuestionRepository questionRepository,
            SelfDisclosureDataConverter selfDisclosureDataConverter) {
        this.questionRepository = questionRepository;
        this.selfDisclosureDataConverter = selfDisclosureDataConverter;
    }


    public List<SelfDisclosureQuestionBean> getQuestionBeans() {
        return questionRepository.findAll().stream()
                .map(selfDisclosureDataConverter::selfDisclosureQuestionToBean)
                .collect(Collectors.toList());
    }

}
