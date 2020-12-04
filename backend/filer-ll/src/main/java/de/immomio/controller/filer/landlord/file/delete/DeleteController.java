package de.immomio.controller.filer.landlord.file.delete;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.shared.bean.common.S3DeleteResponse;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.landlord.service.security.PropertyAccessRightsService;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.service.LandlordAsyncDeleteFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Controller
public class DeleteController {

    private final LandlordAsyncDeleteFileService landlordAsyncDeleteFileService;

    private final UserSecurityService securityService;

    private final PropertyAccessRightsService propertyAccessRightsService;

    @Autowired
    public DeleteController(
            LandlordAsyncDeleteFileService landlordAsyncDeleteFileService,
            UserSecurityService securityService,
            PropertyAccessRightsService propertyAccessRightsService) {
        this.landlordAsyncDeleteFileService = landlordAsyncDeleteFileService;
        this.securityService = securityService;
        this.propertyAccessRightsService = propertyAccessRightsService;
    }

    @PostMapping("/delete/{property}")
    public ResponseEntity deleteFile(
            @PathVariable("property") Property property,
            @RequestBody List<S3File> files
    ) {
        LandlordUser principal = securityService.getPrincipalUser();
        boolean allowDeletion = propertyAccessRightsService.isPropertyAccessAllowed(property, principal);
        if (!allowDeletion) {
            return new ResponseEntity<>("ACCESS_DENIED_L", HttpStatus.UNAUTHORIZED);
        }

        S3DeleteResponse response = landlordAsyncDeleteFileService.deleteFiles(property, files);
        if (response.getFailure().isEmpty()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
