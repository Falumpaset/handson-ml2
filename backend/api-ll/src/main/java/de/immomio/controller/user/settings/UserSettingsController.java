package de.immomio.controller.user.settings;

import de.immomio.data.landlord.entity.user.settings.LandlordUserSettingsBean;
import de.immomio.landlord.service.user.settings.LandlordUserSettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@RepositoryRestController
@RequestMapping(value = "/usersettings")
public class UserSettingsController {
    private LandlordUserSettingsService userSettingsService;

    @Autowired
    public UserSettingsController(LandlordUserSettingsService userSettingsService) {
        this.userSettingsService = userSettingsService;
    }

    @GetMapping
    public ResponseEntity<LandlordUserSettingsBean> getSettings() {
        return ResponseEntity.ok(userSettingsService.getUserSettings());
    }
}
