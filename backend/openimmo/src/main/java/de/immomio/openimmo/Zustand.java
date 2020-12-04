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
 *       &lt;attribute name="zustand_art">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="ERSTBEZUG"/>
 *             &lt;enumeration value="TEIL_VOLLRENOVIERUNGSBED"/>
 *             &lt;enumeration value="NEUWERTIG"/>
 *             &lt;enumeration value="TEIL_VOLLSANIERT"/>
 *             &lt;enumeration value="TEIL_VOLLRENOVIERT"/>
 *             &lt;enumeration value="TEIL_SANIERT"/>
 *             &lt;enumeration value="VOLL_SANIERT"/>
 *             &lt;enumeration value="SANIERUNGSBEDUERFTIG"/>
 *             &lt;enumeration value="BAUFAELLIG"/>
 *             &lt;enumeration value="NACH_VEREINBARUNG"/>
 *             &lt;enumeration value="MODERNISIERT"/>
 *             &lt;enumeration value="GEPFLEGT"/>
 *             &lt;enumeration value="ROHBAU"/>
 *             &lt;enumeration value="ENTKERNT"/>
 *             &lt;enumeration value="ABRISSOBJEKT"/>
 *             &lt;enumeration value="PROJEKTIERT"/>
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
@XmlRootElement(name = "zustand")
public class Zustand {

    @XmlAttribute(name = "zustand_art")
    protected String zustandArt;

    /**
     * Ruft den Wert der zustandArt-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getZustandArt() {
        return zustandArt;
    }

    /**
     * Legt den Wert der zustandArt-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setZustandArt(String value) {
        this.zustandArt = value;
    }

}
