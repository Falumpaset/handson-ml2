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
 *         &lt;element ref="{}vorname" minOccurs="0"/>
 *         &lt;element ref="{}name" minOccurs="0"/>
 *         &lt;element ref="{}titel" minOccurs="0"/>
 *         &lt;element ref="{}anrede" minOccurs="0"/>
 *         &lt;element ref="{}anrede_brief" minOccurs="0"/>
 *         &lt;element ref="{}firma" minOccurs="0"/>
 *         &lt;element ref="{}zusatzfeld" minOccurs="0"/>
 *         &lt;element ref="{}strasse" minOccurs="0"/>
 *         &lt;element ref="{}hausnummer" minOccurs="0"/>
 *         &lt;element ref="{}plz" minOccurs="0"/>
 *         &lt;element ref="{}ort" minOccurs="0"/>
 *         &lt;element ref="{}postfach" minOccurs="0"/>
 *         &lt;element ref="{}postf_plz" minOccurs="0"/>
 *         &lt;element ref="{}postf_ort" minOccurs="0"/>
 *         &lt;element ref="{}land" minOccurs="0"/>
 *         &lt;element ref="{}email_zentrale" minOccurs="0"/>
 *         &lt;element ref="{}email_direkt" minOccurs="0"/>
 *         &lt;element ref="{}email_privat" minOccurs="0"/>
 *         &lt;element ref="{}email_sonstige" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}tel_durchw" minOccurs="0"/>
 *         &lt;element ref="{}tel_zentrale" minOccurs="0"/>
 *         &lt;element ref="{}tel_handy" minOccurs="0"/>
 *         &lt;element ref="{}tel_fax" minOccurs="0"/>
 *         &lt;element ref="{}tel_privat" minOccurs="0"/>
 *         &lt;element ref="{}tel_sonstige" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}url" minOccurs="0"/>
 *         &lt;element ref="{}adressfreigabe" minOccurs="0"/>
 *         &lt;element ref="{}personennummer" minOccurs="0"/>
 *         &lt;element ref="{}freitextfeld" minOccurs="0"/>
 *         &lt;element ref="{}user_defined_simplefield" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}user_defined_anyfield" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}user_defined_extend" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="adressart" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "vorname",
        "name",
        "titel",
        "anrede",
        "anredeBrief",
        "firma",
        "zusatzfeld",
        "strasse",
        "hausnummer",
        "plz",
        "ort",
        "postfach",
        "postfPlz",
        "postfOrt",
        "land",
        "emailZentrale",
        "emailDirekt",
        "emailPrivat",
        "emailSonstige",
        "telDurchw",
        "telZentrale",
        "telHandy",
        "telFax",
        "telPrivat",
        "telSonstige",
        "url",
        "adressfreigabe",
        "personennummer",
        "freitextfeld",
        "userDefinedSimplefield",
        "userDefinedAnyfield",
        "userDefinedExtend"
})
@XmlRootElement(name = "weitere_adresse")
public class WeitereAdresse {

    protected String vorname;

    protected String name;

    protected String titel;

    protected String anrede;

    @XmlElement(name = "anrede_brief")
    protected String anredeBrief;

    protected String firma;

    protected Object zusatzfeld;

    protected String strasse;

    protected String hausnummer;

    protected String plz;

    protected String ort;

    protected String postfach;

    @XmlElement(name = "postf_plz")
    protected String postfPlz;

    @XmlElement(name = "postf_ort")
    protected String postfOrt;

    protected Land land;

    @XmlElement(name = "email_zentrale")
    protected String emailZentrale;

    @XmlElement(name = "email_direkt")
    protected String emailDirekt;

    @XmlElement(name = "email_privat")
    protected String emailPrivat;

    @XmlElement(name = "email_sonstige")
    protected List<EmailSonstige> emailSonstige;

    @XmlElement(name = "tel_durchw")
    protected String telDurchw;

    @XmlElement(name = "tel_zentrale")
    protected String telZentrale;

    @XmlElement(name = "tel_handy")
    protected String telHandy;

    @XmlElement(name = "tel_fax")
    protected String telFax;

    @XmlElement(name = "tel_privat")
    protected String telPrivat;

    @XmlElement(name = "tel_sonstige")
    protected List<TelSonstige> telSonstige;

    protected String url;

    protected Boolean adressfreigabe;

    protected String personennummer;

    protected String freitextfeld;

    @XmlElement(name = "user_defined_simplefield")
    protected List<UserDefinedSimplefield> userDefinedSimplefield;

    @XmlElement(name = "user_defined_anyfield")
    protected List<UserDefinedAnyfield> userDefinedAnyfield;

    @XmlElement(name = "user_defined_extend")
    protected List<UserDefinedExtend> userDefinedExtend;

    @XmlAttribute(name = "adressart", required = true)
    protected String adressart;

    /**
     * Ruft den Wert der vorname-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getVorname() {
        return vorname;
    }

    /**
     * Legt den Wert der vorname-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setVorname(String value) {
        this.vorname = value;
    }

    /**
     * Ruft den Wert der name-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * Legt den Wert der name-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Ruft den Wert der titel-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getTitel() {
        return titel;
    }

    /**
     * Legt den Wert der titel-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setTitel(String value) {
        this.titel = value;
    }

    /**
     * Ruft den Wert der anrede-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getAnrede() {
        return anrede;
    }

    /**
     * Legt den Wert der anrede-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setAnrede(String value) {
        this.anrede = value;
    }

    /**
     * Ruft den Wert der anredeBrief-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getAnredeBrief() {
        return anredeBrief;
    }

    /**
     * Legt den Wert der anredeBrief-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setAnredeBrief(String value) {
        this.anredeBrief = value;
    }

    /**
     * Ruft den Wert der firma-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getFirma() {
        return firma;
    }

    /**
     * Legt den Wert der firma-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setFirma(String value) {
        this.firma = value;
    }

    /**
     * Ruft den Wert der zusatzfeld-Eigenschaft ab.
     *
     * @return possible object is {@link Object }
     */
    public Object getZusatzfeld() {
        return zusatzfeld;
    }

    /**
     * Legt den Wert der zusatzfeld-Eigenschaft fest.
     *
     * @param value allowed object is {@link Object }
     */
    public void setZusatzfeld(Object value) {
        this.zusatzfeld = value;
    }

    /**
     * Ruft den Wert der strasse-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getStrasse() {
        return strasse;
    }

    /**
     * Legt den Wert der strasse-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setStrasse(String value) {
        this.strasse = value;
    }

    /**
     * Ruft den Wert der hausnummer-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getHausnummer() {
        return hausnummer;
    }

    /**
     * Legt den Wert der hausnummer-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setHausnummer(String value) {
        this.hausnummer = value;
    }

    /**
     * Ruft den Wert der plz-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getPlz() {
        return plz;
    }

    /**
     * Legt den Wert der plz-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setPlz(String value) {
        this.plz = value;
    }

    /**
     * Ruft den Wert der ort-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getOrt() {
        return ort;
    }

    /**
     * Legt den Wert der ort-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setOrt(String value) {
        this.ort = value;
    }

    /**
     * Ruft den Wert der postfach-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getPostfach() {
        return postfach;
    }

    /**
     * Legt den Wert der postfach-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setPostfach(String value) {
        this.postfach = value;
    }

    /**
     * Ruft den Wert der postfPlz-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getPostfPlz() {
        return postfPlz;
    }

    /**
     * Legt den Wert der postfPlz-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setPostfPlz(String value) {
        this.postfPlz = value;
    }

    /**
     * Ruft den Wert der postfOrt-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getPostfOrt() {
        return postfOrt;
    }

    /**
     * Legt den Wert der postfOrt-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setPostfOrt(String value) {
        this.postfOrt = value;
    }

    /**
     * Ruft den Wert der land-Eigenschaft ab.
     *
     * @return possible object is {@link Land }
     */
    public Land getLand() {
        return land;
    }

    /**
     * Legt den Wert der land-Eigenschaft fest.
     *
     * @param value allowed object is {@link Land }
     */
    public void setLand(Land value) {
        this.land = value;
    }

    /**
     * Ruft den Wert der emailZentrale-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getEmailZentrale() {
        return emailZentrale;
    }

    /**
     * Legt den Wert der emailZentrale-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setEmailZentrale(String value) {
        this.emailZentrale = value;
    }

    /**
     * Ruft den Wert der emailDirekt-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getEmailDirekt() {
        return emailDirekt;
    }

    /**
     * Legt den Wert der emailDirekt-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setEmailDirekt(String value) {
        this.emailDirekt = value;
    }

    /**
     * Ruft den Wert der emailPrivat-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getEmailPrivat() {
        return emailPrivat;
    }

    /**
     * Legt den Wert der emailPrivat-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setEmailPrivat(String value) {
        this.emailPrivat = value;
    }

    /**
     * Gets the value of the emailSonstige property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the emailSonstige property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEmailSonstige().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link EmailSonstige }
     */
    public List<EmailSonstige> getEmailSonstige() {
        if (emailSonstige == null) {
            emailSonstige = new ArrayList<>();
        }
        return this.emailSonstige;
    }

    /**
     * Ruft den Wert der telDurchw-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getTelDurchw() {
        return telDurchw;
    }

    /**
     * Legt den Wert der telDurchw-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setTelDurchw(String value) {
        this.telDurchw = value;
    }

    /**
     * Ruft den Wert der telZentrale-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getTelZentrale() {
        return telZentrale;
    }

    /**
     * Legt den Wert der telZentrale-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setTelZentrale(String value) {
        this.telZentrale = value;
    }

    /**
     * Ruft den Wert der telHandy-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getTelHandy() {
        return telHandy;
    }

    /**
     * Legt den Wert der telHandy-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setTelHandy(String value) {
        this.telHandy = value;
    }

    /**
     * Ruft den Wert der telFax-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getTelFax() {
        return telFax;
    }

    /**
     * Legt den Wert der telFax-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setTelFax(String value) {
        this.telFax = value;
    }

    /**
     * Ruft den Wert der telPrivat-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getTelPrivat() {
        return telPrivat;
    }

    /**
     * Legt den Wert der telPrivat-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setTelPrivat(String value) {
        this.telPrivat = value;
    }

    /**
     * Gets the value of the telSonstige property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the telSonstige property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTelSonstige().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link TelSonstige }
     */
    public List<TelSonstige> getTelSonstige() {
        if (telSonstige == null) {
            telSonstige = new ArrayList<>();
        }
        return this.telSonstige;
    }

    /**
     * Ruft den Wert der url-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getUrl() {
        return url;
    }

    /**
     * Legt den Wert der url-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setUrl(String value) {
        this.url = value;
    }

    /**
     * Ruft den Wert der adressfreigabe-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isAdressfreigabe() {
        return adressfreigabe;
    }

    /**
     * Legt den Wert der adressfreigabe-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setAdressfreigabe(Boolean value) {
        this.adressfreigabe = value;
    }

    /**
     * Ruft den Wert der personennummer-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getPersonennummer() {
        return personennummer;
    }

    /**
     * Legt den Wert der personennummer-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setPersonennummer(String value) {
        this.personennummer = value;
    }

    /**
     * Ruft den Wert der freitextfeld-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getFreitextfeld() {
        return freitextfeld;
    }

    /**
     * Legt den Wert der freitextfeld-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setFreitextfeld(String value) {
        this.freitextfeld = value;
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

    /**
     * Ruft den Wert der adressart-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getAdressart() {
        return adressart;
    }

    /**
     * Legt den Wert der adressart-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setAdressart(String value) {
        this.adressart = value;
    }

}
