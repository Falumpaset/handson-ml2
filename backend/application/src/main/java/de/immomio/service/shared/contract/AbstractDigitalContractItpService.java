package de.immomio.service.shared.contract;

import de.immomio.data.propertysearcher.entity.itp.ItpCheckRequestBean;
import de.immomio.data.propertysearcher.entity.itp.ItpCheckResponseBean;
import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import de.immomio.data.shared.entity.contract.signer.history.aes.itp.DigitalContractItpHistory;
import de.immomio.data.shared.entity.contract.signer.history.aes.itp.DigitalContractItpState;
import de.immomio.data.shared.entity.contract.signer.history.aes.itp.ItpMaskedRequestBean;
import de.immomio.itp.client.ItpClient;
import de.immomio.model.repository.core.shared.contract.signer.BaseDigitalContractSignerRepository;
import de.immomio.model.repository.core.shared.contract.signer.aes.BaseDigitalContractItpHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static de.immomio.data.shared.entity.contract.signer.history.aes.itp.DigitalContractItpState.TECHNICAL_ERROR;
import static de.immomio.data.shared.entity.contract.signer.history.aes.itp.DigitalContractItpState.findByInternalState;

/**
 * @author Niklas Lindemann
 */

@Slf4j
public abstract class AbstractDigitalContractItpService<IHR extends BaseDigitalContractItpHistoryRepository, SR extends BaseDigitalContractSignerRepository> {

    private final IHR itpHistoryRepository;
    private final SR signerRepository;
    private final ItpClient itpClient;

    public AbstractDigitalContractItpService(IHR itpHistoryRepository, SR signerRepository, ItpClient itpClient) {
        this.itpHistoryRepository = itpHistoryRepository;
        this.signerRepository = signerRepository;
        this.itpClient = itpClient;
    }

    public DigitalContractItpState processItpCheck(DigitalContractSigner signer, String iban, boolean isMockIban) {

        try {
            ItpCheckRequestBean itpCheckRequestBean = getItpCheckRequestBean(signer, iban, signer.getAesVerificationData().getAesCode(), isMockIban);
            Mono<ItpCheckResponseBean> checkResponseBeanMono =
                    (isMockIban) ? itpClient.mockedIdentCheck(itpCheckRequestBean) : itpClient.identCheck(itpCheckRequestBean);
            ItpCheckResponseBean checkResponseBean = checkResponseBeanMono.block();
            log.info("checkResponseBean: {}", checkResponseBean);
            DigitalContractItpState itpState = findByInternalState(checkResponseBean.getProcessStatus());
            saveItpHistory(
                    signer,
                    checkResponseBean,
                    itpState,
                    null,
                    itpCheckRequestBean.getIdentificationCode(),
                    signer.getAesVerificationData().getMaskedIban()
            );
            saveSignerItpData(signer, checkResponseBean.getIdentcheckUuid());

            return identCheckStatus(signer, isMockIban);
        } catch (WebClientResponseException ex) {
            String responseBodyAsString = ex.getResponseBodyAsString();
            log.error("status: " + ex.getStatusCode());
            log.error("message: " + responseBodyAsString);
            saveItpErrorState(signer, responseBodyAsString);

            return TECHNICAL_ERROR;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            saveItpErrorState(signer, e.getMessage());
            return TECHNICAL_ERROR;
        }

    }


    public void saveItpHistory(
            DigitalContractSigner signer,
            ItpCheckResponseBean responseBean,
            DigitalContractItpState itpState,
            String identcheckUuid,
            String identificationCode,
            String maskedIban
    ) {
        ItpMaskedRequestBean maskedRequestBean = ItpMaskedRequestBean.builder()
                .identcheckUuid(identcheckUuid)
                .identificationCode(identificationCode)
                .maskedIban(maskedIban)
                .build();
        DigitalContractItpHistory itpHistory = new DigitalContractItpHistory();
        itpHistory.setItpRequest(maskedRequestBean);
        itpHistory.setItpResponse(responseBean);
        itpHistory.setSigner(signer);
        itpHistory.setState(itpState);
        itpHistoryRepository.save(itpHistory);
        signer.getCurrentState().setItpState(itpState);
        signerRepository.save(signer);
    }

    private void saveItpErrorState(DigitalContractSigner signer, String error) {
        String identcheckUuid = signer.getAesVerificationData().getIdentcheckUuid();
        String identificationCode = signer.getAesVerificationData().getAesCode();
        String maskedIban = signer.getAesVerificationData().getMaskedIban();

        ItpCheckResponseBean itpCheckResponseBean = new ItpCheckResponseBean();
        itpCheckResponseBean.setError(error);
        saveItpHistory(signer, itpCheckResponseBean, TECHNICAL_ERROR, identcheckUuid, identificationCode, maskedIban);
    }

    private ItpCheckRequestBean getItpCheckRequestBean(DigitalContractSigner signer, String iban, String identCode, boolean isMockIban) {
        return ItpCheckRequestBean.builder()
                .payeeLastname(getLastnameOrMock(isMockIban, signer.getData().getLastname()))
                .payeeFirstname(signer.getData().getFirstname())
                .payeeIban(iban)
                .identificationCode(identCode)
                .paymentAsSctinst(true)
                .build();
    }

    private DigitalContractItpState identCheckStatus(DigitalContractSigner signer, boolean isMockIban) {
        String identcheckUuid = signer.getAesVerificationData().getIdentcheckUuid();
        Mono<ItpCheckResponseBean> statusResponseBeanMono =
                (isMockIban) ? itpClient.mockedIdentCheckStatus(identcheckUuid) : itpClient.identCheckStatus(identcheckUuid);
        ItpCheckResponseBean responseBean = statusResponseBeanMono.block();
        DigitalContractItpState itpState = findByInternalState(responseBean.getProcessStatus());
        saveItpHistory(signer, responseBean, itpState, identcheckUuid, null, null);
        saveSignerItpData(signer, identcheckUuid);

        return itpState;
    }

    private String getLastnameOrMock(boolean isMockIban, String lastname) {
        return isMockIban ? "101" : lastname;
    }

    public void saveSignerItpData(DigitalContractSigner signer, String identcheckUuid) {
        signer.getAesVerificationData().setIdentcheckUuid(identcheckUuid);
        signer.getAesVerificationData().setTemporaryIban(null);
        signerRepository.save(signer);
    }


}
