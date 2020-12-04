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
 *       &lt;attribute name="hallen_typ">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="HALLE"/>
 *             &lt;enumeration value="INDUSTRIEHALLE"/>
 *             &lt;enumeration value="LAGER"/>
 *             &lt;enumeration value="LAGERFLAECHEN"/>
 *             &lt;enumeration value="LAGER_MIT_FREIFLAECHE"/>
 *             &lt;enumeration value="HOCHREGALLAGER"/>
 *             &lt;enumeration value="SPEDITIONSLAGER"/>
 *             &lt;enumeration value="PRODUKTION"/>
 *             &lt;enumeration value="WERKSTATT"/>
 *             &lt;enumeration value="SERVICE"/>
 *             &lt;enumeration value="FREIFLAECHEN"/>
 *             &lt;enumeration value="KUEHLHAUS"/>
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
@XmlRootElement(name = "hallen_lager_prod")
public class HallenLagerProd {

    @XmlAttribute(name = "hallen_typ")
    protected String hallenTyp;

    /**
     * Ruft den Wert der hallenTyp-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getHallenTyp() {
        return hallenTyp;
    }

    /**
     * Legt den Wert der hallenTyp-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setHallenTyp(String value) {
        this.hallenTyp = value;
    }

}
