package de.immomio.beans.shared.contract;

import de.immomio.data.base.type.contract.DigitalContractSignatureType;
import de.immomio.data.base.type.contract.signer.DigitalContractSignerType;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.data.shared.bean.contract.DigitalContractPropertyData;
import de.immomio.data.shared.bean.contract.DigitalContractSignerData;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * @author Andreas Hansen
 */

@Data
@Builder
public class DigitalContractDataBean implements Serializable {

    private static final long serialVersionUID = -3086550877259083851L;

    private DigitalContractSignatureType signatureType;

    private DigitalContractSignerType firstSigner;

    private String embeddedSendingRedirectUrl;

    private UUID internalContractId;

    private List<DigitalContractSignerData> landlordSigners;

    private List<DigitalContractSignerData> tenantSigners;

    private DigitalContractPropertyData property;

    private List<S3File> documentFiles;

    private Long applicationId;

    private Boolean actionRequired;

    private Boolean newEnvelopeNeeded;

    private S3File signedDocumentCombinedFile;

    private List<S3File> signedDocumentSingleFiles;

    private S3File signedDocumentArchiveFile;

    private Long onsiteHostAgentId;
}
