package de.immomio.beans.shared.contract;

import de.immomio.data.base.type.contract.DigitalContractSignatureType;
import de.immomio.data.base.type.contract.DigitalContractType;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.data.shared.bean.contract.DigitalContractPropertyData;
import de.immomio.data.shared.bean.contract.DigitalContractSignerData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * @author Niklas Lindemann
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DigitalContractSignBean implements Serializable {

    private static final long serialVersionUID = 1406304582000670346L;

    private UUID internalContractId;
    private DigitalContractType contractType;
    private DigitalContractSignatureType signatureType;
    private List<DigitalContractSignerData> signer;
    private DigitalContractPropertyData propertyData;
    private Boolean continueContractWhenNotVisitedFlat;
    private DigitalContractSignContactBean contactInfo;
    private Boolean actionRequired;
    private S3File propertyTitleImage;
    private S3File signedDocumentCombinedFile;
    private List<S3File> signedDocumentSingleFiles;
    private S3File signedDocumentArchiveFile;

}
