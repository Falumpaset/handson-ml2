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
 *         &lt;element ref="{}kaufpreis" minOccurs="0"/>
 *         &lt;element ref="{}kaufpreisnetto" minOccurs="0"/>
 *         &lt;element ref="{}kaufpreisbrutto" minOccurs="0"/>
 *         &lt;element ref="{}nettokaltmiete" minOccurs="0"/>
 *         &lt;element ref="{}kaltmiete" minOccurs="0"/>
 *         &lt;element ref="{}warmmiete" minOccurs="0"/>
 *         &lt;element ref="{}nebenkosten" minOccurs="0"/>
 *         &lt;element ref="{}heizkosten_enthalten" minOccurs="0"/>
 *         &lt;element ref="{}heizkosten" minOccurs="0"/>
 *         &lt;element ref="{}zzg_mehrwertsteuer" minOccurs="0"/>
 *         &lt;element ref="{}mietzuschlaege" minOccurs="0"/>
 *         &lt;element ref="{}hauptmietzinsnetto" minOccurs="0"/>
 *         &lt;element ref="{}pauschalmiete" minOccurs="0"/>
 *         &lt;element ref="{}betriebskostennetto" minOccurs="0"/>
 *         &lt;element ref="{}evbnetto" minOccurs="0"/>
 *         &lt;element ref="{}gesamtmietenetto" minOccurs="0"/>
 *         &lt;element ref="{}gesamtmietebrutto" minOccurs="0"/>
 *         &lt;element ref="{}gesamtbelastungnetto" minOccurs="0"/>
 *         &lt;element ref="{}gesamtbelastungbrutto" minOccurs="0"/>
 *         &lt;element ref="{}gesamtkostenprom2von" minOccurs="0"/>
 *         &lt;element ref="{}heizkostennetto" minOccurs="0"/>
 *         &lt;element ref="{}monatlichekostennetto" minOccurs="0"/>
 *         &lt;element ref="{}monatlichekostenbrutto" minOccurs="0"/>
 *         &lt;element ref="{}nebenkostenprom2von" minOccurs="0"/>
 *         &lt;element ref="{}ruecklagenetto" minOccurs="0"/>
 *         &lt;element ref="{}sonstigekostennetto" minOccurs="0"/>
 *         &lt;element ref="{}sonstigemietenetto" minOccurs="0"/>
 *         &lt;element ref="{}summemietenetto" minOccurs="0"/>
 *         &lt;element ref="{}nettomieteprom2von" minOccurs="0"/>
 *         &lt;element ref="{}pacht" minOccurs="0"/>
 *         &lt;element ref="{}erbpacht" minOccurs="0"/>
 *         &lt;element ref="{}hausgeld" minOccurs="0"/>
 *         &lt;element ref="{}abstand" minOccurs="0"/>
 *         &lt;element ref="{}preis_zeitraum_von" minOccurs="0"/>
 *         &lt;element ref="{}preis_zeitraum_bis" minOccurs="0"/>
 *         &lt;element ref="{}preis_zeiteinheit" minOccurs="0"/>
 *         &lt;element ref="{}mietpreis_pro_qm" minOccurs="0"/>
 *         &lt;element ref="{}kaufpreis_pro_qm" minOccurs="0"/>
 *         &lt;element ref="{}provisionspflichtig" minOccurs="0"/>
 *         &lt;element ref="{}provision_teilen" minOccurs="0"/>
 *         &lt;element ref="{}innen_courtage" minOccurs="0"/>
 *         &lt;element ref="{}aussen_courtage" minOccurs="0"/>
 *         &lt;element ref="{}courtage_hinweis" minOccurs="0"/>
 *         &lt;element ref="{}provisionnetto" minOccurs="0"/>
 *         &lt;element ref="{}provisionbrutto" minOccurs="0"/>
 *         &lt;element ref="{}waehrung" minOccurs="0"/>
 *         &lt;element ref="{}mwst_satz" minOccurs="0"/>
 *         &lt;element ref="{}mwst_gesamt" minOccurs="0"/>
 *         &lt;element ref="{}freitext_preis" minOccurs="0"/>
 *         &lt;element ref="{}x_fache" minOccurs="0"/>
 *         &lt;element ref="{}nettorendite" minOccurs="0"/>
 *         &lt;element ref="{}nettorendite_soll" minOccurs="0"/>
 *         &lt;element ref="{}nettorendite_ist" minOccurs="0"/>
 *         &lt;element ref="{}mieteinnahmen_ist" minOccurs="0"/>
 *         &lt;element ref="{}mieteinnahmen_soll" minOccurs="0"/>
 *         &lt;element ref="{}erschliessungskosten" minOccurs="0"/>
 *         &lt;element ref="{}kaution" minOccurs="0"/>
 *         &lt;element ref="{}kaution_text" minOccurs="0"/>
 *         &lt;element ref="{}geschaeftsguthaben" minOccurs="0"/>
 *         &lt;element ref="{}stp_carport" minOccurs="0"/>
 *         &lt;element ref="{}stp_duplex" minOccurs="0"/>
 *         &lt;element ref="{}stp_freiplatz" minOccurs="0"/>
 *         &lt;element ref="{}stp_garage" minOccurs="0"/>
 *         &lt;element ref="{}stp_parkhaus" minOccurs="0"/>
 *         &lt;element ref="{}stp_tiefgarage" minOccurs="0"/>
 *         &lt;element ref="{}stp_sonstige" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}richtpreis" minOccurs="0"/>
 *         &lt;element ref="{}richtpreisprom2" minOccurs="0"/>
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
        "kaufpreis",
        "kaufpreisnetto",
        "kaufpreisbrutto",
        "nettokaltmiete",
        "kaltmiete",
        "warmmiete",
        "nebenkosten",
        "heizkostenEnthalten",
        "heizkosten",
        "zzgMehrwertsteuer",
        "mietzuschlaege",
        "hauptmietzinsnetto",
        "pauschalmiete",
        "betriebskostennetto",
        "evbnetto",
        "gesamtmietenetto",
        "gesamtmietebrutto",
        "gesamtbelastungnetto",
        "gesamtbelastungbrutto",
        "gesamtkostenprom2Von",
        "heizkostennetto",
        "monatlichekostennetto",
        "monatlichekostenbrutto",
        "nebenkostenprom2Von",
        "ruecklagenetto",
        "sonstigekostennetto",
        "sonstigemietenetto",
        "summemietenetto",
        "nettomieteprom2Von",
        "pacht",
        "erbpacht",
        "hausgeld",
        "abstand",
        "preisZeitraumVon",
        "preisZeitraumBis",
        "preisZeiteinheit",
        "mietpreisProQm",
        "kaufpreisProQm",
        "provisionspflichtig",
        "provisionTeilen",
        "innenCourtage",
        "aussenCourtage",
        "courtageHinweis",
        "provisionnetto",
        "provisionbrutto",
        "waehrung",
        "mwstSatz",
        "mwstGesamt",
        "freitextPreis",
        "xFache",
        "nettorendite",
        "nettorenditeSoll",
        "nettorenditeIst",
        "mieteinnahmenIst",
        "mieteinnahmenSoll",
        "erschliessungskosten",
        "kaution",
        "kautionText",
        "geschaeftsguthaben",
        "stpCarport",
        "stpDuplex",
        "stpFreiplatz",
        "stpGarage",
        "stpParkhaus",
        "stpTiefgarage",
        "stpSonstige",
        "richtpreis",
        "richtpreisprom2",
        "userDefinedSimplefield",
        "userDefinedAnyfield",
        "userDefinedExtend"
})
@XmlRootElement(name = "preise")
public class Preise {

    protected Kaufpreis kaufpreis;

    protected Kaufpreisnetto kaufpreisnetto;

    protected BigDecimal kaufpreisbrutto;

    protected BigDecimal nettokaltmiete;

    protected BigDecimal kaltmiete;

    protected BigDecimal warmmiete;

    protected BigDecimal nebenkosten;

    @XmlElement(name = "heizkosten_enthalten")
    protected Boolean heizkostenEnthalten;

    protected BigDecimal heizkosten;

    @XmlElement(name = "zzg_mehrwertsteuer")
    protected Boolean zzgMehrwertsteuer;

    protected BigDecimal mietzuschlaege;

    protected Hauptmietzinsnetto hauptmietzinsnetto;

    protected BigDecimal pauschalmiete;

    protected Betriebskostennetto betriebskostennetto;

    protected Evbnetto evbnetto;

    protected Gesamtmietenetto gesamtmietenetto;

    protected BigDecimal gesamtmietebrutto;

    protected Gesamtbelastungnetto gesamtbelastungnetto;

    protected BigDecimal gesamtbelastungbrutto;

    @XmlElement(name = "gesamtkostenprom2von")
    protected Gesamtkostenprom2Von gesamtkostenprom2Von;

    protected Heizkostennetto heizkostennetto;

    protected Monatlichekostennetto monatlichekostennetto;

    protected BigDecimal monatlichekostenbrutto;

    @XmlElement(name = "nebenkostenprom2von")
    protected Nebenkostenprom2Von nebenkostenprom2Von;

    protected Ruecklagenetto ruecklagenetto;

    protected Sonstigekostennetto sonstigekostennetto;

    protected Sonstigemietenetto sonstigemietenetto;

    protected Summemietenetto summemietenetto;

    @XmlElement(name = "nettomieteprom2von")
    protected Nettomieteprom2Von nettomieteprom2Von;

    protected BigDecimal pacht;

    protected BigDecimal erbpacht;

    protected BigDecimal hausgeld;

    protected BigDecimal abstand;

    @XmlElement(name = "preis_zeitraum_von")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar preisZeitraumVon;

    @XmlElement(name = "preis_zeitraum_bis")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar preisZeitraumBis;

    @XmlElement(name = "preis_zeiteinheit")
    protected PreisZeiteinheit preisZeiteinheit;

    @XmlElement(name = "mietpreis_pro_qm")
    protected BigDecimal mietpreisProQm;

    @XmlElement(name = "kaufpreis_pro_qm")
    protected BigDecimal kaufpreisProQm;

    protected Boolean provisionspflichtig;

    @XmlElement(name = "provision_teilen")
    protected ProvisionTeilen provisionTeilen;

    @XmlElement(name = "innen_courtage")
    protected InnenCourtage innenCourtage;

    @XmlElement(name = "aussen_courtage")
    protected AussenCourtage aussenCourtage;

    @XmlElement(name = "courtage_hinweis")
    protected String courtageHinweis;

    protected Provisionnetto provisionnetto;

    protected BigDecimal provisionbrutto;

    protected Waehrung waehrung;

    @XmlElement(name = "mwst_satz")
    protected BigDecimal mwstSatz;

    @XmlElement(name = "mwst_gesamt")
    protected BigDecimal mwstGesamt;

    @XmlElement(name = "freitext_preis")
    protected String freitextPreis;

    @XmlElement(name = "x_fache")
    protected String xFache;

    protected BigDecimal nettorendite;

    @XmlElement(name = "nettorendite_soll")
    protected BigDecimal nettorenditeSoll;

    @XmlElement(name = "nettorendite_ist")
    protected BigDecimal nettorenditeIst;

    @XmlElement(name = "mieteinnahmen_ist")
    protected MieteinnahmenIst mieteinnahmenIst;

    @XmlElement(name = "mieteinnahmen_soll")
    protected MieteinnahmenSoll mieteinnahmenSoll;

    protected BigDecimal erschliessungskosten;

    protected BigDecimal kaution;

    @XmlElement(name = "kaution_text")
    protected String kautionText;

    protected BigDecimal geschaeftsguthaben;

    @XmlElement(name = "stp_carport")
    protected Stellplatz stpCarport;

    @XmlElement(name = "stp_duplex")
    protected Stellplatz stpDuplex;

    @XmlElement(name = "stp_freiplatz")
    protected Stellplatz stpFreiplatz;

    @XmlElement(name = "stp_garage")
    protected Stellplatz stpGarage;

    @XmlElement(name = "stp_parkhaus")
    protected Stellplatz stpParkhaus;

    @XmlElement(name = "stp_tiefgarage")
    protected Stellplatz stpTiefgarage;

    @XmlElement(name = "stp_sonstige")
    protected List<StpSonstige> stpSonstige;

    protected BigDecimal richtpreis;

    protected BigDecimal richtpreisprom2;

    @XmlElement(name = "user_defined_simplefield")
    protected List<UserDefinedSimplefield> userDefinedSimplefield;

    @XmlElement(name = "user_defined_anyfield")
    protected List<UserDefinedAnyfield> userDefinedAnyfield;

    @XmlElement(name = "user_defined_extend")
    protected List<UserDefinedExtend> userDefinedExtend;

    /**
     * Ruft den Wert der kaufpreis-Eigenschaft ab.
     *
     * @return possible object is {@link Kaufpreis }
     */
    public Kaufpreis getKaufpreis() {
        return kaufpreis;
    }

    /**
     * Legt den Wert der kaufpreis-Eigenschaft fest.
     *
     * @param value allowed object is {@link Kaufpreis }
     */
    public void setKaufpreis(Kaufpreis value) {
        this.kaufpreis = value;
    }

    /**
     * Ruft den Wert der kaufpreisnetto-Eigenschaft ab.
     *
     * @return possible object is {@link Kaufpreisnetto }
     */
    public Kaufpreisnetto getKaufpreisnetto() {
        return kaufpreisnetto;
    }

    /**
     * Legt den Wert der kaufpreisnetto-Eigenschaft fest.
     *
     * @param value allowed object is {@link Kaufpreisnetto }
     */
    public void setKaufpreisnetto(Kaufpreisnetto value) {
        this.kaufpreisnetto = value;
    }

    /**
     * Ruft den Wert der kaufpreisbrutto-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getKaufpreisbrutto() {
        return kaufpreisbrutto;
    }

    /**
     * Legt den Wert der kaufpreisbrutto-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setKaufpreisbrutto(BigDecimal value) {
        this.kaufpreisbrutto = value;
    }

    /**
     * Ruft den Wert der nettokaltmiete-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getNettokaltmiete() {
        return nettokaltmiete;
    }

    /**
     * Legt den Wert der nettokaltmiete-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setNettokaltmiete(BigDecimal value) {
        this.nettokaltmiete = value;
    }

    /**
     * Ruft den Wert der kaltmiete-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getKaltmiete() {
        return kaltmiete;
    }

    /**
     * Legt den Wert der kaltmiete-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setKaltmiete(BigDecimal value) {
        this.kaltmiete = value;
    }

    /**
     * Ruft den Wert der warmmiete-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getWarmmiete() {
        return warmmiete;
    }

    /**
     * Legt den Wert der warmmiete-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setWarmmiete(BigDecimal value) {
        this.warmmiete = value;
    }

    /**
     * Ruft den Wert der nebenkosten-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getNebenkosten() {
        return nebenkosten;
    }

    /**
     * Legt den Wert der nebenkosten-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setNebenkosten(BigDecimal value) {
        this.nebenkosten = value;
    }

    /**
     * Ruft den Wert der heizkostenEnthalten-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isHeizkostenEnthalten() {
        return heizkostenEnthalten;
    }

    /**
     * Legt den Wert der heizkostenEnthalten-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setHeizkostenEnthalten(Boolean value) {
        this.heizkostenEnthalten = value;
    }

    /**
     * Ruft den Wert der heizkosten-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getHeizkosten() {
        return heizkosten;
    }

    /**
     * Legt den Wert der heizkosten-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setHeizkosten(BigDecimal value) {
        this.heizkosten = value;
    }

    /**
     * Ruft den Wert der zzgMehrwertsteuer-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isZzgMehrwertsteuer() {
        return zzgMehrwertsteuer;
    }

    /**
     * Legt den Wert der zzgMehrwertsteuer-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setZzgMehrwertsteuer(Boolean value) {
        this.zzgMehrwertsteuer = value;
    }

    /**
     * Ruft den Wert der mietzuschlaege-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getMietzuschlaege() {
        return mietzuschlaege;
    }

    /**
     * Legt den Wert der mietzuschlaege-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setMietzuschlaege(BigDecimal value) {
        this.mietzuschlaege = value;
    }

    /**
     * Ruft den Wert der hauptmietzinsnetto-Eigenschaft ab.
     *
     * @return possible object is {@link Hauptmietzinsnetto }
     */
    public Hauptmietzinsnetto getHauptmietzinsnetto() {
        return hauptmietzinsnetto;
    }

    /**
     * Legt den Wert der hauptmietzinsnetto-Eigenschaft fest.
     *
     * @param value allowed object is {@link Hauptmietzinsnetto }
     */
    public void setHauptmietzinsnetto(Hauptmietzinsnetto value) {
        this.hauptmietzinsnetto = value;
    }

    /**
     * Ruft den Wert der pauschalmiete-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getPauschalmiete() {
        return pauschalmiete;
    }

    /**
     * Legt den Wert der pauschalmiete-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setPauschalmiete(BigDecimal value) {
        this.pauschalmiete = value;
    }

    /**
     * Ruft den Wert der betriebskostennetto-Eigenschaft ab.
     *
     * @return possible object is {@link Betriebskostennetto }
     */
    public Betriebskostennetto getBetriebskostennetto() {
        return betriebskostennetto;
    }

    /**
     * Legt den Wert der betriebskostennetto-Eigenschaft fest.
     *
     * @param value allowed object is {@link Betriebskostennetto }
     */
    public void setBetriebskostennetto(Betriebskostennetto value) {
        this.betriebskostennetto = value;
    }

    /**
     * Ruft den Wert der evbnetto-Eigenschaft ab.
     *
     * @return possible object is {@link Evbnetto }
     */
    public Evbnetto getEvbnetto() {
        return evbnetto;
    }

    /**
     * Legt den Wert der evbnetto-Eigenschaft fest.
     *
     * @param value allowed object is {@link Evbnetto }
     */
    public void setEvbnetto(Evbnetto value) {
        this.evbnetto = value;
    }

    /**
     * Ruft den Wert der gesamtmietenetto-Eigenschaft ab.
     *
     * @return possible object is {@link Gesamtmietenetto }
     */
    public Gesamtmietenetto getGesamtmietenetto() {
        return gesamtmietenetto;
    }

    /**
     * Legt den Wert der gesamtmietenetto-Eigenschaft fest.
     *
     * @param value allowed object is {@link Gesamtmietenetto }
     */
    public void setGesamtmietenetto(Gesamtmietenetto value) {
        this.gesamtmietenetto = value;
    }

    /**
     * Ruft den Wert der gesamtmietebrutto-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getGesamtmietebrutto() {
        return gesamtmietebrutto;
    }

    /**
     * Legt den Wert der gesamtmietebrutto-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setGesamtmietebrutto(BigDecimal value) {
        this.gesamtmietebrutto = value;
    }

    /**
     * Ruft den Wert der gesamtbelastungnetto-Eigenschaft ab.
     *
     * @return possible object is {@link Gesamtbelastungnetto }
     */
    public Gesamtbelastungnetto getGesamtbelastungnetto() {
        return gesamtbelastungnetto;
    }

    /**
     * Legt den Wert der gesamtbelastungnetto-Eigenschaft fest.
     *
     * @param value allowed object is {@link Gesamtbelastungnetto }
     */
    public void setGesamtbelastungnetto(Gesamtbelastungnetto value) {
        this.gesamtbelastungnetto = value;
    }

    /**
     * Ruft den Wert der gesamtbelastungbrutto-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getGesamtbelastungbrutto() {
        return gesamtbelastungbrutto;
    }

    /**
     * Legt den Wert der gesamtbelastungbrutto-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setGesamtbelastungbrutto(BigDecimal value) {
        this.gesamtbelastungbrutto = value;
    }

    /**
     * Ruft den Wert der gesamtkostenprom2Von-Eigenschaft ab.
     *
     * @return possible object is {@link Gesamtkostenprom2Von }
     */
    public Gesamtkostenprom2Von getGesamtkostenprom2Von() {
        return gesamtkostenprom2Von;
    }

    /**
     * Legt den Wert der gesamtkostenprom2Von-Eigenschaft fest.
     *
     * @param value allowed object is {@link Gesamtkostenprom2Von }
     */
    public void setGesamtkostenprom2Von(Gesamtkostenprom2Von value) {
        this.gesamtkostenprom2Von = value;
    }

    /**
     * Ruft den Wert der heizkostennetto-Eigenschaft ab.
     *
     * @return possible object is {@link Heizkostennetto }
     */
    public Heizkostennetto getHeizkostennetto() {
        return heizkostennetto;
    }

    /**
     * Legt den Wert der heizkostennetto-Eigenschaft fest.
     *
     * @param value allowed object is {@link Heizkostennetto }
     */
    public void setHeizkostennetto(Heizkostennetto value) {
        this.heizkostennetto = value;
    }

    /**
     * Ruft den Wert der monatlichekostennetto-Eigenschaft ab.
     *
     * @return possible object is {@link Monatlichekostennetto }
     */
    public Monatlichekostennetto getMonatlichekostennetto() {
        return monatlichekostennetto;
    }

    /**
     * Legt den Wert der monatlichekostennetto-Eigenschaft fest.
     *
     * @param value allowed object is {@link Monatlichekostennetto }
     */
    public void setMonatlichekostennetto(Monatlichekostennetto value) {
        this.monatlichekostennetto = value;
    }

    /**
     * Ruft den Wert der monatlichekostenbrutto-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getMonatlichekostenbrutto() {
        return monatlichekostenbrutto;
    }

    /**
     * Legt den Wert der monatlichekostenbrutto-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setMonatlichekostenbrutto(BigDecimal value) {
        this.monatlichekostenbrutto = value;
    }

    /**
     * Ruft den Wert der nebenkostenprom2Von-Eigenschaft ab.
     *
     * @return possible object is {@link Nebenkostenprom2Von }
     */
    public Nebenkostenprom2Von getNebenkostenprom2Von() {
        return nebenkostenprom2Von;
    }

    /**
     * Legt den Wert der nebenkostenprom2Von-Eigenschaft fest.
     *
     * @param value allowed object is {@link Nebenkostenprom2Von }
     */
    public void setNebenkostenprom2Von(Nebenkostenprom2Von value) {
        this.nebenkostenprom2Von = value;
    }

    /**
     * Ruft den Wert der ruecklagenetto-Eigenschaft ab.
     *
     * @return possible object is {@link Ruecklagenetto }
     */
    public Ruecklagenetto getRuecklagenetto() {
        return ruecklagenetto;
    }

    /**
     * Legt den Wert der ruecklagenetto-Eigenschaft fest.
     *
     * @param value allowed object is {@link Ruecklagenetto }
     */
    public void setRuecklagenetto(Ruecklagenetto value) {
        this.ruecklagenetto = value;
    }

    /**
     * Ruft den Wert der sonstigekostennetto-Eigenschaft ab.
     *
     * @return possible object is {@link Sonstigekostennetto }
     */
    public Sonstigekostennetto getSonstigekostennetto() {
        return sonstigekostennetto;
    }

    /**
     * Legt den Wert der sonstigekostennetto-Eigenschaft fest.
     *
     * @param value allowed object is {@link Sonstigekostennetto }
     */
    public void setSonstigekostennetto(Sonstigekostennetto value) {
        this.sonstigekostennetto = value;
    }

    /**
     * Ruft den Wert der sonstigemietenetto-Eigenschaft ab.
     *
     * @return possible object is {@link Sonstigemietenetto }
     */
    public Sonstigemietenetto getSonstigemietenetto() {
        return sonstigemietenetto;
    }

    /**
     * Legt den Wert der sonstigemietenetto-Eigenschaft fest.
     *
     * @param value allowed object is {@link Sonstigemietenetto }
     */
    public void setSonstigemietenetto(Sonstigemietenetto value) {
        this.sonstigemietenetto = value;
    }

    /**
     * Ruft den Wert der summemietenetto-Eigenschaft ab.
     *
     * @return possible object is {@link Summemietenetto }
     */
    public Summemietenetto getSummemietenetto() {
        return summemietenetto;
    }

    /**
     * Legt den Wert der summemietenetto-Eigenschaft fest.
     *
     * @param value allowed object is {@link Summemietenetto }
     */
    public void setSummemietenetto(Summemietenetto value) {
        this.summemietenetto = value;
    }

    /**
     * Ruft den Wert der nettomieteprom2Von-Eigenschaft ab.
     *
     * @return possible object is {@link Nettomieteprom2Von }
     */
    public Nettomieteprom2Von getNettomieteprom2Von() {
        return nettomieteprom2Von;
    }

    /**
     * Legt den Wert der nettomieteprom2Von-Eigenschaft fest.
     *
     * @param value allowed object is {@link Nettomieteprom2Von }
     */
    public void setNettomieteprom2Von(Nettomieteprom2Von value) {
        this.nettomieteprom2Von = value;
    }

    /**
     * Ruft den Wert der pacht-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getPacht() {
        return pacht;
    }

    /**
     * Legt den Wert der pacht-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setPacht(BigDecimal value) {
        this.pacht = value;
    }

    /**
     * Ruft den Wert der erbpacht-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getErbpacht() {
        return erbpacht;
    }

    /**
     * Legt den Wert der erbpacht-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setErbpacht(BigDecimal value) {
        this.erbpacht = value;
    }

    /**
     * Ruft den Wert der hausgeld-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getHausgeld() {
        return hausgeld;
    }

    /**
     * Legt den Wert der hausgeld-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setHausgeld(BigDecimal value) {
        this.hausgeld = value;
    }

    /**
     * Ruft den Wert der abstand-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getAbstand() {
        return abstand;
    }

    /**
     * Legt den Wert der abstand-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setAbstand(BigDecimal value) {
        this.abstand = value;
    }

    /**
     * Ruft den Wert der preisZeitraumVon-Eigenschaft ab.
     *
     * @return possible object is {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getPreisZeitraumVon() {
        return preisZeitraumVon;
    }

    /**
     * Legt den Wert der preisZeitraumVon-Eigenschaft fest.
     *
     * @param value allowed object is {@link XMLGregorianCalendar }
     */
    public void setPreisZeitraumVon(XMLGregorianCalendar value) {
        this.preisZeitraumVon = value;
    }

    /**
     * Ruft den Wert der preisZeitraumBis-Eigenschaft ab.
     *
     * @return possible object is {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getPreisZeitraumBis() {
        return preisZeitraumBis;
    }

    /**
     * Legt den Wert der preisZeitraumBis-Eigenschaft fest.
     *
     * @param value allowed object is {@link XMLGregorianCalendar }
     */
    public void setPreisZeitraumBis(XMLGregorianCalendar value) {
        this.preisZeitraumBis = value;
    }

    /**
     * Ruft den Wert der preisZeiteinheit-Eigenschaft ab.
     *
     * @return possible object is {@link PreisZeiteinheit }
     */
    public PreisZeiteinheit getPreisZeiteinheit() {
        return preisZeiteinheit;
    }

    /**
     * Legt den Wert der preisZeiteinheit-Eigenschaft fest.
     *
     * @param value allowed object is {@link PreisZeiteinheit }
     */
    public void setPreisZeiteinheit(PreisZeiteinheit value) {
        this.preisZeiteinheit = value;
    }

    /**
     * Ruft den Wert der mietpreisProQm-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getMietpreisProQm() {
        return mietpreisProQm;
    }

    /**
     * Legt den Wert der mietpreisProQm-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setMietpreisProQm(BigDecimal value) {
        this.mietpreisProQm = value;
    }

    /**
     * Ruft den Wert der kaufpreisProQm-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getKaufpreisProQm() {
        return kaufpreisProQm;
    }

    /**
     * Legt den Wert der kaufpreisProQm-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setKaufpreisProQm(BigDecimal value) {
        this.kaufpreisProQm = value;
    }

    /**
     * Ruft den Wert der provisionspflichtig-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isProvisionspflichtig() {
        return provisionspflichtig;
    }

    /**
     * Legt den Wert der provisionspflichtig-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setProvisionspflichtig(Boolean value) {
        this.provisionspflichtig = value;
    }

    /**
     * Ruft den Wert der provisionTeilen-Eigenschaft ab.
     *
     * @return possible object is {@link ProvisionTeilen }
     */
    public ProvisionTeilen getProvisionTeilen() {
        return provisionTeilen;
    }

    /**
     * Legt den Wert der provisionTeilen-Eigenschaft fest.
     *
     * @param value allowed object is {@link ProvisionTeilen }
     */
    public void setProvisionTeilen(ProvisionTeilen value) {
        this.provisionTeilen = value;
    }

    /**
     * Ruft den Wert der innenCourtage-Eigenschaft ab.
     *
     * @return possible object is {@link InnenCourtage }
     */
    public InnenCourtage getInnenCourtage() {
        return innenCourtage;
    }

    /**
     * Legt den Wert der innenCourtage-Eigenschaft fest.
     *
     * @param value allowed object is {@link InnenCourtage }
     */
    public void setInnenCourtage(InnenCourtage value) {
        this.innenCourtage = value;
    }

    /**
     * Ruft den Wert der aussenCourtage-Eigenschaft ab.
     *
     * @return possible object is {@link AussenCourtage }
     */
    public AussenCourtage getAussenCourtage() {
        return aussenCourtage;
    }

    /**
     * Legt den Wert der aussenCourtage-Eigenschaft fest.
     *
     * @param value allowed object is {@link AussenCourtage }
     */
    public void setAussenCourtage(AussenCourtage value) {
        this.aussenCourtage = value;
    }

    /**
     * Ruft den Wert der courtageHinweis-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getCourtageHinweis() {
        return courtageHinweis;
    }

    /**
     * Legt den Wert der courtageHinweis-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setCourtageHinweis(String value) {
        this.courtageHinweis = value;
    }

    /**
     * Ruft den Wert der provisionnetto-Eigenschaft ab.
     *
     * @return possible object is {@link Provisionnetto }
     */
    public Provisionnetto getProvisionnetto() {
        return provisionnetto;
    }

    /**
     * Legt den Wert der provisionnetto-Eigenschaft fest.
     *
     * @param value allowed object is {@link Provisionnetto }
     */
    public void setProvisionnetto(Provisionnetto value) {
        this.provisionnetto = value;
    }

    /**
     * Ruft den Wert der provisionbrutto-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getProvisionbrutto() {
        return provisionbrutto;
    }

    /**
     * Legt den Wert der provisionbrutto-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setProvisionbrutto(BigDecimal value) {
        this.provisionbrutto = value;
    }

    /**
     * Ruft den Wert der waehrung-Eigenschaft ab.
     *
     * @return possible object is {@link Waehrung }
     */
    public Waehrung getWaehrung() {
        return waehrung;
    }

    /**
     * Legt den Wert der waehrung-Eigenschaft fest.
     *
     * @param value allowed object is {@link Waehrung }
     */
    public void setWaehrung(Waehrung value) {
        this.waehrung = value;
    }

    /**
     * Ruft den Wert der mwstSatz-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getMwstSatz() {
        return mwstSatz;
    }

    /**
     * Legt den Wert der mwstSatz-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setMwstSatz(BigDecimal value) {
        this.mwstSatz = value;
    }

    /**
     * Ruft den Wert der mwstGesamt-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getMwstGesamt() {
        return mwstGesamt;
    }

    /**
     * Legt den Wert der mwstGesamt-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setMwstGesamt(BigDecimal value) {
        this.mwstGesamt = value;
    }

    /**
     * Ruft den Wert der freitextPreis-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getFreitextPreis() {
        return freitextPreis;
    }

    /**
     * Legt den Wert der freitextPreis-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setFreitextPreis(String value) {
        this.freitextPreis = value;
    }

    /**
     * Ruft den Wert der xFache-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getXFache() {
        return xFache;
    }

    /**
     * Legt den Wert der xFache-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setXFache(String value) {
        this.xFache = value;
    }

    /**
     * Ruft den Wert der nettorendite-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getNettorendite() {
        return nettorendite;
    }

    /**
     * Legt den Wert der nettorendite-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setNettorendite(BigDecimal value) {
        this.nettorendite = value;
    }

    /**
     * Ruft den Wert der nettorenditeSoll-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getNettorenditeSoll() {
        return nettorenditeSoll;
    }

    /**
     * Legt den Wert der nettorenditeSoll-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setNettorenditeSoll(BigDecimal value) {
        this.nettorenditeSoll = value;
    }

    /**
     * Ruft den Wert der nettorenditeIst-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getNettorenditeIst() {
        return nettorenditeIst;
    }

    /**
     * Legt den Wert der nettorenditeIst-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setNettorenditeIst(BigDecimal value) {
        this.nettorenditeIst = value;
    }

    /**
     * Ruft den Wert der mieteinnahmenIst-Eigenschaft ab.
     *
     * @return possible object is {@link MieteinnahmenIst }
     */
    public MieteinnahmenIst getMieteinnahmenIst() {
        return mieteinnahmenIst;
    }

    /**
     * Legt den Wert der mieteinnahmenIst-Eigenschaft fest.
     *
     * @param value allowed object is {@link MieteinnahmenIst }
     */
    public void setMieteinnahmenIst(MieteinnahmenIst value) {
        this.mieteinnahmenIst = value;
    }

    /**
     * Ruft den Wert der mieteinnahmenSoll-Eigenschaft ab.
     *
     * @return possible object is {@link MieteinnahmenSoll }
     */
    public MieteinnahmenSoll getMieteinnahmenSoll() {
        return mieteinnahmenSoll;
    }

    /**
     * Legt den Wert der mieteinnahmenSoll-Eigenschaft fest.
     *
     * @param value allowed object is {@link MieteinnahmenSoll }
     */
    public void setMieteinnahmenSoll(MieteinnahmenSoll value) {
        this.mieteinnahmenSoll = value;
    }

    /**
     * Ruft den Wert der erschliessungskosten-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getErschliessungskosten() {
        return erschliessungskosten;
    }

    /**
     * Legt den Wert der erschliessungskosten-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setErschliessungskosten(BigDecimal value) {
        this.erschliessungskosten = value;
    }

    /**
     * Ruft den Wert der kaution-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getKaution() {
        return kaution;
    }

    /**
     * Legt den Wert der kaution-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setKaution(BigDecimal value) {
        this.kaution = value;
    }

    /**
     * Ruft den Wert der kautionText-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getKautionText() {
        return kautionText;
    }

    /**
     * Legt den Wert der kautionText-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setKautionText(String value) {
        this.kautionText = value;
    }

    /**
     * Ruft den Wert der geschaeftsguthaben-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getGeschaeftsguthaben() {
        return geschaeftsguthaben;
    }

    /**
     * Legt den Wert der geschaeftsguthaben-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setGeschaeftsguthaben(BigDecimal value) {
        this.geschaeftsguthaben = value;
    }

    /**
     * Ruft den Wert der stpCarport-Eigenschaft ab.
     *
     * @return possible object is {@link Stellplatz }
     */
    public Stellplatz getStpCarport() {
        return stpCarport;
    }

    /**
     * Legt den Wert der stpCarport-Eigenschaft fest.
     *
     * @param value allowed object is {@link Stellplatz }
     */
    public void setStpCarport(Stellplatz value) {
        this.stpCarport = value;
    }

    /**
     * Ruft den Wert der stpDuplex-Eigenschaft ab.
     *
     * @return possible object is {@link Stellplatz }
     */
    public Stellplatz getStpDuplex() {
        return stpDuplex;
    }

    /**
     * Legt den Wert der stpDuplex-Eigenschaft fest.
     *
     * @param value allowed object is {@link Stellplatz }
     */
    public void setStpDuplex(Stellplatz value) {
        this.stpDuplex = value;
    }

    /**
     * Ruft den Wert der stpFreiplatz-Eigenschaft ab.
     *
     * @return possible object is {@link Stellplatz }
     */
    public Stellplatz getStpFreiplatz() {
        return stpFreiplatz;
    }

    /**
     * Legt den Wert der stpFreiplatz-Eigenschaft fest.
     *
     * @param value allowed object is {@link Stellplatz }
     */
    public void setStpFreiplatz(Stellplatz value) {
        this.stpFreiplatz = value;
    }

    /**
     * Ruft den Wert der stpGarage-Eigenschaft ab.
     *
     * @return possible object is {@link Stellplatz }
     */
    public Stellplatz getStpGarage() {
        return stpGarage;
    }

    /**
     * Legt den Wert der stpGarage-Eigenschaft fest.
     *
     * @param value allowed object is {@link Stellplatz }
     */
    public void setStpGarage(Stellplatz value) {
        this.stpGarage = value;
    }

    /**
     * Ruft den Wert der stpParkhaus-Eigenschaft ab.
     *
     * @return possible object is {@link Stellplatz }
     */
    public Stellplatz getStpParkhaus() {
        return stpParkhaus;
    }

    /**
     * Legt den Wert der stpParkhaus-Eigenschaft fest.
     *
     * @param value allowed object is {@link Stellplatz }
     */
    public void setStpParkhaus(Stellplatz value) {
        this.stpParkhaus = value;
    }

    /**
     * Ruft den Wert der stpTiefgarage-Eigenschaft ab.
     *
     * @return possible object is {@link Stellplatz }
     */
    public Stellplatz getStpTiefgarage() {
        return stpTiefgarage;
    }

    /**
     * Legt den Wert der stpTiefgarage-Eigenschaft fest.
     *
     * @param value allowed object is {@link Stellplatz }
     */
    public void setStpTiefgarage(Stellplatz value) {
        this.stpTiefgarage = value;
    }

    /**
     * Gets the value of the stpSonstige property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the stpSonstige property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStpSonstige().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link StpSonstige }
     */
    public List<StpSonstige> getStpSonstige() {
        if (stpSonstige == null) {
            stpSonstige = new ArrayList<>();
        }
        return this.stpSonstige;
    }

    /**
     * Ruft den Wert der richtpreis-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getRichtpreis() {
        return richtpreis;
    }

    /**
     * Legt den Wert der richtpreis-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setRichtpreis(BigDecimal value) {
        this.richtpreis = value;
    }

    /**
     * Ruft den Wert der richtpreisprom2-Eigenschaft ab.
     *
     * @return possible object is {@link BigDecimal }
     */
    public BigDecimal getRichtpreisprom2() {
        return richtpreisprom2;
    }

    /**
     * Legt den Wert der richtpreisprom2-Eigenschaft fest.
     *
     * @param value allowed object is {@link BigDecimal }
     */
    public void setRichtpreisprom2(BigDecimal value) {
        this.richtpreisprom2 = value;
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
