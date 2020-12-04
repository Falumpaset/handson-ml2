package de.immomio.service.contract;

import com.docusign.esign.model.NewUser;
import de.immomio.docusign.service.DocuSignApiConfig;
import de.immomio.docusign.service.DocuSignUserService;
import de.immomio.mail.sender.LandlordMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.model.repository.service.landlord.contract.DigitalContractApiUserRepository;
import de.immomio.service.shared.contract.AbstractDigitalContractUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AdminDigitalContractUserService
        extends AbstractDigitalContractUserService<DigitalContractApiUserRepository> {

        private LandlordMailSender landlordMailSender;
    private DocuSignApiConfig docuSignApiConfig;
    private List<String> NEW_CONTRACT_BOOKED_EMAILS = Arrays.asList(
            "aschoenfeldt@immomio.de",
            "ahansen@immomio.de",
            "nlindemann@immomio.de",
            "jhetzel@immomio.de",
            "cmenz@immomio.de",
            "rhaschemi@immomio.de",
            "njacobi@immomio.de");

    private static final String ERROR_CONSENT_URL_FORMAT = "%s/oauth/auth?response_type=code&scope=%s" +
            "&client_id=%s" +
            "&redirect_uri=%s";


    @Autowired
    public AdminDigitalContractUserService(
            DigitalContractApiUserRepository digitalContractApiUserRepository,
            DocuSignUserService docuSignUserService,
            LandlordMailSender landlordMailSender, DocuSignApiConfig docuSignApiConfig) {
        super(digitalContractApiUserRepository, docuSignUserService);
        this.landlordMailSender = landlordMailSender;
        this.docuSignApiConfig = docuSignApiConfig;
    }

    @Override
    public NewUser createApiUser(String customerName, Long customerId) {
        NewUser apiUser = super.createApiUser(customerName, customerId);

        Map<String, Object> model = new HashMap<>();
        model.put("accountmail", apiUser.getEmail());
        model.put("customername", customerName);
        model.put("customerid", customerId);
        String consentUrl = String.format(ERROR_CONSENT_URL_FORMAT,
                docuSignApiConfig.getAuthServer(),
                docuSignApiConfig.getPermissionScopes(),
                docuSignApiConfig.getClientId(),
                docuSignApiConfig.getOauthRedirectUri());
        model.put("consenturl", consentUrl);

        NEW_CONTRACT_BOOKED_EMAILS.forEach(mailAddress -> {
            landlordMailSender.send(mailAddress, MailTemplate.DIGITAL_CONTRACT_INTERNAL_BOOKED, "dmv.booked.subject", model);
        });

        return apiUser;
    }
}
