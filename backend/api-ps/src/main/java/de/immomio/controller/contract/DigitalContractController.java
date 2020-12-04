package de.immomio.controller.contract;

import de.immomio.beans.TokenBean;
import de.immomio.beans.shared.contract.DigitalContractSignBean;
import de.immomio.beans.shared.contract.tenant.DigitalContractAesStateBean;
import de.immomio.beans.shared.contract.tenant.DigitalContractTenantWorkflowBean;
import de.immomio.beans.shared.contract.tenant.token.DigitalContractConfirmSchufaTokenBean;
import de.immomio.beans.shared.contract.tenant.token.DigitalContractFlatVisitedTokenBean;
import de.immomio.beans.shared.contract.tenant.token.DigitalContractSigningTokenBean;
import de.immomio.beans.shared.contract.tenant.token.DigitalContractStartAesTokenBean;
import de.immomio.beans.shared.contract.tenant.token.DigitalContractVerifyAesCodeTokenBean;
import de.immomio.constants.exceptions.DigitalContractException;
import de.immomio.constants.exceptions.DocuSignApiException;
import de.immomio.data.base.bean.schufa.cbi.SchufaConnectorException;
import de.immomio.data.shared.entity.contract.signer.SignerCurrentStateBean;
import de.immomio.service.contract.DigitalContractSignerService;
import de.immomio.service.contract.aes.DigitalContractAesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@RepositoryRestController
@RequestMapping(value = "/dmv")
public class DigitalContractController {

    private static final String TOKEN_MAY_NOT_BE_NULL_L = "TOKEN_MAY_NOT_BE_NULL_L";
    private static final String REDIRECT_URL_MAY_NOT_BE_NULL_L = "REDIRECT_URL_MAY_NOT_BE_NULL_L";
    private static final String AES_CODE_MAY_NOT_BE_NULL_L = "AES_CODE_MAY_NOT_BE_NULL_L";
    private static final String FLAT_VISITED_MAY_NOT_BE_NULL_L = "FLAT_VISITED_MAY_NOT_BE_NULL_L";
    private static final String ERROR_AT_REQUESTING_DOCUSIGN_L = "ERROR_AT_REQUESTING_DOCUSIGN_L";
    private static final String IBAN_MAY_NOT_BE_NULL_L = "IBAN_MAY_NOT_BE_NULL_L";
    public static final String DATE_OF_BIRTH_MAY_NOT_BE_NULL_L = "DATE_OF_BIRTH_MAY_NOT_BE_NULL_L";

    private final DigitalContractSignerService digitalContractSignerService;

    private final DigitalContractAesService digitalContractAesService;

    @Autowired
    public DigitalContractController(
            DigitalContractSignerService digitalContractSignerService,
            DigitalContractAesService digitalContractAesService
    ) {
        this.digitalContractSignerService = digitalContractSignerService;
        this.digitalContractAesService = digitalContractAesService;
    }

    @PostMapping("/getContractData")
    public ResponseEntity<DigitalContractSignBean> getContractData(@RequestBody TokenBean tokenBean) {
        validateTokenBean(tokenBean);

        return ResponseEntity.ok(digitalContractSignerService.getContractSignBean(tokenBean.getToken()));
    }

    @PostMapping("/updateFlatVisited")
    public ResponseEntity<?> updateFlatVisited(@RequestBody DigitalContractFlatVisitedTokenBean tokenBean) {
        validateDigitalContractSigningTokenBean(tokenBean);

        try {
            return ResponseEntity.ok(digitalContractSignerService.flatVisited(tokenBean));
        } catch (DigitalContractException e) {
            return ResponseEntity.ok(e.getMessage());
        } catch (DocuSignApiException e) {
            return new ResponseEntity<>(ERROR_AT_REQUESTING_DOCUSIGN_L, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/notifyLandlordDataNotCorrect")
    public ResponseEntity<String> notifyLandlord(@RequestBody TokenBean tokenBean) {
        validateTokenBean(tokenBean);
        digitalContractSignerService.notifyLandlordDataNotCorrect(tokenBean);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/startAesCheck")
    public ResponseEntity<DigitalContractAesStateBean> startAesCheck(
            @RequestBody DigitalContractStartAesTokenBean tokenBean
    ) throws SchufaConnectorException {
        validateDigitalContractIbanTokenBean(tokenBean);

        return ResponseEntity.ok(digitalContractAesService.processAesCheck(tokenBean));
    }

    @PostMapping("/confirmAesData")
    public ResponseEntity<DigitalContractAesStateBean> confirmAesData(
            @RequestBody @Valid DigitalContractConfirmSchufaTokenBean tokenBean
    ) {
        validateDigitalContractConfirmSchufaTokenBean(tokenBean);

        return ResponseEntity.ok(digitalContractAesService.confirmAesData(tokenBean));
    }

    @PostMapping("/verifyAesCode")
    public ResponseEntity<DigitalContractTenantWorkflowBean> verifyAesCode(
            @RequestBody @Valid DigitalContractVerifyAesCodeTokenBean tokenBean
    ) {
        validateDigitalContractVerifyAesCodeTokenBean(tokenBean);

        return ResponseEntity.ok(digitalContractAesService.verifyAesCode(tokenBean));
    }

    @PostMapping("/currentStatus")
    public ResponseEntity<SignerCurrentStateBean> getCurrentStatus(
            @RequestBody @Valid TokenBean tokenBean
    ) {
        validateTokenBean(tokenBean);

        return ResponseEntity.ok(digitalContractAesService.getCurrentState(tokenBean));
    }

    @PostMapping("/signingUrl")
    public ResponseEntity<DigitalContractTenantWorkflowBean> getSigningUrl(
            @RequestBody @Valid DigitalContractSigningTokenBean tokenBean
    ) {
        validateDigitalContractSigningTokenBean(tokenBean);

        return ResponseEntity.ok(digitalContractAesService.getSigningUrl(tokenBean));
    }


    private void validateTokenBean(TokenBean tokenBean) {
        Assert.notNull(tokenBean.getToken(), TOKEN_MAY_NOT_BE_NULL_L);
    }

    private void validateDigitalContractSigningTokenBean(DigitalContractSigningTokenBean tokenBean) {
        Assert.notNull(tokenBean.getToken(), TOKEN_MAY_NOT_BE_NULL_L);
        Assert.notNull(tokenBean.getRedirectUrl(), REDIRECT_URL_MAY_NOT_BE_NULL_L);
    }

    private void validateDigitalContractIbanTokenBean(DigitalContractStartAesTokenBean tokenBean) {
        Assert.notNull(tokenBean.getToken(), TOKEN_MAY_NOT_BE_NULL_L);
        Assert.notNull(tokenBean.getIban(), IBAN_MAY_NOT_BE_NULL_L);
        Assert.notNull(tokenBean.getDateOfBirth(), DATE_OF_BIRTH_MAY_NOT_BE_NULL_L);
    }

    private void validateDigitalContractVerifyAesCodeTokenBean(DigitalContractVerifyAesCodeTokenBean tokenBean) {
        Assert.notNull(tokenBean.getToken(), TOKEN_MAY_NOT_BE_NULL_L);
        Assert.notNull(tokenBean.getRedirectUrl(), REDIRECT_URL_MAY_NOT_BE_NULL_L);
        Assert.notNull(tokenBean.getAesCode(), AES_CODE_MAY_NOT_BE_NULL_L);
    }

    private void validateDigitalContractConfirmSchufaTokenBean(DigitalContractConfirmSchufaTokenBean tokenBean) {
        Assert.notNull(tokenBean.getToken(), TOKEN_MAY_NOT_BE_NULL_L);
    }

}
