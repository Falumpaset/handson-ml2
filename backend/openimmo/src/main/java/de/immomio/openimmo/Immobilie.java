//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.10.16 um 11:10:09 AM CEST 
//

package de.immomio.openimmo;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Java-Klasse f�r anonymous complex type.
 * <p>
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}objektkategorie"/>
 *         &lt;element ref="{}geo"/>
 *         &lt;element ref="{}kontaktperson"/>
 *         &lt;element ref="{}weitere_adresse" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}preise" minOccurs="0"/>
 *         &lt;element ref="{}bieterverfahren" minOccurs="0"/>
 *         &lt;element ref="{}versteigerung" minOccurs="0"/>
 *         &lt;element ref="{}flaechen" minOccurs="0"/>
 *         &lt;element ref="{}ausstattung" minOccurs="0"/>
 *         &lt;element ref="{}zustand_angaben" minOccurs="0"/>
 *         &lt;element ref="{}bewertung" minOccurs="0"/>
 *         &lt;element ref="{}infrastruktur" minOccurs="0"/>
 *         &lt;element ref="{}freitexte" minOccurs="0"/>
 *         &lt;element ref="{}anhaenge" minOccurs="0"/>
 *         &lt;element ref="{}verwaltung_objekt" minOccurs="0"/>
 *         &lt;element ref="{}verwaltung_techn"/>
 *         &lt;element ref="{}user_defined_simplefield" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}user_defined_anyfield" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}user_defined_extend" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "objektkategorie",
        "geo",
        "kontaktperson",
        "weitereAdresse",
        "preise",
        "bieterverfahren",
        "versteigerung",
        "flaechen",
        "ausstattung",
        "zustandAngaben",
        "bewertung",
        "infrastruktur",
        "freitexte",
        "anhaenge",
        "verwaltungObjekt",
        "verwaltungTechn",
        "userDefinedSimplefield",
        "userDefinedAnyfield",
        "userDefinedExtend"
})
@XmlRootElement(name = "immobilie")
public class Immobilie {

    @XmlElement(required = true)
    protected Objektkategorie objektkategorie;

    @XmlElement(required = true)
    protected Geo geo;

    @XmlElement(required = true)
    protected Kontaktperson kontaktperson;

    @XmlElement(name = "weitere_adresse")
    protected List<WeitereAdresse> weitereAdresse;

    protected Preise preise;

    protected Bieterverfahren bieterverfahren;

    protected Versteigerung versteigerung;

    protected Flaechen flaechen;

    protected Ausstattung ausstattung;

    @XmlElement(name = "zustand_angaben")
    protected ZustandAngaben zustandAngaben;

    protected Bewertung bewertung;

    protected Infrastruktur infrastruktur;

    protected Freitexte freitexte;

    protected Anhaenge anhaenge;

    @XmlElement(name = "verwaltung_objekt")
    protected VerwaltungObjekt verwaltungObjekt;

    @XmlElement(name = "verwaltung_techn", required = true)
    protected VerwaltungTechn verwaltungTechn;

    @XmlElement(name = "user_defined_simplefield")
    protected List<UserDefinedSimplefield> userDefinedSimplefield;

    @XmlElement(name = "user_defined_anyfield")
    protected List<UserDefinedAnyfield> userDefinedAnyfield;

    @XmlElement(name = "user_defined_extend")
    protected List<UserDefinedExtend> userDefinedExtend;

    /**
     * Ruft den Wert der objektkategorie-Eigenschaft ab.
     *
     * @return possible object is {@link Objektkategorie }
     */
    public Objektkategorie getObjektkategorie() {
        return objektkategorie;
    }

    /**
     * Legt den Wert der objektkategorie-Eigenschaft fest.
     *
     * @param value allowed object is {@link Objektkategorie }
     */
    public void setObjektkategorie(Objektkategorie value) {
        this.objektkategorie = value;
    }

    /**
     * Ruft den Wert der geo-Eigenschaft ab.
     *
     * @return possible object is {@link Geo }
     */
    public Geo getGeo() {
        return geo;
    }

    /**
     * Legt den Wert der geo-Eigenschaft fest.
     *
     * @param value allowed object is {@link Geo }
     */
    public void setGeo(Geo value) {
        this.geo = value;
    }

    /**
     * Ruft den Wert der kontaktperson-Eigenschaft ab.
     *
     * @return possible object is {@link Kontaktperson }
     */
    public Kontaktperson getKontaktperson() {
        return kontaktperson;
    }

    /**
     * Legt den Wert der kontaktperson-Eigenschaft fest.
     *
     * @param value allowed object is {@link Kontaktperson }
     */
    public void setKontaktperson(Kontaktperson value) {
        this.kontaktperson = value;
    }

    /**
     * Gets the value of the weitereAdresse property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the weitereAdresse property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWeitereAdresse().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link WeitereAdresse }
     */
    public List<WeitereAdresse> getWeitereAdresse() {
        if (weitereAdresse == null) {
            weitereAdresse = new ArrayList<>();
        }
        return this.weitereAdresse;
    }

    /**
     * Ruft den Wert der preise-Eigenschaft ab.
     *
     * @return possible object is {@link Preise }
     */
    public Preise getPreise() {
        return preise;
    }

    /**
     * Legt den Wert der preise-Eigenschaft fest.
     *
     * @param value allowed object is {@link Preise }
     */
    public void setPreise(Preise value) {
        this.preise = value;
    }

    /**
     * Ruft den Wert der bieterverfahren-Eigenschaft ab.
     *
     * @return possible object is {@link Bieterverfahren }
     */
    public Bieterverfahren getBieterverfahren() {
        return bieterverfahren;
    }

    /**
     * Legt den Wert der bieterverfahren-Eigenschaft fest.
     *
     * @param value allowed object is {@link Bieterverfahren }
     */
    public void setBieterverfahren(Bieterverfahren value) {
        this.bieterverfahren = value;
    }

    /**
     * Ruft den Wert der versteigerung-Eigenschaft ab.
     *
     * @return possible object is {@link Versteigerung }
     */
    public Versteigerung getVersteigerung() {
        return versteigerung;
    }

    /**
     * Legt den Wert der versteigerung-Eigenschaft fest.
     *
     * @param value allowed object is {@link Versteigerung }
     */
    public void setVersteigerung(Versteigerung value) {
        this.versteigerung = value;
    }

    /**
     * Ruft den Wert der flaechen-Eigenschaft ab.
     *
     * @return possible object is {@link Flaechen }
     */
    public Flaechen getFlaechen() {
        return flaechen;
    }

    /**
     * Legt den Wert der flaechen-Eigenschaft fest.
     *
     * @param value allowed object is {@link Flaechen }
     */
    public void setFlaechen(Flaechen value) {
        this.flaechen = value;
    }

    /**
     * Ruft den Wert der ausstattung-Eigenschaft ab.
     *
     * @return possible object is {@link Ausstattung }
     */
    public Ausstattung getAusstattung() {
        return ausstattung;
    }

    /**
     * Legt den Wert der ausstattung-Eigenschaft fest.
     *
     * @param value allowed object is {@link Ausstattung }
     */
    public void setAusstattung(Ausstattung value) {
        this.ausstattung = value;
    }

    /**
     * Ruft den Wert der zustandAngaben-Eigenschaft ab.
     *
     * @return possible object is {@link ZustandAngaben }
     */
    public ZustandAngaben getZustandAngaben() {
        return zustandAngaben;
    }

    /**
     * Legt den Wert der zustandAngaben-Eigenschaft fest.
     *
     * @param value allowed object is {@link ZustandAngaben }
     */
    public void setZustandAngaben(ZustandAngaben value) {
        this.zustandAngaben = value;
    }

    /**
     * Ruft den Wert der bewertung-Eigenschaft ab.
     *
     * @return possible object is {@link Bewertung }
     */
    public Bewertung getBewertung() {
        return bewertung;
    }

    /**
     * Legt den Wert der bewertung-Eigenschaft fest.
     *
     * @param value allowed object is {@link Bewertung }
     */
    public void setBewertung(Bewertung value) {
        this.bewertung = value;
    }

    /**
     * Ruft den Wert der infrastruktur-Eigenschaft ab.
     *
     * @return possible object is {@link Infrastruktur }
     */
    public Infrastruktur getInfrastruktur() {
        return infrastruktur;
    }

    /**
     * Legt den Wert der infrastruktur-Eigenschaft fest.
     *
     * @param value allowed object is {@link Infrastruktur }
     */
    public void setInfrastruktur(Infrastruktur value) {
        this.infrastruktur = value;
    }

    /**
     * Ruft den Wert der freitexte-Eigenschaft ab.
     *
     * @return possible object is {@link Freitexte }
     */
    public Freitexte getFreitexte() {
        return freitexte;
    }

    /**
     * Legt den Wert der freitexte-Eigenschaft fest.
     *
     * @param value allowed object is {@link Freitexte }
     */
    public void setFreitexte(Freitexte value) {
        this.freitexte = value;
    }

    /**
     * Ruft den Wert der anhaenge-Eigenschaft ab.
     *
     * @return possible object is {@link Anhaenge }
     */
    public Anhaenge getAnhaenge() {
        return anhaenge;
    }

    /**
     * Legt den Wert der anhaenge-Eigenschaft fest.
     *
     * @param value allowed object is {@link Anhaenge }
     */
    public void setAnhaenge(Anhaenge value) {
        this.anhaenge = value;
    }

    /**
     * Ruft den Wert der verwaltungObjekt-Eigenschaft ab.
     *
     * @return possible object is {@link VerwaltungObjekt }
     */
    public VerwaltungObjekt getVerwaltungObjekt() {
        return verwaltungObjekt;
    }

    /**
     * Legt den Wert der verwaltungObjekt-Eigenschaft fest.
     *
     * @param value allowed object is {@link VerwaltungObjekt }
     */
    public void setVerwaltungObjekt(VerwaltungObjekt value) {
        this.verwaltungObjekt = value;
    }

    /**
     * Ruft den Wert der verwaltungTechn-Eigenschaft ab.
     *
     * @return possible object is {@link VerwaltungTechn }
     */
    public VerwaltungTechn getVerwaltungTechn() {
        return verwaltungTechn;
    }

    /**
     * Legt den Wert der verwaltungTechn-Eigenschaft fest.
     *
     * @param value allowed object is {@link VerwaltungTechn }
     */
    public void setVerwaltungTechn(VerwaltungTechn value) {
        this.verwaltungTechn = value;
    }

    /**
     * Gets the value of the userDefinedSimplefield property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the userDefinedSimplefield property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUserDefinedSimplefield().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link UserDefinedSimplefield }
     */
    public List<UserDefinedSimplefield> getUserDefinedSimplefield() {
        if (userDefinedSimplefield == null) {
            userDefinedSimplefield = new ArrayList<>();
        }
        return this.userDefinedSimplefield;
    }

    /**
     * Gets the value of the userDefinedAnyfield property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the userDefinedAnyfield property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUserDefinedAnyfield().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link UserDefinedAnyfield }
     */
    public List<UserDefinedAnyfield> getUserDefinedAnyfield() {
        if (userDefinedAnyfield == null) {
            userDefinedAnyfield = new ArrayList<>();
        }
        return this.userDefinedAnyfield;
    }

    /**
     * Gets the value of the userDefinedExtend property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the userDefinedExtend property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUserDefinedExtend().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link UserDefinedExtend }
     */
    public List<UserDefinedExtend> getUserDefinedExtend() {
        if (userDefinedExtend == null) {
            userDefinedExtend = new ArrayList<>();
        }
        return this.userDefinedExtend;
    }

}
