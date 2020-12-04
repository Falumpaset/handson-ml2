package de.immomio.controller.property;

import de.immomio.beans.propertysearcher.SharedPropertyData;
import de.immomio.constants.exceptions.NotAuthorizedException;
import de.immomio.controller.BaseController;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.model.repository.shared.property.PropertyRepositoryCustom;
import de.immomio.service.property.PropertyService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RepositoryRestController
@RequestMapping(value = "/properties")
public class PropertyController extends BaseController {

    @Autowired
    private PropertyRepositoryCustom propertyRepositoryImpl;

    @Autowired
    private PropertyService propertyService;

    @GetMapping(value = "/shared/{id}")
    public ResponseEntity<SharedPropertyData> shared(@PathVariable Long id) {
        Property property = propertyRepositoryImpl.customFindOne(id);

        if (property != null) {
            SharedPropertyData sharedPropertyData = new SharedPropertyData(property);
            return ResponseEntity.ok(sharedPropertyData);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id, @Parameter(hidden = true)  PersistentEntityResourceAssembler assembler) {
        Property property = null;
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

}
