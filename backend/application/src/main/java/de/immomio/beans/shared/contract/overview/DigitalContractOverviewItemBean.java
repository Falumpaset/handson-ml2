package de.immomio.beans.shared.contract.overview;

import de.immomio.beans.shared.contract.DigitalContractSimpleState;
import de.immomio.data.base.type.contract.DigitalContractSignatureType;
import de.immomio.data.base.type.contract.DigitalContractType;
import de.immomio.data.base.type.contract.signer.DigitalContractSignerType;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.data.shared.bean.contract.DigitalContractPropertyData;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Andreas Hansen
 */

@Data
@Builder
public class DigitalContractOverviewItemBean implements Serializable {

    private static final long serialVersionUID = 1526013817736558619L;

    private UUID internalContractId;

    private DigitalContractType contractType;

    private Date created;

    private DigitalContractPropertyData property;

    private List<DigitalContractOverviewSignerDataBean> landlordSigners;

    private List<DigitalContractOverviewSignerDataBean> tenantSigners;

    private DigitalContractSimpleState status;

    private Boolean actionRequired;

    private Boolean newEnvelopeNeeded;

    private DigitalContractSignerType firstSignerType;

    private S3File signedDocumentCombinedFile;

    private List<S3File> signedDocumentSingleFiles;

    private S3File signedDocumentArchiveFile;

    private DigitalContractSignatureType signatureType;

    private Long onsiteHostAgentId;

    private boolean contractAlreadyUpdated;

}
