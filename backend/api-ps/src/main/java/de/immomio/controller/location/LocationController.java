package de.immomio.controller.location;

import de.immomio.data.shared.entity.common.City;
import de.immomio.data.shared.entity.common.CityCacheBean;
import de.immomio.data.shared.entity.common.DistrictCacheBean;
import de.immomio.service.location.RegionService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Niklas Lindemann
 */


@Slf4j
@RepositoryRestController
@RequestMapping(value = "/locations")
public class LocationController {

    private final RegionService regionService;

    @Autowired
    public LocationController(RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping("/cities")
    public ResponseEntity getCities(@RequestParam("name") String name) {
        List<CityCacheBean> cities = regionService.findCitiesByName(name);

        return ResponseEntity.ok(cities);
    }

    @GetMapping("/districts")
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "city", schema = @Schema(type = "Long"), required = true)})
    public ResponseEntity getDistricts(@RequestParam("city") City city) {
        List<DistrictCacheBean> districts = regionService.findDistrictsByCity(city);

        return ResponseEntity.ok(districts);
    }

}
