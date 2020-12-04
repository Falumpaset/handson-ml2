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
 *         &lt;element ref="{}anhangtitel" minOccurs="0"/>
 *         &lt;element ref="{}format"/>
 *         &lt;element ref="{}check" minOccurs="0"/>
 *         &lt;element ref="{}daten"/>
 *       &lt;/sequence>
 *       &lt;attribute name="location" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="INTERN"/>
 *             &lt;enumeration value="EXTERN"/>
 *             &lt;enumeration value="REMOTE"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="gruppe">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="TITELBILD"/>
 *             &lt;enumeration value="INNENANSICHTEN"/>
 *             &lt;enumeration value="AUSSENANSICHTEN"/>
 *             &lt;enumeration value="GRUNDRISS"/>
 *             &lt;enumeration value="KARTEN_LAGEPLAN"/>
 *             &lt;enumeration value="ANBIETERLOGO"/>
 *             &lt;enumeration value="BILD"/>
 *             &lt;enumeration value="DOKUMENTE"/>
 *             &lt;enumeration value="LINKS"/>
 *             &lt;enumeration value="PANORAMA"/>
 *             &lt;enumeration value="QRCODE"/>
 *             &lt;enumeration value="FILM"/>
 *             &lt;enumeration value="FILMLINK"/>
 *             &lt;enumeration value="EPASS-SKALA"/>
 *             &lt;enumeration value="ANBOBJURL"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "anhangtitel",
        "format",
        "check",
        "daten"
})
@XmlRootElement(name = "anhang")
public class Anhang {

    protected String anhangtitel;

    @XmlElement(required = true)
    protected String format;

    protected Check check;

    @XmlElement(required = true)
    protected Daten daten;

    @XmlAttribute(name = "location", required = true)
    protected String location;

    @XmlAttribute(name = "gruppe")
    protected String gruppe;

    /**
     * Ruft den Wert der anhangtitel-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getAnhangtitel() {
        return anhangtitel;
    }

    /**
     * Legt den Wert der anhangtitel-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setAnhangtitel(String value) {
        this.anhangtitel = value;
    }

    /**
     * Ruft den Wert der format-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getFormat() {
        return format;
    }

    /**
     * Legt den Wert der format-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setFormat(String value) {
        this.format = value;
    }

    /**
     * Ruft den Wert der check-Eigenschaft ab.
     *
     * @return possible object is {@link Check }
     */
    public Check getCheck() {
        return check;
    }

    /**
     * Legt den Wert der check-Eigenschaft fest.
     *
     * @param value allowed object is {@link Check }
     */
    public void setCheck(Check value) {
        this.check = value;
    }

    /**
     * Ruft den Wert der daten-Eigenschaft ab.
     *
     * @return possible object is {@link Daten }
     */
    public Daten getDaten() {
        return daten;
    }

    /**
     * Legt den Wert der daten-Eigenschaft fest.
     *
     * @param value allowed object is {@link Daten }
     */
    public void setDaten(Daten value) {
        this.daten = value;
    }

    /**
     * Ruft den Wert der location-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getLocation() {
        return location;
    }

    /**
     * Legt den Wert der location-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setLocation(String value) {
        this.location = value;
    }

    /**
     * Ruft den Wert der gruppe-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getGruppe() {
        return gruppe;
    }

    /**
     * Legt den Wert der gruppe-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setGruppe(String value) {
        this.gruppe = value;
    }

}
