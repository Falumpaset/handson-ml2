package de.immomio.beans.shared;

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
public class CommonCountBean implements Serializable {
    private static final long serialVersionUID = -3897865850196831317L;

    private Long count;
}
