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
 *       &lt;attribute name="BAUSATZHAUS" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="AUSBAUHAUS" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="SCHLUESSELFERTIGMITKELLER" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="SCHLUESSELFERTIGOHNEBODENPLATTE" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="SCHLUESSELFERTIGMITBODENPLATTE" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "ausbaustufe")
public class Ausbaustufe {

    @XmlAttribute(name = "BAUSATZHAUS")
    protected Boolean bausatzhaus;

    @XmlAttribute(name = "AUSBAUHAUS")
    protected Boolean ausbauhaus;

    @XmlAttribute(name = "SCHLUESSELFERTIGMITKELLER")
    protected Boolean schluesselfertigmitkeller;

    @XmlAttribute(name = "SCHLUESSELFERTIGOHNEBODENPLATTE")
    protected Boolean schluesselfertigohnebodenplatte;

    @XmlAttribute(name = "SCHLUESSELFERTIGMITBODENPLATTE")
    protected Boolean schluesselfertigmitbodenplatte;

    /**
     * Ruft den Wert der bausatzhaus-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isBAUSATZHAUS() {
        return bausatzhaus;
    }

    /**
     * Legt den Wert der bausatzhaus-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setBAUSATZHAUS(Boolean value) {
        this.bausatzhaus = value;
    }

    /**
     * Ruft den Wert der ausbauhaus-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isAUSBAUHAUS() {
        return ausbauhaus;
    }

    /**
     * Legt den Wert der ausbauhaus-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setAUSBAUHAUS(Boolean value) {
        this.ausbauhaus = value;
    }

    /**
     * Ruft den Wert der schluesselfertigmitkeller-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isSCHLUESSELFERTIGMITKELLER() {
        return schluesselfertigmitkeller;
    }

    /**
     * Legt den Wert der schluesselfertigmitkeller-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setSCHLUESSELFERTIGMITKELLER(Boolean value) {
        this.schluesselfertigmitkeller = value;
    }

    /**
     * Ruft den Wert der schluesselfertigohnebodenplatte-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isSCHLUESSELFERTIGOHNEBODENPLATTE() {
        return schluesselfertigohnebodenplatte;
    }

    /**
     * Legt den Wert der schluesselfertigohnebodenplatte-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setSCHLUESSELFERTIGOHNEBODENPLATTE(Boolean value) {
        this.schluesselfertigohnebodenplatte = value;
    }

    /**
     * Ruft den Wert der schluesselfertigmitbodenplatte-Eigenschaft ab.
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isSCHLUESSELFERTIGMITBODENPLATTE() {
        return schluesselfertigmitbodenplatte;
    }

    /**
     * Legt den Wert der schluesselfertigmitbodenplatte-Eigenschaft fest.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setSCHLUESSELFERTIGMITBODENPLATTE(Boolean value) {
        this.schluesselfertigmitbodenplatte = value;
    }

}
