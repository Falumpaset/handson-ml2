package de.immomio.recipient.service.portal.parser.immowelt.xml;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Johannes Hiemer
 */
@XmlRootElement
public class Objektanfrage {

    private Objekt objekt;

    private Interessent interessent;

    public Objekt getObjekt() {
        return objekt;
    }

    public void setObjekt(Objekt objekt) {
        this.objekt = objekt;
    }

    public Interessent getInteressent() {
        return interessent;
    }

    public void setInteressent(Interessent interessent) {
        this.interessent = interessent;
    }

}