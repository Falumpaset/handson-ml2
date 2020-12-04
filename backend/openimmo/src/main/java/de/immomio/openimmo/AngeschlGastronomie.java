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
 *       &lt;attribute name="HOTELRESTAURANT" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="BAR" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "angeschl_gastronomie")
public class AngeschlGastronomie {

    @XmlAttribute(name = "HOTELRESTAURANT")
    protected Boolean hotelrestaurant;

    @XmlAttribute(name = "BAR")
    protected Boolean bar;

    /**
     * Ruft den Wert der hotelrestaurant-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isHOTELRESTAURANT() {
        return hotelrestaurant;
    }

    /**
     * Legt den Wert der hotelrestaurant-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setHOTELRESTAURANT(Boolean value) {
        this.hotelrestaurant = value;
    }

    /**
     * Ruft den Wert der bar-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isBAR() {
        return bar;
    }

    /**
     * Legt den Wert der bar-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setBAR(Boolean value) {
        this.bar = value;
    }

}
