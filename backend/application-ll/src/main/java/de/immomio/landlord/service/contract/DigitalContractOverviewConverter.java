package de.immomio.landlord.service.contract;

import de.immomio.beans.shared.contract.DigitalContractSignerSimpleState;
import de.immomio.beans.shared.contract.DigitalContractSimpleState;
import de.immomio.beans.shared.contract.overview.DigitalContractOverviewItemBean;
import de.immomio.beans.shared.contract.overview.DigitalContractOverviewSignerDataBean;
import de.immomio.data.base.type.contract.signer.DigitalContractSignerType;
import de.immomio.data.shared.bean.contract.DigitalContractSignerData;
import de.immomio.data.shared.entity.contract.DigitalContract;
import de.immomio.data.shared.entity.contract.history.DigitalContractHistoryState;
import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import de.immomio.data.shared.entity.contract.signer.history.DigitalContractSignerHistoryState;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DigitalContractOverviewConverter {

    public DigitalContractOverviewItemBean convertContractEntityToBean(DigitalContract digitalContract) {
        DigitalContractOverviewItemBean.DigitalContractOverviewItemBeanBuilder itemBuilder =
                DigitalContractOverviewItemBean.builder()
                        .internalContractId(digitalContract.getInternalContractId())
                        .contractType(digitalContract.getDigitalContractType())
                        .created(digitalContract.getCreated())
                        .property(digitalContract.getPropertyData())
                        .actionRequired(digitalContract.getCurrentState() == DigitalContractHistoryState.INTERNAL_INTERRUPTED)
                        .newEnvelopeNeeded(digitalContract.getCountSignersCompleted() > 0L)
                        .status(getSimpleState(digitalContract))
                        .firstSignerType(digitalContract.getFirstSignerType())
                        .signedDocumentCombinedFile(digitalContract.getSignedDocumentCombinedFile())
                        .signedDocumentSingleFiles(digitalContract.getSignedDocumentSingleFiles())
                        .signedDocumentArchiveFile(digitalContract.getSignedDocumentArchiveCertificateFile())
                        .signatureType(digitalContract.getSignatureType())
                        .onsiteHostAgentId(digitalContract.getOnsiteHostAgent() != null ? digitalContract.getOnsiteHostAgent().getId() : null)
                        .contractAlreadyUpdated(digitalContract.isAlreadyUpdated());
        itemBuilder.landlordSigners(
                convertSignerEntitiesToBeans(
                        digitalContract.getSigners(),
                        DigitalContractSignerType.LANDLORD
                )
        );
        itemBuilder.tenantSigners(
                convertSignerEntitiesToBeans(
                        digitalContract.getSigners(),
                        DigitalContractSignerType.TENANT
                )
        );

        return itemBuilder.build();
    }

    public List<DigitalContractOverviewSignerDataBean> convertSignerEntitiesToBeans(
            List<DigitalContractSigner> signers,
            DigitalContractSignerType signerType
    ) {
        return signers
                .stream()
                .filter(signer -> signer.getType() == signerType)
                .map(this::convertToSignerData)
                .collect(Collectors.toList());
    }

    private DigitalContractOverviewSignerDataBean convertToSignerData(DigitalContractSigner signer) {
        DigitalContractSignerData data = signer.getData();
        DigitalContractSignerHistoryState currentState = signer.getCurrentState().getSignerState();

        DigitalContractSignerSimpleState currentSimpleState = currentState != null ?
                DigitalContractSignerSimpleState.getByContractState(currentState) :
                DigitalContractSignerSimpleState.WAITING;

        return DigitalContractOverviewSignerDataBean
                .builder()
                .firstname(data.getFirstname())
                .lastname(data.getLastname())
                .status(currentSimpleState)
                .email(data.getEmail())
                .internalSignerId(signer.getInternalSignerId())
                .currentState(signer.getCurrentState())
                .onsiteHost(signer.isOnsiteHost())
                .build();
    }

    private DigitalContractSimpleState getSimpleState(DigitalContract digitalContract) {
        DigitalContractHistoryState currentContractState = digitalContract.getCurrentState();
        if (currentContractState == DigitalContractHistoryState.DOCUSIGN_SENT) {
            Optional<DigitalContractSignerHistoryState> signerCompleted = digitalContract.getSigners()
                    .stream()
                    .map(signer -> signer.getCurrentState().getSignerState())
                    .filter(state -> state == DigitalContractSignerHistoryState.DOCUSIGN_COMPLETED)
                    .findAny();
            if (signerCompleted.isPresent()) {
                return DigitalContractSimpleState.PROTECTED;
            } else {
                return DigitalContractSimpleState.getByContractState(currentContractState);
            }
        } else {
            return DigitalContractSimpleState.getByContractState(currentContractState);
        }
    }

}
