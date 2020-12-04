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
 *       &lt;attribute name="PERSONEN" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="LASTEN" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "fahrstuhl")
public class Fahrstuhl {

    @XmlAttribute(name = "PERSONEN")
    protected Boolean personen;

    @XmlAttribute(name = "LASTEN")
    protected Boolean lasten;

    /**
     * Ruft den Wert der personen-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isPERSONEN() {
        return personen;
    }

    /**
     * Legt den Wert der personen-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setPERSONEN(Boolean value) {
        this.personen = value;
    }

    /**
     * Ruft den Wert der lasten-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isLASTEN() {
        return lasten;
    }

    /**
     * Legt den Wert der lasten-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setLASTEN(Boolean value) {
        this.lasten = value;
    }

}
