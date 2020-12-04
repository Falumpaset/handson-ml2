package de.immomio.utils.compare;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Niklas Lindemann
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompareBean implements Serializable {
    private static final long serialVersionUID = 4038745690046311408L;
    private String fieldName;
    private Object value;
    private Object prevValue;
    private Class parentClass;

}
