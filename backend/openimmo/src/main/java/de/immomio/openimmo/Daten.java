//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.10.16 um 11:10:09 AM CEST 
//

package de.immomio.openimmo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java-Klasse f�r anonymous complex type.
 * <p>
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element ref="{}pfad" minOccurs="0"/>
 *         &lt;element ref="{}anhanginhalt" minOccurs="0"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "pfad",
        "anhanginhalt"
})
@XmlRootElement(name = "daten")
public class Daten {

    protected String pfad;

    protected byte[] anhanginhalt;

    /**
     * Ruft den Wert der pfad-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getPfad() {
        return pfad;
    }

    /**
     * Legt den Wert der pfad-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setPfad(String value) {
        this.pfad = value;
    }

    /**
     * Ruft den Wert der anhanginhalt-Eigenschaft ab.
     *
     * @return possible object is byte[]
     */
    public byte[] getAnhanginhalt() {
        return anhanginhalt;
    }

    /**
     * Legt den Wert der anhanginhalt-Eigenschaft fest.
     *
     * @param value allowed object is byte[]
     */
    public void setAnhanginhalt(byte[] value) {
        this.anhanginhalt = value;
    }

}
