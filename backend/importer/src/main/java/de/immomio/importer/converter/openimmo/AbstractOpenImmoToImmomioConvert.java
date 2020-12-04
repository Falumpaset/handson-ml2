package de.immomio.importer.converter.openimmo;

import de.immomio.constants.exceptions.OpenImmoToImmomioConverterException;
import de.immomio.constants.exceptions.OpenImmoToImmomioUnsuportedLocationException;
import de.immomio.data.base.type.common.FileType;
import de.immomio.data.base.type.common.ParkingType;
import de.immomio.data.landlord.bean.property.AvailableFrom;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.data.shared.bean.common.GeoCoordinates;
import de.immomio.data.shared.bean.common.ParkingSpace;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.openimmo.Anhaenge;
import de.immomio.openimmo.Anhang;
import de.immomio.openimmo.Freitexte;
import de.immomio.openimmo.Geo;
import de.immomio.openimmo.Geokoordinaten;
import de.immomio.openimmo.Immobilie;
import de.immomio.openimmo.Kueche;
import de.immomio.openimmo.Objektart;
import de.immomio.openimmo.Preise;
import de.immomio.openimmo.Stellplatz;
import de.immomio.openimmo.Unterkellert;
import de.immomio.openimmo.VerwaltungObjekt;
import de.immomio.openimmo.VerwaltungTechn;
import de.immomio.openimmo.Zustand;
import de.immomio.openimmo.ZustandAngaben;
import de.immomio.openimmo.typen.OpenImmoDateiOrtTyp;
import de.immomio.openimmo.typen.OpenImmoDateiTyp;
import de.immomio.openimmo.typen.OpenImmoKellerTyp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author Fabian Beck
 */

@Slf4j
public abstract class AbstractOpenImmoToImmomioConvert {

    private final String dateFormat = "yyyy-MM-dd";

    public void addImmobilie(Property property, Immobilie immobilie) throws OpenImmoToImmomioConverterException {
        addObjektart(property, immobilie.getObjektkategorie().getObjektart());
        addGeo(property, immobilie.getGeo());
        addFreitexte(property, immobilie.getFreitexte());
        addVerwaltungTechn(property, immobilie.getVerwaltungTechn());
        addAnhaenge(property, immobilie.getAnhaenge());
        addZustandAngaben(property, immobilie.getZustandAngaben());
        addVerwaltungObjekt(property, immobilie.getVerwaltungObjekt());
    }

    protected abstract void addObjektart(Property property, Objektart objektart) throws
            OpenImmoToImmomioConverterException;

    protected void addGeo(Property property, Geo geo) {
        if (geo == null) {
            return;
        }

        PropertyData data = property.getData();

        data.setFloor(geo.getEtage());
        data.setNumberOfFloors(geo.getAnzahlEtagen());
        data.setAddress(createAddress(geo));
    }

    protected Address createAddress(Geo geo) {
        Address address = new Address();

        address.setCity(geo.getOrt());
        address.setZipCode(geo.getPlz());
        address.setStreet(geo.getStrasse());
        address.setRegion(geo.getBundesland());
        address.setHouseNumber(geo.getHausnummer());
        address.setCountry(OpenImmoToImmomioConverterUtils.convertCountry(geo.getLand()));
        address.setCoordinates(createGeokoordinaten(geo.getGeokoordinaten()));

        return address;
    }

    protected GeoCoordinates createGeokoordinaten(Geokoordinaten geokoordinaten) {
        if (geokoordinaten == null) {
            return null;
        }

        if (geokoordinaten.getBreitengrad() < 0 || geokoordinaten.getLaengengrad() < 0) {
            return null;
        }

        GeoCoordinates coordinates = new GeoCoordinates();
        coordinates.setLongitude((double) geokoordinaten.getLaengengrad());
        coordinates.setLatitude((double) geokoordinaten.getBreitengrad());
        return coordinates;
    }

    protected void addFreitexte(Property property, Freitexte freitexte) {
        if (freitexte == null) {
            return;
        }

        PropertyData data = property.getData();

        data.setName(freitexte.getObjekttitel());
        data.setObjectLocationText(freitexte.getLage());
        data.setObjectDescription(freitexte.getObjektbeschreibung());
        data.setFurnishingDescription(freitexte.getAusstattBeschr());
        data.setObjectMiscellaneousText(freitexte.getSonstigeAngaben());
    }

    protected void addVerwaltungTechn(Property property, VerwaltungTechn verwaltungTechn) {
        if (verwaltungTechn == null) {
            return;
        }

        property.setExternalId(verwaltungTechn.getObjektnrIntern());
        property.getData().setReferenceId(verwaltungTechn.getObjektnrExtern());
    }

    protected void addAnhaenge(Property property, Anhaenge anhaenge) {
        if (anhaenge == null) {
            return;
        }

        PropertyData data = property.getData();

        data.setAttachments(createPictures(anhaenge.getAnhang()));
        data.setDocuments(createDocuments(anhaenge.getAnhang()));
    }

    protected void addZustandAngaben(Property property, ZustandAngaben zustandAngaben) {
        if (zustandAngaben == null) {
            return;
        }

        PropertyData data = property.getData();
        data.setConstructionYear(zustandAngaben.getBaujahr());

        Zustand zustand = zustandAngaben.getZustand();
        if (zustand != null) {
            data.setBuildingCondition(OpenImmoToImmomioConverterUtils.convertZustandArt(zustand.getZustandArt()));
        }
    }

    protected void addStellplaetze(Property property, Preise preise) {
        List<ParkingSpace> parkingSpaces = new ArrayList<>();

        addParkingSpaceIfExist(parkingSpaces, preise.getStpGarage(), ParkingType.GARAGE);
        addParkingSpaceIfExist(parkingSpaces, preise.getStpTiefgarage(), ParkingType.UNDERGROUND_CAR_PARK);
        addParkingSpaceIfExist(parkingSpaces, preise.getStpCarport(), ParkingType.CARPORT);
        addParkingSpaceIfExist(parkingSpaces, preise.getStpFreiplatz(), ParkingType.FREE_SPACE);
        addParkingSpaceIfExist(parkingSpaces, preise.getStpParkhaus(), ParkingType.CAR_PARK);
        addParkingSpaceIfExist(parkingSpaces, preise.getStpDuplex(), ParkingType.DUPLEX);

        if (!parkingSpaces.isEmpty()) {
            property.getData().setParkingSpaces(parkingSpaces);
        }
    }

    protected void addKueche(Property property, Kueche kueche) {
        if (kueche != null) {
            property.getData().setKitchenette(OpenImmoToImmomioConverterUtils.booleanToBoolean(kueche.isEBK()));
        } else {
            property.getData().setKitchenette(false);
        }
    }

    protected void addKeller(Property property, Unterkellert unterkellert) {
        if (unterkellert != null && StringUtils.isNotBlank(unterkellert.getKeller())) {
            property.getData()
                    .setBasementAvailable(
                            unterkellert.getKeller().equals(OpenImmoKellerTyp.JA.name()) || unterkellert.getKeller()
                                    .equals(OpenImmoKellerTyp.TEIL.name()));
        }
    }

    protected void addVerwaltungObjekt(Property property, VerwaltungObjekt verwaltungObjekt) {
        if (verwaltungObjekt == null) {
            return;
        }

        PropertyData data = property.getData();

        Boolean objektadresseFreigeben = verwaltungObjekt.isObjektadresseFreigeben();
        if (objektadresseFreigeben != null) {
            data.setShowAddress(OpenImmoToImmomioConverterUtils.booleanToBoolean(objektadresseFreigeben));
        } else {
            data.setShowAddress(true);
        }
    }

    protected void addBasePrice(Preise preise, PropertyData data) {
        if (preise.getKaltmiete() != null && preise.getKaltmiete().compareTo(BigDecimal.ZERO) > 0) {
            data.setBasePrice(OpenImmoToImmomioConverterUtils.bigDecimalToDouble(preise.getKaltmiete()));
        } else if (preise.getNettokaltmiete() != null && preise.getNettokaltmiete().compareTo(BigDecimal.ZERO) > 0) {
            data.setBasePrice(OpenImmoToImmomioConverterUtils.bigDecimalToDouble(preise.getNettokaltmiete()));
        }
    }

    private List<S3File> createPictures(List<Anhang> anhaenge) {
        AtomicLong atomicLong = new AtomicLong(1);

        return anhaenge.stream().filter(OpenImmoToImmomioConverterUtils::isPicture).map(anhang -> {
            try {
                return createPictureS3File(anhang, atomicLong);
            } catch (OpenImmoToImmomioUnsuportedLocationException e) {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private List<S3File> createDocuments(List<Anhang> anhaenge) {

        return anhaenge.stream().filter(OpenImmoToImmomioConverterUtils::isDocument).map(anhang -> {
            try {
                return createDcumentS3File(anhang);
            } catch (OpenImmoToImmomioUnsuportedLocationException e) {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private S3File createS3File(Anhang anhang) throws OpenImmoToImmomioUnsuportedLocationException {
        S3File s3File = new S3File();

        s3File.setUrl(createPath(anhang));
        s3File.setTitle(anhang.getAnhangtitel());
        s3File.setExtension(FilenameUtils.getExtension(s3File.getUrl().toLowerCase()));

        s3File.setEncrypted(false);
        s3File.setIdentifier(null);

        return s3File;
    }

    private S3File createPictureS3File(Anhang anhang, AtomicLong atomicLong) throws
            OpenImmoToImmomioUnsuportedLocationException {
        S3File s3File = createS3File(anhang);

        s3File.setType(FileType.IMG);

        if (OpenImmoDateiTyp.TITELBILD.name().equals(anhang.getGruppe().toUpperCase())) {
            s3File.setIndex(0L);
        } else {
            s3File.setIndex(atomicLong.getAndAdd(1));
        }

        return s3File;
    }

    private S3File createDcumentS3File(Anhang anhang) throws OpenImmoToImmomioUnsuportedLocationException {
        S3File s3File = createS3File(anhang);

        if (OpenImmoDateiTyp.GRUNDRISS.name().equals(anhang.getGruppe().toUpperCase())) {
            s3File.setType(FileType.FLOOR_PLAN);
        } else {
            s3File.setType(FileType.PDF);
        }

        return s3File;
    }

    private String createPath(Anhang anhang) throws OpenImmoToImmomioUnsuportedLocationException {
        OpenImmoDateiOrtTyp dateiOrtTyp = EnumUtils.getEnumIgnoreCase(OpenImmoDateiOrtTyp.class, anhang.getLocation());

        if (dateiOrtTyp == null) {
            log.error("Unsupported location -> attachment skipped [" + anhang.getLocation() + "]");
            throw new OpenImmoToImmomioUnsuportedLocationException(
                    "Unsupported location -> attachment skipped [" + anhang.getLocation() + "]");
        }

        switch (dateiOrtTyp) {
            case EXTERN:
            case INTERN:
                return "file://" + anhang.getDaten().getPfad();
            case REMOTE:
                return anhang.getDaten().getPfad();
            default:
                throw new IllegalArgumentException();
        }
    }

    private void addParkingSpaceIfExist(List<ParkingSpace> parkingSpaces, Stellplatz parkingPlace,
                                        ParkingType parkingType) {

        if (parkingPlace != null && (parkingPlace.getAnzahl() != null && parkingPlace.getAnzahl() > 0)) {
            ParkingSpace parkingSpace = new ParkingSpace(parkingType, parkingPlace.getStellplatzmiete(),
                    parkingPlace.getAnzahl());
            parkingSpaces.add(parkingSpace);
        }
    }

    protected AvailableFrom convertStringToAvailableFrom(VerwaltungObjekt verwaltungObjekt, boolean isStringPossible) {
        if (verwaltungObjekt.getVerfuegbarAb() != null && !verwaltungObjekt.getVerfuegbarAb().trim().isEmpty()) {
            LocalDate availableFrom = convertStringToLocalDate(verwaltungObjekt.getVerfuegbarAb());

            if (availableFrom != null) {
                return new AvailableFrom(availableFrom);
            } else if (isStringPossible) {
                return new AvailableFrom(verwaltungObjekt.getVerfuegbarAb());
            }

        }
        return new AvailableFrom();
    }

    protected LocalDate convertStringToLocalDate(String dateAsString) {
        try {
            return LocalDate.parse(dateAsString);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
