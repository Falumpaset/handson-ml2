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
 *       &lt;attribute name="MASSIV" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="FERTIGTEILE" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="HOLZ" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "bauweise")
public class Bauweise {

    @XmlAttribute(name = "MASSIV")
    protected Boolean massiv;

    @XmlAttribute(name = "FERTIGTEILE")
    protected Boolean fertigteile;

    @XmlAttribute(name = "HOLZ")
    protected Boolean holz;

    /**
     * Ruft den Wert der massiv-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isMASSIV() {
        return massiv;
    }

    /**
     * Legt den Wert der massiv-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setMASSIV(Boolean value) {
        this.massiv = value;
    }

    /**
     * Ruft den Wert der fertigteile-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isFERTIGTEILE() {
        return fertigteile;
    }

    /**
     * Legt den Wert der fertigteile-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setFERTIGTEILE(Boolean value) {
        this.fertigteile = value;
    }

    /**
     * Ruft den Wert der holz-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isHOLZ() {
        return holz;
    }

    /**
     * Legt den Wert der holz-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setHOLZ(Boolean value) {
        this.holz = value;
    }

}
