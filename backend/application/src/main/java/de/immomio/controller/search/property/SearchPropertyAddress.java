package de.immomio.controller.search.property;

import de.immomio.data.shared.bean.common.Address;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchPropertyAddress extends Address {

    private static final long serialVersionUID = 6032583337116443344L;

    public String getCountryString() {
        if (getCountry() != null) {
            return getCountry().toString();
        } else {
            return null;
        }
    }

}
