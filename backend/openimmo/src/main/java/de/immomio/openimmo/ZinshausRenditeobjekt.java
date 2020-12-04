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
 *       &lt;attribute name="zins_typ">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="MEHRFAMILIENHAUS"/>
 *             &lt;enumeration value="WOHN_UND_GESCHAEFTSHAUS"/>
 *             &lt;enumeration value="GESCHAEFTSHAUS"/>
 *             &lt;enumeration value="BUEROGEBAEUDE"/>
 *             &lt;enumeration value="SB_MAERKTE"/>
 *             &lt;enumeration value="EINKAUFSCENTREN"/>
 *             &lt;enumeration value="WOHNANLAGEN"/>
 *             &lt;enumeration value="VERBRAUCHERMAERKTE"/>
 *             &lt;enumeration value="INDUSTRIEANLAGEN"/>
 *             &lt;enumeration value="PFLEGEHEIM"/>
 *             &lt;enumeration value="SANATORIUM"/>
 *             &lt;enumeration value="SENIORENHEIM"/>
 *             &lt;enumeration value="BETREUTES-WOHNEN"/>
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
@XmlRootElement(name = "zinshaus_renditeobjekt")
public class ZinshausRenditeobjekt {

    @XmlAttribute(name = "zins_typ")
    protected String zinsTyp;

    /**
     * Ruft den Wert der zinsTyp-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getZinsTyp() {
        return zinsTyp;
    }

    /**
     * Legt den Wert der zinsTyp-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setZinsTyp(String value) {
        this.zinsTyp = value;
    }

}
