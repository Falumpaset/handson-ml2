package de.immomio.service.sdtest;

import de.immomio.data.shared.entity.selfdisclosure.json.answertype.AddressAnswer;
import de.immomio.data.shared.entity.selfdisclosure.json.answertype.EmploymentAnswer;
import de.immomio.data.shared.entity.selfdisclosure.json.answertype.PersonAnswer;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
public class SdResponseTenant {
    private PersonAnswer personDetails;
    private AddressAnswer addressDetails;
    private EmploymentAnswer employmentDetails;
    private List<SdResponseCheckedAnswer> checkedAnswers = new ArrayList<>();

}
