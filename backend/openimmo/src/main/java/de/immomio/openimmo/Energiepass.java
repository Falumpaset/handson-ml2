//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.10.16 um 11:10:09 AM CEST 
//

package de.immomio.openimmo;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;

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
 *         &lt;element name="epart" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="BEDARF"/>
 *               &lt;enumeration value="VERBRAUCH"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="gueltig_bis" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="energieverbrauchkennwert" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mitwarmwasser" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="endenergiebedarf" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="primaerenergietraeger" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="stromwert" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="waermewert" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="wertklasse" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="baujahr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ausstelldatum" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="jahrgang" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="2008"/>
 *               &lt;enumeration value="2014"/>
 *               &lt;enumeration value="ohne"/>
 *               &lt;enumeration value="nicht_noetig"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="gebaeudeart" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="wohn"/>
 *               &lt;enumeration value="nichtwohn"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="epasstext" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="hwbwert" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="hwbklasse" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fgeewert" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fgeeklasse" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "epart",
        "gueltigBis",
        "energieverbrauchkennwert",
        "mitwarmwasser",
        "endenergiebedarf",
        "primaerenergietraeger",
        "stromwert",
        "waermewert",
        "wertklasse",
        "baujahr",
        "ausstelldatum",
        "jahrgang",
        "gebaeudeart",
        "epasstext",
        "hwbwert",
        "hwbklasse",
        "fgeewert",
        "fgeeklasse"
})
@XmlRootElement(name = "energiepass")
public class Energiepass {

    protected String epart;

    @XmlElement(name = "gueltig_bis")
    protected String gueltigBis;

    protected String energieverbrauchkennwert;

    protected Boolean mitwarmwasser;

    protected String endenergiebedarf;

    protected String primaerenergietraeger;

    protected String stromwert;

    protected String waermewert;

    protected String wertklasse;

    protected String baujahr;

    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar ausstelldatum;

    protected String jahrgang;

    protected String gebaeudeart;

    protected String epasstext;

    protected String hwbwert;

    protected String hwbklasse;

    protected String fgeewert;

    protected String fgeeklasse;

    /**
     * Ruft den Wert der epart-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getEpart() {
        return epart;
    }

    /**
     * Legt den Wert der epart-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setEpart(String value) {
        this.epart = value;
    }

    /**
     * Ruft den Wert der gueltigBis-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getGueltigBis() {
        return gueltigBis;
    }

    /**
     * Legt den Wert der gueltigBis-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setGueltigBis(String value) {
        this.gueltigBis = value;
    }

    /**
     * Ruft den Wert der energieverbrauchkennwert-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getEnergieverbrauchkennwert() {
        return energieverbrauchkennwert;
    }

    /**
     * Legt den Wert der energieverbrauchkennwert-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setEnergieverbrauchkennwert(String value) {
        this.energieverbrauchkennwert = value;
    }

    /**
     * Ruft den Wert der mitwarmwasser-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isMitwarmwasser() {
        return mitwarmwasser;
    }

    /**
     * Legt den Wert der mitwarmwasser-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setMitwarmwasser(Boolean value) {
        this.mitwarmwasser = value;
    }

    /**
     * Ruft den Wert der endenergiebedarf-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getEndenergiebedarf() {
        return endenergiebedarf;
    }

    /**
     * Legt den Wert der endenergiebedarf-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setEndenergiebedarf(String value) {
        this.endenergiebedarf = value;
    }

    /**
     * Ruft den Wert der primaerenergietraeger-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getPrimaerenergietraeger() {
        return primaerenergietraeger;
    }

    /**
     * Legt den Wert der primaerenergietraeger-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setPrimaerenergietraeger(String value) {
        this.primaerenergietraeger = value;
    }

    /**
     * Ruft den Wert der stromwert-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getStromwert() {
        return stromwert;
    }

    /**
     * Legt den Wert der stromwert-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setStromwert(String value) {
        this.stromwert = value;
    }

    /**
     * Ruft den Wert der waermewert-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getWaermewert() {
        return waermewert;
    }

    /**
     * Legt den Wert der waermewert-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setWaermewert(String value) {
        this.waermewert = value;
    }

    /**
     * Ruft den Wert der wertklasse-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getWertklasse() {
        return wertklasse;
    }

    /**
     * Legt den Wert der wertklasse-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setWertklasse(String value) {
        this.wertklasse = value;
    }

    /**
     * Ruft den Wert der baujahr-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getBaujahr() {
        return baujahr;
    }

    /**
     * Legt den Wert der baujahr-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setBaujahr(String value) {
        this.baujahr = value;
    }

    /**
     * Ruft den Wert der ausstelldatum-Eigenschaft ab.
     *
     * @return possible object is {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getAusstelldatum() {
        return ausstelldatum;
    }

    /**
     * Legt den Wert der ausstelldatum-Eigenschaft fest.
     *
     * @param value allowed object is {@link XMLGregorianCalendar }
     */
    public void setAusstelldatum(XMLGregorianCalendar value) {
        this.ausstelldatum = value;
    }

    /**
     * Ruft den Wert der jahrgang-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getJahrgang() {
        return jahrgang;
    }

    /**
     * Legt den Wert der jahrgang-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setJahrgang(String value) {
        this.jahrgang = value;
    }

    /**
     * Ruft den Wert der gebaeudeart-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getGebaeudeart() {
        return gebaeudeart;
    }

    /**
     * Legt den Wert der gebaeudeart-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setGebaeudeart(String value) {
        this.gebaeudeart = value;
    }

    /**
     * Ruft den Wert der epasstext-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getEpasstext() {
        return epasstext;
    }

    /**
     * Legt den Wert der epasstext-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setEpasstext(String value) {
        this.epasstext = value;
    }

    /**
     * Ruft den Wert der hwbwert-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getHwbwert() {
        return hwbwert;
    }

    /**
     * Legt den Wert der hwbwert-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setHwbwert(String value) {
        this.hwbwert = value;
    }

    /**
     * Ruft den Wert der hwbklasse-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getHwbklasse() {
        return hwbklasse;
    }

    /**
     * Legt den Wert der hwbklasse-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setHwbklasse(String value) {
        this.hwbklasse = value;
    }

    /**
     * Ruft den Wert der fgeewert-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getFgeewert() {
        return fgeewert;
    }

    /**
     * Legt den Wert der fgeewert-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setFgeewert(String value) {
        this.fgeewert = value;
    }

    /**
     * Ruft den Wert der fgeeklasse-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getFgeeklasse() {
        return fgeeklasse;
    }

    /**
     * Legt den Wert der fgeeklasse-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setFgeeklasse(String value) {
        this.fgeeklasse = value;
    }

}
