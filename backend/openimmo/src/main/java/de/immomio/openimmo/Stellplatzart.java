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
 *       &lt;attribute name="GARAGE" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="TIEFGARAGE" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="CARPORT" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="FREIPLATZ" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="PARKHAUS" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="DUPLEX" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "stellplatzart")
public class Stellplatzart {

    @XmlAttribute(name = "GARAGE")
    protected Boolean garage;

    @XmlAttribute(name = "TIEFGARAGE")
    protected Boolean tiefgarage;

    @XmlAttribute(name = "CARPORT")
    protected Boolean carport;

    @XmlAttribute(name = "FREIPLATZ")
    protected Boolean freiplatz;

    @XmlAttribute(name = "PARKHAUS")
    protected Boolean parkhaus;

    @XmlAttribute(name = "DUPLEX")
    protected Boolean duplex;

    /**
     * Ruft den Wert der garage-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isGARAGE() {
        return garage;
    }

    /**
     * Legt den Wert der garage-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setGARAGE(Boolean value) {
        this.garage = value;
    }

    /**
     * Ruft den Wert der tiefgarage-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isTIEFGARAGE() {
        return tiefgarage;
    }

    /**
     * Legt den Wert der tiefgarage-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setTIEFGARAGE(Boolean value) {
        this.tiefgarage = value;
    }

    /**
     * Ruft den Wert der carport-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isCARPORT() {
        return carport;
    }

    /**
     * Legt den Wert der carport-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setCARPORT(Boolean value) {
        this.carport = value;
    }

    /**
     * Ruft den Wert der freiplatz-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isFREIPLATZ() {
        return freiplatz;
    }

    /**
     * Legt den Wert der freiplatz-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setFREIPLATZ(Boolean value) {
        this.freiplatz = value;
    }

    /**
     * Ruft den Wert der parkhaus-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isPARKHAUS() {
        return parkhaus;
    }

    /**
     * Legt den Wert der parkhaus-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setPARKHAUS(Boolean value) {
        this.parkhaus = value;
    }

    /**
     * Ruft den Wert der duplex-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isDUPLEX() {
        return duplex;
    }

    /**
     * Legt den Wert der duplex-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setDUPLEX(Boolean value) {
        this.duplex = value;
    }

}
