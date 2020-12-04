package de.immomio.service.report;

import de.immomio.beans.ProductAddonPriceAndTypeBean;
import de.immomio.constants.product.ProductSubscriptionPeriod;
import de.immomio.data.base.entity.customer.AbstractCustomerProduct;
import de.immomio.data.base.entity.invoice.AbstractInvoice;
import de.immomio.data.base.type.addon.AddonType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.invoice.LandlordInvoice;
import de.immomio.data.landlord.entity.product.LandlordCustomerProduct;
import de.immomio.data.landlord.entity.product.LandlordProduct;
import de.immomio.data.landlord.entity.product.addon.LandlordAddonProduct;
import de.immomio.data.shared.bean.price.PriceBean;
import de.immomio.model.repository.service.landlord.customer.LandlordCustomerRepository;
import de.immomio.model.repository.service.landlord.invoice.LandlordInvoiceRepository;
import de.immomio.model.repository.service.landlord.product.addon.AddonProductRepository;
import de.immomio.service.landlord.discount.DiscountCalculationService;
import de.immomio.service.landlord.product.price.PriceCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */

@Service
public class CustomerOverviewReportService {

    private static final String DELIMITER = ";";
    private static final String IMMOMIO_DE = "immomio.de";
    private static final String CUSTOMER = "Customer";
    private static final String MULTIPLIER = "Multiplier";
    private static final String SUBSCRIPTION = "Subscription";
    private static final String DISCOUNT = "Discount";
    private static final String NEXT_INCOICE_AMOUNT = "Next Invoice Amount";
    private static final String LAST_INVOICE_AMOUNT = "Last Invoice Amount";
    private static final String LAST_INVOICE_DATE = "Last Invoice Date";
    private static final String NEXT_INVOICE_DATE = "Next Invoice Date";
    private static final String ACTIVE = "Active";
    private static final String LINE_BREAK = "\r\n";

    private LandlordCustomerRepository customerRepository;

    private DiscountCalculationService discountCalculationService;

    private PriceCalculationService priceCalculationService;

    private AddonProductRepository addonProductRepository;

    private LandlordInvoiceRepository invoiceRepository;

    @Autowired
    public CustomerOverviewReportService(
            LandlordCustomerRepository customerRepository,
            DiscountCalculationService discountCalculationService,
            PriceCalculationService priceCalculationService, AddonProductRepository addonProductRepository,
            LandlordInvoiceRepository invoiceRepository
    ) {
        this.customerRepository = customerRepository;
        this.discountCalculationService = discountCalculationService;
        this.priceCalculationService = priceCalculationService;
        this.addonProductRepository = addonProductRepository;
        this.invoiceRepository = invoiceRepository;
    }

    public String getCustomerReport() {
        List<LandlordAddonProduct> addonProducts = addonProductRepository.findAll();
        List<String> allAddons = addonProducts.stream()
                .map(addonProduct -> addonProduct.getAddonType().name())
                .distinct()
                .sorted(Comparator.comparing(Function.identity()))
                .collect(Collectors.toList());

        StringBuilder report = new StringBuilder();
        report.append(CUSTOMER)
                .append(DELIMITER)
                .append(MULTIPLIER)
                .append(DELIMITER)
                .append(SUBSCRIPTION)
                .append(DELIMITER)
                .append(DISCOUNT)
                .append(DELIMITER)
                .append(LAST_INVOICE_DATE)
                .append(DELIMITER)
                .append(LAST_INVOICE_AMOUNT)
                .append(DELIMITER)
                .append(NEXT_INVOICE_DATE)
                .append(DELIMITER)
                .append(NEXT_INCOICE_AMOUNT)
                .append(DELIMITER)
                .append(ACTIVE)
                .append(DELIMITER)
                .append(String.join(DELIMITER, allAddons)).append(LINE_BREAK);
        List<LandlordCustomer> customers = customerRepository.findAll();
        customers.stream()
                .filter(customer -> !customer.getInvoiceEmail().endsWith(IMMOMIO_DE))
                .forEach(customer -> report.append(getCustomerLine(customer)));
        return report.toString();
    }

    private String getCustomerLine(LandlordCustomer customer) {
        LandlordCustomerProduct activeProduct = customer.getActiveProduct();
        LandlordCustomerProduct latestProduct = customer.getProducts().stream().max(Comparator.comparing(AbstractCustomerProduct::getDueDate)).orElse(null);
        LandlordCustomerProduct customerProduct = getProduct(activeProduct, latestProduct);

        if (customerProduct == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        LandlordInvoice lastInvoice = getLastInvoice(customer);
        NumberFormat currencyInstance = NumberFormat.getInstance(Locale.GERMANY);
        stringBuilder.append(customer.getName() != null ? customer.getName() : customer.getInvoiceEmail())
                .append(DELIMITER)
                .append(NumberFormat.getInstance().format(customer.getPriceMultiplier()))
                .append(DELIMITER)
                .append(customerProduct.getProduct().getSubscriptionPeriod().name())
                .append(DELIMITER)
                .append(discountCalculationService.getNewestCustomerDiscount(customer).getValue())
                .append(DELIMITER)
                .append(lastInvoice != null ? lastInvoice.getInvoiceDate() : "")
                .append(DELIMITER)
                .append(lastInvoice != null ? currencyInstance.format(lastInvoice.getPostDiscountGrossPrice()) : "")
                .append(DELIMITER)
                .append(activeProduct != null ? activeProduct.getDueDate() : "")
                .append(DELIMITER)
                .append(activeProduct != null ? currencyInstance.format(getPrice(customer)) : "")
                .append(DELIMITER)
                .append(activeProduct != null)
                .append(DELIMITER)
                .append(String.join(DELIMITER, getAddonAmounts(customerProduct)))
                .append(LINE_BREAK);
       return stringBuilder.toString();
    }

    private LandlordCustomerProduct getProduct(LandlordCustomerProduct activeProduct, LandlordCustomerProduct newestProduct) {
        return activeProduct != null ? activeProduct : newestProduct;
    }

    private BigDecimal getPrice(LandlordCustomer customer) {
        LandlordCustomerProduct activeProduct = customer.getActiveProduct();
        Map<Long, ProductAddonPriceAndTypeBean> calculatedDiscounts = priceCalculationService.getAddonPrices(customer, activeProduct.getProduct());
        calculatedDiscounts.entrySet().removeIf(entry -> activeProduct.getAddons().stream()
                .noneMatch(landlordCustomerAddonProduct -> landlordCustomerAddonProduct.getAddonProduct().getId().equals(entry.getKey())));

        return calculatedDiscounts.values().stream()
                .map(priceAndTypeBean -> calculateAddonPrice(customer, activeProduct, priceAndTypeBean))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateAddonPrice(LandlordCustomer customer, LandlordCustomerProduct activeProduct, ProductAddonPriceAndTypeBean priceAndTypeBean) {
        PriceBean priceBean = priceAndTypeBean.getPriceBean();
        BigDecimal price = activeProduct.getProduct().getSubscriptionPeriod().equals(ProductSubscriptionPeriod.MONTHLY) ?
                priceBean.getPostDiscountMonthlyPriceGross() :
                priceBean.getPostDiscountYearlyPriceGross();

        return BigDecimal.valueOf(getQuantity(customer, priceAndTypeBean.getAddonType()))
                .multiply(price);
    }

    private Long getQuantity(LandlordCustomer customer, AddonType addonType) {
        long quantity = customer.getActiveProduct().getAddons().stream()
                .filter(landlordCustomerAddonProduct -> landlordCustomerAddonProduct.getAddonProduct().getAddonType().equals(addonType))
                .count();
        if (addonType.equals(AddonType.AGENT)) {
            quantity = quantity - 1;
        }
        return quantity;
    }

    private List<String> getAddonAmounts(LandlordCustomerProduct customerProduct) {
        List<AddonType> addonTypes = getAddonTypes(customerProduct.getProduct());

        List<String> amounts = new LinkedList<>();
        addonTypes.forEach(addonType -> {
            long addonCount = customerProduct.getAddons().stream()
                    .filter(addonProduct -> addonProduct.getAddonProduct().getAddonType().equals(addonType)).count();
            amounts.add(String.valueOf(addonCount));
        });
        return amounts;
    }

    private List<AddonType> getAddonTypes(LandlordProduct product) {
        return product.getProductAddons().stream()
                .map(productAddon -> productAddon.getAddonProduct().getAddonType())
                .sorted(Comparator.comparing(AddonType::name))
                .collect(Collectors.toList());
    }

    private LandlordInvoice getLastInvoice(LandlordCustomer customer) {
        List<LandlordInvoice> invoices = invoiceRepository.findAllByCustomer(customer);
        return invoices.stream()
                .filter(landlordInvoice -> !landlordInvoice.isCancellation())
                .max(Comparator.comparing(AbstractInvoice::getInvoiceDate))
                .orElse(null);
    }
}
