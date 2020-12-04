package de.immomio.beans.landlord;

import de.immomio.data.base.json.JsonPayload;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Maik Kingma
 */

@Getter
@Setter
@NoArgsConstructor
public class CustomQuestionCreateBean implements Serializable {

    private static final long serialVersionUID = -4936568426355231159L;

    private JsonPayload jsonPayload;

    private Map<String, Object> desiredResponses = new HashMap<>();

    private boolean scoring = false;

    @Max(10)
    private int importance = 0;

    private boolean commentAllowed = false;

    private String commentHint;

    private boolean global;

}
