package de.immomio.landlord.service.customer;

import de.immomio.data.landlord.bean.customer.LandlordCustomerBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.model.repository.landlord.customer.LandlordCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Freddy Sawma
 */

@Service
public class LandlordCustomerService {

    private final LandlordCustomerRepository customerRepository;

    @Autowired
    public LandlordCustomerService(LandlordCustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Optional<LandlordCustomer> findById(Long id) {
        if (id == null) {
            return null;
        }

        return customerRepository.findById(id);
    }

    public void updateCustomer(LandlordCustomer customer, LandlordCustomerBean customerBean) {
        if (customerBean.getAddress() != null) {
            customer.setAddress(customerBean.getAddress());
        }
        if (customerBean.getDescription() != null) {
            customer.setDescription(customerBean.getDescription());
        }
        if (customerBean.getName() != null) {
            customer.setName(customerBean.getName());
        }
        if (customerBean.getTaxId() != null) {
            customer.setTaxId(customerBean.getTaxId());
        }
        if (customerBean.getInvoiceEmail() != null) {
            customer.setInvoiceEmail(customerBean.getInvoiceEmail());
        }
        if (customerBean.getPaymentMethods() != null && !customerBean.getPaymentMethods().isEmpty()) {
            customer.setPaymentMethods(customerBean.getPaymentMethods());
        }
        customerRepository.save(customer);
    }

}
