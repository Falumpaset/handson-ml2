package de.immomio.service.sdtest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SdResponseCheckedAnswer {
    private String question;
    private Boolean answer;
    private String comment = "";
}
