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
 *       &lt;attribute name="wohnungtyp">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="DACHGESCHOSS"/>
 *             &lt;enumeration value="MAISONETTE"/>
 *             &lt;enumeration value="LOFT-STUDIO-ATELIER"/>
 *             &lt;enumeration value="PENTHOUSE"/>
 *             &lt;enumeration value="TERRASSEN"/>
 *             &lt;enumeration value="ETAGE"/>
 *             &lt;enumeration value="ERDGESCHOSS"/>
 *             &lt;enumeration value="SOUTERRAIN"/>
 *             &lt;enumeration value="APARTMENT"/>
 *             &lt;enumeration value="FERIENWOHNUNG"/>
 *             &lt;enumeration value="GALERIE"/>
 *             &lt;enumeration value="ROHDACHBODEN"/>
 *             &lt;enumeration value="ATTIKAWOHNUNG"/>
 *             &lt;enumeration value="KEINE_ANGABE"/>
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
@XmlRootElement(name = "wohnung")
public class Wohnung {

    @XmlAttribute(name = "wohnungtyp")
    protected String wohnungtyp;

    /**
     * Ruft den Wert der wohnungtyp-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getWohnungtyp() {
        return wohnungtyp;
    }

    /**
     * Legt den Wert der wohnungtyp-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setWohnungtyp(String value) {
        this.wohnungtyp = value;
    }

}
