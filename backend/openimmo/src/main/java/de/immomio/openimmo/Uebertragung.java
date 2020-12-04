//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.10.16 um 11:10:09 AM CEST 
//

package de.immomio.openimmo;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>Java-Klasse f�r anonymous complex type.
 * <p>
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="art" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="ONLINE"/>
 *             &lt;enumeration value="OFFLINE"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="umfang" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="TEIL"/>
 *             &lt;enumeration value="VOLL"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="modus">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="NEW"/>
 *             &lt;enumeration value="CHANGE"/>
 *             &lt;enumeration value="DELETE"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="sendersoftware" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="senderversion" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="techn_email" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="regi_id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="timestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "uebertragung")
public class Uebertragung {

    @XmlAttribute(name = "art", required = true)
    protected String art;

    @XmlAttribute(name = "umfang", required = true)
    protected String umfang;

    @XmlAttribute(name = "modus")
    protected String modus;

    @XmlAttribute(name = "version", required = true)
    protected String version;

    @XmlAttribute(name = "sendersoftware", required = true)
    protected String sendersoftware;

    @XmlAttribute(name = "senderversion", required = true)
    protected String senderversion;

    @XmlAttribute(name = "techn_email")
    protected String technEmail;

    @XmlAttribute(name = "regi_id")
    protected String regiId;

    @XmlAttribute(name = "timestamp")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar timestamp;

    /**
     * Ruft den Wert der art-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getArt() {
        return art;
    }

    /**
     * Legt den Wert der art-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setArt(String value) {
        this.art = value;
    }

    /**
     * Ruft den Wert der umfang-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getUmfang() {
        return umfang;
    }

    /**
     * Legt den Wert der umfang-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setUmfang(String value) {
        this.umfang = value;
    }

    /**
     * Ruft den Wert der modus-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getModus() {
        return modus;
    }

    /**
     * Legt den Wert der modus-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setModus(String value) {
        this.modus = value;
    }

    /**
     * Ruft den Wert der version-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getVersion() {
        return version;
    }

    /**
     * Legt den Wert der version-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Ruft den Wert der sendersoftware-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getSendersoftware() {
        return sendersoftware;
    }

    /**
     * Legt den Wert der sendersoftware-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setSendersoftware(String value) {
        this.sendersoftware = value;
    }

    /**
     * Ruft den Wert der senderversion-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getSenderversion() {
        return senderversion;
    }

    /**
     * Legt den Wert der senderversion-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setSenderversion(String value) {
        this.senderversion = value;
    }

    /**
     * Ruft den Wert der technEmail-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getTechnEmail() {
        return technEmail;
    }

    /**
     * Legt den Wert der technEmail-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setTechnEmail(String value) {
        this.technEmail = value;
    }

    /**
     * Ruft den Wert der regiId-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getRegiId() {
        return regiId;
    }

    /**
     * Legt den Wert der regiId-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setRegiId(String value) {
        this.regiId = value;
    }

    /**
     * Ruft den Wert der timestamp-Eigenschaft ab.
     *
     * @return possible object is {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getTimestamp() {
        return timestamp;
    }

    /**
     * Legt den Wert der timestamp-Eigenschaft fest.
     *
     * @param value allowed object is {@link XMLGregorianCalendar }
     */
    public void setTimestamp(XMLGregorianCalendar value) {
        this.timestamp = value;
    }

}
