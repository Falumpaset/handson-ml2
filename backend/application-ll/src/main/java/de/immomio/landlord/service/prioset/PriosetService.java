package de.immomio.landlord.service.prioset;

import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.landlord.bean.prioset.ChangePriosetBean;
import de.immomio.data.landlord.bean.prioset.PriosetData;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.prioset.Prioset;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.landlord.service.customquestion.LandlordCustomQuestionAssociationService;
import de.immomio.landlord.service.sender.LandlordPropertyScoreRefreshSender;
import de.immomio.model.repository.landlord.customer.prioset.LandlordPriosetRepository;
import de.immomio.model.repository.shared.property.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author Niklas Lindemann
 */

@Service
public class PriosetService {

    private static final String NO_TEMPLATE = "PRIOSET_IS_NO_TEMPLATE_L";

    private static final String REPLACE_PRIOSET_WITH_ITSELF = "REPLACE_PRIOSET_WITH_ITSELF_L";

    private static final String DEFAULT_PRIOSET = "Replaced default prioset";
    public static final String PRIOSET_DATA_MISSING_L = "PRIOSET_DATA_MISSING_L";
    public static final String PRIOSET_DESCRIPTION_MISSING_L = "PRIOSET_DESCRIPTION_MISSING_L";
    public static final String PRIOSET_NAME_MISSING_L = "PRIOSET_NAME_MISSING_L";

    private LandlordPriosetRepository priosetRepository;

    private PropertyRepository propertyRepository;

    private PriosetPopulateCountService populateCountService;

    private final LandlordCustomQuestionAssociationService customQuestionAssociationService;

    private final LandlordPropertyScoreRefreshSender scoreRefreshSender;

    @Autowired
    public PriosetService(LandlordPriosetRepository priosetRepository, PropertyRepository propertyRepository,
            PriosetPopulateCountService populateCountService,
            LandlordCustomQuestionAssociationService customQuestionAssociationService, LandlordPropertyScoreRefreshSender scoreRefreshSender) {
        this.priosetRepository = priosetRepository;
        this.propertyRepository = propertyRepository;
        this.populateCountService = populateCountService;
        this.customQuestionAssociationService = customQuestionAssociationService;
        this.scoreRefreshSender = scoreRefreshSender;
    }

    public void delete(Prioset prioset, Prioset priosetToReplace, LandlordCustomer customer) {
        if (!prioset.getTemplate()) {
            throw new ApiValidationException(NO_TEMPLATE);
        }

        replacePrioset(prioset, priosetToReplace, customer);

        priosetRepository.delete(prioset);
    }

    public List<Property> replacePrioset(Prioset prioset, Prioset priosetToReplace, LandlordCustomer customer) {
        if (priosetToReplace != null && Objects.equals(prioset, priosetToReplace)) {
            throw new ApiValidationException(REPLACE_PRIOSET_WITH_ITSELF);
        }

        List<Property> properties = prioset.getProperties();
        properties.forEach(property -> {
            if (priosetToReplace != null) {
                property.setPrioset(priosetToReplace);
            } else {
                property.setPrioset(createDefaultPrioset(customer));
            }
        });
        return propertyRepository.saveAll(properties);
    }

    public Prioset create(ChangePriosetBean newPriosetBean, LandlordCustomer customer) {
        if (newPriosetBean.getName() == null) {
            throw new ApiValidationException(PRIOSET_NAME_MISSING_L);
        }
        if (newPriosetBean.getDescription() == null) {
            throw new ApiValidationException(PRIOSET_DESCRIPTION_MISSING_L);
        }

        Prioset prioset = new Prioset();
        prioset.setName(newPriosetBean.getName());
        prioset.setDescription(newPriosetBean.getDescription());
        prioset.setTemplate(newPriosetBean.isTemplate());
        prioset.setLocked(false);
        prioset.setCustomer(customer);
        prioset.setData(newPriosetBean.getData());

        priosetRepository.save(prioset);

        customQuestionAssociationService.mergeCustomQuestionAssociations(prioset,
                newPriosetBean.getCustomQuestionAssociations());

        return prioset;
    }

    @Transactional
    public Prioset update(Prioset prioset, ChangePriosetBean updatePriosetBean) {
        prioset.setName(updatePriosetBean.getName());
        prioset.setDescription(updatePriosetBean.getDescription());
        prioset.setData(updatePriosetBean.getData());

        prioset = priosetRepository.save(prioset);

        customQuestionAssociationService.mergeCustomQuestionAssociations(prioset,
                updatePriosetBean.getCustomQuestionAssociations());

        updateScores(prioset);

        return prioset;
    }

    public Prioset get(Prioset prioset) {
        return prioset;
    }

    public Page<Prioset> findByTemplate(Boolean template, Pageable pageable) {
        Page<Prioset> priosets = priosetRepository.findByTemplate(template, pageable);
        populateCountService.populatePriosetsWithCountData(priosets.getContent());
        return priosets;
    }

    public Prioset duplicatePrioset(Prioset prioset) {
        Prioset newPrioset = new Prioset();
        newPrioset.setCustomer(prioset.getCustomer());
        newPrioset.setData(prioset.getData());
        newPrioset.setDescription(prioset.getDescription());
        newPrioset.setLocked(prioset.getLocked());
        newPrioset.setName(prioset.getName());
        newPrioset.setTemplate(prioset.getTemplate());

        return priosetRepository.save(newPrioset);
    }

    private void updateScores(Prioset prioset) {
        if (!prioset.getTemplate()) {
            scoreRefreshSender.sendUpdateMessage(prioset);
        }
    }

    private Prioset createDefaultPrioset(LandlordCustomer customer) {
        Prioset prioset = new Prioset();
        prioset.setName(DEFAULT_PRIOSET);
        prioset.setCustomer(customer);
        prioset.setTemplate(false);
        prioset.setLocked(false);
        prioset.setData(new PriosetData());
        prioset = priosetRepository.save(prioset);

        return prioset;
    }
}
