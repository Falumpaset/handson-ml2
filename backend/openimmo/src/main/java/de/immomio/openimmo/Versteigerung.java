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
 *         &lt;element ref="{}zwangsversteigerung" minOccurs="0"/>
 *         &lt;element ref="{}aktenzeichen" minOccurs="0"/>
 *         &lt;element ref="{}zvtermin" minOccurs="0"/>
 *         &lt;element ref="{}zusatztermin" minOccurs="0"/>
 *         &lt;element ref="{}amtsgericht" minOccurs="0"/>
 *         &lt;element ref="{}verkehrswert" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "zwangsversteigerung",
        "aktenzeichen",
        "zvtermin",
        "zusatztermin",
        "amtsgericht",
        "verkehrswert"
})
@XmlRootElement(name = "versteigerung")
public class Versteigerung {

    protected Boolean zwangsversteigerung;

    protected String aktenzeichen;

    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar zvtermin;

    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar zusatztermin;

    protected String amtsgericht;

    protected BigDecimal verkehrswert;

    /**
     * Ruft den Wert der zwangsversteigerung-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isZwangsversteigerung() {
        return zwangsversteigerung;
    }

    /**
     * Legt den Wert der zwangsversteigerung-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setZwangsversteigerung(Boolean value) {
        this.zwangsversteigerung = value;
    }

    /**
     * Ruft den Wert der aktenzeichen-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getAktenzeichen() {
        return aktenzeichen;
    }

    /**
     * Legt den Wert der aktenzeichen-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setAktenzeichen(String value) {
        this.aktenzeichen = value;
    }

    /**
     * Ruft den Wert der zvtermin-Eigenschaft ab.
     *
     * @return possible object is {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getZvtermin() {
        return zvtermin;
    }

    /**
     * Legt den Wert der zvtermin-Eigenschaft fest.
     *
     * @param value allowed object is {@link XMLGregorianCalendar }
     */
    public void setZvtermin(XMLGregorianCalendar value) {
        this.zvtermin = value;
    }

    /**
     * Ruft den Wert der zusatztermin-Eigenschaft ab.
     *
     * @return possible object is {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getZusatztermin() {
        return zusatztermin;
    }

    /**
     * Legt den Wert der zusatztermin-Eigenschaft fest.
     *
     * @param value allowed object is {@link XMLGregorianCalendar }
     */
    public void setZusatztermin(XMLGregorianCalendar value) {
        this.zusatztermin = value;
    }

    /**
     * Ruft den Wert der amtsgericht-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getAmtsgericht() {
        return amtsgericht;
    }

    /**
     * Legt den Wert der amtsgericht-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setAmtsgericht(String value) {
        this.amtsgericht = value;
    }

    /**
     * Ruft den Wert der verkehrswert-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getVerkehrswert() {
        return verkehrswert;
    }

    /**
     * Legt den Wert der verkehrswert-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setVerkehrswert(BigDecimal value) {
        this.verkehrswert = value;
    }

}
