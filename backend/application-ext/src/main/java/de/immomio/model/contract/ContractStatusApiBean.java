package de.immomio.model.contract;

import de.immomio.model.ApiPropertyInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Niklas Lindemann
 */
@Data
public class ContractStatusApiBean implements Serializable {
    private static final long serialVersionUID = -7039383665177313927L;
    private List<SignerStatusInfo> landlordSigners = new ArrayList<>();
    private List<SignerStatusInfo> tenantSigners = new ArrayList<>();
    private String id;
    private ApiPropertyInfo property;
    private SignerType firstSigner;
    private ApiSignatureType signatureType;
    private ApiContractState status;
}
