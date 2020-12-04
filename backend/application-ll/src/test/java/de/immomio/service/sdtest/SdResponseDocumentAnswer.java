package de.immomio.service.sdtest;

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
public class SdResponseDocumentAnswer implements Serializable {
    private static final long serialVersionUID = -7290338287002709208L;
    private String description;
    private String fileName;
}
