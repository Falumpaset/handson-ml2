package de.immomio.controller.property;

import de.immomio.data.landlord.bean.property.PropertyEventBean;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.landlord.service.property.PropertyEventService;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Niklas Lindemann
 */

@RepositoryRestController
@RequestMapping(value = "/properties/events")
public class PropertyEventController {


    private final PropertyEventService eventService;

    public PropertyEventController(PropertyEventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/{property}")
    @PreAuthorize("#property.customer.id == principal.customer.id")
    public ResponseEntity<List<PropertyEventBean>> getEvents(@PathVariable("property") Property property) {

        return ResponseEntity.ok(eventService.getPropertyEvents(property));
    }
}
