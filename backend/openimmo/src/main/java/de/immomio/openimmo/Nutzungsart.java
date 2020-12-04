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
 *       &lt;attribute name="WOHNEN" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="GEWERBE" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="ANLAGE" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="WAZ" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "nutzungsart")
public class Nutzungsart {

    @XmlAttribute(name = "WOHNEN", required = true)
    protected boolean wohnen;

    @XmlAttribute(name = "GEWERBE", required = true)
    protected boolean gewerbe;

    @XmlAttribute(name = "ANLAGE")
    protected Boolean anlage;

    @XmlAttribute(name = "WAZ")
    protected Boolean waz;

    /**
     * Ruft den Wert der wohnen-Eigenschaft ab.
     */
    public boolean isWOHNEN() {
        return wohnen;
    }

    /**
     * Legt den Wert der wohnen-Eigenschaft fest.
     */
    public void setWOHNEN(boolean value) {
        this.wohnen = value;
    }

    /**
     * Ruft den Wert der gewerbe-Eigenschaft ab.
     */
    public boolean isGEWERBE() {
        return gewerbe;
    }

    /**
     * Legt den Wert der gewerbe-Eigenschaft fest.
     */
    public void setGEWERBE(boolean value) {
        this.gewerbe = value;
    }

    /**
     * Ruft den Wert der anlage-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isANLAGE() {
        return anlage;
    }

    /**
     * Legt den Wert der anlage-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setANLAGE(Boolean value) {
        this.anlage = value;
    }

    /**
     * Ruft den Wert der waz-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isWAZ() {
        return waz;
    }

    /**
     * Legt den Wert der waz-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setWAZ(Boolean value) {
        this.waz = value;
    }

}
