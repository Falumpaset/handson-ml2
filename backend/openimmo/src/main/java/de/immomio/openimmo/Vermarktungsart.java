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
 *       &lt;attribute name="KAUF" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="MIETE_PACHT" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="ERBPACHT" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="LEASING" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "vermarktungsart")
public class Vermarktungsart {

    @XmlAttribute(name = "KAUF", required = true)
    protected boolean kauf;

    @XmlAttribute(name = "MIETE_PACHT", required = true)
    protected boolean mietepacht;

    @XmlAttribute(name = "ERBPACHT")
    protected Boolean erbpacht;

    @XmlAttribute(name = "LEASING")
    protected Boolean leasing;

    /**
     * Ruft den Wert der kauf-Eigenschaft ab.
     */
    public boolean isKAUF() {
        return kauf;
    }

    /**
     * Legt den Wert der kauf-Eigenschaft fest.
     */
    public void setKAUF(boolean value) {
        this.kauf = value;
    }

    /**
     * Ruft den Wert der mietepacht-Eigenschaft ab.
     */
    public boolean isMIETEPACHT() {
        return mietepacht;
    }

    /**
     * Legt den Wert der mietepacht-Eigenschaft fest.
     */
    public void setMIETEPACHT(boolean value) {
        this.mietepacht = value;
    }

    /**
     * Ruft den Wert der erbpacht-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isERBPACHT() {
        return erbpacht;
    }

    /**
     * Legt den Wert der erbpacht-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setERBPACHT(Boolean value) {
        this.erbpacht = value;
    }

    /**
     * Ruft den Wert der leasing-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isLEASING() {
        return leasing;
    }

    /**
     * Legt den Wert der leasing-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setLEASING(Boolean value) {
        this.leasing = value;
    }

}
