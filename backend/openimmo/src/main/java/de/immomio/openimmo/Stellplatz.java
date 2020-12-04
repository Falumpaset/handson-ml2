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
 * <p>Java-Klasse f�r stellplatz complex type.
 * <p>
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;complexType name="stellplatz">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="stellplatzmiete" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="stellplatzkaufpreis" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="anzahl" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "stellplatz")
@XmlSeeAlso({
        StpSonstige.class
})
public class Stellplatz {

    @XmlAttribute(name = "stellplatzmiete")
    protected BigDecimal stellplatzmiete;

    @XmlAttribute(name = "stellplatzkaufpreis")
    protected BigDecimal stellplatzkaufpreis;

    @XmlAttribute(name = "anzahl")
    protected Integer anzahl;

    /**
     * Ruft den Wert der stellplatzmiete-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getStellplatzmiete() {
        return stellplatzmiete;
    }

    /**
     * Legt den Wert der stellplatzmiete-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setStellplatzmiete(BigDecimal value) {
        this.stellplatzmiete = value;
    }

    /**
     * Ruft den Wert der stellplatzkaufpreis-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getStellplatzkaufpreis() {
        return stellplatzkaufpreis;
    }

    /**
     * Legt den Wert der stellplatzkaufpreis-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setStellplatzkaufpreis(BigDecimal value) {
        this.stellplatzkaufpreis = value;
    }

    /**
     * Ruft den Wert der anzahl-Eigenschaft ab.
     *
     * @return possible object is {@link Integer }
     */
    public Integer getAnzahl() {
        return anzahl;
    }

    /**
     * Legt den Wert der anzahl-Eigenschaft fest.
     *
     * @param value allowed object is {@link Integer }
     */
    public void setAnzahl(Integer value) {
        this.anzahl = value;
    }

}
