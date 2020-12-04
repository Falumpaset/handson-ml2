//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.10.16 um 11:10:09 AM CEST 
//

package de.immomio.openimmo;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Java-Klasse f�r anonymous complex type.
 * <p>
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}objekttitel" minOccurs="0"/>
 *         &lt;element ref="{}dreizeiler" minOccurs="0"/>
 *         &lt;element ref="{}lage" minOccurs="0"/>
 *         &lt;element ref="{}ausstatt_beschr" minOccurs="0"/>
 *         &lt;element ref="{}objektbeschreibung" minOccurs="0"/>
 *         &lt;element ref="{}sonstige_angaben" minOccurs="0"/>
 *         &lt;element ref="{}objekt_text" minOccurs="0"/>
 *         &lt;element ref="{}user_defined_simplefield" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}user_defined_anyfield" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}user_defined_extend" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "objekttitel",
        "dreizeiler",
        "lage",
        "ausstattBeschr",
        "objektbeschreibung",
        "sonstigeAngaben",
        "objektText",
        "userDefinedSimplefield",
        "userDefinedAnyfield",
        "userDefinedExtend"
})
@XmlRootElement(name = "freitexte")
public class Freitexte {

    protected String objekttitel;

    protected String dreizeiler;

    protected String lage;

    @XmlElement(name = "ausstatt_beschr")
    protected String ausstattBeschr;

    protected String objektbeschreibung;

    @XmlElement(name = "sonstige_angaben")
    protected String sonstigeAngaben;

    @XmlElement(name = "objekt_text")
    protected ObjektText objektText;

    @XmlElement(name = "user_defined_simplefield")
    protected List<UserDefinedSimplefield> userDefinedSimplefield;

    @XmlElement(name = "user_defined_anyfield")
    protected List<UserDefinedAnyfield> userDefinedAnyfield;

    @XmlElement(name = "user_defined_extend")
    protected List<UserDefinedExtend> userDefinedExtend;

    /**
     * Ruft den Wert der objekttitel-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getObjekttitel() {
        return objekttitel;
    }

    /**
     * Legt den Wert der objekttitel-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setObjekttitel(String value) {
        this.objekttitel = value;
    }

    /**
     * Ruft den Wert der dreizeiler-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getDreizeiler() {
        return dreizeiler;
    }

    /**
     * Legt den Wert der dreizeiler-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setDreizeiler(String value) {
        this.dreizeiler = value;
    }

    /**
     * Ruft den Wert der lage-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getLage() {
        return lage;
    }

    /**
     * Legt den Wert der lage-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setLage(String value) {
        this.lage = value;
    }

    /**
     * Ruft den Wert der ausstattBeschr-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getAusstattBeschr() {
        return ausstattBeschr;
    }

    /**
     * Legt den Wert der ausstattBeschr-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setAusstattBeschr(String value) {
        this.ausstattBeschr = value;
    }

    /**
     * Ruft den Wert der objektbeschreibung-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getObjektbeschreibung() {
        return objektbeschreibung;
    }

    /**
     * Legt den Wert der objektbeschreibung-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setObjektbeschreibung(String value) {
        this.objektbeschreibung = value;
    }

    /**
     * Ruft den Wert der sonstigeAngaben-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getSonstigeAngaben() {
        return sonstigeAngaben;
    }

    /**
     * Legt den Wert der sonstigeAngaben-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setSonstigeAngaben(String value) {
        this.sonstigeAngaben = value;
    }

    /**
     * Ruft den Wert der objektText-Eigenschaft ab.
     *
     * @return possible object is {@link ObjektText }
     */
    public ObjektText getObjektText() {
        return objektText;
    }

    /**
     * Legt den Wert der objektText-Eigenschaft fest.
     *
     * @param value allowed object is {@link ObjektText }
     */
    public void setObjektText(ObjektText value) {
        this.objektText = value;
    }

    /**
     * Gets the value of the userDefinedSimplefield property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the userDefinedSimplefield property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUserDefinedSimplefield().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link UserDefinedSimplefield }
     */
    public List<UserDefinedSimplefield> getUserDefinedSimplefield() {
        if (userDefinedSimplefield == null) {
            userDefinedSimplefield = new ArrayList<>();
        }
        return this.userDefinedSimplefield;
    }

    /**
     * Gets the value of the userDefinedAnyfield property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the userDefinedAnyfield property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUserDefinedAnyfield().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link UserDefinedAnyfield }
     */
    public List<UserDefinedAnyfield> getUserDefinedAnyfield() {
        if (userDefinedAnyfield == null) {
            userDefinedAnyfield = new ArrayList<>();
        }
        return this.userDefinedAnyfield;
    }

    /**
     * Gets the value of the userDefinedExtend property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the userDefinedExtend property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUserDefinedExtend().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link UserDefinedExtend }
     */
    public List<UserDefinedExtend> getUserDefinedExtend() {
        if (userDefinedExtend == null) {
            userDefinedExtend = new ArrayList<>();
        }
        return this.userDefinedExtend;
    }

}
