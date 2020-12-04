package de.immomio.controller.search.property;

import de.immomio.data.landlord.entity.property.portal.PropertyPortalState;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class SearchPropertyPortalState implements Serializable {

    private List<PropertyPortalState> states;

    private SearchPropertyPredicate predicate = SearchPropertyPredicate.OR;

    private boolean includeWithoutPortals = false;

}
