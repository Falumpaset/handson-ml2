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
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>float">
 *       &lt;attribute name="distanz_zu" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="FLUGHAFEN"/>
 *             &lt;enumeration value="FERNBAHNHOF"/>
 *             &lt;enumeration value="AUTOBAHN"/>
 *             &lt;enumeration value="US_BAHN"/>
 *             &lt;enumeration value="BUS"/>
 *             &lt;enumeration value="KINDERGAERTEN"/>
 *             &lt;enumeration value="GRUNDSCHULE"/>
 *             &lt;enumeration value="HAUPTSCHULE"/>
 *             &lt;enumeration value="REALSCHULE"/>
 *             &lt;enumeration value="GESAMTSCHULE"/>
 *             &lt;enumeration value="GYMNASIUM"/>
 *             &lt;enumeration value="ZENTRUM"/>
 *             &lt;enumeration value="EINKAUFSMOEGLICHKEITEN"/>
 *             &lt;enumeration value="GASTSTAETTEN"/>
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
@XmlRootElement(name = "distanzen")
public class Distanzen {

    @XmlValue
    protected float value;

    @XmlAttribute(name = "distanz_zu", required = true)
    protected String distanzZu;

    /**
     * Ruft den Wert der value-Eigenschaft ab.
     */
    public float getValue() {
        return value;
    }

    /**
     * Legt den Wert der value-Eigenschaft fest.
     */
    public void setValue(float value) {
        this.value = value;
    }

    /**
     * Ruft den Wert der distanzZu-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getDistanzZu() {
        return distanzZu;
    }

    /**
     * Legt den Wert der distanzZu-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setDistanzZu(String value) {
        this.distanzZu = value;
    }

}
