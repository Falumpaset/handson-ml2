//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.10.16 um 11:10:09 AM CEST 
//

package de.immomio.openimmo;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
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
 *         &lt;element ref="{}objektnr_intern" minOccurs="0"/>
 *         &lt;element ref="{}objektnr_extern"/>
 *         &lt;element ref="{}aktion"/>
 *         &lt;element ref="{}aktiv_von" minOccurs="0"/>
 *         &lt;element ref="{}aktiv_bis" minOccurs="0"/>
 *         &lt;element ref="{}openimmo_obid"/>
 *         &lt;element ref="{}kennung_ursprung" minOccurs="0"/>
 *         &lt;element ref="{}stand_vom"/>
 *         &lt;element ref="{}weitergabe_generell" minOccurs="0"/>
 *         &lt;element ref="{}weitergabe_positiv" minOccurs="0"/>
 *         &lt;element ref="{}weitergabe_negativ" minOccurs="0"/>
 *         &lt;element ref="{}gruppen_kennung" minOccurs="0"/>
 *         &lt;element ref="{}master" minOccurs="0"/>
 *         &lt;element ref="{}sprache" minOccurs="0"/>
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
        "objektnrIntern",
        "objektnrExtern",
        "aktion",
        "aktivVon",
        "aktivBis",
        "openimmoObid",
        "kennungUrsprung",
        "standVom",
        "weitergabeGenerell",
        "weitergabePositiv",
        "weitergabeNegativ",
        "gruppenKennung",
        "master",
        "sprache",
        "userDefinedSimplefield",
        "userDefinedAnyfield",
        "userDefinedExtend"
})
@XmlRootElement(name = "verwaltung_techn")
public class VerwaltungTechn {

    @XmlElement(name = "objektnr_intern")
    protected String objektnrIntern;

    @XmlElement(name = "objektnr_extern", required = true)
    protected String objektnrExtern;

    @XmlElement(required = true)
    protected Aktion aktion;

    @XmlElement(name = "aktiv_von")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar aktivVon;

    @XmlElement(name = "aktiv_bis")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar aktivBis;

    @XmlElement(name = "openimmo_obid", required = true)
    protected String openimmoObid;

    @XmlElement(name = "kennung_ursprung")
    protected String kennungUrsprung;

    @XmlElement(name = "stand_vom", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar standVom;

    @XmlElement(name = "weitergabe_generell")
    protected Boolean weitergabeGenerell;

    @XmlElement(name = "weitergabe_positiv")
    protected String weitergabePositiv;

    @XmlElement(name = "weitergabe_negativ")
    protected String weitergabeNegativ;

    @XmlElement(name = "gruppen_kennung")
    protected String gruppenKennung;

    protected Master master;

    protected String sprache;

    @XmlElement(name = "user_defined_simplefield")
    protected List<UserDefinedSimplefield> userDefinedSimplefield;

    @XmlElement(name = "user_defined_anyfield")
    protected List<UserDefinedAnyfield> userDefinedAnyfield;

    @XmlElement(name = "user_defined_extend")
    protected List<UserDefinedExtend> userDefinedExtend;

    /**
     * Ruft den Wert der objektnrIntern-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getObjektnrIntern() {
        return objektnrIntern;
    }

    /**
     * Legt den Wert der objektnrIntern-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setObjektnrIntern(String value) {
        this.objektnrIntern = value;
    }

    /**
     * Ruft den Wert der objektnrExtern-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getObjektnrExtern() {
        return objektnrExtern;
    }

    /**
     * Legt den Wert der objektnrExtern-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setObjektnrExtern(String value) {
        this.objektnrExtern = value;
    }

    /**
     * Ruft den Wert der aktion-Eigenschaft ab.
     *
     * @return possible object is {@link Aktion }
     */
    public Aktion getAktion() {
        return aktion;
    }

    /**
     * Legt den Wert der aktion-Eigenschaft fest.
     *
     * @param value allowed object is {@link Aktion }
     */
    public void setAktion(Aktion value) {
        this.aktion = value;
    }

    /**
     * Ruft den Wert der aktivVon-Eigenschaft ab.
     *
     * @return possible object is {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getAktivVon() {
        return aktivVon;
    }

    /**
     * Legt den Wert der aktivVon-Eigenschaft fest.
     *
     * @param value allowed object is {@link XMLGregorianCalendar }
     */
    public void setAktivVon(XMLGregorianCalendar value) {
        this.aktivVon = value;
    }

    /**
     * Ruft den Wert der aktivBis-Eigenschaft ab.
     *
     * @return possible object is {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getAktivBis() {
        return aktivBis;
    }

    /**
     * Legt den Wert der aktivBis-Eigenschaft fest.
     *
     * @param value allowed object is {@link XMLGregorianCalendar }
     */
    public void setAktivBis(XMLGregorianCalendar value) {
        this.aktivBis = value;
    }

    /**
     * Ruft den Wert der openimmoObid-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getOpenimmoObid() {
        return openimmoObid;
    }

    /**
     * Legt den Wert der openimmoObid-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setOpenimmoObid(String value) {
        this.openimmoObid = value;
    }

    /**
     * Ruft den Wert der kennungUrsprung-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getKennungUrsprung() {
        return kennungUrsprung;
    }

    /**
     * Legt den Wert der kennungUrsprung-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setKennungUrsprung(String value) {
        this.kennungUrsprung = value;
    }

    /**
     * Ruft den Wert der standVom-Eigenschaft ab.
     *
     * @return possible object is {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getStandVom() {
        return standVom;
    }

    /**
     * Legt den Wert der standVom-Eigenschaft fest.
     *
     * @param value allowed object is {@link XMLGregorianCalendar }
     */
    public void setStandVom(XMLGregorianCalendar value) {
        this.standVom = value;
    }

    /**
     * Ruft den Wert der weitergabeGenerell-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isWeitergabeGenerell() {
        return weitergabeGenerell;
    }

    /**
     * Legt den Wert der weitergabeGenerell-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setWeitergabeGenerell(Boolean value) {
        this.weitergabeGenerell = value;
    }

    /**
     * Ruft den Wert der weitergabePositiv-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getWeitergabePositiv() {
        return weitergabePositiv;
    }

    /**
     * Legt den Wert der weitergabePositiv-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setWeitergabePositiv(String value) {
        this.weitergabePositiv = value;
    }

    /**
     * Ruft den Wert der weitergabeNegativ-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getWeitergabeNegativ() {
        return weitergabeNegativ;
    }

    /**
     * Legt den Wert der weitergabeNegativ-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setWeitergabeNegativ(String value) {
        this.weitergabeNegativ = value;
    }

    /**
     * Ruft den Wert der gruppenKennung-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getGruppenKennung() {
        return gruppenKennung;
    }

    /**
     * Legt den Wert der gruppenKennung-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setGruppenKennung(String value) {
        this.gruppenKennung = value;
    }

    /**
     * Ruft den Wert der master-Eigenschaft ab.
     *
     * @return possible object is {@link Master }
     */
    public Master getMaster() {
        return master;
    }

    /**
     * Legt den Wert der master-Eigenschaft fest.
     *
     * @param value allowed object is {@link Master }
     */
    public void setMaster(Master value) {
        this.master = value;
    }

    /**
     * Ruft den Wert der sprache-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getSprache() {
        return sprache;
    }

    /**
     * Legt den Wert der sprache-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setSprache(String value) {
        this.sprache = value;
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
