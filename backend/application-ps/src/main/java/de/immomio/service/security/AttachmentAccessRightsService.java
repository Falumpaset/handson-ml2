package de.immomio.service.security;

import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.bean.common.S3File;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Service
public class AttachmentAccessRightsService {

    public boolean isAttachmentAccessAllowed(S3File file, PropertySearcherUserProfile userProfile) {
        List<S3File> attachments = userProfile.getData().getAttachments();
        return attachments.stream().anyMatch(file::equals);
    }
}
