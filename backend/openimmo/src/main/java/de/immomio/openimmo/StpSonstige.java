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
 *     &lt;extension base="{}stellplatz">
 *       &lt;attribute name="platzart">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="FREIPLATZ"/>
 *             &lt;enumeration value="GARAGE"/>
 *             &lt;enumeration value="TIEFGARAGE"/>
 *             &lt;enumeration value="CARPORT"/>
 *             &lt;enumeration value="DUPLEX"/>
 *             &lt;enumeration value="PARKHAUS"/>
 *             &lt;enumeration value="SONSTIGES"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="bemerkung" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "stp_sonstige")
public class StpSonstige
        extends Stellplatz {

    @XmlAttribute(name = "platzart")
    protected String platzart;

    @XmlAttribute(name = "bemerkung")
    protected String bemerkung;

    /**
     * Ruft den Wert der platzart-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getPlatzart() {
        return platzart;
    }

    /**
     * Legt den Wert der platzart-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setPlatzart(String value) {
        this.platzart = value;
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
