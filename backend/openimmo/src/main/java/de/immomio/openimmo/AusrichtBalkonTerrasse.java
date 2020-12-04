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
 *       &lt;attribute name="NORD" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="OST" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="SUED" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="WEST" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="NORDOST" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="NORDWEST" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="SUEDOST" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="SUEDWEST" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "ausricht_balkon_terrasse")
public class AusrichtBalkonTerrasse {

    @XmlAttribute(name = "NORD")
    protected Boolean nord;

    @XmlAttribute(name = "OST")
    protected Boolean ost;

    @XmlAttribute(name = "SUED")
    protected Boolean sued;

    @XmlAttribute(name = "WEST")
    protected Boolean west;

    @XmlAttribute(name = "NORDOST")
    protected Boolean nordost;

    @XmlAttribute(name = "NORDWEST")
    protected Boolean nordwest;

    @XmlAttribute(name = "SUEDOST")
    protected Boolean suedost;

    @XmlAttribute(name = "SUEDWEST")
    protected Boolean suedwest;

    /**
     * Ruft den Wert der nord-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isNORD() {
        return nord;
    }

    /**
     * Legt den Wert der nord-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setNORD(Boolean value) {
        this.nord = value;
    }

    /**
     * Ruft den Wert der ost-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isOST() {
        return ost;
    }

    /**
     * Legt den Wert der ost-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setOST(Boolean value) {
        this.ost = value;
    }

    /**
     * Ruft den Wert der sued-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isSUED() {
        return sued;
    }

    /**
     * Legt den Wert der sued-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setSUED(Boolean value) {
        this.sued = value;
    }

    /**
     * Ruft den Wert der west-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isWEST() {
        return west;
    }

    /**
     * Legt den Wert der west-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setWEST(Boolean value) {
        this.west = value;
    }

    /**
     * Ruft den Wert der nordost-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isNORDOST() {
        return nordost;
    }

    /**
     * Legt den Wert der nordost-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setNORDOST(Boolean value) {
        this.nordost = value;
    }

    /**
     * Ruft den Wert der nordwest-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isNORDWEST() {
        return nordwest;
    }

    /**
     * Legt den Wert der nordwest-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setNORDWEST(Boolean value) {
        this.nordwest = value;
    }

    /**
     * Ruft den Wert der suedost-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isSUEDOST() {
        return suedost;
    }

    /**
     * Legt den Wert der suedost-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setSUEDOST(Boolean value) {
        this.suedost = value;
    }

    /**
     * Ruft den Wert der suedwest-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isSUEDWEST() {
        return suedwest;
    }

    /**
     * Legt den Wert der suedwest-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setSUEDWEST(Boolean value) {
        this.suedwest = value;
    }

}
