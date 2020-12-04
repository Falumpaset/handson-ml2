package de.immomio.landlord.service.property.portal;

import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.landlord.entity.credential.Credential;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.portal.PropertyPortal;
import de.immomio.data.landlord.entity.property.portal.PropertyPortalState;
import de.immomio.model.repository.landlord.customer.credential.LandlordCredentialRepository;
import de.immomio.model.repository.landlord.customer.property.portal.LandlordPropertyPortalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropertyPortalService {

    public static final String CREDENTIAL_ID_NOT_AUTHORIZED_L = "CREDENTIAL_ID_NOT_AUTHORIZED_L";
    public static final String ACTIVE_PORTAL_CANNOT_BE_REMOVED_L = "ACTIVE_PORTAL_CANNOT_BE_REMOVED_L";
    private final LandlordPropertyPortalRepository propertyPortalRepository;

    private final LandlordCredentialRepository credentialRepository;

    @Autowired
    public PropertyPortalService(LandlordPropertyPortalRepository propertyPortalRepository,
            LandlordCredentialRepository credentialRepository) {
        this.propertyPortalRepository = propertyPortalRepository;
        this.credentialRepository = credentialRepository;
    }

    public void mergePropertyPortals(Property property, List<Long> credentialIdChanges) {
        if (!credentialIdChanges.containsAll(property.getPortals()
                .stream()
                .filter(PropertyPortal::isActivated)
                .map(propertyPortal -> propertyPortal.getCredentials().getId())
                .collect(Collectors.toList()))) {
            throw new ApiValidationException(ACTIVE_PORTAL_CANNOT_BE_REMOVED_L);
        }

        propertyPortalRepository.deleteAll(getPortalsToDelete(property, credentialIdChanges));
        propertyPortalRepository.saveAll(getPortalsToSave(property, credentialIdChanges));

        property.setPortals(propertyPortalRepository.findAllByProperty(property));
    }

    private List<PropertyPortal> getPortalsToDelete(Property property, List<Long> credentialIdChanges) {
        return property.getPortals()
                .stream()
                .filter(propertyPortal -> !credentialIdChanges.contains(propertyPortal.getCredentials().getId()))
                .collect(Collectors.toList());
    }

    private List<PropertyPortal> getPortalsToSave(Property property, List<Long> credentialIdChanges) {
        List<Long> propertyPortalCredentialIds = property.getPortals()
                .stream()
                .map(propertyPortal -> propertyPortal.getCredentials().getId())
                .collect(Collectors.toList());

        List<Credential> credentials = credentialRepository.findAllById(credentialIdChanges.stream()
                .filter(credentialId -> !propertyPortalCredentialIds.contains(credentialId))
                .collect(Collectors.toList()));
        if (credentials.stream().anyMatch(credential -> !property.getCustomer().equals(credential.getCustomer()))) {
            throw new ApiValidationException(CREDENTIAL_ID_NOT_AUTHORIZED_L);
        }

        return credentials.stream().map(credential -> {
            PropertyPortal propertyPortal = new PropertyPortal();
            propertyPortal.setCredentials(credential);
            propertyPortal.setPortal(credential.getPortal());
            propertyPortal.setProperty(property);
            propertyPortal.setState(PropertyPortalState.DEACTIVATED);
            return propertyPortal;
        }).collect(Collectors.toList());
    }
}
