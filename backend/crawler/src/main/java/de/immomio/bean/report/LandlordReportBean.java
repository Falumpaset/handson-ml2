package de.immomio.bean.report;

import de.immomio.data.base.bean.customer.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class LandlordReportBean implements Serializable {

    private static final long serialVersionUID = 1658094302200424770L;

    private Long id;

    private String name;

    private String createdDate;

    private Integer flatsUnderManagement;

    private Long addonCount;

    private String landlordAddonProducts;

    private String email;

    private PaymentMethod paymentMethod;

    private BigDecimal revenue;

    private String subscriptionPeriod;
}
