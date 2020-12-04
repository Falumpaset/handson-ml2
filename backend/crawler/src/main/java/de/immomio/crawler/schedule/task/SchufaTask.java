package de.immomio.crawler.schedule.task;

import de.immomio.crawler.schedule.task.base.BaseTask;
import de.immomio.data.base.bean.schufa.cbi.SchufaConnectorException;
import de.immomio.data.base.bean.schufa.cbi.creditRating.CreditRatingResponse;
import de.immomio.data.base.bean.schufa.cbi.enums.CbiActionType;
import de.immomio.data.base.type.schufa.JobState;
import de.immomio.data.landlord.entity.schufa.LandlordSchufaJob;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUserBean;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.LandlordMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.model.entity.landlord.schufa.SchufaReportBean;
import de.immomio.model.repository.core.landlord.customer.user.BaseLandlordUserRepository;
import de.immomio.model.repository.core.landlord.schufa.BaseLandlordSchufaJobRepository;
import de.immomio.schufa.SchufaConnector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Component
public class SchufaTask extends BaseTask {
    private static final String SCHUFA_UPDATE_SUBJECT = "schufa.report.update";

    private final BaseLandlordSchufaJobRepository landlordSchufaJobRepository;

    private final SchufaConnector schufaConnector;

    private final LandlordMailSender mailSender;

    private final BaseLandlordUserRepository landlordUserRepository;

    @Autowired
    public SchufaTask(
            BaseLandlordSchufaJobRepository landlordSchufaJobRepository,
            SchufaConnector schufaConnector,
            LandlordMailSender mailSender,
            BaseLandlordUserRepository landlordUserRepository
    ) {
        this.landlordSchufaJobRepository = landlordSchufaJobRepository;
        this.schufaConnector = schufaConnector;
        this.mailSender = mailSender;
        this.landlordUserRepository = landlordUserRepository;
    }

    @Override
    public boolean run() {
        List<LandlordSchufaJob> schufaJobs = landlordSchufaJobRepository.findByStateInAndAndType(
                Arrays.asList(JobState.IN_PROGRESS, JobState.ACCEPTED),
                CbiActionType.SCHUFA2_AUSKUNFT_BONITAETSAUSKUNFT);

        schufaJobs.forEach(this::executeSchufaJob);

        return true;
    }

    private void executeSchufaJob(LandlordSchufaJob landlordSchufaJob) {
        try {
            CreditRatingResponse creditRatingResponse = schufaConnector
                    .runCreditRatingInformationJob(
                            landlordSchufaJob.getJobId(),
                            landlordSchufaJob.getCreditRatingCheckResponse());

            JobState currentJobState = null;
            if (creditRatingResponse.getCbiState() != null) {
                currentJobState = creditRatingResponse.getCbiState().getCode();
            }

            if (currentJobState == JobState.IN_PROGRESS) {
                log.info("Job [%s] is running (%s) and state %s",
                        landlordSchufaJob.getId(),
                        landlordSchufaJob.getType(),
                        landlordSchufaJob.getState());
            } else if (isFinished(currentJobState)) {
                log.info("Job [%s] finished (%s) and state %s",
                        landlordSchufaJob.getId(),
                        landlordSchufaJob.getType(),
                        landlordSchufaJob.getState());
            }

            landlordSchufaJob.setState(currentJobState);
            landlordSchufaJob.setCreditRatingResponse(creditRatingResponse);

            landlordSchufaJobRepository.save(landlordSchufaJob);
            sendReportUpdateNotification(landlordSchufaJob.getAgentInfo().getEmail(), landlordSchufaJob);
        } catch (SchufaConnectorException ex) {
            landlordSchufaJob.setState(JobState.ERROR);
            landlordSchufaJobRepository.save(landlordSchufaJob);
            log.info("Running Job exited with: %s", ex);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private boolean isFinished(JobState currentJobState) {
        return currentJobState == JobState.ERROR ||
                currentJobState == JobState.FATAL_ERROR ||
                currentJobState == JobState.PROBLEM ||
                currentJobState == JobState.RESULT;
    }

    private void sendReportUpdateNotification(String email, LandlordSchufaJob schufaJob) {
        Map<String, Object> model = getSchufaReportModel(schufaJob, schufaJob.getAgentInfo().getEmail());

        mailSender.send(email, MailTemplate.SCHUFA_UPDATE, SCHUFA_UPDATE_SUBJECT, model);
    }

    private Map<String, Object> getSchufaReportModel(LandlordSchufaJob schufaJob, String email) {
        Map<String, Object> model = new HashMap<>();
        LandlordUser user = landlordUserRepository.findByEmail(email);

        model.put(ModelParams.MODEL_USER, new LandlordUserBean(user));
        model.put(ModelParams.SCHUFA_REPORT, new SchufaReportBean(schufaJob));

        return model;
    }
}
