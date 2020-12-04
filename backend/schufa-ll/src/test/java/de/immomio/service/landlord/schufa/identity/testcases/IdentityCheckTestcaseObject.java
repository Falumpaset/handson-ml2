package de.immomio.service.landlord.schufa.identity.testcases;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.immomio.data.base.bean.schufa.cbi.CbiRequest;
import de.immomio.data.base.bean.schufa.cbi.SchufaAddress;
import de.immomio.data.base.bean.schufa.cbi.creditRating.enums.SchufaGenderType;
import de.immomio.data.base.bean.schufa.cbi.identityCheck.enums.IdentityCheckType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author Johannes Hiemer.
 */
@Getter
@Setter
public class IdentityCheckTestcaseObject {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @JsonProperty("CBI")
    protected CbiRequest cbiRequest;

    @JsonProperty("IDENTITAETSCHECK_VARIANTE")
    private IdentityCheckType identityCheckType;

    @JsonProperty("VORNAME")
    private String name;

    @JsonProperty("NACHNAME")
    private String surname;

    @JsonProperty("GESCHLECHT")
    private SchufaGenderType gender;

    @JsonProperty("GEBURTSDATUM")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private Date dateOfBirth;

    @JsonProperty("GEBURTSORT")
    private String placeOfBirth;

    @JsonProperty("AKTUELLE_ADRESSE")
    private SchufaAddress address;

    @JsonProperty("VORADRESSE")
    private SchufaAddress preAddress;

    @JsonProperty("KOMMENTAR")
    private String comment;

    @JsonProperty("MERKMALCODE")
    private String attributeCode;

    @JsonProperty("TITEL")
    private String title;

}
