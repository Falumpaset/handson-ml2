package de.immomio.service.landlord.schufa.account.testcases;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.immomio.data.base.bean.schufa.cbi.CbiRequest;
import de.immomio.data.base.bean.schufa.cbi.SchufaAddress;
import de.immomio.data.base.bean.schufa.cbi.accountNumberCheck.information.SchufaBankAccount;
import de.immomio.data.base.bean.schufa.cbi.creditRating.enums.SchufaGenderType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author Johannes Hiemer.
 */
@Getter
@Setter
public class AccountNumberTestcaseObject {

    @JsonProperty("CBI")
    protected CbiRequest cbiRequest;

    @JsonProperty("VORNAME")
    private String name;

    @JsonProperty("NACHNAME")
    private String surname;

    @JsonProperty("GESCHLECHT")
    private SchufaGenderType gender;

    @JsonProperty("GEBURTSDATUM")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    @JsonProperty("AKTUELLE_ADRESSE")
    private SchufaAddress address;

    @JsonProperty("BANKVERBINDUNG")
    private SchufaBankAccount bankAccount;

    @JsonProperty("TITEL")
    private String title;

}
