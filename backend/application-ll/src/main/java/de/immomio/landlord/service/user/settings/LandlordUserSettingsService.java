package de.immomio.landlord.service.user.settings;

import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataFieldBaseBean;
import de.immomio.data.landlord.bean.landlordfilter.BaseAgentFilterBean;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.settings.CustomDataModalSettingsData;
import de.immomio.data.landlord.entity.user.settings.FollowupSettingsData;
import de.immomio.data.landlord.entity.user.settings.LandlordUserFilterSettings;
import de.immomio.data.landlord.entity.user.settings.LandlordUserSettings;
import de.immomio.data.landlord.entity.user.settings.LandlordUserSettingsBean;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.model.repository.landlord.user.settings.LandlordUserSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Niklas Lindemann
 */
@Service
public class LandlordUserSettingsService {

    private final UserSecurityService userSecurityService;

    private final LandlordUserSettingsRepository settingsRepository;

    @Autowired
    public LandlordUserSettingsService(UserSecurityService userSecurityService, LandlordUserSettingsRepository settingsRepository) {
        this.userSecurityService = userSecurityService;
        this.settingsRepository = settingsRepository;
    }

    public void saveCustomDataModalSettings(List<ApplicationCustomDataFieldBaseBean> fields, boolean anonymised) {
        LandlordUserSettings userSettings = getOrCreateSettings();
        CustomDataModalSettingsData customDataModalSettings = CustomDataModalSettingsData.builder().fields(fields).isAnonymised(anonymised).build();
        userSettings.setCustomDataModalSettingsData(customDataModalSettings);
        settingsRepository.save(userSettings);
    }

    public void saveFollowupIntervals(List<Long> intervals) {
        LandlordUserSettings userSettings = getOrCreateSettings();
        FollowupSettingsData followupSettingsData = FollowupSettingsData.builder().notificationTimeIntervals(intervals).build();
        userSettings.setFollowupSettingsData(followupSettingsData);
        settingsRepository.save(userSettings);
    }

    public LandlordUserSettingsBean getUserSettings() {
        LandlordUserSettings userSettings = userSecurityService.getPrincipalUser().getSettings();
        if (userSettings == null) {
            return new LandlordUserSettingsBean();
        }
        return LandlordUserSettingsBean.builder()
                .followupSettingsData(userSettings.getFollowupSettingsData())
                .customDataModalSettingsData(userSettings.getCustomDataModalSettingsData())
                .filterSettings(userSettings.getLandlordUserFilterSettings())
                .build();
    }

    public void saveAgentFilter(BaseAgentFilterBean filterBean) {
        LandlordUserSettings userSettings = getOrCreateSettings();

        if (filterHasChanged(filterBean, userSettings.getLandlordUserFilterSettings())) {
            userSettings.setLandlordUserFilterSettings(LandlordUserFilterSettings.builder().agentIds(filterBean.getAgents()).build());
            settingsRepository.save(userSettings);
        }
    }

    private boolean filterHasChanged(BaseAgentFilterBean newFilter, LandlordUserFilterSettings userFilter) {
        if (newFilter == null && userFilter == null) {
            return false;
        }
        if (newFilter == null || userFilter == null) {
            return true;
        }

        List<Long> newAgentFilter = newFilter.getAgents();
        List<Long> userAgentFilter = userFilter.getAgentIds();

        if (newAgentFilter == null && userAgentFilter == null) {
            return false;
        }
        if (newAgentFilter == null || userAgentFilter == null) {
            return true;
        }

        return !(userAgentFilter.size() == newAgentFilter.size() && userAgentFilter.containsAll(newAgentFilter));
    }

    private LandlordUserSettings getOrCreateSettings() {
        LandlordUser user = userSecurityService.getPrincipalUser();
        return settingsRepository.findById(user.getId()).orElseGet(() -> {
            LandlordUserSettings settings = new LandlordUserSettings();
            settings.setUser(user);
            return settingsRepository.save(settings);
        });
    }
}
