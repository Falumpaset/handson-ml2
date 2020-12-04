package de.immomio.beans.landlord;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class InviteToViewingsBean implements Serializable {

    private static final long serialVersionUID = -7357173363372642098L;

    private List<String> emails = new ArrayList<>();

}
