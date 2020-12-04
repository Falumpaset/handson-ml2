//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.10.16 um 11:10:09 AM CEST 
//

package de.immomio.openimmo;

import javax.xml.bind.annotation.*;

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
 *         &lt;element name="firmenname" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="firmenanschrift" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="telefon" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="vertretungsberechtigter" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="berufsaufsichtsbehoerde" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="handelsregister" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="handelsregister_nr" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="umsst-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="steuernummer" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="weiteres" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "firmenname",
        "firmenanschrift",
        "telefon",
        "vertretungsberechtigter",
        "berufsaufsichtsbehoerde",
        "handelsregister",
        "handelsregisterNr",
        "umsstId",
        "steuernummer",
        "weiteres"
})
@XmlRootElement(name = "impressum_strukt")
public class ImpressumStrukt {

    @XmlElement(required = true)
    protected String firmenname;

    @XmlElement(required = true)
    protected String firmenanschrift;

    @XmlElement(required = true)
    protected String telefon;

    @XmlElement(required = true)
    protected String vertretungsberechtigter;

    @XmlElement(required = true)
    protected String berufsaufsichtsbehoerde;

    @XmlElement(required = true)
    protected String handelsregister;

    @XmlElement(name = "handelsregister_nr", required = true)
    protected String handelsregisterNr;

    @XmlElement(name = "umsst-id", required = true)
    protected String umsstId;

    @XmlElement(required = true)
    protected String steuernummer;

    @XmlElement(required = true)
    protected String weiteres;

    /**
     * Ruft den Wert der firmenname-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getFirmenname() {
        return firmenname;
    }

    /**
     * Legt den Wert der firmenname-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setFirmenname(String value) {
        this.firmenname = value;
    }

    /**
     * Ruft den Wert der firmenanschrift-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getFirmenanschrift() {
        return firmenanschrift;
    }

    /**
     * Legt den Wert der firmenanschrift-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setFirmenanschrift(String value) {
        this.firmenanschrift = value;
    }

    /**
     * Ruft den Wert der telefon-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getTelefon() {
        return telefon;
    }

    /**
     * Legt den Wert der telefon-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setTelefon(String value) {
        this.telefon = value;
    }

    /**
     * Ruft den Wert der vertretungsberechtigter-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getVertretungsberechtigter() {
        return vertretungsberechtigter;
    }

    /**
     * Legt den Wert der vertretungsberechtigter-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setVertretungsberechtigter(String value) {
        this.vertretungsberechtigter = value;
    }

    /**
     * Ruft den Wert der berufsaufsichtsbehoerde-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getBerufsaufsichtsbehoerde() {
        return berufsaufsichtsbehoerde;
    }

    /**
     * Legt den Wert der berufsaufsichtsbehoerde-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setBerufsaufsichtsbehoerde(String value) {
        this.berufsaufsichtsbehoerde = value;
    }

    /**
     * Ruft den Wert der handelsregister-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getHandelsregister() {
        return handelsregister;
    }

    /**
     * Legt den Wert der handelsregister-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setHandelsregister(String value) {
        this.handelsregister = value;
    }

    /**
     * Ruft den Wert der handelsregisterNr-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getHandelsregisterNr() {
        return handelsregisterNr;
    }

    /**
     * Legt den Wert der handelsregisterNr-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setHandelsregisterNr(String value) {
        this.handelsregisterNr = value;
    }

    /**
     * Ruft den Wert der umsstId-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getUmsstId() {
        return umsstId;
    }

    /**
     * Legt den Wert der umsstId-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setUmsstId(String value) {
        this.umsstId = value;
    }

    /**
     * Ruft den Wert der steuernummer-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getSteuernummer() {
        return steuernummer;
    }

    /**
     * Legt den Wert der steuernummer-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setSteuernummer(String value) {
        this.steuernummer = value;
    }

    /**
     * Ruft den Wert der weiteres-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getWeiteres() {
        return weiteres;
    }

    /**
     * Legt den Wert der weiteres-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setWeiteres(String value) {
        this.weiteres = value;
    }

}
