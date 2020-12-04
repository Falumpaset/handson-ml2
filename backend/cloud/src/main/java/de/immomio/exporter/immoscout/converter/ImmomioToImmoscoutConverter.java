package de.immomio.exporter.immoscout.converter;

import de.immobilienscout24.rest.schema.common._1.AirConditioningType;
import de.immobilienscout24.rest.schema.common._1.ApartmentType;
import de.immobilienscout24.rest.schema.common._1.BuildingEnergyRatingType;
import de.immobilienscout24.rest.schema.common._1.BuildingType;
import de.immobilienscout24.rest.schema.common._1.CommercializationType;
import de.immobilienscout24.rest.schema.common._1.CountryCode;
import de.immobilienscout24.rest.schema.common._1.CountryRegion;
import de.immobilienscout24.rest.schema.common._1.CourtageInfo;
import de.immobilienscout24.rest.schema.common._1.Currency;
import de.immobilienscout24.rest.schema.common._1.EnergyCertificateAvailability;
import de.immobilienscout24.rest.schema.common._1.EnergyCertificateCreationDate;
import de.immobilienscout24.rest.schema.common._1.EnergyPerformanceCertificate;
import de.immobilienscout24.rest.schema.common._1.EnergySourceEnev2014;
import de.immobilienscout24.rest.schema.common._1.EnergySourcesEnev2014;
import de.immobilienscout24.rest.schema.common._1.FlooringType;
import de.immobilienscout24.rest.schema.common._1.GarageType;
import de.immobilienscout24.rest.schema.common._1.HeatingTypeEnev2014;
import de.immobilienscout24.rest.schema.common._1.ItInfrastructureType;
import de.immobilienscout24.rest.schema.common._1.MarketingType;
import de.immobilienscout24.rest.schema.common._1.OfficeType;
import de.immobilienscout24.rest.schema.common._1.ParkingSpaceType;
import de.immobilienscout24.rest.schema.common._1.Price;
import de.immobilienscout24.rest.schema.common._1.RealEstateCondition;
import de.immobilienscout24.rest.schema.common._1.RealEstateState;
import de.immobilienscout24.rest.schema.common._1.RealtorContactDetails;
import de.immobilienscout24.rest.schema.common._1.SalutationType;
import de.immobilienscout24.rest.schema.common._1.ShortTermAccommodationType;
import de.immobilienscout24.rest.schema.common._1.Wgs84Address;
import de.immobilienscout24.rest.schema.common._1.YesNoNotApplicableType;
import de.immobilienscout24.rest.schema.common._1.YesNotApplicableType;
import de.immobilienscout24.rest.schema.offer.realestates._1.ApartmentRent;
import de.immobilienscout24.rest.schema.offer.realestates._1.GarageRent;
import de.immobilienscout24.rest.schema.offer.realestates._1.HouseRent;
import de.immobilienscout24.rest.schema.offer.realestates._1.Office;
import de.immobilienscout24.rest.schema.offer.realestates._1.RealEstate;
import de.immobilienscout24.rest.schema.offer.realestates._1.ShortTermAccommodation;
import de.immomio.data.base.type.common.ParkingType;
import de.immomio.data.base.type.property.GroundType;
import de.immomio.data.base.type.property.HeaterFiringType;
import de.immomio.data.base.type.property.HeaterType;
import de.immomio.data.landlord.bean.property.AvailableFrom;
import de.immomio.data.landlord.bean.property.Contact;
import de.immomio.data.landlord.bean.property.TemporaryLiving;
import de.immomio.data.landlord.bean.property.certificate.EnergyCertificate;
import de.immomio.data.landlord.bean.property.data.CommercialData;
import de.immomio.data.landlord.bean.property.data.GarageData;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.data.shared.bean.common.ParkingSpace;
import de.immomio.exporter.immoscout.Immoscout24EnergyCertificate;
import de.immomio.exporter.immoscout.Immoscout24Prices;
import de.immomio.util.DateUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import static de.immomio.data.landlord.bean.property.certificate.EnergyCertificate.CertificateCreationDate;
import static de.immomio.data.landlord.bean.property.certificate.EnergyCertificate.EnergyCertificateType;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 */

public class ImmomioToImmoscoutConverter {

    private static final String DD_MM_YYYY = "dd.MM.yyyy";
    private static final String ENERGY_CERTIFICATE_CLASS_A_P = "A_P";
    private static final String ENERGY_CERTIFICATE_CLASS_A_PLUS = "A_PLUS";
    private static final String NOT_APPLICABLE = "NOT_APPLICABLE";
    private static final String APLUS = "A+";
    private static final Locale LOCALE = Locale.GERMANY;

    @Value("${timezone.europe}")
    private String EUROPE_BERLIN;

    public static ApartmentRent convertApartmentRent(Property property) {
        return convertApartmentRent(property, null, property.getData().getObjectMiscellaneousText());
    }

    public static ApartmentRent convertApartmentRent(Property property, String propertyDescription, String miscellaneousText) {
        PropertyData data = property.getData();
        ApartmentRent apartmentRent = new ApartmentRent();

        convertCommonRealEstateData(property, apartmentRent, propertyDescription, miscellaneousText);

        apartmentRent.setCourtage(convertCourtage());
        apartmentRent.setApartmentType(convertFlatType(data));
        apartmentRent.setBalcony(data.hasBalcony());
        apartmentRent.setGarden(data.isGarden());
        apartmentRent.setConstructionYear(convertConstructionYear(data));

        populateCertificates(property, apartmentRent);

        apartmentRent.setFreeFrom(getAvailableDate(data));
        apartmentRent.setBuiltInKitchen(data.isKitchenette());
        apartmentRent.setLift(data.isElevator());

        populatePrices(data, apartmentRent);
        populateParkingData(data, apartmentRent);

        apartmentRent.setCondition(convertRealEstateCondition(data));
        apartmentRent.setCellar(convertCellar(data));
        apartmentRent.setGuestToilet(convertBooleanToYesNotApplicableType(data.isGuestToilette()));
        apartmentRent.setFloor(data.getFloor());
        apartmentRent.setNumberOfBathRooms(data.getBathRooms());
        apartmentRent.setHandicappedAccessible(convertBooleanToYesNotApplicableType(data.isWheelchairAccessible()));

        if (data.getBailment() != null) {
            apartmentRent.setDeposit(NumberFormat.getCurrencyInstance(LOCALE).format(data.getBailment()));
        }

        if (data.getRooms() != null) {
            apartmentRent.setNumberOfRooms(data.getRooms());
        }

        if (data.getSize() != null) {
            apartmentRent.setLivingSpace(data.getSize());
        }

        return apartmentRent;
    }

    public static ShortTermAccommodation convertShortTermAccommodation(Property property, String propertyDescription, String miscellaneousText) {
        PropertyData data = property.getData();
        TemporaryLiving temporaryLiving = data.getTemporaryLiving();
        ShortTermAccommodation shortTermAccommodation = new ShortTermAccommodation();
        convertCommonRealEstateData(property, shortTermAccommodation, propertyDescription, miscellaneousText);

        shortTermAccommodation.setCourtage(convertCourtage());
        shortTermAccommodation.setBalcony(data.hasBalcony());
        shortTermAccommodation.setGarden(data.isGarden());
        shortTermAccommodation.setConstructionYear(convertConstructionYear(data));
        populateCertificates(data, shortTermAccommodation);

        shortTermAccommodation.setStartRentalDate(DateUtil.dateToGregorianCalendar(temporaryLiving.getStart()));
        shortTermAccommodation.setEndRentalDate(DateUtil.dateToGregorianCalendar(temporaryLiving.getEnd()));
        shortTermAccommodation.setLift(data.isElevator());

        populatePrices(data, shortTermAccommodation);
        populateParkingData(data, shortTermAccommodation);

        shortTermAccommodation.setCondition(convertRealEstateCondition(data));
        shortTermAccommodation.setCellar(convertYesNotApplicableType(convertCellar(data)));
        shortTermAccommodation.setGuestToilet(convertYesNotApplicableType(convertBooleanToYesNotApplicableType(data.isGuestToilette())));
        shortTermAccommodation.setFloor(String.valueOf(data.getFloor()));
        shortTermAccommodation.setShortTermAccomodationType(ShortTermAccommodationType.FLAT);
        shortTermAccommodation.setNonSmoker(true);
        shortTermAccommodation.setHandicappedAccessible(data.isWheelchairAccessible());

        if (data.getBailment() != null) {
            shortTermAccommodation.setDeposit(NumberFormat.getCurrencyInstance(LOCALE).format(data.getBailment()));
        }

        if (data.getRooms() != null) {
            shortTermAccommodation.setNumberOfRooms(data.getRooms());
        }

        if (data.getSize() != null) {
            shortTermAccommodation.setLivingSpace(data.getSize());
        }
        return shortTermAccommodation;
    }

    public static HouseRent convertHouseRent(Property property, String propertyDescription, String miscellaneousText) {
        PropertyData data = property.getData();
        HouseRent houseRent = new HouseRent();

        convertCommonRealEstateData(property, houseRent, propertyDescription, miscellaneousText);
        houseRent.setCourtage(convertCourtage());

        if (data.getLandArea() != null) {
            houseRent.setPlotArea(data.getLandArea());
        }

        houseRent.setBuildingType(convertBuildingType(data));
        houseRent.setConstructionYear(convertConstructionYear(data));
        houseRent.setFreeFrom(getAvailableDate(data));
        houseRent.setBuiltInKitchen(data.isKitchenette());
        houseRent.setCondition(convertRealEstateCondition(data));

        populateCertificates(data, houseRent);
        populatePrices(data, houseRent);
        populateParkingData(data, houseRent);

        if (data.getRooms() != null) {
            houseRent.setNumberOfRooms(data.getRooms());
        }

        if (data.getBailment() != null) {
            houseRent.setDeposit(NumberFormat.getCurrencyInstance(LOCALE).format(data.getBailment()));
        }

        if (data.getSize() != null) {
            houseRent.setLivingSpace(data.getSize());
        }

        houseRent.setNumberOfBathRooms(data.getBathRooms());
        houseRent.setCellar(convertCellar(data));
        houseRent.setGuestToilet(convertBooleanToYesNotApplicableType(data.isGuestToilette()));

        return houseRent;
    }

    public static GarageRent convertGarageRent(Property property) {
        PropertyData data = property.getData();
        GarageData garageData = data.getGarageData();
        GarageRent garageRent = new GarageRent();

        convertCommonRealEstateData(property, garageRent, data.getObjectDescription(), data.getObjectMiscellaneousText());
        garageRent.setCourtage(convertCourtage());
        garageRent.setConstructionYear(convertConstructionYear(data));
        if (data.getAvailableFrom() != null) {
            garageRent.setFreeFrom(DateUtil.localDateToGregorianCalender(data.getAvailableFrom().getDateAvailable()));
        }
        garageRent.setFreeUntil(DateUtil.dateToGregorianCalendar(garageData.getFreeUntil()));

        Price price = new Price();
        price.setValue(data.getTotalRentGross());
        price.setCurrency(Currency.EUR);
        price.setMarketingType(MarketingType.RENT);
        garageRent.setPrice(price);

        garageRent.setGarageType(convertGarageType(garageData));
        garageRent.setLengthGarage(garageData.getLength());
        garageRent.setWidthGarage(garageData.getWidth());
        garageRent.setHeightGarage(garageData.getHeight());

        return garageRent;
    }

    public static Office convertOffice(Property property) {
        PropertyData data = property.getData();
        CommercialData commercialData = data.getCommercialData();
        Office office = new Office();

        convertCommonRealEstateData(property, office, data.getObjectDescription(), data.getObjectMiscellaneousText());
        office.setCourtage(convertCourtage());
        office.setConstructionYear(convertConstructionYear(data));

        populateCertificates(data, office);
        office.setFreeFrom(getAvailableDate(data));
        office.setLift(convertBooleanToYesNotApplicableType(data.isElevator()));

        office.setCondition(convertRealEstateCondition(data));
        office.setCellar(convertCellar(data));
        office.setDeposit(NumberFormat.getCurrencyInstance(LOCALE).format(data.getBailment()));

        populateOfficePrices(data, office);
        office.setNumberOfParkingSpaces(data.getTotalParkingSpaces());

        office.setHandicappedAccessible(convertBooleanToYesNotApplicableType(data.isBarrierFree()));
        office.setOfficeType(convertOfficeType(commercialData));
        office.setFlooringType(convertFlooringType(data.getGround()));
        office.setHasCanteen(convertBooleanToYesNotApplicableType(commercialData.isHasCanteen()));
        office.setLanCables(convertLanCables(commercialData));
        office.setHighVoltage(convertBooleanToYesNotApplicableType(commercialData.isHighVoltage()));
        office.setKitchenComplete(convertBooleanToYesNotApplicableType(data.isKitchenette()));
        office.setListed(convertBooleanToYesNotApplicableType(data.isHistoricBuilding()));
        office.setAirConditioning(convertAirConditioning(commercialData));
        office.setHandicappedAccessible(convertBooleanToYesNotApplicableType(data.isWheelchairAccessible()));

        office.setDistanceToMRS(doubleToInteger(commercialData.getDistanceToTrainStation()));
        office.setDistanceToFM(doubleToInteger(commercialData.getDistanceToHighway()));
        office.setDistanceToPT(doubleToInteger(commercialData.getDistanceToPublicTransport()));
        office.setDistanceToAirport(doubleToInteger(commercialData.getDistanceToAirport()));

        if (data.getNumberOfFloors() != null) {
            office.setNumberOfFloors(String.valueOf(data.getNumberOfFloors()));
        }
        office.setCommercializationType(CommercializationType.RENT);

        office.setNetFloorSpace(data.getSize());

        return office;
    }

    private static Integer doubleToInteger(Double value) {
        return value != null ? value.intValue() : null;
    }

    private static void populateOfficePrices(PropertyData data, Office office) {
        CommercialData commercialData = data.getCommercialData();

        office.setPrice(createPricePerSQMorNormal(commercialData.getPricePerSquareMeter(), data.getBasePrice()));
        if (data.getServiceCharge() != null) {
            office.setAdditionalCosts(createPrice(data.getServiceCharge()));
        }
        office.setCalculatedPrice(createPrice(data.getTotalRentGross()));
        if (data.getParkingPrice() != null) {
            office.setParkingSpacePrice(data.getParkingPrice());
        }
    }

    private static Price createPricePerSQMorNormal(Double sqmPrice, Double price) {
        if (sqmPrice != null && sqmPrice > 0) {
            return createPrice(sqmPrice, MarketingType.RENT_PER_SQM);
        } else {
            return createPrice(price);
        }
    }

    private static Price createPrice(double priceValue) {
        return createPrice(priceValue, MarketingType.RENT);
    }

    private static Price createPrice(double priceValue, MarketingType marketingType) {
        Price price = new Price();

        price.setValue(priceValue);
        price.setCurrency(Currency.EUR);
        price.setMarketingType(marketingType);

        return price;
    }

    private static void populateCertificates(Property property, ApartmentRent apartmentRent) {
        PropertyData data = property.getData();
        EnergyCertificate energyCertificateImmomio = data.getEnergyCertificate();
        if (energyCertificateImmomio != null) {
            apartmentRent.setEnergyPerformanceCertificate(true);
            Immoscout24EnergyCertificate certificate = new Immoscout24EnergyCertificate();
            apartmentRent.setEnergyCertificate(
                    convertEnergyPerformanceCertificate(energyCertificateImmomio, certificate));
            apartmentRent.setThermalCharacteristic(certificate.getThermalCharacteristic());
            apartmentRent.setBuildingEnergyRatingType(certificate.getBuildingEnergyRatingType());
            apartmentRent.setEnergyConsumptionContainsWarmWater(certificate.getEnergyConsumptionContainsWarmWater());
            apartmentRent.setEnergySourcesEnev2014(
                    convertPrimaryEnergy(energyCertificateImmomio.getPrimaryEnergyProvider()));
        } else {
            apartmentRent.setEnergyPerformanceCertificate(false);

        }
        apartmentRent.setHeatingTypeEnev2014(convertPrimaryType(data.getHeater()));
        apartmentRent.setCertificateOfEligibilityNeeded(property.getPrioset().getData().getWbs());
    }

    private static void populateCertificates(PropertyData data, HouseRent houseRent) {
        EnergyCertificate energyCertificateImmomio = data.getEnergyCertificate();
        if (energyCertificateImmomio != null) {
            houseRent.setEnergyPerformanceCertificate(true);
            Immoscout24EnergyCertificate certificate = new Immoscout24EnergyCertificate();
            houseRent.setEnergyCertificate(convertEnergyPerformanceCertificate(energyCertificateImmomio, certificate));
            houseRent.setEnergySourcesEnev2014(convertPrimaryEnergy(energyCertificateImmomio.getPrimaryEnergyProvider()));
            houseRent.setBuildingEnergyRatingType(certificate.getBuildingEnergyRatingType());
            houseRent.setThermalCharacteristic(certificate.getThermalCharacteristic());
            houseRent.setEnergyConsumptionContainsWarmWater(certificate.getEnergyConsumptionContainsWarmWater());
        } else {
            houseRent.setEnergyPerformanceCertificate(false);
        }
        houseRent.setHeatingTypeEnev2014(convertPrimaryType(data.getHeater()));
    }

    private static void populateCertificates(PropertyData data, ShortTermAccommodation shortTermAccommodation) {
        EnergyCertificate energyCertificateImmomio = data.getEnergyCertificate();
        if (energyCertificateImmomio != null) {
            Immoscout24EnergyCertificate certificate = new Immoscout24EnergyCertificate();
            shortTermAccommodation.setEnergyCertificate(convertEnergyPerformanceCertificate(energyCertificateImmomio, certificate));
            shortTermAccommodation.setEnergySourcesEnev2014(convertPrimaryEnergy(energyCertificateImmomio.getPrimaryEnergyProvider()));
            shortTermAccommodation.setThermalCharacteristic(certificate.getThermalCharacteristic());
            shortTermAccommodation.setBuildingEnergyRatingType(certificate.getBuildingEnergyRatingType());
            shortTermAccommodation.setEnergyConsumptionContainsWarmWater(certificate.getEnergyConsumptionContainsWarmWater());
        }
        shortTermAccommodation.setHeatingTypeEnev2014(convertPrimaryType(data.getHeater()));
    }

    private static void populateCertificates(PropertyData data, Office office) {
        EnergyCertificate energyCertificateImmomio = data.getEnergyCertificate();
        if (energyCertificateImmomio != null) {
            Immoscout24EnergyCertificate certificate = new Immoscout24EnergyCertificate();
            office.setEnergyCertificate(convertEnergyPerformanceCertificate(energyCertificateImmomio, certificate));
            office.setEnergySourcesEnev2014(convertPrimaryEnergy(energyCertificateImmomio.getPrimaryEnergyProvider()));
            office.setThermalCharacteristic(certificate.getThermalCharacteristic());
            office.setBuildingEnergyRatingType(certificate.getBuildingEnergyRatingType());
            office.setEnergyConsumptionContainsWarmWater(certificate.getEnergyConsumptionContainsWarmWater());
        }
        office.setHeatingTypeEnev2014(convertPrimaryType(data.getHeater()));
    }

    private static void populatePrices(PropertyData data, HouseRent houseRent) {
        Immoscout24Prices prices = new Immoscout24Prices(data);
        houseRent.setHeatingCosts(prices.getHeatingCost());
        houseRent.setBaseRent(prices.getBasePrice());
        houseRent.setServiceCharge(prices.getServiceCharge());
        houseRent.setTotalRent(convertTotalRent(data, prices));
        houseRent.setHeatingCostsInServiceCharge(convertHeatingCostsInServiceCharge(data));
    }

    private static void populateParkingData(PropertyData data, HouseRent houseRent) {
        List<ParkingSpace> parkingSpaces = data.getParkingSpaces();
        if (parkingSpaces != null && !parkingSpaces.isEmpty()) {
            if (!data.isParkingPriceIncludedToAdditionalCosts()) {
                houseRent.setParkingSpacePrice(data.getTotalParkingPrice());
            }

            houseRent.setNumberOfParkingSpaces(data.getTotalParkingSpaces());

            ParkingType parkingType = parkingSpaces.get(0).getType();
            houseRent.setParkingSpaceType(convertParkingSpaceType(parkingType));
        }
    }

    private static void populatePrices(PropertyData data, ApartmentRent apartmentRent) {
        Immoscout24Prices prices = new Immoscout24Prices(data);
        apartmentRent.setBaseRent(prices.getBasePrice());
        apartmentRent.setHeatingCosts(prices.getHeatingCost());
        apartmentRent.setServiceCharge(prices.getServiceCharge());
        apartmentRent.setHeatingCostsInServiceCharge(convertHeatingCostsInServiceCharge(data));
        apartmentRent.setTotalRent(convertTotalRent(data, prices));
    }

    private static void populatePrices(PropertyData data, ShortTermAccommodation shortTermAccommodation) {
        Immoscout24Prices prices = new Immoscout24Prices(data);
        shortTermAccommodation.setBaseRent(prices.getBasePrice());
        shortTermAccommodation.setServiceCharge(prices.getServiceCharge());
        shortTermAccommodation.setTotalRent(convertTotalRent(data, prices));
        Price price = new Price();
        price.setValue(convertTotalRent(data, prices));
        price.setCurrency(Currency.EUR);
        price.setMarketingType(MarketingType.RENT);
        shortTermAccommodation.setPrice(price);
    }

    private static void populateParkingData(PropertyData data, ApartmentRent apartmentRent) {
        List<ParkingSpace> parkingSpaces = data.getParkingSpaces();
        if (parkingSpaces != null && !parkingSpaces.isEmpty()) {
            if (!data.isParkingPriceIncludedToAdditionalCosts()) {
                apartmentRent.setParkingSpacePrice(data.getTotalParkingPrice());
            }
            apartmentRent.setNumberOfParkingSpaces(data.getTotalParkingSpaces());

            ParkingType parkingType = parkingSpaces.get(0).getType();
            apartmentRent.setParkingSpaceType(convertParkingSpaceType(parkingType));
        }
    }

    private static void populateParkingData(PropertyData data, ShortTermAccommodation shortTermAccommodation) {
        List<ParkingSpace> parkingSpaces = data.getParkingSpaces();
        if (parkingSpaces != null && !parkingSpaces.isEmpty()) {
            if (!data.isParkingPriceIncludedToAdditionalCosts()) {
                shortTermAccommodation.setParkingSpacePrice(data.getTotalParkingPrice());
            }
            shortTermAccommodation.setNumberOfParkingSpaces(data.getTotalParkingSpaces());

            ParkingType parkingType = parkingSpaces.get(0).getType();
            shortTermAccommodation.setParkingSpaceType(convertParkingSpaceType(parkingType));
        }
    }

    private static ParkingSpaceType convertParkingSpaceType(ParkingType parkingType) {
        if (parkingType == null) {
            return ParkingSpaceType.NO_INFORMATION;
        }

        switch (parkingType) {
            case GARAGE:
                return ParkingSpaceType.GARAGE;
            case FREE_SPACE:
                return ParkingSpaceType.OUTSIDE;
            case CARPORT:
                return ParkingSpaceType.CARPORT;
            case DUPLEX:
                return ParkingSpaceType.DUPLEX;
            case CAR_PARK:
                return ParkingSpaceType.CAR_PARK;
            case UNDERGROUND_CAR_PARK:
                return ParkingSpaceType.UNDERGROUND_GARAGE;
            default:
                return ParkingSpaceType.NO_INFORMATION;
        }
    }

    private static YesNoNotApplicableType convertHeatingCostsInServiceCharge(PropertyData data) {
        return data.isHeatingCostIncluded() ? YesNoNotApplicableType.YES : YesNoNotApplicableType.NO;
    }

    private static Double convertTotalRent(PropertyData data, Immoscout24Prices prices) {
        double totalPrice = prices.getBasePrice() + prices.getServiceCharge();

        if (!data.isParkingPriceIncludedToAdditionalCosts()) {
            totalPrice += data.getTotalParkingPrice();
        }

        if (!data.isHeatingCostIncluded()) {
            totalPrice += prices.getHeatingCost();
        }

        return totalPrice;
    }

    private static YesNotApplicableType convertCellar(PropertyData data) {
        return BooleanUtils.isTrue(data.getBasementAvailable()) ? YesNotApplicableType.YES : YesNotApplicableType.NOT_APPLICABLE;
    }

    private static ItInfrastructureType convertLanCables(CommercialData commercialData) {
        return commercialData.isLanCables() ? ItInfrastructureType.YES : ItInfrastructureType.NO;
    }

    private static AirConditioningType convertAirConditioning(CommercialData commercialData) {
        return commercialData.isAirConditioning() ? AirConditioningType.YES : AirConditioningType.NO;
    }

    private static void convertCommonRealEstateData(Property property, RealEstate realEstate, String description, String miscellaneousText) {
        PropertyData data = property.getData();
        realEstate.setExternalId(property.getId().toString());

        String name = data.getName();
        String title = name != null ? StringUtils.substring(name, 0, 100) : "";
        realEstate.setTitle(title);
        realEstate.setRealEstateState(RealEstateState.ACTIVE);

        if (description != null && !description.isEmpty()) {
            realEstate.setDescriptionNote(description);
        } else {
            realEstate.setDescriptionNote(data.getObjectDescription());
        }
        realEstate.setLocationNote(data.getObjectLocationText());
        realEstate.setFurnishingNote(data.getFurnishingDescription());

        realEstate.setOtherNote(miscellaneousText);

        realEstate.setAddress(convertAddress(data.getAddress()));
        realEstate.setShowAddress(data.isShowAddress());
    }

    private static Integer convertConstructionYear(PropertyData data) {
        try {
            return Integer.valueOf(data.getConstructionYear());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String getAvailableDate(PropertyData data) {
        AvailableFrom availableFrom = data.getAvailableFrom();
        if (availableFrom != null && availableFrom.dateAsString() != null && StringUtils.isNotEmpty(availableFrom.dateAsString().trim())) {
            return availableFrom.dateAsString();
        }

        return null;
    }

    private static RealEstateCondition convertRealEstateCondition(PropertyData data) {
        if (data.getBuildingCondition() == null) {
            return RealEstateCondition.NO_INFORMATION;
        }

        switch (data.getBuildingCondition()) {
            case FIRST_TIME_USE:
                return RealEstateCondition.FIRST_TIME_USE;
            case FIRST_TIME_USE_AFTER_REFURBISHMENT:
                return RealEstateCondition.FIRST_TIME_USE_AFTER_REFURBISHMENT;
            case MINT_CONDITION:
                return RealEstateCondition.MINT_CONDITION;
            case REFURBISHED:
                return RealEstateCondition.REFURBISHED;
            case MODERNIZED:
                return RealEstateCondition.MODERNIZED;
            case FULLY_RENOVATED:
                return RealEstateCondition.FULLY_RENOVATED;
            case WELL_KEPT:
                return RealEstateCondition.WELL_KEPT;
            case NEED_OF_RENOVATION:
                return RealEstateCondition.NEED_OF_RENOVATION;
            case NEGOTIABLE:
                return RealEstateCondition.NEGOTIABLE;
            case RIPE_FOR_DEMOLITION:
                return RealEstateCondition.RIPE_FOR_DEMOLITION;
            case NO_INFORMATION:
            default:
                return RealEstateCondition.NO_INFORMATION;
        }
    }

    private static YesNotApplicableType convertBooleanToYesNotApplicableType(Boolean value) {
        if (BooleanUtils.isTrue(value)) {
            return YesNotApplicableType.YES;
        } else {
            return YesNotApplicableType.NOT_APPLICABLE;
        }
    }

    private static boolean convertYesNotApplicableType(YesNotApplicableType applicableType) {
        return YesNotApplicableType.YES == applicableType;
    }

    private static ApartmentType convertFlatType(PropertyData data) {
        if (data.getFlatType() == null) {
            return ApartmentType.OTHER;
        }

        switch (data.getFlatType()) {
            case GROUND_FLOOR:
                return ApartmentType.GROUND_FLOOR;
            case HALF_BASEMENT:
                return ApartmentType.HALF_BASEMENT;
            case LOFT:
                return ApartmentType.LOFT;
            case MAISONETTE:
                return ApartmentType.MAISONETTE;
            case PENTHOUSE:
                return ApartmentType.PENTHOUSE;
            case RAISED_GROUND_FLOOR:
                return ApartmentType.GROUND_FLOOR;
            case ROOF_STOREY:
                return ApartmentType.ROOF_STOREY;
            case TERRACED_FLAT:
                return ApartmentType.TERRACED_FLAT;
            case FLOOR:
                return ApartmentType.APARTMENT;
            case GALERY:
                return ApartmentType.MAISONETTE;
            case APARTMENT:
                return ApartmentType.APARTMENT;
            case OTHER:
                return ApartmentType.OTHER;
            default:
                return ApartmentType.OTHER;
        }
    }

    private static BuildingType convertBuildingType(PropertyData data) {
        if (data.getFlatType() == null) {
            return BuildingType.OTHER;
        }

        switch (data.getHouseType()) {
            case DETACHED_HOUSE:
                return BuildingType.SINGLE_FAMILY_HOUSE;
            case CITY_HOUSE:
                return BuildingType.SINGLE_FAMILY_HOUSE;
            case TOWNHOUSE:
                return BuildingType.MID_TERRACE_HOUSE;
            case ROW_HOUSE:
                return BuildingType.MID_TERRACE_HOUSE;
            case SUPERB_TOWNHOUSE:
                return BuildingType.END_TERRACE_HOUSE;
            case CHAIN_HOUSE:
                return BuildingType.END_TERRACE_HOUSE;
            case BLOCK_OF_FLATS:
                return BuildingType.MULTI_FAMILY_HOUSE;
            case TWIN_HOUSE:
                return BuildingType.MULTI_FAMILY_HOUSE;
            case APARTMENT_BLOCK_HOUSE:
                return BuildingType.MULTI_FAMILY_HOUSE;
            case BUNGALOW:
                return BuildingType.BUNGALOW;
            case FARMHOUSE:
                return BuildingType.FARMHOUSE;
            case COUNTRY_HOUSE:
                return BuildingType.FARMHOUSE;
            case DUPLEX:
                return BuildingType.SEMIDETACHED_HOUSE;
            case VILLA:
                return BuildingType.VILLA;
            case FINCA:
                return BuildingType.VILLA;
            case CASTLE:
                return BuildingType.CASTLE_MANOR_HOUSE;
            case BURG:
                return BuildingType.CASTLE_MANOR_HOUSE;
            default:
                return BuildingType.OTHER;
        }
    }

    private static GarageType convertGarageType(GarageData garageData) {
        if (garageData.getGarageType() == null) {
            return GarageType.NO_INFORMATION;
        }

        switch (garageData.getGarageType()) {
            case GARAGE:
                return GarageType.GARAGE;
            case DUPLEX:
                return GarageType.DUPLEX;
            case CARPORT:
                return GarageType.CARPORT;
            case CAR_PARK:
                return GarageType.CAR_PARK;
            case UNDERGROUND_GARAGE:
                return GarageType.UNDERGROUND_GARAGE;
            case SPACE:
                return GarageType.STREET_PARKING;
            default:
                return GarageType.NO_INFORMATION;
        }
    }

    private static OfficeType convertOfficeType(CommercialData commercialData) {
        if (commercialData.getOfficeType() == null) {
            return OfficeType.OFFICE;
        }

        switch (commercialData.getOfficeType()) {
            case LOFT_ATELIER:
                return OfficeType.LOFT;
            case PRACTICE:
                return OfficeType.SURGERY;
            case OFFICE_CENTRE:
                return OfficeType.OFFICE_CENTRE;
            case PRACTICE_AREA:
                return OfficeType.SURGERY_FLOOR;
            case OFFICE_BUILDING:
                return OfficeType.OFFICE_BUILDING;
            case PRACTICE_BUILDING:
                return OfficeType.SURGERY_BUILDING;
            default:
                return OfficeType.OFFICE;
        }
    }

    private static FlooringType convertFlooringType(GroundType groundType) {
        if (groundType == null) {
            return FlooringType.NO_INFORMATION;
        }

        switch (groundType) {
            case LAMINATE:
                return FlooringType.LAMINATE;
            case PARQUET:
                return FlooringType.PARQUET;
            case TILE:
                return FlooringType.TILES;
            case CARPET:
                return FlooringType.CARPET;
            case OTHER:
                return FlooringType.WITHOUT;
            case STONE:
                return FlooringType.STONE;
            case PLASTIC:
                return FlooringType.PVC;
            default:
                return FlooringType.NO_INFORMATION;
        }
    }

    /**
     * @param energyCertificate
     * @param immoscoutEnergyCertificate
     * @return
     * @see <a href="http://api.immobilienscout24.de/useful/energy-certificate-2014.html">http://api.immobilienscout24.de</a>
     */
    private static EnergyPerformanceCertificate convertEnergyPerformanceCertificate(
            EnergyCertificate energyCertificate,
            Immoscout24EnergyCertificate immoscoutEnergyCertificate
    ) {
        EnergyPerformanceCertificate energyPerformanceCertificate = new EnergyPerformanceCertificate();
        energyPerformanceCertificate.setEnergyCertificateAvailability(EnergyCertificateAvailability.NOT_AVAILABLE_YET);

        if (energyCertificate == null || energyCertificate.getEnergyCertificateType() == null) {
            energyPerformanceCertificate.setEnergyCertificateAvailability(
                    EnergyCertificateAvailability.NOT_AVAILABLE_YET);
        } else {
            EnergyCertificateType energyCertificateType = energyCertificate.getEnergyCertificateType();
            CertificateCreationDate creationDate = energyCertificate.getCreationDate();

            if (creationDate == null && energyCertificateType == EnergyCertificateType.NO_AVAILABLE) {
                energyPerformanceCertificate.setEnergyCertificateAvailability(EnergyCertificateAvailability.NOT_AVAILABLE_YET);
            } else if (creationDate == CertificateCreationDate.NOT_NECESSARY) {
                energyPerformanceCertificate.setEnergyCertificateAvailability(EnergyCertificateAvailability.NOT_REQUIRED);
                energyPerformanceCertificate.setEnergyCertificateCreationDate(EnergyCertificateCreationDate.NOT_APPLICABLE);
            } else if (creationDate != null && energyCertificateType == EnergyCertificateType.DEMAND_IDENTIFICATION) {
                populateDemandIdentification(energyPerformanceCertificate, energyCertificate, immoscoutEnergyCertificate);
            } else if (creationDate != null
                    && energyCertificateType == EnergyCertificateType.USAGE_IDENTIFICATION
                    && energyCertificate.getUsageCertificate() != null) {
                populateUsageIdentification(energyPerformanceCertificate, energyCertificate, immoscoutEnergyCertificate);
            }
        }

        return energyPerformanceCertificate;
    }

    private static void populateDemandIdentification(
            EnergyPerformanceCertificate energyPerformanceCertificate,
            EnergyCertificate energyCertificate,
            Immoscout24EnergyCertificate immoscoutEnergyCertificate
    ) {
        CertificateCreationDate creationDate = energyCertificate.getCreationDate();

        energyPerformanceCertificate.setEnergyCertificateAvailability(EnergyCertificateAvailability.AVAILABLE);
        immoscoutEnergyCertificate.setBuildingEnergyRatingType(BuildingEnergyRatingType.ENERGY_REQUIRED);
        immoscoutEnergyCertificate.setThermalCharacteristic(
                Double.valueOf(energyCertificate.getDemandCertificate().getEndEnergyConsumption()));

        if (creationDate == CertificateCreationDate.APRIL_2014
                && energyCertificate.getDemandCertificate() != null) {
            energyPerformanceCertificate.setEnergyCertificateCreationDate(
                    EnergyCertificateCreationDate.BEFORE_01_MAY_2014);

        } else if (creationDate == CertificateCreationDate.MAY_2014
                && energyCertificate.getDemandCertificate() != null) {
            energyPerformanceCertificate.setEnergyCertificateCreationDate(
                    EnergyCertificateCreationDate.FROM_01_MAY_2014);

            if (energyCertificate.getDemandCertificate().getEnergyEfficiencyClass() != null) {
                energyPerformanceCertificate.setEnergyEfficiencyClass(convertEnergyEfficiencyClass(
                        energyCertificate.getDemandCertificate().getEnergyEfficiencyClass().getSymbol()));
            }
        }
    }

    private static void populateUsageIdentification(
            EnergyPerformanceCertificate energyPerformanceCertificate,
            EnergyCertificate energyCertificate,
            Immoscout24EnergyCertificate immoscoutEnergyCertificate
    ) {
        CertificateCreationDate creationDate = energyCertificate.getCreationDate();

        energyPerformanceCertificate.setEnergyCertificateAvailability(EnergyCertificateAvailability.AVAILABLE);
        immoscoutEnergyCertificate.setBuildingEnergyRatingType(BuildingEnergyRatingType.ENERGY_CONSUMPTION);

        if (energyCertificate.getUsageCertificate().isIncludesHeatConsumption()) {
            immoscoutEnergyCertificate.setEnergyConsumptionContainsWarmWater(
                    YesNotApplicableType.NOT_APPLICABLE);
        }

        if (creationDate == CertificateCreationDate.APRIL_2014) {
            energyPerformanceCertificate.setEnergyCertificateCreationDate(
                    EnergyCertificateCreationDate.BEFORE_01_MAY_2014);

            String ecp = energyCertificate.getUsageCertificate().getEnergyConsumptionParameter();
            immoscoutEnergyCertificate.setThermalCharacteristic(ecp != null ? Double.valueOf(ecp) : null);
        } else if (creationDate == CertificateCreationDate.MAY_2014
                && energyCertificate.getUsageCertificate() != null) {
            energyPerformanceCertificate.setEnergyCertificateCreationDate(
                    EnergyCertificateCreationDate.FROM_01_MAY_2014);

            immoscoutEnergyCertificate.setThermalCharacteristic(
                    Double.valueOf(energyCertificate.getUsageCertificate().getEnergyConsumption()));

            if (energyCertificate.getUsageCertificate().getEnergyEfficiencyClass() != null) {
                energyPerformanceCertificate.setEnergyEfficiencyClass(convertEnergyEfficiencyClass(
                        energyCertificate.getUsageCertificate().getEnergyEfficiencyClass().getSymbol()));
            }
        }
    }

    private static String convertEnergyEfficiencyClass(String eeClass) {
        if (eeClass == null || eeClass.trim().isEmpty()) {
            return NOT_APPLICABLE;
        } else if (eeClass.equalsIgnoreCase(ENERGY_CERTIFICATE_CLASS_A_P) || eeClass.equalsIgnoreCase(ENERGY_CERTIFICATE_CLASS_A_PLUS)) {
            return APLUS;
        }

        return eeClass;
    }

    private static HeatingTypeEnev2014 convertPrimaryType(HeaterFiringType heaterType) {

        if (heaterType == null) {
            return HeatingTypeEnev2014.NO_INFORMATION;
        }

        switch (heaterType) {
            case CENTRAL:
                return HeatingTypeEnev2014.CENTRAL_HEATING;
            case ELECTRIC_HEATING:
                return HeatingTypeEnev2014.ELECTRIC_HEATING;
            case FLOOR:
                return HeatingTypeEnev2014.SELF_CONTAINED_CENTRAL_HEATING;
            case GROUND_FLOOR:
                return HeatingTypeEnev2014.CENTRAL_HEATING;
            case LONG_DISTANCE:
                return HeatingTypeEnev2014.DISTRICT_HEATING;
            case OVEN:
                return HeatingTypeEnev2014.STOVE_HEATING;
            case GAS:
                return HeatingTypeEnev2014.GAS_HEATING;
            default:
                return HeatingTypeEnev2014.NO_INFORMATION;
        }
    }

    private static EnergySourcesEnev2014 convertPrimaryEnergy(HeaterType heater) {
        EnergySourcesEnev2014 energySourcesEnev2014
                = new EnergySourcesEnev2014();

        if (heater == null) {
            energySourcesEnev2014
                    .getEnergySourceEnev2014()
                    .add(EnergySourceEnev2014.NO_INFORMATION);
            return energySourcesEnev2014;
        }

        switch (heater) {
            case ALTERNATIVE:
                energySourcesEnev2014
                        .getEnergySourceEnev2014()
                        .add(EnergySourceEnev2014.NO_INFORMATION);
                break;
            case BLOCK:
                energySourcesEnev2014
                        .getEnergySourceEnev2014()
                        .add(EnergySourceEnev2014.LOCAL_HEATING);
                break;
            case COAL:
                energySourcesEnev2014
                        .getEnergySourceEnev2014()
                        .add(EnergySourceEnev2014.COAL);
                break;
            case ELECTRO:
                energySourcesEnev2014
                        .getEnergySourceEnev2014()
                        .add(EnergySourceEnev2014.ELECTRICITY);
                break;
            case GAS:
                energySourcesEnev2014
                        .getEnergySourceEnev2014()
                        .add(EnergySourceEnev2014.GAS);
                break;
            case GROUND:
                energySourcesEnev2014
                        .getEnergySourceEnev2014()
                        .add(EnergySourceEnev2014.GEOTHERMAL);
                break;
            case LIQUEFIED_GAS:
                energySourcesEnev2014
                        .getEnergySourceEnev2014()
                        .add(EnergySourceEnev2014.LIQUID_GAS);
                break;
            case LONG_DISTANCE:
                energySourcesEnev2014
                        .getEnergySourceEnev2014()
                        .add(EnergySourceEnev2014.DISTRICT_HEATING);
                break;
            case OIL:
                energySourcesEnev2014
                        .getEnergySourceEnev2014()
                        .add(EnergySourceEnev2014.OIL);
                break;
            case PELLET:
                energySourcesEnev2014
                        .getEnergySourceEnev2014()
                        .add(EnergySourceEnev2014.PELLET_HEATING);
                break;
            case WOOD:
                energySourcesEnev2014
                        .getEnergySourceEnev2014()
                        .add(EnergySourceEnev2014.WOOD);
                break;
            case SOLAR:
                energySourcesEnev2014
                        .getEnergySourceEnev2014()
                        .add(EnergySourceEnev2014.SOLAR_HEATING);
                break;
            default:
                break;
        }
        return energySourcesEnev2014;
    }

    private static CourtageInfo convertCourtage() {
        CourtageInfo courtageInfo = new CourtageInfo();
        courtageInfo.setHasCourtage(YesNoNotApplicableType.NO);

        return courtageInfo;
    }

    private static Wgs84Address convertAddress(Address address) {
        Wgs84Address wgs84Address = new Wgs84Address();
        if (address != null) {
            wgs84Address.setCity(address.getCity());
            wgs84Address.setStreet(address.getStreet());
            wgs84Address.setHouseNumber(address.getHouseNumber());
            wgs84Address.setPostcode(address.getZipCode());

            CountryRegion countryRegion = new CountryRegion();
            countryRegion.setCountry(CountryCode.DEU);
            wgs84Address.setInternationalCountryRegion(countryRegion);

            if (address.getRegion() != null) {
                countryRegion.setRegion(address.getRegion());
            }
        }

        return wgs84Address;
    }

    public static RealtorContactDetails convertContact(Contact contact, String defaultContactEmail) {
        RealtorContactDetails realtorContactDetails = new RealtorContactDetails();

        realtorContactDetails.setSalutation(SalutationType.NO_SALUTATION);
        realtorContactDetails.setFirstname(contact.getFirstName());
        realtorContactDetails.setLastname(contact.getName());
        realtorContactDetails.setEmail(defaultContactEmail);

        return realtorContactDetails;
    }
}
