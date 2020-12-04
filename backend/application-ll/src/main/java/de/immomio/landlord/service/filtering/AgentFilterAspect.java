package de.immomio.landlord.service.filtering;

import de.immomio.data.landlord.bean.landlordfilter.BaseAgentFilterBean;
import de.immomio.landlord.service.user.settings.LandlordUserSettingsService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AgentFilterAspect {

    private final LandlordUserSettingsService settingsService;

    public AgentFilterAspect(LandlordUserSettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @Pointcut("execution(* de.immomio.controller..* (..)) && args(..,filterBean+)")
    public void interceptAgentFilter(BaseAgentFilterBean filterBean) {
    }

    @Before(value = "interceptAgentFilter(filterBean)", argNames = "filterBean")
    public void checkForFilterChange(BaseAgentFilterBean filterBean) {
        settingsService.saveAgentFilter(filterBean);
    }

}
