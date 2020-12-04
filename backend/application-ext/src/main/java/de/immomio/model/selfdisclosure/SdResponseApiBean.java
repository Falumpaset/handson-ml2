package de.immomio.model.selfdisclosure;

import de.immomio.model.ApiPropertyInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
@Schema(name = "RootInformation")
public class SdResponseApiBean {
    @Schema(description = "reference id of the tenant")
    private String id;
    @Schema(description = "information of the main tenant")
    private ApiSdResponseTenant tenant;
    @Schema(description = "information of the property")
    private ApiPropertyInfo property;
    @Schema(description = "optional list containing the informations of fellow tenants")
    private List<ApiSdResponseTenant> fellowTenants = new ArrayList<>();
    @Schema(description = "optional list containing the informations of fellow further residents. E.g. children")
    private List<ApiChildAnswer> furtherResidents = new ArrayList<>();
    @Schema(description = "list of confirmed answers")
    private List<ApiCheckedAnswer> checkedAnswers = new ArrayList<>();
    @Schema(description = "list of provided documents")
    private List<ApiDocumentAnswer> documents= new ArrayList<>();
    @Schema(description = "list of phrases which the tenant has to confirm", example = "Ich habe die Datenschutzhinweise zur Kenntnis genommen")
    private List<String> confirmations = new ArrayList<>();
}
