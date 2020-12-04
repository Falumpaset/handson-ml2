package de.immomio.beans.landlord;

import de.immomio.beans.AbstractCustomerRegisterBean;
import de.immomio.data.base.type.customer.CustomerLocation;
import de.immomio.data.base.type.customer.LandlordCustomerSize;
import de.immomio.data.base.type.customer.LandlordCustomerType;
import de.immomio.data.base.type.customer.PaymentMethodType;
import de.immomio.data.shared.bean.common.Address;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
@Setter
public class LandlordCustomerRegisterBean extends AbstractCustomerRegisterBean {

    private String description;

    private String name;

    private String taxId;

    private String invoiceEmail;

    private PaymentMethodType paymentMethod;

    @NotNull
    private LandlordCustomerType customerType;

    private Address address;

    @NotNull
    private CustomerLocation location;

    private Integer managementUnits;

    @NotNull
    private LandlordCustomerSize customerSize;

    private Double priceMultiplier;

    private FtpAccessBean ftpAccess;

    private Map<String, String> preferences;

}
