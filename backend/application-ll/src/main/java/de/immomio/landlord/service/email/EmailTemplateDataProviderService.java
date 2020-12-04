package de.immomio.landlord.service.email;

import de.immomio.beans.landlord.email.TemplateRequestBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.mail.ModelParams;
import de.immomio.model.repository.shared.application.PropertyApplicationRepository;
import de.immomio.model.repository.shared.appointment.AppointmentRepository;
import de.immomio.model.repository.shared.property.PropertyRepository;
import de.immomio.service.shared.EmailModelProvider;
import de.immomio.utils.PreviewDataUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailTemplateDataProviderService {

    private final PropertyApplicationRepository propertyApplicationRepository;

    private final PropertyRepository propertyRepository;

    private final EmailModelProvider emailModelProvider;

    private final AppointmentRepository appointmentRepository;

    public EmailTemplateDataProviderService(PropertyApplicationRepository propertyApplicationRepository,
            PropertyRepository propertyRepository,
            EmailModelProvider emailModelProvider,
            AppointmentRepository appointmentRepository) {
        this.propertyApplicationRepository = propertyApplicationRepository;
        this.propertyRepository = propertyRepository;
        this.emailModelProvider = emailModelProvider;
        this.appointmentRepository = appointmentRepository;
    }

    public Map<String, Object> getTemplateEmailModel(TemplateRequestBean requestBean, LandlordCustomer customer) {
        Map<String, Object> model = new HashMap<>();

        fillWithDynamicData(model, requestBean, customer);
        if (!requestBean.isSkipDefaultData()) {
            fillWithDefaultData(model);
        }

        return model;
    }

    private void fillWithDynamicData(Map<String, Object> model, TemplateRequestBean requestBean, LandlordCustomer customer) {
        if (requestBean.getPropertyApplicationId() != null) {
            propertyApplicationRepository.findByIdAndCustomer(requestBean.getPropertyApplicationId(), customer)
                    .ifPresent(application -> appendPropertyApplication(model, application));
        }

        if (requestBean.getAppointmentId() != null && !model.containsKey(ModelParams.MODEL_APPOINTMENT)) {
            appointmentRepository.findByIdAndCustomer(requestBean.getAppointmentId(), customer).ifPresent(appointment -> appendAppointment(model, appointment));
        }

        if (requestBean.getPropertyId() != null && !model.containsKey(ModelParams.MODEL_FLAT)) {
            propertyRepository.findByIdAndCustomer(requestBean.getPropertyId(), customer).ifPresent(property -> appendProperty(model, property));
        }

        if (customer != null && !model.containsKey(ModelParams.MODEL_PREFERENCES)) {
            appendLandlordCustomer(model, customer);
        }
    }

    private void appendPropertyApplication(Map<String, Object> model, PropertyApplication propertyApplication) {
        appendProperty(model, propertyApplication.getProperty());
        appendUser(model, propertyApplication.getUserProfile());
    }

    private void appendAppointment(Map<String, Object> model, Appointment appointment) {
        if (!model.containsKey(ModelParams.MODEL_APPOINTMENT)) {
            emailModelProvider.appendAppointment(model, appointment);
        }

        appendProperty(model, appointment.getProperty());
    }

    private void appendUser(Map<String, Object> model, PropertySearcherUserProfile userProfile) {
        if (!model.containsKey(ModelParams.MODEL_USER)) {
            emailModelProvider.appendUserProfile(model, userProfile);
        }
    }

    private void appendProperty(Map<String, Object> model, Property property) {
        if (!model.containsKey(ModelParams.MODEL_FLAT)) {
            emailModelProvider.appendProperty(model, property);
        }
    }

    private void appendLandlordCustomer(Map<String, Object> model, LandlordCustomer customer) {
        if (!model.containsKey(ModelParams.MODEL_PREFERENCES)) {
            emailModelProvider.appendLandlordCustomer(model, customer);
        }
    }

    private void fillWithDefaultData(Map<String, Object> model) {
        if (!model.containsKey(ModelParams.MODEL_USER)) {
            appendUser(model, PreviewDataUtils.getPropertySearcherUserProfile());
        }

        if (!model.containsKey(ModelParams.MODEL_FLAT)) {
            appendProperty(model, PreviewDataUtils.getProperty());
        }

        if (!model.containsKey(ModelParams.MODEL_APPOINTMENT_DATE_FORMATTED)) {
            emailModelProvider.appendFormattedDate(new Date(), model, ModelParams.MODEL_APPOINTMENT_DATE_FORMATTED);
        }
    }
}
