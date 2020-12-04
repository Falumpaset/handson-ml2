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
 *       &lt;attribute name="OFEN" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="ETAGE" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="ZENTRAL" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="FERN" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="FUSSBODEN" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "heizungsart")
public class Heizungsart {

    @XmlAttribute(name = "OFEN")
    protected Boolean ofen;

    @XmlAttribute(name = "ETAGE")
    protected Boolean etage;

    @XmlAttribute(name = "ZENTRAL")
    protected Boolean zentral;

    @XmlAttribute(name = "FERN")
    protected Boolean fern;

    @XmlAttribute(name = "FUSSBODEN")
    protected Boolean fussboden;

    /**
     * Ruft den Wert der ofen-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isOFEN() {
        return ofen;
    }

    /**
     * Legt den Wert der ofen-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setOFEN(Boolean value) {
        this.ofen = value;
    }

    /**
     * Ruft den Wert der etage-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isETAGE() {
        return etage;
    }

    /**
     * Legt den Wert der etage-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setETAGE(Boolean value) {
        this.etage = value;
    }

    /**
     * Ruft den Wert der zentral-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isZENTRAL() {
        return zentral;
    }

    /**
     * Legt den Wert der zentral-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setZENTRAL(Boolean value) {
        this.zentral = value;
    }

    /**
     * Ruft den Wert der fern-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isFERN() {
        return fern;
    }

    /**
     * Legt den Wert der fern-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setFERN(Boolean value) {
        this.fern = value;
    }

    /**
     * Ruft den Wert der fussboden-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isFUSSBODEN() {
        return fussboden;
    }

    /**
     * Legt den Wert der fussboden-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setFUSSBODEN(Boolean value) {
        this.fussboden = value;
    }

}
