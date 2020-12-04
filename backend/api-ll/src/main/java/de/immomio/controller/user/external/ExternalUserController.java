package de.immomio.controller.user.external;

import de.immomio.data.landlord.entity.user.external.LandlordExternalApiUser;
import de.immomio.data.landlord.entity.user.external.LandlordExternalApiUserBean;
import de.immomio.data.landlord.entity.user.external.LandlordExternalApiUserCreatedBean;
import de.immomio.landlord.service.external.ExternalApiUserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Niklas Lindemann
 */

@RestController
@RequestMapping("/users/external")
public class ExternalUserController {
    private ExternalApiUserService externalApiUserService;
    private PagedResourcesAssembler pagedResourcesAssembler;

    public ExternalUserController(ExternalApiUserService externalApiUserService, PagedResourcesAssembler pagedResourcesAssembler) {
        this.externalApiUserService = externalApiUserService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @PostMapping("/create")
    public ResponseEntity<LandlordExternalApiUserCreatedBean> createExternalApiUser() {
        LandlordExternalApiUserCreatedBean apiUser = externalApiUserService.createApiUser();
        return ResponseEntity.ok(apiUser);
    }

    @GetMapping("/findAll")
    public ResponseEntity<PagedModel<LandlordExternalApiUserBean>> findAll(Pageable pageable) {
        Page<LandlordExternalApiUserBean> userBeans = externalApiUserService.findAll(pageable);
        PagedModel<LandlordExternalApiUserBean> body = pagedResourcesAssembler.toModel(userBeans);
        return ResponseEntity.ok(body);
    }

    @PreAuthorize("#user.customer.id == principal.customer.id")
    @DeleteMapping("/{user}")
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "user", schema = @Schema(type = "Long"), required = true)})
    public ResponseEntity delete(@PathVariable("user") LandlordExternalApiUser user) {
        externalApiUserService.delete(user);
        return ResponseEntity.accepted().build();
    }
}
