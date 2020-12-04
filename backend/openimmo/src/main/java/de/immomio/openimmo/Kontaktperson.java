//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.10.16 um 11:10:09 AM CEST 
//

package de.immomio.openimmo;

import javax.xml.bind.JAXBElement;
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
 *             &lt;element ref="{}email_zentrale"/>
 *             &lt;element ref="{}email_direkt" minOccurs="0"/>
 *             &lt;element ref="{}tel_zentrale" minOccurs="0"/>
 *             &lt;element ref="{}tel_durchw" minOccurs="0"/>
 *             &lt;element ref="{}tel_fax" minOccurs="0"/>
 *             &lt;element ref="{}tel_handy" minOccurs="0"/>
 *           &lt;/sequence>
 *           &lt;sequence>
 *             &lt;element ref="{}email_direkt"/>
 *             &lt;element ref="{}tel_zentrale" minOccurs="0"/>
 *             &lt;element ref="{}tel_durchw" minOccurs="0"/>
 *             &lt;element ref="{}tel_fax" minOccurs="0"/>
 *             &lt;element ref="{}tel_handy" minOccurs="0"/>
 *           &lt;/sequence>
 *           &lt;sequence>
 *             &lt;element ref="{}tel_zentrale"/>
 *             &lt;element ref="{}tel_durchw" minOccurs="0"/>
 *             &lt;element ref="{}tel_fax" minOccurs="0"/>
 *             &lt;element ref="{}tel_handy" minOccurs="0"/>
 *           &lt;/sequence>
 *           &lt;sequence>
 *             &lt;element ref="{}tel_durchw"/>
 *             &lt;element ref="{}tel_fax" minOccurs="0"/>
 *             &lt;element ref="{}tel_handy" minOccurs="0"/>
 *           &lt;/sequence>
 *           &lt;sequence>
 *             &lt;element ref="{}tel_fax"/>
 *             &lt;element ref="{}tel_handy" minOccurs="0"/>
 *           &lt;/sequence>
 *           &lt;element ref="{}tel_handy"/>
 *         &lt;/choice>
 *         &lt;element ref="{}name"/>
 *         &lt;element ref="{}vorname" minOccurs="0"/>
 *         &lt;element ref="{}titel" minOccurs="0"/>
 *         &lt;element ref="{}anrede" minOccurs="0"/>
 *         &lt;element ref="{}position" minOccurs="0"/>
 *         &lt;element ref="{}anrede_brief" minOccurs="0"/>
 *         &lt;element ref="{}firma" minOccurs="0"/>
 *         &lt;element ref="{}zusatzfeld" minOccurs="0"/>
 *         &lt;element ref="{}strasse" minOccurs="0"/>
 *         &lt;element ref="{}hausnummer" minOccurs="0"/>
 *         &lt;element ref="{}plz" minOccurs="0"/>
 *         &lt;element ref="{}ort" minOccurs="0"/>
 *         &lt;element ref="{}postfach" minOccurs="0"/>
 *         &lt;element ref="{}postf_plz" minOccurs="0"/>
 *         &lt;element ref="{}postf_ort" minOccurs="0"/>
 *         &lt;element ref="{}land" minOccurs="0"/>
 *         &lt;element ref="{}email_privat" minOccurs="0"/>
 *         &lt;element ref="{}email_sonstige" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}email_feedback" minOccurs="0"/>
 *         &lt;element ref="{}tel_privat" minOccurs="0"/>
 *         &lt;element ref="{}tel_sonstige" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}url" minOccurs="0"/>
 *         &lt;element ref="{}adressfreigabe" minOccurs="0"/>
 *         &lt;element ref="{}personennummer" minOccurs="0"/>
 *         &lt;element ref="{}immobilientreuhaenderid" minOccurs="0"/>
 *         &lt;element ref="{}foto" minOccurs="0"/>
 *         &lt;element ref="{}freitextfeld" minOccurs="0"/>
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
        "content",
        "email_zentrale",
        "email_direkt",
        "name",
        "vorname",
        "firma"
})
@XmlRootElement(name = "kontaktperson")
public class Kontaktperson {

    @XmlElementRefs({
            @XmlElementRef(name = "anrede_brief", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "user_defined_anyfield", type = UserDefinedAnyfield.class, required = false),
            @XmlElementRef(name = "tel_handy", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "vorname", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "email_feedback", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "ort", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "url", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "email_zentrale", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "email_privat", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "tel_fax", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "zusatzfeld", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "tel_privat", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "position", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "postf_ort", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "land", type = Land.class, required = false),
            @XmlElementRef(name = "tel_sonstige", type = TelSonstige.class, required = false),
            @XmlElementRef(name = "tel_durchw", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "plz", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "name", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "anrede", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "postf_plz", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "foto", type = Foto.class, required = false),
            @XmlElementRef(name = "adressfreigabe", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "freitextfeld", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "postfach", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "user_defined_simplefield", type = UserDefinedSimplefield.class, required = false),
            @XmlElementRef(name = "titel", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "tel_zentrale", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "email_direkt", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "personennummer", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "immobilientreuhaenderid", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "hausnummer", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "email_sonstige", type = EmailSonstige.class, required = false),
            @XmlElementRef(name = "firma", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "strasse", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "user_defined_extend", type = UserDefinedExtend.class, required = false)
    })
    protected List<Object> content = new ArrayList<>();

    @XmlElement(name = "email_zentrale")
    private String email_zentrale;
    @XmlElement(name = "email_direkt")
    private String email_direkt;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "vorname")
    private String vorname;
    @XmlElement(name = "firma")
    private String firma;

    public String getEmailDirekt() {
        return email_direkt;
    }

    public void setEmailDirekt(String emailDirekt) {
        this.email_direkt = emailDirekt;
    }

    /**
     * Ruft das restliche Contentmodell ab.
     * <p>
     * <p>
     * Sie rufen diese "catch-all"-Eigenschaft aus folgendem Grund ab: Der Feldname "EmailDirekt" wird von zwei
     * verschiedenen Teilen eines Schemas verwendet. Siehe: Zeile 220 von file:/D:/immomio/sourcetree/backend/test_jsxb/src/openimmo_127.xsd
     * Zeile 213 von file:/D:/immomio/sourcetree/backend/test_jsxb/src/openimmo_127.xsd
     * <p>
     * Um diese Eigenschaft zu entfernen, wenden Sie eine Eigenschaftenanpassung f�r eine der beiden folgenden
     * Deklarationen an, um deren Namen zu �ndern: Gets the value of the content property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the content property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link   }{@code <}{@link String }{@code >}
     * {@link UserDefinedAnyfield } {@link JAXBElement }{@code <}{@link String }{@code >} {@link JAXBElement }{@code
     * <}{@link String }{@code >} {@link JAXBElement }{@code <}{@link String }{@code >} {@link JAXBElement }{@code
     * <}{@link String }{@code >} {@link JAXBElement }{@code <}{@link String }{@code >} {@link JAXBElement }{@code
     * <}{@link String }{@code >} {@link JAXBElement }{@code <}{@link String }{@code >} {@link JAXBElement }{@code
     * <}{@link String }{@code >} {@link JAXBElement }{@code <}{@link Object }{@code >} {@link JAXBElement }{@code
     * <}{@link String }{@code >} {@link JAXBElement }{@code <}{@link String }{@code >} {@link JAXBElement }{@code
     * <}{@link String }{@code >} {@link Land } {@link TelSonstige } {@link JAXBElement }{@code <}{@link String }{@code
     * >} {@link JAXBElement }{@code <}{@link String }{@code >} {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >} {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link Foto } {@link JAXBElement }{@code <}{@link Boolean }{@code >} {@link JAXBElement }{@code <}{@link String
     * }{@code >} {@link JAXBElement }{@code <}{@link String }{@code >} {@link UserDefinedSimplefield } {@link
     * JAXBElement }{@code <}{@link String }{@code >} {@link JAXBElement }{@code <}{@link String }{@code >} {@link
     * JAXBElement }{@code <}{@link String }{@code >} {@link JAXBElement }{@code <}{@link String }{@code >} {@link
     * JAXBElement }{@code <}{@link String }{@code >} {@link JAXBElement }{@code <}{@link String }{@code >} {@link
     * EmailSonstige } {@link JAXBElement }{@code <}{@link String }{@code >} {@link JAXBElement }{@code <}{@link String
     * }{@code >} {@link UserDefinedExtend }
     */
    public List<Object> getContent() {
        if (content == null) {
            content = new ArrayList<>();
        }
        return this.content;
    }

    public String getEmailZentrale() {
        return email_zentrale;
    }

    public void setEmailZentrale(String email_zentrale) {
        this.email_zentrale = email_zentrale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }
}
