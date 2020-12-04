package de.immomio.landlord.analytics;

import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.model.repository.shared.property.PropertyRepository;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
public abstract class BasePropertyApplicationAnalyticsController {

    protected PropertyRepository propertyRepository;

    protected UserSecurityService userSecurityService;

    private static final int[] ACCEPTED_TIME_FRAME = {1, 3, 7, 30};

    @GetMapping(value = "/getApplicationStatisticsFor")
    public ResponseEntity<Object> getApplicationsWithinTimeFrame(
            @RequestParam(value = "timeFrameInDays") Integer days
    ) {
        if (Arrays.stream(ACCEPTED_TIME_FRAME).noneMatch(days::equals)) {
            return new ResponseEntity<>("DAYS_NOT_ALLOWED_L", HttpStatus.BAD_REQUEST);
        }

        Long customerId = userSecurityService.getPrincipalUser().getCustomer().getId();
        List<Property> properties = propertyRepository.findByCustomerId(customerId);

        List<PropertyApplication> applications = new ArrayList<>();
        properties.forEach(property -> applications.addAll(property.getPropertyApplications()));

        DateTime now = DateTime.now();
        Date from = now.minusDays(days).toDate();

        List<PropertyApplication> filteredApplications =
                applications.stream()
                            .filter(propertyApplication -> isBetweenDates(propertyApplication, from, now.toDate()))
                            .collect(Collectors.toList());

        ApplicationAnalyticsResponseBody body = new ApplicationAnalyticsResponseBody();
        if (!filteredApplications.isEmpty()) {
            for (Integer i = 0; i < days; i++) {
                Date upperBoundDate = now.minusDays(i).toDate();
                Date lowerBoundDate = now.minusDays(i + 1).toDate();

                populateBodyWithStats(lowerBoundDate, upperBoundDate, filteredApplications, body);
            }
        }

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    private void populateBodyWithStats(
            Date lowerBoundDate,
            Date upperBoundDate,
            List<PropertyApplication> filteredApplications,
            ApplicationAnalyticsResponseBody body
    ) {
        List<PropertyApplication> dayApplications =
                filteredApplications.stream()
                                    .filter(application -> isBetweenDates(application, lowerBoundDate, upperBoundDate))
                                    .collect(Collectors.toList());

        long total = dayApplications.size();
        long unanswered = countApplicationsByStatus(dayApplications, ApplicationStatus.UNANSWERED);
        long accepted = countApplicationsByStatus(dayApplications, ApplicationStatus.ACCEPTED);
        long rejected = countApplicationsByStatus(dayApplications, ApplicationStatus.REJECTED);

        body.addElementToMap(new OneDayApplicationStatistics(total, unanswered,
                                                             accepted, rejected), lowerBoundDate);
    }

    private long countApplicationsByStatus(List<PropertyApplication> dayApplications, ApplicationStatus status) {
        return dayApplications
                .stream()
                .filter(propertyApplication -> propertyApplication.getStatus() == status).count();
    }

    private boolean isBetweenDates(PropertyApplication propertyApplication, Date from, Date till) {
        return propertyApplication.getCreated().after(from) && propertyApplication.getCreated().before(till);
    }

}
