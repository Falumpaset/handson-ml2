package de.immomio.controller.dashboard;

import de.immomio.data.base.bean.dashboard.DigitalContractCustomerOverviewBean;
import de.immomio.data.base.bean.dashboard.DigitalContractOverviewBean;
import de.immomio.landlord.service.dashboard.DigitalContractDashboardService;
import de.immomio.landlord.service.security.UserSecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Fabian Beck
 */

@Slf4j
@RepositoryRestController
@RequestMapping(value = "/dashboard/contract")
public class DashboardDigitalContractController {

    private final DigitalContractDashboardService digitalContractDashboardService;

    private final UserSecurityService userSecurityService;

    @Autowired
    public DashboardDigitalContractController(DigitalContractDashboardService digitalContractDashboardService,
            UserSecurityService userSecurityService) {
        this.digitalContractDashboardService = digitalContractDashboardService;
        this.userSecurityService = userSecurityService;
    }

    @GetMapping("/customer")
    public ResponseEntity<DigitalContractCustomerOverviewBean> getDigitalContractCustomerOverview() {
        DigitalContractCustomerOverviewBean dashboardDataBean = digitalContractDashboardService.getCustomerDmvDashboardData(
                userSecurityService.getPrincipalUser().getCustomer());

        return ResponseEntity.ok(dashboardDataBean);
    }

    @GetMapping("/agent")
    public ResponseEntity<DigitalContractOverviewBean> getDigitalContractAgentOverview() {
        DigitalContractOverviewBean dashboardDataBean = digitalContractDashboardService.getAgentDmvDashboardData(
                userSecurityService.getPrincipalUser());

        return ResponseEntity.ok(dashboardDataBean);
    }
}
