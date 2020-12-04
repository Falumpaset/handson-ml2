package de.immomio.importer.server;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.importer.converter.csv.CSVToImportObjectConverter;
import de.immomio.constants.exceptions.MissingHeaderKeysException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@ExtendWith(SpringExtension.class)
public class CSVImporterTest {

    public static final String RELION_OBJECT_ID_PATTERN = "[0-9]{4}\\.[0-9]{5}";

    @Test
    public void testPropertyConversion() throws IOException {

        File csvFile = getCSVFile();
        Reader reader = getReader(csvFile);
        CSVParser csvParser = getCsvRecords(reader);

        int recordNumber = csvParser.getRecords().size();

        CSVToImportObjectConverter csvToImportObjectConverter = getCsvToImportObjectConverter();

        List<Property> properties = null;
        try {
            properties = csvToImportObjectConverter.convert(csvFile, Collections.emptySet());
        } catch (MissingHeaderKeysException e) {
            log.error(e.getMessage(), e);
        }

        assert properties != null;
        assert properties.size() == recordNumber;

        System.out.println("properties.size: " + properties.size());
    }

    private CSVToImportObjectConverter getCsvToImportObjectConverter() {
        return new CSVToImportObjectConverter();
    }

    @Test
    public void testRelionObjectIdConstruction() throws IOException {
        File csvFile = getCSVFile();
        Reader reader = getReader(csvFile);
        CSVParser csvParser = getCsvRecords(reader);
        CSVToImportObjectConverter csvToImportObjectConverter = getCsvToImportObjectConverter();

        csvParser.forEach(record -> {
            assertTrue(csvToImportObjectConverter.csvRecordMatchesImmomioCriteria(record), "TestFile must match immomio criteria for csv import file");
            String uniqueName = csvToImportObjectConverter.getUniqueNameFromRecord(record);
            assertTrue(uniqueNameMatchesPattern(uniqueName), "The generated ID must be of the pattern xxxx.xxxxx");
        });

    }

    private boolean uniqueNameMatchesPattern(String uniqueName) {
        return uniqueName.matches(RELION_OBJECT_ID_PATTERN);
    }

    private CSVParser getCsvRecords(Reader reader) throws IOException {
        return new CSVParser(reader, CSVFormat.INFORMIX_UNLOAD.withFirstRecordAsHeader());
    }

    private Reader getReader(File csvFile) throws FileNotFoundException {
        return new InputStreamReader(new FileInputStream(csvFile.getAbsolutePath()), StandardCharsets.UTF_8);
    }

    private File getCSVFile() {
        return new File("src/test/resources/TestObjekte.csv");
    }
}
