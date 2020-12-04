package de.immomio.crawler.schedule.task;

import de.immomio.cloud.service.google.GoogleSheetsService;
import de.immomio.crawler.schedule.task.base.BaseTask;
import de.immomio.crawler.schedule.task.utils.EnvironmentService;
import de.immomio.data.base.type.user.PropertySearcherUserType;
import de.immomio.data.propertysearcher.entity.user.ProfileCompletenessResponseBean;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.model.repository.core.propertysearcher.user.BasePropertySearcherUserProfileRepository;
import de.immomio.service.propertysearcher.PropertySearcherUserService;
import de.immomio.utils.CalculationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Component
public class ProfileCompletenessReportTask extends BaseTask {
    private static final String PRODUCTION_CELL = "A1";
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final int PAGE_SIZE = 500;
    private static final int ONE = 1;
    private static final int THIRTY = 30;

    private final GoogleSheetsService googleSheetsService;

    private final BasePropertySearcherUserProfileRepository userProfileRepository;

    private final EnvironmentService environmentService;

    private final PropertySearcherUserService userService;

    @Value("${google.sheets.spreadsheet_id.profile_completeness}")
    private String spreadsheetId;

    @Value("${timezone.europe}")
    private String ZONE_ID;

    @Autowired
    public ProfileCompletenessReportTask(
            GoogleSheetsService googleSheetsService,
            BasePropertySearcherUserProfileRepository userProfileRepository,
            EnvironmentService environmentService, PropertySearcherUserService userService
    ) {
        this.googleSheetsService = googleSheetsService;
        this.userProfileRepository = userProfileRepository;
        this.environmentService = environmentService;
        this.userService = userService;
    }

    @Override
    public boolean run() {
        try {
            if (environmentService.isProduction()) {
                calculateProfileCompletenessAverage();
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);

            return false;
        }

        return true;
    }

    private void calculateProfileCompletenessAverage() throws IOException {
        Date lastThirtyDays = Date.from(getTimeInstantMinusDays(THIRTY));
        Date lastDay = Date.from(getTimeInstantMinusDays(ONE));
        Long lastThirtyDaysUsers = userProfileRepository.countRegisteredByCreatedAfterAndLastLoginNotNull(lastThirtyDays);
        Long lastDayUsers = userProfileRepository.countRegisteredByCreatedAfterAndLastLoginNotNull(lastDay);
        Long totalNumberOfUsers = userProfileRepository.countRegisteredByLastLoginNotNull(PropertySearcherUserType.REGISTERED);

        BigDecimal lastThirtyDaysAverage = getCompletenessAverage(lastThirtyDays, lastThirtyDaysUsers);
        BigDecimal lastDayAverage = getCompletenessAverage(lastDay, lastDayUsers);
        BigDecimal allTimeAverage = getCompletenessAverage(totalNumberOfUsers);

        writePercentToSheet(lastThirtyDaysAverage, allTimeAverage, lastDayAverage);
    }

    private BigDecimal getCompletenessAverage(Long totalNumberOfUsers) {
        return getCompletenessAverage(null, totalNumberOfUsers);
    }

    private BigDecimal getCompletenessAverage(Date lastThirtyDays, Long totalNumberOfUsers) {
        int page = 0;
        double completenessSum = 0;
        Page<PropertySearcherUserProfile> registeredUsers;

        do {
            PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);
            registeredUsers = getPropertySearcherUsers(lastThirtyDays, pageRequest);

            completenessSum += registeredUsers.getContent().stream()
                    .map(userService::calculateProfileCompleteness)
                    .mapToDouble(ProfileCompletenessResponseBean::getCompletenessPercentage)
                    .sum();

            page++;
        } while (!registeredUsers.isLast());

        return CalculationUtils.divide(completenessSum, totalNumberOfUsers);
    }

    private Page<PropertySearcherUserProfile> getPropertySearcherUsers(Date lastThirtyDays, PageRequest pageRequest) {
        if (lastThirtyDays != null) {
            return userProfileRepository.findRegisteredByTypeAndLastLoginNotNullAndCreatedAfter(
                    lastThirtyDays,
                    pageRequest);
        } else {
            return userProfileRepository.findRegisteredByTypeAndLastLoginNotNull(
                    pageRequest);
        }
    }

    private Instant getTimeInstantMinusDays(int days) {
        return LocalDate.now().minusDays(days).atStartOfDay(ZoneId.systemDefault()).toInstant();
    }

    private void writePercentToSheet(BigDecimal lastThirtyDaysAverage,
                                     BigDecimal allTimeAverage,
                                     BigDecimal lastDayAverage
    )throws IOException {
        NumberFormat getPercentInstance = NumberFormat.getPercentInstance(Locale.GERMANY);
        googleSheetsService.writeValue(
                spreadsheetId,
                PRODUCTION_CELL,
                getFormattedDate(),
                getPercentInstance.format(lastThirtyDaysAverage),
                getPercentInstance.format(allTimeAverage),
                getPercentInstance.format(lastDayAverage));
    }

    private String getFormattedDate() {
        return LocalDate.now(ZoneId.of(ZONE_ID)).minusDays(1).format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }
}
