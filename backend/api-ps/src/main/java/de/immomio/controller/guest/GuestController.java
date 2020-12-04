package de.immomio.controller.guest;

import de.immomio.controller.BaseController;
import de.immomio.data.landlord.bean.customer.settings.theme.LandlordCustomerBranding;
import de.immomio.data.propertysearcher.bean.user.PropertySearcherUserRegisterResultBean;
import de.immomio.data.propertysearcher.bean.user.guest.PropertySearcherGuestUserApplicationPossibleBean;
import de.immomio.data.propertysearcher.bean.user.guest.PropertySearcherGuestUserApplyBean;
import de.immomio.data.propertysearcher.bean.user.guest.PropertySearcherGuestUserRegisterBean;
import de.immomio.data.shared.bean.application.PropertyApplicationBean;
import de.immomio.data.shared.bean.application.PropertySearcherGuestPropertyApplicationBean;
import de.immomio.data.shared.bean.customQuestion.CustomQuestionResponseQuestionBean;
import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptance;
import de.immomio.propertysearcher.bean.AppointmentAcceptanceBean;
import de.immomio.propertysearcher.bean.AppointmentBundleBean;
import de.immomio.propertysearcher.bean.PropertySearcherRegisterApplyBean;
import de.immomio.service.guest.PropertySearcherUserGuestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@RepositoryRestController
@RequestMapping(value = "/guest")
public class GuestController extends BaseController {

    private final PropertySearcherUserGuestService guestService;

    @Autowired
    public GuestController(PropertySearcherUserGuestService guestService) {
        this.guestService = guestService;
    }

    @GetMapping(value = "/application/possible")
    public ResponseEntity<PropertySearcherGuestUserApplicationPossibleBean> isGuestPropertyApplicationPossible(@RequestParam("email") String email,
            @RequestParam("property") Long propertyId) {
        PropertySearcherGuestUserApplicationPossibleBean applicationPossibleBean = guestService.isGuestPropertyApplicationPossible(email, propertyId);
        return ResponseEntity.ok(applicationPossibleBean);
    }

    @PostMapping(value = "/application/apply")
    public ResponseEntity<Void> applyToPropertyAsGuest(@RequestParam("token") String token, @RequestBody PropertySearcherGuestUserApplyBean applyBean) {
        guestService.applyToPropertyAsGuest(token, applyBean);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/application/register")
    public ResponseEntity<PropertySearcherUserRegisterResultBean> registerAndApplyToProperty(@RequestParam("token") String token,
            @RequestBody PropertySearcherRegisterApplyBean registerBean) {
        return ResponseEntity.ok(guestService.registerAndApplyToProperty(token, registerBean));
    }

    @DeleteMapping()
    public ResponseEntity<Void> cancelGuestApplication(@RequestParam("token") String token) {
        guestService.deleteGuest(token);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/register")
    public ResponseEntity<PropertySearcherUserRegisterResultBean> register(@RequestParam("token") String token,
            @RequestBody PropertySearcherGuestUserRegisterBean registerBean) {
        return ResponseEntity.ok(guestService.register(token, registerBean));
    }

    @GetMapping(value = "/application")
    public ResponseEntity<PropertySearcherGuestPropertyApplicationBean> getApplication(@RequestParam("token") String token) {
        return ResponseEntity.ok(guestService.getApplication(token));
    }

    @GetMapping(value = "/appointments")
    public ResponseEntity<List<AppointmentBundleBean>> getAppointmentOverview(@RequestParam("token") String token) {
        return ResponseEntity.ok(guestService.getAppointmentBundleBeans(token));
    }

    @PostMapping(value = "/appointments/{appointment}/accept")
    public ResponseEntity<AppointmentAcceptanceBean> acceptAppointment(@PathVariable("appointment") Appointment appointment,
            @RequestParam("token") String token) {

        return ResponseEntity.ok(guestService.acceptViewing(appointment, token));
    }

    @PostMapping(value = "/appointmentAcceptances/{appointmentAcceptance}/cancel")
    public ResponseEntity<AppointmentAcceptanceBean> cancelAppointment(@PathVariable("appointmentAcceptance") AppointmentAcceptance appointmentAcceptance,
            @RequestParam("token") String token) {
        return ResponseEntity.ok(guestService.cancel(appointmentAcceptance, token));
    }

    @PostMapping(value = "/applications/declareIntent")
    public ResponseEntity<Void> declareIntentNoAuth(@RequestParam("intent") boolean intent, @RequestParam("token") String token) {
        guestService.updateIntentOfApplication(intent, token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/customQuestionResponses")
    public ResponseEntity<List<CustomQuestionResponseQuestionBean>> getCustomQuestionResponses(@RequestParam("token") String token) {
        return ResponseEntity.ok(guestService.getCustomQuestionResponses(token));
    }

    @GetMapping("/branding")
    public ResponseEntity<LandlordCustomerBranding> getBranding(@RequestParam("token") String token) {
        return ResponseEntity.ok(guestService.getUserProfileBranding(token));
    }
}
