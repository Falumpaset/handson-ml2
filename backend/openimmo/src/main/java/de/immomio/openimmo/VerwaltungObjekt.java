//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.10.16 um 11:10:09 AM CEST 
//

package de.immomio.openimmo;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.BigInteger;
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
 *         &lt;element ref="{}objektadresse_freigeben" minOccurs="0"/>
 *         &lt;element ref="{}verfuegbar_ab" minOccurs="0"/>
 *         &lt;element ref="{}abdatum" minOccurs="0"/>
 *         &lt;element ref="{}bisdatum" minOccurs="0"/>
 *         &lt;element ref="{}min_mietdauer" minOccurs="0"/>
 *         &lt;element ref="{}max_mietdauer" minOccurs="0"/>
 *         &lt;element ref="{}versteigerungstermin" minOccurs="0"/>
 *         &lt;element ref="{}wbs_sozialwohnung" minOccurs="0"/>
 *         &lt;element ref="{}vermietet" minOccurs="0"/>
 *         &lt;element ref="{}gruppennummer" minOccurs="0"/>
 *         &lt;element ref="{}zugang" minOccurs="0"/>
 *         &lt;element ref="{}laufzeit" minOccurs="0"/>
 *         &lt;element ref="{}max_personen" minOccurs="0"/>
 *         &lt;element ref="{}nichtraucher" minOccurs="0"/>
 *         &lt;element ref="{}haustiere" minOccurs="0"/>
 *         &lt;element ref="{}geschlecht" minOccurs="0"/>
 *         &lt;element ref="{}denkmalgeschuetzt" minOccurs="0"/>
 *         &lt;element ref="{}als_ferien" minOccurs="0"/>
 *         &lt;element ref="{}gewerbliche_nutzung" minOccurs="0"/>
 *         &lt;element ref="{}branchen" minOccurs="0"/>
 *         &lt;element ref="{}hochhaus" minOccurs="0"/>
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
        "objektadresseFreigeben",
        "verfuegbarAb",
        "abdatum",
        "bisdatum",
        "minMietdauer",
        "maxMietdauer",
        "versteigerungstermin",
        "wbsSozialwohnung",
        "vermietet",
        "gruppennummer",
        "zugang",
        "laufzeit",
        "maxPersonen",
        "nichtraucher",
        "haustiere",
        "geschlecht",
        "denkmalgeschuetzt",
        "alsFerien",
        "gewerblicheNutzung",
        "branchen",
        "hochhaus",
        "userDefinedSimplefield",
        "userDefinedAnyfield",
        "userDefinedExtend"
})
@XmlRootElement(name = "verwaltung_objekt")
public class VerwaltungObjekt {

    @XmlElement(name = "objektadresse_freigeben")
    protected Boolean objektadresseFreigeben;

    @XmlElement(name = "verfuegbar_ab")
    protected String verfuegbarAb;

    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar abdatum;

    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar bisdatum;

    @XmlElement(name = "min_mietdauer")
    protected MinMietdauer minMietdauer;

    @XmlElement(name = "max_mietdauer")
    protected MaxMietdauer maxMietdauer;

    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar versteigerungstermin;

    @XmlElement(name = "wbs_sozialwohnung")
    protected Boolean wbsSozialwohnung;

    protected Boolean vermietet;

    protected String gruppennummer;

    protected String zugang;

    protected BigDecimal laufzeit;

    @XmlElement(name = "max_personen")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger maxPersonen;

    protected Boolean nichtraucher;

    protected Boolean haustiere;

    protected Geschlecht geschlecht;

    protected Boolean denkmalgeschuetzt;

    @XmlElement(name = "als_ferien")
    protected Boolean alsFerien;

    @XmlElement(name = "gewerbliche_nutzung")
    protected Boolean gewerblicheNutzung;

    protected String branchen;

    protected Boolean hochhaus;

    @XmlElement(name = "user_defined_simplefield")
    protected List<UserDefinedSimplefield> userDefinedSimplefield;

    @XmlElement(name = "user_defined_anyfield")
    protected List<UserDefinedAnyfield> userDefinedAnyfield;

    @XmlElement(name = "user_defined_extend")
    protected List<UserDefinedExtend> userDefinedExtend;

    /**
     * Ruft den Wert der objektadresseFreigeben-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isObjektadresseFreigeben() {
        return objektadresseFreigeben;
    }

    /**
     * Legt den Wert der objektadresseFreigeben-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setObjektadresseFreigeben(Boolean value) {
        this.objektadresseFreigeben = value;
    }

    /**
     * Ruft den Wert der verfuegbarAb-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getVerfuegbarAb() {
        return verfuegbarAb;
    }

    /**
     * Legt den Wert der verfuegbarAb-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setVerfuegbarAb(String value) {
        this.verfuegbarAb = value;
    }

    /**
     * Ruft den Wert der abdatum-Eigenschaft ab.
     *
     * @return possible object is {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getAbdatum() {
        return abdatum;
    }

    /**
     * Legt den Wert der abdatum-Eigenschaft fest.
     *
     * @param value allowed object is {@link XMLGregorianCalendar }
     */
    public void setAbdatum(XMLGregorianCalendar value) {
        this.abdatum = value;
    }

    /**
     * Ruft den Wert der bisdatum-Eigenschaft ab.
     *
     * @return possible object is {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getBisdatum() {
        return bisdatum;
    }

    /**
     * Legt den Wert der bisdatum-Eigenschaft fest.
     *
     * @param value allowed object is {@link XMLGregorianCalendar }
     */
    public void setBisdatum(XMLGregorianCalendar value) {
        this.bisdatum = value;
    }

    /**
     * Ruft den Wert der minMietdauer-Eigenschaft ab.
     *
     * @return possible object is {@link MinMietdauer }
     */
    public MinMietdauer getMinMietdauer() {
        return minMietdauer;
    }

    /**
     * Legt den Wert der minMietdauer-Eigenschaft fest.
     *
     * @param value allowed object is {@link MinMietdauer }
     */
    public void setMinMietdauer(MinMietdauer value) {
        this.minMietdauer = value;
    }

    /**
     * Ruft den Wert der maxMietdauer-Eigenschaft ab.
     *
     * @return possible object is {@link MaxMietdauer }
     */
    public MaxMietdauer getMaxMietdauer() {
        return maxMietdauer;
    }

    /**
     * Legt den Wert der maxMietdauer-Eigenschaft fest.
     *
     * @param value allowed object is {@link MaxMietdauer }
     */
    public void setMaxMietdauer(MaxMietdauer value) {
        this.maxMietdauer = value;
    }

    /**
     * Ruft den Wert der versteigerungstermin-Eigenschaft ab.
     *
     * @return possible object is {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getVersteigerungstermin() {
        return versteigerungstermin;
    }

    /**
     * Legt den Wert der versteigerungstermin-Eigenschaft fest.
     *
     * @param value allowed object is {@link XMLGregorianCalendar }
     */
    public void setVersteigerungstermin(XMLGregorianCalendar value) {
        this.versteigerungstermin = value;
    }

    /**
     * Ruft den Wert der wbsSozialwohnung-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isWbsSozialwohnung() {
        return wbsSozialwohnung;
    }

    /**
     * Legt den Wert der wbsSozialwohnung-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setWbsSozialwohnung(Boolean value) {
        this.wbsSozialwohnung = value;
    }

    /**
     * Ruft den Wert der vermietet-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isVermietet() {
        return vermietet;
    }

    /**
     * Legt den Wert der vermietet-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setVermietet(Boolean value) {
        this.vermietet = value;
    }

    /**
     * Ruft den Wert der gruppennummer-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getGruppennummer() {
        return gruppennummer;
    }

    /**
     * Legt den Wert der gruppennummer-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setGruppennummer(String value) {
        this.gruppennummer = value;
    }

    /**
     * Ruft den Wert der zugang-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getZugang() {
        return zugang;
    }

    /**
     * Legt den Wert der zugang-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setZugang(String value) {
        this.zugang = value;
    }

    /**
     * Ruft den Wert der laufzeit-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getLaufzeit() {
        return laufzeit;
    }

    /**
     * Legt den Wert der laufzeit-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setLaufzeit(BigDecimal value) {
        this.laufzeit = value;
    }

    /**
     * Ruft den Wert der maxPersonen-Eigenschaft ab.
     *
     * @return possible object is {@link BigInteger }
     */
    public BigInteger getMaxPersonen() {
        return maxPersonen;
    }

    /**
     * Legt den Wert der maxPersonen-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigInteger }
     */
    public void setMaxPersonen(BigInteger value) {
        this.maxPersonen = value;
    }

    /**
     * Ruft den Wert der nichtraucher-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isNichtraucher() {
        return nichtraucher;
    }

    /**
     * Legt den Wert der nichtraucher-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setNichtraucher(Boolean value) {
        this.nichtraucher = value;
    }

    /**
     * Ruft den Wert der haustiere-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isHaustiere() {
        return haustiere;
    }

    /**
     * Legt den Wert der haustiere-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setHaustiere(Boolean value) {
        this.haustiere = value;
    }

    /**
     * Ruft den Wert der geschlecht-Eigenschaft ab.
     *
     * @return possible object is {@link Geschlecht }
     */
    public Geschlecht getGeschlecht() {
        return geschlecht;
    }

    /**
     * Legt den Wert der geschlecht-Eigenschaft fest.
     *
     * @param value allowed object is {@link Geschlecht }
     */
    public void setGeschlecht(Geschlecht value) {
        this.geschlecht = value;
    }

    /**
     * Ruft den Wert der denkmalgeschuetzt-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isDenkmalgeschuetzt() {
        return denkmalgeschuetzt;
    }

    /**
     * Legt den Wert der denkmalgeschuetzt-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setDenkmalgeschuetzt(Boolean value) {
        this.denkmalgeschuetzt = value;
    }

    /**
     * Ruft den Wert der alsFerien-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isAlsFerien() {
        return alsFerien;
    }

    /**
     * Legt den Wert der alsFerien-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setAlsFerien(Boolean value) {
        this.alsFerien = value;
    }

    /**
     * Ruft den Wert der gewerblicheNutzung-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isGewerblicheNutzung() {
        return gewerblicheNutzung;
    }

    /**
     * Legt den Wert der gewerblicheNutzung-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setGewerblicheNutzung(Boolean value) {
        this.gewerblicheNutzung = value;
    }

    /**
     * Ruft den Wert der branchen-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getBranchen() {
        return branchen;
    }

    /**
     * Legt den Wert der branchen-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setBranchen(String value) {
        this.branchen = value;
    }

    /**
     * Ruft den Wert der hochhaus-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isHochhaus() {
        return hochhaus;
    }

    /**
     * Legt den Wert der hochhaus-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setHochhaus(Boolean value) {
        this.hochhaus = value;
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
