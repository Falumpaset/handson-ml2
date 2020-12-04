package de.immomio.beans.landlord;

import de.immomio.controller.paging.CustomPageable;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Data
public class LandlordUserSearchBean extends CustomPageable implements Serializable {
    private static final long serialVersionUID = 6454720260738253400L;
    private List<LandlordUsertype> types = new ArrayList<>();
    private Boolean enabled;
    private String searchTerm;
}
