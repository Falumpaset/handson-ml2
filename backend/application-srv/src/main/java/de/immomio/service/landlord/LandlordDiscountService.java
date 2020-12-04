package de.immomio.service.landlord;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.discount.LandlordCustomerProductAddonDiscount;
import de.immomio.data.landlord.entity.discount.LandlordCustomerQuotaPackageDiscount;
import de.immomio.data.landlord.entity.discount.LandlordDiscount;
import de.immomio.data.landlord.entity.product.productaddon.LandlordProductAddon;
import de.immomio.data.landlord.entity.product.quota.LandlordQuotaPackage;
import de.immomio.data.shared.bean.discount.LandlordAddonDiscountBean;
import de.immomio.data.shared.bean.discount.LandlordQuotaDiscountBean;
import de.immomio.model.repository.service.landlord.customer.LandlordCustomerRepository;
import de.immomio.model.repository.service.landlord.discount.CustomerProductAddonDiscountRepository;
import de.immomio.model.repository.service.landlord.discount.DiscountRepository;
import de.immomio.model.repository.service.landlord.discount.LandlordCustomerQuotaPackageDiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */

@Service
public class LandlordDiscountService {

    private static final String DISCOUNT_PREFIX = "Admin Panel Discount ";
    private static final String ADDON_DISCOUNT_PREFIX = "Customer specific addon discount";
    private final LandlordCustomerRepository landlordCustomerRepository;
    private final DiscountRepository discountRepository;
    private final CustomerProductAddonDiscountRepository productAddonDiscountRepository;
    private final LandlordCustomerQuotaPackageDiscountRepository quotaPackageDiscountRepository;
    private EntityManager entityManager;

    @Autowired
    public LandlordDiscountService(
            LandlordCustomerRepository landlordCustomerRepository,
            DiscountRepository discountRepository,
            CustomerProductAddonDiscountRepository productAddonDiscountRepository,
            LandlordCustomerQuotaPackageDiscountRepository quotaPackageDiscountRepository,
            EntityManager entityManager
    ) {
        this.landlordCustomerRepository = landlordCustomerRepository;
        this.discountRepository = discountRepository;
        this.productAddonDiscountRepository = productAddonDiscountRepository;
        this.quotaPackageDiscountRepository = quotaPackageDiscountRepository;
        this.entityManager = entityManager;
    }

    public LandlordDiscount createDiscount(Long customerId, LandlordDiscount landlordDiscount) {
        LandlordCustomer customer = landlordCustomerRepository.findById(customerId).get();
        landlordDiscount.setName(DISCOUNT_PREFIX + customer.getName());
        LandlordDiscount savedDiscount = discountRepository.save(landlordDiscount);
        customer.getDiscounts().add(savedDiscount);
        landlordCustomerRepository.save(customer);

        return savedDiscount;
    }

    public LandlordDiscount updateDiscount(LandlordDiscount landlordDiscount) {
        return discountRepository.save(landlordDiscount);
    }

    public void deleteDiscount(LandlordDiscount landlordDiscount) {
        discountRepository.delete(landlordDiscount);
    }

    public void createLandlordAddonDiscounts(
            LandlordCustomer customer,
            List<LandlordAddonDiscountBean> addonDiscountsBeans
    ) {
        addonDiscountsBeans.stream().filter(this::isValidDiscountBean).forEach(discountBean -> {
            LandlordCustomerProductAddonDiscount addonDiscount = findOrCreateAddonDiscount(discountBean, customer);
            if (addonDiscount.isNew()) {
                createLandlordAddonDiscount(customer, discountBean, addonDiscount);
            } else {
                updateLandlordAddonDiscount(discountBean, addonDiscount);
            }
        });
    }

    public void createLandlordQuotaDiscounts(
            LandlordCustomer customer,
            List<LandlordQuotaDiscountBean> quotaDiscountBeans
    ) {
        quotaDiscountBeans.forEach(discountBean -> {
            LandlordCustomerQuotaPackageDiscount quotaDiscount = findOrCreateQuotaDiscount(discountBean, customer);
            if (quotaDiscount.isNew()) {
                createLandlordQuotaDiscount(customer, discountBean, quotaDiscount);
            } else {
                updateLandlordQuotaDiscount(discountBean, quotaDiscount);
            }
        });
    }

    public void updateLandlordAddonDiscount(
            LandlordAddonDiscountBean discountBean,
            LandlordCustomerProductAddonDiscount addonDiscount
    ) {
        LandlordDiscount discount = addonDiscount.getDiscount();
        if (discountNeedsToBeUpdated(discountBean.getValue(), discountBean.getEndDate(), discount)) {
            LandlordDiscount savedDiscount = createLandlordDiscount(discountBean.getValue(), discountBean.getEndDate());
            addonDiscount.setDiscount(savedDiscount);
            productAddonDiscountRepository.save(addonDiscount);
        }
    }

    public void updateLandlordQuotaDiscount(
            LandlordQuotaDiscountBean discountBean,
            LandlordCustomerQuotaPackageDiscount quotaPackageDiscount
    ) {
        LandlordDiscount discount = quotaPackageDiscount.getDiscount();
        if (discountNeedsToBeUpdated(discountBean.getValue(), discountBean.getEndDate(), discount)) {
            LandlordDiscount savedDiscount = createLandlordDiscount(discountBean.getValue(), discountBean.getEndDate());
            quotaPackageDiscount.setDiscount(savedDiscount);
            quotaPackageDiscountRepository.save(quotaPackageDiscount);
        }
    }

    public LandlordCustomerProductAddonDiscount findOrCreateAddonDiscount(
            LandlordAddonDiscountBean discountBean,
            LandlordCustomer customer
    ) {
        List<LandlordCustomerProductAddonDiscount> discounts =
                productAddonDiscountRepository.findAllByCustomer(customer);

        return discounts.stream()
                .filter(item -> item.getProductAddon().getId().equals(discountBean.getAddonId()))
                .findFirst()
                .orElse(new LandlordCustomerProductAddonDiscount());
    }

    public LandlordCustomerQuotaPackageDiscount findOrCreateQuotaDiscount(
            LandlordQuotaDiscountBean discountBean,
            LandlordCustomer customer
    ) {
        List<LandlordCustomerQuotaPackageDiscount> discounts =
                quotaPackageDiscountRepository.findAllByCustomer(customer);

        return discounts.stream()
                .filter(item -> item.getQuotaPackage().getId().equals(discountBean.getQuotaPackageId()))
                .findFirst()
                .orElse(new LandlordCustomerQuotaPackageDiscount());
    }

    public LandlordCustomerProductAddonDiscount createLandlordAddonDiscount(
            LandlordCustomer customer,
            LandlordAddonDiscountBean discountBean,
            LandlordCustomerProductAddonDiscount addonDiscount
    ) {
        LandlordProductAddon productAddonReference = entityManager
                .getReference(LandlordProductAddon.class, discountBean.getAddonId());

        addonDiscount.setCustomer(customer);
        LandlordDiscount savedDiscount = createLandlordDiscount(discountBean.getValue(), discountBean.getEndDate());
        addonDiscount.setDiscount(savedDiscount);
        addonDiscount.setProductAddon(productAddonReference);

        return productAddonDiscountRepository.save(addonDiscount);
    }

    public LandlordCustomerQuotaPackageDiscount createLandlordQuotaDiscount(
            LandlordCustomer customer,
            LandlordQuotaDiscountBean discountBean,
            LandlordCustomerQuotaPackageDiscount quotaPackageDiscount
    ) {
        LandlordQuotaPackage quotaPackageRef = entityManager
                .getReference(LandlordQuotaPackage.class, discountBean.getQuotaPackageId());
        quotaPackageDiscount.setCustomer(customer);
        LandlordDiscount savedDiscount = createLandlordDiscount(discountBean.getValue(), discountBean.getEndDate());
        quotaPackageDiscount.setDiscount(savedDiscount);
        quotaPackageDiscount.setQuotaPackage(quotaPackageRef);

        return quotaPackageDiscountRepository.save(quotaPackageDiscount);
    }

    public List<LandlordAddonDiscountBean> getDiscountsByCustomer(LandlordCustomer customer) {
        List<LandlordCustomerProductAddonDiscount> productAddonDiscounts =
                productAddonDiscountRepository.findAllByCustomer(customer);

        return productAddonDiscounts.stream().map(this::mapToLandlordAddonDiscountBean).collect(Collectors.toList());
    }

    public List<LandlordQuotaDiscountBean> getQuotaDiscountsByCustomer(LandlordCustomer customer) {
        List<LandlordCustomerQuotaPackageDiscount> productAddonDiscounts =
                quotaPackageDiscountRepository.findAllByCustomer(customer);

        return productAddonDiscounts.stream().map(this::mapToLandlordQuotaDiscountBean).collect(Collectors.toList());
    }

    private LandlordAddonDiscountBean mapToLandlordAddonDiscountBean(LandlordCustomerProductAddonDiscount discount) {
        LandlordDiscount landlordDiscount = discount.getDiscount();

        return new LandlordAddonDiscountBean(
                landlordDiscount.getValue(),
                landlordDiscount.getEndDate(),
                discount.getProductAddon().getId()
        );
    }

    private LandlordQuotaDiscountBean mapToLandlordQuotaDiscountBean(LandlordCustomerQuotaPackageDiscount discount) {
        LandlordDiscount landlordDiscount = discount.getDiscount();

        return new LandlordQuotaDiscountBean(
                discount.getQuotaPackage().getId(),
                landlordDiscount.getValue(),
                landlordDiscount.getEndDate());
    }

    private boolean discountNeedsToBeUpdated(Double value, Date endDate, LandlordDiscount discount) {
        return !value.equals(discount.getValue()) ||
                discount.getEndDate().getTime() != endDate.getTime();
    }

    private LandlordDiscount createLandlordDiscount(Double value, Date endDate) {
        LandlordDiscount landlordDiscount = new LandlordDiscount();
        landlordDiscount.setName(ADDON_DISCOUNT_PREFIX);
        landlordDiscount.setValue(value);
        landlordDiscount.setEndDate(endDate);
        landlordDiscount.setStartDate(new Date());

        return discountRepository.save(landlordDiscount);
    }

    private boolean isValidDiscountBean(LandlordAddonDiscountBean landlordAddonDiscountBean) {
        return landlordAddonDiscountBean.getAddonId() != null && landlordAddonDiscountBean.getEndDate() != null && landlordAddonDiscountBean.getValue() != null;
    }

}
