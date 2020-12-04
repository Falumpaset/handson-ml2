package de.immomio.sendgrid.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
@NoArgsConstructor
public class SendgridTemplateVersion implements Serializable {

    private static final long serialVersionUID = -835392679900787775L;

    private String id;

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("template_id")
    private String templateId;

    private Integer active;

    private String name;

    @JsonProperty("html_content")
    private String htmlContent;

    @JsonProperty("plain_content")
    private String plainContent;

    @JsonProperty("generate_plain_content")
    private Boolean generatePlainContent;

    private String subject;

    @JsonProperty("updated_at")
    private String updatedAt;

    private String editor;
}
