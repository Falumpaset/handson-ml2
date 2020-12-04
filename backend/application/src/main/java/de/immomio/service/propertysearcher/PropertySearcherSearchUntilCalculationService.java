package de.immomio.service.propertysearcher;

import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author Niklas Lindemann
 */

@Service
public class PropertySearcherSearchUntilCalculationService {

    private final PropertySearcherOfflineUserService offlineUserService;

    @Value("${email.period.searchUntilInWeeks}")
    private int searchUntilPeriod;
    @Value("${email.period.searchUntilInternalPoolDifferenceInWeeks}")
    private int internalPoolDifferenceInWeeks;
    @Value("${email.period.searchUntilOfflineInMonths}")
    private int searchUntilOfflinePeriod;

    @Autowired
    public PropertySearcherSearchUntilCalculationService(PropertySearcherOfflineUserService offlineUserService) {
        this.offlineUserService = offlineUserService;
    }

    public Date getSearchUntil(PropertySearcherUserProfile userProfile) {
        LocalDate now = LocalDate.now();
        if (offlineUserService.isOfflineProfile(userProfile)) {
            return Date.from(now.plusMonths(searchUntilOfflinePeriod).atStartOfDay(ZoneId.systemDefault()).toInstant());
        } else if (userProfile.getTenantPoolCustomer() != null) {
            int searchUntil = getSearchUntilForInternalPool(userProfile);
            return Date.from(now.plusWeeks(searchUntil).atStartOfDay(ZoneId.systemDefault()).toInstant());
        } else {
            return Date.from(now.plusWeeks(searchUntilPeriod).atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
    }

    private int getSearchUntilForInternalPool(PropertySearcherUserProfile userProfile) {
        Integer tenantPoolReminder = userProfile.getTenantPoolCustomer().getCustomerSettings().getSearchUntilIntervalWeeks();
        int searchUntil = tenantPoolReminder != null ? tenantPoolReminder : searchUntilPeriod;
        searchUntil += internalPoolDifferenceInWeeks;

        return searchUntil;
    }

}
