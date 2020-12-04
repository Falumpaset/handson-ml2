package de.immomio.beans.shared.selfdisclosure;

import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosureSubQuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Niklas Lindemann
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelfDisclosureSubQuestionBean implements Serializable {
    private static final long serialVersionUID = 8218885065517717979L;
    private Long id;
    private String title;
    private SelfDisclosureSubQuestionType type;
    private Boolean commentAllowed;
    private String commentHint;
    private Integer orderNumber;
    private Boolean mandatory;
    private Boolean hidden;
    private String constantName;
}
