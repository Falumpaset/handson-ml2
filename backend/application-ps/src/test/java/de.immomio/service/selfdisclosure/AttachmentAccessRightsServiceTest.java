package de.immomio.service.selfdisclosure;

import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.service.security.AttachmentAccessRightsService;
import de.immomio.utils.TestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class AttachmentAccessRightsServiceTest {

    @Spy
    private AttachmentAccessRightsService accessRightsService;

    @Test
    public void isAttachmentAccessAllowedTrue() {
        S3File file = new S3File();
        List<S3File> attachments = new ArrayList<>();
        attachments.add(file);
        PropertySearcherUserProfile principal = TestHelper.generatePropertySearcherUser(TestHelper.generateCustomer()).getMainProfile();
        principal.getData().setAttachments(attachments);

        Assertions.assertTrue(accessRightsService.isAttachmentAccessAllowed(file, principal));
    }

    @Test
    public void isAttachmentAccessAllowedFalse() {
        S3File file = new S3File();
        List<S3File> attachments = new ArrayList<>();
        PropertySearcherUserProfile principal = TestHelper.generatePropertySearcherUser(TestHelper.generateCustomer()).getMainProfile();
        principal.getData().setAttachments(attachments);

        Assertions.assertFalse(accessRightsService.isAttachmentAccessAllowed(file, principal));
    }
}