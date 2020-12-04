package de.immomio.service.landlord.selfdisclosure;

import de.immomio.model.repository.service.shared.selfdisclosure.SelfDisclosureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SelfDisclosureService extends AbstractSelfDisclosureService<SelfDisclosureRepository> {

    @Autowired
    public SelfDisclosureService(SelfDisclosureRepository selfDisclosureRepository) {
       super(selfDisclosureRepository);
    }

}
