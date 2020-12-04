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
 *       &lt;attribute name="breitengrad" use="required" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="laengengrad" use="required" type="{http://www.w3.org/2001/XMLSchema}float" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "geokoordinaten")
public class Geokoordinaten {

    @XmlAttribute(name = "breitengrad", required = true)
    protected float breitengrad;

    @XmlAttribute(name = "laengengrad", required = true)
    protected float laengengrad;

    /**
     * Ruft den Wert der breitengrad-Eigenschaft ab.
     */
    public float getBreitengrad() {
        return breitengrad;
    }

    /**
     * Legt den Wert der breitengrad-Eigenschaft fest.
     */
    public void setBreitengrad(float value) {
        this.breitengrad = value;
    }

    /**
     * Ruft den Wert der laengengrad-Eigenschaft ab.
     */
    public float getLaengengrad() {
        return laengengrad;
    }

    /**
     * Legt den Wert der laengengrad-Eigenschaft fest.
     */
    public void setLaengengrad(float value) {
        this.laengengrad = value;
    }

}
