package de.immomio.recipient.service.portal.parser.immowelt.xml;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Johannes Hiemer.
 */
public class Interessent {

    private String anrede;

    private String vorname;

    private String name;

    private String firma;

    private String strasse;

    private String plz;

    private String ort;

    private String tel;

    private String fax;

    private String mobil;

    private String email;

    private String info;

    @XmlElement(name = "i_anrede")
    public String getAnrede() {
        return anrede;
    }

    public void setAnrede(String anrede) {
        this.anrede = anrede;
    }

    @XmlElement(name = "i_vorname")
    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    @XmlElement(name = "i_name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "i_firma")
    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    @XmlElement(name = "i_strasse")
    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    @XmlElement(name = "i_plz")
    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    @XmlElement(name = "i_ort")
    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    @XmlElement(name = "i_tel")
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @XmlElement(name = "i_fax")
    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @XmlElement(name = "i_mobil")
    public String getMobil() {
        return mobil;
    }

    public void setMobil(String mobil) {
        this.mobil = mobil;
    }

    @XmlElement(name = "i_email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @XmlElement(name = "i_info")
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}
