package de.immomio.importer.worker.is24;

import de.immobilienscout24.rest.schema.common._1.ApartmentType;
import de.immobilienscout24.rest.schema.common._1.Attachment;
import de.immobilienscout24.rest.schema.common._1.BuildingEnergyRatingType;
import de.immobilienscout24.rest.schema.common._1.BuildingType;
import de.immobilienscout24.rest.schema.common._1.CountryCode;
import de.immobilienscout24.rest.schema.common._1.EnergyCertificateCreationDate;
import de.immobilienscout24.rest.schema.common._1.EnergyPerformanceCertificate;
import de.immobilienscout24.rest.schema.common._1.EnergySourceEnev2014;
import de.immobilienscout24.rest.schema.common._1.EnergySourcesEnev2014;
import de.immobilienscout24.rest.schema.common._1.HeatingTypeEnev2014;
import de.immobilienscout24.rest.schema.common._1.PDFDocument;
import de.immobilienscout24.rest.schema.common._1.ParkingSpaceType;
import de.immobilienscout24.rest.schema.common._1.Picture;
import de.immobilienscout24.rest.schema.common._1.RealEstateCondition;
import de.immobilienscout24.rest.schema.common._1.Wgs84Address;
import de.immobilienscout24.rest.schema.common._1.YesNoNotApplicableType;
import de.immobilienscout24.rest.schema.common._1.YesNotApplicableType;
import de.immobilienscout24.rest.schema.offer.realestates._1.ApartmentRent;
import de.immobilienscout24.rest.schema.offer.realestates._1.HouseRent;
import de.immobilienscout24.rest.schema.offer.realestates._1.RealEstate;
import de.immomio.common.amazon.s3.S3FileManagerException;
import de.immomio.common.file.FileUtilities;
import de.immomio.common.upload.FileStoreObject;
import de.immomio.common.upload.FileStoreService;
import de.immomio.constants.property.EnergyClassType;
import de.immomio.data.base.type.common.FileType;
import de.immomio.data.base.type.common.ImportStatus;
import de.immomio.data.base.type.common.ParkingType;
import de.immomio.data.base.type.property.BuildingConditionType;
import de.immomio.data.base.type.property.FlatType;
import de.immomio.data.base.type.property.HeaterFiringType;
import de.immomio.data.base.type.property.HeaterType;
import de.immomio.data.base.type.property.HouseType;
import de.immomio.data.base.type.property.ObjectType;
import de.immomio.data.landlord.bean.property.AvailableFrom;
import de.immomio.data.landlord.bean.property.certificate.DemandCertificate;
import de.immomio.data.landlord.bean.property.certificate.EnergyCertificate;
import de.immomio.data.landlord.bean.property.certificate.EnergyCertificate.CertificateCreationDate;
import de.immomio.data.landlord.bean.property.certificate.EnergyCertificate.EnergyCertificateType;
import de.immomio.data.landlord.bean.property.certificate.UsageCertificate;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.importlog.ImportLog;
import de.immomio.data.landlord.entity.prioset.Prioset;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.data.shared.bean.common.GeoCoordinates;
import de.immomio.data.shared.bean.common.ParkingSpace;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.exporter.immoscout.Immoscout24EnergyCertificate;
import de.immomio.importer.worker.s3.ImporterWorkerS3FileManager;
import de.immomio.importer.worker.service.ImporterPropertyMessageSender;
import de.immomio.importer.worker.service.ImporterWorkerImageService;
import de.immomio.model.repository.service.landlord.customer.LandlordCustomerRepository;
import de.immomio.model.repository.service.landlord.customer.importlog.ImportLogRepository;
import de.immomio.model.repository.service.landlord.customer.prioset.PriosetRepository;
import de.immomio.model.repository.service.landlord.customer.property.PropertyRepository;
import de.is24.rest.api.export.api.Is24Api;
import de.is24.rest.api.export.api.impl.IS24ApiImpl;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author Bastian Bliemeister, Maik Kingma.
 */

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class Immoscout24Importer {

    @Value("${immoscout.endpoint}")
    private String ENDPOINT;

    @Value("${immoscout.consumer.key}")
    private String CONSUMER_KEY;

    @Value("${immoscout.secret.key}")
    private String CONSUMER_SECRET;

    private static final String DATE_FORMAT = "dd.MM.yyyy";

    private static final String LAST_REFURBISHMENT_DATE_FORMAT = "yyyy";

    private final ImporterWorkerS3FileManager s3FileManager;

    private final PropertyRepository propertyRepository;

    private final ImportLogRepository ftpImportLogRepository;

    private final LandlordCustomerRepository customerRepository;

    private final PriosetRepository priosetRepository;

    private final ImporterWorkerImageService imageResizeService;

    private final ImporterPropertyMessageSender messageSender;

    private Is24Api isAIs24Api;

    @Autowired
    public Immoscout24Importer(
            ImporterWorkerS3FileManager s3FileManager,
            PropertyRepository propertyRepository,
            ImportLogRepository ftpImportLogRepository,
            LandlordCustomerRepository customerRepository,
            PriosetRepository priosetRepository,
            ImporterWorkerImageService imageResizeService,
            ImporterPropertyMessageSender messageSender
    ) {
        this.s3FileManager = s3FileManager;
        this.propertyRepository = propertyRepository;
        this.ftpImportLogRepository = ftpImportLogRepository;
        this.customerRepository = customerRepository;
        this.priosetRepository = priosetRepository;
        this.imageResizeService = imageResizeService;
        this.messageSender = messageSender;
    }

    private static Address convertAddress(de.immobilienscout24.rest.schema.common._1.Address is24Address) {
        Address address = new Address();

        address.setStreet(is24Address.getStreet());
        address.setHouseNumber(is24Address.getHouseNumber());
        address.setZipCode(is24Address.getPostcode());
        address.setCity(is24Address.getCity());

        if (is24Address.getInternationalCountryRegion() != null) {
            address.setCountry(convertCountry(is24Address.getInternationalCountryRegion().getCountry()));
            address.setRegion(is24Address.getInternationalCountryRegion().getRegion());
        }

        if (!(is24Address instanceof Wgs84Address)) {
            return address;
        }

        Wgs84Address tmp = (Wgs84Address) is24Address;

        if (tmp.getWgs84Coordinate() != null) {
            GeoCoordinates geo = new GeoCoordinates();

            geo.setLatitude(tmp.getWgs84Coordinate().getLatitude());
            geo.setLongitude(tmp.getWgs84Coordinate().getLongitude());

            address.setCoordinates(geo);
        }

        if (tmp.getDescription() != null) {
            address.setAdditional(tmp.getDescription().getText());
        }

        return address;
    }

    private static EnergyCertificate convertEnergyPerformanceCertificate(Immoscout24EnergyCertificate certificate) {
        EnergyPerformanceCertificate epc = certificate.getEnergyCertificate();
        EnergyCertificate ec = new EnergyCertificate();

        if (certificate.getConstructionYear() != null) {
            ec.setYearOfConstruction(certificate.getConstructionYear());
        }

        if (epc == null) {
            return ec;
        }

        boolean hasCreationDate = false;
        if (epc.getEnergyCertificateCreationDate() != null) {
            hasCreationDate = true;

            switch (epc.getEnergyCertificateCreationDate()) {
                case BEFORE_01_MAY_2014:
                    ec.setCreationDate(CertificateCreationDate.APRIL_2014);
                    break;
                case FROM_01_MAY_2014:
                    ec.setCreationDate(CertificateCreationDate.MAY_2014);
                    break;
                default:
                    ec.setCreationDate(null);
            }
        }

        if (certificate.getBuildingEnergyRatingType() == null) {
            ec.setEnergyCertificateType(EnergyCertificateType.NO_AVAILABLE);
        } else if (certificate.getBuildingEnergyRatingType() == BuildingEnergyRatingType.ENERGY_REQUIRED) {
            ec.setEnergyCertificateType(EnergyCertificateType.DEMAND_IDENTIFICATION);

            DemandCertificate dc = new DemandCertificate();

            dc.setEndEnergyConsumption(certificate.getThermalCharacteristic() + "");

            if (hasCreationDate
                    && epc.getEnergyCertificateCreationDate() == EnergyCertificateCreationDate.FROM_01_MAY_2014) {
                dc.setEnergyEfficiencyClass(convertEnergyClassType(epc.getEnergyEfficiencyClass()));
            }

            ec.setDemandCertificate(dc);
        } else if (certificate.getBuildingEnergyRatingType() == BuildingEnergyRatingType.ENERGY_CONSUMPTION) {
            ec.setEnergyCertificateType(EnergyCertificateType.USAGE_IDENTIFICATION);

            UsageCertificate uc = new UsageCertificate();

            uc.setEnergyConsumptionParameter(certificate.getThermalCharacteristic() + "");
            uc.setEnergyConsumption(certificate.getThermalCharacteristic() + "");
            uc.setIncludesHeatConsumption(
                    certificate.getEnergyConsumptionContainsWarmWater() == YesNotApplicableType.YES);

            if (hasCreationDate
                    && epc.getEnergyCertificateCreationDate() == EnergyCertificateCreationDate.FROM_01_MAY_2014) {
                uc.setEnergyEfficiencyClass(convertEnergyClassType(epc.getEnergyEfficiencyClass()));
            }

            ec.setUsageCertificate(uc);
        } else {
            ec.setEnergyCertificateType(EnergyCertificateType.NO_AVAILABLE);
        }

        ec.setPrimaryEnergyProvider(convertPrimaryEnergy(certificate.getEnergySourcesEnev2014()));

        return ec;
    }

    private static HeaterFiringType convertPrimaryType(HeatingTypeEnev2014 heaterType) {
        if (heaterType == null) {
            return null;
        }

        switch (heaterType) {
            case GAS_HEATING:
                return HeaterFiringType.GAS;
            case STOVE_HEATING:
                return HeaterFiringType.OVEN;
            case FLOOR_HEATING:
                return HeaterFiringType.GROUND_FLOOR;
            case SELF_CONTAINED_CENTRAL_HEATING:
                return HeaterFiringType.FLOOR;
            case CENTRAL_HEATING:
            case OIL_HEATING:
            case WOOD_PELLET_HEATING:
            case HEAT_PUMP:
                return HeaterFiringType.CENTRAL;
            case DISTRICT_HEATING:
            case COMBINED_HEAT_AND_POWER_PLANT:
                return HeaterFiringType.LONG_DISTANCE;
            case ELECTRIC_HEATING:
            case SOLAR_HEATING:
            case NIGHT_STORAGE_HEATER:
                return HeaterFiringType.ELECTRIC_HEATING;
            default:
                return null;
        }
    }

    private static HeaterType convertPrimaryEnergy(EnergySourcesEnev2014 energyType) {
        Map<EnergySourceEnev2014, Integer> map = new HashMap<>();
        EnergySourceEnev2014 source = null;
        int count = 0;

        for (EnergySourceEnev2014 ese : energyType.getEnergySourceEnev2014()) {
            Integer cnt = map.get(ese);

            if (cnt == null) {
                cnt = 1;
            } else {
                cnt++;
            }

            if (cnt > count) {
                count = cnt;
                source = ese;
            }

            map.put(ese, cnt);
        }

        if (source == null) {
            return null;
        }

        HeaterType ret = convertPrimaryEnergy(source);

        if (ret != null) {
            return ret;
        }

        for (EnergySourceEnev2014 ese : energyType.getEnergySourceEnev2014()) {
            ret = convertPrimaryEnergy(ese);

            if (ret != null) {
                break;
            }
        }

        return ret;
    }

    private static HeaterType convertPrimaryEnergy(EnergySourceEnev2014 energyType) {
        switch (energyType) {
            case LOCAL_HEATING:
                return HeaterType.BLOCK;
            case COAL:
            case COAL_COKE:
                return HeaterType.COAL;
            case ELECTRICITY:
                return HeaterType.ELECTRO;
            case GAS:
            case ACID_GAS:
            case SOUR_GAS:
                return HeaterType.GAS;
            case GEOTHERMAL:
            case ENVIRONMENTAL_THERMAL_ENERGY:
                return HeaterType.GROUND;
            case LIQUID_GAS:
                return HeaterType.LIQUEFIED_GAS;
            case DISTRICT_HEATING:
            case HEAT_SUPPLY:
            case STEAM_DISTRICT_HEATING:
                return HeaterType.LONG_DISTANCE;
            case OIL:
                return HeaterType.OIL;
            case PELLET_HEATING:
                return HeaterType.PELLET;
            case WOOD:
            case WOOD_CHIPS:
                return HeaterType.WOOD;
            case SOLAR_HEATING:
                return HeaterType.SOLAR;
            case HYDRO_ENERGY:
            case BIO_ENERGY:
            case WIND_ENERGY:
            case COMBINED_HEAT_AND_POWER_BIO_ENERGY:
            case COMBINED_HEAT_AND_POWER_REGENERATIVE_ENERGY:
            case COMBINED_HEAT_AND_POWER_RENEWABLE_ENERGY:
                return HeaterType.ALTERNATIVE;
            case COMBINED_HEAT_AND_POWER_FOSSIL_FUELS:
            case NO_INFORMATION:
            default:
                return null;
        }
    }

    private static BuildingConditionType convertBuildingCondition(RealEstateCondition realEstateCondition) {

        switch (realEstateCondition) {
            case FIRST_TIME_USE:
                return BuildingConditionType.FIRST_TIME_USE;
            case FIRST_TIME_USE_AFTER_REFURBISHMENT:
                return BuildingConditionType.FIRST_TIME_USE_AFTER_REFURBISHMENT;
            case FULLY_RENOVATED:
                return BuildingConditionType.FULLY_RENOVATED;
            case MINT_CONDITION:
                return BuildingConditionType.MINT_CONDITION;
            case MODERNIZED:
                return BuildingConditionType.MODERNIZED;
            case NEED_OF_RENOVATION:
                return BuildingConditionType.NEED_OF_RENOVATION;
            case NEGOTIABLE:
                return BuildingConditionType.NEGOTIABLE;
            case REFURBISHED:
                return BuildingConditionType.REFURBISHED;
            case RIPE_FOR_DEMOLITION:
                return BuildingConditionType.RIPE_FOR_DEMOLITION;
            case WELL_KEPT:
                return BuildingConditionType.WELL_KEPT;
            case NO_INFORMATION:
            default:
                return BuildingConditionType.NO_INFORMATION;
        }
    }

    private static FlatType convertFlatType(ApartmentType apartmentType) {
        if (apartmentType == null) {
            return FlatType.OTHER;
        }

        switch (apartmentType) {
            case GROUND_FLOOR:
                return FlatType.GROUND_FLOOR;
            case HALF_BASEMENT:
                return FlatType.HALF_BASEMENT;
            case LOFT:
                return FlatType.LOFT;
            case MAISONETTE:
                return FlatType.MAISONETTE;
            case PENTHOUSE:
                return FlatType.PENTHOUSE;
            case RAISED_GROUND_FLOOR:
                return FlatType.RAISED_GROUND_FLOOR;
            case ROOF_STOREY:
                return FlatType.ROOF_STOREY;
            case TERRACED_FLAT:
                return FlatType.TERRACED_FLAT;
            case APARTMENT:
                return FlatType.APARTMENT;
            case OTHER:
            default:
                return FlatType.OTHER;
        }
    }

    private static HouseType convertHouseType(BuildingType houseType) {
        if (houseType == null) {
            return HouseType.OTHER;
        }

        switch (houseType) {
            case SINGLE_FAMILY_HOUSE:
                return HouseType.DETACHED_HOUSE;
            case MID_TERRACE_HOUSE:
                return HouseType.TOWNHOUSE;
            case END_TERRACE_HOUSE:
                return HouseType.SUPERB_TOWNHOUSE;
            case MULTI_FAMILY_HOUSE:
                return HouseType.BLOCK_OF_FLATS;
            case BUNGALOW:
                return HouseType.BUNGALOW;
            case FARMHOUSE:
                return HouseType.FARMHOUSE;
            case SEMIDETACHED_HOUSE:
                return HouseType.DUPLEX;
            case VILLA:
                return HouseType.VILLA;
            case CASTLE_MANOR_HOUSE:
                return HouseType.CASTLE;
            case SPECIAL_REAL_ESTATE:
            case TERRACE_HOUSE:
            case OTHER:
                return HouseType.OTHER;
            default:
                return HouseType.OTHER;
        }
    }

    private static EnergyClassType convertEnergyClassType(String classType) {
        return EnergyClassType.bySymbol(classType);
    }

    private static com.neovisionaries.i18n.CountryCode convertCountry(CountryCode countryCode) {
        if (countryCode == null) {
            return null;
        }

        return com.neovisionaries.i18n.CountryCode.valueOf(countryCode.name());
    }

    private static ImportLog getFtpImportLog(LandlordCustomer customer, int total) {
        ImportLog ftpImportLog = new ImportLog();

        ftpImportLog.setCreated(new Date());
        ftpImportLog.setUpdated(new Date());

        ftpImportLog.setStatus(ImportStatus.QUEUED);

        ftpImportLog.setCustomer(customer);

        ftpImportLog.setCurrent(0);
        ftpImportLog.setTotal(total);

        ftpImportLog.setStatusMessage("");

        return ftpImportLog;
    }

    void init() {
        String endpoint = ENDPOINT + "/restapi/api";

        isAIs24Api = new IS24ApiImpl();
        isAIs24Api.init(CONSUMER_KEY, CONSUMER_SECRET, endpoint);

        log.info("*******************************************************************");
        log.info("Initialize " + this.getClass().getSimpleName());
        log.info("CONSUMER_KEY -> " + CONSUMER_KEY);
        log.info("CONSUMER_SECRET -> " + CONSUMER_SECRET);
        log.info("ENDPOINT -> " + ENDPOINT);
        log.info("*******************************************************************");
    }

    void login(String token, String tokenSecret) {
        isAIs24Api.signIn(token, tokenSecret);

        log.info("*******************************************************************");
        log.info("Login successful");
        log.info("TOKEN -> " + token);
        log.info("TOKENSECRET -> " + tokenSecret);
        log.info("*******************************************************************");
    }

    @Async
    public void importAll(long userID, long customerID) {
        importAll(userID, customerID, -1);
    }

    @Async
    public void importAll(long userID, long customerID, int max) {
        importAll(userID, customerID, max, null);
    }

    @Async
    public void importAll(long userID, long customerID, int max, List<Long> exclude) {
        LandlordCustomer customer = customerRepository.getOne(customerID);

        if (customer == null) {
            log.info("Unknown customerID -> " + customerID);
            return;
        }

        LandlordUser user = null;
        for (LandlordUser tmpUser : customer.getUsers()) {
            if (tmpUser.getId() == userID) {
                user = tmpUser;
            }
        }

        if (hasErrors(user, userID, customerID)) {
            return;
        }

        importAll(user, customer, max, exclude);
    }

    @Async
    public void importAll(LandlordUser user, LandlordCustomer customer) {
        importAll(user, customer, -1);
    }

    @Async
    public void importAll(LandlordUser user, LandlordCustomer customer, int max) {
        importAll(user, customer, max, null);
    }

    @Async
    public void importAll(LandlordUser user, LandlordCustomer customer, int max, List<Long> exclude) {

        if (hasErrors(user, customer)) {
            return;
        }

        List<RealEstate> realEstates = new LinkedList<>();

        int pageNumber = 1;
        int pageSize = max > 0 ? max : 100;
        while (true) {
            List<RealEstate> tmp = isAIs24Api.getAllRealestates(pageSize, pageNumber++);

            realEstates.addAll(tmp);

            if (max > 0 && realEstates.size() >= max) {
                break;
            }
            if (tmp.size() < pageSize) {
                break;
            }
        }

        importList(user, customer, realEstates, max, exclude);
    }

    @Async
    public void importSome(long userID, long customerID, List<Long> realEstateIDs) {
        LandlordCustomer customer = customerRepository.getOne(customerID);

        if (customer == null) {
            log.info("Unknown customerID -> " + customerID);
            return;
        }

        LandlordUser user = null;
        for (LandlordUser tmpUser : customer.getUsers()) {
            if (tmpUser.getId() == userID) {
                user = tmpUser;
                break;
            }
        }

        if (hasErrors(user, userID, customerID)) {
            return;
        }

        importSome(user, customer, realEstateIDs);
    }

    @Async
    public void importSome(LandlordUser user, LandlordCustomer customer, List<Long> realEstateIDs) {

        if (hasErrors(user, customer)) {
            return;
        }

        List<RealEstate> realEstates = new LinkedList<>();

        for (Long id : realEstateIDs) {
            RealEstate tmp = isAIs24Api.getRealestate(id + "");

            if (tmp != null) {
                realEstates.add(tmp);
            }
        }

        importList(user, customer, realEstates, -1, null);
    }

    private List<Property> importList(LandlordUser user, LandlordCustomer customer, List<RealEstate> realEstates,
                                      int max, List<Long> exclude) {
        log.info("Starting import for " + user.getId() + " (userID) and " + customer.getId() + " (customerID");

        List<Property> list = new LinkedList<>();
        Immoscout24ImportStatistics statistics = new Immoscout24ImportStatistics();

        log.info(realEstates.size() + " available ...");

        ImportLog ftpImportLog = getFtpImportLog(customer, realEstates.size());
        ftpImportLog.setStatus(ImportStatus.STARTED);
        updateLog(ftpImportLog);

        Prioset prioset = createAndSaveDefaultPrioset(customer);

        for (RealEstate realEstate : realEstates) {
            if (max > 0 && list.size() >= max) {
                log.info("Import maximum reached -> " + max);
                break;
            }

            updateLogIncreaseCurrent(ftpImportLog);

            long realEstateId = realEstate.getId();
            if (exclude != null && exclude.contains(realEstateId)) {
                log.info("Flat excluded -> ID " + realEstateId);
                statistics.increaseExcludedCount();
                continue;
            }

            if (isSupportConvertType(realEstate)) {
                Property property = convertToProperty(realEstate, customer, user, ftpImportLog, prioset, statistics);
                if (property != null) {
                    list.add(property);
                }
            } else {
                String className = realEstate.getClass().getSimpleName();
                String logMessage = "Skipping import for ID " + realEstateId + " (class: " + className + ")";
                log.info(logMessage);
                updateLogStatusMessage(ftpImportLog, logMessage);
            }
        }

        ftpImportLog.setStatusMessage("Converted Flats   : " + list.size() + "/" + realEstates.size());
        ftpImportLog.setStatus(ImportStatus.COMPLETED);
        updateLog(ftpImportLog);

        log.info("*******************************************************************");
        log.info("Import Summary");
        log.info("Total Objects        : " + realEstates.size());
        log.info("Skipped Objects      : " + (realEstates.size() - statistics.getPropertyCount()));
        log.info("Converted Properties : " + list.size());
        log.info("Skipped Properties   : " + (statistics.getPropertyCount() - list.size()));
        log.info("Already exists       : " + (statistics.getExcludedCount()));
        log.info("Files converted      : " + (statistics.getFilesCount() - statistics.getFilesSkippedCount()) + "/" +
                statistics.getFilesCount());
        log.info("*******************************************************************");

        return list;
    }

    private boolean isSupportConvertType(RealEstate realEstate) {
        return realEstate instanceof ApartmentRent || realEstate instanceof HouseRent;
    }

    private Property convertToProperty(RealEstate realEstate,
                                       LandlordCustomer customer,
                                       LandlordUser user,
                                       ImportLog ftpImportLog,
                                       Prioset prioset,
                                       Immoscout24ImportStatistics statistics) {
        statistics.increasePropertyCount();
        long realEstateId = realEstate.getId();

        log.info("Starting import for ID " + realEstateId);
        updateLogStatusMessage(ftpImportLog, "Starting import for ID " + realEstateId);

        Property property = findExistingProperty(customer, realEstate);
        try {
            if (realEstate instanceof ApartmentRent) {
                property = convertToProperty((ApartmentRent) realEstate, user, customer, property);
            } else if (realEstate instanceof HouseRent) {
                property = convertToProperty((HouseRent) realEstate, user, customer, property);
            } else {
                throw new RuntimeException("Unknown object type");
            }
        } catch (Exception e) {
            log.info("Unknown Exception converting to Flat -> skipped ID " + realEstateId, e);
            return null;
        }

        List<Attachment> attachments = realEstate.getAttachments().getAttachment();
        statistics.increaseFilesCount(attachments.size());

        try {
            int filesSkipped = convertAttachments(attachments, property);
            statistics.increaseFilesSkippedCount(filesSkipped);

            property.setPrioset(prioset);

            propertyRepository.save(property);
            messageSender.createGeoCodesAndSendProposalUpdateMessage(property, true);

            log.info("Finished import for ID " + realEstateId);
        } catch (Exception e) {
            log.info("Unknown Exception converting Attachments -> skipped ID " + realEstateId, e);
            return null;
        }

        return property;
    }

    private Property findExistingProperty(LandlordCustomer customer, RealEstate realEstate) {
        Property property = null;
        String realEstateId = realEstate.getId().toString().toLowerCase();
        List<Property> properties = propertyRepository.findByCustomerIdAndExternalId(customer.getId(), realEstateId);
        if (!properties.isEmpty()) {
            property = properties.get(0);

            if (properties.size() > 1) {
                log.info("More than one property for externalId -> " + realEstateId + " [" + properties.size() + "]");
            }
        }

        return property;
    }

    private Prioset createAndSaveDefaultPrioset(LandlordCustomer landlordCustomer) {
        Prioset prioset = new Prioset();
        prioset.setName("Import " +    new SimpleDateFormat(DATE_FORMAT).format(new Date()));
        prioset.setCustomer(landlordCustomer);
        prioset.setLocked(false);
        priosetRepository.save(prioset);

        return prioset;
    }

    private Property convertToProperty(ApartmentRent apartmentRent, LandlordUser user, LandlordCustomer customer,
                                       Property property) {
        initializeProperty(property, customer, user, apartmentRent.getAddress());
        PropertyData data = property.getData();

        EnergyCertificate energyCertificate = convertEnergyPerformanceCertificate(
                new Immoscout24EnergyCertificate(apartmentRent));

        data.setEnergyCertificate(energyCertificate);
        data.setName(apartmentRent.getTitle());
        data.setObjectDescription(apartmentRent.getDescriptionNote());
        data.setObjectLocationText(apartmentRent.getLocationNote());
        data.setObjectMiscellaneousText(apartmentRent.getOtherNote());
        data.setFurnishingDescription(apartmentRent.getFurnishingNote());


        data.setAvailableFrom(new AvailableFrom(apartmentRent.getFreeFrom()));

        data.setBasePrice(apartmentRent.getBaseRent());
        data.setServiceCharge(apartmentRent.getServiceCharge());
        data.setHeatingCost(apartmentRent.getHeatingCosts());
        data.setHeatingCostIncluded(apartmentRent.getHeatingCostsInServiceCharge() == YesNoNotApplicableType.YES);

        data.setShowAddress(apartmentRent.isShowAddress());
        data.setHeater(convertPrimaryType(apartmentRent.getHeatingTypeEnev2014()));

        if (apartmentRent.isBalcony()) {
            data.setNumberOfBalconies(1.0);
        }
        data.setBasementAvailable(apartmentRent.getCellar() == YesNotApplicableType.YES);
        data.setBuildingCondition(convertBuildingCondition(apartmentRent.getCondition()));
        data.setFlatType(convertFlatType(apartmentRent.getApartmentType()));
        data.setObjectType(ObjectType.FLAT);
        data.setSize(apartmentRent.getLivingSpace());
        data.setFlatShare(apartmentRent.getUseAsFlatshareRoom() == YesNotApplicableType.YES);
        data.setElevator(apartmentRent.isLift());
        data.setGarden(apartmentRent.isGarden());
        data.setGuestToilette(apartmentRent.getGuestToilet() == YesNotApplicableType.YES);
        data.setKitchenette(apartmentRent.isBuiltInKitchen());

        data.setConstructionYear(String.valueOf(apartmentRent.getConstructionYear()));

        if (apartmentRent.getFloor() != null) {
            data.setFloor(apartmentRent.getFloor());
        }

        data.setRooms(apartmentRent.getNumberOfRooms());
        data.setBathRooms(apartmentRent.getNumberOfBathRooms());

        if (apartmentRent.getNumberOfBathRooms() != null) {
            data.setBathRooms(apartmentRent.getNumberOfBathRooms());
        }

        convertLastRefurbishment(data, apartmentRent.getLastRefurbishment());

        data.setBarrierFree(apartmentRent.getHandicappedAccessible() == YesNotApplicableType.YES);
        data.setContact(null);
        data.setShowContact(false);

        convertBailment(data, apartmentRent.getDeposit());
        convertParkingSpaces(
                data,
                apartmentRent.getParkingSpacePrice(),
                apartmentRent.getParkingSpaceType(),
                apartmentRent.getNumberOfParkingSpaces()
        );

        property.setExternalId(apartmentRent.getId().toString());

        return property;
    }

    private Property convertToProperty(HouseRent houseRent, LandlordUser user, LandlordCustomer customer,
                                       Property property) {
        initializeProperty(property, customer, user, houseRent.getAddress());
        PropertyData data = property.getData();

        Immoscout24EnergyCertificate energyCertificateBean = new Immoscout24EnergyCertificate(houseRent);
        EnergyCertificate energyCertificate = convertEnergyPerformanceCertificate(energyCertificateBean);

        data.setEnergyCertificate(energyCertificate);
        data.setName(houseRent.getTitle());
        data.setObjectDescription(houseRent.getDescriptionNote());
        data.setObjectLocationText(houseRent.getLocationNote());
        data.setFurnishingDescription(houseRent.getFurnishingNote());
        data.setObjectMiscellaneousText(houseRent.getOtherNote());
        data.setAvailableFrom(new AvailableFrom(houseRent.getFreeFrom()));


        data.setBasePrice(houseRent.getBaseRent());
        data.setServiceCharge(houseRent.getServiceCharge());
        data.setHeatingCostIncluded(houseRent.getHeatingCostsInServiceCharge() == YesNoNotApplicableType.YES);
        data.setHeatingCost(houseRent.getHeatingCosts());

        data.setHeater(convertPrimaryType(houseRent.getHeatingTypeEnev2014()));
        data.setShowAddress(houseRent.isShowAddress());
        data.setBasementAvailable(houseRent.getCellar() == YesNotApplicableType.YES);

        data.setBuildingCondition(convertBuildingCondition(houseRent.getCondition()));
        data.setHouseType(convertHouseType(houseRent.getBuildingType()));
        data.setObjectType(ObjectType.HOUSE);
        data.setFlatShare(false);
        data.setSize(houseRent.getLivingSpace());
        data.setGuestToilette(houseRent.getGuestToilet() == YesNotApplicableType.YES);
        data.setKitchenette(houseRent.isBuiltInKitchen());

        data.setConstructionYear(String.valueOf(houseRent.getConstructionYear()));
        data.setRooms(houseRent.getNumberOfRooms());
        data.setLandArea(houseRent.getPlotArea());
        data.setBarrierFree(houseRent.getHandicappedAccessible() == YesNotApplicableType.YES);

        if (houseRent.getNumberOfBathRooms() != null) {
            data.setBathRooms(houseRent.getNumberOfBathRooms());
        }

        convertLastRefurbishment(data, houseRent.getLastRefurbishment());

        data.setContact(null);
        data.setShowContact(false);

        convertBailment(data, houseRent.getDeposit());
        convertParkingSpaces(
                data,
                houseRent.getParkingSpacePrice(),
                houseRent.getParkingSpaceType(),
                houseRent.getNumberOfParkingSpaces()
        );

        property.setExternalId(houseRent.getId().toString());

        return property;
    }

    private void convertParkingSpaces(PropertyData data, Double price, ParkingSpaceType type, Integer count) {
        ParkingSpace parkingSpace = convertParkingSpace(price, type, count);
        List<ParkingSpace> parkingSpaces = Collections.singletonList(parkingSpace);
        data.setParkingSpaces(parkingSpaces);
    }

    private ParkingSpace convertParkingSpace(Double price, ParkingSpaceType parkingSpaceType, Integer count) {
        ParkingSpace parkingSpace = new ParkingSpace();
        parkingSpace.setPrice(price);
        parkingSpace.setType(convertParkingType(parkingSpaceType));
        parkingSpace.setCount(count);

        return parkingSpace;
    }

    private ParkingType convertParkingType(ParkingSpaceType parkingSpaceType) {
        switch (parkingSpaceType) {
            case GARAGE:
                return ParkingType.GARAGE;
            case OUTSIDE:
                return ParkingType.FREE_SPACE;
            case CARPORT:
                return ParkingType.CARPORT;
            case DUPLEX:
                return ParkingType.DUPLEX;
            case CAR_PARK:
                return ParkingType.CAR_PARK;
            case UNDERGROUND_GARAGE:
                return ParkingType.UNDERGROUND_CAR_PARK;
            default:
                return ParkingType.GARAGE;
        }
    }

    private void convertBailment(PropertyData propertyData, String deposit) {
        Double bailment = parseDouble(deposit);
        if (bailment != null) {
            propertyData.setBailment(bailment);
        }
    }

    private void initializeProperty(
            Property property,
            LandlordCustomer customer,
            LandlordUser user,
            Wgs84Address address
    ) {
        if (property == null) {
            property = new Property();
        }

        property.setCustomer(customer);
        property.setUser(user);

        PropertyData data = property.getData();
        if (data == null) {
            data = new PropertyData();
            property.setData(data);
        }

        data.setAddress(convertAddress(address));
    }

    private void convertLastRefurbishment(PropertyData propertyData, Integer lastRefurbishment) {
        if (lastRefurbishment != null && lastRefurbishment > 0) {
            propertyData.setLastRefurbishment(String.valueOf(lastRefurbishment));
        }
    }

    private int convertAttachments(List<Attachment> attachments, Property flat) {

        int skipped = 0;

        for (Attachment attachment : attachments) {

            InputStream is;
            FileType fileType;

            if (attachment instanceof Picture) {
                try {
                    is = isAIs24Api.getAttachmentData((Picture) attachment);
                } catch (Exception e) {
                    log.info("ATTACHMENT ERROR: Skip Attachment -> " + attachment.getId());
                    skipped++;
                    continue;
                }

                fileType = FileType.IMG;
            } else if (attachment instanceof PDFDocument) {
                try {
                    is = isAIs24Api.getAttachmentData((PDFDocument) attachment);
                } catch (Exception e) {
                    log.info("ATTACHMENT ERROR: Skip Attachment -> " + attachment.getId());
                    skipped++;
                    continue;
                }

                fileType = FileType.PDF;
            } else {
                log.info("UNKNOWN FORMAT: Skip Attachment -> " + attachment.getId());
                skipped++;
                continue;
            }

            File file = null;
            try {
                file = File.createTempFile(attachment.getId() + "", attachment.getTitle());

                FileUtils.copyInputStreamToFile(is, file);
            } catch (IOException e) {
                if (file != null && file.exists()) {
                    FileUtils.deleteQuietly(file);
                }

                log.info("DOWNLOAD ERROR: Skip Attachment -> " + attachment.getId());
                skipped++;
                continue;
            }

            FileStoreObject fso = createFileStoreObject(attachment, file, fileType);

            if (fso == null) {
                skipped++;
                continue;
            }

            S3File tmp = createAttachment(fso);

            if (attachment instanceof Picture) {
                flat.getData().getAttachments().add(tmp);

                tmp.setType(FileType.IMG);

                imageResizeService.resize(file, fso.getType(), fso.getIdentifier(), fso.getExtension(), true);
            } else if (attachment instanceof PDFDocument) {
                flat.getData().getDocuments().add(tmp);

                if (((PDFDocument) attachment).isFloorplan()) {
                    tmp.setType(FileType.FLOOR_PLAN);
                } else {
                    tmp.setType(FileType.PDF);
                }

                FileUtils.deleteQuietly(file);
            } else {
                log.info("UNKNOWN ERROR: Skip Attachment -> " + attachment.getId());
                skipped++;

                FileUtils.deleteQuietly(file);

                continue;
            }

            tmp.setTitle(tmp.getFilename());
        }

        return skipped;
    }

    private S3File createAttachment(FileStoreObject fso) {
        S3File tmp = new S3File();

        tmp.setEncrypted(fso.isEncrypted());
        tmp.setExtension(fso.getExtension());
        tmp.setIdentifier(fso.getIdentifier());
        tmp.setType(fso.getType());
        tmp.setUrl(fso.getUrl());

        return tmp;
    }

    private FileStoreObject createFileStoreObject(Attachment attachment, File file, FileType fileType) {
        FileStoreObject fso = FileStoreService.fileStoreObject(file, fileType, false);

        if (attachment instanceof Picture) {
            String mimetype = FileUtilities.detectImageMimetype(file);

            if (mimetype == null || mimetype.isEmpty()) {
                log.info("UNKNOWN IMAGE: Skip Attachment -> " + attachment.getId());
                return null;
            }

            String extension = FileStoreService.multipartFileExtension(mimetype);

            if (extension == null || extension.isEmpty()) {
                log.info("UNKNOWN IMAGE: Skip Attachment -> " + attachment.getId());
                return null;
            }

            fso.setExtension(extension);

        } else if (attachment instanceof PDFDocument) {
            fso.setExtension("pdf"); // Workaround da IS24 nicht den
            // mimetype angibt
        } else {
            log.info("UNKNOWN MIMETYPE: Skip Attachment -> " + attachment.getId());
            return null;
        }

        String url;
        try {
            url = s3FileManager.uploadFile(file, fileType, fso.getIdentifier(), fso.getExtension());
        } catch (S3FileManagerException e) {
            log.info("UPLOAD ERROR: Skip Attachment -> " + attachment.getId(), e);
            return null;
        }

        fso.setUrl(url);

        return fso;
    }

    private Double parseDouble(String number) {
        if (number == null || number.isEmpty()) {
            return null;
        }

        try {
            return Double.parseDouble(number.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void updateLogStatusMessage(ImportLog importLog, String message) {
        importLog.setStatusMessage(message);

        updateLog(importLog);
    }

    private void updateLogIncreaseCurrent(ImportLog importLog) {
        importLog.setCurrent(importLog.getCurrent() + 1);

        updateLog(importLog);
    }

    private void updateLog(ImportLog ftpImportLog) {
        ftpImportLog.setUpdated(new Date());

        ftpImportLogRepository.save(ftpImportLog);
    }

    private boolean hasErrors(LandlordUser user, long userID, long customerID) {
        boolean error = false;
        if (user == null || user.getCustomer().getId() != customerID) {
            log.info("Unknown userID (" + userID + ") for customerID (" + customerID + ")");
            error = true;
        }

        return error;
    }

    private boolean hasErrors(LandlordUser user, LandlordCustomer customer) {
        boolean error = false;
        if (user == null) {
            log.info("user is null");
            error = true;
        } else if (customer == null) {
            log.info("customer is null");
            error = true;
        } else if (!Objects.equals(user.getCustomer().getId(), customer.getId())) {
            log.info("customer and user do not match");
            error = true;
        }
        return error;
    }
}
