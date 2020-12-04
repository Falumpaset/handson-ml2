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
 *         &lt;element ref="{}anbieternr" minOccurs="0"/>
 *         &lt;element ref="{}firma"/>
 *         &lt;element ref="{}openimmo_anid"/>
 *         &lt;element ref="{}lizenzkennung" minOccurs="0"/>
 *         &lt;element ref="{}anhang" minOccurs="0"/>
 *         &lt;element ref="{}immobilie" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}impressum" minOccurs="0"/>
 *         &lt;element ref="{}impressum_strukt" minOccurs="0"/>
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
        "anbieternr",
        "firma",
        "openimmoAnid",
        "lizenzkennung",
        "anhang",
        "immobilie",
        "impressum",
        "impressumStrukt",
        "userDefinedSimplefield",
        "userDefinedAnyfield",
        "userDefinedExtend"
})
@XmlRootElement(name = "anbieter")
public class Anbieter {

    protected String anbieternr;

    @XmlElement(required = true)
    protected String firma;

    @XmlElement(name = "openimmo_anid", required = true)
    protected String openimmoAnid;

    protected String lizenzkennung;

    protected Anhang anhang;

    protected List<Immobilie> immobilie;

    protected String impressum;

    @XmlElement(name = "impressum_strukt")
    protected ImpressumStrukt impressumStrukt;

    @XmlElement(name = "user_defined_simplefield")
    protected List<UserDefinedSimplefield> userDefinedSimplefield;

    @XmlElement(name = "user_defined_anyfield")
    protected List<UserDefinedAnyfield> userDefinedAnyfield;

    @XmlElement(name = "user_defined_extend")
    protected List<UserDefinedExtend> userDefinedExtend;

    /**
     * Ruft den Wert der anbieternr-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getAnbieternr() {
        return anbieternr;
    }

    /**
     * Legt den Wert der anbieternr-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setAnbieternr(String value) {
        this.anbieternr = value;
    }

    /**
     * Ruft den Wert der firma-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getFirma() {
        return firma;
    }

    /**
     * Legt den Wert der firma-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setFirma(String value) {
        this.firma = value;
    }

    /**
     * Ruft den Wert der openimmoAnid-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getOpenimmoAnid() {
        return openimmoAnid;
    }

    /**
     * Legt den Wert der openimmoAnid-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setOpenimmoAnid(String value) {
        this.openimmoAnid = value;
    }

    /**
     * Ruft den Wert der lizenzkennung-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getLizenzkennung() {
        return lizenzkennung;
    }

    /**
     * Legt den Wert der lizenzkennung-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setLizenzkennung(String value) {
        this.lizenzkennung = value;
    }

    /**
     * Ruft den Wert der anhang-Eigenschaft ab.
     *
     * @return possible object is {@link Anhang }
     */
    public Anhang getAnhang() {
        return anhang;
    }

    /**
     * Legt den Wert der anhang-Eigenschaft fest.
     *
     * @param value allowed object is {@link Anhang }
     */
    public void setAnhang(Anhang value) {
        this.anhang = value;
    }

    /**
     * Gets the value of the immobilie property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the immobilie property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getImmobilie().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Immobilie }
     */
    public List<Immobilie> getImmobilie() {
        if (immobilie == null) {
            immobilie = new ArrayList<>();
        }
        return this.immobilie;
    }

    /**
     * Ruft den Wert der impressum-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getImpressum() {
        return impressum;
    }

    /**
     * Legt den Wert der impressum-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setImpressum(String value) {
        this.impressum = value;
    }

    /**
     * Ruft den Wert der impressumStrukt-Eigenschaft ab.
     *
     * @return possible object is {@link ImpressumStrukt }
     */
    public ImpressumStrukt getImpressumStrukt() {
        return impressumStrukt;
    }

    /**
     * Legt den Wert der impressumStrukt-Eigenschaft fest.
     *
     * @param value allowed object is {@link ImpressumStrukt }
     */
    public void setImpressumStrukt(ImpressumStrukt value) {
        this.impressumStrukt = value;
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
