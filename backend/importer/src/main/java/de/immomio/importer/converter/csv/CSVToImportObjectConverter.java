package de.immomio.importer.converter.csv;

import com.google.common.collect.Sets;
import com.neovisionaries.i18n.CountryCode;
import de.immomio.common.upload.FileStoreService;
import de.immomio.constants.exceptions.MissingHeaderKeysException;
import de.immomio.constants.property.EnergyClassType;
import de.immomio.data.base.type.common.FileType;
import de.immomio.data.base.type.property.FlatType;
import de.immomio.data.base.type.property.HeaterType;
import de.immomio.data.base.type.property.HouseType;
import de.immomio.data.base.type.property.ObjectType;
import de.immomio.data.landlord.bean.property.AvailableFrom;
import de.immomio.data.landlord.bean.property.certificate.DemandCertificate;
import de.immomio.data.landlord.bean.property.certificate.EnergyCertificate;
import de.immomio.data.landlord.bean.property.certificate.UsageCertificate;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.data.shared.bean.common.S3File;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;

import static de.immomio.data.landlord.bean.property.certificate.EnergyCertificate.CertificateCreationDate;
import static de.immomio.data.landlord.bean.property.certificate.EnergyCertificate.EnergyCertificateType;
import static de.immomio.importer.converter.csv.CSVRelionConstants.AUSTRIA;
import static de.immomio.importer.converter.csv.CSVRelionConstants.AUSTRIA_1;
import static de.immomio.importer.converter.csv.CSVRelionConstants.AUSTRIA_2;
import static de.immomio.importer.converter.csv.CSVRelionConstants.BEDARFSENERGIEAUSWEIS;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_AVAILABLE_FROM;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_BAILMENT;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_BASE_PRICE;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_CERTIFICATE_TYPE;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_CITY;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_COUNTRY;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_END_ENERGY_CONSUMPTION;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_ENERGY_CERTIFICATE_CREATION_DATE;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_ENERGY_EFFICIENCY_CLASS;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_FURNISHING_DESCRIPTION;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_HALF_ROOMS;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_HEATING_COSTS;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_HEATING_COST_INCLUDED;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_HEATING_TYPE;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_HISTORICAL_BUILDING;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_HOUSE;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_LEVEL;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_LOCATION_DESCRIPTION_TEXT;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_ME;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_OBJECT_MISCELLANEOUS_TEXT;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_OBJECT_TYPE;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_PRIMARY_ENERGY_CONSUMPTION;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_PRIMARY_ENERGY_SOURCE;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_REGION;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_ROOMS;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_SERVICE_CHARGES;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_SIDE_COSTS;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_SIZE;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_STREET_AND_HOUSENUMBER;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_SURCHARGES;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_VACANCY_REASON;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_WATER;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_WI;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_YEAR_OF_CONSTRUCTION;
import static de.immomio.importer.converter.csv.CSVRelionConstants.CSV_ZIPCODE;
import static de.immomio.importer.converter.csv.CSVRelionConstants.DD_MM_YY;
import static de.immomio.importer.converter.csv.CSVRelionConstants.DD_MM_YYYY;
import static de.immomio.importer.converter.csv.CSVRelionConstants.DELIM;
import static de.immomio.importer.converter.csv.CSVRelionConstants.DOT;
import static de.immomio.importer.converter.csv.CSVRelionConstants.EG;
import static de.immomio.importer.converter.csv.CSVRelionConstants.ELECTRICITY;
import static de.immomio.importer.converter.csv.CSVRelionConstants.FERN;
import static de.immomio.importer.converter.csv.CSVRelionConstants.FLAT;
import static de.immomio.importer.converter.csv.CSVRelionConstants.FREE_RENTING;
import static de.immomio.importer.converter.csv.CSVRelionConstants.FW;
import static de.immomio.importer.converter.csv.CSVRelionConstants.GAS;
import static de.immomio.importer.converter.csv.CSVRelionConstants.GERMANY;
import static de.immomio.importer.converter.csv.CSVRelionConstants.GERMANY_1;
import static de.immomio.importer.converter.csv.CSVRelionConstants.HOUSE;
import static de.immomio.importer.converter.csv.CSVRelionConstants.IMMEDIATELY;
import static de.immomio.importer.converter.csv.CSVRelionConstants.KG;
import static de.immomio.importer.converter.csv.CSVRelionConstants.NAH;
import static de.immomio.importer.converter.csv.CSVRelionConstants.NO;
import static de.immomio.importer.converter.csv.CSVRelionConstants.NO_1;
import static de.immomio.importer.converter.csv.CSVRelionConstants.OG;
import static de.immomio.importer.converter.csv.CSVRelionConstants.OIL;
import static de.immomio.importer.converter.csv.CSVRelionConstants.OIL_2;
import static de.immomio.importer.converter.csv.CSVRelionConstants.OIL_3;
import static de.immomio.importer.converter.csv.CSVRelionConstants.REGEX;
import static de.immomio.importer.converter.csv.CSVRelionConstants.TIMEZONE;
import static de.immomio.importer.converter.csv.CSVRelionConstants.UG;
import static de.immomio.importer.converter.csv.CSVRelionConstants.VERBRAUCHSENERGIEAUSWEIS;
import static de.immomio.importer.converter.csv.CSVRelionConstants.WOOD;
import static de.immomio.importer.converter.csv.CSVRelionConstants.YES;
import static de.immomio.importer.converter.csv.CSVRelionConstants.YES_1;

@Slf4j
@Service
public class CSVToImportObjectConverter {

    public List<Property> convert(File file, Set<File> attachments) throws IOException, MissingHeaderKeysException {
        Reader reader = new InputStreamReader(new FileInputStream(file.getAbsolutePath()), StandardCharsets.UTF_8);
        CSVParser csvParser = new CSVParser(reader,
                CSVFormat.INFORMIX_UNLOAD.withFirstRecordAsHeader());

        checkHeader(csvParser);
        List<Property> properties = new ArrayList<>();
        csvParser.forEach(record -> {

            if (csvRecordMatchesImmomioCriteria(record)) {

                PropertyData propertyData = new PropertyData();
                String uniqueName = getUniqueNameFromRecord(record);
                propertyData.setName(uniqueName);
                propertyData.setAddress(getAddressFromRecord(record));
                propertyData.setShowAddress(true);
                convertObjectType(propertyData, record.get(CSV_OBJECT_TYPE));
                propertyData.setConstructionYear(record.get(CSV_YEAR_OF_CONSTRUCTION));
                propertyData.setAvailableFrom(new AvailableFrom(availableFrom(record.get(CSV_AVAILABLE_FROM))));
                propertyData.setFurnishingDescription(record.get(CSV_FURNISHING_DESCRIPTION));
                propertyData.setObjectLocationText(record.get(CSV_LOCATION_DESCRIPTION_TEXT));
                propertyData.setObjectMiscellaneousText(record.get(CSV_OBJECT_MISCELLANEOUS_TEXT));
                propertyData.setEnergyCertificate(getEnergyCertificate(record));
                setBooleanTypes(propertyData, record);
                setNumericTypes(propertyData, record);

                setAttachmentForMatchingFlat(propertyData, record, attachments);

                Property property = new Property();
                property.setData(propertyData);
                property.setExternalId(uniqueName);
                properties.add(property);
            }
        });
        return properties;
    }

    private void convertObjectType(PropertyData propertyData, String data) {
        ObjectType objectType = convertFromObjectType(data);
        if (objectType == ObjectType.HOUSE) {
            propertyData.setHouseType(HouseType.DETACHED_HOUSE);
            propertyData.setObjectType(ObjectType.HOUSE);
        } else {
            propertyData.setFlatType(FlatType.APARTMENT);
            propertyData.setObjectType(ObjectType.FLAT);
        }
    }

    public boolean csvRecordMatchesImmomioCriteria(CSVRecord record) {
        ObjectType objectType = convertFromObjectType(record.get(CSV_OBJECT_TYPE));
        return (objectType == ObjectType.FLAT || objectType == ObjectType.HOUSE) &&
                record.get(CSV_VACANCY_REASON).equals(FREE_RENTING);
    }

    private String availableFrom(String value) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DD_MM_YYYY);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(TIMEZONE));
        String refined = value.toLowerCase().trim();

        if (refined.equals(IMMEDIATELY)) {
            Date now = Date.from(Instant.now());
            return simpleDateFormat.format(now);
        } else {
            try {
                return simpleDateFormat.format(refined.trim());
            } catch (Exception e) {
                log.error("Could not format date. Using raw value instead.");
                return value;
            }
        }
    }

    private void setAttachmentForMatchingFlat(
            PropertyData propertyData,
            CSVRecord record,
            Set<File> allAttachments
    ) {

        int wi = Integer.parseInt(record.get(CSV_WI));
        int house = Integer.parseInt(record.get(CSV_HOUSE));
        int me = Integer.parseInt(record.get(CSV_ME));

        allAttachments.forEach(file -> {

            String[] splittedFileName = file.getName().split(REGEX);

            if (wi == Integer.parseInt(splittedFileName[1])) {
                if ("gr".equals(splittedFileName[0]) && me == Integer.parseInt(splittedFileName[3])) {

                    log.info("Found Document for " + propertyData.getName());
                    propertyData.getDocuments().add(fileToS3File(file, FileType.FLOOR_PLAN));

                } else if ("fo".equals(splittedFileName[0]) && house == Integer.parseInt(splittedFileName[2])) {

                    log.info("Found Picture for " + propertyData.getName());
                    propertyData.getAttachments().add(fileToS3File(file, FileType.IMG));
                }
            }
        });
    }

    private S3File fileToS3File(File file, FileType type) {

        String identifier = FileStoreService.generateIdentifier();

        S3File s3File = new S3File();
        s3File.setTitle(file.getName());
        s3File.setType(type);
        s3File.setUrl(file.getAbsolutePath());
        s3File.setIdentifier(identifier);
        s3File.setExtension(FilenameUtils.getExtension(file.getName()).toLowerCase());
        s3File.setEncrypted(false);

        return s3File;
    }

    //this method has been customized towards the needs of the HANOVA customer as they are the only one
    // currently using Relion import. If other customers start using Relion as an import way then we need to think
    // of another solution here
    public String getUniqueNameFromRecord(CSVRecord record) {

        String wi = record.get(CSV_WI);
        String me = record.get(CSV_ME);

        return wi + DOT + me;
    }

    private void setBooleanTypes(PropertyData propertyData, CSVRecord record) {

        try {
            propertyData.setHistoricBuilding(getBooleanTypesFromString(record.get(CSV_HISTORICAL_BUILDING)));
            propertyData.setHeatingCostIncluded(getBooleanTypesFromString(record.get(CSV_HEATING_COST_INCLUDED)));

        } catch (IllegalArgumentException e) {
            log.error("Unexpected value", e);
        }
    }

    private boolean getBooleanTypesFromString(String value) throws IllegalArgumentException {

        if (value == null) {
            throw new IllegalArgumentException("Only 'ja'/'yes' or 'nein'/'no' allowed, but was null");
        }

        switch (value.toLowerCase()) {
            case YES_1:
            case YES:
                return true;
            case NO_1:
            case NO:
                return false;
            default:
                throw new IllegalArgumentException("Only 'ja'/'yes' or 'nein'/'no' allowed, but was: " + value);
        }
    }

    private void setNumericTypes(PropertyData propertyData, CSVRecord record) {
        try {
            propertyData.setBailment(parseDoubleValue(record.get(CSV_BAILMENT)));
            propertyData.setSize(parseDoubleValue(record.get(CSV_SIZE)));
            propertyData.setHeatingCost(parseDoubleValue(record.get(CSV_HEATING_COSTS)));
            propertyData.setBasePrice(parseDoubleValue(record.get(CSV_BASE_PRICE)));
            propertyData.setRooms(calculateRooms(record));
            propertyData.setServiceCharge(calculateServiceCharge(record));
            propertyData.setFloor(convertFloor(record.get(CSV_LEVEL)));

        } catch (IllegalArgumentException e) {
            log.error("Unexpected value", e);
        }
    }

    private Double parseDoubleValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("No value Present.");
        } else {
            return Double.valueOf(value);
        }
    }

    private Double calculateServiceCharge(CSVRecord record) {

        Double extraCharges = parseDoubleValue(record.get(CSV_SIDE_COSTS));
        Double serviceCharges = parseDoubleValue(record.get(CSV_SERVICE_CHARGES));
        Double water = parseDoubleValue(record.get(CSV_WATER));
        Double surcharges = parseDoubleValue(record.get(CSV_SURCHARGES));

        return extraCharges + serviceCharges + water + surcharges;
    }

    private Double calculateRooms(CSVRecord record) {

        Double entireRooms = parseDoubleValue(record.get(CSV_ROOMS));
        Double halfRooms = parseDoubleValue(record.get(CSV_HALF_ROOMS)) / 2;

        return entireRooms + halfRooms;
    }

    private EnergyCertificate getEnergyCertificate(CSVRecord record) {
        EnergyCertificate certificate = new EnergyCertificate();

        certificate.setPrimaryEnergyProvider(getHeaterType(record.get(CSV_PRIMARY_ENERGY_SOURCE)));

        Integer yearOfConstruction = null;
        LocalDate localDate = parseCertificateCreationDate(record.get(CSV_ENERGY_CERTIFICATE_CREATION_DATE));

        try {
            yearOfConstruction = Integer.valueOf(record.get(CSV_YEAR_OF_CONSTRUCTION));
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
        }

        if (yearOfConstruction == null && localDate != null) {
            yearOfConstruction = localDate.getYear();
        }

        if (yearOfConstruction != null) {
            certificate.setYearOfConstruction(yearOfConstruction);
        } else {
            log.warn("Year of construction could not be set for energy certificate.");
        }

        String efficiencyClass = record.get(CSV_ENERGY_EFFICIENCY_CLASS);
        Boolean efficiencyClassIsSet = efficiencyClass != null && !efficiencyClass.isEmpty();

        CertificateCreationDate creationDate = evaluateCreationDate(localDate, efficiencyClassIsSet);
        certificate.setCreationDate(creationDate);
        certificate.setEnergyCertificateType(getCertificateType(record.get(CSV_CERTIFICATE_TYPE)));

        String primaryConsumption = record.get(CSV_PRIMARY_ENERGY_CONSUMPTION);
        certificate.setPrimaryEnergyConsumption(primaryConsumption);
        switch (certificate.getEnergyCertificateType()) {
            case NO_AVAILABLE:
                break;
            case USAGE_IDENTIFICATION:
                certificate.setUsageCertificate(getUsageCertificate(record));
                break;
            case DEMAND_IDENTIFICATION:
                certificate.setDemandCertificate(getDemandCertificate(record));
                break;
        }
        return certificate;
    }

    private CertificateCreationDate evaluateCreationDate(LocalDate date, Boolean efficiencyClassIsSet) {
        if (date != null) {
            LocalDate before = LocalDate.of(2014, Month.MAY, 1);
            LocalDate after = LocalDate.of(2014, Month.APRIL, 30);

            if (date.isBefore(before)) {
                return CertificateCreationDate.APRIL_2014;
            } else if (date.isAfter(after)) {
                return CertificateCreationDate.MAY_2014;
            }
        }
        if (efficiencyClassIsSet) {
            return CertificateCreationDate.MAY_2014;
        } else {
            return CertificateCreationDate.APRIL_2014;
        }
    }

    private LocalDate parseCertificateCreationDate(String creationDate) {
        if (creationDate == null || creationDate.isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern(DD_MM_YY);
            LocalDate localDate = LocalDate.parse(creationDate, inputFormat);
            DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern(DD_MM_YYYY);
            String formatted = outputFormat.format(localDate);

            return LocalDate.parse(formatted, outputFormat);
        } catch (DateTimeParseException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private EnergyCertificateType getCertificateType(String certificateTypeString) {
        String typeString = certificateTypeString.toLowerCase();

        switch (typeString) {
            case VERBRAUCHSENERGIEAUSWEIS:
                return EnergyCertificateType.USAGE_IDENTIFICATION;
            case BEDARFSENERGIEAUSWEIS:
                return EnergyCertificateType.DEMAND_IDENTIFICATION;
            default:
                return EnergyCertificateType.NO_AVAILABLE;
        }
    }

    private DemandCertificate getDemandCertificate(CSVRecord record) {
        DemandCertificate demandCertificate = new DemandCertificate();
        demandCertificate.setEndEnergyConsumption(record.get(CSV_END_ENERGY_CONSUMPTION));
        demandCertificate.setEnergyEfficiencyClass(EnergyClassType.bySymbol(record.get(CSV_ENERGY_EFFICIENCY_CLASS)));

        return demandCertificate;
    }

    private UsageCertificate getUsageCertificate(CSVRecord record) {
        UsageCertificate usageCertificate = new UsageCertificate();
        String energyConsumption = record.get(CSV_END_ENERGY_CONSUMPTION);
        usageCertificate.setEnergyConsumption(energyConsumption);
        usageCertificate.setEnergyConsumptionParameter(energyConsumption);
        usageCertificate.setEnergyEfficiencyClass(EnergyClassType.bySymbol(record.get(CSV_ENERGY_EFFICIENCY_CLASS)));
        try {
            usageCertificate.setIncludesHeatConsumption(getBooleanTypesFromString(record.get(CSV_HEATING_COST_INCLUDED)));
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
        }

        return usageCertificate;
    }


    private HeaterType getHeaterType(String heaterTypeString) {
        StringTokenizer st = new StringTokenizer(heaterTypeString);
        if (st.hasMoreTokens()) {
            String token = st.nextToken(DELIM);
            token = token.toLowerCase();

            if (st.countTokens() > 1) {
                return HeaterType.ALTERNATIVE;
            } else if (token.toLowerCase().contains(FERN) || token.contains(FW) || token.contains(NAH)) {
                return HeaterType.LONG_DISTANCE;
            } else if (token.toLowerCase().contains(GAS)) {
                return HeaterType.GAS;
            } else if (token.toLowerCase().contains(WOOD)) {
                return HeaterType.WOOD;
            } else if (token.toLowerCase().contains(OIL) || token.contains(OIL_2) || token.contains(OIL_3)) {
                return HeaterType.OIL;
            } else if (token.toLowerCase().contains(ELECTRICITY)) {
                return HeaterType.ELECTRO;
            } else {
                log.warn("Unknown heating type: " + token);
                return null;
            }
        } else {
            log.warn("heater type was null.");
            return null;
        }
    }

    private Address getAddressFromRecord(CSVRecord record) {
        Address address = setStreetAndHouseNumber(record.get(CSV_STREET_AND_HOUSENUMBER));
        address.setCountry(translateCountry(record.get(CSV_COUNTRY)));
        address.setZipCode(record.get(CSV_ZIPCODE));
        address.setCity(record.get(CSV_CITY));
        address.setRegion(record.get(CSV_REGION));

        return address;
    }

    private ObjectType convertFromObjectType(String type) {
        switch (type.toLowerCase()) {
            case FLAT:
                return ObjectType.FLAT;
            case HOUSE:
                return ObjectType.HOUSE;
            default:
                return ObjectType.OTHER;
        }
    }

    private int convertFloor(String floorString) {

        if (floorString == null || floorString.equals("")) {
            log.warn("No value for floor found");
            return 0;
        }

        String floor = floorString.toLowerCase();
        if (floor.contains(EG)) {
            return 0;
        } else if (floor.contains(OG)) {

            String value = floor.replaceAll(OG, "");

            return Integer.valueOf(value);
        } else if (floor.contains(UG) || floor.contains(KG)) {
            return -1;
        }

        log.warn("No supported value for floor has been found: " + floorString);
        return 0;
    }

    private CountryCode translateCountry(String germanName) {
        switch (germanName.toLowerCase()) {
            case GERMANY_1:
            case GERMANY:
                return CountryCode.DE;
            case AUSTRIA_1:
            case AUSTRIA_2: //This will make trouble as the files don't support UTF8
            case AUSTRIA:
                return CountryCode.AT;
            default:
                return null;
        }
    }

    //Works as long as the house number starts with a digit
    private Address setStreetAndHouseNumber(String street) {
        Address address = new Address();
        char[] charArray = street.toCharArray();

        for (int i = 0; i < charArray.length; i++) {
            if (Character.isDigit(charArray[i])) {
                StringBuilder sB = new StringBuilder(street);

                address.setStreet(sB.substring(0, i).trim());
                address.setHouseNumber(sB.substring(i, street.length()).trim());
                return address;
            }
        }

        address.setStreet(street);
        return address;
    }

    private void checkHeader(CSVParser csvParser) throws MissingHeaderKeysException {
        Set<String> headerKeys = Sets.newHashSet(
                CSV_WI, CSV_HOUSE, CSV_ME, CSV_OBJECT_TYPE, CSV_STREET_AND_HOUSENUMBER, CSV_COUNTRY, CSV_ZIPCODE,
                CSV_CITY, CSV_REGION, CSV_LEVEL, CSV_LOCATION_DESCRIPTION_TEXT, CSV_ROOMS, CSV_HALF_ROOMS, CSV_SIZE,
                CSV_BASE_PRICE, CSV_SIDE_COSTS, CSV_SERVICE_CHARGES, CSV_HEATING_COSTS, CSV_WATER, CSV_SURCHARGES,
                CSV_BAILMENT, CSV_AVAILABLE_FROM, CSV_FURNISHING_DESCRIPTION, CSV_YEAR_OF_CONSTRUCTION,
                CSV_HEATING_TYPE, CSV_CERTIFICATE_TYPE, CSV_PRIMARY_ENERGY_SOURCE, CSV_END_ENERGY_CONSUMPTION,
                CSV_HEATING_COST_INCLUDED, CSV_ENERGY_EFFICIENCY_CLASS, CSV_OBJECT_MISCELLANEOUS_TEXT,
                CSV_HISTORICAL_BUILDING, CSV_VACANCY_REASON, CSV_ENERGY_CERTIFICATE_CREATION_DATE
        );

        Set<String> headerKeySet = csvParser.getHeaderMap().keySet();
        Set<String> missingKeys = new HashSet<>();
        headerKeys.forEach(s -> {
            if (!headerKeySet.contains(s)) {
                missingKeys.add(s);
            }
        });
        if (!missingKeys.isEmpty()) {
            throw new MissingHeaderKeysException("Unknown csv format. The following header Keys could not be found: " +
                    missingKeys.toString());
        }
    }
}
