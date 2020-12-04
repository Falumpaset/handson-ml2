package de.immomio.landlord.service.dashboard;

import de.immomio.data.base.bean.dashboard.DigitalContractCustomerOverviewBean;
import de.immomio.data.base.bean.dashboard.DigitalContractOverviewBean;
import de.immomio.data.base.type.contract.signer.DigitalContractSignerType;
import de.immomio.data.base.type.product.quota.QuotaProductType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.product.quota.LandlordQuotaCustomerProduct;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.shared.entity.contract.history.DigitalContractHistoryStateConstants;
import de.immomio.data.shared.entity.contract.signer.history.DigitalContractSignerHistoryStateConstants;
import de.immomio.model.repository.landlord.product.quota.LandlordQuotaCustomerProductRepository;
import de.immomio.model.repository.shared.contract.DigitalContractRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Fabian Beck
 */

@Slf4j
@Service
public class DigitalContractDashboardService {

    private static final List<String> SIGNER_HISTORY_STATE_WITH_WAITING_STRINGS = Stream.of(DigitalContractSignerHistoryStateConstants.IN_PROGRESS_STATES,
            DigitalContractSignerHistoryStateConstants.WAITING_STATES)
            .flatMap(Collection::stream)
            .map(Objects::toString)
            .collect(Collectors.toList());

    private static final List<String> SIGNER_HISTORY_STATE_STRINGS = Stream.of(DigitalContractSignerHistoryStateConstants.IN_PROGRESS_STATES)
            .flatMap(Collection::stream)
            .map(Objects::toString)
            .collect(Collectors.toList());

    private final LandlordQuotaCustomerProductRepository landlordQuotaCustomerProductRepository;

    private final DigitalContractRepository digitalContractRepository;

    @Autowired
    public DigitalContractDashboardService(
            LandlordQuotaCustomerProductRepository landlordQuotaCustomerProductRepository,
            DigitalContractRepository digitalContractRepository) {
        this.landlordQuotaCustomerProductRepository = landlordQuotaCustomerProductRepository;
        this.digitalContractRepository = digitalContractRepository;
    }

    public DigitalContractCustomerOverviewBean getCustomerDmvDashboardData(LandlordCustomer customer) {
        DigitalContractCustomerOverviewBean overviewBean = new DigitalContractCustomerOverviewBean();

        overviewBean.setContractsRemaining(getRemainingContracts(customer));

        overviewBean.setContractsOpen(digitalContractRepository.countAllByCustomerAndCurrentStateIn(customer,
                DigitalContractHistoryStateConstants.IN_PROGRESS_STATES));
        overviewBean.setContractsSigned(digitalContractRepository.countAllByCustomerAndCurrentStateIn(customer,
                DigitalContractHistoryStateConstants.FINISHED_STATES));
        overviewBean.setContractsWaitingForPS(
                digitalContractRepository.countAllByStateForCustomer(customer,
                        DigitalContractSignerType.TENANT, SIGNER_HISTORY_STATE_WITH_WAITING_STRINGS, SIGNER_HISTORY_STATE_STRINGS,
                        DigitalContractHistoryStateConstants.IN_PROGRESS_STATES));
        overviewBean.setContractsWaitingForLL(
                digitalContractRepository.countAllByStateForCustomer(customer,
                        DigitalContractSignerType.LANDLORD, SIGNER_HISTORY_STATE_WITH_WAITING_STRINGS, SIGNER_HISTORY_STATE_STRINGS,
                        DigitalContractHistoryStateConstants.IN_PROGRESS_STATES));

        return overviewBean;
    }

    public DigitalContractOverviewBean getAgentDmvDashboardData(LandlordUser user) {
        DigitalContractOverviewBean overviewBean = new DigitalContractOverviewBean();

        overviewBean.setContractsOpen(
                digitalContractRepository.countyByStateForAgent(user.getCustomer(),
                        DigitalContractHistoryStateConstants.IN_PROGRESS_STATES, user.getId().toString()));
        overviewBean.setContractsSigned(
                digitalContractRepository.countyByStateForAgent(user.getCustomer(),
                        DigitalContractHistoryStateConstants.FINISHED_STATES, user.getId().toString()));
        overviewBean.setContractsWaitingForPS(
                digitalContractRepository.countBySignerTypeAndStateForAgent(user.getCustomer(),
                        DigitalContractSignerType.TENANT, user.getId().toString(), SIGNER_HISTORY_STATE_WITH_WAITING_STRINGS, SIGNER_HISTORY_STATE_STRINGS,
                        DigitalContractHistoryStateConstants.IN_PROGRESS_STATES));
        overviewBean.setContractsWaitingForLL(
                digitalContractRepository.countBySignerTypeAndStateForAgent(user.getCustomer(),
                        DigitalContractSignerType.LANDLORD, user.getId().toString(), SIGNER_HISTORY_STATE_WITH_WAITING_STRINGS, SIGNER_HISTORY_STATE_STRINGS,
                        DigitalContractHistoryStateConstants.IN_PROGRESS_STATES));

        return overviewBean;
    }

    private Long getRemainingContracts(LandlordCustomer customer) {
        Optional<LandlordQuotaCustomerProduct> optionalQuotaProduct = landlordQuotaCustomerProductRepository.findFirstFromCustomerByType(
                customer, QuotaProductType.DIGITAL_CONTRACT);

        if (optionalQuotaProduct.isEmpty()) {
            return 0L;
        }

        LandlordQuotaCustomerProduct quotaProduct = optionalQuotaProduct.get();
        return quotaProduct.getAvailableQuota();
    }
}
