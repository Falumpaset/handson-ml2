//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.10.16 um 11:10:09 AM CEST 
//

package de.immomio.openimmo;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;

/**
 * <p>Java-Klasse f�r anonymous complex type.
 * <p>
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="art" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="speed" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "breitband_zugang")
public class BreitbandZugang {

    @XmlAttribute(name = "art")
    protected String art;

    @XmlAttribute(name = "speed")
    protected BigDecimal speed;

    /**
     * Ruft den Wert der art-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getArt() {
        return art;
    }

    /**
     * Legt den Wert der art-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setArt(String value) {
        this.art = value;
    }

    /**
     * Ruft den Wert der speed-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getSpeed() {
        return speed;
    }

    /**
     * Legt den Wert der speed-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setSpeed(BigDecimal value) {
        this.speed = value;
    }

}
