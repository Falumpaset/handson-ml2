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
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;>kontakt">
 *       &lt;attribute name="telefonart">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="TEL_ZENTRALE"/>
 *             &lt;enumeration value="TEL_DURCHW"/>
 *             &lt;enumeration value="TEL_PRIVAT"/>
 *             &lt;enumeration value="TEL_HANDY"/>
 *             &lt;enumeration value="TEL_FAX"/>
 *             &lt;enumeration value="TEL_SONSTIGE"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="bemerkung" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "value"
})
@XmlRootElement(name = "tel_sonstige")
public class TelSonstige {

    @XmlValue
    protected String value;

    @XmlAttribute(name = "telefonart")
    protected String telefonart;

    @XmlAttribute(name = "bemerkung")
    protected String bemerkung;

    /**
     * Ruft den Wert der value-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getValue() {
        return value;
    }

    /**
     * Legt den Wert der value-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Ruft den Wert der telefonart-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getTelefonart() {
        return telefonart;
    }

    /**
     * Legt den Wert der telefonart-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setTelefonart(String value) {
        this.telefonart = value;
    }

    /**
     * Ruft den Wert der bemerkung-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getBemerkung() {
        return bemerkung;
    }

    /**
     * Legt den Wert der bemerkung-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setBemerkung(String value) {
        this.bemerkung = value;
    }

}
