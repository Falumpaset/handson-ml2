package de.immomio.docusign.service;

import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopesInformation;
import de.immomio.constants.exceptions.DocuSignApiException;
import de.immomio.docusign.service.beans.DigitalContractEnvelopeStatusBean;
import de.immomio.docusign.service.beans.DigitalContractSignerStatusBean;
import de.immomio.docusign.service.beans.DocuSignEnvelopeStatusType;
import de.immomio.docusign.service.beans.DocuSignRecipientStatusType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Andreas Hansen.
 */
@Slf4j
@Service
public class DocuSignStatusService {

    private static final String CHUNK_DELIMITER = ",";

    private final DocuSignApiService docuSignApiService;

    @Autowired
    public DocuSignStatusService(DocuSignApiService docuSignApiService) {
        this.docuSignApiService = docuSignApiService;
    }

    public List<DigitalContractEnvelopeStatusBean> getEnvelopeStatusChanges(List<UUID> envelopeIds) {
        List<DigitalContractEnvelopeStatusBean> envelopeStatusBeans = new ArrayList<>();
        int envsChunkSize = 40;
        AtomicInteger counter = new AtomicInteger();
        Collection<List<UUID>> envIdsChunks = envelopeIds.stream()
                .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / envsChunkSize))
                .values();

        envIdsChunks.forEach(envIdsChunk -> {
            try {
                String envelopeIdsAsCommaSeparatedString = envIdsChunk
                        .stream()
                        .map(UUID::toString)
                        .collect(Collectors.joining(CHUNK_DELIMITER));
                EnvelopesInformation envelopesInformation = docuSignApiService.apiListStatusChanges(envelopeIdsAsCommaSeparatedString);

                List<DigitalContractEnvelopeStatusBean> envelopeChunkStatusBeans = envelopesInformation.getEnvelopes()
                        .stream()
                        .map(envelope -> DigitalContractEnvelopeStatusBean
                                .builder()
                                .envelopeId(UUID.fromString(envelope.getEnvelopeId()))
                                .status(DocuSignEnvelopeStatusType.findByStatusValue(envelope.getStatus()))
                                .signers(
                                        envelope.getRecipients().getSigners()
                                                .stream()
                                                .map(signer -> DigitalContractSignerStatusBean
                                                        .builder()
                                                        .recipientId(Long.parseLong(signer.getRecipientId()))
                                                        .status(
                                                                DocuSignRecipientStatusType.findByStatusValue(
                                                                        signer.getStatus()
                                                                )
                                                        )
                                                        .build()
                                                )
                                                .collect(Collectors.toList())
                                )
                                .build()
                        )
                        .collect(Collectors.toList());
                envelopeStatusBeans.addAll(envelopeChunkStatusBeans);
            } catch (ApiException e) {
                log.error(e.getMessage(), e);

                throw new DocuSignApiException(e.getMessage(), e.getResponseBody());
            }
        });

        return envelopeStatusBeans;
    }

}
