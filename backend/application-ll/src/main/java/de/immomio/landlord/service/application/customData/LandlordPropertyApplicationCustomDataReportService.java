package de.immomio.landlord.service.application.customData;

import de.immomio.constants.exceptions.ImmomioRuntimeException;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataBundle;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.landlord.service.application.customData.mapper.report.LandlordPropertyApplicationCustomDataReportMapperService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class LandlordPropertyApplicationCustomDataReportService {

    private static final String DOT_XLSX = ".xlsx";
    private static final String INTERESSENTENAUSAHL = "Interessentenausahl-";
    private static final String FILE_SEPERATOR = "-";
    private final LandlordPropertyApplicationCustomDataReportMapperService customDataReportMapperService;

    public LandlordPropertyApplicationCustomDataReportService(LandlordPropertyApplicationCustomDataReportMapperService customDataReportMapperService) {
        this.customDataReportMapperService = customDataReportMapperService;
    }

    public File getCustomDataAsXlsxFile(ApplicationCustomDataBundle customDataBundle, Property property) {
        ByteArrayOutputStream outputStream = customDataReportMapperService.writeDataToStream(customDataBundle, property);

        try {
            File reportFile = File.createTempFile(generateFileName(property), DOT_XLSX);
            outputStream.writeTo(new FileOutputStream(reportFile));
            return reportFile;
        } catch (IOException e) {
            throw new ImmomioRuntimeException();
        }
    }

    private String generateFileName(Property property) {
        return INTERESSENTENAUSAHL +
                property.getId().toString() +
                FILE_SEPERATOR +
                property.getData().getName().replaceAll("\\W+", FILE_SEPERATOR) +
                FILE_SEPERATOR;
    }
}
