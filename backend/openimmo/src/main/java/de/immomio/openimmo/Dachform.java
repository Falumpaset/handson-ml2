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
 *       &lt;attribute name="KRUEPPELWALMDACH" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="MANSARDDACH" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="PULTDACH" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="SATTELDACH" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="WALMDACH" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="FLACHDACH" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="PYRAMIDENDACH" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "dachform")
public class Dachform {

    @XmlAttribute(name = "KRUEPPELWALMDACH")
    protected Boolean krueppelwalmdach;

    @XmlAttribute(name = "MANSARDDACH")
    protected Boolean mansarddach;

    @XmlAttribute(name = "PULTDACH")
    protected Boolean pultdach;

    @XmlAttribute(name = "SATTELDACH")
    protected Boolean satteldach;

    @XmlAttribute(name = "WALMDACH")
    protected Boolean walmdach;

    @XmlAttribute(name = "FLACHDACH")
    protected Boolean flachdach;

    @XmlAttribute(name = "PYRAMIDENDACH")
    protected Boolean pyramidendach;

    /**
     * Ruft den Wert der krueppelwalmdach-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isKRUEPPELWALMDACH() {
        return krueppelwalmdach;
    }

    /**
     * Legt den Wert der krueppelwalmdach-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setKRUEPPELWALMDACH(Boolean value) {
        this.krueppelwalmdach = value;
    }

    /**
     * Ruft den Wert der mansarddach-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isMANSARDDACH() {
        return mansarddach;
    }

    /**
     * Legt den Wert der mansarddach-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setMANSARDDACH(Boolean value) {
        this.mansarddach = value;
    }

    /**
     * Ruft den Wert der pultdach-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isPULTDACH() {
        return pultdach;
    }

    /**
     * Legt den Wert der pultdach-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setPULTDACH(Boolean value) {
        this.pultdach = value;
    }

    /**
     * Ruft den Wert der satteldach-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isSATTELDACH() {
        return satteldach;
    }

    /**
     * Legt den Wert der satteldach-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setSATTELDACH(Boolean value) {
        this.satteldach = value;
    }

    /**
     * Ruft den Wert der walmdach-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isWALMDACH() {
        return walmdach;
    }

    /**
     * Legt den Wert der walmdach-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setWALMDACH(Boolean value) {
        this.walmdach = value;
    }

    /**
     * Ruft den Wert der flachdach-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isFLACHDACH() {
        return flachdach;
    }

    /**
     * Legt den Wert der flachdach-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setFLACHDACH(Boolean value) {
        this.flachdach = value;
    }

    /**
     * Ruft den Wert der pyramidendach-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isPYRAMIDENDACH() {
        return pyramidendach;
    }

    /**
     * Legt den Wert der pyramidendach-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setPYRAMIDENDACH(Boolean value) {
        this.pyramidendach = value;
    }

}
