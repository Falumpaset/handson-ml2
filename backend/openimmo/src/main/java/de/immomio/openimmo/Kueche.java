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
 *       &lt;attribute name="EBK" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="OFFEN" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="PANTRY" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "kueche")
public class Kueche {

    @XmlAttribute(name = "EBK")
    protected Boolean ebk;

    @XmlAttribute(name = "OFFEN")
    protected Boolean offen;

    @XmlAttribute(name = "PANTRY")
    protected Boolean pantry;

    /**
     * Ruft den Wert der ebk-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isEBK() {
        return ebk;
    }

    /**
     * Legt den Wert der ebk-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setEBK(Boolean value) {
        this.ebk = value;
    }

    /**
     * Ruft den Wert der offen-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isOFFEN() {
        return offen;
    }

    /**
     * Legt den Wert der offen-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setOFFEN(Boolean value) {
        this.offen = value;
    }

    /**
     * Ruft den Wert der pantry-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isPANTRY() {
        return pantry;
    }

    /**
     * Legt den Wert der pantry-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setPANTRY(Boolean value) {
        this.pantry = value;
    }

}
