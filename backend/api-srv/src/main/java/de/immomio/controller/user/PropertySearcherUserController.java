package de.immomio.controller.user;

import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserBean;
import de.immomio.service.propertysearcher.PropertySearcherUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Niklas Lindemann
 */

@RepositoryRestController
@RequestMapping(value = "ps-users")
@Slf4j
public class PropertySearcherUserController {

    private PropertySearcherUserService propertySearcherUserService;

    @Autowired
    public PropertySearcherUserController(PropertySearcherUserService propertySearcherUserService) {
        this.propertySearcherUserService = propertySearcherUserService;
    }

    @GetMapping(value = "search/list")
    public ResponseEntity<PagedModel<EntityModel<PropertySearcherUserBean>>> search(@RequestParam("searchTerm") String searchTerm, Pageable pageable, PagedResourcesAssembler<PropertySearcherUserBean> pagedResourcesAssembler) {
        Page<PropertySearcherUserBean> users = propertySearcherUserService.search(searchTerm, pageable);
        PagedModel<EntityModel<PropertySearcherUserBean>> pagedModel = pagedResourcesAssembler.toModel(users);
        return ResponseEntity.ok(pagedModel);
    }


}
