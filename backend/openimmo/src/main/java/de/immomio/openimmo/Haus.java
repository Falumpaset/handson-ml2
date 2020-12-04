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
 *       &lt;attribute name="haustyp">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="REIHENHAUS"/>
 *             &lt;enumeration value="REIHENEND"/>
 *             &lt;enumeration value="REIHENMITTEL"/>
 *             &lt;enumeration value="REIHENECK"/>
 *             &lt;enumeration value="DOPPELHAUSHAELFTE"/>
 *             &lt;enumeration value="EINFAMILIENHAUS"/>
 *             &lt;enumeration value="STADTHAUS"/>
 *             &lt;enumeration value="BUNGALOW"/>
 *             &lt;enumeration value="VILLA"/>
 *             &lt;enumeration value="RESTHOF"/>
 *             &lt;enumeration value="BAUERNHAUS"/>
 *             &lt;enumeration value="LANDHAUS"/>
 *             &lt;enumeration value="SCHLOSS"/>
 *             &lt;enumeration value="ZWEIFAMILIENHAUS"/>
 *             &lt;enumeration value="MEHRFAMILIENHAUS"/>
 *             &lt;enumeration value="FERIENHAUS"/>
 *             &lt;enumeration value="BERGHUETTE"/>
 *             &lt;enumeration value="CHALET"/>
 *             &lt;enumeration value="STRANDHAUS"/>
 *             &lt;enumeration value="LAUBE-DATSCHE-GARTENHAUS"/>
 *             &lt;enumeration value="APARTMENTHAUS"/>
 *             &lt;enumeration value="BURG"/>
 *             &lt;enumeration value="HERRENHAUS"/>
 *             &lt;enumeration value="FINCA"/>
 *             &lt;enumeration value="RUSTICO"/>
 *             &lt;enumeration value="FERTIGHAUS"/>
 *             &lt;enumeration value="KEINE_ANGABE"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "haus")
public class Haus {

    @XmlAttribute(name = "haustyp")
    protected String haustyp;

    /**
     * Ruft den Wert der haustyp-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getHaustyp() {
        return haustyp;
    }

    /**
     * Legt den Wert der haustyp-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setHaustyp(String value) {
        this.haustyp = value;
    }

}
