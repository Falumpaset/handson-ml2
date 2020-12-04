/**
 *
 */
package de.immomio.model.repository.landlord.customer.credential;

import de.immomio.data.landlord.entity.credential.Credential;

/**
 * @author Bastian Bliemeister.
 */
public interface LandlordCredentialRepositoryCustom {

    Credential customSave(Credential usage);

    void customDelete(Credential usage);

    Credential customFindOne(Long id);

}
