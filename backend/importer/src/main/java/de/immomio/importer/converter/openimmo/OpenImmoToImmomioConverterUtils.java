package de.immomio.importer.converter.openimmo;

import com.neovisionaries.i18n.CountryCode;
import de.immomio.constants.property.EnergyClassType;
import de.immomio.data.base.type.common.ParkingType;
import de.immomio.data.base.type.property.BuildingConditionType;
import de.immomio.data.base.type.property.FlatType;
import de.immomio.data.base.type.property.FurnishingType;
import de.immomio.data.base.type.property.GarageType;
import de.immomio.data.base.type.property.GroundType;
import de.immomio.data.base.type.property.HeaterFiringType;
import de.immomio.data.base.type.property.HeaterType;
import de.immomio.data.base.type.property.HouseType;
import de.immomio.data.base.type.property.OfficeType;
import de.immomio.data.landlord.bean.property.certificate.EnergyCertificate;
import de.immomio.data.landlord.bean.property.data.IntercomType;
import de.immomio.openimmo.Anhang;
import de.immomio.openimmo.Befeuerung;
import de.immomio.openimmo.Boden;
import de.immomio.openimmo.Fahrstuhl;
import de.immomio.openimmo.Heizungsart;
import de.immomio.openimmo.Land;
import de.immomio.openimmo.Stellplatzart;
import de.immomio.openimmo.UserDefinedSimplefield;
import de.immomio.openimmo.typen.OpenImmoBueroPraxenTyp;
import de.immomio.openimmo.typen.OpenImmoDateiTyp;
import de.immomio.openimmo.typen.OpenImmoHausTyp;
import de.immomio.openimmo.typen.OpenImmoJahrgangTyp;
import de.immomio.openimmo.typen.OpenImmoMoebTyp;
import de.immomio.openimmo.typen.OpenImmoParkenTyp;
import de.immomio.openimmo.typen.OpenImmoWohnungTyp;
import de.immomio.openimmo.typen.OpenImmoZustandArtTyp;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;

@Slf4j
public class OpenImmoToImmomioConverterUtils {
    public static final Set<OpenImmoDateiTyp> DOCUMENT_SET = Set.of(OpenImmoDateiTyp.DOKUMENTE,
            OpenImmoDateiTyp.KARTEN_LAGEPLAN, OpenImmoDateiTyp.EPASS_SKALA, OpenImmoDateiTyp.GRUNDRISS);
    public static final Set<OpenImmoDateiTyp> PICTURE_SET = Set.of(OpenImmoDateiTyp.TITELBILD, OpenImmoDateiTyp.BILD,
            OpenImmoDateiTyp.INNENANSICHTEN, OpenImmoDateiTyp.AUSSENANSICHTEN, OpenImmoDateiTyp.ANBIETERLOGO,
            OpenImmoDateiTyp.PANORAMA);
    private static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp))$)";
    private static final Pattern COMPILED_IMAGE_PATTERN = Pattern.compile(IMAGE_PATTERN);
    private static final String YYYY = "yyyy";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(YYYY);

    public static boolean booleanToBoolean(Boolean b) {
        if (b == null) {
            return false;
        } else {
            return b;
        }
    }

    public static int bigDecimalToInt(BigDecimal bd) {
        if (bd == null) {
            return 0;
        } else {
            return bd.intValue();
        }
    }

    public static double bigDecimalToDouble(BigDecimal bd) {
        if (bd == null) {
            return 0;
        } else {
            return bd.doubleValue();
        }
    }

    public static CountryCode convertCountry(Land land) {
        if (land == null) {
            return null;
        }

        return CountryCode.getByCode(land.getIsoLand());
    }

    public static Double parseDouble(String value) {
        // never change a running system...
        DecimalFormatSymbols symbols;

        if (value == null) {
            return null;
        } else if (value.matches("[0-9]{1,3}((\\.[0-9]{3})+)?(,[0-9]+)?")) {
            symbols = new DecimalFormatSymbols();

            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator('.');
        } else if (value.matches("[0-9]{1,3}((,[0-9]{3})+)?(\\.[0-9]+)?")) {
            symbols = new DecimalFormatSymbols();

            symbols.setDecimalSeparator('.');
            symbols.setGroupingSeparator(',');
        } else {
            return null;
        }

        DecimalFormat df = new DecimalFormat();
        df.setDecimalFormatSymbols(symbols);

        Number number;
        try {
            number = df.parse(value);
        } catch (ParseException e) {
            return null;
        }

        return number.doubleValue();
    }

    public static GroundType convertBoden(Boden boden) {
        if (boden == null) {
            return GroundType.OTHER;
        }

        if (OpenImmoToImmomioConverterUtils.booleanToBoolean(boden.isFLIESEN())) {
            return GroundType.TILE;
        } else if (OpenImmoToImmomioConverterUtils.booleanToBoolean(boden.isSTEIN())) {
            return GroundType.STONE;
        } else if (OpenImmoToImmomioConverterUtils.booleanToBoolean(boden.isTEPPICH())) {
            return GroundType.CARPET;
        } else if (OpenImmoToImmomioConverterUtils.booleanToBoolean(boden.isPARKETT())) {
            return GroundType.PARQUET;
        } else if (OpenImmoToImmomioConverterUtils.booleanToBoolean(boden.isFERTIGPARKETT())) {
            return GroundType.ENGINEERED;
        } else if (OpenImmoToImmomioConverterUtils.booleanToBoolean(boden.isLAMINAT())) {
            return GroundType.LAMINATE;
        } else if (OpenImmoToImmomioConverterUtils.booleanToBoolean(boden.isDIELEN())) {
            return GroundType.BATTEN;
        } else if (OpenImmoToImmomioConverterUtils.booleanToBoolean(boden.isKUNSTSTOFF())) {
            return GroundType.PLASTIC;
        } else if (OpenImmoToImmomioConverterUtils.booleanToBoolean(boden.isESTRICH())) {
            return GroundType.ESTRICH;
        } else if (OpenImmoToImmomioConverterUtils.booleanToBoolean(boden.isDOPPELBODEN())) {
            return GroundType.DOUBLE_FLOOR;
        } else if (OpenImmoToImmomioConverterUtils.booleanToBoolean(boden.isLINOLEUM())) {
            return GroundType.LINOLEUM;
        } else if (OpenImmoToImmomioConverterUtils.booleanToBoolean(boden.isMARMOR())) {
            return GroundType.MARBLE;
        } else if (OpenImmoToImmomioConverterUtils.booleanToBoolean(boden.isTERRAKOTTA())) {
            return GroundType.TERRACOTTA;
        } else if (OpenImmoToImmomioConverterUtils.booleanToBoolean(boden.isGRANIT())) {
            return GroundType.GRANITE;
        } else {
            return GroundType.OTHER;
        }
    }

    public static ParkingType convertStellplatzart(Stellplatzart parkingSpace) {
        if (booleanToBoolean(parkingSpace.isGARAGE())) {
            return ParkingType.GARAGE;
        } else if (booleanToBoolean(parkingSpace.isTIEFGARAGE())) {
            return ParkingType.UNDERGROUND_CAR_PARK;
        } else if (booleanToBoolean(parkingSpace.isCARPORT())) {
            return ParkingType.CARPORT;
        } else if (booleanToBoolean(parkingSpace.isFREIPLATZ())) {
            return ParkingType.FREE_SPACE;
        } else if (booleanToBoolean(parkingSpace.isPARKHAUS())) {
            return ParkingType.CAR_PARK;
        } else if (booleanToBoolean(parkingSpace.isDUPLEX())) {
            return ParkingType.DUPLEX;
        } else {
            return null;
        }
    }

    public static EnergyCertificate.CertificateCreationDate convertJahrgang(String jahrgang) {
        OpenImmoJahrgangTyp jahrgangTyp = OpenImmoJahrgangTyp.getByKey(jahrgang);

        if (jahrgangTyp == null) {
            return null;
        }

        switch (jahrgangTyp) {
            case TWOTHOUSANDEIGHT:
                return EnergyCertificate.CertificateCreationDate.APRIL_2014;
            case TWOTHOUSANDFOURTEEN:
                return EnergyCertificate.CertificateCreationDate.MAY_2014;
            case OHNE:
                return EnergyCertificate.CertificateCreationDate.WITHOUT;
            case NICHT_NOETIG:
                return EnergyCertificate.CertificateCreationDate.NOT_NECESSARY;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static BuildingConditionType convertZustandArt(String zustandArt) {
        OpenImmoZustandArtTyp zustandArtTyp = EnumUtils.getEnumIgnoreCase(OpenImmoZustandArtTyp.class, zustandArt);

        if (zustandArt == null) {
            return BuildingConditionType.NO_INFORMATION;
        }

        switch (zustandArtTyp) {
            case ERSTBEZUG:
                return BuildingConditionType.FIRST_TIME_USE;
            case NEUWERTIG:
                return BuildingConditionType.MINT_CONDITION;
            case VOLL_SANIERT:
                return BuildingConditionType.FULLY_RENOVATED;
            case MODERNISIERT:
                return BuildingConditionType.MODERNIZED;
            case NACH_VEREINBARUNG:
                return BuildingConditionType.NEGOTIABLE;
            case SANIERUNGSBEDUERFTIG:
            case TEIL_VOLLRENOVIERUNGSBED:
                return BuildingConditionType.NEED_OF_RENOVATION;
            case TEIL_SANIERT:
                return BuildingConditionType.REFURBISHED;
            case ABRISSOBJEKT:
                return BuildingConditionType.RIPE_FOR_DEMOLITION;
            case GEPFLEGT:
                return BuildingConditionType.WELL_KEPT;
            default:
                return BuildingConditionType.NO_INFORMATION;
        }
    }

    public static FlatType convertFlatType(String type) {
        OpenImmoWohnungTyp wohnungTyp = OpenImmoWohnungTyp.getByKey(type);

        if (wohnungTyp == null) {
            return FlatType.OTHER;
        }

        switch (wohnungTyp) {
            case ERDGESCHOSS:
                return FlatType.GROUND_FLOOR;
            case SOUTERRAIN:
                return FlatType.HALF_BASEMENT;
            case LOFT_STUDIO_ATELIER:
                return FlatType.LOFT;
            case MAISONETTE:
                return FlatType.MAISONETTE;
            case PENTHOUSE:
                return FlatType.PENTHOUSE;
            case DACHGESCHOSS:
                return FlatType.ROOF_STOREY;
            case TERRASSEN:
                return FlatType.TERRACED_FLAT;
            case ETAGE:
                return FlatType.FLOOR;
            case GALERIE:
                return FlatType.GALERY;
            case APARTMENT:
                return FlatType.APARTMENT;
            default:
                return FlatType.OTHER;
        }
    }

    public static HouseType convertHouseType(String type) {
        OpenImmoHausTyp immoHausTyp = EnumUtils.getEnumIgnoreCase(OpenImmoHausTyp.class, type);

        if (immoHausTyp == null) {
            return HouseType.OTHER;
        }

        switch (immoHausTyp) {
            case REIHENHAUS:
                return HouseType.TOWNHOUSE;
            case REIHENEND:
                return HouseType.SUPERB_TOWNHOUSE;
            case REIHENMITTEL:
                return HouseType.ROW_HOUSE;
            case REIHENECK:
                return HouseType.CHAIN_HOUSE;
            case DOPPELHAUSHAELFTE:
                return HouseType.DUPLEX;
            case EINFAMILIENHAUS:
                return HouseType.DETACHED_HOUSE;
            case STADTHAUS:
                return HouseType.CITY_HOUSE;
            case BUNGALOW:
                return HouseType.BUNGALOW;
            case VILLA:
                return HouseType.VILLA;
            case RESTHOF:
                return HouseType.FARMSTEAD;
            case BAUERNHAUS:
                return HouseType.FARMHOUSE;
            case LANDHAUS:
                return HouseType.COUNTRY_HOUSE;
            case SCHLOSS:
                return HouseType.CASTLE;
            case ZWEIFAMILIENHAUS:
                return HouseType.TWIN_HOUSE;
            case MEHRFAMILIENHAUS:
                return HouseType.BLOCK_OF_FLATS;
            case FERIENHAUS:
                return HouseType.HOLIDAY_COTTAGE;
            case BERGHUETTE:
                return HouseType.MOUNTAIN_LODGE;
            case CHALET:
                return HouseType.CHALET;
            case STRANDHAUS:
                return HouseType.BEACH_HOUSE;
            case LAUBE_DATSCHE_GARTENHAUS:
                return HouseType.ARBOUR_COTTAGE_HOUSE;
            case APARTMENTHAUS:
                return HouseType.APARTMENT_BLOCK_HOUSE;
            case BURG:
                return HouseType.BURG;
            case HERRENHAUS:
                return HouseType.MANOR_HOUSE;
            case FINCA:
                return HouseType.FINCA;
            case RUSTICO:
                return HouseType.RUSTICO;
            case FERTIGHAUS:
                return HouseType.FINISHED_HOUSE;
            default:
                return HouseType.OTHER;
        }
    }

    public static OfficeType convertCommercialType(String type) {
        OpenImmoBueroPraxenTyp bueroPraxenTyp = EnumUtils.getEnumIgnoreCase(OpenImmoBueroPraxenTyp.class, type);

        if (bueroPraxenTyp == null) {
            return null;
        }

        switch (bueroPraxenTyp) {
            case BUEROFLAECHE:
                return OfficeType.OFFICE;
            case BUEROHAUS:
                return OfficeType.OFFICE_BUILDING;
            case BUEROZENTRUM:
                return OfficeType.OFFICE_CENTRE;
            case LOFT_ATELIER:
                return OfficeType.LOFT_ATELIER;
            case PRAXIS:
                return OfficeType.PRACTICE;
            case PRAXISFLAECHE:
                return OfficeType.PRACTICE_AREA;
            case PRAXISHAUS:
                return OfficeType.PRACTICE_BUILDING;
            case AUSSTELLUNGSFLAECHE:
                return OfficeType.EXHIBITION_AREA;
            case COWORKING:
                return OfficeType.COWORKING;
            case SHARED_OFFICE:
                return OfficeType.SHARED_OFFICE;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static GarageType convertGarageType(String type) {
        OpenImmoParkenTyp parkenTyp = EnumUtils.getEnumIgnoreCase(OpenImmoParkenTyp.class, type);

        if (parkenTyp == null) {
            return GarageType.NO_INFORMATION;
        }

        switch (parkenTyp) {
            case STELLPLATZ:
                return GarageType.SPACE;
            case CARPORT:
                return GarageType.CARPORT;
            case DOPPELGARAGE:
                return GarageType.DOUBLE_GARAGE;
            case DUPLEX:
                return GarageType.DUPLEX;
            case TIEFGARAGE:
                return GarageType.UNDERGROUND_GARAGE;
            case BOOTSLIEGEPLATZ:
                return GarageType.BOATYARD;
            case EINZELGARAGE:
                return GarageType.GARAGE;
            case PARKHAUS:
                return GarageType.CAR_PARK;
            case TIEFGARAGENSTELLPLATZ:
                return GarageType.UNDERGROUND_GARAGE_SPACE;
            case PARKPLATZ_STROM:
                return GarageType.SPACE_ELECTRICITY;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static HeaterType convertPrimaryEnergy(Befeuerung befeuerung) {
        if (befeuerung == null) {
            return HeaterType.ALTERNATIVE;
        }

        if (OpenImmoToImmomioConverterUtils.booleanToBoolean(befeuerung.isALTERNATIV())) {
            return HeaterType.ALTERNATIVE;
        } else if (OpenImmoToImmomioConverterUtils.booleanToBoolean(befeuerung.isBLOCK())) {
            return HeaterType.BLOCK;
        } else if (OpenImmoToImmomioConverterUtils.booleanToBoolean(befeuerung.isKOHLE())) {
            return HeaterType.COAL;
        } else if (OpenImmoToImmomioConverterUtils.booleanToBoolean(befeuerung.isELEKTRO())) {
            return HeaterType.ELECTRO;
        } else if (OpenImmoToImmomioConverterUtils.booleanToBoolean(befeuerung.isGAS())) {
            return HeaterType.GAS;
        } else if (OpenImmoToImmomioConverterUtils.booleanToBoolean(befeuerung.isERDWAERME())) {
            return HeaterType.GROUND;
        } else if (OpenImmoToImmomioConverterUtils.booleanToBoolean(befeuerung.isFLUESSIGGAS())) {
            return HeaterType.LIQUEFIED_GAS;
        } else if (OpenImmoToImmomioConverterUtils.booleanToBoolean(befeuerung.isFERN())) {
            return HeaterType.LONG_DISTANCE;
        } else if (OpenImmoToImmomioConverterUtils.booleanToBoolean(befeuerung.isOEL())) {
            return HeaterType.OIL;
        } else if (OpenImmoToImmomioConverterUtils.booleanToBoolean(befeuerung.isPELLET())) {
            return HeaterType.PELLET;
        } else if (OpenImmoToImmomioConverterUtils.booleanToBoolean(befeuerung.isHOLZ())) {
            return HeaterType.WOOD;
        } else if (OpenImmoToImmomioConverterUtils.booleanToBoolean(befeuerung.isSOLAR())) {
            return HeaterType.SOLAR;
        }

        return HeaterType.ALTERNATIVE;
    }

    public static HeaterFiringType convertHeaterType(Heizungsart heizungsart) {
        if (heizungsart == null) {
            return HeaterFiringType.CENTRAL;
        }

        if (OpenImmoToImmomioConverterUtils.booleanToBoolean(heizungsart.isOFEN())) {
            return HeaterFiringType.OVEN;
        } else if (OpenImmoToImmomioConverterUtils.booleanToBoolean(heizungsart.isZENTRAL())) {
            return HeaterFiringType.CENTRAL;
        } else if (OpenImmoToImmomioConverterUtils.booleanToBoolean(heizungsart.isETAGE())) {
            return HeaterFiringType.FLOOR;
        } else if (OpenImmoToImmomioConverterUtils.booleanToBoolean(heizungsart.isFUSSBODEN())) {
            return HeaterFiringType.GROUND_FLOOR;
        } else if (OpenImmoToImmomioConverterUtils.booleanToBoolean(heizungsart.isFERN())) {
            return HeaterFiringType.LONG_DISTANCE;
        }

        return HeaterFiringType.CENTRAL;
    }

    public static Date parseYear(String year) {
        try {
            return dateFormat.parse(year);
        } catch (ParseException e) {
            log.info("Error parsing year -> " + year, e);
        }
        return null;
    }

    public static FurnishingType convertFurnishingType(String type) {
        OpenImmoMoebTyp moebTyp = EnumUtils.getEnumIgnoreCase(OpenImmoMoebTyp.class, type);

        if (moebTyp == null) {
            return null;
        }

        switch (moebTyp) {
            case VOLL:
                return FurnishingType.FULL;
            case TEIL:
                return FurnishingType.PARTLY;
            default:
                return null;
        }
    }

    public static IntercomType convertIntercomType(String type) {
        IntercomType intercomType = EnumUtils.getEnumIgnoreCase(IntercomType.class, type);

        if (intercomType == null) {
            return null;
        }

        switch (intercomType) {
            case VIDEO:
                return IntercomType.VIDEO;
            case STANDARD:
                return IntercomType.STANDARD;
            default:
                return null;
        }
    }

    public static boolean convertElevator(Fahrstuhl fahrstuhl) {
        return fahrstuhl != null && fahrstuhl.isPERSONEN() == Boolean.TRUE;
    }

    public static boolean isDocument(Anhang anhang) {
        return anhang != null && DOCUMENT_SET.contains(OpenImmoDateiTyp.valueOf(anhang.getGruppe()));
    }

    public static boolean isPicture(Anhang anhang) {
        if (anhang == null) {
            return false;
        }

        if (anhang.getGruppe() == null || anhang.getGruppe().isEmpty()) {
            return COMPILED_IMAGE_PATTERN.matcher(anhang.getDaten().getPfad()).matches();
        }

        return PICTURE_SET.contains(OpenImmoDateiTyp.valueOf(anhang.getGruppe()));
    }

    public static EnergyClassType convertEnergyClassType(String classType) {
        return EnergyClassType.bySymbol(classType);
    }

    public static String getValueFromSimpleField(List<UserDefinedSimplefield> simpleFields, String field) {
        Optional<UserDefinedSimplefield> optionalField = simpleFields.stream()
                .filter(userDefinedSimpleField -> field.equalsIgnoreCase(userDefinedSimpleField.getFeldname()))
                .findFirst();

        return optionalField.map(UserDefinedSimplefield::getContent).orElse("");
    }
}
