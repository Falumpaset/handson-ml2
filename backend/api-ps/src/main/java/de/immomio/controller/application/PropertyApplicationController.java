package de.immomio.controller.application;

import de.immomio.beans.IdBean;
import de.immomio.beans.TokenBean;
import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.bean.application.PropertyApplicationBean;
import de.immomio.data.shared.bean.application.PropertySearcherPropertyApplicationBean;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.security.common.bean.ApplicationIntentToken;
import de.immomio.service.application.PropertySearcherPropertyApplicationService;
import de.immomio.service.propertySearcher.PropertySearcherUserService;
import de.immomio.service.security.UserSecurityService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping(value = "/applications")
public class PropertyApplicationController {

    private final PropertySearcherPropertyApplicationService propertyApplicationService;

    private final PropertySearcherUserService propertySearcherUserService;

    private PagedResourcesAssembler<PropertySearcherPropertyApplicationBean> pagedResourcesAssembler;

    private final UserSecurityService userSecurityService;

    @Autowired
    public PropertyApplicationController(PropertySearcherPropertyApplicationService propertyApplicationService,
            PropertySearcherUserService propertySearcherUserService,
            PagedResourcesAssembler<PropertySearcherPropertyApplicationBean> pagedResourcesAssembler,
            UserSecurityService userSecurityService) {
        this.propertyApplicationService = propertyApplicationService;
        this.propertySearcherUserService = propertySearcherUserService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.userSecurityService = userSecurityService;
    }

    @PreAuthorize("#userProfile.user.id == principal.id")
    @PostMapping(value = "/create")
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "userProfile", schema = @Schema(type = "Long"), required = true)})
    public ResponseEntity<PropertySearcherPropertyApplicationBean> createApplication(@Parameter(hidden = true) PersistentEntityResourceAssembler assembler,
            @RequestParam("userProfile") PropertySearcherUserProfile userProfile,
            @RequestParam("property") Long propertyId,
            @RequestParam(value = "shortUrlToken", required = false) String shortUrlToken) {
        try {

            PropertySearcherPropertyApplicationBean application = propertyApplicationService.createNewApplication(propertyId, userProfile, shortUrlToken);

            return ResponseEntity.ok(application);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "/declareIntentNoAuth")
    public ResponseEntity<Void> declareIntentNoAuth(@RequestParam("intent") ApplicationStatus intent, @RequestBody TokenBean tokenBean) {
        ApplicationIntentToken applicationIntentToken = propertySearcherUserService.extractDataFromApplicationToken(tokenBean.getToken());
        propertyApplicationService.updateIntentOfApplication(applicationIntentToken.getApplicationId(), intent);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("#application.userProfile.user.id == principal.id")
    @PostMapping(value = "/declareIntent")
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "application", schema = @Schema(type = "Long"), required = true)})
    public ResponseEntity<Void> declareIntent(@RequestParam("application") PropertyApplication application,
            @RequestParam("intent") ApplicationStatus intent) {
        propertyApplicationService.updateIntentOfApplication(application, intent);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("#userProfile.user.id == principal.id")
    @GetMapping("/search/findByUserProfile")
    @SuppressWarnings("unchecked")
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "userProfile", schema = @Schema(type = "Long"), required = true)})
    public ResponseEntity<PagedModel<EntityModel<PropertySearcherPropertyApplicationBean>>> findByUser(@RequestParam("userProfile") PropertySearcherUserProfile userProfile,
            Pageable pageable) {
        Page<PropertySearcherPropertyApplicationBean> applications = propertyApplicationService.findByUserProfile(userProfile, pageable);
        PagedModel<EntityModel<PropertySearcherPropertyApplicationBean>> pagedModel = pagedResourcesAssembler.toModel(applications);
        return ResponseEntity.ok(pagedModel);
    }

    @PreAuthorize("#userProfile.user.id == principal.id")
    @GetMapping("/search/findAvailableForNewConversation")
    @SuppressWarnings("unchecked")
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "userProfile", schema = @Schema(type = "Long"), required = true)})
    public ResponseEntity<PagedModel<EntityModel<PropertySearcherPropertyApplicationBean>>> findAvailableForConversation(@RequestParam("userProfile") PropertySearcherUserProfile userProfile) {
        Page<PropertySearcherPropertyApplicationBean> applications = propertyApplicationService.findAvailableForConversation(userProfile);
        PagedModel<EntityModel<PropertySearcherPropertyApplicationBean>> pagedModel = pagedResourcesAssembler.toModel(applications);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/search/findApplicationsWithRentedFlatsById")
    @SuppressWarnings("unchecked")
    public ResponseEntity<PagedModel<EntityModel<PropertySearcherPropertyApplicationBean>>> findApplicationsWithRentedFlatsById() {
        Page<PropertySearcherPropertyApplicationBean> applications = propertyApplicationService.findApplicationsWithRentedFlatsById();
        propertySearcherUserService.setRentedApplicationsFetched(userSecurityService.getPrincipalUserProfile());
        PagedModel<EntityModel<PropertySearcherPropertyApplicationBean>> pagedModel = pagedResourcesAssembler.toModel(applications);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{applicationId}")
    @PreAuthorize("@userSecurityService.isUserAllowedToReadApplication(#applicationId)")
    public ResponseEntity<PropertySearcherPropertyApplicationBean> findOne(@PathVariable Long applicationId, @Parameter(hidden = true) PersistentEntityResourceAssembler assembler) {
        PropertySearcherPropertyApplicationBean result = propertyApplicationService.findApplicationBeanById(applicationId);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("#application.userProfile.user.id == principal.id")
    @DeleteMapping("/{application}")
    public ResponseEntity<Void> delete(@PathVariable(name = "application") PropertyApplication application) {
        propertyApplicationService.delete(application);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/exists")
    public ResponseEntity<IdBean> exists(@RequestParam("property") Long propertyId) {
        IdBean idBean = propertyApplicationService.exists(userSecurityService.getPrincipalUserProfile(), propertyId);
        return ResponseEntity.ok(idBean);
    }
}
