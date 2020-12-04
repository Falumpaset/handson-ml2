package de.immomio.controller.property;

import de.immomio.beans.IdsList;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.model.repository.service.landlord.customer.property.PropertyRepository;
import de.immomio.service.property.AdminPropertyProposalService;
import de.immomio.service.property.AdminPropertyUpdatingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Maik Kingma
 */

@Slf4j
@RepositoryRestController
@RequestMapping(value = "/ll-properties")
public class PropertyController {

    private final AdminPropertyUpdatingService propertySendingService;

    private final PropertyRepository propertyRepository;

    private final AdminPropertyProposalService propertyProposalService;

    @Autowired
    public PropertyController(AdminPropertyUpdatingService propertySendingService, PropertyRepository propertyRepository, AdminPropertyProposalService propertyProposalService) {
        this.propertySendingService = propertySendingService;
        this.propertyRepository = propertyRepository;
        this.propertyProposalService = propertyProposalService;
    }

    @PostMapping(value = "/publish/{property}")
    public ResponseEntity<Object> publish(@ModelAttribute("property") Property property) {
        return runIfValid(property, () -> propertySendingService.publishToPortals(property.getId()));
    }

    @PostMapping(value = "/update/{property}")
    public ResponseEntity<Object> update(@ModelAttribute("property") Property property) {
        return runIfValid(property, () -> propertySendingService.updateForPortals(property.getId()));
    }

    @PostMapping(value = "/update/batch")
    public ResponseEntity<Object> batchUpdate(@RequestBody @Valid IdsList ids) {
        ids.getIds().forEach(id -> {
            propertyRepository.findById(id).ifPresent(property -> {
                if (!property.getPortals().isEmpty()) {
                    propertySendingService.updateForPortals(id);
                }
            });
        });

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/deactivate/batch")
    public ResponseEntity<Object> batchDeactivate(@RequestBody @Valid IdsList ids) {
        ids.getIds().forEach(id -> {
            propertyRepository.findById(id).ifPresent(property -> {
                if (!property.getPortals().isEmpty()) {
                    propertySendingService.deactivateForPortals(id);
                }
            });
        });

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/deactivate/{property}")
    public ResponseEntity<Object> deactivate(@ModelAttribute("property") Property property) {
        return runIfValid(property, () -> propertySendingService.deactivateForPortals(property.getId()));
    }

    @GetMapping("/calculateProposals")
    public ResponseEntity calculateProposals() {
        propertyProposalService.refreshProposals();
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
        return String.join(", ", errors);
    }

}
