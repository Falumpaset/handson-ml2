package de.immomio.service.location;

import de.immomio.data.shared.entity.common.City;
import de.immomio.data.shared.entity.common.CityCacheBean;
import de.immomio.data.shared.entity.common.District;
import de.immomio.data.shared.entity.common.DistrictCacheBean;
import de.immomio.data.shared.entity.common.ZipCodeCacheBean;
import de.immomio.model.repository.shared.location.CityRepository;
import de.immomio.model.repository.shared.location.DistrictRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Service
public class RegionCacheService {

    private static final String ZIPCODE_CACHE = "ZIPCODE_CACHE";
    private static final String ZIPCODE_CACHE_KEY = "'ZIPCODES'";
    private static final String CITY_CACHE = "CITY_CACHE";
    private static final String DISTRICT_CACHE = "DISTRICT_CACHE";
    private static final String CITY_CACHE_KEY = "'CITIES'";

    private final CityRepository cityRepository;

    private final DistrictRepository districtRepository;


    @Autowired
    public RegionCacheService(CityRepository cityRepository, DistrictRepository districtRepository) {
        this.cityRepository = cityRepository;
        this.districtRepository = districtRepository;
    }

    @Cacheable(value = CITY_CACHE, key = CITY_CACHE_KEY, cacheManager = "cacheManager")
    public List<CityCacheBean> getCities() {
        return cityRepository.findAll().stream().map(CityCacheBean::new).collect(Collectors.toList());
    }

    @Cacheable(value = DISTRICT_CACHE, key = "#city.id.toString()", cacheManager = "cacheManager")
    public List<DistrictCacheBean> getDistrictsByCity(City city) {
        return districtRepository.findAllByCityId(city.getId()).stream()
                .map(DistrictCacheBean::new)
                .collect(Collectors.toList());
    }

    @Cacheable(value = ZIPCODE_CACHE, key = "#zipCode", cacheManager = "cacheManager")
    public List<ZipCodeCacheBean> getDistrictsByZipCode(String zipCode) {
        return districtRepository.findAllByZipCodesZipCodeContains(zipCode).stream()
                .map(District::getZipCodes).flatMap(Set::stream)
                .map(ZipCodeCacheBean::new)
                .collect(Collectors.toList());
    }

}
