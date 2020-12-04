package de.immomio.sendgrid.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
@NoArgsConstructor
public class SendgridTemplate implements Serializable {

    private static final long serialVersionUID = 3233020026670589476L;

    private String id;

    private String name;

    private String generation;

    @JsonProperty("updated_at")
    private String updatedAt;

    @JsonProperty("versions")
    public List<SendgridTemplateVersion> versions = new ArrayList<>();

}
