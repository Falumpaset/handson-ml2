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
 *       &lt;attribute name="land_typ">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="LANDWIRTSCHAFTLICHE_BETRIEBE"/>
 *             &lt;enumeration value="BAUERNHOF"/>
 *             &lt;enumeration value="AUSSIEDLERHOF"/>
 *             &lt;enumeration value="GARTENBAU"/>
 *             &lt;enumeration value="ACKERBAU"/>
 *             &lt;enumeration value="WEINBAU"/>
 *             &lt;enumeration value="VIEHWIRTSCHAFT"/>
 *             &lt;enumeration value="JAGD_UND_FORSTWIRTSCHAFT"/>
 *             &lt;enumeration value="TEICH_UND_FISCHWIRTSCHAFT"/>
 *             &lt;enumeration value="SCHEUNEN"/>
 *             &lt;enumeration value="REITERHOEFE"/>
 *             &lt;enumeration value="SONSTIGE_LANDWIRTSCHAFTSIMMOBILIEN"/>
 *             &lt;enumeration value="ANWESEN"/>
 *             &lt;enumeration value="JAGDREVIER"/>
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
@XmlRootElement(name = "land_und_forstwirtschaft")
public class LandUndForstwirtschaft {

    @XmlAttribute(name = "land_typ")
    protected String landTyp;

    /**
     * Ruft den Wert der landTyp-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getLandTyp() {
        return landTyp;
    }

    /**
     * Legt den Wert der landTyp-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setLandTyp(String value) {
        this.landTyp = value;
    }

}
