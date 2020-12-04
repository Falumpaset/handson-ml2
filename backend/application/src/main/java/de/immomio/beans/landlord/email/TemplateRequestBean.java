package de.immomio.beans.landlord.email;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TemplateRequestBean {

    private boolean skipDefaultData;

    private boolean translateKeys;

    private Long propertyApplicationId;

    private Long propertyId;

    private Long appointmentId;
}
