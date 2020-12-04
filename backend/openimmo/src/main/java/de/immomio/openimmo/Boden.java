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
 *       &lt;attribute name="FLIESEN" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="STEIN" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="TEPPICH" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="PARKETT" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="FERTIGPARKETT" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="LAMINAT" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="DIELEN" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="KUNSTSTOFF" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="ESTRICH" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="DOPPELBODEN" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="LINOLEUM" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="MARMOR" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="TERRAKOTTA" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="GRANIT" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "boden")
public class Boden {

    @XmlAttribute(name = "FLIESEN")
    protected Boolean fliesen;

    @XmlAttribute(name = "STEIN")
    protected Boolean stein;

    @XmlAttribute(name = "TEPPICH")
    protected Boolean teppich;

    @XmlAttribute(name = "PARKETT")
    protected Boolean parkett;

    @XmlAttribute(name = "FERTIGPARKETT")
    protected Boolean fertigparkett;

    @XmlAttribute(name = "LAMINAT")
    protected Boolean laminat;

    @XmlAttribute(name = "DIELEN")
    protected Boolean dielen;

    @XmlAttribute(name = "KUNSTSTOFF")
    protected Boolean kunststoff;

    @XmlAttribute(name = "ESTRICH")
    protected Boolean estrich;

    @XmlAttribute(name = "DOPPELBODEN")
    protected Boolean doppelboden;

    @XmlAttribute(name = "LINOLEUM")
    protected Boolean linoleum;

    @XmlAttribute(name = "MARMOR")
    protected Boolean marmor;

    @XmlAttribute(name = "TERRAKOTTA")
    protected Boolean terrakotta;

    @XmlAttribute(name = "GRANIT")
    protected Boolean granit;

    /**
     * Ruft den Wert der fliesen-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isFLIESEN() {
        return fliesen;
    }

    /**
     * Legt den Wert der fliesen-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setFLIESEN(Boolean value) {
        this.fliesen = value;
    }

    /**
     * Ruft den Wert der stein-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isSTEIN() {
        return stein;
    }

    /**
     * Legt den Wert der stein-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setSTEIN(Boolean value) {
        this.stein = value;
    }

    /**
     * Ruft den Wert der teppich-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isTEPPICH() {
        return teppich;
    }

    /**
     * Legt den Wert der teppich-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setTEPPICH(Boolean value) {
        this.teppich = value;
    }

    /**
     * Ruft den Wert der parkett-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isPARKETT() {
        return parkett;
    }

    /**
     * Legt den Wert der parkett-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setPARKETT(Boolean value) {
        this.parkett = value;
    }

    /**
     * Ruft den Wert der fertigparkett-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isFERTIGPARKETT() {
        return fertigparkett;
    }

    /**
     * Legt den Wert der fertigparkett-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setFERTIGPARKETT(Boolean value) {
        this.fertigparkett = value;
    }

    /**
     * Ruft den Wert der laminat-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isLAMINAT() {
        return laminat;
    }

    /**
     * Legt den Wert der laminat-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setLAMINAT(Boolean value) {
        this.laminat = value;
    }

    /**
     * Ruft den Wert der dielen-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isDIELEN() {
        return dielen;
    }

    /**
     * Legt den Wert der dielen-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setDIELEN(Boolean value) {
        this.dielen = value;
    }

    /**
     * Ruft den Wert der kunststoff-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isKUNSTSTOFF() {
        return kunststoff;
    }

    /**
     * Legt den Wert der kunststoff-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setKUNSTSTOFF(Boolean value) {
        this.kunststoff = value;
    }

    /**
     * Ruft den Wert der estrich-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isESTRICH() {
        return estrich;
    }

    /**
     * Legt den Wert der estrich-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setESTRICH(Boolean value) {
        this.estrich = value;
    }

    /**
     * Ruft den Wert der doppelboden-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isDOPPELBODEN() {
        return doppelboden;
    }

    /**
     * Legt den Wert der doppelboden-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setDOPPELBODEN(Boolean value) {
        this.doppelboden = value;
    }

    /**
     * Ruft den Wert der linoleum-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isLINOLEUM() {
        return linoleum;
    }

    /**
     * Legt den Wert der linoleum-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setLINOLEUM(Boolean value) {
        this.linoleum = value;
    }

    /**
     * Ruft den Wert der marmor-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isMARMOR() {
        return marmor;
    }

    /**
     * Legt den Wert der marmor-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setMARMOR(Boolean value) {
        this.marmor = value;
    }

    /**
     * Ruft den Wert der terrakotta-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isTERRAKOTTA() {
        return terrakotta;
    }

    /**
     * Legt den Wert der terrakotta-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setTERRAKOTTA(Boolean value) {
        this.terrakotta = value;
    }

    /**
     * Ruft den Wert der granit-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isGRANIT() {
        return granit;
    }

    /**
     * Legt den Wert der granit-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setGRANIT(Boolean value) {
        this.granit = value;
    }

}
