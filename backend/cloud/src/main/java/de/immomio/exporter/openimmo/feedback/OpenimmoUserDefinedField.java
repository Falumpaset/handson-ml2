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
public class OpenimmoUserDefinedField {

    @XmlElement(name = "feld")
    private OpenimmoField field;

    public OpenimmoUserDefinedField(String name, String value) {
        this.field = new OpenimmoField(name, value);
    }

}
