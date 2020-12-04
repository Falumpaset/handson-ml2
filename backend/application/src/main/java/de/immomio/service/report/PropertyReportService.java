package de.immomio.service.report;

import com.google.common.base.CharMatcher;
import de.immomio.common.amazon.s3.AbstractS3FileManager;
import de.immomio.common.file.FileUtilities;
import de.immomio.config.ApplicationMessageSource;
import de.immomio.data.base.type.common.FileType;
import de.immomio.data.base.type.property.BuildingConditionType;
import de.immomio.data.base.type.property.FlatType;
import de.immomio.data.base.type.property.HouseType;
import de.immomio.data.base.type.property.ObjectType;
import de.immomio.data.landlord.bean.property.BathroomEquipment;
import de.immomio.data.landlord.bean.property.Contact;
import de.immomio.data.landlord.bean.property.certificate.EnergyCertificate;
import de.immomio.data.landlord.bean.property.certificate.UsageCertificate;
import de.immomio.data.landlord.bean.property.data.IntercomType;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.data.shared.bean.common.GeoCoordinates;
import de.immomio.data.shared.bean.common.ParkingSpace;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.service.landlord.selfdisclosure.PdfMergerService;
import de.immomio.utils.FileStorageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static de.immomio.service.report.PropertyReportParams.ADDRESS_CITY;
import static de.immomio.service.report.PropertyReportParams.ADDRESS_COUNTRY;
import static de.immomio.service.report.PropertyReportParams.ADDRESS_FULL;
import static de.immomio.service.report.PropertyReportParams.ADDRESS_HOUSE_NUMBER;
import static de.immomio.service.report.PropertyReportParams.ADDRESS_LATITUDE;
import static de.immomio.service.report.PropertyReportParams.ADDRESS_LONGITUDE;
import static de.immomio.service.report.PropertyReportParams.ADDRESS_STREET;
import static de.immomio.service.report.PropertyReportParams.ADDRESS_ZIP;
import static de.immomio.service.report.PropertyReportParams.AREA_IMAGE;
import static de.immomio.service.report.PropertyReportParams.BALCONY_IMAGE;
import static de.immomio.service.report.PropertyReportParams.BATHROOM_HAS_BIDET;
import static de.immomio.service.report.PropertyReportParams.BATHROOM_HAS_URINAL;
import static de.immomio.service.report.PropertyReportParams.BATHROOM_HAS_WINDOW;
import static de.immomio.service.report.PropertyReportParams.COMPANY_NAME;
import static de.immomio.service.report.PropertyReportParams.CONTACT_EMAIL;
import static de.immomio.service.report.PropertyReportParams.CONTACT_NAME;
import static de.immomio.service.report.PropertyReportParams.CONTACT_PHONE;
import static de.immomio.service.report.PropertyReportParams.CUSTOMER_LOGO;
import static de.immomio.service.report.PropertyReportParams.ENERGY_CERTIFICATE_CREATION_DATE;
import static de.immomio.service.report.PropertyReportParams.ENERGY_CERTIFICATE_INCLUDES_HEAT_CONSUMPTION;
import static de.immomio.service.report.PropertyReportParams.ENERGY_CONSUMPTION;
import static de.immomio.service.report.PropertyReportParams.ENERGY_PERFORMANCE_CERTIFICATE_TYPE;
import static de.immomio.service.report.PropertyReportParams.FLOOR_PLAN_IMAGES;
import static de.immomio.service.report.PropertyReportParams.GOOGLE_TYPE_ADDRESS_FULL;
import static de.immomio.service.report.PropertyReportParams.HEATER_TYPE;
import static de.immomio.service.report.PropertyReportParams.PRICE_BAILMENT;
import static de.immomio.service.report.PropertyReportParams.PRICE_HEATING_COST;
import static de.immomio.service.report.PropertyReportParams.PRICE_PARKING;
import static de.immomio.service.report.PropertyReportParams.PRICE_SERVICE_CHARGE;
import static de.immomio.service.report.PropertyReportParams.PRICE_TOTAL_RENT;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_AMOUNT_BALCONY;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_AVAILABLE_FROM;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_BATH_ROOMS;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_CONDITION;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_CONSTRUCTION_YEAR;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_DESCRIPTION;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_FLOOR;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_FURNISHING_DESCRIPTION;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_HAS_ATTIC;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_HAS_BALCONY;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_HAS_BARRIER_FREE;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_HAS_BICYCLE_ROOM;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_HAS_ELEVATOR;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_HAS_FIREPLACE;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_HAS_GARDEN;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_HAS_GARDEN_USE;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_HAS_GUEST_TOILETTE;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_HAS_KITCHENETTE;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_HAS_PARKING;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_HAS_SENIORS;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_HAS_SHUTTER;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_HAS_STORE_ROOM;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_HAS_TVSATCABLE;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_HAS_WASH_DRY_ROOM;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_ID;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_IMAGES;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_INTERCOM_TYPE;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_LOCATION_DESCRIPTION;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_MAIN_IMAGE;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_MISCELLANEOUS_TEXT;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_NAME;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_NUMBER_LOGGIAS;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_NUMBER_OF_FLOORS;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_OBJEKT_ID;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_PRICE;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_ROOMS;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_SHOW_ADDRESS;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_SIZE;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_SUB_TYPE;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_TYPE;
import static de.immomio.service.report.PropertyReportParams.PROPERTY_WHEELCHAIR_ACCESSIBLE;
import static de.immomio.service.report.PropertyReportParams.ROOMS_IMAGE;

@Slf4j
@Service
public class PropertyReportService {

    private static final String EXPOSE_REPORT_TEMPLATE_NAME = "report/expose.jrxml";

    private static final String IMG_ROOMS = "report/img/icon-rooms.png";

    private static final String IMG_AREA = "report/img/icon-area.png";

    private static final String IMG_BALCONY = "report/img/icon-balcony.png";

    private static final String FLOOR_PLAN_IMAGE_PATTERNS = "(((?i)(jpg|jpeg|png|gif|bmp))$)";

    private static final String PDF_EXT = "pdf";

    private static final String EXPOSE_REPORT_NAME = "expose-%s";

    private static final char TAB = '\t';

    private static final String TAB_REPLACEMENT = "   ";

    private static final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.GERMANY);

    private final ApplicationMessageSource messageSource;

    private final JasperReportService jasperReportService;

    private final PdfMergerService pdfMergerService;

    @Autowired
    public PropertyReportService(
            ApplicationMessageSource messageSource,
            JasperReportService jasperReportService,
            PdfMergerService pdfMergerService
    ) {
        this.messageSource = messageSource;
        this.jasperReportService = jasperReportService;
        this.pdfMergerService = pdfMergerService;
    }

    public File generatePdfExpose(Property property, AbstractS3FileManager fileManager) {
        URL reportTemplateResource = this.getClass().getClassLoader().getResource(EXPOSE_REPORT_TEMPLATE_NAME);
        File report = null;
        try {
            if (reportTemplateResource != null) {
                String reportTemplate = reportTemplateResource.getFile();
                Map<String, Object> model = generatePdfExposeModel(property);

                report = generatePdfExposeReport(property, fileManager, reportTemplate, model);
            } else {
                log.error(EXPOSE_REPORT_TEMPLATE_NAME + " not found.");
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return report;
    }

    private File generatePdfExposeReport(
            Property property,
            AbstractS3FileManager fileManager,
            String reportTemplate,
            Map<String, Object> model
    ) throws IOException {
        File report = null;

        PropertyData propertyData = property.getData();
        List<File> downloadedFloorPlanImages = getDownloadedFloorPlanImages(propertyData, fileManager);
        if (!downloadedFloorPlanImages.isEmpty()) {
            model.put(FLOOR_PLAN_IMAGES, downloadedFloorPlanImages);
        }

        ByteArrayOutputStream reportStream = (ByteArrayOutputStream) jasperReportService.generatePdfReport(reportTemplate, model);
        if (reportStream != null) {
            String destFileName = getExposePdfFileName(property);
            appendFloorPlanDocumentsIfExist(propertyData, reportStream, fileManager);

            report = File.createTempFile(destFileName, ".pdf");
            reportStream.writeTo(new FileOutputStream(report));
        }

        // we need to delete previously downloaded images
        downloadedFloorPlanImages.forEach(FileUtilities::forceDelete);

        return report;
    }

    private Map<String, Object> generatePdfExposeModel(Property property) {
        Map<String, Object> model = new HashMap<>();

        PropertyData data = property.getData();
        populateMainData(model, property);
        populatePrices(model, data);
        populateContact(model, property);
        populateImages(model, data);
        populateResources(model);
        populateCustomerData(model, property);

        if (data.getAddress() != null) {
            populateAddress(model, data.getAddress());
            populateCoordinates(model, data.getAddress().getCoordinates());
        }

        return model;
    }

    private void populateMainData(Map<String, Object> model, Property property) {
        PropertyData data = property.getData();

        model.put(PROPERTY_ID, property.getId());
        model.put(PROPERTY_ROOMS, data.getRooms());
        model.put(PROPERTY_BATH_ROOMS, data.getBathRooms());
        model.put(PROPERTY_SIZE, data.getSize());
        model.put(PROPERTY_NAME, data.getName());
        model.put(PROPERTY_AVAILABLE_FROM, data.getAvailableFrom() != null ? data.getAvailableFrom().dateAsString() : null);
        model.put(PROPERTY_FLOOR, data.getFloor());
        model.put(PROPERTY_DESCRIPTION, replaceTabsFromTextWithWhitespace(data.getObjectDescription()));
        model.put(PROPERTY_LOCATION_DESCRIPTION, replaceTabsFromTextWithWhitespace(data.getObjectLocationText()));
        model.put(PROPERTY_FURNISHING_DESCRIPTION, replaceTabsFromTextWithWhitespace(data.getFurnishingDescription()));
        model.put(PROPERTY_MISCELLANEOUS_TEXT, replaceTabsFromTextWithWhitespace(data.getObjectMiscellaneousText()));
        model.put(PROPERTY_CONSTRUCTION_YEAR, data.getConstructionYear());
        model.put(PROPERTY_OBJEKT_ID, property.getExternalId());
        model.put(PROPERTY_HAS_FIREPLACE, data.isFireplace());
        model.put(PROPERTY_HAS_GARDEN_USE, data.isGardenUse());
        model.put(PROPERTY_HAS_WASH_DRY_ROOM, data.isWashDryRoom());
        model.put(PROPERTY_HAS_STORE_ROOM, data.isStoreRoom());
        model.put(PROPERTY_HAS_BICYCLE_ROOM, data.isBicycleRoom());
        model.put(PROPERTY_HAS_ATTIC, data.isAttic());
        model.put(PROPERTY_HAS_SENIORS, data.isSeniors());
        model.put(PROPERTY_HAS_GUEST_TOILETTE, data.isGuestToilette());
        model.put(PROPERTY_HAS_KITCHENETTE, data.isKitchenette());
        model.put(PROPERTY_HAS_GARDEN, data.isGarden());
        model.put(PROPERTY_HAS_BARRIER_FREE, data.isBarrierFree());
        model.put(PROPERTY_HAS_ELEVATOR, data.isElevator());
        model.put(PROPERTY_HAS_TVSATCABLE, data.isTvSatCable());
        model.put(PROPERTY_NUMBER_OF_FLOORS, data.getNumberOfFloors());
        model.put(PROPERTY_SHOW_ADDRESS, data.isShowAddress());
        model.put(PROPERTY_HAS_SHUTTER, data.isShutter());
        model.put(PROPERTY_NUMBER_LOGGIAS, truncateDoubleIfNeeded(data.getNumberOfLoggias()));
        model.put(PROPERTY_HAS_BALCONY, data.hasBalcony());
        model.put(PROPERTY_AMOUNT_BALCONY, truncateDoubleIfNeeded(data.getNumberOfBalconies()));
        populateBathroomEquipment(model, data.getBathroomEquipment());
        model.put(PROPERTY_WHEELCHAIR_ACCESSIBLE, data.isWheelchairAccessible());
        populateIntercomType(model, data.getIntercomType());
        List<ParkingSpace> parkingSpaces = data.getParkingSpaces();
        model.put(PROPERTY_HAS_PARKING, parkingSpaces != null && !parkingSpaces.isEmpty());

        BuildingConditionType condition = data.getBuildingCondition();
        if (condition != null) {
            model.put(PROPERTY_CONDITION, getMessage("BuildingConditionType." + condition.name().toLowerCase()));
        }

        if (data.getHeater() != null) {
            model.put(HEATER_TYPE, getMessage("HeaterFiringType." + data.getHeater().name().toLowerCase()));
        }

        populatePropertyTypes(model, data);
        populateEnergyCertificateData(model, data);
    }

    private void populateBathroomEquipment(Map<String, Object> model, BathroomEquipment bathroomEquipment) {
        if (bathroomEquipment != null) {
            model.put(BATHROOM_HAS_URINAL, bathroomEquipment.getUrinal());
            model.put(BATHROOM_HAS_BIDET, bathroomEquipment.getBidet());
            model.put(BATHROOM_HAS_WINDOW, bathroomEquipment.getWindow());
        }
    }

    private void populateIntercomType(Map<String, Object> model, IntercomType intercomType) {
        String intercomTypeString = intercomType != null ? getMessage("IntercomType." + intercomType.name().toLowerCase()) : null;

        model.put(PROPERTY_INTERCOM_TYPE, intercomTypeString);
    }

    private void populatePropertyTypes(Map<String, Object> model, PropertyData data) {
        ObjectType objectType = data.getObjectType();
        if (objectType != null) {
            model.put(PROPERTY_TYPE, getMessage("ObjectType." + data.getObjectType().name().toLowerCase()));

            if (objectType == ObjectType.FLAT) {
                populateFlatPropertyTypes(model, data);
            } else if (objectType == ObjectType.HOUSE) {
                populateHousePropertyTypes(model, data);
            } else {
                populateDefaultPropertyTypes(model, data);
            }
        } else {
            populateDefaultPropertyTypes(model, data);
        }
    }

    private void populateFlatPropertyTypes(Map<String, Object> model, PropertyData data) {
        FlatType flatType = data.getFlatType();
        if (flatType == null) {
            flatType = FlatType.OTHER;
        }

        model.put(PROPERTY_SUB_TYPE, getMessage("FlatType." + flatType.name().toLowerCase()));
    }

    private void populateHousePropertyTypes(Map<String, Object> model, PropertyData data) {
        HouseType houseType = data.getHouseType();
        if (houseType == null) {
            houseType = HouseType.OTHER;
        }
        model.put(PROPERTY_SUB_TYPE, getMessage("HouseType." + houseType.name().toLowerCase()));
    }

    private void populateDefaultPropertyTypes(Map<String, Object> model, PropertyData data) {
        model.put(PROPERTY_TYPE, getMessage("ObjectType." + ObjectType.FLAT.name().toLowerCase()));
        model.put(PROPERTY_SUB_TYPE, getMessage("FlatType." + FlatType.OTHER.name().toLowerCase()));
    }

    private void populateEnergyCertificateData(Map<String, Object> model, PropertyData data) {
        if (data.getEnergyCertificate() != null) {
            EnergyCertificate energyCertificate = data.getEnergyCertificate();

            if (energyCertificate.getEnergyCertificateType() != null) {
                model.put(ENERGY_PERFORMANCE_CERTIFICATE_TYPE, getMessage("EnergyPerformanceCertificateType." +
                        energyCertificate.getEnergyCertificateType().name().toLowerCase()));
            }

            if (energyCertificate.getCreationDate() != null) {
                model.put(ENERGY_CERTIFICATE_CREATION_DATE, energyCertificate.getCreationDate().name());
            }

            UsageCertificate usageCertificate = energyCertificate.getUsageCertificate();
            if (usageCertificate != null) {
                model.put(ENERGY_CERTIFICATE_INCLUDES_HEAT_CONSUMPTION, usageCertificate.isIncludesHeatConsumption());
                model.put(ENERGY_CONSUMPTION, usageCertificate.getEnergyConsumption());
            }
        }
    }

    private void populatePrices(Map<String, Object> model, PropertyData data) {
        model.put(PROPERTY_PRICE, currencyFormatter.format(getValue(data.getBasePrice())));
        model.put(PRICE_BAILMENT, getValue(data.getBailment()));
        model.put(PRICE_HEATING_COST, getValue(data.getHeatingCost()));
        model.put(PRICE_TOTAL_RENT, getValue(data.getTotalRentGross()));
        model.put(PRICE_SERVICE_CHARGE, getValue(data.getServiceCharge()));
        model.put(PRICE_PARKING, getValue(data.getTotalParkingPrice()));
    }

    private void populateAddress(Map<String, Object> model, Address address) {
        model.put(ADDRESS_STREET, address.getStreet());
        model.put(ADDRESS_HOUSE_NUMBER, address.getHouseNumber());
        model.put(ADDRESS_ZIP, address.getZipCode());
        model.put(ADDRESS_CITY, address.getCity());

        String countryCode = address.getCountry() != null ? address.getCountry().getAlpha2() : "";
        model.put(ADDRESS_COUNTRY, countryCode);
        model.put(ADDRESS_FULL, address.toString());
        model.put(GOOGLE_TYPE_ADDRESS_FULL, address.toGoogleString());
    }

    private void populateContact(Map<String, Object> model, Property property) {
        if (property.getData().getContact() != null) {
            Contact contact = property.getData().getContact();
            model.put(CONTACT_NAME, contact.getFullName());
            model.put(CONTACT_EMAIL, contact.getEmail());
            model.put(CONTACT_PHONE, contact.getPhone());
        } else {
            LandlordUser user = property.getUser();
            model.put(CONTACT_NAME, user.fullName());
            model.put(CONTACT_EMAIL, user.getEmail());
            model.put(CONTACT_PHONE, user.getProfile().getPhone());
        }
    }

    private void populateImages(Map<String, Object> model, PropertyData propertyData) {
        List<S3File> attachments = propertyData.getAttachments();
        if (attachments != null && !attachments.isEmpty()) {
            List<S3File> allImages = attachments.stream()
                    .filter(attachment -> attachment != null && attachment.getType() == FileType.IMG)
                    .collect(Collectors.toList());

            List<String> images = new ArrayList<>();
            if (!allImages.isEmpty()) {
                model.put(PROPERTY_MAIN_IMAGE, allImages.get(0).getUrl());

                allImages.forEach(image -> images.add(image.getUrl()));
            }
            model.put(PROPERTY_IMAGES, images);
        }
    }

    private List<File> downloadUploadedDocumentImages(List<S3File> floorPlanImages, AbstractS3FileManager fileManager) {
        List<File> downloaded = new ArrayList<>();
        floorPlanImages.stream()
                .filter(floorPlan -> FLOOR_PLAN_IMAGE_PATTERNS.contains(floorPlan.getExtension()))
                .forEach(s3File -> {
                    try {
                        File file = FileStorageUtils.downloadFile(s3File.getUrl(), fileManager, false, null);
                        downloaded.add(file);
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                });

        return downloaded;
    }

    private boolean isFloorPlanImage(S3File document) {
        if (document.getType() == FileType.FLOOR_PLAN && document.getExtension() != null) {
            Matcher matcher = Pattern.compile(FLOOR_PLAN_IMAGE_PATTERNS).matcher(document.getExtension());

            return matcher.matches();
        }

        return false;
    }

    private boolean isFloorPlanDocument(S3File document) {
        return document.getType() == FileType.FLOOR_PLAN && document.getExtension().equalsIgnoreCase(PDF_EXT);
    }

    private List<S3File> getFloorPlanDocuments(PropertyData propertyData) {
        return propertyData.getDocuments()
                .stream()
                .filter(this::isFloorPlanDocument)
                .collect(Collectors.toList());
    }

    private List<File> getDownloadedFloorPlanImages(PropertyData propertyData, AbstractS3FileManager fileManager) {
        List<S3File> floorPlanImages = propertyData.getDocuments()
                .stream()
                .filter(this::isFloorPlanImage)
                .collect(Collectors.toList());

        return downloadUploadedDocumentImages(floorPlanImages, fileManager);
    }

    private void populateCustomerData(Map<String, Object> model, Property property) {
        LandlordCustomer customer = property.getCustomer();
        model.put(COMPANY_NAME, customer.getName());

        String customerLogo = customer.getBrandingLogo();
        if (!StringUtils.isEmpty(customerLogo)) {
            model.put(CUSTOMER_LOGO, customer.getBrandingLogo());
        }
    }

    private void populateCoordinates(Map<String, Object> model, GeoCoordinates coordinates) {
        if (coordinates != null) {
            model.put(ADDRESS_LONGITUDE, coordinates.getLongitude());
            model.put(ADDRESS_LATITUDE, coordinates.getLatitude());
        }
    }

    private void populateResources(Map<String, Object> model) {
        model.put(AREA_IMAGE, this.getClass().getClassLoader().getResourceAsStream(IMG_AREA));
        model.put(ROOMS_IMAGE, this.getClass().getClassLoader().getResourceAsStream(IMG_ROOMS));
        model.put(BALCONY_IMAGE, this.getClass().getClassLoader().getResourceAsStream(IMG_BALCONY));
    }

    private Double getValue(Double value) {
        return value != null ? value : 0d;
    }

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, key, Locale.GERMAN);
    }

    private String getExposePdfFileName(Property property) {
        return String.format(EXPOSE_REPORT_NAME, property.getId());
    }

    private String truncateDoubleIfNeeded(Double value) {
        if (value == null) {
            return null;
        }
        return new DecimalFormat("#.#").format(value);
    }

    private String replaceTabsFromTextWithWhitespace(String text) {
        return text != null ? CharMatcher.is(TAB).replaceFrom(text, TAB_REPLACEMENT) : null;
    }

    private void appendFloorPlanDocumentsIfExist(
            PropertyData propertyData,
            OutputStream outputStream,
            AbstractS3FileManager fileManager
    ) {
        List<S3File> floorPlanDocuments = getFloorPlanDocuments(propertyData);

        if (!floorPlanDocuments.isEmpty()) {
            pdfMergerService.appendDocuments(floorPlanDocuments, outputStream, fileManager);
        }
    }
}
