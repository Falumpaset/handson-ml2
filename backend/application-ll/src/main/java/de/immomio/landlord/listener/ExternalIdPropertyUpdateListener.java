package de.immomio.landlord.listener;

import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.model.repository.shared.property.PropertyRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Niklas Lindemann
 * @implNote for further listeners depending on properties please use the LandlordPropertyListener.
 * The purpose of this listener is only handling setting the external id. Due to the entityManager.detach() call,
 * the entityManager loses the current transaction so every other listener would throw an Exception
 */

@Component
@RepositoryEventHandler(Property.class)
public class ExternalIdPropertyUpdateListener {

    private PropertyRepository propertyRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ExternalIdPropertyUpdateListener(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @HandleBeforeCreate
    @HandleBeforeSave
    public void beforeSave(Property property) {
        if (property.getId() != null) {
            // we have to detach the property because otherwise the findOne returns the passed one and not the old
            entityManager.detach(property);
            propertyRepository.findById(property.getId()).ifPresent(oldProperty -> {
                PropertyData oldData = oldProperty.getData();
                PropertyData newData = property.getData();

                if (StringUtils.isNotBlank(oldProperty.getExternalId()) && StringUtils.isBlank(property.getExternalId())) {
                    property.setExternalId(oldProperty.getExternalId());
                }
            });
        }
    }
}
