package de.immomio.controller.property;

import de.immomio.beans.landlord.UpdatePropertyBean;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.constants.exceptions.NotAuthorizedException;
import de.immomio.controller.BaseController;
import de.immomio.controller.paging.CustomPageable;
import de.immomio.controller.search.property.SearchProperty;
import de.immomio.data.base.type.property.PropertyStatus;
import de.immomio.data.landlord.bean.property.PropertySendExposeBean;
import de.immomio.data.landlord.bean.property.PropertyShortBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.portal.PropertyPortal;
import de.immomio.data.landlord.entity.property.portal.PropertyPortalState;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.shared.bean.tenant.PropertyTenantBean;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.landlord.service.property.PropertyService;
import de.immomio.landlord.service.property.PropertyUpdatingService;
import de.immomio.landlord.service.property.notification.PropertyNotificationService;
import de.immomio.landlord.service.property.tenant.PropertyTenantService;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.model.repository.shared.property.PropertyRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Maik Kingma
 */

@Slf4j
@RepositoryRestController
@RequestMapping(value = "/properties")
public class PropertyController extends BaseController {

    private static final String AT_LEAST_ONE_PORTAL_STATE_NOT_EQUALS_DEACTIVATED =
            "AT_LEAST_ONE_PORTAL_STATE_NOT_EQUALS_DEACTIVATED_L";

    private static final String DELIMITER = ", ";
    private static final String PROPERTY = "property";
    private static final Integer MAX_DUPLICATES = 10;
    private static final String MAX_DUPLICATES_ERROR = "MAXIMUM_EXCEEDED_L";
    private static final String RESET_APPLICATIONS = "resetApplications";
    private static final String DELETE_APPOINTMENTS = "deleteAppointments";
    public static final String EXTERNALID_ALREADY_TAKEN = "EXTERNALID_ALREADY_TAKEN_L";
    private static final String DD_MM_YYYY = "dd-MM-yyyy";

    private final PropertyUpdatingService propertySendingService;

    private final PropertyRepository propertyRepository;

    private final PagedResourcesAssembler<Object> pagedResourcesAssembler;

    private final UserSecurityService userSecurityService;

    private final PropertyService propertyService;

    private final PropertyTenantService propertyTenantService;

    private final PropertyNotificationService notificationService;

    @Autowired
    public PropertyController(
            PropertyUpdatingService propertySendingService,
            PropertyRepository propertyRepository,
            PagedResourcesAssembler<Object> pagedResourcesAssembler,
            UserSecurityService userSecurityService,
            PropertyService propertyService,
            PropertyTenantService propertyTenantService,
            PropertyNotificationService notificationService) {
        this.propertySendingService = propertySendingService;
        this.propertyRepository = propertyRepository;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.userSecurityService = userSecurityService;
        this.propertyService = propertyService;
        this.propertyTenantService = propertyTenantService;
        this.notificationService = notificationService;
    }

    @InitBinder
    public void dataBinding(WebDataBinder binder) {
        binder.registerCustomEditor(String[].class, new StringArrayPropertyEditor(null));
    }

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestBody UpdatePropertyBean updatePropertyBean,
            @Parameter(hidden = true) PersistentEntityResourceAssembler assembler
    ) {
        LandlordCustomer customer = userSecurityService.getPrincipalUser().getCustomer();
        Property property = propertyService.createProperty(updatePropertyBean, customer);
        return new ResponseEntity<>(assembler.toModel(property), HttpStatus.OK);
    }

    @PreAuthorize("#property.customer.id == principal.customer.id")
    @PatchMapping(value = "/{property}")
    public ResponseEntity<Object> updatePropertyData(
            @PathVariable(PROPERTY) Property property,
            @RequestBody UpdatePropertyBean updatePropertyBean,
            @Parameter(hidden = true) PersistentEntityResourceAssembler assembler
    ) {
        property = propertyService.updateProperty(updatePropertyBean, property);
        return new ResponseEntity<>(assembler.toModel(property), HttpStatus.OK);
    }

    @GetMapping("/search/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id, @Parameter(hidden = true) PersistentEntityResourceAssembler assembler) {
        Property property;
        try {
            property = propertyService.findById(id);
        } catch (NotAuthorizedException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (property != null) {
            return new ResponseEntity<>(assembler.toModel(property), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("#property.customer.id == principal.customer.id")
    @PostMapping(value = "/publish/{property}")
    public ResponseEntity<Object> publish(@ModelAttribute(PROPERTY) Property property) {
        try {
            propertyService.validateProperty(property);
        } catch (ApiValidationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return runIfValid(property, () -> propertySendingService.publishToPortals(property.getId()));
    }

    @PreAuthorize("#property.customer.id == principal.customer.id")
    @PostMapping(value = "/deactivate/{property}")
    public ResponseEntity<Object> deactivate(@ModelAttribute(PROPERTY) Property property) {
        return runIfValid(property, () -> propertySendingService.deactivateForPortals(property.getId()));
    }

    @PreAuthorize("#property.customer.id == principal.customer.id")
    @PostMapping(value = "/reset/{property}")
    public ResponseEntity<Object> reset(@PathVariable(PROPERTY) Property property,
            @RequestParam(RESET_APPLICATIONS) boolean resetApplications,
            @RequestParam(DELETE_APPOINTMENTS) boolean deleteAppointments) {
        propertyService.resetProperty(property, resetApplications, deleteAppointments);

        return ResponseEntity.accepted().build();
    }

    @GetMapping("/list")
    @SuppressWarnings("unchecked")
    public ResponseEntity<Object> findAll(CustomPageable pageable, @Parameter(hidden = true) PersistentEntityResourceAssembler assembler) {
        PageRequest pageRequest = PageRequest.of(pageable.getPage(), pageable.getSize(), pageable.getSort());
        Page result = propertyRepository.findAll(pageRequest);
        return new ResponseEntity<>(pagedResourcesAssembler.toModel(result, assembler), HttpStatus.OK);
    }

    @PostMapping("/search/list")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> search(@Parameter(hidden = true) PersistentEntityResourceAssembler assembler,
            @RequestBody SearchProperty searchBean) {
        PageRequest pageRequest = PageRequest.of(searchBean.getPage(), searchBean.getSize(), searchBean.getSort());
        LandlordUser user = userSecurityService.getPrincipalUser();
        Page result = propertyService.search(searchBean, user.getCustomer(), pageRequest);
        return new ResponseEntity<>(pagedResourcesAssembler.toModel(result, assembler), HttpStatus.OK);
    }

    @PostMapping("search/list/count")
    public ResponseEntity<?> propertiesCountForFilter(
            @RequestBody Map<String, SearchProperty> searchPropertiesMap
    ) {
        Map<String, Long> result = propertyService.getPropertyCountsForFilter(searchPropertiesMap);

        return ResponseEntity.ok(result);
    }

    @PreAuthorize("#property.customer.id == principal.customer.id")
    @PostMapping(value = "/delete/{property}")
    public ResponseEntity<Object> delete(@PathVariable(PROPERTY) Property property) {
        if (!userSecurityService.hasPropertyDeleteRight(property)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        for (PropertyPortal portal : property.getPortals()) {
            if (!portal.getState().equals(PropertyPortalState.DEACTIVATED)) {
                return new ResponseEntity<>(AT_LEAST_ONE_PORTAL_STATE_NOT_EQUALS_DEACTIVATED,
                        HttpStatus.BAD_REQUEST);
            }
        }
        propertySendingService.deactivateFromPortalsThenDeleteProperty(property.getId());
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/deleteProperties")
    public ResponseEntity<Object> deleteProperties(@RequestParam(PROPERTY) List<Property> properties) {
        if (propertyService.isOnePortalActivated(properties)) {
            return new ResponseEntity<>(AT_LEAST_ONE_PORTAL_STATE_NOT_EQUALS_DEACTIVATED,
                    HttpStatus.BAD_REQUEST);
        }

        boolean oneHasNoDeleteRight = properties.stream()
                .anyMatch(property -> !userSecurityService.hasPropertyDeleteRight(property));

        if (oneHasNoDeleteRight) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        propertySendingService.deleteProperties(properties);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "property", schema = @Schema(type = "Long"), required = true)})
    @PreAuthorize("#property.customer.id == principal.customer.id")
    @PostMapping(value = "/duplicate")
    public ResponseEntity duplicateFlats(
            @RequestParam(value = "amount", defaultValue = "1") Integer amount,
            @RequestParam(value = "property") Property property
    ) {
        if (amount > MAX_DUPLICATES) {
            return new ResponseEntity<>(MAX_DUPLICATES_ERROR, HttpStatus.BAD_REQUEST);
        }

        List<Property> properties = propertyService.duplicateProperties(property, amount);
        return ResponseEntity.ok(properties);
    }

    @PostMapping(value = "/states")
    public ResponseEntity updateStates(@RequestBody List<Long> propertyIds, @RequestParam(value = "status") PropertyStatus status) {
        propertyService.updatePropertyStates(propertyIds, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("search/findContaining")
    public ResponseEntity findContaining(@RequestParam("search") String search) {
        List<Property> properties = propertyRepository.findAllContaining(search);

        return ResponseEntity.ok(properties.stream().map(PropertyShortBean::new).collect(Collectors.toList()));
    }

    @PostMapping("/countApplications")
    public ResponseEntity countPropertyApplications(@RequestBody Map<Long, Date> propertiesWithLastQueriedDate) {
        Map<String, Long> applicationCount = propertyService.countPropertyApplications(propertiesWithLastQueriedDate);

        return ResponseEntity.ok(applicationCount);
    }

    @PostMapping("/setNote")
    @PreAuthorize("#property?.customer?.id == principal.customer.id")
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "property", schema = @Schema(type = "Long"), required = true)})
    public ResponseEntity setPropertyNote(
            @RequestParam(value = "property") Property property,
            @RequestParam(value = "note") String note) {
        propertyService.setPropertyNote(property, note);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "property", schema = @Schema(type = "Long"), required = true),
            @Parameter(in = ParameterIn.QUERY, name = "application", schema = @Schema(type = "Long"), required = true)})
    @PostMapping("/setRented")
    @PreAuthorize("#property?.customer?.id == principal.customer.id || #application?.property?.customer?.id == principal.customer.id ")
    public ResponseEntity<PropertyTenantBean> setRented(
            @RequestParam(value = "property", required = false) Property property,
            @RequestParam(value = "application", required = false) PropertyApplication application,
            @DateTimeFormat(pattern = DD_MM_YYYY) @RequestParam(value = "contractStart") Date start,
            @RequestParam(value = "rejectAll", required = false) Boolean rejectAll,
            @RequestParam(value = "deactivate", required = false) Boolean deactivate
    ) {
        PropertyTenantBean propertyTenant = propertyTenantService.makeTenant(property, application, start, rejectAll, deactivate);
        return ResponseEntity.ok(propertyTenant);
    }

    @PostMapping(path = "/checkexternalid")
    @PreAuthorize("#property == null || #property?.customer?.id == principal.customer.id")
    public ResponseEntity<String> checkExternalId(@RequestParam(value = "property", required = false) Property property,
            @RequestParam("externalId") String externalId) {
        return propertyService.isExternalIdAvailableForCustomerAndProperty(externalId, property) ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.CONFLICT).body(EXTERNALID_ALREADY_TAKEN);
    }

    @DeleteMapping("/all")
    @Operation(description = "deleted every property from the logged in customer")
    public ResponseEntity deleteAllProperties() {
        propertyService.deleteAllPropertiesFromPrincipal();
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/sendExpose/{property}")
    @PreAuthorize("#property?.customer?.id == principal.customer.id")
    public ResponseEntity sendExpose(@RequestBody PropertySendExposeBean sendExposeBean, @PathVariable("property") Property property) {

        notificationService.sendExpose(property, sendExposeBean);
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<Object> runIfValid(Property property, Runnable action) {
        List<String> errors = checkPreconditions(property);
        if (!errors.isEmpty()) {
            return new ResponseEntity<>(concatErrors(errors), HttpStatus.BAD_REQUEST);
        }
        action.run();
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    private List<String> checkPreconditions(Property property) {
        final List<String> errors = new ArrayList<>();
        if (property.getPortals().isEmpty()) {
            errors.add("no portal to export to for property " + property.getId());
        }
        return errors;
    }

    private String concatErrors(List<String> errors) {
        return String.join(DELIMITER, errors);
    }

}
