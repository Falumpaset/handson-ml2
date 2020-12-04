package de.immomio.service.landlord.product.quota;

import de.immomio.beans.landlord.product.quota.QuotaPackageBean;
import de.immomio.data.base.type.product.quota.QuotaProductType;
import de.immomio.data.landlord.entity.product.quota.LandlordQuotaPackage;
import de.immomio.model.repository.service.landlord.product.quota.LandlordQuotaPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */

@Service
public class AdminQuotaPackageService {

    private LandlordQuotaPackageRepository quotaPackageRepository;


    @Autowired
    public AdminQuotaPackageService(LandlordQuotaPackageRepository quotaPackageRepository) {
        this.quotaPackageRepository = quotaPackageRepository;
    }

    public List<QuotaPackageBean> getQuotaPackagesByType(QuotaProductType quotaProductType) {

        List<LandlordQuotaPackage> quotaPackages = quotaPackageRepository.findAllByType(quotaProductType);

        return quotaPackages.stream()
                .map(this::getQuotaPackageBean)
                .collect(Collectors.toList());
    }

    private QuotaPackageBean getQuotaPackageBean(LandlordQuotaPackage landlordQuotaPackage) {

        QuotaPackageBean quotaPackageBean = QuotaPackageBean.builder()
                .id(landlordQuotaPackage.getId())
                .quantity(landlordQuotaPackage.getQuantity())
                .price(BigDecimal.valueOf(landlordQuotaPackage.getPrice().getFixedPart()))
                .type(landlordQuotaPackage.getType())
                .build();

        return quotaPackageBean;
    }
}
