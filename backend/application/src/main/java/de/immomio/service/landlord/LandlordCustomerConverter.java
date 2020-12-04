package de.immomio.service.landlord;

import de.immomio.data.landlord.bean.customer.LandlordCustomerBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import org.springframework.stereotype.Component;

/**
 * @author Niklas Lindemann
 */
@Component
public class LandlordCustomerConverter {

    public LandlordCustomerBean convertCustomerToBean(LandlordCustomer customer) {
        return LandlordCustomerBean.builder()
                .name(customer.getName())
                .address(customer.getAddress())
                .customerSize(customer.getCustomerSize())
                .customerType(customer.getCustomerType())
                .customQuestionAllowed(customer.isCustomQuestionAllowed())
                .description(customer.getDescription())
                .invoiceEmail(customer.getInvoiceEmail())
                .id(customer.getId())
                .paymentMethods(customer.getPaymentMethods())
                .logo(customer.getBrandingLogo())
                .priceMultiplier(customer.getPriceMultiplier())
                .taxId(customer.getTaxId())
                .build();

    }
}
