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
 *         &lt;choice>
 *           &lt;element ref="{}zimmer" maxOccurs="unbounded"/>
 *           &lt;element ref="{}wohnung" maxOccurs="unbounded"/>
 *           &lt;element ref="{}haus" maxOccurs="unbounded"/>
 *           &lt;element ref="{}grundstueck" maxOccurs="unbounded"/>
 *           &lt;element ref="{}buero_praxen" maxOccurs="unbounded"/>
 *           &lt;element ref="{}einzelhandel" maxOccurs="unbounded"/>
 *           &lt;element ref="{}gastgewerbe" maxOccurs="unbounded"/>
 *           &lt;element ref="{}hallen_lager_prod" maxOccurs="unbounded"/>
 *           &lt;element ref="{}land_und_forstwirtschaft" maxOccurs="unbounded"/>
 *           &lt;element ref="{}parken" maxOccurs="unbounded"/>
 *           &lt;element ref="{}sonstige" maxOccurs="unbounded"/>
 *           &lt;element ref="{}freizeitimmobilie_gewerblich" maxOccurs="unbounded"/>
 *           &lt;element ref="{}zinshaus_renditeobjekt" maxOccurs="unbounded"/>
 *         &lt;/choice>
 *         &lt;element ref="{}objektart_zusatz" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "zimmer",
        "wohnung",
        "haus",
        "grundstueck",
        "bueroPraxen",
        "einzelhandel",
        "gastgewerbe",
        "hallenLagerProd",
        "landUndForstwirtschaft",
        "parken",
        "sonstige",
        "freizeitimmobilieGewerblich",
        "zinshausRenditeobjekt",
        "objektartZusatz"
})
@XmlRootElement(name = "objektart")
public class Objektart {

    protected List<Zimmer> zimmer;

    protected List<Wohnung> wohnung;

    protected List<Haus> haus;

    protected List<Grundstueck> grundstueck;

    @XmlElement(name = "buero_praxen")
    protected List<BueroPraxen> bueroPraxen;

    protected List<Einzelhandel> einzelhandel;

    protected List<Gastgewerbe> gastgewerbe;

    @XmlElement(name = "hallen_lager_prod")
    protected List<HallenLagerProd> hallenLagerProd;

    @XmlElement(name = "land_und_forstwirtschaft")
    protected List<LandUndForstwirtschaft> landUndForstwirtschaft;

    protected List<Parken> parken;

    protected List<Sonstige> sonstige;

    @XmlElement(name = "freizeitimmobilie_gewerblich")
    protected List<FreizeitimmobilieGewerblich> freizeitimmobilieGewerblich;

    @XmlElement(name = "zinshaus_renditeobjekt")
    protected List<ZinshausRenditeobjekt> zinshausRenditeobjekt;

    @XmlElement(name = "objektart_zusatz")
    protected List<String> objektartZusatz;

    /**
     * Gets the value of the zimmer property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the zimmer property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getZimmer().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Zimmer }
     */
    public List<Zimmer> getZimmer() {
        if (zimmer == null) {
            zimmer = new ArrayList<>();
        }
        return this.zimmer;
    }

    /**
     * Gets the value of the wohnung property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the wohnung property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWohnung().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Wohnung }
     */
    public List<Wohnung> getWohnung() {
        if (wohnung == null) {
            wohnung = new ArrayList<>();
        }
        return this.wohnung;
    }

    /**
     * Gets the value of the haus property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the haus property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHaus().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Haus }
     */
    public List<Haus> getHaus() {
        if (haus == null) {
            haus = new ArrayList<>();
        }
        return this.haus;
    }

    /**
     * Gets the value of the grundstueck property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the grundstueck property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGrundstueck().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Grundstueck }
     */
    public List<Grundstueck> getGrundstueck() {
        if (grundstueck == null) {
            grundstueck = new ArrayList<>();
        }
        return this.grundstueck;
    }

    /**
     * Gets the value of the bueroPraxen property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the bueroPraxen property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBueroPraxen().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link BueroPraxen }
     */
    public List<BueroPraxen> getBueroPraxen() {
        if (bueroPraxen == null) {
            bueroPraxen = new ArrayList<>();
        }
        return this.bueroPraxen;
    }

    /**
     * Gets the value of the einzelhandel property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the einzelhandel property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEinzelhandel().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Einzelhandel }
     */
    public List<Einzelhandel> getEinzelhandel() {
        if (einzelhandel == null) {
            einzelhandel = new ArrayList<>();
        }
        return this.einzelhandel;
    }

    /**
     * Gets the value of the gastgewerbe property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the gastgewerbe property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGastgewerbe().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Gastgewerbe }
     */
    public List<Gastgewerbe> getGastgewerbe() {
        if (gastgewerbe == null) {
            gastgewerbe = new ArrayList<>();
        }
        return this.gastgewerbe;
    }

    /**
     * Gets the value of the hallenLagerProd property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the hallenLagerProd property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHallenLagerProd().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link HallenLagerProd }
     */
    public List<HallenLagerProd> getHallenLagerProd() {
        if (hallenLagerProd == null) {
            hallenLagerProd = new ArrayList<>();
        }
        return this.hallenLagerProd;
    }

    /**
     * Gets the value of the landUndForstwirtschaft property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the landUndForstwirtschaft property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLandUndForstwirtschaft().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link LandUndForstwirtschaft }
     */
    public List<LandUndForstwirtschaft> getLandUndForstwirtschaft() {
        if (landUndForstwirtschaft == null) {
            landUndForstwirtschaft = new ArrayList<>();
        }
        return this.landUndForstwirtschaft;
    }

    /**
     * Gets the value of the parken property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the parken property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParken().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Parken }
     */
    public List<Parken> getParken() {
        if (parken == null) {
            parken = new ArrayList<>();
        }
        return this.parken;
    }

    /**
     * Gets the value of the sonstige property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the sonstige property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSonstige().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Sonstige }
     */
    public List<Sonstige> getSonstige() {
        if (sonstige == null) {
            sonstige = new ArrayList<>();
        }
        return this.sonstige;
    }

    /**
     * Gets the value of the freizeitimmobilieGewerblich property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the freizeitimmobilieGewerblich property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFreizeitimmobilieGewerblich().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link FreizeitimmobilieGewerblich }
     */
    public List<FreizeitimmobilieGewerblich> getFreizeitimmobilieGewerblich() {
        if (freizeitimmobilieGewerblich == null) {
            freizeitimmobilieGewerblich = new ArrayList<>();
        }
        return this.freizeitimmobilieGewerblich;
    }

    /**
     * Gets the value of the zinshausRenditeobjekt property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the zinshausRenditeobjekt property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getZinshausRenditeobjekt().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link ZinshausRenditeobjekt }
     */
    public List<ZinshausRenditeobjekt> getZinshausRenditeobjekt() {
        if (zinshausRenditeobjekt == null) {
            zinshausRenditeobjekt = new ArrayList<>();
        }
        return this.zinshausRenditeobjekt;
    }

    /**
     * Gets the value of the objektartZusatz property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the objektartZusatz property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getObjektartZusatz().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link String }
     */
    public List<String> getObjektartZusatz() {
        if (objektartZusatz == null) {
            objektartZusatz = new ArrayList<>();
        }
        return this.objektartZusatz;
    }

}
