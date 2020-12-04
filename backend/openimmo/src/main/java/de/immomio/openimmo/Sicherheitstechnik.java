//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.10.16 um 11:10:09 AM CEST 
//

package de.immomio.openimmo;

import javax.xml.bind.annotation.*;

/**
 * <p>Java-Klasse f�r anonymous complex type.
 * <p>
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="ALARMANLAGE" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="KAMERA" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="POLIZEIRUF" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "sicherheitstechnik")
public class Sicherheitstechnik {

    @XmlAttribute(name = "ALARMANLAGE")
    protected Boolean alarmanlage;

    @XmlAttribute(name = "KAMERA")
    protected Boolean kamera;

    @XmlAttribute(name = "POLIZEIRUF")
    protected Boolean polizeiruf;

    /**
     * Ruft den Wert der alarmanlage-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isALARMANLAGE() {
        return alarmanlage;
    }

    /**
     * Legt den Wert der alarmanlage-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setALARMANLAGE(Boolean value) {
        this.alarmanlage = value;
    }

    /**
     * Ruft den Wert der kamera-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isKAMERA() {
        return kamera;
    }

    /**
     * Legt den Wert der kamera-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setKAMERA(Boolean value) {
        this.kamera = value;
    }

    /**
     * Ruft den Wert der polizeiruf-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isPOLIZEIRUF() {
        return polizeiruf;
    }

    /**
     * Legt den Wert der polizeiruf-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setPOLIZEIRUF(Boolean value) {
        this.polizeiruf = value;
    }

}
