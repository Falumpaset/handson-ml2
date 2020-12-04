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
 *       &lt;attribute name="grundst_typ">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="WOHNEN"/>
 *             &lt;enumeration value="GEWERBE"/>
 *             &lt;enumeration value="INDUSTRIE"/>
 *             &lt;enumeration value="LAND_FORSTWIRSCHAFT"/>
 *             &lt;enumeration value="FREIZEIT"/>
 *             &lt;enumeration value="GEMISCHT"/>
 *             &lt;enumeration value="GEWERBEPARK"/>
 *             &lt;enumeration value="SONDERNUTZUNG"/>
 *             &lt;enumeration value="SEELIEGENSCHAFT"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "grundstueck")
public class Grundstueck {

    @XmlAttribute(name = "grundst_typ")
    protected String grundstTyp;

    /**
     * Ruft den Wert der grundstTyp-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getGrundstTyp() {
        return grundstTyp;
    }

    /**
     * Legt den Wert der grundstTyp-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setGrundstTyp(String value) {
        this.grundstTyp = value;
    }

}
