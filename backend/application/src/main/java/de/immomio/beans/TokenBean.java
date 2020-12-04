package de.immomio.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class TokenBean implements Serializable {

    private static final long serialVersionUID = -6754277790005765421L;

    private String token;

}
