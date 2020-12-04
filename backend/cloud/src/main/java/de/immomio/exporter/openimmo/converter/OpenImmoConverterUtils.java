package de.immomio.exporter.openimmo.converter;

import de.immomio.data.base.type.property.FlatType;
import de.immomio.data.base.type.property.FurnishingType;
import de.immomio.data.base.type.property.GarageType;
import de.immomio.data.base.type.property.HeaterType;
import de.immomio.data.base.type.property.HouseType;
import de.immomio.data.base.type.property.OfficeType;
import de.immomio.data.landlord.bean.property.certificate.EnergyCertificate;
import de.immomio.openimmo.typen.OpenImmoBueroPraxenTyp;
import de.immomio.openimmo.typen.OpenImmoHausTyp;
import de.immomio.openimmo.typen.OpenImmoJahrgangTyp;
import de.immomio.openimmo.typen.OpenImmoMoebTyp;
import de.immomio.openimmo.typen.OpenImmoParkenTyp;
import de.immomio.openimmo.typen.OpenImmoPrimaerenergietraegerTyp;
import de.immomio.openimmo.typen.OpenImmoWohnungTyp;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import static de.immomio.openimmo.constants.OpenImmoConstants.OPENIMMOKENNUNG;

/**
 * @author Fabian Beck
 */

@Slf4j
public class OpenImmoConverterUtils {

    public static BigDecimal convertNumberToBigDecimal(Number nmb) {
        return convertNumberToBigDecimal(nmb, false);
    }

    public static BigDecimal convertNumberToBigDecimal(Number nmb, boolean round) {
        if (nmb == null) {
            return null;
        }

        BigDecimal bd = BigDecimal.valueOf(nmb.doubleValue());

        if (round) {
            bd = bd.setScale(2, RoundingMode.HALF_UP);
        }

        return bd;
    }

    public static String generateANID() {
        StringBuilder sb = new StringBuilder();
        sb.append("A");
        sb.append(OPENIMMOKENNUNG);
        sb.append(System.currentTimeMillis());

        sb.append(getRandomHexString(31 - sb.length()));

        return sb.toString();
    }

    public static String getRandomHexString(int numchars) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < numchars) {
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, numchars);
    }

    public static String convertFurnishingType(FurnishingType type) {
        if (type == null) {
            return null;
        }

        OpenImmoMoebTyp convertedType;
        switch (type) {
            case FULL:
                convertedType = OpenImmoMoebTyp.VOLL;
                break;
            case PARTLY:
                convertedType = OpenImmoMoebTyp.TEIL;
                break;
            default:
                log.error("FurnishingType " + type + " not translatable");
                throw new IllegalArgumentException("FurnishingType " + type + " not translatable");
        }

        return convertedType.name();
    }

    public static String convertGarageType(GarageType type) {
        if (type == null) {
            log.warn("GarageType was null -> return default");
            return OpenImmoParkenTyp.STELLPLATZ.name();
        }

        OpenImmoParkenTyp convertedType;
        switch (type) {
            case NO_INFORMATION:
            case SPACE:
                convertedType = OpenImmoParkenTyp.STELLPLATZ;
                break;
            case CARPORT:
                convertedType = OpenImmoParkenTyp.CARPORT;
                break;
            case DOUBLE_GARAGE:
                convertedType = OpenImmoParkenTyp.DOPPELGARAGE;
                break;
            case DUPLEX:
                convertedType = OpenImmoParkenTyp.DUPLEX;
                break;
            case UNDERGROUND_GARAGE:
                convertedType = OpenImmoParkenTyp.TIEFGARAGE;
                break;
            case BOATYARD:
                convertedType = OpenImmoParkenTyp.BOOTSLIEGEPLATZ;
                break;
            case GARAGE:
                convertedType = OpenImmoParkenTyp.EINZELGARAGE;
                break;
            case CAR_PARK:
                convertedType = OpenImmoParkenTyp.PARKHAUS;
                break;
            case UNDERGROUND_GARAGE_SPACE:
                convertedType = OpenImmoParkenTyp.TIEFGARAGENSTELLPLATZ;
                break;
            case SPACE_ELECTRICITY:
                convertedType = OpenImmoParkenTyp.PARKPLATZ_STROM;
                break;
            default:
                log.error("GarageType " + type + " not translatable");
                throw new IllegalArgumentException("GarageType " + type + " not translatable");
        }

        return convertedType.name();
    }

    public static OpenImmoPrimaerenergietraegerTyp convertPrimaerEnergieTraeger(HeaterType heater) {
        if (heater == null) {
            return OpenImmoPrimaerenergietraegerTyp.KEINE_ANGABEN;
        }

        switch (heater) {
            case ALTERNATIVE:
                return OpenImmoPrimaerenergietraegerTyp.ALTERNATIV;
            case BLOCK:
                return OpenImmoPrimaerenergietraegerTyp.BLOCKHEIZKRAFTWERK;
            case COAL:
                return OpenImmoPrimaerenergietraegerTyp.KOHLE;
            case ELECTRO:
                return OpenImmoPrimaerenergietraegerTyp.ELEKTRO;
            case GAS:
                return OpenImmoPrimaerenergietraegerTyp.GAS;
            case GROUND:
                return OpenImmoPrimaerenergietraegerTyp.ERDWAERME;
            case LIQUEFIED_GAS:
                return OpenImmoPrimaerenergietraegerTyp.FLUESSIGGAS;
            case LONG_DISTANCE:
                return OpenImmoPrimaerenergietraegerTyp.FERNWAERME;
            case OIL:
                return OpenImmoPrimaerenergietraegerTyp.OIL;
            case PELLET:
                return OpenImmoPrimaerenergietraegerTyp.PELLET;
            case WOOD:
                return OpenImmoPrimaerenergietraegerTyp.HOLZ;
            case SOLAR:
                return OpenImmoPrimaerenergietraegerTyp.SOLAR;
            default:
                log.error("HeaterType " + heater + " not translatable");
                throw new IllegalArgumentException("HeaterType " + heater + " not translatable");
        }
    }

    public static OpenImmoWohnungTyp convertFlatType(FlatType flatType) {
        if (flatType == null) {
            return OpenImmoWohnungTyp.KEINE_ANGABE;
        }

        switch (flatType) {
            case GROUND_FLOOR:
                return OpenImmoWohnungTyp.ERDGESCHOSS;
            case HALF_BASEMENT:
                return OpenImmoWohnungTyp.SOUTERRAIN;
            case LOFT:
                return OpenImmoWohnungTyp.LOFT_STUDIO_ATELIER;
            case MAISONETTE:
                return OpenImmoWohnungTyp.MAISONETTE;
            case PENTHOUSE:
                return OpenImmoWohnungTyp.PENTHOUSE;
            case FLOOR:
            case RAISED_GROUND_FLOOR:
                return OpenImmoWohnungTyp.ETAGE;
            case ROOF_STOREY:
                return OpenImmoWohnungTyp.DACHGESCHOSS;
            case TERRACED_FLAT:
                return OpenImmoWohnungTyp.TERRASSEN;
            case GALERY:
                return OpenImmoWohnungTyp.GALERIE;
            case APARTMENT:
                return OpenImmoWohnungTyp.APARTMENT;
            default:
                return OpenImmoWohnungTyp.KEINE_ANGABE;
        }
    }

    public static String convertBueroTyp(OfficeType type) {
        if (type == null) {
            log.warn("GarageType was null -> return default");
            return OpenImmoBueroPraxenTyp.BUEROFLAECHE.name();
        }

        OpenImmoBueroPraxenTyp convertedType;
        switch (type) {
            case OFFICE:
                convertedType = OpenImmoBueroPraxenTyp.BUEROFLAECHE;
                break;
            case OFFICE_BUILDING:
                convertedType = OpenImmoBueroPraxenTyp.BUEROHAUS;
                break;
            case OFFICE_CENTRE:
                convertedType = OpenImmoBueroPraxenTyp.BUEROZENTRUM;
                break;
            case LOFT_ATELIER:
                convertedType = OpenImmoBueroPraxenTyp.LOFT_ATELIER;
                break;
            case PRACTICE:
                convertedType = OpenImmoBueroPraxenTyp.PRAXIS;
                break;
            case PRACTICE_AREA:
                convertedType = OpenImmoBueroPraxenTyp.PRAXISFLAECHE;
                break;
            case COWORKING:
                convertedType = OpenImmoBueroPraxenTyp.COWORKING;
                break;
            case SHARED_OFFICE:
                convertedType = OpenImmoBueroPraxenTyp.SHARED_OFFICE;
                break;
            case EXHIBITION_AREA:
                convertedType = OpenImmoBueroPraxenTyp.AUSSTELLUNGSFLAECHE;
                break;
            case PRACTICE_BUILDING:
                convertedType = OpenImmoBueroPraxenTyp.PRAXISHAUS;
                break;
            default:
                log.error("OfficeType " + type + " not translatable");
                throw new IllegalArgumentException("OfficeType " + type + " not translatable");
        }

        return convertedType.name();
    }

    public static OpenImmoHausTyp convertHouseType(HouseType type) {
        if (type == null) {
            return OpenImmoHausTyp.KEINE_ANGABE;
        }

        switch (type) {
            case TOWNHOUSE:
                return OpenImmoHausTyp.REIHENHAUS;
            case SUPERB_TOWNHOUSE:
                return OpenImmoHausTyp.REIHENEND;
            case ROW_HOUSE:
                return OpenImmoHausTyp.REIHENMITTEL;
            case CHAIN_HOUSE:
                return OpenImmoHausTyp.REIHENECK;
            case DUPLEX:
                return OpenImmoHausTyp.DOPPELHAUSHAELFTE;
            case DETACHED_HOUSE:
                return OpenImmoHausTyp.EINFAMILIENHAUS;
            case CITY_HOUSE:
                return OpenImmoHausTyp.STADTHAUS;
            case BUNGALOW:
                return OpenImmoHausTyp.BUNGALOW;
            case VILLA:
                return OpenImmoHausTyp.VILLA;
            case FARMSTEAD:
                return OpenImmoHausTyp.RESTHOF;
            case FARMHOUSE:
                return OpenImmoHausTyp.BAUERNHAUS;
            case COUNTRY_HOUSE:
                return OpenImmoHausTyp.LANDHAUS;
            case CASTLE:
                return OpenImmoHausTyp.SCHLOSS;
            case TWIN_HOUSE:
                return OpenImmoHausTyp.ZWEIFAMILIENHAUS;
            case BLOCK_OF_FLATS:
                return OpenImmoHausTyp.MEHRFAMILIENHAUS;
            case HOLIDAY_COTTAGE:
                return OpenImmoHausTyp.FERIENHAUS;
            case MOUNTAIN_LODGE:
                return OpenImmoHausTyp.BERGHUETTE;
            case CHALET:
                return OpenImmoHausTyp.CHALET;
            case BEACH_HOUSE:
                return OpenImmoHausTyp.STRANDHAUS;
            case ARBOUR_COTTAGE_HOUSE:
                return OpenImmoHausTyp.LAUBE_DATSCHE_GARTENHAUS;
            case APARTMENT_BLOCK_HOUSE:
                return OpenImmoHausTyp.APARTMENTHAUS;
            case BURG:
                return OpenImmoHausTyp.BURG;
            case MANOR_HOUSE:
                return OpenImmoHausTyp.HERRENHAUS;
            case FINCA:
                return OpenImmoHausTyp.FINCA;
            case RUSTICO:
                return OpenImmoHausTyp.RUSTICO;
            case FINISHED_HOUSE:
                return OpenImmoHausTyp.FERTIGHAUS;
            default:
                return OpenImmoHausTyp.KEINE_ANGABE;
        }
    }

    public static OpenImmoJahrgangTyp convertCreationDateType(
            EnergyCertificate.CertificateCreationDate certificateCreationDate) {
        if (certificateCreationDate == null) {
            return OpenImmoJahrgangTyp.OHNE;
        }

        switch (certificateCreationDate) {
            case APRIL_2014:
                return OpenImmoJahrgangTyp.TWOTHOUSANDEIGHT;
            case MAY_2014:
                return OpenImmoJahrgangTyp.TWOTHOUSANDFOURTEEN;
            case NOT_NECESSARY:
                return OpenImmoJahrgangTyp.NICHT_NOETIG;
            case WITHOUT:
            default:
                return OpenImmoJahrgangTyp.OHNE;
        }
    }
}
