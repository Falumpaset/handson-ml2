//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.10.16 um 11:10:09 AM CEST 
//

package de.immomio.openimmo;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
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
 *         &lt;element ref="{}beginn_angebotsphase" minOccurs="0"/>
 *         &lt;element ref="{}besichtigungstermin" minOccurs="0"/>
 *         &lt;element ref="{}besichtigungstermin_2" minOccurs="0"/>
 *         &lt;element ref="{}beginn_bietzeit" minOccurs="0"/>
 *         &lt;element ref="{}ende_bietzeit" minOccurs="0"/>
 *         &lt;element ref="{}hoechstgebot_zeigen" minOccurs="0"/>
 *         &lt;element ref="{}mindestpreis" minOccurs="0"/>
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
        "beginnAngebotsphase",
        "besichtigungstermin",
        "besichtigungstermin2",
        "beginnBietzeit",
        "endeBietzeit",
        "hoechstgebotZeigen",
        "mindestpreis",
        "userDefinedSimplefield",
        "userDefinedAnyfield",
        "userDefinedExtend"
})
@XmlRootElement(name = "bieterverfahren")
public class Bieterverfahren {

    @XmlElement(name = "beginn_angebotsphase")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar beginnAngebotsphase;

    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar besichtigungstermin;

    @XmlElement(name = "besichtigungstermin_2")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar besichtigungstermin2;

    @XmlElement(name = "beginn_bietzeit")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar beginnBietzeit;

    @XmlElement(name = "ende_bietzeit")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar endeBietzeit;

    @XmlElement(name = "hoechstgebot_zeigen")
    protected Boolean hoechstgebotZeigen;

    protected BigDecimal mindestpreis;

    @XmlElement(name = "user_defined_simplefield")
    protected List<UserDefinedSimplefield> userDefinedSimplefield;

    @XmlElement(name = "user_defined_anyfield")
    protected List<UserDefinedAnyfield> userDefinedAnyfield;

    @XmlElement(name = "user_defined_extend")
    protected List<UserDefinedExtend> userDefinedExtend;

    /**
     * Ruft den Wert der beginnAngebotsphase-Eigenschaft ab.
     *
     * @return possible object is {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getBeginnAngebotsphase() {
        return beginnAngebotsphase;
    }

    /**
     * Legt den Wert der beginnAngebotsphase-Eigenschaft fest.
     *
     * @param value allowed object is {@link XMLGregorianCalendar }
     */
    public void setBeginnAngebotsphase(XMLGregorianCalendar value) {
        this.beginnAngebotsphase = value;
    }

    /**
     * Ruft den Wert der besichtigungstermin-Eigenschaft ab.
     *
     * @return possible object is {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getBesichtigungstermin() {
        return besichtigungstermin;
    }

    /**
     * Legt den Wert der besichtigungstermin-Eigenschaft fest.
     *
     * @param value allowed object is {@link XMLGregorianCalendar }
     */
    public void setBesichtigungstermin(XMLGregorianCalendar value) {
        this.besichtigungstermin = value;
    }

    /**
     * Ruft den Wert der besichtigungstermin2-Eigenschaft ab.
     *
     * @return possible object is {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getBesichtigungstermin2() {
        return besichtigungstermin2;
    }

    /**
     * Legt den Wert der besichtigungstermin2-Eigenschaft fest.
     *
     * @param value allowed object is {@link XMLGregorianCalendar }
     */
    public void setBesichtigungstermin2(XMLGregorianCalendar value) {
        this.besichtigungstermin2 = value;
    }

    /**
     * Ruft den Wert der beginnBietzeit-Eigenschaft ab.
     *
     * @return possible object is {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getBeginnBietzeit() {
        return beginnBietzeit;
    }

    /**
     * Legt den Wert der beginnBietzeit-Eigenschaft fest.
     *
     * @param value allowed object is {@link XMLGregorianCalendar }
     */
    public void setBeginnBietzeit(XMLGregorianCalendar value) {
        this.beginnBietzeit = value;
    }

    /**
     * Ruft den Wert der endeBietzeit-Eigenschaft ab.
     *
     * @return possible object is {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getEndeBietzeit() {
        return endeBietzeit;
    }

    /**
     * Legt den Wert der endeBietzeit-Eigenschaft fest.
     *
     * @param value allowed object is {@link XMLGregorianCalendar }
     */
    public void setEndeBietzeit(XMLGregorianCalendar value) {
        this.endeBietzeit = value;
    }

    /**
     * Ruft den Wert der hoechstgebotZeigen-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isHoechstgebotZeigen() {
        return hoechstgebotZeigen;
    }

    /**
     * Legt den Wert der hoechstgebotZeigen-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setHoechstgebotZeigen(Boolean value) {
        this.hoechstgebotZeigen = value;
    }

    /**
     * Ruft den Wert der mindestpreis-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getMindestpreis() {
        return mindestpreis;
    }

    /**
     * Legt den Wert der mindestpreis-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setMindestpreis(BigDecimal value) {
        this.mindestpreis = value;
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
