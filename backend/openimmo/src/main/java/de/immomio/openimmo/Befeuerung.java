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
 *       &lt;attribute name="OEL" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="GAS" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="ELEKTRO" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="ALTERNATIV" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="SOLAR" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="ERDWAERME" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="LUFTWP" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="FERN" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="BLOCK" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="WASSER-ELEKTRO" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="PELLET" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="KOHLE" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="HOLZ" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="FLUESSIGGAS" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "befeuerung")
public class Befeuerung {

    @XmlAttribute(name = "OEL")
    protected Boolean oel;

    @XmlAttribute(name = "GAS")
    protected Boolean gas;

    @XmlAttribute(name = "ELEKTRO")
    protected Boolean elektro;

    @XmlAttribute(name = "ALTERNATIV")
    protected Boolean alternativ;

    @XmlAttribute(name = "SOLAR")
    protected Boolean solar;

    @XmlAttribute(name = "ERDWAERME")
    protected Boolean erdwaerme;

    @XmlAttribute(name = "LUFTWP")
    protected Boolean luftwp;

    @XmlAttribute(name = "FERN")
    protected Boolean fern;

    @XmlAttribute(name = "BLOCK")
    protected Boolean block;

    @XmlAttribute(name = "WASSER-ELEKTRO")
    protected Boolean wasserelektro;

    @XmlAttribute(name = "PELLET")
    protected Boolean pellet;

    @XmlAttribute(name = "KOHLE")
    protected Boolean kohle;

    @XmlAttribute(name = "HOLZ")
    protected Boolean holz;

    @XmlAttribute(name = "FLUESSIGGAS")
    protected Boolean fluessiggas;

    /**
     * Ruft den Wert der oel-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isOEL() {
        return oel;
    }

    /**
     * Legt den Wert der oel-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setOEL(Boolean value) {
        this.oel = value;
    }

    /**
     * Ruft den Wert der gas-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isGAS() {
        return gas;
    }

    /**
     * Legt den Wert der gas-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setGAS(Boolean value) {
        this.gas = value;
    }

    /**
     * Ruft den Wert der elektro-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isELEKTRO() {
        return elektro;
    }

    /**
     * Legt den Wert der elektro-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setELEKTRO(Boolean value) {
        this.elektro = value;
    }

    /**
     * Ruft den Wert der alternativ-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isALTERNATIV() {
        return alternativ;
    }

    /**
     * Legt den Wert der alternativ-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setALTERNATIV(Boolean value) {
        this.alternativ = value;
    }

    /**
     * Ruft den Wert der solar-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isSOLAR() {
        return solar;
    }

    /**
     * Legt den Wert der solar-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setSOLAR(Boolean value) {
        this.solar = value;
    }

    /**
     * Ruft den Wert der erdwaerme-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isERDWAERME() {
        return erdwaerme;
    }

    /**
     * Legt den Wert der erdwaerme-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setERDWAERME(Boolean value) {
        this.erdwaerme = value;
    }

    /**
     * Ruft den Wert der luftwp-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isLUFTWP() {
        return luftwp;
    }

    /**
     * Legt den Wert der luftwp-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setLUFTWP(Boolean value) {
        this.luftwp = value;
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
     * Ruft den Wert der block-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isBLOCK() {
        return block;
    }

    /**
     * Legt den Wert der block-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setBLOCK(Boolean value) {
        this.block = value;
    }

    /**
     * Ruft den Wert der wasserelektro-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isWASSERELEKTRO() {
        return wasserelektro;
    }

    /**
     * Legt den Wert der wasserelektro-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setWASSERELEKTRO(Boolean value) {
        this.wasserelektro = value;
    }

    /**
     * Ruft den Wert der pellet-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isPELLET() {
        return pellet;
    }

    /**
     * Legt den Wert der pellet-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setPELLET(Boolean value) {
        this.pellet = value;
    }

    /**
     * Ruft den Wert der kohle-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isKOHLE() {
        return kohle;
    }

    /**
     * Legt den Wert der kohle-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setKOHLE(Boolean value) {
        this.kohle = value;
    }

    /**
     * Ruft den Wert der holz-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isHOLZ() {
        return holz;
    }

    /**
     * Legt den Wert der holz-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setHOLZ(Boolean value) {
        this.holz = value;
    }

    /**
     * Ruft den Wert der fluessiggas-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isFLUESSIGGAS() {
        return fluessiggas;
    }

    /**
     * Legt den Wert der fluessiggas-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setFLUESSIGGAS(Boolean value) {
        this.fluessiggas = value;
    }

}
