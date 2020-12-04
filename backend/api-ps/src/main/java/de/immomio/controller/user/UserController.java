package de.immomio.controller.user;

import de.immomio.beans.ChangeEmailBean;
import de.immomio.beans.ChangePasswordBean;
import de.immomio.beans.ResetPasswordBean;
import de.immomio.beans.ResetPasswordConfirmBean;
import de.immomio.beans.TokenBean;
import de.immomio.controller.BaseController;
import de.immomio.data.propertysearcher.bean.user.PropertySearcherUserRegisterResultBean;
import de.immomio.data.propertysearcher.entity.user.ProfileCompletenessResponseBean;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherChangeEmail;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherChangePassword;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.propertysearcher.bean.EditUserProfileBean;
import de.immomio.propertysearcher.bean.PropertySearcherRegisterBean;
import de.immomio.service.propertySearcher.PropertySearcherUserService;
import de.immomio.service.propertySearcher.change.PropertySearcherChangeEmailService;
import de.immomio.service.propertySearcher.change.PropertySearcherChangePasswordService;
import de.immomio.service.propertySearcher.customer.PropertySearcherCustomerService;
import de.immomio.service.propertysearcher.PropertySearcherSearchUntilCalculationService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RepositoryRestController
@RequestMapping(value = "/users")
public class UserController extends BaseController {


    private final PropertySearcherChangePasswordService changePasswordService;

    private final PropertySearcherChangeEmailService changeEmailService;
    private final PropertySearcherUserService userService;
    private final PropertySearcherCustomerService customerService;
    private final PropertySearcherSearchUntilCalculationService searchUntilService;

    @Autowired
    public UserController(
            PropertySearcherChangePasswordService changePasswordService,
            PropertySearcherChangeEmailService changeEmailService,
            PropertySearcherUserService userService,
            PropertySearcherCustomerService customerService, PropertySearcherSearchUntilCalculationService searchUntilService) {
        this.changePasswordService = changePasswordService;
        this.changeEmailService = changeEmailService;
        this.userService = userService;
        this.customerService = customerService;
        this.searchUntilService = searchUntilService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> register(@RequestBody PropertySearcherRegisterBean registerBean) {
        PropertySearcherUserRegisterResultBean registerResultBean = userService.register(registerBean);

        return ResponseEntity.ok(registerResultBean.getToken());
    }

    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public @ResponseBody
    PersistentEntityResource me(@Parameter(hidden = true) PersistentEntityResourceAssembler assembler) {
        //TODO small hack to update lastLogin for sure since some callback in FE does not trigger 100% - remove in
        // future
        PropertySearcherUserProfile userProfile = userService.setLastLogin();

        return assembler.toModel(userProfile);
    }

    @PreAuthorize("#userProfile.user.id == principal?.id")
    @PostMapping("/editprofile/{id}")
    public ResponseEntity<Object> edit(@PathVariable("id") PropertySearcherUserProfile userProfile, @RequestBody EditUserProfileBean editUserBean) {
        userService.editProfile(userProfile, editUserBean);

        return ResponseEntity.accepted().build();
    }

    @PutMapping(value = "/email")
    public ResponseEntity<Object> changeEmail(@RequestBody ChangeEmailBean email, @Parameter(hidden = true) PersistentEntityResourceAssembler assembler) {
        PropertySearcherChangeEmail changeEmail = changeEmailService.changeEmail(email);

        return ResponseEntity.accepted().body(assembler.toModel(changeEmail));
    }

    @GetMapping(value = "/email/confirm")
    public ResponseEntity<Object> confirmEmail(@RequestParam String token) {
        changeEmailService.confirmEmail(token);

        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/password")
    public ResponseEntity<Object> password(@RequestBody ChangePasswordBean changePassword) {
        boolean passwordChanged = changePasswordService.changePassword(changePassword);

        return passwordChanged ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PostMapping(value = "/reset-password")
    public ResponseEntity<Object> resetPassword(@RequestBody ResetPasswordBean resetPassword,
            @Parameter(hidden = true) PersistentEntityResourceAssembler assembler) {
        PropertySearcherChangePassword changePassword = changePasswordService.resetPassword(resetPassword.getEmail());

        return ResponseEntity.accepted().body(assembler.toModel(changePassword));
    }

    @PostMapping(value = "/reset-password/confirm")
    public ResponseEntity<Object> resetPassword(@RequestBody ResetPasswordConfirmBean changePassword) {
        boolean passwordChanged = changePasswordService.resetPassword(changePassword);

        return passwordChanged ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PreAuthorize("#userProfile.user.id == principal?.id")
    @DeleteMapping(value = "/remove/{userProfile}")
    public ResponseEntity<Void> deleteRequest(@PathVariable("userProfile") PropertySearcherUserProfile userProfile) {
        customerService.delete(userProfile);

        return ResponseEntity.accepted().build();
    }

    @PostMapping(value = "/resend-email-verification/{userProfile}")
    public ResponseEntity<Void> resendEmailVerification(@PathVariable PropertySearcherUserProfile userProfile) {
        userService.resendEmailVerification(userProfile);

        return ResponseEntity.accepted().build();
    }

    @PostMapping(value = "/verify-email/{userID}")
    public ResponseEntity<Void> verifyEmail(@PathVariable Long userID, @RequestBody TokenBean emailVerification) {
        userService.verifyEmail(userID, emailVerification.getToken());

        return ResponseEntity.accepted().build();
    }

    @PostMapping(value = "/unlock")
    public ResponseEntity<Void> unlockUser(@RequestBody TokenBean tokenBean) {
        userService.unlockUser(tokenBean);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/updateLastLogin")
    public ResponseEntity<Void> updateLastLogin() {
        userService.updateLastLoginAndSearch();

        return ResponseEntity.accepted().build();
    }

    @GetMapping(value = "/profileCompleteness")
    public ResponseEntity<ProfileCompletenessResponseBean> getProfileCompleteness() {
        ProfileCompletenessResponseBean profileCompletenessResponseBean = userService.calculateProfileCompleteness();

        return ResponseEntity.ok(profileCompletenessResponseBean);
    }

    @PostMapping(value = "/extendSearchUntil")
    public ResponseEntity<Void> extendSearchUntil(@RequestParam("searching") boolean searching, @RequestBody TokenBean tokenBean) {
        userService.updateSearchUntil(tokenBean, searching);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/reactivateUser")
    public ResponseEntity<Void> extendSearchUntil() {
        userService.updateSearchUntil();

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/firstSocialLogin")
    public ResponseEntity<Boolean> getUserExists(@RequestParam("email") String email) {

        return ResponseEntity.ok(userService.isFirstSocialLogin(email));
    }

    @PostMapping(value = "unlockTenantPool")
    public ResponseEntity<Void> unlockTenantPool() {
        userService.resetInternalTenantPool();

        return ResponseEntity.ok().build();
    }

}
