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
 *       &lt;attribute name="sonstige_typ">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="PARKHAUS"/>
 *             &lt;enumeration value="TANKSTELLE"/>
 *             &lt;enumeration value="KRANKENHAUS"/>
 *             &lt;enumeration value="SONSTIGE"/>
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
@XmlRootElement(name = "sonstige")
public class Sonstige {

    @XmlAttribute(name = "sonstige_typ")
    protected String sonstigeTyp;

    /**
     * Ruft den Wert der sonstigeTyp-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getSonstigeTyp() {
        return sonstigeTyp;
    }

    /**
     * Legt den Wert der sonstigeTyp-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setSonstigeTyp(String value) {
        this.sonstigeTyp = value;
    }

}
