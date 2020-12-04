package de.immomio.service;

import de.immomio.cloud.amazon.s3.LandlordS3FileManager;
import de.immomio.config.ApplicationMessageSource;
import de.immomio.service.report.AbstractSelfDisclosureReportService;
import de.immomio.service.report.JasperReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */

@Service
public class LandlordSelfDisclosureReportService
        extends AbstractSelfDisclosureReportService<LandlordS3FileManager, ApplicationMessageSource> {

    private LandlordS3FileManager s3FileManager;

    private ApplicationMessageSource messageSource;

    private JasperReportService jasperReportService;

    @Autowired
    public LandlordSelfDisclosureReportService(
            LandlordS3FileManager s3FileManager,
            ApplicationMessageSource messageSource,
            JasperReportService jasperReportService
    ) {
        this.s3FileManager = s3FileManager;
        this.messageSource = messageSource;
        this.jasperReportService = jasperReportService;
    }

    @Override
    public LandlordS3FileManager getFileManager() {
        return s3FileManager;
    }

    @Override
    public ApplicationMessageSource getMessageSource() {
        return messageSource;
    }

    @Override
    public JasperReportService getJasperReportService() {
        return jasperReportService;
    }
}
