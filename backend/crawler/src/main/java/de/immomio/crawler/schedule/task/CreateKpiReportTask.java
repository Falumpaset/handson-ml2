package de.immomio.crawler.schedule.task;

import de.immomio.crawler.schedule.task.base.BaseTask;
import de.immomio.service.report.SaveReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Service
public class CreateKpiReportTask extends BaseTask {

    private SaveReportService saveReportService;

    @Autowired
    public CreateKpiReportTask(SaveReportService saveReportService) {
        this.saveReportService = saveReportService;
    }

    @Override
    public boolean run() {
        LocalDate endDate = LocalDate.now().minusDays(1);
        LocalDate startDate = endDate.withDayOfMonth(1);

        saveReportService.saveLandlordReport(startDate, endDate);
        saveReportService.savePropertySearcherReport(startDate, endDate);
        saveReportService.saveTotalReport(startDate, endDate);

        return true;
    }
}
