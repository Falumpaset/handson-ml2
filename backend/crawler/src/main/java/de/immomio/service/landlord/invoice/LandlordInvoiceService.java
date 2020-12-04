package de.immomio.service.landlord.invoice;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.model.repository.core.landlord.invoice.BaseLandlordInvoiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
@Service
public class LandlordInvoiceService {
    private BaseLandlordInvoiceRepository landlordInvoiceRepository;

    @Autowired
    public LandlordInvoiceService(BaseLandlordInvoiceRepository landlordInvoiceRepository) {
        this.landlordInvoiceRepository = landlordInvoiceRepository;
    }

    public BigDecimal getRevenue(Date dateFrom, Date dateTo) {
        Double revenue = landlordInvoiceRepository.getRevenueBetweenDates(dateFrom, dateTo);
        return toBigDecimal(revenue);
    }

    public BigDecimal getRevenue(LandlordCustomer customer, Date dateFrom, Date dateTo) {
        Double revenue = landlordInvoiceRepository.getRevenueByCustomerBetweenDates(customer, dateFrom, dateTo);
        return toBigDecimal(revenue);
    }

    private BigDecimal toBigDecimal(Double aDouble) {
        if (aDouble != null) {
            return BigDecimal.valueOf(aDouble);
        }
        return BigDecimal.ZERO;
    }
}
