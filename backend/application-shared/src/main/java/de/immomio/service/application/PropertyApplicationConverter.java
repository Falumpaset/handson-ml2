package de.immomio.service.application;

import de.immomio.data.base.type.application.ApplicantStatus;
import de.immomio.data.base.type.application.ApplicationUpdateStatus;
import de.immomio.data.landlord.bean.property.dk.DkApprovalLevel;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.bean.application.LandlordPropertyApplicationBean;
import de.immomio.data.shared.bean.application.PropertyApplicationBean;
import de.immomio.data.shared.bean.application.PropertySearcherGuestPropertyApplicationBean;
import de.immomio.data.shared.bean.application.PropertySearcherPropertyApplicationBean;
import de.immomio.data.shared.bean.note.NoteBean;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptanceState;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.LimitedAppointmentAcceptance;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.data.shared.entity.property.tenant.PropertyTenant;
import de.immomio.service.landlord.property.PropertyConverter;
import de.immomio.service.propertysearcher.userprofile.PropertySearcherUserProfileConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class PropertyApplicationConverter {

    private final PropertyConverter propertyConverter;
    private final PropertySearcherUserProfileConverter userProfileConverter;

    @Autowired
    public PropertyApplicationConverter(PropertyConverter propertyConverter, PropertySearcherUserProfileConverter userProfileConverter) {
        this.propertyConverter = propertyConverter;
        this.userProfileConverter = userProfileConverter;
    }

    public PropertySearcherPropertyApplicationBean convertToPropertySearcherPropertyApplicationBean(PropertyApplication application,
            ApplicantStatus applicantStatus,
            Boolean appointmentSlotsAvailable,
            List<LimitedAppointmentAcceptance> acceptances) {

        PropertySearcherPropertyApplicationBean applicationBean = new PropertySearcherPropertyApplicationBean();
        fillPropertySearcherData(applicationBean, application, applicantStatus, appointmentSlotsAvailable, acceptances);
        return applicationBean;
    }

    public PropertySearcherGuestPropertyApplicationBean convertToPropertySearcherGuestPropertyApplicationBean(PropertyApplication application,
            ApplicantStatus applicantStatus,
            Boolean appointmentSlotsAvailable,
            List<LimitedAppointmentAcceptance> acceptances) {

        PropertySearcherGuestPropertyApplicationBean applicationBean = new PropertySearcherGuestPropertyApplicationBean();
        fillPropertySearcherData(applicationBean, application, applicantStatus, appointmentSlotsAvailable, acceptances);
        applicationBean.setUserProfile(userProfileConverter.convertUserProfile(application.getUserProfile()));
        return applicationBean;
    }

    public LandlordPropertyApplicationBean convertToLandlordPropertyApplicationBean(PropertyApplication application,
            DkApprovalLevel newestDkLevel,
            List<LimitedAppointmentAcceptance> acceptances,
            boolean conversationExists,
            Long invitationCount,
            NoteBean noteBean,
            Date firstSpCreated) {

        LandlordPropertyApplicationBean applicationBean = new LandlordPropertyApplicationBean();
        fillBaseData(applicationBean, application, acceptances);
        applicationBean.setNote(noteBean);
        applicationBean.setDkApprovalLevel(newestDkLevel);

        Long lastAppointmentId = acceptances.stream()
                .filter(appointmentAcceptance -> appointmentAcceptance.getState() == AppointmentAcceptanceState.ACTIVE)
                .sorted(Comparator.comparing(LimitedAppointmentAcceptance::getCreated))
                .map(LimitedAppointmentAcceptance::getAppointmentId)
                .findFirst()
                .orElse(null);

        applicationBean.setAttendingExclusiveViewing(invitationCount != null && invitationCount > 0);
        applicationBean.setLastAppointmentId(lastAppointmentId);
        applicationBean.setAcceptedAppointment(lastAppointmentId != null);
        applicationBean.setConversationExists(conversationExists);
        applicationBean.setUpdateStatus(application.getSeen() != null ? ApplicationUpdateStatus.SEEN : ApplicationUpdateStatus.NEW);
        applicationBean.setFirstSearchProfileCreated(firstSpCreated);
        applicationBean.setUserProfile(userProfileConverter.convertUserProfile(application.getUserProfile()));
        return applicationBean;
    }

    public boolean isInInternalPool(Property property, PropertySearcherUserProfile userProfile) {
        return property.getCustomer().equals(userProfile.getTenantPoolCustomer());
    }

    private void fillBaseData(PropertyApplicationBean applicationBean, PropertyApplication application, List<LimitedAppointmentAcceptance> acceptances) {

        if (acceptances == null) {
            acceptances = Collections.emptyList();
        }
        long sizeOfAppointmentAcceptancesPast = acceptances.stream()
                .filter(acceptance -> acceptance.getState() == AppointmentAcceptanceState.ACTIVE && acceptance.getAppointmentDate().compareTo(new Date()) < 0)
                .count();

        applicationBean.setId(application.getId());
        applicationBean.setPortal(application.getPortal());
        applicationBean.setCreated(application.getCreated());
        applicationBean.setScore(application.getScore());
        applicationBean.setCustomQuestionScore(application.getCustomQuestionScore());
        applicationBean.setAskedForIntent(application.isAskedForIntent());
        applicationBean.setStatus(application.getStatus());
        applicationBean.setText(application.getText());
        applicationBean.setArchived(application.getArchived() != null);
        applicationBean.setInInternalPool(isInInternalPool(application.getProperty(), application.getUserProfile()));

        applicationBean.setAttendedAppointment(sizeOfAppointmentAcceptancesPast > 0);
    }

    private void fillPropertySearcherData(PropertySearcherPropertyApplicationBean applicationBean,
            PropertyApplication application,
            ApplicantStatus applicantStatus,
            Boolean appointmentSlotsAvailable,
            List<LimitedAppointmentAcceptance> acceptances) {

        fillBaseData(applicationBean, application, acceptances);
        applicationBean.setApplicantStatus(applicantStatus);
        applicationBean.setAppointmentSlotsAvailable(appointmentSlotsAvailable);
        applicationBean.setPropertyTenantId(getPropertyTenantId(application));
        applicationBean.setProperty(propertyConverter.convertToPropertySearcherPropertyBean(application.getProperty()));
        applicationBean.setUpcomingAppointmentDate(getUpcomingAppointmentDate(acceptances));
    }

    private Date getUpcomingAppointmentDate(List<LimitedAppointmentAcceptance> acceptances) {
        return acceptances.stream()
                .filter(acceptance -> acceptance.getState() == AppointmentAcceptanceState.ACTIVE && acceptance.getAppointmentDate().after(new Date()))
                .map(LimitedAppointmentAcceptance::getAppointmentDate)
                .min(Comparator.comparing(Date::getTime))
                .orElse(null);
    }

    private Long getPropertyTenantId(PropertyApplication application) {
        PropertyTenant tenant = application.getProperty().getTenant();
        Long propertyTenantUserId = null;
        if (tenant != null && tenant.getUserProfile() != null) {
            propertyTenantUserId = tenant.getUserProfile().getId();
        }
        return propertyTenantUserId;
    }

}
