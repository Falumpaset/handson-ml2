package de.immomio.model.contract;

import de.immomio.model.ApiPropertyInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
@Schema(name = "ContractRoot", description = "the root of a rental contract")
public class CreateContractApiBean implements Serializable {
    private static final long serialVersionUID = 3391540070654988819L;
    @NotNull
    private String id;
    @NotNull
    private ApiPropertyInfo property;
    @NotNull
    private List<SignerInfo> landlordSigners;
    @NotNull
    private List<SignerInfo> tenantSigners;
    @Schema(description = "the party who should sign the contract first, if not set, the signer specified in the settings will be used")
    private SignerType firstSigner;
    @NotNull
    @Schema(description = "the signature type (legally secure or simple)")
    private ApiSignatureType signatureType;
    @Schema(description = "the email address of the person who performs the onsite signing session. Only mandatory if the signatureType is 'ONSITE'")
    private String onsiteHostEmailAddress;
}
