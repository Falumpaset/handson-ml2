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
 *         &lt;element ref="{}ausstatt_kategorie" minOccurs="0"/>
 *         &lt;element ref="{}wg_geeignet" minOccurs="0"/>
 *         &lt;element ref="{}raeume_veraenderbar" minOccurs="0"/>
 *         &lt;element ref="{}bad" minOccurs="0"/>
 *         &lt;element ref="{}kueche" minOccurs="0"/>
 *         &lt;element ref="{}boden" minOccurs="0"/>
 *         &lt;element ref="{}kamin" minOccurs="0"/>
 *         &lt;element ref="{}heizungsart" minOccurs="0"/>
 *         &lt;element ref="{}befeuerung" minOccurs="0"/>
 *         &lt;element ref="{}klimatisiert" minOccurs="0"/>
 *         &lt;element ref="{}fahrstuhl" minOccurs="0"/>
 *         &lt;element ref="{}stellplatzart" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}gartennutzung" minOccurs="0"/>
 *         &lt;element ref="{}ausricht_balkon_terrasse" minOccurs="0"/>
 *         &lt;element ref="{}moebliert" minOccurs="0"/>
 *         &lt;element ref="{}rollstuhlgerecht" minOccurs="0"/>
 *         &lt;element ref="{}kabel_sat_tv" minOccurs="0"/>
 *         &lt;element ref="{}dvbt" minOccurs="0"/>
 *         &lt;element ref="{}barrierefrei" minOccurs="0"/>
 *         &lt;element ref="{}sauna" minOccurs="0"/>
 *         &lt;element ref="{}swimmingpool" minOccurs="0"/>
 *         &lt;element ref="{}wasch_trockenraum" minOccurs="0"/>
 *         &lt;element ref="{}wintergarten" minOccurs="0"/>
 *         &lt;element ref="{}dv_verkabelung" minOccurs="0"/>
 *         &lt;element ref="{}rampe" minOccurs="0"/>
 *         &lt;element ref="{}hebebuehne" minOccurs="0"/>
 *         &lt;element ref="{}kran" minOccurs="0"/>
 *         &lt;element ref="{}gastterrasse" minOccurs="0"/>
 *         &lt;element ref="{}stromanschlusswert" minOccurs="0"/>
 *         &lt;element ref="{}kantine_cafeteria" minOccurs="0"/>
 *         &lt;element ref="{}teekueche" minOccurs="0"/>
 *         &lt;element ref="{}hallenhoehe" minOccurs="0"/>
 *         &lt;element ref="{}angeschl_gastronomie" minOccurs="0"/>
 *         &lt;element ref="{}brauereibindung" minOccurs="0"/>
 *         &lt;element ref="{}sporteinrichtungen" minOccurs="0"/>
 *         &lt;element ref="{}wellnessbereich" minOccurs="0"/>
 *         &lt;element ref="{}serviceleistungen" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}telefon_ferienimmobilie" minOccurs="0"/>
 *         &lt;element ref="{}breitband_zugang" minOccurs="0"/>
 *         &lt;element ref="{}umts_empfang" minOccurs="0"/>
 *         &lt;element ref="{}sicherheitstechnik" minOccurs="0"/>
 *         &lt;element ref="{}unterkellert" minOccurs="0"/>
 *         &lt;element ref="{}abstellraum" minOccurs="0"/>
 *         &lt;element ref="{}fahrradraum" minOccurs="0"/>
 *         &lt;element ref="{}rolladen" minOccurs="0"/>
 *         &lt;element ref="{}dachform" minOccurs="0"/>
 *         &lt;element ref="{}bauweise" minOccurs="0"/>
 *         &lt;element ref="{}ausbaustufe" minOccurs="0"/>
 *         &lt;element ref="{}energietyp" minOccurs="0"/>
 *         &lt;element ref="{}bibliothek" minOccurs="0"/>
 *         &lt;element ref="{}dachboden" minOccurs="0"/>
 *         &lt;element ref="{}gaestewc" minOccurs="0"/>
 *         &lt;element ref="{}kabelkanaele" minOccurs="0"/>
 *         &lt;element ref="{}seniorengerecht" minOccurs="0"/>
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
        "ausstattKategorie",
        "wgGeeignet",
        "raeumeVeraenderbar",
        "bad",
        "kueche",
        "boden",
        "kamin",
        "heizungsart",
        "befeuerung",
        "klimatisiert",
        "fahrstuhl",
        "stellplatzart",
        "gartennutzung",
        "ausrichtBalkonTerrasse",
        "moebliert",
        "rollstuhlgerecht",
        "kabelSatTv",
        "dvbt",
        "barrierefrei",
        "sauna",
        "swimmingpool",
        "waschTrockenraum",
        "wintergarten",
        "dvVerkabelung",
        "rampe",
        "hebebuehne",
        "kran",
        "gastterrasse",
        "stromanschlusswert",
        "kantineCafeteria",
        "teekueche",
        "hallenhoehe",
        "angeschlGastronomie",
        "brauereibindung",
        "sporteinrichtungen",
        "wellnessbereich",
        "serviceleistungen",
        "telefonFerienimmobilie",
        "breitbandZugang",
        "umtsEmpfang",
        "sicherheitstechnik",
        "unterkellert",
        "abstellraum",
        "fahrradraum",
        "rolladen",
        "dachform",
        "bauweise",
        "ausbaustufe",
        "energietyp",
        "bibliothek",
        "dachboden",
        "gaestewc",
        "kabelkanaele",
        "seniorengerecht",
        "userDefinedSimplefield",
        "userDefinedAnyfield",
        "userDefinedExtend"
})
@XmlRootElement(name = "ausstattung")
public class Ausstattung {

    @XmlElement(name = "ausstatt_kategorie")
    protected String ausstattKategorie;

    @XmlElement(name = "wg_geeignet")
    protected Boolean wgGeeignet;

    @XmlElement(name = "raeume_veraenderbar")
    protected Boolean raeumeVeraenderbar;

    protected Bad bad;

    protected Kueche kueche;

    protected Boden boden;

    protected Boolean kamin;

    protected Heizungsart heizungsart;

    protected Befeuerung befeuerung;

    protected Boolean klimatisiert;

    protected Fahrstuhl fahrstuhl;

    protected List<Stellplatzart> stellplatzart;

    protected Boolean gartennutzung;

    @XmlElement(name = "ausricht_balkon_terrasse")
    protected AusrichtBalkonTerrasse ausrichtBalkonTerrasse;

    protected Moebliert moebliert;

    protected Boolean rollstuhlgerecht;

    @XmlElement(name = "kabel_sat_tv")
    protected Boolean kabelSatTv;

    protected Boolean dvbt;

    protected Boolean barrierefrei;

    protected Boolean sauna;

    protected Boolean swimmingpool;

    @XmlElement(name = "wasch_trockenraum")
    protected Boolean waschTrockenraum;

    protected Boolean wintergarten;

    @XmlElement(name = "dv_verkabelung")
    protected Boolean dvVerkabelung;

    protected Boolean rampe;

    protected Boolean hebebuehne;

    protected Boolean kran;

    protected Boolean gastterrasse;

    protected Object stromanschlusswert;

    @XmlElement(name = "kantine_cafeteria")
    protected Boolean kantineCafeteria;

    protected Boolean teekueche;

    protected Object hallenhoehe;

    @XmlElement(name = "angeschl_gastronomie")
    protected AngeschlGastronomie angeschlGastronomie;

    protected Boolean brauereibindung;

    protected Boolean sporteinrichtungen;

    protected Boolean wellnessbereich;

    protected List<Serviceleistungen> serviceleistungen;

    @XmlElement(name = "telefon_ferienimmobilie")
    protected Boolean telefonFerienimmobilie;

    @XmlElement(name = "breitband_zugang")
    protected BreitbandZugang breitbandZugang;

    @XmlElement(name = "umts_empfang")
    protected Boolean umtsEmpfang;

    protected Sicherheitstechnik sicherheitstechnik;

    protected Unterkellert unterkellert;

    protected Boolean abstellraum;

    protected Boolean fahrradraum;

    protected Boolean rolladen;

    protected Dachform dachform;

    protected Bauweise bauweise;

    protected Ausbaustufe ausbaustufe;

    protected Energietyp energietyp;

    protected Boolean bibliothek;

    protected Boolean dachboden;

    protected Boolean gaestewc;

    protected Boolean kabelkanaele;

    protected Boolean seniorengerecht;

    @XmlElement(name = "user_defined_simplefield")
    protected List<UserDefinedSimplefield> userDefinedSimplefield;

    @XmlElement(name = "user_defined_anyfield")
    protected List<UserDefinedAnyfield> userDefinedAnyfield;

    @XmlElement(name = "user_defined_extend")
    protected List<UserDefinedExtend> userDefinedExtend;

    /**
     * Ruft den Wert der ausstattKategorie-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getAusstattKategorie() {
        return ausstattKategorie;
    }

    /**
     * Legt den Wert der ausstattKategorie-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setAusstattKategorie(String value) {
        this.ausstattKategorie = value;
    }

    /**
     * Ruft den Wert der wgGeeignet-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isWgGeeignet() {
        return wgGeeignet;
    }

    /**
     * Legt den Wert der wgGeeignet-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setWgGeeignet(Boolean value) {
        this.wgGeeignet = value;
    }

    /**
     * Ruft den Wert der raeumeVeraenderbar-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isRaeumeVeraenderbar() {
        return raeumeVeraenderbar;
    }

    /**
     * Legt den Wert der raeumeVeraenderbar-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setRaeumeVeraenderbar(Boolean value) {
        this.raeumeVeraenderbar = value;
    }

    /**
     * Ruft den Wert der bad-Eigenschaft ab.
     *
     * @return possible object is {@link Bad }
     */
    public Bad getBad() {
        return bad;
    }

    /**
     * Legt den Wert der bad-Eigenschaft fest.
     *
     * @param value allowed object is {@link Bad }
     */
    public void setBad(Bad value) {
        this.bad = value;
    }

    /**
     * Ruft den Wert der kueche-Eigenschaft ab.
     *
     * @return possible object is {@link Kueche }
     */
    public Kueche getKueche() {
        return kueche;
    }

    /**
     * Legt den Wert der kueche-Eigenschaft fest.
     *
     * @param value allowed object is {@link Kueche }
     */
    public void setKueche(Kueche value) {
        this.kueche = value;
    }

    /**
     * Ruft den Wert der boden-Eigenschaft ab.
     *
     * @return possible object is {@link Boden }
     */
    public Boden getBoden() {
        return boden;
    }

    /**
     * Legt den Wert der boden-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boden }
     */
    public void setBoden(Boden value) {
        this.boden = value;
    }

    /**
     * Ruft den Wert der kamin-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isKamin() {
        return kamin;
    }

    /**
     * Legt den Wert der kamin-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setKamin(Boolean value) {
        this.kamin = value;
    }

    /**
     * Ruft den Wert der heizungsart-Eigenschaft ab.
     *
     * @return possible object is {@link Heizungsart }
     */
    public Heizungsart getHeizungsart() {
        return heizungsart;
    }

    /**
     * Legt den Wert der heizungsart-Eigenschaft fest.
     *
     * @param value allowed object is {@link Heizungsart }
     */
    public void setHeizungsart(Heizungsart value) {
        this.heizungsart = value;
    }

    /**
     * Ruft den Wert der befeuerung-Eigenschaft ab.
     *
     * @return possible object is {@link Befeuerung }
     */
    public Befeuerung getBefeuerung() {
        return befeuerung;
    }

    /**
     * Legt den Wert der befeuerung-Eigenschaft fest.
     *
     * @param value allowed object is {@link Befeuerung }
     */
    public void setBefeuerung(Befeuerung value) {
        this.befeuerung = value;
    }

    /**
     * Ruft den Wert der klimatisiert-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isKlimatisiert() {
        return klimatisiert;
    }

    /**
     * Legt den Wert der klimatisiert-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setKlimatisiert(Boolean value) {
        this.klimatisiert = value;
    }

    /**
     * Ruft den Wert der fahrstuhl-Eigenschaft ab.
     *
     * @return possible object is {@link Fahrstuhl }
     */
    public Fahrstuhl getFahrstuhl() {
        return fahrstuhl;
    }

    /**
     * Legt den Wert der fahrstuhl-Eigenschaft fest.
     *
     * @param value allowed object is {@link Fahrstuhl }
     */
    public void setFahrstuhl(Fahrstuhl value) {
        this.fahrstuhl = value;
    }

    /**
     * Gets the value of the stellplatzart property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the stellplatzart property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStellplatzart().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Stellplatzart }
     */
    public List<Stellplatzart> getStellplatzart() {
        if (stellplatzart == null) {
            stellplatzart = new ArrayList<>();
        }
        return this.stellplatzart;
    }

    /**
     * Ruft den Wert der gartennutzung-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isGartennutzung() {
        return gartennutzung;
    }

    /**
     * Legt den Wert der gartennutzung-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setGartennutzung(Boolean value) {
        this.gartennutzung = value;
    }

    /**
     * Ruft den Wert der ausrichtBalkonTerrasse-Eigenschaft ab.
     *
     * @return possible object is {@link AusrichtBalkonTerrasse }
     */
    public AusrichtBalkonTerrasse getAusrichtBalkonTerrasse() {
        return ausrichtBalkonTerrasse;
    }

    /**
     * Legt den Wert der ausrichtBalkonTerrasse-Eigenschaft fest.
     *
     * @param value allowed object is {@link AusrichtBalkonTerrasse }
     */
    public void setAusrichtBalkonTerrasse(AusrichtBalkonTerrasse value) {
        this.ausrichtBalkonTerrasse = value;
    }

    /**
     * Ruft den Wert der moebliert-Eigenschaft ab.
     *
     * @return possible object is {@link Moebliert }
     */
    public Moebliert getMoebliert() {
        return moebliert;
    }

    /**
     * Legt den Wert der moebliert-Eigenschaft fest.
     *
     * @param value allowed object is {@link Moebliert }
     */
    public void setMoebliert(Moebliert value) {
        this.moebliert = value;
    }

    /**
     * Ruft den Wert der rollstuhlgerecht-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isRollstuhlgerecht() {
        return rollstuhlgerecht;
    }

    /**
     * Legt den Wert der rollstuhlgerecht-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setRollstuhlgerecht(Boolean value) {
        this.rollstuhlgerecht = value;
    }

    /**
     * Ruft den Wert der kabelSatTv-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isKabelSatTv() {
        return kabelSatTv;
    }

    /**
     * Legt den Wert der kabelSatTv-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setKabelSatTv(Boolean value) {
        this.kabelSatTv = value;
    }

    /**
     * Ruft den Wert der dvbt-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isDvbt() {
        return dvbt;
    }

    /**
     * Legt den Wert der dvbt-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setDvbt(Boolean value) {
        this.dvbt = value;
    }

    /**
     * Ruft den Wert der barrierefrei-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isBarrierefrei() {
        return barrierefrei;
    }

    /**
     * Legt den Wert der barrierefrei-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setBarrierefrei(Boolean value) {
        this.barrierefrei = value;
    }

    /**
     * Ruft den Wert der sauna-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isSauna() {
        return sauna;
    }

    /**
     * Legt den Wert der sauna-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setSauna(Boolean value) {
        this.sauna = value;
    }

    /**
     * Ruft den Wert der swimmingpool-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isSwimmingpool() {
        return swimmingpool;
    }

    /**
     * Legt den Wert der swimmingpool-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setSwimmingpool(Boolean value) {
        this.swimmingpool = value;
    }

    /**
     * Ruft den Wert der waschTrockenraum-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isWaschTrockenraum() {
        return waschTrockenraum;
    }

    /**
     * Legt den Wert der waschTrockenraum-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setWaschTrockenraum(Boolean value) {
        this.waschTrockenraum = value;
    }

    /**
     * Ruft den Wert der wintergarten-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isWintergarten() {
        return wintergarten;
    }

    /**
     * Legt den Wert der wintergarten-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setWintergarten(Boolean value) {
        this.wintergarten = value;
    }

    /**
     * Ruft den Wert der dvVerkabelung-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isDvVerkabelung() {
        return dvVerkabelung;
    }

    /**
     * Legt den Wert der dvVerkabelung-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setDvVerkabelung(Boolean value) {
        this.dvVerkabelung = value;
    }

    /**
     * Ruft den Wert der rampe-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isRampe() {
        return rampe;
    }

    /**
     * Legt den Wert der rampe-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setRampe(Boolean value) {
        this.rampe = value;
    }

    /**
     * Ruft den Wert der hebebuehne-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isHebebuehne() {
        return hebebuehne;
    }

    /**
     * Legt den Wert der hebebuehne-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setHebebuehne(Boolean value) {
        this.hebebuehne = value;
    }

    /**
     * Ruft den Wert der kran-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isKran() {
        return kran;
    }

    /**
     * Legt den Wert der kran-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setKran(Boolean value) {
        this.kran = value;
    }

    /**
     * Ruft den Wert der gastterrasse-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isGastterrasse() {
        return gastterrasse;
    }

    /**
     * Legt den Wert der gastterrasse-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setGastterrasse(Boolean value) {
        this.gastterrasse = value;
    }

    /**
     * Ruft den Wert der stromanschlusswert-Eigenschaft ab.
     *
     * @return possible object is {@link Object }
     */
    public Object getStromanschlusswert() {
        return stromanschlusswert;
    }

    /**
     * Legt den Wert der stromanschlusswert-Eigenschaft fest.
     *
     * @param value allowed object is {@link Object }
     */
    public void setStromanschlusswert(Object value) {
        this.stromanschlusswert = value;
    }

    /**
     * Ruft den Wert der kantineCafeteria-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isKantineCafeteria() {
        return kantineCafeteria;
    }

    /**
     * Legt den Wert der kantineCafeteria-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setKantineCafeteria(Boolean value) {
        this.kantineCafeteria = value;
    }

    /**
     * Ruft den Wert der teekueche-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isTeekueche() {
        return teekueche;
    }

    /**
     * Legt den Wert der teekueche-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setTeekueche(Boolean value) {
        this.teekueche = value;
    }

    /**
     * Ruft den Wert der hallenhoehe-Eigenschaft ab.
     *
     * @return possible object is {@link Object }
     */
    public Object getHallenhoehe() {
        return hallenhoehe;
    }

    /**
     * Legt den Wert der hallenhoehe-Eigenschaft fest.
     *
     * @param value allowed object is {@link Object }
     */
    public void setHallenhoehe(Object value) {
        this.hallenhoehe = value;
    }

    /**
     * Ruft den Wert der angeschlGastronomie-Eigenschaft ab.
     *
     * @return possible object is {@link AngeschlGastronomie }
     */
    public AngeschlGastronomie getAngeschlGastronomie() {
        return angeschlGastronomie;
    }

    /**
     * Legt den Wert der angeschlGastronomie-Eigenschaft fest.
     *
     * @param value allowed object is {@link AngeschlGastronomie }
     */
    public void setAngeschlGastronomie(AngeschlGastronomie value) {
        this.angeschlGastronomie = value;
    }

    /**
     * Ruft den Wert der brauereibindung-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isBrauereibindung() {
        return brauereibindung;
    }

    /**
     * Legt den Wert der brauereibindung-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setBrauereibindung(Boolean value) {
        this.brauereibindung = value;
    }

    /**
     * Ruft den Wert der sporteinrichtungen-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isSporteinrichtungen() {
        return sporteinrichtungen;
    }

    /**
     * Legt den Wert der sporteinrichtungen-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setSporteinrichtungen(Boolean value) {
        this.sporteinrichtungen = value;
    }

    /**
     * Ruft den Wert der wellnessbereich-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isWellnessbereich() {
        return wellnessbereich;
    }

    /**
     * Legt den Wert der wellnessbereich-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setWellnessbereich(Boolean value) {
        this.wellnessbereich = value;
    }

    /**
     * Gets the value of the serviceleistungen property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the serviceleistungen property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServiceleistungen().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Serviceleistungen }
     */
    public List<Serviceleistungen> getServiceleistungen() {
        if (serviceleistungen == null) {
            serviceleistungen = new ArrayList<>();
        }
        return this.serviceleistungen;
    }

    /**
     * Ruft den Wert der telefonFerienimmobilie-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isTelefonFerienimmobilie() {
        return telefonFerienimmobilie;
    }

    /**
     * Legt den Wert der telefonFerienimmobilie-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setTelefonFerienimmobilie(Boolean value) {
        this.telefonFerienimmobilie = value;
    }

    /**
     * Ruft den Wert der breitbandZugang-Eigenschaft ab.
     *
     * @return possible object is {@link BreitbandZugang }
     */
    public BreitbandZugang getBreitbandZugang() {
        return breitbandZugang;
    }

    /**
     * Legt den Wert der breitbandZugang-Eigenschaft fest.
     *
     * @param value allowed object is {@link BreitbandZugang }
     */
    public void setBreitbandZugang(BreitbandZugang value) {
        this.breitbandZugang = value;
    }

    /**
     * Ruft den Wert der umtsEmpfang-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isUmtsEmpfang() {
        return umtsEmpfang;
    }

    /**
     * Legt den Wert der umtsEmpfang-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setUmtsEmpfang(Boolean value) {
        this.umtsEmpfang = value;
    }

    /**
     * Ruft den Wert der sicherheitstechnik-Eigenschaft ab.
     *
     * @return possible object is {@link Sicherheitstechnik }
     */
    public Sicherheitstechnik getSicherheitstechnik() {
        return sicherheitstechnik;
    }

    /**
     * Legt den Wert der sicherheitstechnik-Eigenschaft fest.
     *
     * @param value allowed object is {@link Sicherheitstechnik }
     */
    public void setSicherheitstechnik(Sicherheitstechnik value) {
        this.sicherheitstechnik = value;
    }

    /**
     * Ruft den Wert der unterkellert-Eigenschaft ab.
     *
     * @return possible object is {@link Unterkellert }
     */
    public Unterkellert getUnterkellert() {
        return unterkellert;
    }

    /**
     * Legt den Wert der unterkellert-Eigenschaft fest.
     *
     * @param value allowed object is {@link Unterkellert }
     */
    public void setUnterkellert(Unterkellert value) {
        this.unterkellert = value;
    }

    /**
     * Ruft den Wert der abstellraum-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isAbstellraum() {
        return abstellraum;
    }

    /**
     * Legt den Wert der abstellraum-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setAbstellraum(Boolean value) {
        this.abstellraum = value;
    }

    /**
     * Ruft den Wert der fahrradraum-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isFahrradraum() {
        return fahrradraum;
    }

    /**
     * Legt den Wert der fahrradraum-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setFahrradraum(Boolean value) {
        this.fahrradraum = value;
    }

    /**
     * Ruft den Wert der rolladen-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isRolladen() {
        return rolladen;
    }

    /**
     * Legt den Wert der rolladen-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setRolladen(Boolean value) {
        this.rolladen = value;
    }

    /**
     * Ruft den Wert der dachform-Eigenschaft ab.
     *
     * @return possible object is {@link Dachform }
     */
    public Dachform getDachform() {
        return dachform;
    }

    /**
     * Legt den Wert der dachform-Eigenschaft fest.
     *
     * @param value allowed object is {@link Dachform }
     */
    public void setDachform(Dachform value) {
        this.dachform = value;
    }

    /**
     * Ruft den Wert der bauweise-Eigenschaft ab.
     *
     * @return possible object is {@link Bauweise }
     */
    public Bauweise getBauweise() {
        return bauweise;
    }

    /**
     * Legt den Wert der bauweise-Eigenschaft fest.
     *
     * @param value allowed object is {@link Bauweise }
     */
    public void setBauweise(Bauweise value) {
        this.bauweise = value;
    }

    /**
     * Ruft den Wert der ausbaustufe-Eigenschaft ab.
     *
     * @return possible object is {@link Ausbaustufe }
     */
    public Ausbaustufe getAusbaustufe() {
        return ausbaustufe;
    }

    /**
     * Legt den Wert der ausbaustufe-Eigenschaft fest.
     *
     * @param value allowed object is {@link Ausbaustufe }
     */
    public void setAusbaustufe(Ausbaustufe value) {
        this.ausbaustufe = value;
    }

    /**
     * Ruft den Wert der energietyp-Eigenschaft ab.
     *
     * @return possible object is {@link Energietyp }
     */
    public Energietyp getEnergietyp() {
        return energietyp;
    }

    /**
     * Legt den Wert der energietyp-Eigenschaft fest.
     *
     * @param value allowed object is {@link Energietyp }
     */
    public void setEnergietyp(Energietyp value) {
        this.energietyp = value;
    }

    /**
     * Ruft den Wert der bibliothek-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isBibliothek() {
        return bibliothek;
    }

    /**
     * Legt den Wert der bibliothek-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setBibliothek(Boolean value) {
        this.bibliothek = value;
    }

    /**
     * Ruft den Wert der dachboden-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isDachboden() {
        return dachboden;
    }

    /**
     * Legt den Wert der dachboden-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setDachboden(Boolean value) {
        this.dachboden = value;
    }

    /**
     * Ruft den Wert der gaestewc-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isGaestewc() {
        return gaestewc;
    }

    /**
     * Legt den Wert der gaestewc-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setGaestewc(Boolean value) {
        this.gaestewc = value;
    }

    /**
     * Ruft den Wert der kabelkanaele-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isKabelkanaele() {
        return kabelkanaele;
    }

    /**
     * Legt den Wert der kabelkanaele-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setKabelkanaele(Boolean value) {
        this.kabelkanaele = value;
    }

    /**
     * Ruft den Wert der seniorengerecht-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isSeniorengerecht() {
        return seniorengerecht;
    }

    /**
     * Legt den Wert der seniorengerecht-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setSeniorengerecht(Boolean value) {
        this.seniorengerecht = value;
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
