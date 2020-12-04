package de.immomio.beans.landlord.product.quota;

import de.immomio.data.base.type.product.quota.QuotaProductType;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Niklas Lindemann
 */

@Data
@Builder
public class QuotaPackageBean implements Serializable {
    private static final long serialVersionUID = -2385934307328417799L;

    private Long id;
    private QuotaProductType type;
    private BigDecimal price;
    private BigDecimal postDiscountPrice;
    private Integer quantity;
    private BigDecimal discount = BigDecimal.ZERO;
}
