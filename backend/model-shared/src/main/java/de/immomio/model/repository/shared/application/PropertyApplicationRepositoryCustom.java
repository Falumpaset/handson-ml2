package de.immomio.model.repository.shared.application;

import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;

/**
 * @author Maik Kingma
 */

public interface PropertyApplicationRepositoryCustom {

    PropertyApplication customSave(PropertyApplication propertyApplication);

    PropertyApplication customFindOne(Long id);
}
