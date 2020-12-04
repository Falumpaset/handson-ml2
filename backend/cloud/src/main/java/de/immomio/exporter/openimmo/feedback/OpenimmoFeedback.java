package de.immomio.exporter.openimmo.feedback;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Bastian Bliemeister.
 */
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "openimmo_feedback")
public class OpenimmoFeedback {

    @XmlElement(name = "version")
    private String version;

    @XmlElement(name = "sender")
    private OpenimmoSender sender;

    @XmlElement(name = "objekt")
    private OpenimmoObject object;

    @XmlElement(name = "fehlerliste")
    private OpenimmoErrors errorList;

    @XmlElement(name = "status")
    private OpenimmoStatus status;

}
