package de.immomio.exporter.openimmo.feedback;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.Date;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class OpenimmoSender {

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "openimmo_anid")
    private String openimmoAnid;

    @XmlElement(name = "datum")
    private Date date;

    @XmlElement(name = "makler_id")
    private String agentId;

    @XmlElement(name = "regi_id")
    private String regiId;

}
