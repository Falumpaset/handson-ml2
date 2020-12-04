@TypeDefs({
        @TypeDef(defaultForType = Address.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = AdminUserType.class, typeClass = AdminUserTypeConverter.class),
        @TypeDef(defaultForType = ApplicationStatus.class, typeClass = ApplicationStatusConverter.class),
        @TypeDef(defaultForType = AppointmentAcceptanceState.class, typeClass = AppointmentAcceptanceStateConverter.class),
        @TypeDef(defaultForType = AppointmentState.class, typeClass = AppointmentStateConverter.class),
        @TypeDef(defaultForType = Contact.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = CurrencyType.class, typeClass = CurrencyTypeConverter.class),
        @TypeDef(defaultForType = CustomerLocation.class, typeClass = CustomerLocationConverter.class),
        @TypeDef(defaultForType = DiscountDetails.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = DkApprovalLevel.class, typeClass = DkApprovalLevelConverter.class),
        @TypeDef(defaultForType = InvoiceStatus.class, typeClass = InvoiceStatusConverter.class),
        @TypeDef(defaultForType = JsonForm.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = JsonModel.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = LineItem.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = MessageBean.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = PaymentMethod.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = TenantInfo.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = PaymentMethodType.class, typeClass = PaymentMethodTypeConverter.class),
        @TypeDef(defaultForType = Portal.class, typeClass = PortalConverter.class),
        @TypeDef(defaultForType = ProductBasketStatus.class, typeClass = ProductBasketStatusConverter.class),
        @TypeDef(defaultForType = ProductSubscriptionPeriod.class, typeClass = ProductSubscriptionPeriodConverter.class),
        @TypeDef(defaultForType = ProductType.class, typeClass = ProductTypeConverter.class),
        @TypeDef(defaultForType = PropertyProposalState.class, typeClass = PropertyProposalStateConverter.class),
        @TypeDef(defaultForType = S3File.class, typeClass = JsonUserType.class),
        @TypeDef(name = "jsonb-s3files", typeClass = JsonListUserType.class),
        @TypeDef(defaultForType = SelfDisclosureQuestionType.class, typeClass = SelfDisclosureQuestionTypeConverter.class),
        @TypeDef(defaultForType = SelfDisclosureSubQuestionType.class, typeClass = SelfDisclosureSubQuestionTypeConverter.class),
        @TypeDef(defaultForType = ApplicationCustomDataFieldType.class, typeClass = ApplicationCustomDataFieldTypeConverter.class),

        @TypeDef(defaultForType = List.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = Map.class, typeClass = JsonUserType.class),


        @TypeDef(name = "json", typeClass = JsonUserType.class),
        @TypeDef(name = "paymentMethodEnumArray", typeClass = PaymentMethodsEnumArrayConvert.class),
        @TypeDef(name = "paymentmethods", typeClass = PaymentMethodsConverter.class),
        @TypeDef(name = "lineItems", typeClass = LineItemsConverter.class),

        @TypeDef(defaultForType = TimespanType.class, typeClass = TimespanTypeConverter.class),
        @TypeDef(defaultForType = ReportType.class, typeClass = ReportTypeConverter.class),
        @TypeDef(defaultForType = CbiActionType.class, typeClass = CbiActionTypeConverter.class),

        @TypeDef(defaultForType = CreditRatingCheck.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = CreditRatingCheckResponse.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = CreditRatingResponse.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = IdentityCheck.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = IdentityCheckResponse.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = AccountNumberCheck.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = AccountNumberCheckResponse.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = SchufaUserInfo.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = SelfDisclosureSubQuestion.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = ConversationMessageSource.class, typeClass = ConversationMessageSourceConverter.class),
        @TypeDef(defaultForType = ConversationMessageSender.class, typeClass = ConversationMessageSenderConverter.class),
        @TypeDef(defaultForType = LandlordCustomerMailConfig.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = ReportChart.class, typeClass = ReportChartConverter.class),
        @TypeDef(defaultForType = DigitalContractContactInfo.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = DigitalContractSchufaState.class, typeClass = DigitalContractSchufaStateConverter.class),
        @TypeDef(defaultForType = SignerCurrentStateBean.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = ItpCheckRequestBean.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = ItpCheckResponseBean.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = ItpMaskedRequestBean.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = DigitalContractItpState.class, typeClass = DigitalContractItpStateConverter.class),
        @TypeDef(defaultForType = DigitalContractCreateMethod.class, typeClass = DigitalContractCreateMethodConverter.class),
        @TypeDef(defaultForType = FollowupSettingsData.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = LandlordUserFilterSettings.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = FollowupWorkingState.class, typeClass = FollowupStateConverter.class),
        @TypeDef(defaultForType = CustomQuestionScoreBean.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = CustomDataModalSettingsData.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = ApplicationCustomDataFieldBean.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = DkLevelCustomerSettings.class, typeClass = JsonUserType.class),
        @TypeDef(defaultForType = PropertyNoteBean.class, typeClass = JsonUserType.class)
})

/**
 *
 * @author Bastian Bliemeister, Maik Kingma
 *
 */
        package de.immomio.model;

import de.immomio.constants.product.ProductSubscriptionPeriod;
import de.immomio.data.base.bean.customer.PaymentMethod;
import de.immomio.data.base.bean.invoice.DiscountDetails;
import de.immomio.data.base.bean.invoice.LineItem;
import de.immomio.data.base.bean.invoice.LineItemsConverter;
import de.immomio.data.base.bean.schufa.cbi.CbiActionTypeConverter;
import de.immomio.data.base.bean.schufa.cbi.accountNumberCheck.AccountNumberCheck;
import de.immomio.data.base.bean.schufa.cbi.accountNumberCheck.AccountNumberCheckResponse;
import de.immomio.data.base.bean.schufa.cbi.creditRating.CreditRatingCheck;
import de.immomio.data.base.bean.schufa.cbi.creditRating.CreditRatingCheckResponse;
import de.immomio.data.base.bean.schufa.cbi.creditRating.CreditRatingResponse;
import de.immomio.data.base.bean.schufa.cbi.enums.CbiActionType;
import de.immomio.data.base.bean.schufa.cbi.identityCheck.IdentityCheck;
import de.immomio.data.base.bean.schufa.cbi.identityCheck.IdentityCheckResponse;
import de.immomio.data.base.bean.score.CustomQuestionScoreBean;
import de.immomio.data.base.entity.product.ProductSubscriptionPeriodConverter;
import de.immomio.data.base.json.JsonForm;
import de.immomio.data.base.json.JsonListUserType;
import de.immomio.data.base.json.JsonModel;
import de.immomio.data.base.json.JsonUserType;
import de.immomio.data.base.type.addonproduct.ProductType;
import de.immomio.data.base.type.addonproduct.ProductTypeConverter;
import de.immomio.data.base.type.application.ApplicationCustomDataFieldType;
import de.immomio.data.base.type.application.ApplicationCustomDataFieldTypeConverter;
import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.base.type.application.ApplicationStatusConverter;
import de.immomio.data.base.type.contract.DigitalContractCreateMethod;
import de.immomio.data.base.type.contract.DigitalContractCreateMethodConverter;
import de.immomio.data.base.type.conversation.ConversationMessageSender;
import de.immomio.data.base.type.conversation.ConversationMessageSenderConverter;
import de.immomio.data.base.type.conversation.ConversationMessageSource;
import de.immomio.data.base.type.conversation.ConversationMessageSourceConverter;
import de.immomio.data.base.type.customer.CustomerLocation;
import de.immomio.data.base.type.customer.CustomerLocationConverter;
import de.immomio.data.base.type.customer.PaymentMethodType;
import de.immomio.data.base.type.customer.PaymentMethodTypeConverter;
import de.immomio.data.base.type.customer.PaymentMethodsConverter;
import de.immomio.data.base.type.customer.PaymentMethodsEnumArrayConvert;
import de.immomio.data.base.type.invoice.InvoiceStatus;
import de.immomio.data.base.type.invoice.InvoiceStatusConverter;
import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.base.type.portal.PortalConverter;
import de.immomio.data.base.type.price.CurrencyType;
import de.immomio.data.base.type.price.CurrencyTypeConverter;
import de.immomio.data.base.type.product.basket.ProductBasketStatus;
import de.immomio.data.base.type.product.basket.ProductBasketStatusConverter;
import de.immomio.data.base.type.schufa.SchufaUserInfo;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataFieldBean;
import de.immomio.data.landlord.bean.customer.settings.DkLevelCustomerSettings;
import de.immomio.data.landlord.bean.customer.settings.LandlordCustomerMailConfig;
import de.immomio.data.landlord.bean.property.Contact;
import de.immomio.data.landlord.bean.property.dk.DkApprovalLevel;
import de.immomio.data.landlord.bean.property.dk.DkApprovalLevelConverter;
import de.immomio.data.landlord.entity.property.followup.FollowupStateConverter;
import de.immomio.data.landlord.entity.property.followup.FollowupWorkingState;
import de.immomio.data.landlord.entity.property.note.PropertyNoteBean;
import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosureQuestionType;
import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosureQuestionTypeConverter;
import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosureSubQuestion;
import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosureSubQuestionType;
import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosureSubQuestionTypeConverter;
import de.immomio.data.landlord.entity.user.reporting.enums.ReportChart;
import de.immomio.data.landlord.entity.user.reporting.enums.ReportChartConverter;
import de.immomio.data.landlord.entity.user.settings.CustomDataModalSettingsData;
import de.immomio.data.landlord.entity.user.settings.FollowupSettingsData;
import de.immomio.data.landlord.entity.user.settings.LandlordUserFilterSettings;
import de.immomio.data.propertysearcher.entity.itp.ItpCheckRequestBean;
import de.immomio.data.propertysearcher.entity.itp.ItpCheckResponseBean;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.data.shared.bean.contract.DigitalContractContactInfo;
import de.immomio.data.shared.entity.appointment.AppointmentState;
import de.immomio.data.shared.entity.appointment.AppointmentStateConverter;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptanceState;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptanceStateConverter;
import de.immomio.data.shared.entity.contract.signer.SignerCurrentStateBean;
import de.immomio.data.shared.entity.contract.signer.history.aes.itp.DigitalContractItpState;
import de.immomio.data.shared.entity.contract.signer.history.aes.itp.DigitalContractItpStateConverter;
import de.immomio.data.shared.entity.contract.signer.history.aes.itp.ItpMaskedRequestBean;
import de.immomio.data.shared.entity.contract.signer.history.aes.schufa.DigitalContractSchufaState;
import de.immomio.data.shared.entity.contract.signer.history.aes.schufa.DigitalContractSchufaStateConverter;
import de.immomio.data.shared.entity.email.MessageBean;
import de.immomio.data.shared.entity.property.TenantInfo;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposalState;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposalStateConverter;
import de.immomio.model.entity.admin.report.ReportType;
import de.immomio.model.entity.admin.report.ReportTypeConverter;
import de.immomio.model.entity.admin.report.TimespanType;
import de.immomio.model.entity.admin.report.TimespanTypeConverter;
import de.immomio.model.entity.admin.user.AdminUserType;
import de.immomio.model.entity.admin.user.AdminUserTypeConverter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import java.util.List;
import java.util.Map;
