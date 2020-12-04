package de.immomio.controller.application;

import de.immomio.beans.landlord.application.ApplicationSearchBean;
import de.immomio.beans.shared.CommonCountBean;
import de.immomio.data.shared.bean.application.LandlordPropertyApplicationBean;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptance;
import de.immomio.data.shared.entity.property.search.PropertySearcherSearchBean;
import de.immomio.landlord.service.application.LandlordPropertyApplicationSearchService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@RepositoryRestController
@RequestMapping(value = "/applications/search")
public class PropertyApplicationSearchController {

    private static final String SEARCH = "search";

    private final PagedResourcesAssembler<LandlordPropertyApplicationBean> pagedResourcesAssembler;

    private final LandlordPropertyApplicationSearchService propertyApplicationSearchService;

    @Autowired
    public PropertyApplicationSearchController(
            PagedResourcesAssembler<LandlordPropertyApplicationBean> pagedResourcesAssembler,
            LandlordPropertyApplicationSearchService propertyApplicationSearchService) {
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.propertyApplicationSearchService = propertyApplicationSearchService;
    }

    @GetMapping("/findByUser")
    public ResponseEntity<List<PropertySearcherSearchBean>> searchByUser(@RequestParam(SEARCH) String searchValue) {
        List<PropertySearcherSearchBean> userBeans = propertyApplicationSearchService.findByNameAndEmail(searchValue);

        return new ResponseEntity<>(userBeans, HttpStatus.OK);
    }

    @PostMapping("/list")
    @PreAuthorize("@userSecurityService.allowUserToReadProperty(#searchBean?.propertyId)")
    public ResponseEntity<PagedModel<EntityModel<LandlordPropertyApplicationBean>>> findApplications(
            @RequestBody ApplicationSearchBean searchBean
    ) {
        PageRequest pageRequest = PageRequest.of(searchBean.getPage(), searchBean.getSize(), searchBean.getSort());
        Page<LandlordPropertyApplicationBean> overviewBeans = propertyApplicationSearchService.search(searchBean, pageRequest);

        PagedModel<EntityModel<LandlordPropertyApplicationBean>> pagedModel = pagedResourcesAssembler.toModel(overviewBeans);
        return ResponseEntity.ok(pagedModel);
    }

    @PostMapping("/list/count")
    @PreAuthorize("@userSecurityService.allowUserToReadProperty(#searchBean?.propertyId)")
    public ResponseEntity<CommonCountBean> findApplicationsCount(
            @RequestBody ApplicationSearchBean searchBean
    ) {
        return ResponseEntity.ok(propertyApplicationSearchService.getCountOfApplications(searchBean));

    }

    @GetMapping("/findByUserProfileIdAndPropertyId")
    @SuppressWarnings("unchecked")
    public ResponseEntity<LandlordPropertyApplicationBean> findByUserProfileIdAndPropertyId(
            @RequestParam("propertyId") Long propertyId,
            @RequestParam("userProfile") Long userProfileId
    ) {
        LandlordPropertyApplicationBean applicationBean =
                propertyApplicationSearchService.findByUserProfileIdAndPropertyId(propertyId, userProfileId);

        return ResponseEntity.ok(applicationBean);
    }

    // TODO: 14.10.20 remove after refactoring
    @GetMapping("/findByAppointmentAcceptance")
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "appointmentAcceptance", schema = @Schema(type = "Long"), required = true)})
    public ResponseEntity<LandlordPropertyApplicationBean> findByUserProfileIdAndPropertyId(
            @RequestParam("appointmentAcceptance") AppointmentAcceptance appointmentAcceptance
    ) {
        LandlordPropertyApplicationBean applicationBean = propertyApplicationSearchService.findByPropertyApplication(appointmentAcceptance);
        return ResponseEntity.ok(applicationBean);
    }
}
