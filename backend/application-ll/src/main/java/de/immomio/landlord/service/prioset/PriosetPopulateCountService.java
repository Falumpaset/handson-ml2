package de.immomio.landlord.service.prioset;

import de.immomio.data.base.entity.AbstractEntity;
import de.immomio.data.landlord.bean.property.EntityCountBean;
import de.immomio.data.landlord.entity.prioset.Prioset;
import de.immomio.model.repository.shared.property.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Service
public class PriosetPopulateCountService {

    private final PropertyRepository propertyRepository;

    @Autowired
    public PriosetPopulateCountService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    void populatePriosetsWithCountData(List<Prioset> priosets) {
        if (!priosets.isEmpty()) {
            populatePriosetCount(priosets);
        }
    }

    public void populatePriosetWithCountData(Prioset prioset) {
        List<Prioset> priosets = Collections.singletonList(prioset);
        populatePriosetsWithCountData(priosets);
    }

    private void populatePriosetCount(List<Prioset> priosets) {
        List<EntityCountBean> sizeOfProperties = propertyRepository.getSizeOfProperties(priosets);
        priosets.forEach(prioset -> prioset.setSizeOfProperties(populateEntity(sizeOfProperties, prioset)));
    }

    private Long populateEntity(List<EntityCountBean> countBeans, AbstractEntity entity) {
        return countBeans.stream()
                .filter(propertyCountBean -> propertyEquals(entity, propertyCountBean))
                .map(EntityCountBean::getCount)
                .findFirst()
                .orElse(0L);
    }

    private boolean propertyEquals(AbstractEntity entity, EntityCountBean entityCountBean) {
        return entityCountBean.getId().equals(entity.getId());
    }
}
