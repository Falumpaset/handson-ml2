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
public class OpenimmoObject {

    @XmlElement(name = "portal_unique_id")
    private String portalUniqueId;

    @XmlElement(name = "portal_obj_id")
    private String portalObjectId;

    @XmlElement(name = "anbieter_id")
    private String providerId;

    @XmlElement(name = "oobj_id")
    private String oobjId;

    @XmlElement(name = "zusatz_refnr")
    private String additionalReference;

    @XmlElement(name = "expose_url")
    private String exposeUrl;

    @XmlElement(name = "vermarktungsart")
    private String marketingMethod;

    @XmlElement(name = "bezeichnung")
    private String designation;

    @XmlElement(name = "etage")
    private String floor;

    @XmlElement(name = "whg_nr")
    private String whgNr;

    @XmlElement(name = "strasse")
    private String street;

    @XmlElement(name = "ort")
    private String region;

    @XmlElement(name = "land")
    private String country;

    @XmlElement(name = "stadtbezirk")
    private String borough;

    @XmlElement(name = "preis")
    private String price;

    @XmlElement(name = "gebot")
    private String bid;

    @XmlElement(name = "wae")
    private String wae;

    @XmlElement(name = "anzahl_zimmer")
    private String numberOfRooms;

    @XmlElement(name = "flaeche")
    private String surface;

    @XmlElement(name = "interessent")
    private OpenimmoInterest interest;

    @XmlElement(name = "user_defined_extend")
    private List<OpenimmoUserDefinedField> userDefinedFields;

}
