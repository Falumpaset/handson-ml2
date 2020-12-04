package de.immomio.controller.searchprofile;

import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherSearchProfile;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.propertysearcher.bean.searchprofile.SearchProfileBean;
import de.immomio.service.searchProfile.SearchProfileService;
import de.immomio.service.security.UserSecurityService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Maik Kingma
 */

@Slf4j
@RepositoryRestController
@RequestMapping(value = "/searchProfiles")
public class SearchProfileController {

    private final SearchProfileService searchProfileService;

    private final UserSecurityService userSecurityService;



    private static final String SEARCH_PROFILE_DELETED = "SEARCH_PROFILE_DELETED_L";

    @Autowired
    public SearchProfileController(
            SearchProfileService searchProfileService,
            UserSecurityService userSecurityService
    ) {

        this.searchProfileService = searchProfileService;
        this.userSecurityService = userSecurityService;
    }

    @PostMapping("/create")
    @PreAuthorize("#userProfile.user.id == principal?.id")
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "userProfile", schema = @Schema(type = "Long"), required = true)})
    public ResponseEntity createSearchProfile(
            @RequestParam("userProfile") PropertySearcherUserProfile userProfile,
            @RequestBody SearchProfileBean searchProfileBean
    ) {
        try {
            searchProfileService.create(searchProfileBean, userProfile);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{searchProfile}")
    @PreAuthorize("#searchProfile.userProfile.user.id == principal?.id")
    public ResponseEntity setDeletedFlag(@PathVariable("searchProfile") PropertySearcherSearchProfile searchProfile) {
        if (searchProfile.getDeleted()) {
            return new ResponseEntity<>(SEARCH_PROFILE_DELETED, HttpStatus.BAD_REQUEST);
        }
        searchProfileService.flagSearchProfileAsDeletedAndDeleteProposals(searchProfile);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity deleteAll() {
        searchProfileService.flagSearchProfilesAsDeletedAndDeleteProposals(userSecurityService.getPrincipalUserProfile());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/edit/{searchProfile}")
    @PreAuthorize("#searchProfile.userProfile.user.id == principal?.id")
    public ResponseEntity editSearchProfile(
            @PathVariable("searchProfile") PropertySearcherSearchProfile searchProfile,
            @RequestBody SearchProfileBean searchProfileBean
    ) {
        if (searchProfile.getDeleted()) {
            return new ResponseEntity<>(SEARCH_PROFILE_DELETED, HttpStatus.BAD_REQUEST);
        }
        try {
            searchProfileService.create(searchProfileBean, searchProfile.getUserProfile());
        } catch (ApiValidationException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        searchProfileService.flagSearchProfileAsDeletedAndDeleteProposals(searchProfile);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
