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
 *       &lt;attribute name="feldname" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "content"
})
@XmlRootElement(name = "user_defined_simplefield")
public class UserDefinedSimplefield {

    @XmlValue
    protected String content;

    @XmlAttribute(name = "feldname", required = true)
    protected String feldname;

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
     * Ruft den Wert der feldname-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getFeldname() {
        return feldname;
    }

    /**
     * Legt den Wert der feldname-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setFeldname(String value) {
        this.feldname = value;
    }

}
