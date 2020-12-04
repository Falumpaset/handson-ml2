package de.immomio.beans;

import de.immomio.data.base.type.addon.AddonType;
import de.immomio.data.shared.bean.price.PriceBean;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Maik Kingma
 */

@Getter
@Setter
public class ProductAddonPriceAndTypeBean implements Serializable {

    private static final long serialVersionUID = -2892132991481522506L;
    private PriceBean priceBean;
    private AddonType addonType;

    public ProductAddonPriceAndTypeBean(PriceBean priceBean, AddonType addonType) {
        this.priceBean = priceBean;
        this.addonType = addonType;
    }
}
