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
 *       &lt;attribute name="PASSIVHAUS" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="NIEDRIGENERGIE" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="NEUBAUSTANDARD" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="KFW40" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="KFW60" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="KFW55" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="KFW70" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="MINERGIEBAUWEISE" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="MINERGIE_ZERTIFIZIERT" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "energietyp")
public class Energietyp {

    @XmlAttribute(name = "PASSIVHAUS")
    protected Boolean passivhaus;

    @XmlAttribute(name = "NIEDRIGENERGIE")
    protected Boolean niedrigenergie;

    @XmlAttribute(name = "NEUBAUSTANDARD")
    protected Boolean neubaustandard;

    @XmlAttribute(name = "KFW40")
    protected Boolean kfw40;

    @XmlAttribute(name = "KFW60")
    protected Boolean kfw60;

    @XmlAttribute(name = "KFW55")
    protected Boolean kfw55;

    @XmlAttribute(name = "KFW70")
    protected Boolean kfw70;

    @XmlAttribute(name = "MINERGIEBAUWEISE")
    protected Boolean minergiebauweise;

    @XmlAttribute(name = "MINERGIE_ZERTIFIZIERT")
    protected Boolean minergiezertifiziert;

    /**
     * Ruft den Wert der passivhaus-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isPASSIVHAUS() {
        return passivhaus;
    }

    /**
     * Legt den Wert der passivhaus-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setPASSIVHAUS(Boolean value) {
        this.passivhaus = value;
    }

    /**
     * Ruft den Wert der niedrigenergie-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isNIEDRIGENERGIE() {
        return niedrigenergie;
    }

    /**
     * Legt den Wert der niedrigenergie-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setNIEDRIGENERGIE(Boolean value) {
        this.niedrigenergie = value;
    }

    /**
     * Ruft den Wert der neubaustandard-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isNEUBAUSTANDARD() {
        return neubaustandard;
    }

    /**
     * Legt den Wert der neubaustandard-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setNEUBAUSTANDARD(Boolean value) {
        this.neubaustandard = value;
    }

    /**
     * Ruft den Wert der kfw40-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isKFW40() {
        return kfw40;
    }

    /**
     * Legt den Wert der kfw40-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setKFW40(Boolean value) {
        this.kfw40 = value;
    }

    /**
     * Ruft den Wert der kfw60-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isKFW60() {
        return kfw60;
    }

    /**
     * Legt den Wert der kfw60-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setKFW60(Boolean value) {
        this.kfw60 = value;
    }

    /**
     * Ruft den Wert der kfw55-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isKFW55() {
        return kfw55;
    }

    /**
     * Legt den Wert der kfw55-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setKFW55(Boolean value) {
        this.kfw55 = value;
    }

    /**
     * Ruft den Wert der kfw70-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isKFW70() {
        return kfw70;
    }

    /**
     * Legt den Wert der kfw70-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setKFW70(Boolean value) {
        this.kfw70 = value;
    }

    /**
     * Ruft den Wert der minergiebauweise-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isMINERGIEBAUWEISE() {
        return minergiebauweise;
    }

    /**
     * Legt den Wert der minergiebauweise-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setMINERGIEBAUWEISE(Boolean value) {
        this.minergiebauweise = value;
    }

    /**
     * Ruft den Wert der minergiezertifiziert-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isMINERGIEZERTIFIZIERT() {
        return minergiezertifiziert;
    }

    /**
     * Legt den Wert der minergiezertifiziert-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setMINERGIEZERTIFIZIERT(Boolean value) {
        this.minergiezertifiziert = value;
    }

}
