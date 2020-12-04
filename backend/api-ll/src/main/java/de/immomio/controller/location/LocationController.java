package de.immomio.controller.location;

import de.immomio.data.shared.entity.common.ZipCodeCacheBean;
import de.immomio.service.location.RegionService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/locations")
@RestController
public class LocationController {


    private final RegionService regionService;

    public LocationController(RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping("/zipCodes")
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "zipCode", schema = @Schema(type = "String"), required = true)})
    public ResponseEntity<ZipCodeCacheBean> getZipCodes(@RequestParam("zipCode") String stringZipCode) {
        ZipCodeCacheBean zipCodeCacheBean = regionService.findDistrictsByZipCode(stringZipCode);
        return zipCodeCacheBean != null ? ResponseEntity.ok(zipCodeCacheBean) : ResponseEntity.notFound().build();
    }
}
