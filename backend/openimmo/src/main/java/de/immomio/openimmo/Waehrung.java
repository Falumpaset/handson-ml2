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
 *       &lt;attribute name="iso_waehrung">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="AED"/>
 *             &lt;enumeration value="AFA"/>
 *             &lt;enumeration value="ALL"/>
 *             &lt;enumeration value="AMD"/>
 *             &lt;enumeration value="ANG"/>
 *             &lt;enumeration value="AON"/>
 *             &lt;enumeration value="ARP"/>
 *             &lt;enumeration value="ATS"/>
 *             &lt;enumeration value="AUD"/>
 *             &lt;enumeration value="AWF"/>
 *             &lt;enumeration value="AZM"/>
 *             &lt;enumeration value="BAK"/>
 *             &lt;enumeration value="BBD"/>
 *             &lt;enumeration value="BDT"/>
 *             &lt;enumeration value="BEF"/>
 *             &lt;enumeration value="BGL"/>
 *             &lt;enumeration value="BHD"/>
 *             &lt;enumeration value="BIF"/>
 *             &lt;enumeration value="BMD"/>
 *             &lt;enumeration value="BND"/>
 *             &lt;enumeration value="BOB"/>
 *             &lt;enumeration value="BES"/>
 *             &lt;enumeration value="BRL"/>
 *             &lt;enumeration value="BSD"/>
 *             &lt;enumeration value="BTR"/>
 *             &lt;enumeration value="BWP"/>
 *             &lt;enumeration value="BYR"/>
 *             &lt;enumeration value="BZD"/>
 *             &lt;enumeration value="CAD"/>
 *             &lt;enumeration value="CDF"/>
 *             &lt;enumeration value="CHF"/>
 *             &lt;enumeration value="CLP"/>
 *             &lt;enumeration value="CNY"/>
 *             &lt;enumeration value="COP"/>
 *             &lt;enumeration value="CRC"/>
 *             &lt;enumeration value="CZK"/>
 *             &lt;enumeration value="CUP"/>
 *             &lt;enumeration value="CVE"/>
 *             &lt;enumeration value="CUW"/>
 *             &lt;enumeration value="CYP"/>
 *             &lt;enumeration value="DEM"/>
 *             &lt;enumeration value="DJF"/>
 *             &lt;enumeration value="DKK"/>
 *             &lt;enumeration value="DOP"/>
 *             &lt;enumeration value="DZD"/>
 *             &lt;enumeration value="ECS"/>
 *             &lt;enumeration value="EEK"/>
 *             &lt;enumeration value="EGP"/>
 *             &lt;enumeration value="ERN"/>
 *             &lt;enumeration value="ESP"/>
 *             &lt;enumeration value="ETB"/>
 *             &lt;enumeration value="EUR"/>
 *             &lt;enumeration value="FIM"/>
 *             &lt;enumeration value="FJD"/>
 *             &lt;enumeration value="FKP"/>
 *             &lt;enumeration value="FRF"/>
 *             &lt;enumeration value="GBP"/>
 *             &lt;enumeration value="GEL"/>
 *             &lt;enumeration value="GHC"/>
 *             &lt;enumeration value="GIP"/>
 *             &lt;enumeration value="GMD"/>
 *             &lt;enumeration value="GNF"/>
 *             &lt;enumeration value="GRD"/>
 *             &lt;enumeration value="GTQ"/>
 *             &lt;enumeration value="GYD"/>
 *             &lt;enumeration value="HKD"/>
 *             &lt;enumeration value="HNL"/>
 *             &lt;enumeration value="HRK"/>
 *             &lt;enumeration value="HTG"/>
 *             &lt;enumeration value="HUF"/>
 *             &lt;enumeration value="IDR"/>
 *             &lt;enumeration value="IEP"/>
 *             &lt;enumeration value="IEP"/>
 *             &lt;enumeration value="ILS"/>
 *             &lt;enumeration value="INR"/>
 *             &lt;enumeration value="IQD"/>
 *             &lt;enumeration value="IRR"/>
 *             &lt;enumeration value="ISK"/>
 *             &lt;enumeration value="ITL"/>
 *             &lt;enumeration value="JMD"/>
 *             &lt;enumeration value="JOD"/>
 *             &lt;enumeration value="JPY"/>
 *             &lt;enumeration value="KES"/>
 *             &lt;enumeration value="KGS"/>
 *             &lt;enumeration value="KHR"/>
 *             &lt;enumeration value="KMF"/>
 *             &lt;enumeration value="KPW"/>
 *             &lt;enumeration value="KRW"/>
 *             &lt;enumeration value="KWD"/>
 *             &lt;enumeration value="KYD"/>
 *             &lt;enumeration value="KZT"/>
 *             &lt;enumeration value="LAK"/>
 *             &lt;enumeration value="LBP"/>
 *             &lt;enumeration value="LKR"/>
 *             &lt;enumeration value="LRD"/>
 *             &lt;enumeration value="LSL"/>
 *             &lt;enumeration value="LTL"/>
 *             &lt;enumeration value="LUF"/>
 *             &lt;enumeration value="LVL"/>
 *             &lt;enumeration value="LYD"/>
 *             &lt;enumeration value="MAD"/>
 *             &lt;enumeration value="MDL"/>
 *             &lt;enumeration value="MGF"/>
 *             &lt;enumeration value="MMK"/>
 *             &lt;enumeration value="MNT"/>
 *             &lt;enumeration value="MOP"/>
 *             &lt;enumeration value="MRO"/>
 *             &lt;enumeration value="MTL"/>
 *             &lt;enumeration value="MUR"/>
 *             &lt;enumeration value="MVR"/>
 *             &lt;enumeration value="MWK"/>
 *             &lt;enumeration value="MXP"/>
 *             &lt;enumeration value="MYR"/>
 *             &lt;enumeration value="MZM"/>
 *             &lt;enumeration value="NAD"/>
 *             &lt;enumeration value="NGN"/>
 *             &lt;enumeration value="NIO"/>
 *             &lt;enumeration value="NLG"/>
 *             &lt;enumeration value="NOK"/>
 *             &lt;enumeration value="NPR"/>
 *             &lt;enumeration value="NZD"/>
 *             &lt;enumeration value="OMR"/>
 *             &lt;enumeration value="PAB"/>
 *             &lt;enumeration value="PEN"/>
 *             &lt;enumeration value="PGK"/>
 *             &lt;enumeration value="PHP"/>
 *             &lt;enumeration value="PKR"/>
 *             &lt;enumeration value="PLZ"/>
 *             &lt;enumeration value="PTE"/>
 *             &lt;enumeration value="PYG"/>
 *             &lt;enumeration value="QAR"/>
 *             &lt;enumeration value="ROL"/>
 *             &lt;enumeration value="RUR"/>
 *             &lt;enumeration value="RWF"/>
 *             &lt;enumeration value="SAR"/>
 *             &lt;enumeration value="SBD"/>
 *             &lt;enumeration value="SBL"/>
 *             &lt;enumeration value="SCR"/>
 *             &lt;enumeration value="SDD"/>
 *             &lt;enumeration value="SEK"/>
 *             &lt;enumeration value="SGD"/>
 *             &lt;enumeration value="SHP"/>
 *             &lt;enumeration value="SIT"/>
 *             &lt;enumeration value="SKK"/>
 *             &lt;enumeration value="SLL"/>
 *             &lt;enumeration value="SOS"/>
 *             &lt;enumeration value="SRG"/>
 *             &lt;enumeration value="STD"/>
 *             &lt;enumeration value="SVC"/>
 *             &lt;enumeration value="SYP"/>
 *             &lt;enumeration value="SZL"/>
 *             &lt;enumeration value="THB"/>
 *             &lt;enumeration value="TJR"/>
 *             &lt;enumeration value="TMM"/>
 *             &lt;enumeration value="TND"/>
 *             &lt;enumeration value="TOP"/>
 *             &lt;enumeration value="TRL"/>
 *             &lt;enumeration value="TTD"/>
 *             &lt;enumeration value="TWD"/>
 *             &lt;enumeration value="TZS"/>
 *             &lt;enumeration value="UAH"/>
 *             &lt;enumeration value="UGX"/>
 *             &lt;enumeration value="USD"/>
 *             &lt;enumeration value="UYU"/>
 *             &lt;enumeration value="UZS"/>
 *             &lt;enumeration value="VEB"/>
 *             &lt;enumeration value="VND"/>
 *             &lt;enumeration value="VUV"/>
 *             &lt;enumeration value="WST"/>
 *             &lt;enumeration value="XAF"/>
 *             &lt;enumeration value="XAG"/>
 *             &lt;enumeration value="XAU"/>
 *             &lt;enumeration value="XCD"/>
 *             &lt;enumeration value="XCO"/>
 *             &lt;enumeration value="XDR"/>
 *             &lt;enumeration value="XPD"/>
 *             &lt;enumeration value="XPF"/>
 *             &lt;enumeration value="XPT"/>
 *             &lt;enumeration value="YER"/>
 *             &lt;enumeration value="YUN"/>
 *             &lt;enumeration value="ZAR"/>
 *             &lt;enumeration value="ZMK"/>
 *             &lt;enumeration value="ZWD"/>
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
@XmlRootElement(name = "waehrung")
public class Waehrung {

    @XmlAttribute(name = "iso_waehrung")
    protected String isoWaehrung;

    /**
     * Ruft den Wert der isoWaehrung-Eigenschaft ab.
     *
     * @return possible object is {@link String }
     */
    public String getIsoWaehrung() {
        return isoWaehrung;
    }

    /**
     * Legt den Wert der isoWaehrung-Eigenschaft fest.
     *
     * @param value allowed object is {@link String }
     */
    public void setIsoWaehrung(String value) {
        this.isoWaehrung = value;
    }

}
