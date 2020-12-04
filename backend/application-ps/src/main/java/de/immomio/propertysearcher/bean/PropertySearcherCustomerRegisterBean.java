package de.immomio.propertysearcher.bean;

import de.immomio.beans.AbstractCustomerRegisterBean;
import de.immomio.data.base.type.customer.CustomerLocation;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
@Setter
public class PropertySearcherCustomerRegisterBean extends AbstractCustomerRegisterBean {

    private String description;

    private String name;

    private String taxId;

    @NotNull
    private CustomerLocation location;

    private Integer managementUnits;

    private Map<String, String> preferences;

}