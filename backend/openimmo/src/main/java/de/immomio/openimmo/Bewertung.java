//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.10.16 um 11:10:09 AM CEST 
//

package de.immomio.openimmo;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Java-Klasse f�r anonymous complex type.
 * <p>
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="feld" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="wert" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="typ" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="modus" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "feld"
})
@XmlRootElement(name = "bewertung")
public class Bewertung {

    protected List<Bewertung.Feld> feld;

    /**
     * Gets the value of the feld property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the feld property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFeld().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Bewertung.Feld }
     */
    public List<Bewertung.Feld> getFeld() {
        if (feld == null) {
            feld = new ArrayList<>();
        }
        return this.feld;
    }

    /**
     * <p>Java-Klasse f�r anonymous complex type.
     * <p>
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="wert" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="typ" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="modus" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "name",
            "wert",
            "typ",
            "modus"
    })
    public static class Feld {

        @XmlElement(required = true)
        protected String name;

        @XmlElement(required = true)
        protected String wert;

        protected List<String> typ;

        protected List<String> modus;

        /**
         * Ruft den Wert der name-Eigenschaft ab.
         *
         * @return possible object is {@link String }
         */
        public String getName() {
            return name;
        }

        /**
         * Legt den Wert der name-Eigenschaft fest.
         *
         * @param value allowed object is {@link String }
         */
        public void setName(String value) {
            this.name = value;
        }

        /**
         * Ruft den Wert der wert-Eigenschaft ab.
         *
         * @return possible object is {@link String }
         */
        public String getWert() {
            return wert;
        }

        /**
         * Legt den Wert der wert-Eigenschaft fest.
         *
         * @param value allowed object is {@link String }
         */
        public void setWert(String value) {
            this.wert = value;
        }

        /**
         * Gets the value of the typ property.
         * <p>
         * <p>
         * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you
         * make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE>
         * method for the typ property.
         * <p>
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTyp().add(newItem);
         * </pre>
         * <p>
         * <p>
         * <p>
         * Objects of the following type(s) are allowed in the list {@link String }
         */
        public List<String> getTyp() {
            if (typ == null) {
                typ = new ArrayList<>();
            }
            return this.typ;
        }

        /**
         * Gets the value of the modus property.
         * <p>
         * <p>
         * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you
         * make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE>
         * method for the modus property.
         * <p>
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getModus().add(newItem);
         * </pre>
         * <p>
         * <p>
         * <p>
         * Objects of the following type(s) are allowed in the list {@link String }
         */
        public List<String> getModus() {
            if (modus == null) {
                modus = new ArrayList<>();
            }
            return this.modus;
        }

    }

}
