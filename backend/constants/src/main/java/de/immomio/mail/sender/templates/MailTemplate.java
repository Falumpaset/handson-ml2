package de.immomio.mail.sender.templates;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum MailTemplate {

    /**
     * Web Mail Templates
     */

    /**
     * Commercial
     **/
    COMMERCIAL_NEW_ADMIN("", true, new String[]{"COMMERCIAL_NEW_ADMIN"}, "d-7197409b8ea1403dbf555ae3ba9083e7", MailType.LandlordUser),

    COMMERCIAL_NEW_AGENT("/mail-templates/commercial/new-agent.vm", true, new String[]{"COMMERCIAL_NEW_AGENT"},"d-a94efa96f1994210a8996958abb72257", MailType.LandlordUser),
    COMMERCIAL_NEW_PROPERTY_MANAGER(null, false, new String[]{"COMMERCIAL_NEW_PROPERTY_MANAGER"},"d-a26a7a16af164ef5b24fc9465664db08", MailType.LandlordUser),

    REGISTER_USER("/mail-templates/profile/register-user.vm", false, new String[]{"REGISTER_USER"}, "d-1ab7ad732a0a4f5e9de0dfaca2575737", MailType.PropertysearcherUser),

    CHANGED_PASSWORD("/mail-templates/profile/changed-password.vm", false, new String[]{"CHANGED_PASSWORD"},"d-d4aa808985f449f38577ed3da9fdd5f2", MailType.User),
    NEW_EMAIL("/mail-templates/profile/new-email.vm", false, new String[]{"NEW_EMAIL"},"d-b99e6542497d4248b59beb60b63103e4", MailType.User),
    NEW_EMAIL_CHANGED("/mail-templates/profile/new-email-changed.vm", false, new String[]{"NEW_EMAIL_CHANGED"},"d-dafefa513a8148faa6879c854fbecef4", MailType.User),
    RESET_PASSWORD("/mail-templates/profile/reset-password.vm", false, new String[]{"RESET_PASSWORD"},"d-4360f0195b55477cb226602c9116922b", MailType.User),
    EMAIL_VERIFICATION("/mail-templates/profile/email-verification.vm", false, new String[]{"EMAIL_VERIFICATION"}, "d-96468d19be9f4b48b0d55250575850ef", MailType.User),
    EMAIL_VERIFICATION_LANDLORD("/mail-templates/profile/email-verification.vm", false, new String[]{"EMAIL_VERIFICATION_LANDLORD"}, "d-832fb5316a9f4fa4a0712acf6239e031", MailType.User),
    USER_SEARCHING_INQUIRY("/mail-templates/profile/user-searching-inquiry.vm", false, new String[]{"USER_SEARCHING_INQUIRY"},"d-96d1eeeee0c6440085cacc795a7190c2", MailType.PropertysearcherUser),

    /**
     * Schufa Report Templates
     **/
    SCHUFA_UPDATE("", false, new String[]{"SCHUFA_UPDATE"}, "d-d26dfd446b004edda81cef7e46645d91", MailType.LandlordUser),

    /**
     * Application Templates
     */
    APPLICATION_DAILY_UPDATE("/mail-templates/application/daily-update.vm", false, new String[]{"APPLICATION_DAILY_UPDATE"}, MailType.LandlordUser),

    /**
     * Flat Templates
     */
    FLAT_UNPUBLISHED("/mail-templates/flat/unpublished.vm", false, new String[]{"FLAT_UNPUBLISHED"}, MailType.LandlordUser),

    /**
     * Commercial
     */
    COMMERCIAL_MONTHLY_PRODUCT_INVOICE("/mail-templates/commercial/monthly-product-invoice.vm", false, new String[]{"COMMERCIAL_MONTHLY_PRODUCT_INVOICE"}, MailType.LandlordUser),
    COMMERCIAL_MONTHLY_PRODUCT_INVOICE_CANCELLATION(
            "/mail-templates/commercial/monthly-product-invoice-cancellation.vm", false, new String[]{"COMMERCIAL_MONTHLY_PRODUCT_INVOICE_CANCELLATION"}, MailType.LandlordUser),
    COMMERCIAL_NEW_PLAN_INVOICE("/mail-templates/commercial/new-plan.vm", false, new String[]{"COMMERCIAL_NEW_PLAN_INVOICE"}, MailType.LandlordUser),
    COMMERCIAL_ADD_HOMEPAGE_MODULE("/mail-templates/commercial/add-homepage-module.vm", false, new String[]{"COMMERCIAL_ADD_HOMEPAGE_MODULE"}, MailType.LandlordUser),
    COMMERCIAL_REMOVE_HOMEPAGE_MODULE("/mail-templates/commercial/remove-homepage-module.vm", false, new String[]{"COMMERCIAL_REMOVE_HOMEPAGE_MODULE"}, MailType.LandlordUser),

    /**
     * Invoice
     */
    INVOICE_BOOKING("/mail-templates/invoice/booking.vm", false, new String[]{"INVOICE_BOOKING"},"d-cf1f0721158d469aa92c8f2764f75d86", MailType.LandlordUser),
    INTERNAL_INVOICE_HAS_BEEN_GENERATED(null, false, new String[]{"INTERNAL_INVOICE_HAS_BEEN_GENERATED"},"d-7c0285b4f789456984527a6223cbb0de", MailType.Developer),

    /**
     * FlatApplication Feedback-Email
     */
    APPLICATION_FEEDBACK("/mail-templates/application/feedback.vm", false, new String[]{"APPLICATION_FEEDBACK"}, MailType.PropertysearcherUser),

    /**
     * Application-Mailbox Warning
     */
    APPMAILBOX_WARNING("/mail-templates/appMailboxWarning.vm", false, new String[]{"APPMAILBOX_WARNING"}, MailType.Developer),

    /**
     * Web Mail Templates
     */
    INCOMPLETE_PROFILE("/mail-templates/profile/incomplete.vm", false, new String[]{"INCOMPLETE_PROFILE"},"d-4e236291270248ada85271a195d590e6", MailType.PropertysearcherUser),

    /**
     * PS Account Templates
     */
    ACCOUNT_LOCKED_NOTIFICATION("", false, new String[]{"ACCOUNT_LOCKED_NOTIFICATION"}, "d-01894d820c264d8ea3701bfd8d1cab85", MailType.PropertysearcherUser),
    VERIFICATION_REQUIRED_WARNING("", false, new String[]{"VERIFICATION_REQUIRED_WARNING"}, "d-5c239495245c4e87b0ef99804b1f7296", MailType.PropertysearcherUser),

    /**
     * Welcome Mail Templates
     */
    APPLICATION_NEW("/mail-templates/application/application-new.vm", true, new String[]{"APPLICATION_NEW"},"d-127768604a224ea9b68ccf53b34ab6ec", MailType.PropertysearcherUser),
    APPLICATION_KNOWN("/mail-templates/application/application-known.vm", true, new String[]{"APPLICATION_KNOWN"},"d-98302cbcbb1d4446a96a29add426ca3a", MailType.PropertysearcherUser),
    APPLICATION_NEW_OBJECT_TYPE(null, true, new String[]{"APPLICATION_NEW_OBJECT_TYPE"},"d-71a2171f7f58481eb0c68050ca337f51", MailType.PropertysearcherUser),
    /*Is not sent anymore*/ APPLICATION_REMINDER("/mail-templates/application/reminder.vm", false, new String[]{}, MailType.LandlordUser),

    /**
     * Application Templates
     */
    APPLICATION_CONFIRMED("/mail-templates/application/confirmed.vm", true, new String[]{"APPLICATION_CONFIRMED"},"d-61c017218b9446ea80ac66b5bbfea831", MailType.PropertysearcherUser),

    INVITE_ANONYMOUS_USER_TO_REGISTER("", true, new String[]{"INVITE_ANONYMOUS_USER_TO_REGISTER"}, "d-3d2bcc0fb8fd434b996c1326cf1b8e32", MailType.PropertysearcherUser),

    APPLICATION_ACCEPTED_V1("/mail-templates/application/accepted.vm", true, new String[]{"APPLICATION_ACCEPTED"},
            MailGroup.APPLICATION_ACCEPT, 1,"d-e7f89190733e4e3291c64a9720fed1e2", MailType.PropertysearcherUser),
    APPLICATION_ACCEPTED_V2("/mail-templates/application/accepted-v2.vm", true, new String[]{"APPLICATION_ACCEPTED"},
            MailGroup.APPLICATION_ACCEPT, 2,"d-4660d052ee5548d1aad7048a2da28018", MailType.PropertysearcherUser),
    APPLICATION_ACCEPTED_V3("/mail-templates/application/accepted-v3.vm", true, new String[]{"APPLICATION_ACCEPTED"},
            MailGroup.APPLICATION_ACCEPT, 3,"d-0329cd4324f8424d96ada7ffb47ee81f", MailType.PropertysearcherUser),

    APPLICATION_REJECTED_V1("/mail-templates/application/rejected.vm", true, new String[]{"APPLICATION_REJECTED"},
            MailGroup.APPLICATION_REJECT, 1,"d-4af2ae72ca314cb891adb0efc7aa71e0", MailType.PropertysearcherUser),
    APPLICATION_REJECTED_V2("/mail-templates/application/rejected-v2.vm", true, new String[]{"APPLICATION_REJECTED"},
            MailGroup.APPLICATION_REJECT, 2,"d-8a4b320999fb471f8a27f559d928efce", MailType.PropertysearcherUser),
    APPLICATION_REJECTED_V3("/mail-templates/application/rejected-v3.vm", true, new String[]{"APPLICATION_REJECTED"},
            MailGroup.APPLICATION_REJECT, 3,"d-9c457ac0ecf64c87ae7c76e6b0c893dc", MailType.PropertysearcherUser),

    APPLICATION_UNREJECTED("/mail-templates/application/unrejected.vm", true, new String[]{"APPLICATION_UNREJECTED"},"d-0ccfed7cbc9846be8b59a0916037cbb8", MailType.PropertysearcherUser),
    /**
     * Invitation Templates
     */
    INVITATION_CREATED("/mail-templates/invitation/created.vm", true, new String[]{"INVITATION_CREATED"},"d-9bfd79a4e3c64003a0229d6a9e34e305", MailType.PropertysearcherUser),
    EXCLUSIVE_INVITATION_CREATED(null, true, new String[]{"EXCLUSIVE_INVITATION_CREATED"},"d-e8f81dde6c77470e9da8a066589f2ab7", MailType.PropertysearcherUser),
    EXCLUSIVE_INVITATION_CANCELED_TO_LL(null, false, new String[]{"EXCLUSIVE_INVITATION_CANCELED_TO_LL"},"d-f0d0577a4f8a4548829a8eb26551e0cf", MailType.LandlordUser),
    EXCLUSIVE_INVITATION_CANCELED_TO_PS(null, true, new String[]{"EXCLUSIVE_INVITATION_CANCELED_TO_PS"},"d-2d95debf61514e5b8552889fc01e9f06", MailType.PropertysearcherUser),
    VIEWING_CHANGED(null, true, new String[]{"VIEWING_CHANGED"}, "d-7f4dd17f571e490b9de078b60894fc9b", MailType.PropertysearcherUser),
    INVITATION_CANCELED("/mail-templates/invitation/canceled.vm", true, new String[]{"INVITATION_CANCELED"},"d-91c1ef00e36c4ccfa68a981e6711fa88", MailType.PropertysearcherUser),

    INVITATION_ACCEPTED("/mail-templates/invitation/accepted.vm", true, new String[]{"INVITATION_ACCEPTED"}, "d-7c078963489f471babb40f5749847d23", MailType.PropertysearcherUser),
    INVITATION_DECLINED("/mail-templates/invitation/declined.vm", false, new String[]{"INVITATION_DECLINED"}, MailType.PropertysearcherUser),

    INVITATION_REMINDER("/mail-templates/invitation/reminder.vm", true, new String[]{"INVITATION_REMINDER"},"d-5bf0dc2f32584370854c59d0c77c15b2", MailType.PropertysearcherUser),

    INTENT_REMINDER("/mail-templates/appointment/intent-reminder.vm", true, new String[]{"INTENT_REMINDER"},"d-e73513fea5234794a2555d6ccffc4e21", MailType.PropertysearcherUser),

    INVITATION_DIRECT("/mail-templates/invitation/invitation-direct.vm", true, new String[]{"INVITATION_DIRECT"},"d-61efbffdff4348f48b6ba67ce3adca11", MailType.PropertysearcherUser),

    NEW_PROPERTY_FOR_PROSPECT("/mail-templates/flat/new-for-prospect.vm", true, new String[]{"NEW_PROPERTY_FOR_PROSPECT"}, "d-1b6d5e7c3e2546a9a0300eb4b3da2218", MailType.PropertysearcherUser),
    
    FLAT_ACCEPTED("/mail-templates/flat/accepted.vm", true, new String[]{"FLAT_ACCEPTED"},"d-d41f5583083f4089a913f390be5eea4b", MailType.PropertysearcherUser),

    INVITED_TO_VIEWING("/mail-templates/flat/invite-to-viewings.vm", true, new String[]{"INVITED_TO_VIEWING"},"d-493cfc3c30ea4025b217da3fdd7ab24e", MailType.PropertysearcherUser),

    INVITATION_ACCEPTANCE_NOTIFICATION("/mail-templates/invitation/acceptance-notification.vm", true,
            new String[]{"INVITATION_ACCEPTANCE_NOTIFICATION"},"d-dcab61e32fca43889e2e1de3188ce05e", MailType.LandlordUser),

    INVITATION_DECLINE_NOTIFICATION("/mail-templates/invitation/decline-notification.vm", true,
            new String[]{"INVITATION_DECLINE_NOTIFICATION"},"d-e4c9beedeca94c189e3490acbb5b1021", MailType.LandlordUser),
    INVITATION_SUMMARY("/mail-templates/invitation/summary.vm", true, new String[]{"INVITATION_SUMMARY"},"d-8d1c2caba4d2441b8c75cea736040bc8", MailType.LandlordUser),

    APPOINTMENT_PARTICIPANTS(null, true, new String[]{"APPOINTMENT_PARTICIPANTS"}, "d-f03336b7565d46d084525e58bc15a027", MailType.LandlordUser),
    PRE_TENANT_APPOINTMENT(null, true, new String[]{"PRE_TENANT_APPOINTMENT"}, "d-4d4665f6b9fd4cdab8b0c6135fdbb84c", MailType.PropertysearcherUser),

    SELF_DISCLOSURE_FEEDBACK_EMAIL(null, true, new String[]{"SELF_DISCLOSURE_FEEDBACK_EMAIL"}, "d-dadac6acd54e4d578b58a64ca4c435bc", MailType.PropertysearcherUser),
    MESSENGER_PS_NEW_MESSAGE_NOTIFICATION(null, false, new String[]{"MESSENGER_PS_NEW_MESSAGE_NOTIFICATION"}, "d-f995d43085154a7ca10e1426e29ca183", MailType.PropertysearcherUser),
    MESSENGER_PS_NEW_MESSAGE_DIRECT(null, false, new String[]{"MESSENGER_PS_NEW_MESSAGE_DIRECT"}, "d-8e0d612206424a12acb0224bf1c7e2b2", MailType.PropertysearcherUser),
    MESSENGER_PS_NEW_MESSAGE_DIRECT_WITHOUT_ANSWER(null, false, new String[]{"MESSENGER_PS_NEW_MESSAGE_DIRECT"}, "d-9bbca0cd1ad34a0595745d94bd327f30", MailType.PropertysearcherUser),
    INCOMING_MESSAGE_ID_NOT_PARSEABLE_TO_PS(null, false, new String[]{"INCOMING_MESSAGE_ID_NOT_PARSEABLE_TO_PS"}, "d-e0e87ae24d844ffb9d338474beda8179", MailType.PropertysearcherUser),

    //digital contract
    DIGITAL_CONTRACT_TENANT_SIGN(null, true, new String[]{"DIGITAL_CONTRACT_TENANT_SIGN"}, "d-83ea40ac83b04af0ad92d9bdf668604b", MailType.PropertysearcherUser),
    DIGITAL_CONTRACT_LANDLORD_SIGN(null, false, new String[]{"DIGITAL_CONTRACT_TENANT_SIGN"}, "d-65768ee045734f1cb9307a66ac1cf88a", MailType.LandlordUser),
    DIGITAL_CONTRACT_LL_AGENT_MUST_REVIEW_DATA(null, false, new String[]{"DIGITAL_CONTRACT_TENANT_SIGN"}, "d-a6225e78d4774de58c50f395607c3c1d", MailType.LandlordUser),
    DIGITAL_CONTRACT_LL_AGENT_FLAT_NOT_VISITED(null, false, new String[]{"DIGITAL_CONTRACT_TENANT_SIGN"}, "d-d7f3d919a550425d80ea66961b075ded", MailType.LandlordUser),
    DIGITAL_CONTRACT_LL_SIGNER_FLAT_NOT_VISITED(null, false, new String[]{"DIGITAL_CONTRACT_TENANT_SIGN"}, "d-55ba56393bbd4f719b694c9974915d42", MailType.LandlordUser),
    DIGITAL_CONTRACT_FINISHED_PS(null, true, new String[]{"DIGITAL_CONTRACT_FINISHED_PS"}, "d-d92de04fa3f54ddfa64bbf2dfe9054f2", MailType.PropertysearcherUser),
    DIGITAL_CONTRACT_FINISHED_LL(null, false, new String[]{"DIGITAL_CONTRACT_FINISHED_LL"}, "d-315e88ec75fd4009b00687c247717492", MailType.LandlordUser),
    DIGITAL_CONTRACT_DELETED_LL(null, false, new String[]{"DIGITAL_CONTRACT_DELETED_LL"}, "d-195cc38885044f9ca80db062dcaaad17", MailType.LandlordUser),
    DIGITAL_CONTRACT_PS_DATA_WRONG(null, true, new String[]{"DIGITAL_CONTRACT_PS_DATA_WRONG"}, "d-b804253c5b6e4e07948356fc60646152", MailType.PropertysearcherUser),
    DIGITAL_CONTRACT_PS_DATA_WRONG_TO_LL(null, false, new String[]{"DIGITAL_CONTRACT_PS_DATA_WRONG_TO_LL"}, "d-49b280dcdd15464d976936cf19d7d09a", MailType.LandlordUser),
    DIGITAL_CONTRACT_PS_CONTINUE_SIGNING(null, false, new String[]{"DIGITAL_CONTRACT_PS_CONTINUE_SIGNING"}, "d-da1ae0ca934443c19b07485a3c4915e4", MailType.PropertysearcherUser),
    DIGITAL_CONTRACT_PS_AES_CODE_READY(null, true, new String[]{"DIGITAL_CONTRACT_PS_AES_CODE_READY"}, "d-403f3e5fbf8748199bbdc0bc5d372bef", MailType.PropertysearcherUser),
    DIGITAL_CONTRACT_PS_AES_CODE_INSTANT_TRANSFER_FAILED(null, true, new String[]{"DIGITAL_CONTRACT_PS_AES_CODE_INSTANT_TRANSFER_FAILED"}, "d-f881cfe511d041688bae88eff15c49f2", MailType.PropertysearcherUser),
    DIGITAL_CONTRACT_PS_AES_FAILED_TO_LL(null, false, new String[]{"DIGITAL_CONTRACT_PS_AES_FAILED_TO_LL"}, "d-a3b9c24605b3486aab3c7eb8df9f4dc3", MailType.LandlordUser),
    DIGITAL_CONTRACT_PS_AES_FAILED_TO_LL_AGENT(null, false, new String[]{"DIGITAL_CONTRACT_PS_AES_FAILED_TO_LL_AGENT"}, "d-148d5dd467444326b9ad573bba22b0ac", MailType.LandlordUser),

    DIGITAL_CONTRACT_INTERNAL_BOOKED(null, false, new String[]{"DIGITAL_CONTRACT_INTERNAL_BOOKED"}, "d-d5af7dcbcc3b48e1829838ff57971ab5", MailType.Developer),
    DIGITAL_CONTRACT_INTERNAL_ITP_FAILED(null, false, new String[]{"DIGITAL_CONTRACT_ITP_FAILED"}, "d-2c25a7e51cb24350915e03b1be39601f", MailType.Developer),
    DIGITAL_CONTRACT_INTERNAL_ITP_WARN(null, false, new String[]{"DIGITAL_CONTRACT_ITP_FAILED"}, "d-62d5e55845dc48dd86290b25008cc9e4", MailType.Developer),
    FOLLOWUP_NOTIFICATION(null, false, new String[]{"FOLLOWUP_NOTIFICATION"}, "d-75c532f94ae74034ba44df64ade06ee8", MailType.LandlordUser),

    PROPERTY_EXPOSE_NOTIFICATION_V1(null, true, new String[]{"PROPERTY_EXPOSE_NOTIFICATION"}, "d-80436c263d294bd5be2a04a9e2fcaf05", MailType.PropertysearcherUser),
    PROPERTY_EXPOSE_NOTIFICATION_V2(null, true, new String[]{"PROPERTY_EXPOSE_NOTIFICATION"}, "d-b5e917c33a3948fe9f2043f8601f8be4", MailType.PropertysearcherUser),
    PROPERTY_EXPOSE_NOTIFICATION_V3(null, true, new String[]{"PROPERTY_EXPOSE_NOTIFICATION"}, "d-76fb0470a03c4efda4dc107e5d12c822", MailType.PropertysearcherUser),
    PROPERTY_EXPOSE_NOTIFICATION_V4(null, true, new String[]{"PROPERTY_EXPOSE_NOTIFICATION"}, "d-b10149bdb67a484fb226581b6a7c62f5", MailType.PropertysearcherUser),
    PROPERTY_EXPOSE_NOTIFICATION_V5(null, true, new String[]{"PROPERTY_EXPOSE_NOTIFICATION"}, "d-66f573cdb48e473ba977e3624f6f16b6", MailType.PropertysearcherUser),
    PROPERTY_EXPOSE_NOTIFICATION_V6(null, true, new String[]{"PROPERTY_EXPOSE_NOTIFICATION"}, "d-e4786f80a715455e813d28c8210741ec", MailType.PropertysearcherUser),
    PROPERTY_MANAGER_NEW_PROPERTY_ASSIGNED(null, false, new String[]{"PROPERTY_MANAGER_NEW_APPOINTMENT"}, "d-65f81b2299964bafa5657a33dbb2608a", MailType.LandlordUser),

    /**
     * Guest Templates
     */

    GUEST_APPLICATION_CONFIRMATION(true, new String[]{"GUEST_APPLICATION_CONFIRMATION"}, "d-ebf99b93ef274d5f846c2084034a1ebb", MailType.PropertysearcherUser),
    GUEST_ALREADY_REGISTERED_REMINDER(true, new String[]{"GUEST_ALREADY_REGISTERED_REMINDER"}, "d-91f5c4310d7949a69b69da8846223092", MailType.PropertysearcherUser),
    GUEST_INTENT_REMINDER(false, new String[]{"GUEST_INTENT_REMINDER"}, "d-a760cd74733a400d91174e833359fad0", MailType.PropertysearcherUser),

    CUSTOM_DATA_MODAL_EXCEL(false, new String[]{"CUSTOM_DATA_MODAL_EXCEL"}, "d-d2ab2f2bab1e4716a900f9520620ab4a", MailType.LandlordUser);

    @Getter
    private String templateFile;

    @Getter
    private boolean configurable;

    @Getter
    private String[] categories;

    @Getter
    private MailGroup group;

    @Getter
    private int version = 1;

    @Getter
    private String sendGridTemplate;

    @Getter
    private MailType mailType;

    MailTemplate(boolean configurable, String[] categories, String sendGridTemplate, MailType mailType) {
        this.configurable = configurable;
        this.categories = categories;
        this.sendGridTemplate = sendGridTemplate;
        this.mailType = mailType;
    }

    MailTemplate(String templateFile, boolean configurable, String[] categories, String sendGridTemplate, MailType mailType) {
        this.templateFile = templateFile;
        this.configurable = configurable;
        this.categories = categories;
        this.sendGridTemplate = sendGridTemplate;
        this.mailType = mailType;
    }

    MailTemplate(String templateFile, boolean configurable, String[] categories, MailType mailType) {
        this.templateFile = templateFile;
        this.configurable = configurable;
        this.categories = categories;
        this.mailType = mailType;
    }

    MailTemplate(String templateFile, boolean configurable, String[] categories, MailGroup mailGroup, int version) {
        this.templateFile = templateFile;
        this.configurable = configurable;
        this.categories = categories;
        this.group = mailGroup;
        this.version = version;
    }

    MailTemplate(String templateFile, boolean configurable, String[] categories, MailGroup mailGroup, int version, String sendGridTemplate, MailType mailType) {
        this.templateFile = templateFile;
        this.configurable = configurable;
        this.categories = categories;
        this.group = mailGroup;
        this.version = version;
        this.sendGridTemplate = sendGridTemplate;
        this.mailType = mailType;
    }

    public static List<MailTemplate> getConfigurableTemplates() {
        return Arrays.stream(MailTemplate.values()).filter(MailTemplate::isConfigurable).collect(Collectors.toList());
    }
}
