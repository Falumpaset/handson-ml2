package de.immomio.exporter.openimmo.feedback;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class OpenimmoInterest {

    @XmlElement(name = "int_id")
    private String intId;

    @XmlElement(name = "anrede")
    private String salutation;

    @XmlElement(name = "vorname")
    private String firsName;

    @XmlElement(name = "nachname")
    private String lastName;

    @XmlElement(name = "firma")
    private String firm;

    @XmlElement(name = "strasse")
    private String street;

    @XmlElement(name = "postfach")
    private String poBox;

    @XmlElement(name = "plz")
    private String postCode;

    @XmlElement(name = "ort")
    private String region;

    @XmlElement(name = "tel")
    private String telephone;

    @XmlElement(name = "fax")
    private String fax;

    @XmlElement(name = "mobil")
    private String mobile;

    @XmlElement(name = "email")
    private String email;

    @XmlElement(name = "anfrage")
    private String inquiry;

    @XmlElement(name = "user_defined_extend")
    private List<OpenimmoUserDefinedField> userDefinedFields;
}
