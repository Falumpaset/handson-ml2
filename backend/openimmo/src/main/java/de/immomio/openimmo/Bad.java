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
 *       &lt;attribute name="DUSCHE" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="WANNE" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="FENSTER" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="BIDET" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="PISSOIR" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "bad")
public class Bad {

    @XmlAttribute(name = "DUSCHE")
    protected Boolean dusche;

    @XmlAttribute(name = "WANNE")
    protected Boolean wanne;

    @XmlAttribute(name = "FENSTER")
    protected Boolean fenster;

    @XmlAttribute(name = "BIDET")
    protected Boolean bidet;

    @XmlAttribute(name = "PISSOIR")
    protected Boolean pissoir;

    /**
     * Ruft den Wert der dusche-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isDUSCHE() {
        return dusche;
    }

    /**
     * Legt den Wert der dusche-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setDUSCHE(Boolean value) {
        this.dusche = value;
    }

    /**
     * Ruft den Wert der wanne-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isWANNE() {
        return wanne;
    }

    /**
     * Legt den Wert der wanne-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setWANNE(Boolean value) {
        this.wanne = value;
    }

    /**
     * Ruft den Wert der fenster-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isFENSTER() {
        return fenster;
    }

    /**
     * Legt den Wert der fenster-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setFENSTER(Boolean value) {
        this.fenster = value;
    }

    /**
     * Ruft den Wert der bidet-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isBIDET() {
        return bidet;
    }

    /**
     * Legt den Wert der bidet-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setBIDET(Boolean value) {
        this.bidet = value;
    }

    /**
     * Ruft den Wert der pissoir-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isPISSOIR() {
        return pissoir;
    }

    /**
     * Legt den Wert der pissoir-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setPISSOIR(Boolean value) {
        this.pissoir = value;
    }

}
