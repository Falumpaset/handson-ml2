package de.immomio.controller.constants;

import de.immomio.beans.landlord.application.CustomQuestionFilterType;
import de.immomio.beans.shared.PropertySearcherHistoryType;
import de.immomio.beans.shared.contract.DigitalContractSignerSimpleState;
import de.immomio.beans.shared.contract.DigitalContractSimpleState;
import de.immomio.constants.GenderType;
import de.immomio.constants.Location;
import de.immomio.constants.SolvencyType;
import de.immomio.constants.customer.Title;
import de.immomio.constants.property.EnergyClassType;
import de.immomio.data.base.bean.schufa.cbi.enums.SchufaReportType;
import de.immomio.data.base.type.common.FileType;
import de.immomio.data.base.type.common.ParkingType;
import de.immomio.data.base.type.conversation.ConversationMessageSender;
import de.immomio.data.base.type.conversation.ConversationMessageSource;
import de.immomio.data.base.type.conversationTemplate.ConversationMessageTemplateSubstitutionTag;
import de.immomio.data.base.type.credential.CredentialProperty;
import de.immomio.data.base.type.customer.LandlordCustomerPreference;
import de.immomio.data.base.type.customer.LandlordCustomerSize;
import de.immomio.data.base.type.customer.LandlordCustomerType;
import de.immomio.data.base.type.customer.PaymentMethodType;
import de.immomio.data.base.type.customer.settings.LandlordCustomerApplicationArchiveUnit;
import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.base.type.product.basket.ProductBasketProperty;
import de.immomio.data.base.type.product.limitation.LandlordLimitationEnum;
import de.immomio.data.base.type.property.BuildingConditionType;
import de.immomio.data.base.type.property.EmploymentType;
import de.immomio.data.base.type.property.FlatType;
import de.immomio.data.base.type.property.FurnishingType;
import de.immomio.data.base.type.property.GarageType;
import de.immomio.data.base.type.property.GroundType;
import de.immomio.data.base.type.property.HeaterFiringType;
import de.immomio.data.base.type.property.HeaterType;
import de.immomio.data.base.type.property.HouseType;
import de.immomio.data.base.type.property.HouseholdType;
import de.immomio.data.base.type.property.ObjectType;
import de.immomio.data.base.type.property.OfficeType;
import de.immomio.data.base.type.property.PersonalStatus;
import de.immomio.data.base.type.property.PropertyType;
import de.immomio.data.base.type.property.PropertyWriteProtection;
import de.immomio.data.base.type.schufa.JobState;
import de.immomio.data.base.type.user.PropertySearcherUserType;
import de.immomio.data.landlord.bean.property.certificate.EnergyCertificate.CertificateCreationDate;
import de.immomio.data.landlord.bean.property.certificate.EnergyCertificate.EnergyCertificateType;
import de.immomio.data.landlord.entity.property.portal.PropertyPortalState;
import de.immomio.data.landlord.entity.property.publish.PublishState;
import de.immomio.data.landlord.entity.user.LandlordUserPreference;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.data.landlord.entity.user.reporting.enums.ReportChart;
import de.immomio.data.landlord.entity.user.reporting.enums.ReportChartCategory;
import de.immomio.data.landlord.entity.user.reporting.enums.ReportChartDataType;
import de.immomio.data.landlord.entity.user.reporting.enums.ReportChartType;
import de.immomio.data.propertysearcher.entity.product.limitation.PropertySearcherLimitationEnum;
import de.immomio.data.propertysearcher.entity.user.MissingProfileField;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherSearchProfileType;
import de.immomio.data.shared.entity.contract.signer.history.aes.schufa.DigitalContractSchufaState;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposalState;
import de.immomio.exporter.config.FtpType;
import de.immomio.exporter.immoscout.translator.Immoscout24FlooringType;
import de.immomio.mail.sender.templates.MailGroup;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.security.openid.LoginMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilderFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(value = "/constants")
public class ConstantsController {

    @Autowired
    private WebMvcLinkBuilderFactory webMvcLinkBuilderFactory;

    @SuppressWarnings({"rawtypes", "unchecked"})
    @GetMapping(value = {"/", ""})
    public ResponseEntity overview(HttpServletRequest request, HttpServletResponse response)
            throws IllegalArgumentException {


        List<Link> links = new ArrayList<>();

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).propertyWriteProtection())
                .withRel("propertyWriteProtection"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).propertyState())
                .withRel("propertyState"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).landlordCustomerPreference())
                .withRel("landlordCustomerPreference"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).landlordUserPreference())
                .withRel("landlordUserPreference"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).landlordCustomerType())
                .withRel("landlordCustomerType"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).landlordUserType())
                .withRel("landlordUserType"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).loginMethod())
                .withRel("loginMethod"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).paymentMethod())
                .withRel("paymentMethod"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).landlordCustomerSize())
                .withRel("landlordCustomerSize"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).flatType())
                .withRel("flatType"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).houseType())
                .withRel("houseType"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).objectType())
                .withRel("objectType"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).parkingSpaceType())
                .withRel("parkingSpaceType"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).buildingConditionType())
                .withRel("buildingConditionType"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).heaterType())
                .withRel("heaterType"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).heaterFiringType())
                .withRel("heaterFiringType"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).energyCertificateType())
                .withRel("energyCertificateType"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).energyClassType())
                .withRel("energyClassType"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).solvencyType())
                .withRel("solvencyType"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).landlordLimitation())
                .withRel("landlordLimitation"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).propertySearcherLimitation())
                .withRel("propertySearcherLimitation"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).location())
                .withRel("location"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).portalTypes())
                .withRel("portalTypes"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).certificateCreationDate())
                .withRel("certificateCreationDate"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).fileType())
                .withRel("fileType"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).credentialProperty())
                .withRel("credentialProperty"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).productBasketProperty())
                .withRel("productBasketProperty"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).groundType())
                .withRel("groundType"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).title())
                .withRel("title"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).genderType())
                .withRel("genderType"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).mailTemplate())
                .withRel("mailTemplate"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).configurableMailTemplate())
                .withRel("configurableMailTemplate"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).householdType())
                .withRel("householdType"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).personalStatus())
                .withRel("personalStatus"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).employmentType())
                .withRel("employmentType"));

        links.add(webMvcLinkBuilderFactory
                 .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).userStatus())
                 .withRel("userStatus"));

        links.add(webMvcLinkBuilderFactory
                 .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).propertyProposalState())
                 .withRel("propertyProposalState"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).ftpTypes())
                .withRel("ftpTypes"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).mailGroups())
                .withRel("mailGroups"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).furnishingTypes())
                .withRel("furnishingTypes"));

        links.add(webMvcLinkBuilderFactory
                 .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).missingProfileFields())
                 .withRel("missingProfileFields"));

        links.add(webMvcLinkBuilderFactory
                 .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).schufaJobStates())
                 .withRel("schufaJobState"));

        links.add(webMvcLinkBuilderFactory
                 .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).schufaReportTypes())
                 .withRel("schufaReportType"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).publishStates())
                .withRel("publishState"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).propertySearcherHistoryTypes())
                .withRel("propertySearcherHistoryType"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).searchProfileType())
                .withRel("searchProfileType"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).conversationMessageSource())
                .withRel("conversationMessageSource"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).conversationMessageSender())
                .withRel("conversationMessageSender"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).reportCharts())
                .withRel("reportCharts"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).reportChartCategories())
                .withRel("reportChartCategories"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).reportChartDataTypes())
                .withRel("reportChartDataType"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).reportChartDefaultTypes())
                .withRel("reportChartDefaultType"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).customQuestionFilterType())
                .withRel("customQuestionFilterType"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).applicationArchiveUnit())
                .withRel("applicationArchiveUnit"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).digitalContractSimpleState())
                .withRel("digitalContractSimpleState"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).digitalContractSignerSimpleState())
                .withRel("digitalContractSignerSimpleState"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).digitalContractSchufaState())
                .withRel("digitalContractSchufaState"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).conversationMessageSubstitutionTag())
                .withRel("conversationMessageSubstitutionTag"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).propertyType())
                .withRel("propertyType"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).garageType())
                .withRel("garageType"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).officeType())
                .withRel("officeType"));

        links.add(webMvcLinkBuilderFactory
                .linkTo(WebMvcLinkBuilder.methodOn(ConstantsController.class).flooringType())
                .withRel("flooringType"));

        CollectionModel<List> resources = new CollectionModel(links);
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping(value = {"/userStatus"})
    public ResponseEntity userStatus() throws IllegalArgumentException {

        List<PropertySearcherUserType> list = Arrays.asList(PropertySearcherUserType.values());
        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/propertyProposalState"})
    public ResponseEntity propertyProposalState() throws IllegalArgumentException {

        List<PropertyProposalState> list = Arrays.asList(PropertyProposalState.values());
        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/propertyWriteProtection"})
    public ResponseEntity propertyWriteProtection()
            throws IllegalArgumentException {

        List<PropertyWriteProtection> list = Arrays.asList(PropertyWriteProtection.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/propertyState"})
    public ResponseEntity propertyState() throws IllegalArgumentException {

        List<PropertyPortalState> list = Arrays.asList(PropertyPortalState.values());
        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/landlordCustomerPreference"})
    public ResponseEntity landlordCustomerPreference()
            throws IllegalArgumentException {

        List<LandlordCustomerPreference> list = Arrays.asList(LandlordCustomerPreference.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/landlordUserPreference"})
    public ResponseEntity landlordUserPreference() throws IllegalArgumentException {

        List<LandlordUserPreference> list = Arrays.asList(LandlordUserPreference.values());



        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/landlordCustomerType"})
    public ResponseEntity landlordCustomerType() throws IllegalArgumentException {

        List<LandlordCustomerType> list = Arrays.asList(LandlordCustomerType.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/landlordUserType"})
    public ResponseEntity landlordUserType() throws IllegalArgumentException {

        List<LandlordUsertype> list = Arrays.asList(LandlordUsertype.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/loginMethod"})
    public ResponseEntity loginMethod() throws IllegalArgumentException {

        List<LoginMethod> list = Arrays.asList(LoginMethod.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/paymentMethod"})
    public ResponseEntity paymentMethod() throws IllegalArgumentException {

        List<PaymentMethodType> list = Arrays.asList(PaymentMethodType.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/landlordCustomerSize"})
    public ResponseEntity landlordCustomerSize() throws IllegalArgumentException {

        List<LandlordCustomerSize> list = Arrays.asList(LandlordCustomerSize.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/flatType"})
    public ResponseEntity flatType() throws IllegalArgumentException {

        List<FlatType> list = Arrays.asList(FlatType.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/buildingConditionType"})
    public ResponseEntity buildingConditionType() throws IllegalArgumentException {

        List<BuildingConditionType> list = Arrays.asList(BuildingConditionType.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/energyCertificateType"})
    public ResponseEntity energyCertificateType()
            throws IllegalArgumentException {

        List<EnergyCertificateType> list = Arrays.asList(EnergyCertificateType.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/energyClassType"})
    public ResponseEntity energyClassType() throws IllegalArgumentException {
        List<EnergyClassType> list = Arrays.asList(EnergyClassType.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/solvencyType"})
    public ResponseEntity solvencyType() throws IllegalArgumentException {
        List<SolvencyType> list = Arrays.asList(SolvencyType.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/landlordLimitation"})
    public ResponseEntity landlordLimitation() throws IllegalArgumentException {
        List<LandlordLimitationEnum> list = Arrays.asList(LandlordLimitationEnum.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/propertySearcherLimitation"})
    public ResponseEntity propertySearcherLimitation()
            throws IllegalArgumentException {

        List<PropertySearcherLimitationEnum> list = Arrays.asList(PropertySearcherLimitationEnum.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/location"})
    public ResponseEntity location() throws IllegalArgumentException {

        List<Location> list = Arrays.asList(Location.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/portalTypes"})
    public ResponseEntity portalTypes() throws IllegalArgumentException {
        List<Portal> list = Arrays.asList(Portal.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/certificateCreationDate"})
    public ResponseEntity certificateCreationDate()
            throws IllegalArgumentException {
        List<CertificateCreationDate> list = Arrays.asList(CertificateCreationDate.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/heaterType"})
    public ResponseEntity heaterType() throws IllegalArgumentException {
        List<HeaterType> list = Arrays.asList(HeaterType.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/heaterFiringType"})
    public ResponseEntity heaterFiringType() throws IllegalArgumentException {

        List<HeaterFiringType> list = Arrays.asList(HeaterFiringType.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/fileType"})
    public ResponseEntity fileType() throws IllegalArgumentException {

        List<FileType> list = Arrays.asList(FileType.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/houseType"})
    public ResponseEntity houseType() throws IllegalArgumentException {
        List<HouseType> list = Arrays.asList(HouseType.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/objectType"})
    public ResponseEntity objectType() throws IllegalArgumentException {

        List<ObjectType> list = Arrays.asList(ObjectType.values());


        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/parkingSpaceType"})
    public ResponseEntity parkingSpaceType() throws IllegalArgumentException {

        List<ParkingType> list = Arrays.asList(ParkingType.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/credentialProperty"})
    public ResponseEntity credentialProperty() throws IllegalArgumentException {

        List<CredentialProperty> list = Arrays.asList(CredentialProperty.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/productBasketProperty"})
    public ResponseEntity productBasketProperty() throws IllegalArgumentException {

        List<ProductBasketProperty> list = Arrays.asList(ProductBasketProperty.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/groundType"})
    public ResponseEntity groundType() throws IllegalArgumentException {
        List<GroundType> list = Arrays.asList(GroundType.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/title"})
    public ResponseEntity title() throws IllegalArgumentException {
        List<Title> list = Arrays.asList(Title.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/genderType"})
    public ResponseEntity genderType() throws IllegalArgumentException {

        List<GenderType> list = Arrays.asList(GenderType.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/mailTemplate"})
    public ResponseEntity mailTemplate() throws IllegalArgumentException {

        List<MailTemplate> list = Arrays.asList(MailTemplate.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/configurableMailTemplate"})
    public ResponseEntity configurableMailTemplate() throws IllegalArgumentException {
        List<MailTemplate> list = MailTemplate.getConfigurableTemplates();

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/householdType"})
    public ResponseEntity householdType() throws IllegalArgumentException {

        List<HouseholdType> list = Arrays.asList(HouseholdType.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/personalStatus"})
    public ResponseEntity personalStatus() throws IllegalArgumentException {

        List<PersonalStatus> list = Arrays.asList(PersonalStatus.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/employmentType"})
    public ResponseEntity employmentType() throws IllegalArgumentException {

        List<EmploymentType> list = Arrays.asList(EmploymentType.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/ftpTypes"})
    public ResponseEntity ftpTypes() throws IllegalArgumentException {

        List<FtpType> list = Arrays.asList(FtpType.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/mailGroups"})
    public ResponseEntity mailGroups() throws IllegalArgumentException {

        List<MailGroup> list = Arrays.asList(MailGroup.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/furnishingTypes"})
    public ResponseEntity furnishingTypes() throws IllegalArgumentException {
        List<FurnishingType> list = Arrays.asList(FurnishingType.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/missingProfileField"})
    public ResponseEntity missingProfileFields() throws IllegalArgumentException {
        List<MissingProfileField> list = Arrays.asList(MissingProfileField.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/schufaJobState"})
    public ResponseEntity schufaJobStates() throws IllegalArgumentException {
        List<JobState> list = Arrays.asList(JobState.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/schufaReportType"})
    public ResponseEntity schufaReportTypes() throws IllegalArgumentException {

        List<SchufaReportType> list = Arrays.asList(SchufaReportType.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/publishState"})
    public ResponseEntity publishStates() throws IllegalArgumentException {

        List<PublishState> list = Arrays.asList(PublishState.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/propertySearcherHistoryType"})
    public ResponseEntity propertySearcherHistoryTypes() throws IllegalArgumentException {
        List<PropertySearcherHistoryType> list = Arrays.asList(PropertySearcherHistoryType.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }


    @GetMapping(value = {"/searchProfileType"})
    public ResponseEntity searchProfileType() throws IllegalArgumentException {
        List<PropertySearcherSearchProfileType> list = Arrays.asList(PropertySearcherSearchProfileType.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/reportChart"})
    public ResponseEntity reportCharts() throws IllegalArgumentException {
        List<ReportChart> list = Arrays.asList(ReportChart.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/reportChartCategory"})
    public ResponseEntity reportChartCategories() throws IllegalArgumentException {

        List<ReportChartCategory> list = Arrays.asList(ReportChartCategory.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/reportChartDataType"})
    public ResponseEntity reportChartDataTypes() throws IllegalArgumentException {

        List<ReportChartDataType> list = Arrays.asList(ReportChartDataType.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/reportChartDefaultType"})
    public ResponseEntity reportChartDefaultTypes() throws IllegalArgumentException {

        List<ReportChartType> list = Arrays.asList(ReportChartType.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/customQuestionFilterType"})
    public ResponseEntity customQuestionFilterType()
            throws IllegalArgumentException {

        List<CustomQuestionFilterType> list = Arrays.asList(CustomQuestionFilterType.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/applicationArchiveUnit"})
    public ResponseEntity applicationArchiveUnit() throws IllegalArgumentException {

        List<LandlordCustomerApplicationArchiveUnit> list = Arrays.asList(LandlordCustomerApplicationArchiveUnit.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/conversationMessageSource"})
    public ResponseEntity conversationMessageSource()
            throws IllegalArgumentException {

        List<ConversationMessageSource> list = Arrays.asList(ConversationMessageSource.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/conversationMessageSender"})
    public ResponseEntity conversationMessageSender()
            throws IllegalArgumentException {

        List<ConversationMessageSender> list = Arrays.asList(ConversationMessageSender.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/digitalContractSimpleState"})
    public ResponseEntity digitalContractSimpleState()
            throws IllegalArgumentException {

        List<DigitalContractSimpleState> list = Arrays.asList(DigitalContractSimpleState.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }


    @GetMapping(value = {"/digitalContractSignerSimpleState"})
    public ResponseEntity digitalContractSignerSimpleState()
            throws IllegalArgumentException {

        List<DigitalContractSignerSimpleState> list = Arrays.asList(DigitalContractSignerSimpleState.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/digitalContractSchufaState"})
    public ResponseEntity digitalContractSchufaState()
            throws IllegalArgumentException {

        List<DigitalContractSchufaState> list = Arrays.asList(DigitalContractSchufaState.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/conversationMessageSubstitutionTag"})
    public ResponseEntity conversationMessageSubstitutionTag()
            throws IllegalArgumentException {

        List<ConversationMessageTemplateSubstitutionTag> list = Arrays.asList(ConversationMessageTemplateSubstitutionTag.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/propertyType"})
    public ResponseEntity propertyType()
            throws IllegalArgumentException {

        List<PropertyType> list = Arrays.asList(PropertyType.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/garageType"})
    public ResponseEntity garageType()
            throws IllegalArgumentException {

        List<GarageType> list = Arrays.asList(GarageType.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/officeType"})
    public ResponseEntity officeType()
            throws IllegalArgumentException {

        List<OfficeType> list = Arrays.asList(OfficeType.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }

    @GetMapping(value = {"/flooringType"})
    public ResponseEntity flooringType()
            throws IllegalArgumentException {

        List<Immoscout24FlooringType> list = Arrays.asList(Immoscout24FlooringType.values());

        return new ResponseEntity<>(new CollectionModel<>(list), HttpStatus.OK);
    }
}
