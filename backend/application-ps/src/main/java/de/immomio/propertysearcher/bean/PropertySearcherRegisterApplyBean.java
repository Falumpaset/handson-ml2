package de.immomio.propertysearcher.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PropertySearcherRegisterApplyBean extends PropertySearcherRegisterBean {

    private static final long serialVersionUID = 1041136258090254215L;

    private String shortUrlToken;
}
