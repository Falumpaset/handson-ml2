package de.immomio.exporter.openimmo.feedback;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class OpenimmoStatus {

    @XmlElement(name = "statusnr")
    private Integer statusNumber;

    @XmlElement(name = "text")
    private String text;
}
