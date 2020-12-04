package de.immomio.exporter.openimmo.feedback;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class OpenimmoField {

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "wert")
    private String value;

    @XmlElement(name = "typ")
    private String type;

    @XmlElement(name = "modus")
    private String mode;

    public OpenimmoField(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
