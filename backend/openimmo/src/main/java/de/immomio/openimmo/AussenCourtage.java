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
 *       &lt;attribute name="mit_mwst" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "content"
})
@XmlRootElement(name = "aussen_courtage")
public class AussenCourtage {

    @XmlValue
    protected String content;

    @XmlAttribute(name = "mit_mwst")
    protected Boolean mitMwst;

    /**
     * Ruft den Wert der content-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getContent() {
        return content;
    }

    /**
     * Legt den Wert der content-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setContent(String value) {
        this.content = value;
    }

    /**
     * Ruft den Wert der mitMwst-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isMitMwst() {
        return mitMwst;
    }

    /**
     * Legt den Wert der mitMwst-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setMitMwst(Boolean value) {
        this.mitMwst = value;
    }

}
