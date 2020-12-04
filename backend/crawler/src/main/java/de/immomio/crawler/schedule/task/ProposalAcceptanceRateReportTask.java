package de.immomio.crawler.schedule.task;


import de.immomio.cloud.service.google.GoogleSheetsService;
import de.immomio.crawler.schedule.task.base.BaseTask;
import de.immomio.crawler.schedule.task.utils.EnvironmentService;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposalState;
import de.immomio.model.repository.core.propertysearcher.searchprofile.BasePropertySearcherSearchProfileRepository;
import de.immomio.model.repository.core.shared.propertyProposal.BasePropertyProposalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * @author Freddy Sawma
 */

@Slf4j
@Component
public class ProposalAcceptanceRateReportTask extends BaseTask {

    private static final String PRODUCTION_CELL = "A2";
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final int HUNDRED = 100;
    public static final String PERCENT = "%";

    private final EnvironmentService environmentService;

    private final GoogleSheetsService googleSheetsService;

    private final BasePropertyProposalRepository propertyProposalRepository;

    private final BasePropertySearcherSearchProfileRepository searcherSearchProfileRepository;


    @Value("${google.sheets.spreadsheet_id.proposal_acceptance_rate}")
    private String spreadsheetId;

    @Value("${timezone.europe}")
    private String ZONE_ID;

    @Autowired
    public ProposalAcceptanceRateReportTask(
            GoogleSheetsService googleSheetsService,
            BasePropertyProposalRepository propertyProposalRepository,
            BasePropertySearcherSearchProfileRepository searcherSearchProfileRepository,
            EnvironmentService environmentService) {
        this.googleSheetsService = googleSheetsService;
        this.propertyProposalRepository = propertyProposalRepository;
        this.searcherSearchProfileRepository = searcherSearchProfileRepository;
        this.environmentService = environmentService;
    }

    @Override
    public boolean run() {
        try {
            if (environmentService.isProduction()) {
                calculateProposalAcceptanceRate();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            return false;
        }

        return true;
    }

    private void calculateProposalAcceptanceRate() throws IOException {
        Date lastThirtyDays = Date.from(getTimeInstantMinusDays(30));
        Date startDate = Date.from(getTimeInstantMinusDays(1));
        Date endDate = Date.from(getTimeInstantMinusDays(0));
        Long propertyProposals = propertyProposalRepository.countExcludingInactiveUsers();
        Long proposalsPerDay = propertyProposalRepository.countByCreatedBetween(startDate, endDate);

        Long acceptedProposalsLastThirtyDays =
                propertyProposalRepository.countByAcceptedBetweenAndState(lastThirtyDays,
                        endDate, PropertyProposalState.ACCEPTED);

        Long offeredProposalsLastThirtyDays =
                propertyProposalRepository.countByOfferedBetween(lastThirtyDays, endDate);

        Long searchProfiles = searcherSearchProfileRepository.countExcludingInactiveUsers();
        Long searchProfilesWithMatches = searcherSearchProfileRepository.countProfilesWithMatches();

        BigDecimal profilesWithAtLeastOneMatch =
                BigDecimal.valueOf(searchProfilesWithMatches)
                        .divide(BigDecimal.valueOf(searchProfiles), 2, RoundingMode.HALF_UP);

        BigDecimal averageMatches =
                BigDecimal.valueOf(propertyProposals)
                        .divide(BigDecimal.valueOf(searchProfiles), 2, RoundingMode.HALF_UP);


        writePercentToSheet(offeredProposalsLastThirtyDays,
                acceptedProposalsLastThirtyDays,
                proposalsPerDay,
                averageMatches,
                profilesWithAtLeastOneMatch,
                propertyProposals);
    }

    private void writePercentToSheet(Long offeredProposals,
                                     Long acceptedProposals,
                                     Long numberOfProposalsPerDay,
                                     BigDecimal averageMatches,
                                     BigDecimal profilesWithAtLeastOneMatch,
                                     Long totalNumberOfProposals
    ) throws IOException {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.GERMANY);

        googleSheetsService.writeValue(
                spreadsheetId,
                PRODUCTION_CELL,
                getFormattedDate(),
                numberFormat.format(offeredProposals),
                numberFormat.format(acceptedProposals),
                numberFormat.format(numberOfProposalsPerDay),
                numberFormat.format(averageMatches),
                numberFormat.getPercentInstance().format(profilesWithAtLeastOneMatch),
                numberFormat.format(totalNumberOfProposals));
    }

    private static Instant getTimeInstantMinusDays(int days) {
        return LocalDate.now().minusDays(days).atStartOfDay(ZoneId.systemDefault()).toInstant();
    }

    private String getFormattedDate() {
        return LocalDate.now(ZoneId.of(ZONE_ID)).minusDays(1).format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }
}
