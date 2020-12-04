package de.immomio.reporting.model.beans.propertysearcher;

import de.immomio.reporting.model.beans.ReportingPropertyBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportingCommentBean implements ReportingPropertySearcherIndexable, Serializable {

    private static final long serialVersionUID = -8728170032431441621L;

    private String comment;

    private ReportingPropertyBean property;

}
