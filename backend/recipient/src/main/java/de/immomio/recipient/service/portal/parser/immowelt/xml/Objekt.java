package de.immomio.recipient.service.portal.parser.immowelt.xml;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Johannes Hiemer.
 */
public class Objekt {

    private String datum;

    private String referenzNummer;

    private String maklerNummer;

    private String link;

    private String vertrieb;

    private String stichwort;

    private String strasse;

    private String ort;

    private String preis;

    private String waehrung;

    @XmlElement(name = "o_datum")
    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    @XmlElement(name = "o_refnr")
    public String getReferenzNummer() {
        return referenzNummer;
    }

    public void setReferenzNummer(String referenzNummer) {
        this.referenzNummer = referenzNummer;
    }

    @XmlElement(name = "o_maklernr")
    public String getMaklerNummer() {
        return maklerNummer;
    }

    public void setMaklerNummer(String maklerNummer) {
        this.maklerNummer = maklerNummer;
    }

    @XmlElement(name = "o_link")
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @XmlElement(name = "o_vertrieb")
    public String getVertrieb() {
        return vertrieb;
    }

    public void setVertrieb(String vertrieb) {
        this.vertrieb = vertrieb;
    }

    @XmlElement(name = "o_stichwort")
    public String getStichwort() {
        return stichwort;
    }

    public void setStichwort(String stichwort) {
        this.stichwort = stichwort;
    }

    @XmlElement(name = "o_strasse")
    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    @XmlElement(name = "o_ort")
    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    @XmlElement(name = "o_preis")
    public String getPreis() {
        return preis;
    }

    public void setPreis(String preis) {
        this.preis = preis;
    }

    @XmlElement(name = "o_LW")
    public String getWaehrung() {
        return waehrung;
    }

    public void setWaehrung(String waehrung) {
        this.waehrung = waehrung;
    }

}
