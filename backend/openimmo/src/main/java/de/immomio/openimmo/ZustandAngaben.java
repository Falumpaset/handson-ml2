//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.10.16 um 11:10:09 AM CEST 
//

package de.immomio.openimmo;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

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
 *         &lt;element ref="{}baujahr" minOccurs="0"/>
 *         &lt;element ref="{}letztemodernisierung" minOccurs="0"/>
 *         &lt;element ref="{}zustand" minOccurs="0"/>
 *         &lt;element ref="{}alter" minOccurs="0"/>
 *         &lt;element ref="{}bebaubar_nach" minOccurs="0"/>
 *         &lt;element ref="{}erschliessung" minOccurs="0"/>
 *         &lt;element ref="{}erschliessung_umfang" minOccurs="0"/>
 *         &lt;element ref="{}bauzone" minOccurs="0"/>
 *         &lt;element ref="{}altlasten" minOccurs="0"/>
 *         &lt;element ref="{}energiepass" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}verkaufstatus" minOccurs="0"/>
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
        "baujahr",
        "letztemodernisierung",
        "zustand",
        "alter",
        "bebaubarNach",
        "erschliessung",
        "erschliessungUmfang",
        "bauzone",
        "altlasten",
        "energiepass",
        "verkaufstatus",
        "userDefinedSimplefield",
        "userDefinedAnyfield",
        "userDefinedExtend"
})
@XmlRootElement(name = "zustand_angaben")
public class ZustandAngaben {

    protected String baujahr;

    protected String letztemodernisierung;

    protected Zustand zustand;

    protected Alter alter;

    @XmlElement(name = "bebaubar_nach")
    protected BebaubarNach bebaubarNach;

    protected Erschliessung erschliessung;

    @XmlElement(name = "erschliessung_umfang")
    protected ErschliessungUmfang erschliessungUmfang;

    protected String bauzone;

    protected String altlasten;

    protected List<Energiepass> energiepass;

    protected Verkaufstatus verkaufstatus;

    @XmlElement(name = "user_defined_simplefield")
    protected List<UserDefinedSimplefield> userDefinedSimplefield;

    @XmlElement(name = "user_defined_anyfield")
    protected List<UserDefinedAnyfield> userDefinedAnyfield;

    @XmlElement(name = "user_defined_extend")
    protected List<UserDefinedExtend> userDefinedExtend;

    /**
     * Ruft den Wert der baujahr-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getBaujahr() {
        return baujahr;
    }


    /**
     * Legt den Wert der baujahr-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setBaujahr(String value) {
        this.baujahr = value;
    }

    /**
     * Ruft den Wert der letztemodernisierung-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getLetztemodernisierung() {
        return letztemodernisierung;
    }

    /**
     * Legt den Wert der letztemodernisierung-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setLetztemodernisierung(String value) {
        this.letztemodernisierung = value;
    }

    /**
     * Ruft den Wert der zustand-Eigenschaft ab.
     *
     * @return possible object is {@link Zustand }
     */
    public Zustand getZustand() {
        return zustand;
    }

    /**
     * Legt den Wert der zustand-Eigenschaft fest.
     *
     * @param value allowed object is {@link Zustand }
     */
    public void setZustand(Zustand value) {
        this.zustand = value;
    }

    /**
     * Ruft den Wert der alter-Eigenschaft ab.
     *
     * @return possible object is {@link Alter }
     */
    public Alter getAlter() {
        return alter;
    }

    /**
     * Legt den Wert der alter-Eigenschaft fest.
     *
     * @param value allowed object is {@link Alter }
     */
    public void setAlter(Alter value) {
        this.alter = value;
    }

    /**
     * Ruft den Wert der bebaubarNach-Eigenschaft ab.
     *
     * @return possible object is {@link BebaubarNach }
     */
    public BebaubarNach getBebaubarNach() {
        return bebaubarNach;
    }

    /**
     * Legt den Wert der bebaubarNach-Eigenschaft fest.
     *
     * @param value allowed object is {@link BebaubarNach }
     */
    public void setBebaubarNach(BebaubarNach value) {
        this.bebaubarNach = value;
    }

    /**
     * Ruft den Wert der erschliessung-Eigenschaft ab.
     *
     * @return possible object is {@link Erschliessung }
     */
    public Erschliessung getErschliessung() {
        return erschliessung;
    }

    /**
     * Legt den Wert der erschliessung-Eigenschaft fest.
     *
     * @param value allowed object is {@link Erschliessung }
     */
    public void setErschliessung(Erschliessung value) {
        this.erschliessung = value;
    }

    /**
     * Ruft den Wert der erschliessungUmfang-Eigenschaft ab.
     *
     * @return possible object is {@link ErschliessungUmfang }
     */
    public ErschliessungUmfang getErschliessungUmfang() {
        return erschliessungUmfang;
    }

    /**
     * Legt den Wert der erschliessungUmfang-Eigenschaft fest.
     *
     * @param value allowed object is {@link ErschliessungUmfang }
     */
    public void setErschliessungUmfang(ErschliessungUmfang value) {
        this.erschliessungUmfang = value;
    }

    /**
     * Ruft den Wert der bauzone-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getBauzone() {
        return bauzone;
    }

    /**
     * Legt den Wert der bauzone-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setBauzone(String value) {
        this.bauzone = value;
    }

    /**
     * Ruft den Wert der altlasten-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getAltlasten() {
        return altlasten;
    }

    /**
     * Legt den Wert der altlasten-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setAltlasten(String value) {
        this.altlasten = value;
    }

    /**
     * Gets the value of the energiepass property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the energiepass property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEnergiepass().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Energiepass }
     */
    public List<Energiepass> getEnergiepass() {
        if (energiepass == null) {
            energiepass = new ArrayList<>();
        }
        return this.energiepass;
    }

    /**
     * Ruft den Wert der verkaufstatus-Eigenschaft ab.
     *
     * @return possible object is {@link Verkaufstatus }
     */
    public Verkaufstatus getVerkaufstatus() {
        return verkaufstatus;
    }

    /**
     * Legt den Wert der verkaufstatus-Eigenschaft fest.
     *
     * @param value allowed object is {@link Verkaufstatus }
     */
    public void setVerkaufstatus(Verkaufstatus value) {
        this.verkaufstatus = value;
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
