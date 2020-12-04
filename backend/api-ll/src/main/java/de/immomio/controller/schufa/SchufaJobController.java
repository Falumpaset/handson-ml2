package de.immomio.controller.schufa;

import de.immomio.beans.landlord.schufa.SchufaReportSearchBean;
import de.immomio.data.base.bean.schufa.cbi.SchufaConnectorException;
import de.immomio.data.base.bean.schufa.cbi.SchufaValidationException;
import de.immomio.data.base.type.schufa.SchufaUserInfo;
import de.immomio.data.landlord.entity.schufa.LandlordSchufaJob;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.landlord.service.schufa.SchufaJobService;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.model.entity.landlord.schufa.SchufaReportBean;
import de.immomio.model.repository.landlord.schufa.LandlordSchufaJobRepository;
import de.immomio.schufa.SchufaJobDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Johannes Hiemer.
 */
@Slf4j
@RepositoryRestController
@RequestMapping(value = "/schufaJobs")
public class SchufaJobController {

    private static final String FIRSTNAME_MAY_NOT_BE_NULL_L = "FIRSTNAME_MAY_NOT_BE_NULL_L";
    private static final String NAME_MAY_NOT_BE_NULL_L = "NAME_MAY_NOT_BE_NULL_L";
    private static final String DAY_OF_BIRTH_MAY_NOT_BE_NULL_L = "DAY_OF_BIRTH_MAY_NOT_BE_NULL_L";
    private static final String GENDER_MAY_NOT_BE_NULL_L = "GENDER_MAY_NOT_BE_NULL_L";
    private static final String ADDRESS_MAY_NOT_BE_NULL_L = "ADDRESS_MAY_NOT_BE_NULL_L";
    private static final String TYPE_MAY_NOT_BE_NULL_L = "TYPE_MAY_NOT_BE_NULL_L";
    private static final String STREET_MAY_NOT_BE_NULL_L = "STREET_MAY_NOT_BE_NULL_L";
    private static final String HOUSENUMBER_MAY_NOT_BE_NULL_L = "HOUSENUMBER_MAY_NOT_BE_NULL_L";
    private static final String CITY_MAY_NOT_BE_NULL_L = "CITY_MAY_NOT_BE_NULL_L";
    private static final String ZIPCODE_MAY_NOT_BE_NULL_L = "ZIPCODE_MAY_NOT_BE_NULL_L";

    private final SchufaJobDispatcher schufaJobDispatcher;

    private final UserSecurityService userSecurityService;

    private final LandlordSchufaJobRepository schufaJobRepository;

    private final PagedResourcesAssembler<Object> pagedResourcesAssembler;

    private final SchufaJobService schufaJobService;

    @Autowired
    public SchufaJobController(
            UserSecurityService userSecurityService,
            SchufaJobDispatcher schufaJobDispatcher,
            LandlordSchufaJobRepository schufaJobRepository,
            PagedResourcesAssembler<Object> pagedResourcesAssembler,
            SchufaJobService schufaJobService
    ) {
        this.schufaJobDispatcher = schufaJobDispatcher;
        this.userSecurityService = userSecurityService;
        this.schufaJobRepository = schufaJobRepository;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.schufaJobService = schufaJobService;
    }

    @PostMapping(value = "/dispatch")
    public ResponseEntity dispatch(
            @RequestBody SchufaUserInfo schufaReportBean,
            @RequestParam(value = "user", required = false) Long userId) {
        try {
            validateSchufaRequestBean(schufaReportBean);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        LandlordUser landlordUser = userSecurityService.getPrincipalUser();
        if (landlordUser != null) {
            try {
                schufaJobDispatcher.dispatch(landlordUser, schufaReportBean, userId);
            } catch (SchufaConnectorException | SchufaValidationException ex) {
                log.info("Error during SCHUFA job execution", ex);
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping
    public ResponseEntity findAll(Pageable pageable) {
        Page<LandlordSchufaJob> schufaJobs = schufaJobRepository.findAll(pageable);
        List<SchufaReportBean> reportBeans = schufaJobs.getContent()
                                                     .stream()
                                                     .map(SchufaReportBean::new)
                                                     .collect(Collectors.toList());
        PageImpl page = new PageImpl<>(reportBeans, pageable, schufaJobRepository.countByCustomer());

        return new ResponseEntity<>(pagedResourcesAssembler.toModel(page), HttpStatus.OK);
    }

    @GetMapping("/search/findByUser")
    public ResponseEntity findByUser(Pageable pageable, @RequestParam("userProfile") Long userProfileId) {
        Page<LandlordSchufaJob> schufaJobs = schufaJobRepository.findByUserProfile(pageable, userProfileId);
        List<SchufaReportBean> reportBeans = schufaJobs.getContent()
                .stream()
                .map(SchufaReportBean::new)
                .collect(Collectors.toList());
        PageImpl page = new PageImpl<>(reportBeans, pageable, schufaJobRepository.countByUserProfile(userProfileId));

        return new ResponseEntity<>(pagedResourcesAssembler.toModel(page), HttpStatus.OK);
    }

    @PostMapping("/search/list")
    public ResponseEntity<?> find(@RequestBody SchufaReportSearchBean searchBean) {
        PageRequest pageRequest = PageRequest.of(searchBean.getPage(), searchBean.getSize(), searchBean.getSort());
        Page result = schufaJobService.search(searchBean, pageRequest);

        return ResponseEntity.ok(pagedResourcesAssembler.toModel(result));
    }

    private void validateSchufaRequestBean(SchufaUserInfo schufaUserInfo) {
        Assert.notNull(schufaUserInfo.getFirstname(), FIRSTNAME_MAY_NOT_BE_NULL_L);
        Assert.notNull(schufaUserInfo.getName(), NAME_MAY_NOT_BE_NULL_L);
        Assert.notNull(schufaUserInfo.getDateOfBirth(), DAY_OF_BIRTH_MAY_NOT_BE_NULL_L);
        Assert.notNull(schufaUserInfo.getGender(), GENDER_MAY_NOT_BE_NULL_L);
        Assert.notNull(schufaUserInfo.getAddress(), ADDRESS_MAY_NOT_BE_NULL_L);
        Assert.notNull(schufaUserInfo.getType(), TYPE_MAY_NOT_BE_NULL_L);
        Assert.notNull(schufaUserInfo.getAddress().getStreet(), STREET_MAY_NOT_BE_NULL_L);
        Assert.notNull(schufaUserInfo.getAddress().getHouseNumber(), HOUSENUMBER_MAY_NOT_BE_NULL_L);
        Assert.notNull(schufaUserInfo.getAddress().getCity(), CITY_MAY_NOT_BE_NULL_L);
        Assert.notNull(schufaUserInfo.getAddress().getZipCode(), ZIPCODE_MAY_NOT_BE_NULL_L);
    }

}
