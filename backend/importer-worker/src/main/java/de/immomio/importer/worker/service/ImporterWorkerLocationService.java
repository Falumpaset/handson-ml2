package de.immomio.importer.worker.service;

import de.immomio.data.shared.entity.common.City;
import de.immomio.data.shared.entity.common.District;
import de.immomio.data.shared.entity.common.State;
import de.immomio.model.repository.service.shared.location.CityRepository;
import de.immomio.model.repository.service.shared.location.DistrictRepository;
import de.immomio.model.repository.service.shared.location.StateRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */
@Slf4j
@Service
public class ImporterWorkerLocationService {

    private final StateRepository stateRepository;
    private final CityRepository cityRepository;
    private final DistrictRepository districtRepository;

    @Autowired
    public ImporterWorkerLocationService(StateRepository stateRepository, CityRepository cityRepository, DistrictRepository districtRepository) {
        this.stateRepository = stateRepository;
        this.cityRepository = cityRepository;
        this.districtRepository = districtRepository;
    }

    public Optional<State> getStateByZip(String zip) {
        try {
            return stateRepository.findByZipCode(zip);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    Optional<City> getCityByZip(String zip) {
        try {
            return cityRepository.findByZipCode(zip);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    List<District> getDistrictByZip(String zip) {
        try {
            return new ArrayList<>(districtRepository.findAllByZipCodesZipCodeContains(zip));
        } catch (Exception e) {
            log.error("Cannot find Districts by ZipCode: " + zip + e);
            return Collections.emptyList();
        }
    }

}
