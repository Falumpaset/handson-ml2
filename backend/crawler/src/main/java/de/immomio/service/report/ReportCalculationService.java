package de.immomio.service.report;

import de.immomio.bean.report.LandlordReportBean;
import de.immomio.bean.report.PropertySearcherReportBean;
import de.immomio.bean.report.TotalUserReportBean;
import de.immomio.constants.product.ProductSubscriptionPeriod;
import de.immomio.data.base.bean.customer.PaymentMethod;
import de.immomio.data.base.entity.customer.AbstractCustomerProduct;
import de.immomio.data.base.type.addon.AddonType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.product.LandlordCustomerProduct;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.service.landlord.CrawlerLandlordCustomerService;
import de.immomio.service.landlord.invoice.LandlordInvoiceService;
import de.immomio.service.landlord.property.PropertyService;
import de.immomio.service.propertysearcher.PropertySearcherUserService;
import de.immomio.service.proposal.ProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */

@Service
public class ReportCalculationService {

    private static final String SPACE_AGENTS = " AGENTS";
    private static final String DELIMITER = ", ";
    private static final String DATE_FORMAT = "dd.MM.yyyy";

    private final CrawlerLandlordCustomerService llCustomerReportService;

    private final PropertySearcherUserService psUserReportService;

    private final LandlordInvoiceService landlordInvoiceService;

    private final PropertyService propertyService;

    private final ProposalService proposalService;

    @Autowired
    public ReportCalculationService(
            CrawlerLandlordCustomerService llCustomerReportService,
            PropertySearcherUserService psUserReportService,
            LandlordInvoiceService landlordInvoiceService,
            PropertyService propertyService,
            ProposalService proposalService
    ) {
        this.llCustomerReportService = llCustomerReportService;
        this.psUserReportService = psUserReportService;
        this.landlordInvoiceService = landlordInvoiceService;
        this.propertyService = propertyService;
        this.proposalService = proposalService;
    }

    public TotalUserReportBean fillTotalUserReport(Date startDate, Date endDate) {
        BigDecimal revenue = landlordInvoiceService.getRevenue(startDate, endDate);

        List<PropertySearcherUserProfile> psUsers = psUserReportService.findAll();

        Long proposalsCount = proposalService.getCount(startDate, endDate);

        Long countProspectOptIn = getProspectCount(psUsers);

        List<LandlordCustomer> llCustomers = llCustomerReportService.findAll();

        Long totalObjects = propertyService.count();

        return new TotalUserReportBean(
                llCustomers.size(),
                revenue,
                psUsers.size(),
                proposalsCount,
                countProspectOptIn,
                totalObjects);
    }

    public List<LandlordReportBean> fillLandlordReportList(Date startdate, Date enddate) {
        List<LandlordCustomer> customers = llCustomerReportService.findAll();

        return customers.stream()
                .map(customer -> getLandlordReport(customer, startdate, enddate))
                .collect(Collectors.toList());
    }

    public List<PropertySearcherReportBean> fillPropertySearcherReport() {
        List<PropertySearcherUserProfile> psUsers = psUserReportService.findAll();

        return psUsers.stream()
                .map(PropertySearcherReportBean::new)
                .collect(Collectors.toList());
    }

    private LandlordReportBean createLandlordReport(
            LandlordCustomer landlordCustomer,
            Long addonCount,
            String productNames,
            BigDecimal revenue,
            ProductSubscriptionPeriod subscriptionPeriod) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);

        PaymentMethod paymentMethod = getPaymentMethod(landlordCustomer);
        Integer managementUnits = landlordCustomer.getManagementUnits();
        if (managementUnits == null) {
            managementUnits = 0;
        }
        LandlordReportBean newReport = new LandlordReportBean();
        newReport.setId(landlordCustomer.getId());
        newReport.setName(landlordCustomer.getName());
        newReport.setCreatedDate(format.format(landlordCustomer.getCreated()));
        newReport.setFlatsUnderManagement(managementUnits);
        newReport.setAddonCount(addonCount);
        newReport.setLandlordAddonProducts(productNames);
        newReport.setEmail(landlordCustomer.getInvoiceEmail());
        newReport.setPaymentMethod(paymentMethod);
        newReport.setRevenue(revenue);

        if (subscriptionPeriod != null) {
            newReport.setSubscriptionPeriod(subscriptionPeriod.name());
        }

        return newReport;
    }

    private PaymentMethod getPaymentMethod(LandlordCustomer landlordCustomer) {
        return landlordCustomer.getPaymentMethods().stream()
                    .filter(PaymentMethod::getPreferred)
                    .findAny()
                    .orElse(null);
    }

    private LandlordReportBean getLandlordReport(LandlordCustomer customer, Date startDate, Date endDate) {
        List<LandlordCustomerProduct> products = customer.getProducts();

        Long addonCount = getAddonCount(products);

        String addonNames = getAddonNames(products);

        BigDecimal revenue = landlordInvoiceService.getRevenue(customer, startDate, endDate);

        LandlordCustomerProduct activeProduct = customer.getActiveProduct();

        ProductSubscriptionPeriod subscriptionPeriod = null;

        if (activeProduct != null) {
            subscriptionPeriod = activeProduct.getProduct().getSubscriptionPeriod();
        }

        return createLandlordReport(
                customer,
                addonCount,
                addonNames,
                revenue,
                subscriptionPeriod);
    }

    private Long getAddonCount(List<LandlordCustomerProduct> products) {
        return products.stream()
                    .map(LandlordCustomerProduct::getAddons)
                    .mapToLong(Collection::size).sum();
    }

    private String getAddonNames(List<LandlordCustomerProduct> products) {
        List<AddonType> addonTypes = products.stream()
                .map(AbstractCustomerProduct::getAddons)
                .flatMap(Collection::stream)
                .map(ap -> ap.getAddonProduct().getAddonType())
                .collect(Collectors.toList());

        long agentCount = addonTypes.stream()
                .filter(AddonType.AGENT::equals)
                .count();

        String addonNames = addonTypes.stream()
                .filter(type -> AddonType.AGENT != type)
                .map(AddonType::name)
                .collect(Collectors.joining(DELIMITER));

        addonNames += DELIMITER + agentCount + SPACE_AGENTS;

        return addonNames;
    }

    private Long getProspectCount(List<PropertySearcherUserProfile> psUsers) {
        return psUsers.stream()
                .filter(user -> user.getUser().getProspectOptIn().isOptInForProspect())
                .count();
    }
}
