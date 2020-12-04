package de.immomio.bean.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
public class TotalUserReportBean implements Serializable {

    private static final long serialVersionUID = -8249660420254945062L;

    private Integer landlordCount;

    private BigDecimal revenueLL;

    private Integer propertySearcherCount;

    private Long proposalsInTotal;

    private Long prospectsInTotal;

    private Long objectsInTotal;

    public BigDecimal getRevenuePerObject() {
        return revenueLL.divide(BigDecimal.valueOf(objectsInTotal), 2, BigDecimal.ROUND_HALF_UP);
    }

}
