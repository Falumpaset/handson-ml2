package de.immomio.reporting.model.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
public abstract class AbstractEvent implements Serializable {

    private static final long serialVersionUID = -2556279342760598405L;

    protected Date timestamp = new Date();

    @JsonIgnore
    protected String documentId;

    public String writeJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.addMixIn(PropertyData.class, PropertyDataMixin.class);
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{'error'}";
        }
    }
}
