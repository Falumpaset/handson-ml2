package de.immomio.controller.search.property;

import de.immomio.controller.paging.CustomPageable;
import de.immomio.data.base.type.property.PropertyStatus;
import de.immomio.data.base.type.property.PropertyType;
import de.immomio.data.landlord.bean.landlordfilter.BaseAgentFilterBean;
import de.immomio.data.shared.bean.common.DoubleRange;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SearchProperty extends CustomPageable implements BaseAgentFilterBean {

    private static final long serialVersionUID = 4857517744670984544L;

    private String name;

    private String externalId;

    private List<Long> agents;

    private SearchPropertyPortalState propertyPortal = new SearchPropertyPortalState();

    private SearchPropertyAddress address;

    private Boolean rented;

    private DoubleRange roomNumber;

    private DoubleRange propertySize;

    private DoubleRange basePrice;

    private Boolean wbs;

    private List<PropertyStatus> statuses = new ArrayList<>();

    private List<PropertyType> types = new ArrayList<>();
}
