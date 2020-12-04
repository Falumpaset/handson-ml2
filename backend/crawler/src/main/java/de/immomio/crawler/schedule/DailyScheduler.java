package de.immomio.crawler.schedule;

import de.immomio.crawler.schedule.task.ArchiveApplicationTask;
import de.immomio.crawler.schedule.task.CheckApplicationMailboxTask;
import de.immomio.crawler.schedule.task.CreateKpiReportTask;
import de.immomio.crawler.schedule.task.CreateProposalsFromBlockedPsTask;
import de.immomio.crawler.schedule.task.DeactivatePropertyTask;
import de.immomio.crawler.schedule.task.DeleteShortUrlsTask;
import de.immomio.crawler.schedule.task.DeleteUserEmailChangeTask;
import de.immomio.crawler.schedule.task.DisableUserWithoutVerifiedEmailTask;
import de.immomio.crawler.schedule.task.DocuSignStatusTask;
import de.immomio.crawler.schedule.task.ExtendCustomerProductTask;
import de.immomio.crawler.schedule.task.FollowupNotificationTask;
import de.immomio.crawler.schedule.task.HandleProductBasketTask;
import de.immomio.crawler.schedule.task.IncompleteProfileTask;
import de.immomio.crawler.schedule.task.ItpNotificationTask;
import de.immomio.crawler.schedule.task.ItpRetryTask;
import de.immomio.crawler.schedule.task.ItpStatusTask;
import de.immomio.crawler.schedule.task.NewPropertyApplicationTask;
import de.immomio.crawler.schedule.task.PostViewingEmailTask;
import de.immomio.crawler.schedule.task.ProfileCompletenessReportTask;
import de.immomio.crawler.schedule.task.PropertyApplicationReminderTask;
import de.immomio.crawler.schedule.task.PropertyInvitationReminderTask;
import de.immomio.crawler.schedule.task.ProposalAcceptanceRateReportTask;
import de.immomio.crawler.schedule.task.RegisteredUserRatioReportTask;
import de.immomio.crawler.schedule.task.SchufaTask;
import de.immomio.crawler.schedule.task.SendMessengerEmailTask;
import de.immomio.crawler.schedule.task.UserSearchingInquiryEmailTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author Johannes Hiemer. Important: Time on server is always -2. Sample: Configured 17.00 o'clock is executed at
 * 19.00 o'clock
 */
@Slf4j
@Service
public class DailyScheduler {

    private final NewPropertyApplicationTask newPropertyApplicationTask;

    private final PropertyApplicationReminderTask propertyApplicationReminderTask;

    private final DeactivatePropertyTask deactivatePropertyTask;

    private final PropertyInvitationReminderTask propertyInvitationReminderTask;

    private final ExtendCustomerProductTask extendCustomerProductTask;

    private final IncompleteProfileTask incompleteProfileTask;

    private final DeleteShortUrlsTask deleteShortUrlsTask;

    private final DeleteUserEmailChangeTask deleteUserEmailChangeTask;

    private final CheckApplicationMailboxTask checkApplicationMailboxTask;

    private final DisableUserWithoutVerifiedEmailTask disableUserWithoutVerifiedEmailTask;

    private final RegisteredUserRatioReportTask registeredUserRatioReportTask;

    private final CreateKpiReportTask createKpiReportTask;

    private final PostViewingEmailTask postViewingEmailTask;

    private final UserSearchingInquiryEmailTask inquiryEmailTask;

    private final SchufaTask schufaTask;

    private final ProfileCompletenessReportTask profileCompletenessReportTask;

    private final CreateProposalsFromBlockedPsTask createProposalsFromBlockedPsTask;

    private final ProposalAcceptanceRateReportTask proposalAcceptanceRateReportTask;

    private final ArchiveApplicationTask archiveApplicationTask;

    private final DocuSignStatusTask docuSignStatusTask;

    private final ItpStatusTask itpStatusTask;

    private final ItpNotificationTask itpNotificationTask;

    private final SendMessengerEmailTask sendMessengerEmailTask;

    private final FollowupNotificationTask followupNotificationTask;

    private final ItpRetryTask itpRetryTask;

    @Autowired
    public DailyScheduler(
            NewPropertyApplicationTask newPropertyApplicationTask,
            PropertyApplicationReminderTask propertyApplicationReminderTask,
            DeactivatePropertyTask deactivatePropertyTask,
            PropertyInvitationReminderTask propertyInvitationReminderTask,
            ExtendCustomerProductTask extendCustomerProductTask,
            IncompleteProfileTask incompleteProfileTask,
            DeleteShortUrlsTask deleteShortUrlsTask,
            DeleteUserEmailChangeTask deleteUserEmailChangeTask,
            CheckApplicationMailboxTask checkApplicationMailboxTask,
            HandleProductBasketTask handleProductBasketTask,
            DisableUserWithoutVerifiedEmailTask disableUserWithoutVerifiedEmailTask,
            RegisteredUserRatioReportTask registeredUserRatioReportTask,
            CreateKpiReportTask createKpiReportTask,
            PostViewingEmailTask postViewingEmailTask,
            UserSearchingInquiryEmailTask inquiryEmailTask,
            SchufaTask schufaTask,
            ProfileCompletenessReportTask profileCompletenessReportTask,
            CreateProposalsFromBlockedPsTask createProposalsFromBlockedPsTask,
            ProposalAcceptanceRateReportTask proposalAcceptanceRateReportTask,
            ArchiveApplicationTask archiveApplicationTask,
            DocuSignStatusTask docuSignStatusTask,
            ItpStatusTask itpStatusTask,
            ItpNotificationTask itpNotificationTask,
            SendMessengerEmailTask sendMessengerEmailTask,
            FollowupNotificationTask followupNotificationTask,
            ItpRetryTask itpRetryTask) {
        this.newPropertyApplicationTask = newPropertyApplicationTask;
        this.propertyApplicationReminderTask = propertyApplicationReminderTask;
        this.deactivatePropertyTask = deactivatePropertyTask;
        this.propertyInvitationReminderTask = propertyInvitationReminderTask;
        this.extendCustomerProductTask = extendCustomerProductTask;
        this.incompleteProfileTask = incompleteProfileTask;
        this.deleteShortUrlsTask = deleteShortUrlsTask;
        this.deleteUserEmailChangeTask = deleteUserEmailChangeTask;
        this.checkApplicationMailboxTask = checkApplicationMailboxTask;
        this.disableUserWithoutVerifiedEmailTask = disableUserWithoutVerifiedEmailTask;
        this.registeredUserRatioReportTask = registeredUserRatioReportTask;
        this.createKpiReportTask = createKpiReportTask;
        this.postViewingEmailTask = postViewingEmailTask;
        this.inquiryEmailTask = inquiryEmailTask;
        this.schufaTask = schufaTask;
        this.profileCompletenessReportTask = profileCompletenessReportTask;
        this.createProposalsFromBlockedPsTask = createProposalsFromBlockedPsTask;
        this.proposalAcceptanceRateReportTask = proposalAcceptanceRateReportTask;
        this.archiveApplicationTask = archiveApplicationTask;
        this.docuSignStatusTask = docuSignStatusTask;
        this.itpStatusTask = itpStatusTask;
        this.itpNotificationTask = itpNotificationTask;
        this.sendMessengerEmailTask = sendMessengerEmailTask;
        this.followupNotificationTask = followupNotificationTask;
        this.itpRetryTask = itpRetryTask;
    }

    //@Scheduled(cron = "0 0 7 * * *")
    public void applicants() {
        log.info("Running new propertyApplication task");

        newPropertyApplicationTask.run();

        log.info("Completed new propertyApplication task");
    }

    //    @Scheduled(cron = "0 0 17 * * *")
    public void application() {
        log.info("Running new propertyApplication task");

        propertyApplicationReminderTask.run();

        log.info("Completed new propertyApplication task");
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void deactivateProperty() {
        log.info("Running new property deactivation task");

        deactivatePropertyTask.run();

        log.info("Completed property deactivation task");
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void calculateProposalAcceptanceRate() {
        log.info("Running proposal acceptance rate task");

        proposalAcceptanceRateReportTask.run();

        log.info("Completed proposal acceptance rate task");
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void calculateProfileCompleteness() {
        log.info("Running profile completeness data export task");

        profileCompletenessReportTask.run();

        log.info("Completed profile completeness data export task");
    }

    @Scheduled(cron = "0 0 6 * * *")
    public void reminder() {
        log.info("Running the property Invitation reminder task");

        propertyInvitationReminderTask.run();

        log.info("Completed the property Invitation reminder task");
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void extendProduct() {
        log.info("Running the Product Extension task");

        extendCustomerProductTask.run();

        log.info("Completed the Product Extension task");

    }

    @Scheduled(cron = "0 0 * * * *")
    public void incompleteProfile() {
        log.info("Running the Incomplete Profile task");

        incompleteProfileTask.run();

        log.info("Completed the Incomplete Profile task");
    }

    @Scheduled(cron = "0 */15 * * * *")
    public void deleteShortUrls() {
        log.info("Running the ShortUrl's delete task");

        deleteShortUrlsTask.run();

        log.info("Completed the ShortUrl's delete task");
    }

    @Scheduled(cron = "0 */30 * * * *")
    @Profile("production")
    public void checkApplicationMailbox() {
        log.info("Running the CheckApplicationMailbox task");

        checkApplicationMailboxTask.run();

        log.info("Completed the CheckApplicationMailbox task");
    }

    @Scheduled(cron = "0 */15 * * * *")
    public void deleteUserEmailChange() {
        log.info("Running the ChangeEmails's delete task");

        deleteUserEmailChangeTask.run();

        log.info("Completed the ChangeEmails's delete task");
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void deactivateUsersWithoutEmailVerification() {
        log.info("Running deactivate users without email verification task");

        disableUserWithoutVerifiedEmailTask.run();

        log.info("Completed deactivate users without email verification task");
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void registeredUserReport() {
        log.info("Running registered user report task");

        registeredUserRatioReportTask.run();

        log.info("Completed registered user report task");
    }

    @Scheduled(cron = "0 0 3 1 */1 ?")
    public void csvKpiReport() {
        log.info("Running csv kpi report task");

        createKpiReportTask.run();

        log.info("Completed csv kpi report task");
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void sendIntentMails() {
        log.info("Running the post viewing email task");

        postViewingEmailTask.run();

        log.info("Completed the post viewing email task");
    }

    @Scheduled(cron = "0 0 7 * * *")
    public void sendSearchingInquiryEmail() {
        log.info("Running the searching inquiry email task");

        inquiryEmailTask.run();

        log.info("Completed the searching inquiry email task");
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void pollSchufa() {
        log.info("Running the schufa polling task");

        schufaTask.run();

        log.info("Completed the schufa polling task");
    }

    @Scheduled(cron = "0 0 7 * * *")
    public void createProposalsForUnblockedUsers() {
        log.info("Running the create proposals for unblocked users task");

        createProposalsFromBlockedPsTask.run();

        log.info("Completed the create proposals for unblocked users task");
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void archiveApplications() {
        log.info("Running the archive application task");

        archiveApplicationTask.run();

        log.info("Completed the archive application  task");
    }

    @Scheduled(cron = "0 */20 * * * *")
    public void docusignStatus() {
        log.info("Running the docusign status task");

        docuSignStatusTask.run();

        log.info("Completed the docusign status task");
    }

    @Scheduled(cron = "0 */15 * * * *")
    public void itpStatus() {
        log.info("Running the itp status task");

        itpStatusTask.run();

        log.info("Completed the itp status task");
    }

    @Scheduled(cron = "0 */15 * * * *")
    public void itpNotification() {
        log.info("Running the itp notification task");

        itpNotificationTask.run();

        log.info("Completed the itp notification task");
    }

    @Scheduled(cron = "0 */15 * * * *")
    public void messageEmails() {
        log.info("Running the send message email task");

        sendMessengerEmailTask.run();

        log.info("Completed the send message email task");
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void followupNotifications() {
        log.info("Running the followup notification task");

        followupNotificationTask.run();

        log.info("Completed the followup notificationtask");
    }

    @Scheduled(cron = "0 0 */1 * * *")
    public void itpRetry() {
        log.info("Running the itp retry task");

        itpRetryTask.run();

        log.info("Completed the itp retry task");
    }
}
