//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.10.16 um 11:10:09 AM CEST 
//

package de.immomio.openimmo;

import javax.xml.bind.annotation.*;
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
 *         &lt;element ref="{}wohnflaeche" minOccurs="0"/>
 *         &lt;element ref="{}nutzflaeche" minOccurs="0"/>
 *         &lt;element ref="{}gesamtflaeche" minOccurs="0"/>
 *         &lt;element ref="{}ladenflaeche" minOccurs="0"/>
 *         &lt;element ref="{}lagerflaeche" minOccurs="0"/>
 *         &lt;element ref="{}verkaufsflaeche" minOccurs="0"/>
 *         &lt;element ref="{}freiflaeche" minOccurs="0"/>
 *         &lt;element ref="{}bueroflaeche" minOccurs="0"/>
 *         &lt;element ref="{}bueroteilflaeche" minOccurs="0"/>
 *         &lt;element ref="{}fensterfront" minOccurs="0"/>
 *         &lt;element ref="{}verwaltungsflaeche" minOccurs="0"/>
 *         &lt;element ref="{}gastroflaeche" minOccurs="0"/>
 *         &lt;element ref="{}grz" minOccurs="0"/>
 *         &lt;element ref="{}gfz" minOccurs="0"/>
 *         &lt;element ref="{}bmz" minOccurs="0"/>
 *         &lt;element ref="{}bgf" minOccurs="0"/>
 *         &lt;element ref="{}grundstuecksflaeche" minOccurs="0"/>
 *         &lt;element ref="{}sonstflaeche" minOccurs="0"/>
 *         &lt;element ref="{}anzahl_zimmer" minOccurs="0"/>
 *         &lt;element ref="{}anzahl_schlafzimmer" minOccurs="0"/>
 *         &lt;element ref="{}anzahl_badezimmer" minOccurs="0"/>
 *         &lt;element ref="{}anzahl_sep_wc" minOccurs="0"/>
 *         &lt;element ref="{}anzahl_balkone" minOccurs="0"/>
 *         &lt;element ref="{}anzahl_terrassen" minOccurs="0"/>
 *         &lt;element ref="{}anzahl_logia" minOccurs="0"/>
 *         &lt;element ref="{}balkon_terrasse_flaeche" minOccurs="0"/>
 *         &lt;element ref="{}anzahl_wohn_schlafzimmer" minOccurs="0"/>
 *         &lt;element ref="{}gartenflaeche" minOccurs="0"/>
 *         &lt;element ref="{}kellerflaeche" minOccurs="0"/>
 *         &lt;element ref="{}fensterfront_qm" minOccurs="0"/>
 *         &lt;element ref="{}grundstuecksfront" minOccurs="0"/>
 *         &lt;element ref="{}dachbodenflaeche" minOccurs="0"/>
 *         &lt;element ref="{}teilbar_ab" minOccurs="0"/>
 *         &lt;element ref="{}beheizbare_flaeche" minOccurs="0"/>
 *         &lt;element ref="{}anzahl_stellplaetze" minOccurs="0"/>
 *         &lt;element ref="{}plaetze_gastraum" minOccurs="0"/>
 *         &lt;element ref="{}anzahl_betten" minOccurs="0"/>
 *         &lt;element ref="{}anzahl_tagungsraeume" minOccurs="0"/>
 *         &lt;element ref="{}vermietbare_flaeche" minOccurs="0"/>
 *         &lt;element ref="{}anzahl_wohneinheiten" minOccurs="0"/>
 *         &lt;element ref="{}anzahl_gewerbeeinheiten" minOccurs="0"/>
 *         &lt;element ref="{}einliegerwohnung" minOccurs="0"/>
 *         &lt;element ref="{}kubatur" minOccurs="0"/>
 *         &lt;element ref="{}ausnuetzungsziffer" minOccurs="0"/>
 *         &lt;element ref="{}flaechevon" minOccurs="0"/>
 *         &lt;element ref="{}flaechebis" minOccurs="0"/>
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
        "wohnflaeche",
        "nutzflaeche",
        "gesamtflaeche",
        "ladenflaeche",
        "lagerflaeche",
        "verkaufsflaeche",
        "freiflaeche",
        "bueroflaeche",
        "bueroteilflaeche",
        "fensterfront",
        "verwaltungsflaeche",
        "gastroflaeche",
        "grz",
        "gfz",
        "bmz",
        "bgf",
        "grundstuecksflaeche",
        "sonstflaeche",
        "anzahlZimmer",
        "anzahlSchlafzimmer",
        "anzahlBadezimmer",
        "anzahlSepWc",
        "anzahlBalkone",
        "anzahlTerrassen",
        "anzahlLogia",
        "balkonTerrasseFlaeche",
        "anzahlWohnSchlafzimmer",
        "gartenflaeche",
        "kellerflaeche",
        "fensterfrontQm",
        "grundstuecksfront",
        "dachbodenflaeche",
        "teilbarAb",
        "beheizbareFlaeche",
        "anzahlStellplaetze",
        "plaetzeGastraum",
        "anzahlBetten",
        "anzahlTagungsraeume",
        "vermietbareFlaeche",
        "anzahlWohneinheiten",
        "anzahlGewerbeeinheiten",
        "einliegerwohnung",
        "kubatur",
        "ausnuetzungsziffer",
        "flaechevon",
        "flaechebis",
        "userDefinedSimplefield",
        "userDefinedAnyfield",
        "userDefinedExtend"
})
@XmlRootElement(name = "flaechen")
public class Flaechen {

    protected BigDecimal wohnflaeche;

    protected BigDecimal nutzflaeche;

    protected BigDecimal gesamtflaeche;

    protected BigDecimal ladenflaeche;

    protected BigDecimal lagerflaeche;

    protected BigDecimal verkaufsflaeche;

    protected BigDecimal freiflaeche;

    protected BigDecimal bueroflaeche;

    protected BigDecimal bueroteilflaeche;

    protected BigDecimal fensterfront;

    protected BigDecimal verwaltungsflaeche;

    protected BigDecimal gastroflaeche;

    protected String grz;

    protected String gfz;

    protected String bmz;

    protected String bgf;

    protected BigDecimal grundstuecksflaeche;

    protected BigDecimal sonstflaeche;

    @XmlElement(name = "anzahl_zimmer")
    protected BigDecimal anzahlZimmer;

    @XmlElement(name = "anzahl_schlafzimmer")
    protected BigDecimal anzahlSchlafzimmer;

    @XmlElement(name = "anzahl_badezimmer")
    protected BigDecimal anzahlBadezimmer;

    @XmlElement(name = "anzahl_sep_wc")
    protected BigDecimal anzahlSepWc;

    @XmlElement(name = "anzahl_balkone")
    protected BigDecimal anzahlBalkone;

    @XmlElement(name = "anzahl_terrassen")
    protected BigDecimal anzahlTerrassen;

    @XmlElement(name = "anzahl_logia")
    protected BigDecimal anzahlLogia;

    @XmlElement(name = "balkon_terrasse_flaeche")
    protected BigDecimal balkonTerrasseFlaeche;

    @XmlElement(name = "anzahl_wohn_schlafzimmer")
    protected BigDecimal anzahlWohnSchlafzimmer;

    protected BigDecimal gartenflaeche;

    protected BigDecimal kellerflaeche;

    @XmlElement(name = "fensterfront_qm")
    protected BigDecimal fensterfrontQm;

    protected BigDecimal grundstuecksfront;

    protected BigDecimal dachbodenflaeche;

    @XmlElement(name = "teilbar_ab")
    protected BigDecimal teilbarAb;

    @XmlElement(name = "beheizbare_flaeche")
    protected BigDecimal beheizbareFlaeche;

    @XmlElement(name = "anzahl_stellplaetze")
    protected BigDecimal anzahlStellplaetze;

    @XmlElement(name = "plaetze_gastraum")
    protected BigDecimal plaetzeGastraum;

    @XmlElement(name = "anzahl_betten")
    protected BigDecimal anzahlBetten;

    @XmlElement(name = "anzahl_tagungsraeume")
    protected BigDecimal anzahlTagungsraeume;

    @XmlElement(name = "vermietbare_flaeche")
    protected BigDecimal vermietbareFlaeche;

    @XmlElement(name = "anzahl_wohneinheiten")
    protected BigDecimal anzahlWohneinheiten;

    @XmlElement(name = "anzahl_gewerbeeinheiten")
    protected BigDecimal anzahlGewerbeeinheiten;

    protected Boolean einliegerwohnung;

    protected BigDecimal kubatur;

    protected BigDecimal ausnuetzungsziffer;

    protected BigDecimal flaechevon;

    protected BigDecimal flaechebis;

    @XmlElement(name = "user_defined_simplefield")
    protected List<UserDefinedSimplefield> userDefinedSimplefield;

    @XmlElement(name = "user_defined_anyfield")
    protected List<UserDefinedAnyfield> userDefinedAnyfield;

    @XmlElement(name = "user_defined_extend")
    protected List<UserDefinedExtend> userDefinedExtend;

    /**
     * Ruft den Wert der wohnflaeche-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getWohnflaeche() {
        return wohnflaeche;
    }

    /**
     * Legt den Wert der wohnflaeche-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setWohnflaeche(BigDecimal value) {
        this.wohnflaeche = value;
    }

    /**
     * Ruft den Wert der nutzflaeche-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getNutzflaeche() {
        return nutzflaeche;
    }

    /**
     * Legt den Wert der nutzflaeche-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setNutzflaeche(BigDecimal value) {
        this.nutzflaeche = value;
    }

    /**
     * Ruft den Wert der gesamtflaeche-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getGesamtflaeche() {
        return gesamtflaeche;
    }

    /**
     * Legt den Wert der gesamtflaeche-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setGesamtflaeche(BigDecimal value) {
        this.gesamtflaeche = value;
    }

    /**
     * Ruft den Wert der ladenflaeche-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getLadenflaeche() {
        return ladenflaeche;
    }

    /**
     * Legt den Wert der ladenflaeche-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setLadenflaeche(BigDecimal value) {
        this.ladenflaeche = value;
    }

    /**
     * Ruft den Wert der lagerflaeche-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getLagerflaeche() {
        return lagerflaeche;
    }

    /**
     * Legt den Wert der lagerflaeche-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setLagerflaeche(BigDecimal value) {
        this.lagerflaeche = value;
    }

    /**
     * Ruft den Wert der verkaufsflaeche-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getVerkaufsflaeche() {
        return verkaufsflaeche;
    }

    /**
     * Legt den Wert der verkaufsflaeche-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setVerkaufsflaeche(BigDecimal value) {
        this.verkaufsflaeche = value;
    }

    /**
     * Ruft den Wert der freiflaeche-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getFreiflaeche() {
        return freiflaeche;
    }

    /**
     * Legt den Wert der freiflaeche-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setFreiflaeche(BigDecimal value) {
        this.freiflaeche = value;
    }

    /**
     * Ruft den Wert der bueroflaeche-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getBueroflaeche() {
        return bueroflaeche;
    }

    /**
     * Legt den Wert der bueroflaeche-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setBueroflaeche(BigDecimal value) {
        this.bueroflaeche = value;
    }

    /**
     * Ruft den Wert der bueroteilflaeche-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getBueroteilflaeche() {
        return bueroteilflaeche;
    }

    /**
     * Legt den Wert der bueroteilflaeche-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setBueroteilflaeche(BigDecimal value) {
        this.bueroteilflaeche = value;
    }

    /**
     * Ruft den Wert der fensterfront-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getFensterfront() {
        return fensterfront;
    }

    /**
     * Legt den Wert der fensterfront-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setFensterfront(BigDecimal value) {
        this.fensterfront = value;
    }

    /**
     * Ruft den Wert der verwaltungsflaeche-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getVerwaltungsflaeche() {
        return verwaltungsflaeche;
    }

    /**
     * Legt den Wert der verwaltungsflaeche-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setVerwaltungsflaeche(BigDecimal value) {
        this.verwaltungsflaeche = value;
    }

    /**
     * Ruft den Wert der gastroflaeche-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getGastroflaeche() {
        return gastroflaeche;
    }

    /**
     * Legt den Wert der gastroflaeche-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setGastroflaeche(BigDecimal value) {
        this.gastroflaeche = value;
    }

    /**
     * Ruft den Wert der grz-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getGrz() {
        return grz;
    }

    /**
     * Legt den Wert der grz-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setGrz(String value) {
        this.grz = value;
    }

    /**
     * Ruft den Wert der gfz-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getGfz() {
        return gfz;
    }

    /**
     * Legt den Wert der gfz-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setGfz(String value) {
        this.gfz = value;
    }

    /**
     * Ruft den Wert der bmz-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getBmz() {
        return bmz;
    }

    /**
     * Legt den Wert der bmz-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setBmz(String value) {
        this.bmz = value;
    }

    /**
     * Ruft den Wert der bgf-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getBgf() {
        return bgf;
    }

    /**
     * Legt den Wert der bgf-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setBgf(String value) {
        this.bgf = value;
    }

    /**
     * Ruft den Wert der grundstuecksflaeche-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getGrundstuecksflaeche() {
        return grundstuecksflaeche;
    }

    /**
     * Legt den Wert der grundstuecksflaeche-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setGrundstuecksflaeche(BigDecimal value) {
        this.grundstuecksflaeche = value;
    }

    /**
     * Ruft den Wert der sonstflaeche-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getSonstflaeche() {
        return sonstflaeche;
    }

    /**
     * Legt den Wert der sonstflaeche-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setSonstflaeche(BigDecimal value) {
        this.sonstflaeche = value;
    }

    /**
     * Ruft den Wert der anzahlZimmer-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getAnzahlZimmer() {
        return anzahlZimmer;
    }

    /**
     * Legt den Wert der anzahlZimmer-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setAnzahlZimmer(BigDecimal value) {
        this.anzahlZimmer = value;
    }

    /**
     * Ruft den Wert der anzahlSchlafzimmer-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getAnzahlSchlafzimmer() {
        return anzahlSchlafzimmer;
    }

    /**
     * Legt den Wert der anzahlSchlafzimmer-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setAnzahlSchlafzimmer(BigDecimal value) {
        this.anzahlSchlafzimmer = value;
    }

    /**
     * Ruft den Wert der anzahlBadezimmer-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getAnzahlBadezimmer() {
        return anzahlBadezimmer;
    }

    /**
     * Legt den Wert der anzahlBadezimmer-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setAnzahlBadezimmer(BigDecimal value) {
        this.anzahlBadezimmer = value;
    }

    /**
     * Ruft den Wert der anzahlSepWc-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getAnzahlSepWc() {
        return anzahlSepWc;
    }

    /**
     * Legt den Wert der anzahlSepWc-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setAnzahlSepWc(BigDecimal value) {
        this.anzahlSepWc = value;
    }

    /**
     * Ruft den Wert der anzahlBalkone-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getAnzahlBalkone() {
        return anzahlBalkone;
    }

    /**
     * Legt den Wert der anzahlBalkone-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setAnzahlBalkone(BigDecimal value) {
        this.anzahlBalkone = value;
    }

    /**
     * Ruft den Wert der anzahlTerrassen-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getAnzahlTerrassen() {
        return anzahlTerrassen;
    }

    /**
     * Legt den Wert der anzahlTerrassen-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setAnzahlTerrassen(BigDecimal value) {
        this.anzahlTerrassen = value;
    }

    /**
     * Ruft den Wert der anzahlLogia-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getAnzahlLogia() {
        return anzahlLogia;
    }

    /**
     * Legt den Wert der anzahlLogia-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setAnzahlLogia(BigDecimal value) {
        this.anzahlLogia = value;
    }

    /**
     * Ruft den Wert der balkonTerrasseFlaeche-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getBalkonTerrasseFlaeche() {
        return balkonTerrasseFlaeche;
    }

    /**
     * Legt den Wert der balkonTerrasseFlaeche-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setBalkonTerrasseFlaeche(BigDecimal value) {
        this.balkonTerrasseFlaeche = value;
    }

    /**
     * Ruft den Wert der anzahlWohnSchlafzimmer-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getAnzahlWohnSchlafzimmer() {
        return anzahlWohnSchlafzimmer;
    }

    /**
     * Legt den Wert der anzahlWohnSchlafzimmer-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setAnzahlWohnSchlafzimmer(BigDecimal value) {
        this.anzahlWohnSchlafzimmer = value;
    }

    /**
     * Ruft den Wert der gartenflaeche-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getGartenflaeche() {
        return gartenflaeche;
    }

    /**
     * Legt den Wert der gartenflaeche-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setGartenflaeche(BigDecimal value) {
        this.gartenflaeche = value;
    }

    /**
     * Ruft den Wert der kellerflaeche-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getKellerflaeche() {
        return kellerflaeche;
    }

    /**
     * Legt den Wert der kellerflaeche-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setKellerflaeche(BigDecimal value) {
        this.kellerflaeche = value;
    }

    /**
     * Ruft den Wert der fensterfrontQm-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getFensterfrontQm() {
        return fensterfrontQm;
    }

    /**
     * Legt den Wert der fensterfrontQm-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setFensterfrontQm(BigDecimal value) {
        this.fensterfrontQm = value;
    }

    /**
     * Ruft den Wert der grundstuecksfront-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getGrundstuecksfront() {
        return grundstuecksfront;
    }

    /**
     * Legt den Wert der grundstuecksfront-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setGrundstuecksfront(BigDecimal value) {
        this.grundstuecksfront = value;
    }

    /**
     * Ruft den Wert der dachbodenflaeche-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getDachbodenflaeche() {
        return dachbodenflaeche;
    }

    /**
     * Legt den Wert der dachbodenflaeche-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setDachbodenflaeche(BigDecimal value) {
        this.dachbodenflaeche = value;
    }

    /**
     * Ruft den Wert der teilbarAb-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getTeilbarAb() {
        return teilbarAb;
    }

    /**
     * Legt den Wert der teilbarAb-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setTeilbarAb(BigDecimal value) {
        this.teilbarAb = value;
    }

    /**
     * Ruft den Wert der beheizbareFlaeche-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getBeheizbareFlaeche() {
        return beheizbareFlaeche;
    }

    /**
     * Legt den Wert der beheizbareFlaeche-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setBeheizbareFlaeche(BigDecimal value) {
        this.beheizbareFlaeche = value;
    }

    /**
     * Ruft den Wert der anzahlStellplaetze-Eigenschaft ab.
     *
     * @return possible object is {@link Object }
     */
    public BigDecimal getAnzahlStellplaetze() {
        return anzahlStellplaetze;
    }

    /**
     * Legt den Wert der anzahlStellplaetze-Eigenschaft fest.
     *
     * @param value allowed object is {@link Object }
     */
    public void setAnzahlStellplaetze(BigDecimal value) {
        this.anzahlStellplaetze = value;
    }

    /**
     * Ruft den Wert der plaetzeGastraum-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getPlaetzeGastraum() {
        return plaetzeGastraum;
    }

    /**
     * Legt den Wert der plaetzeGastraum-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setPlaetzeGastraum(BigDecimal value) {
        this.plaetzeGastraum = value;
    }

    /**
     * Ruft den Wert der anzahlBetten-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getAnzahlBetten() {
        return anzahlBetten;
    }

    /**
     * Legt den Wert der anzahlBetten-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setAnzahlBetten(BigDecimal value) {
        this.anzahlBetten = value;
    }

    /**
     * Ruft den Wert der anzahlTagungsraeume-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getAnzahlTagungsraeume() {
        return anzahlTagungsraeume;
    }

    /**
     * Legt den Wert der anzahlTagungsraeume-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setAnzahlTagungsraeume(BigDecimal value) {
        this.anzahlTagungsraeume = value;
    }

    /**
     * Ruft den Wert der vermietbareFlaeche-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getVermietbareFlaeche() {
        return vermietbareFlaeche;
    }

    /**
     * Legt den Wert der vermietbareFlaeche-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setVermietbareFlaeche(BigDecimal value) {
        this.vermietbareFlaeche = value;
    }

    /**
     * Ruft den Wert der anzahlWohneinheiten-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getAnzahlWohneinheiten() {
        return anzahlWohneinheiten;
    }

    /**
     * Legt den Wert der anzahlWohneinheiten-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setAnzahlWohneinheiten(BigDecimal value) {
        this.anzahlWohneinheiten = value;
    }

    /**
     * Ruft den Wert der anzahlGewerbeeinheiten-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getAnzahlGewerbeeinheiten() {
        return anzahlGewerbeeinheiten;
    }

    /**
     * Legt den Wert der anzahlGewerbeeinheiten-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setAnzahlGewerbeeinheiten(BigDecimal value) {
        this.anzahlGewerbeeinheiten = value;
    }

    /**
     * Ruft den Wert der einliegerwohnung-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isEinliegerwohnung() {
        return einliegerwohnung;
    }

    /**
     * Legt den Wert der einliegerwohnung-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setEinliegerwohnung(Boolean value) {
        this.einliegerwohnung = value;
    }

    /**
     * Ruft den Wert der kubatur-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getKubatur() {
        return kubatur;
    }

    /**
     * Legt den Wert der kubatur-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setKubatur(BigDecimal value) {
        this.kubatur = value;
    }

    /**
     * Ruft den Wert der ausnuetzungsziffer-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getAusnuetzungsziffer() {
        return ausnuetzungsziffer;
    }

    /**
     * Legt den Wert der ausnuetzungsziffer-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setAusnuetzungsziffer(BigDecimal value) {
        this.ausnuetzungsziffer = value;
    }

    /**
     * Ruft den Wert der flaechevon-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getFlaechevon() {
        return flaechevon;
    }

    /**
     * Legt den Wert der flaechevon-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setFlaechevon(BigDecimal value) {
        this.flaechevon = value;
    }

    /**
     * Ruft den Wert der flaechebis-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getFlaechebis() {
        return flaechebis;
    }

    /**
     * Legt den Wert der flaechebis-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setFlaechebis(BigDecimal value) {
        this.flaechebis = value;
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
