package de.immomio.service.propertysearcher;

import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.model.repository.core.propertysearcher.user.BasePropertySearcherUserProfileRepository;
import de.immomio.model.repository.core.propertysearcher.user.BasePropertySearcherUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Service
public class PropertySearcherUserService extends AbstractPropertySearcherUserService {

    private final BasePropertySearcherUserProfileRepository userProfileRepository;

    @Autowired
    public PropertySearcherUserService(BasePropertySearcherUserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public List<PropertySearcherUserProfile> findAll() {
        return userProfileRepository.findAll();
    }

}
