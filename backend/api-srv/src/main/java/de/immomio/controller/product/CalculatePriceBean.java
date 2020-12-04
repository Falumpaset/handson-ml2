package de.immomio.controller.product;

import de.immomio.data.base.type.customer.CustomerLocation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class CalculatePriceBean implements Serializable {
    private static final long serialVersionUID = 7122021823284866426L;

    private CustomerLocation location;

    private Double priceMultiplier;

    private Double discount;

    private Double addonDiscount;
}
