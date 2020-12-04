package de.immomio.exporter.openimmo.feedback;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Getter
@Setter
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class OpenimmoError {

    @XmlElement(name = "objekt_id")
    private String objectId;

    @XmlElement(name = "fehlernr")
    private Integer errorNumber;

    @XmlElement(name = "text")
    private String text;

    public OpenimmoError(String objectId, Integer errorNumber, String text) {
        this.objectId = objectId;
        this.errorNumber = errorNumber;
        this.text = text;
    }
}
