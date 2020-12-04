package de.immomio.service.location;

import de.immomio.constants.exceptions.ImmomioRuntimeException;
import de.immomio.data.shared.entity.common.City;
import de.immomio.data.shared.entity.common.CityCacheBean;
import de.immomio.data.shared.entity.common.DistrictCacheBean;
import de.immomio.data.shared.entity.common.ZipCodeCacheBean;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Service
public class RegionService {

    private static final String INVALID_ZIPCODE_L = "INVALID_ZIPCODE";

    private static final String zipCodeRegEx = "(?!01000|99999)(0[1-9]\\d{3}|[1-9]\\d{4})";

    private final RegionCacheService regionCacheService;

    @Autowired
    public RegionService(RegionCacheService regionCacheService) {
        this.regionCacheService = regionCacheService;
    }

    public List<CityCacheBean> findCitiesByName(String name) {
        List<CityCacheBean> cityCacheBeans = regionCacheService.getCities();

        return cityCacheBeans
                .stream()
                .filter(city -> StringUtils.containsIgnoreCase(city.getSearchField(), name))
                .collect(Collectors.toList());
    }

    public List<DistrictCacheBean> findDistrictsByCity(City city) {
        return regionCacheService.getDistrictsByCity(city);
    }

    public ZipCodeCacheBean findDistrictsByZipCode(String zipCode) {
        String trimmedZipCode = zipCode.trim();
        if (StringUtils.isBlank(trimmedZipCode) || !trimmedZipCode.matches(zipCodeRegEx)) {
            throw new ImmomioRuntimeException(INVALID_ZIPCODE_L);
        }

        return regionCacheService.getDistrictsByZipCode(trimmedZipCode)
                .stream().filter(district -> district.getZipCode().equals(trimmedZipCode)).findFirst().orElse(null);
    }
}
