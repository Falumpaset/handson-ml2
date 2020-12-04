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
 *       &lt;attribute name="BETREUTES_WOHNEN" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="CATERING" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="REINIGUNG" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="EINKAUF" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="WACHDIENST" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "serviceleistungen")
public class Serviceleistungen {

    @XmlAttribute(name = "BETREUTES_WOHNEN")
    protected Boolean betreuteswohnen;

    @XmlAttribute(name = "CATERING")
    protected Boolean catering;

    @XmlAttribute(name = "REINIGUNG")
    protected Boolean reinigung;

    @XmlAttribute(name = "EINKAUF")
    protected Boolean einkauf;

    @XmlAttribute(name = "WACHDIENST")
    protected Boolean wachdienst;

    /**
     * Ruft den Wert der betreuteswohnen-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isBETREUTESWOHNEN() {
        return betreuteswohnen;
    }

    /**
     * Legt den Wert der betreuteswohnen-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setBETREUTESWOHNEN(Boolean value) {
        this.betreuteswohnen = value;
    }

    /**
     * Ruft den Wert der catering-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isCATERING() {
        return catering;
    }

    /**
     * Legt den Wert der catering-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setCATERING(Boolean value) {
        this.catering = value;
    }

    /**
     * Ruft den Wert der reinigung-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isREINIGUNG() {
        return reinigung;
    }

    /**
     * Legt den Wert der reinigung-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setREINIGUNG(Boolean value) {
        this.reinigung = value;
    }

    /**
     * Ruft den Wert der einkauf-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isEINKAUF() {
        return einkauf;
    }

    /**
     * Legt den Wert der einkauf-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setEINKAUF(Boolean value) {
        this.einkauf = value;
    }

    /**
     * Ruft den Wert der wachdienst-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isWACHDIENST() {
        return wachdienst;
    }

    /**
     * Legt den Wert der wachdienst-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setWACHDIENST(Boolean value) {
        this.wachdienst = value;
    }

}
