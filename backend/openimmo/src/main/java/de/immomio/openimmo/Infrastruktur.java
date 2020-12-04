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
 *         &lt;element ref="{}zulieferung" minOccurs="0"/>
 *         &lt;element ref="{}ausblick" minOccurs="0"/>
 *         &lt;element ref="{}distanzen" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}distanzen_sport" maxOccurs="unbounded" minOccurs="0"/>
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
        "zulieferung",
        "ausblick",
        "distanzen",
        "distanzenSport",
        "userDefinedSimplefield",
        "userDefinedAnyfield",
        "userDefinedExtend"
})
@XmlRootElement(name = "infrastruktur")
public class Infrastruktur {

    protected Boolean zulieferung;

    protected Ausblick ausblick;

    protected List<Distanzen> distanzen;

    @XmlElement(name = "distanzen_sport")
    protected List<DistanzenSport> distanzenSport;

    @XmlElement(name = "user_defined_simplefield")
    protected List<UserDefinedSimplefield> userDefinedSimplefield;

    @XmlElement(name = "user_defined_anyfield")
    protected List<UserDefinedAnyfield> userDefinedAnyfield;

    @XmlElement(name = "user_defined_extend")
    protected List<UserDefinedExtend> userDefinedExtend;

    /**
     * Ruft den Wert der zulieferung-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isZulieferung() {
        return zulieferung;
    }

    /**
     * Legt den Wert der zulieferung-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setZulieferung(Boolean value) {
        this.zulieferung = value;
    }

    /**
     * Ruft den Wert der ausblick-Eigenschaft ab.
     *
     * @return possible object is {@link Ausblick }
     */
    public Ausblick getAusblick() {
        return ausblick;
    }

    /**
     * Legt den Wert der ausblick-Eigenschaft fest.
     *
     * @param value allowed object is {@link Ausblick }
     */
    public void setAusblick(Ausblick value) {
        this.ausblick = value;
    }

    /**
     * Gets the value of the distanzen property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the distanzen property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDistanzen().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Distanzen }
     */
    public List<Distanzen> getDistanzen() {
        if (distanzen == null) {
            distanzen = new ArrayList<>();
        }
        return this.distanzen;
    }

    /**
     * Gets the value of the distanzenSport property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the distanzenSport property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDistanzenSport().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link DistanzenSport }
     */
    public List<DistanzenSport> getDistanzenSport() {
        if (distanzenSport == null) {
            distanzenSport = new ArrayList<>();
        }
        return this.distanzenSport;
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
