package de.immomio.service.report;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.governors.MaxPagesGovernor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Service
public class JasperReportService {

    private static final int MAX_PAGES = 500;

    public File generatePdfReport(String template, Map<String, Object> params, String destFileName) throws Exception {
        File file = File.createTempFile(destFileName, ".pdf");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            JasperReport report = initializeReport(template, params);
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, params, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(jasperPrint, fos);
            return file;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return null;
    }

    public OutputStream generatePdfReport(String template, Map<String, Object> params) {
        OutputStream outputStream = null;
        try {
            JasperReport report = initializeReport(template, params);
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, params, new JREmptyDataSource());

            outputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return outputStream;
    }

    private JasperReport initializeReport(String template, Map<String, Object> params) throws JRException {
        JasperReport report = JasperCompileManager.compileReport(template);
        defineCustomProperties(report);
        Locale locale = new Locale("de", "DE");
        params.put(JRParameter.REPORT_LOCALE, locale);

        return report;
    }

    private void defineCustomProperties(JasperReport report) {
        report.setProperty(MaxPagesGovernor.PROPERTY_MAX_PAGES_ENABLED, Boolean.TRUE.toString());
        report.setProperty(MaxPagesGovernor.PROPERTY_MAX_PAGES, String.valueOf(MAX_PAGES));
    }
}
