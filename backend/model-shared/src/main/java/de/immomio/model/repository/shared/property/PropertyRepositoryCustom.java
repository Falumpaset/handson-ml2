/**
 *
 */
package de.immomio.model.repository.shared.property;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.LandlordUser;

import java.util.List;

/**
 * @author Bastian Bliemeister, Maik Kingma
 */
public interface PropertyRepositoryCustom {

    void customSave(Property property);

    Property customFindOne(Long id);

    List<Property> customFindByUser(LandlordUser user);

    void customSave(List<Property> properties);

    void setApplicationsViewed(Long id);
}
