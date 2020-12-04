package de.immomio.recipient.service.portal.parser.openimmo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * @author Bastian Bliemeister.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "openimmo_feedback")
public class OpenimmoFeedback {

    @XmlElement(name = "sender")
    private OpenimmoSender sender;

    @XmlElement(name = "objekt")
    private OpenimmoObjekt objekt;

    public OpenimmoSender getSender() {
        return sender;
    }

    public void setSender(OpenimmoSender sender) {
        this.sender = sender;
    }

    public OpenimmoObjekt getObjekt() {
        return objekt;
    }

    public void setObjekt(OpenimmoObjekt objekt) {
        this.objekt = objekt;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class OpenimmoSender {

        @XmlElement(name = "name")
        private String name;

        @XmlElement(name = "datum")
        private Date datum;

        @XmlElement(name = "makler_id")
        private String maklerId;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getDatum() {
            return datum;
        }

        public void setDatum(Date datum) {
            this.datum = datum;
        }

        public String getMaklerId() {
            return maklerId;
        }

        public void setMaklerId(String maklerId) {
            this.maklerId = maklerId;
        }

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class OpenimmoObjekt {

        @XmlElement(name = "portal_unique_id")
        private String portalUniqueId;

        @XmlElement(name = "portal_obj_id")
        private String portalObjektId;

        @XmlElement(name = "oobj_id")
        private String oobjId;

        @XmlElement(name = "anbieter_id")
        private String anbieterId;

        @XmlElement(name = "expose_url")
        private String exposeUrl;

        @XmlElement(name = "bezeichnung")
        private String bezeichnung;

        @XmlElement(name = "strasse")
        private String strasse;

        @XmlElement(name = "ort")
        private String ort;

        @XmlElement(name = "preis")
        private String preis;

        @XmlElement(name = "interessent")
        private OpenimmoInteressent interessent;

        public String getPortalUniqueId() {
            return portalUniqueId;
        }

        public void setPortalUniqueId(String portalUniqueId) {
            this.portalUniqueId = portalUniqueId;
        }

        public String getPortalObjektId() {
            return portalObjektId;
        }

        public void setPortalObjektId(String portalObjektId) {
            this.portalObjektId = portalObjektId;
        }

        public String getOobjId() {
            return oobjId;
        }

        public void setOobjId(String oobjId) {
            this.oobjId = oobjId;
        }

        public String getAnbieterId() {
            return anbieterId;
        }

        public void setAnbieterId(String anbieterId) {
            this.anbieterId = anbieterId;
        }

        public String getExposeUrl() {
            return exposeUrl;
        }

        public void setExposeUrl(String exposeUrl) {
            this.exposeUrl = exposeUrl;
        }

        public String getBezeichnung() {
            return bezeichnung;
        }

        public void setBezeichnung(String bezeichnung) {
            this.bezeichnung = bezeichnung;
        }

        public String getStrasse() {
            return strasse;
        }

        public void setStrasse(String strasse) {
            this.strasse = strasse;
        }

        public String getOrt() {
            return ort;
        }

        public void setOrt(String ort) {
            this.ort = ort;
        }

        public String getPreis() {
            return preis;
        }

        public void setPreis(String preis) {
            this.preis = preis;
        }

        public OpenimmoInteressent getInteressent() {
            return interessent;
        }

        public void setInteressent(OpenimmoInteressent interessent) {
            this.interessent = interessent;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        public static class OpenimmoInteressent {

            @XmlElement(name = "anrede")
            private String anrede;

            @XmlElement(name = "vorname")
            private String vorname;

            @XmlElement(name = "nachname")
            private String nachname;

            @XmlElement(name = "tel")
            private String tel;

            @XmlElement(name = "email")
            private String email;

            @XmlElement(name = "anfrage")
            private String anfrage;

            public String getAnrede() {
                return anrede;
            }

            public void setAnrede(String anrede) {
                this.anrede = anrede;
            }

            public String getVorname() {
                return vorname;
            }

            public void setVorname(String vorname) {
                this.vorname = vorname;
            }

            public String getNachname() {
                return nachname;
            }

            public void setNachname(String nachname) {
                this.nachname = nachname;
            }

            public String getTel() {
                return tel;
            }

            public void setTel(String tel) {
                this.tel = tel;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getAnfrage() {
                return anfrage;
            }

            public void setAnfrage(String anfrage) {
                this.anfrage = anfrage;
            }
        }
    }
}