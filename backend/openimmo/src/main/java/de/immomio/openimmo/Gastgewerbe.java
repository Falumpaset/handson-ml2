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
 *       &lt;attribute name="gastgew_typ">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="GASTRONOMIE"/>
 *             &lt;enumeration value="GASTRONOMIE_UND_WOHNUNG"/>
 *             &lt;enumeration value="PENSIONEN"/>
 *             &lt;enumeration value="HOTELS"/>
 *             &lt;enumeration value="WEITERE_BEHERBERGUNGSBETRIEBE"/>
 *             &lt;enumeration value="BAR"/>
 *             &lt;enumeration value="CAFE"/>
 *             &lt;enumeration value="DISCOTHEK"/>
 *             &lt;enumeration value="RESTAURANT"/>
 *             &lt;enumeration value="RAUCHERLOKAL"/>
 *             &lt;enumeration value="EINRAUMLOKAL"/>
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
@XmlRootElement(name = "gastgewerbe")
public class Gastgewerbe {

    @XmlAttribute(name = "gastgew_typ")
    protected String gastgewTyp;

    /**
     * Ruft den Wert der gastgewTyp-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getGastgewTyp() {
        return gastgewTyp;
    }

    /**
     * Legt den Wert der gastgewTyp-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setGastgewTyp(String value) {
        this.gastgewTyp = value;
    }

}
