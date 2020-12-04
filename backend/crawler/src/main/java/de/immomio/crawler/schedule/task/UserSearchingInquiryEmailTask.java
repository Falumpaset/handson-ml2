package de.immomio.crawler.schedule.task;

import de.immomio.crawler.schedule.task.base.BaseTask;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.model.repository.core.propertysearcher.user.BasePropertySearcherUserProfileRepository;
import de.immomio.model.repository.core.propertysearcher.user.BasePropertySearcherUserRepository;
import de.immomio.service.propertysearcher.PropertySearcherRefreshService;
import de.immomio.service.proposal.ProposalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * @author Niklas Lindemann
 */
@Slf4j
@Component
public class UserSearchingInquiryEmailTask extends BaseTask {

    private static final long AFTER_FOUR_WEEKS_PERIOD = 5L;
    private static final long AFTER_SIX_WEEKS_PERIOD = 3L;
    private static final long AFTER_EIGHT_WEEKS_PERIOD = 1L;
    private static final int DAY_HOURS = 24;
    private BasePropertySearcherUserProfileRepository userProfileRepository;

    private PropertySearcherRefreshService refreshService;

    private ProposalService proposalService;

    @Value("${email.period.searchUntilInternalPoolDifferenceInWeeks}")
    private int internalPoolDifferenceInWeeks;

    @Autowired
    public UserSearchingInquiryEmailTask(
            BasePropertySearcherUserProfileRepository userProfileRepository,
            PropertySearcherRefreshService refreshService,
            ProposalService proposalService
    ) {
        this.userProfileRepository = userProfileRepository;
        this.refreshService = refreshService;
        this.proposalService = proposalService;
    }

    @Override
    public boolean run() {
        sendRefreshEmailsForCommonUsers(AFTER_FOUR_WEEKS_PERIOD);
        sendRefreshEmailsForCommonUsers(AFTER_SIX_WEEKS_PERIOD);
        sendRefreshEmailsForCommonUsers(AFTER_EIGHT_WEEKS_PERIOD);
        sendRefreshEmailsForInternalUsers();

        cleanUpProposals();
        return true;
    }

    private void sendRefreshEmailsForInternalUsers() {
        LocalDateTime end = LocalDateTime.now().plusWeeks(internalPoolDifferenceInWeeks);
        LocalDateTime start = end.minusHours(DAY_HOURS);
        List<PropertySearcherUserProfile> allInInternalPool = userProfileRepository.findAllInInternalPool(toDate(start), toDate(end));
        refreshService.sendSearchInquiryEmails(allInInternalPool);
    }

    private void sendRefreshEmailsForCommonUsers(Long weeks) {
        LocalDateTime end = LocalDateTime.now().plusWeeks(weeks);
        LocalDateTime start = end.minusHours(24);

        List<PropertySearcherUserProfile> usersToRefresh = userProfileRepository.findAllBySearchUntilBetween(toDate(start), toDate(end));
        refreshService.sendSearchInquiryEmails(usersToRefresh);
    }

    private void cleanUpProposals() {
        List<PropertySearcherUserProfile> usersNotSearching = userProfileRepository.findAllBySearchUntilBefore(new Date());
        usersNotSearching.forEach(proposalService::deleteProposals);
    }

    private Date toDate(LocalDateTime to) {
        return Date.from(to.atZone(ZoneId.systemDefault()).toInstant());
    }
}
