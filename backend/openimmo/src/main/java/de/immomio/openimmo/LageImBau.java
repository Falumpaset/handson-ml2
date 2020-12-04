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
 *       &lt;attribute name="LINKS" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="RECHTS" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="VORNE" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="HINTEN" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "lage_im_bau")
public class LageImBau {

    @XmlAttribute(name = "LINKS")
    protected Boolean links;

    @XmlAttribute(name = "RECHTS")
    protected Boolean rechts;

    @XmlAttribute(name = "VORNE")
    protected Boolean vorne;

    @XmlAttribute(name = "HINTEN")
    protected Boolean hinten;

    /**
     * Ruft den Wert der links-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isLINKS() {
        return links;
    }

    /**
     * Legt den Wert der links-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setLINKS(Boolean value) {
        this.links = value;
    }

    /**
     * Ruft den Wert der rechts-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isRECHTS() {
        return rechts;
    }

    /**
     * Legt den Wert der rechts-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setRECHTS(Boolean value) {
        this.rechts = value;
    }

    /**
     * Ruft den Wert der vorne-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isVORNE() {
        return vorne;
    }

    /**
     * Legt den Wert der vorne-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setVORNE(Boolean value) {
        this.vorne = value;
    }

    /**
     * Ruft den Wert der hinten-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isHINTEN() {
        return hinten;
    }

    /**
     * Legt den Wert der hinten-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setHINTEN(Boolean value) {
        this.hinten = value;
    }

}
