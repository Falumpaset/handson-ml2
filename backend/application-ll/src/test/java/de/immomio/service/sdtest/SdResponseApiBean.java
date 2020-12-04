package de.immomio.service.sdtest;

import de.immomio.data.shared.entity.selfdisclosure.json.answertype.ChildAnswer;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
public class SdResponseApiBean {
    private String id;
    private SdResponseTenant tenant;
    private SdResponseProperty property;
    private List<SdResponseTenant> fellowTenants = new ArrayList<>();
    private List<ChildAnswer> furtherResidents = new ArrayList<>();
    private List<SdResponseCheckedAnswer> checkedAnswers = new ArrayList<>();
    private List<SdResponseDocumentAnswer> documents= new ArrayList<>();
    private List<String> confirmations = new ArrayList<>();
}
