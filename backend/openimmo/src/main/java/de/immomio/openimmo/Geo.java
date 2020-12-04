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
 *         &lt;choice>
 *           &lt;sequence>
 *             &lt;element ref="{}plz"/>
 *             &lt;element ref="{}ort" minOccurs="0"/>
 *             &lt;element ref="{}geokoordinaten" minOccurs="0"/>
 *           &lt;/sequence>
 *         &lt;/choice>
 *         &lt;element ref="{}strasse" minOccurs="0"/>
 *         &lt;element ref="{}hausnummer" minOccurs="0"/>
 *         &lt;element ref="{}bundesland" minOccurs="0"/>
 *         &lt;element ref="{}land" minOccurs="0"/>
 *         &lt;element ref="{}gemeindecode" minOccurs="0"/>
 *         &lt;element ref="{}flur" minOccurs="0"/>
 *         &lt;element ref="{}flurstueck" minOccurs="0"/>
 *         &lt;element ref="{}gemarkung" minOccurs="0"/>
 *         &lt;element ref="{}etage" minOccurs="0"/>
 *         &lt;element ref="{}anzahl_etagen" minOccurs="0"/>
 *         &lt;element ref="{}lage_im_bau" minOccurs="0"/>
 *         &lt;element ref="{}wohnungsnr" minOccurs="0"/>
 *         &lt;element ref="{}lage_gebiet" minOccurs="0"/>
 *         &lt;element ref="{}regionaler_zusatz" minOccurs="0"/>
 *         &lt;element ref="{}karten_makro" minOccurs="0"/>
 *         &lt;element ref="{}karten_mikro" minOccurs="0"/>
 *         &lt;element ref="{}virtuelletour" minOccurs="0"/>
 *         &lt;element ref="{}luftbildern" minOccurs="0"/>
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
        "plz",
        "ort",
        "geokoordinaten",
        "strasse",
        "hausnummer",
        "bundesland",
        "land",
        "gemeindecode",
        "flur",
        "flurstueck",
        "gemarkung",
        "etage",
        "anzahlEtagen",
        "lageImBau",
        "wohnungsnr",
        "lageGebiet",
        "regionalerZusatz",
        "kartenMakro",
        "kartenMikro",
        "virtuelletour",
        "luftbildern",
        "userDefinedSimplefield",
        "userDefinedAnyfield",
        "userDefinedExtend"
})
@XmlRootElement(name = "geo")
public class Geo {

    protected String plz;

    protected String ort;

    protected Geokoordinaten geokoordinaten;

    protected String strasse;

    protected String hausnummer;

    protected String bundesland;

    protected Land land;

    protected String gemeindecode;

    protected String flur;

    protected String flurstueck;

    protected String gemarkung;

    protected Integer etage;

    @XmlElement(name = "anzahl_etagen")
    protected Integer anzahlEtagen;

    @XmlElement(name = "lage_im_bau")
    protected LageImBau lageImBau;

    protected String wohnungsnr;

    @XmlElement(name = "lage_gebiet")
    protected LageGebiet lageGebiet;

    @XmlElement(name = "regionaler_zusatz")
    protected String regionalerZusatz;

    @XmlElement(name = "karten_makro")
    protected Boolean kartenMakro;

    @XmlElement(name = "karten_mikro")
    protected Boolean kartenMikro;

    protected Boolean virtuelletour;

    protected Boolean luftbildern;

    @XmlElement(name = "user_defined_simplefield")
    protected List<UserDefinedSimplefield> userDefinedSimplefield;

    @XmlElement(name = "user_defined_anyfield")
    protected List<UserDefinedAnyfield> userDefinedAnyfield;

    @XmlElement(name = "user_defined_extend")
    protected List<UserDefinedExtend> userDefinedExtend;

    /**
     * Ruft den Wert der plz-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getPlz() {
        return plz;
    }

    /**
     * Legt den Wert der plz-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setPlz(String value) {
        this.plz = value;
    }

    /**
     * Ruft den Wert der ort-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getOrt() {
        return ort;
    }

    /**
     * Legt den Wert der ort-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setOrt(String value) {
        this.ort = value;
    }

    /**
     * Ruft den Wert der geokoordinaten-Eigenschaft ab.
     *
     * @return possible object is {@link Geokoordinaten }
     */
    public Geokoordinaten getGeokoordinaten() {
        return geokoordinaten;
    }

    /**
     * Legt den Wert der geokoordinaten-Eigenschaft fest.
     *
     * @param value allowed object is {@link Geokoordinaten }
     */
    public void setGeokoordinaten(Geokoordinaten value) {
        this.geokoordinaten = value;
    }

    /**
     * Ruft den Wert der strasse-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getStrasse() {
        return strasse;
    }

    /**
     * Legt den Wert der strasse-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setStrasse(String value) {
        this.strasse = value;
    }

    /**
     * Ruft den Wert der hausnummer-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getHausnummer() {
        return hausnummer;
    }

    /**
     * Legt den Wert der hausnummer-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setHausnummer(String value) {
        this.hausnummer = value;
    }

    /**
     * Ruft den Wert der bundesland-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getBundesland() {
        return bundesland;
    }

    /**
     * Legt den Wert der bundesland-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setBundesland(String value) {
        this.bundesland = value;
    }

    /**
     * Ruft den Wert der land-Eigenschaft ab.
     *
     * @return possible object is {@link Land }
     */
    public Land getLand() {
        return land;
    }

    /**
     * Legt den Wert der land-Eigenschaft fest.
     *
     * @param value allowed object is {@link Land }
     */
    public void setLand(Land value) {
        this.land = value;
    }

    /**
     * Ruft den Wert der gemeindecode-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getGemeindecode() {
        return gemeindecode;
    }

    /**
     * Legt den Wert der gemeindecode-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setGemeindecode(String value) {
        this.gemeindecode = value;
    }

    /**
     * Ruft den Wert der flur-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getFlur() {
        return flur;
    }

    /**
     * Legt den Wert der flur-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setFlur(String value) {
        this.flur = value;
    }

    /**
     * Ruft den Wert der flurstueck-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getFlurstueck() {
        return flurstueck;
    }

    /**
     * Legt den Wert der flurstueck-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setFlurstueck(String value) {
        this.flurstueck = value;
    }

    /**
     * Ruft den Wert der gemarkung-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getGemarkung() {
        return gemarkung;
    }

    /**
     * Legt den Wert der gemarkung-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setGemarkung(String value) {
        this.gemarkung = value;
    }

    /**
     * Ruft den Wert der etage-Eigenschaft ab.
     *
     * @return possible object is {@link Integer }
     */
    public Integer getEtage() {
        return etage;
    }

    /**
     * Legt den Wert der etage-Eigenschaft fest.
     *
     * @param value allowed object is {@link Integer }
     */
    public void setEtage(Integer value) {
        this.etage = value;
    }

    /**
     * Ruft den Wert der anzahlEtagen-Eigenschaft ab.
     *
     * @return possible object is {@link Integer }
     */
    public Integer getAnzahlEtagen() {
        return anzahlEtagen;
    }

    /**
     * Legt den Wert der anzahlEtagen-Eigenschaft fest.
     *
     * @param value allowed object is {@link Integer }
     */
    public void setAnzahlEtagen(Integer value) {
        this.anzahlEtagen = value;
    }

    /**
     * Ruft den Wert der lageImBau-Eigenschaft ab.
     *
     * @return possible object is {@link LageImBau }
     */
    public LageImBau getLageImBau() {
        return lageImBau;
    }

    /**
     * Legt den Wert der lageImBau-Eigenschaft fest.
     *
     * @param value allowed object is {@link LageImBau }
     */
    public void setLageImBau(LageImBau value) {
        this.lageImBau = value;
    }

    /**
     * Ruft den Wert der wohnungsnr-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getWohnungsnr() {
        return wohnungsnr;
    }

    /**
     * Legt den Wert der wohnungsnr-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setWohnungsnr(String value) {
        this.wohnungsnr = value;
    }

    /**
     * Ruft den Wert der lageGebiet-Eigenschaft ab.
     *
     * @return possible object is {@link LageGebiet }
     */
    public LageGebiet getLageGebiet() {
        return lageGebiet;
    }

    /**
     * Legt den Wert der lageGebiet-Eigenschaft fest.
     *
     * @param value allowed object is {@link LageGebiet }
     */
    public void setLageGebiet(LageGebiet value) {
        this.lageGebiet = value;
    }

    /**
     * Ruft den Wert der regionalerZusatz-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getRegionalerZusatz() {
        return regionalerZusatz;
    }

    /**
     * Legt den Wert der regionalerZusatz-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setRegionalerZusatz(String value) {
        this.regionalerZusatz = value;
    }

    /**
     * Ruft den Wert der kartenMakro-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isKartenMakro() {
        return kartenMakro;
    }

    /**
     * Legt den Wert der kartenMakro-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setKartenMakro(Boolean value) {
        this.kartenMakro = value;
    }

    /**
     * Ruft den Wert der kartenMikro-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isKartenMikro() {
        return kartenMikro;
    }

    /**
     * Legt den Wert der kartenMikro-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setKartenMikro(Boolean value) {
        this.kartenMikro = value;
    }

    /**
     * Ruft den Wert der virtuelletour-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isVirtuelletour() {
        return virtuelletour;
    }

    /**
     * Legt den Wert der virtuelletour-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setVirtuelletour(Boolean value) {
        this.virtuelletour = value;
    }

    /**
     * Ruft den Wert der luftbildern-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isLuftbildern() {
        return luftbildern;
    }

    /**
     * Legt den Wert der luftbildern-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setLuftbildern(Boolean value) {
        this.luftbildern = value;
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
