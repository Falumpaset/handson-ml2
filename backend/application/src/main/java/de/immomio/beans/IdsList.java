package de.immomio.beans;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class IdsList implements Serializable {

    private static final long serialVersionUID = 8953042283993565563L;

    @NotNull
    private List<Long> ids;
}
