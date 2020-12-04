package de.immomio.landlord.service.reporting.aggregation;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Andreas Hansen
 */
@Service
public class ReportZipConverter {

    public byte[] getZip(Map<String, String> allReports) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(bos);

        for (Map.Entry<String, String> reportEntry : allReports.entrySet()) {
            ZipEntry zipEntry = new ZipEntry(reportEntry.getKey() + ".csv");
            zipOut.putNextEntry(zipEntry);
            zipOut.write(reportEntry.getValue().getBytes(StandardCharsets.ISO_8859_1));
        }

        zipOut.close();
        byte[] zipContent = bos.toByteArray();
        bos.close();

        return zipContent;
    }

}
