//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.10.16 um 11:10:09 AM CEST 
//

package de.immomio.openimmo;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;

/**
 * <p>Java-Klasse f�r anonymous complex type.
 * <p>
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>decimal">
 *       &lt;attribute name="periode">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="TAG"/>
 *             &lt;enumeration value="WOCHE"/>
 *             &lt;enumeration value="MONAT"/>
 *             &lt;enumeration value="JAHR"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "value"
})
@XmlRootElement(name = "mieteinnahmen_ist")
public class MieteinnahmenIst {

    @XmlValue
    protected BigDecimal value;

    @XmlAttribute(name = "periode")
    protected String periode;

    /**
     * Ruft den Wert der value-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * Legt den Wert der value-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * Ruft den Wert der periode-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getPeriode() {
        return periode;
    }

    /**
     * Legt den Wert der periode-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setPeriode(String value) {
        this.periode = value;
    }

}
