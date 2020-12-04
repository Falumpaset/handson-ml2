package de.immomio.service.propertysearcher;

import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * @author Niklas Lindemann
 */
@Service
public class PropertySearcherOfflineUserService {

    private static final String OFFLINE_EMAIL_PATTERN = "assistent\\+*.*@immomio\\.de";
    private static final Pattern COMPILED_OFFLINE_EMAIL_PATTERN = Pattern.compile(OFFLINE_EMAIL_PATTERN);

    public boolean isOfflineProfile(PropertySearcherUserProfile userProfile) {
        return COMPILED_OFFLINE_EMAIL_PATTERN.matcher(userProfile.getEmail()).matches();
    }

}
