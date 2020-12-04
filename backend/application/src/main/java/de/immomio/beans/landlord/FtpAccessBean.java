package de.immomio.beans.landlord;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class FtpAccessBean {

    @NotNull
    private String homeDirectory;

    @NotNull
    private String userPassword;

}
