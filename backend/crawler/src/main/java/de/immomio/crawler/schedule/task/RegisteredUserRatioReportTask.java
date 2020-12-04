package de.immomio.crawler.schedule.task;

import de.immomio.cloud.service.google.GoogleSheetsService;
import de.immomio.crawler.schedule.task.base.BaseTask;
import de.immomio.crawler.schedule.task.utils.EnvironmentService;
import de.immomio.model.repository.core.propertysearcher.user.BasePropertySearcherUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Component
public class RegisteredUserRatioReportTask extends BaseTask {
    private static final String PRODUCTION_CELL = "A1";
    private static final String ZERO = "0";
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final int THIRTY = 30;
    private static final int ONE = 1;

    private final GoogleSheetsService googleSheetsService;

    private final BasePropertySearcherUserRepository userRepository;

    private final DecimalFormat decimalFormatter = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.GERMAN);

    private final EnvironmentService environmentService;

    @Value("${google.sheets.spreadsheet_id.registered_user}")
    private String spreadsheetId;

    @Value("${timezone.europe}")
    private String ZONE_ID;

    @Autowired
    public RegisteredUserRatioReportTask(
            GoogleSheetsService googleSheetsService,
            BasePropertySearcherUserRepository userRepository,
            EnvironmentService environmentService) {
        this.googleSheetsService = googleSheetsService;
        this.userRepository = userRepository;
        this.environmentService = environmentService;
    }

    @Override
    public boolean run() {
        try {
            if (environmentService.isProduction()) {
                log.info("running calculation");
                calculateRatio();
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);

            return false;
        }

        return true;
    }

    private void calculateRatio() throws IOException {
        long anonymousCount = userRepository.countUnregistered();
        long createdAfterOneMonth = userRepository.countByCreatedAfter(getTimeInstantMinusDays(THIRTY));
        long createdAfterOneDay = userRepository.countByCreatedAfter(getTimeInstantMinusDays(ONE));
        long anonymousCountWithinLastMonth = userRepository.countUnregisteredCreatedAfter(getTimeInstantMinusDays(THIRTY));
        long anonymousCountWithinLastDay = userRepository.countUnregisteredCreatedAfter(getTimeInstantMinusDays(ONE));

        BigDecimal all = BigDecimal.valueOf(userRepository.count());
        BigDecimal anonymous = BigDecimal.valueOf(anonymousCount);

        BigDecimal allWithinLastMonth = BigDecimal.valueOf(createdAfterOneMonth);
        BigDecimal anonymousWithinLastMonth = BigDecimal.valueOf(anonymousCountWithinLastMonth);

        BigDecimal allWithinLastDay = BigDecimal.valueOf(createdAfterOneDay);
        BigDecimal anonymousWithinLastDay = BigDecimal.valueOf(anonymousCountWithinLastDay);

        if (zeroValueChecker(Collections.singletonList(all))) {
            writeValuesToSheet(getFormattedDate(), ZERO, ZERO, ZERO);
            return;
        }

        BigDecimal registeredUserRatio = calculateRatio(all, anonymous);
        BigDecimal registeredUserWithinLastMonthRatio = calculateRatio(allWithinLastMonth, anonymousWithinLastMonth);
        BigDecimal registeredUserWithinLastDayRatio = calculateRatio(allWithinLastDay, anonymousWithinLastDay);

        writeValuesToSheet(
                getFormattedDate(),
                ratioFormatter(registeredUserWithinLastMonthRatio),
                ratioFormatter(registeredUserWithinLastDayRatio),
                ratioFormatter(registeredUserRatio));
    }

    private void writeValuesToSheet(String... values) throws IOException {
        googleSheetsService.writeValue(spreadsheetId, PRODUCTION_CELL, values);

    }

    private String ratioFormatter(BigDecimal decimal) {
        return decimalFormatter.format(decimal) + " %";
    }

    private String getFormattedDate() {
        return LocalDate.now(ZoneId.of(ZONE_ID)).minusDays(1).format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    private Date getTimeInstantMinusDays(int days) {
        return Date.from(LocalDate.now().minusDays(days).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private Boolean zeroValueChecker(List<BigDecimal> values) {
        return values.stream().anyMatch(value -> value.equals(BigDecimal.ZERO));
    }

    private BigDecimal calculateRatio(BigDecimal all, BigDecimal anonymous) {
        if (all.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }
        BigDecimal registeredUserRatio = all.subtract(anonymous);
        registeredUserRatio = registeredUserRatio.divide(all, 2, RoundingMode.HALF_UP);
        registeredUserRatio = registeredUserRatio.multiply(BigDecimal.valueOf(100));
        return registeredUserRatio;
    }
}
