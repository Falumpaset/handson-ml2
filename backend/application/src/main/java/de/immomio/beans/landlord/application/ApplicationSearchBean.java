package de.immomio.beans.landlord.application;

import de.immomio.controller.paging.CustomPageable;
import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.base.type.user.profile.PropertySearcherUserProfileType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
public class ApplicationSearchBean extends CustomPageable implements Serializable {
    private static final long serialVersionUID = 2139647026440371395L;

    private Long propertyId;

    private List<ApplicationStatus> statuses = new ArrayList<>();

    private Boolean wbs;

    private Boolean processed;

    private CustomQuestionFilterType customQuestionFilter;

    private List<PropertySearcherUserProfileType> profileTypes = new ArrayList<>();

}
