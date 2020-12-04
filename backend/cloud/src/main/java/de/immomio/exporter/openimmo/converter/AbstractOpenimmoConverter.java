package de.immomio.exporter.openimmo.converter;

import de.immomio.common.amazon.s3.AbstractS3;
import de.immomio.config.ApplicationMessageSource;
import de.immomio.data.base.type.common.FileType;
import de.immomio.data.base.type.property.BuildingConditionType;
import de.immomio.data.base.type.property.FurnishingType;
import de.immomio.data.base.type.property.GroundType;
import de.immomio.data.base.type.property.HeaterFiringType;
import de.immomio.data.base.type.property.HeaterType;
import de.immomio.data.landlord.bean.property.AvailableFrom;
import de.immomio.data.landlord.bean.property.Contact;
import de.immomio.data.landlord.bean.property.certificate.EnergyCertificate;
import de.immomio.data.landlord.bean.property.certificate.UsageCertificate;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.user.profile.LandlordUserProfile;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.data.shared.bean.common.GeoCoordinates;
import de.immomio.data.shared.bean.common.ParkingSpace;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.openimmo.Aktion;
import de.immomio.openimmo.Anbieter;
import de.immomio.openimmo.Anhaenge;
import de.immomio.openimmo.Anhang;
import de.immomio.openimmo.Befeuerung;
import de.immomio.openimmo.Boden;
import de.immomio.openimmo.Check;
import de.immomio.openimmo.Daten;
import de.immomio.openimmo.Energiepass;
import de.immomio.openimmo.Fahrstuhl;
import de.immomio.openimmo.Freitexte;
import de.immomio.openimmo.Geo;
import de.immomio.openimmo.Geokoordinaten;
import de.immomio.openimmo.Heizungsart;
import de.immomio.openimmo.Immobilie;
import de.immomio.openimmo.Kontaktperson;
import de.immomio.openimmo.Kueche;
import de.immomio.openimmo.Land;
import de.immomio.openimmo.Moebliert;
import de.immomio.openimmo.Nutzungsart;
import de.immomio.openimmo.Objektart;
import de.immomio.openimmo.Objektkategorie;
import de.immomio.openimmo.Openimmo;
import de.immomio.openimmo.Preise;
import de.immomio.openimmo.Stellplatz;
import de.immomio.openimmo.Stellplatzart;
import de.immomio.openimmo.StpSonstige;
import de.immomio.openimmo.Uebertragung;
import de.immomio.openimmo.Vermarktungsart;
import de.immomio.openimmo.VerwaltungObjekt;
import de.immomio.openimmo.VerwaltungTechn;
import de.immomio.openimmo.Zustand;
import de.immomio.openimmo.ZustandAngaben;
import de.immomio.openimmo.typen.OpenImmoCTypeTyp;
import de.immomio.openimmo.typen.OpenImmoDateiOrtTyp;
import de.immomio.openimmo.typen.OpenImmoDateiTyp;
import de.immomio.openimmo.typen.OpenImmoEpartTyp;
import de.immomio.openimmo.typen.OpenImmoJahrgangTyp;
import de.immomio.openimmo.typen.OpenImmoUmfangTyp;
import de.immomio.openimmo.typen.OpenImmoZustandArtTyp;
import de.immomio.util.DateUtil;
import de.immomio.util.PhraseAppKeyUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import static de.immomio.data.landlord.bean.property.certificate.EnergyCertificate.CertificateCreationDate.MAY_2014;
import static de.immomio.data.landlord.bean.property.certificate.EnergyCertificate.EnergyCertificateType.DEMAND_IDENTIFICATION;
import static de.immomio.openimmo.constants.OpenImmoConstants.EMPTY_STRING;
import static de.immomio.openimmo.constants.OpenImmoConstants.INTERESSENTENLINK;
import static de.immomio.openimmo.constants.OpenImmoConstants.KEINE_ANGABEN_VORHANDEN;
import static de.immomio.openimmo.constants.OpenImmoConstants.OPENIMMO_VERSION;
import static de.immomio.openimmo.constants.OpenImmoConstants.SENDERSOFTWARE;
import static de.immomio.openimmo.constants.OpenImmoConstants.SENDERVERSION;
import static de.immomio.openimmo.constants.OpenImmoConstants.TRANSMISSION;

public abstract class AbstractOpenimmoConverter {

    private final ApplicationMessageSource messageSource;

    protected AbstractOpenimmoConverter(ApplicationMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public Openimmo createOpenimmo(OpenImmoConverterData data) {
        Openimmo openimmo = new Openimmo();

        openimmo.setUebertragung(createUebertragung());
        openimmo.getAnbieter().add(createAnbieter(data));

        return openimmo;
    }

    protected Objektkategorie createObjektkategorie(OpenImmoConverterData data) {
        Objektkategorie objektkategorie = new Objektkategorie();

        objektkategorie.setObjektart(createObjektart(data));
        objektkategorie.setVermarktungsart(createVermarktungsart(data));
        objektkategorie.setNutzungsart(createNutzungsart(data));

        return objektkategorie;
    }

    protected Immobilie createImmobilie(OpenImmoConverterData data) {
        Immobilie immobilie = new Immobilie();

        immobilie.setObjektkategorie(createObjektkategorie(data));

        immobilie.setKontaktperson(createKontaktperson(data));
        immobilie.setGeo(createGeo(data));
        immobilie.setFreitexte(createFreitexte(data));
        immobilie.setVerwaltungObjekt(createVerwaltungObjekt(data));
        immobilie.setVerwaltungTechn(createVerwaltungTechn(data));
        immobilie.setAnhaenge(createAnhaenge(data));
        immobilie.setZustandAngaben(createZustandAngaben(data));

        return immobilie;
    }

    protected Kontaktperson createKontaktperson(OpenImmoConverterData data) {
        Kontaktperson kontaktperson = new Kontaktperson();

        kontaktperson.setEmailZentrale(data.getDefaultContactEmail());
        kontaktperson.setEmailDirekt(data.getDefaultContactEmail());
        kontaktperson.setFirma(data.getProperty().getCustomer().getName());

        Contact contact = data.getProperty().getData().getContact();
        LandlordUserProfile profile = data.getProperty().getUser().getProfile();

        if (contact != null && contact.getName() != null && contact.getFirstName() != null) {

            addNamesToKontaktpersonContent(kontaktperson, contact.getName(), contact.getFirstName());
        } else if (profile != null && profile.getName() != null && profile.getFirstname() != null) {
            addNamesToKontaktpersonContent(kontaktperson, profile.getName(), profile.getFirstname());
        } else {
            addNamesToKontaktpersonContent(kontaktperson, EMPTY_STRING, KEINE_ANGABEN_VORHANDEN);
        }

        return kontaktperson;
    }

    protected VerwaltungTechn createVerwaltungTechn(OpenImmoConverterData data) {
        VerwaltungTechn verwaltungTechn = new VerwaltungTechn();

        verwaltungTechn.setAktion(createAktion(data));
        verwaltungTechn.setObjektnrExtern(createExternalId(data));
        verwaltungTechn.setObjektnrIntern(data.getProperty().getId().toString());
        verwaltungTechn.setOpenimmoObid(data.getProperty().getId().toString());

        verwaltungTechn.setStandVom(DateUtil.instantToGregorianCalendar(data.getNow()));

        return verwaltungTechn;
    }

    protected Anbieter createAnbieter(OpenImmoConverterData data) {
        Anbieter anbieter = new Anbieter();

        anbieter.setOpenimmoAnid(OpenImmoConverterUtils.generateANID());
        anbieter.setAnbieternr(data.getSupplierNumber());
        anbieter.setFirma(data.getProperty().getCustomer().getName());
        anbieter.getImmobilie().add(createImmobilie(data));

        return anbieter;
    }

    protected Anhaenge createAnhaenge(OpenImmoConverterData data) {
        PropertyData propertyData = data.getProperty().getData();
        Anhaenge anhaenge = new Anhaenge();

        S3File titleImage = propertyData.getTitleImage();
        if (titleImage != null) {
            anhaenge.getAnhang().add(createAnhangFromS3File(titleImage, OpenImmoDateiTyp.TITELBILD, data));
        }

        if (propertyData.getAttachments() != null) {
            anhaenge.getAnhang()
                    .addAll(propertyData
                            .getAttachments()
                            .stream()
                            .filter(s3File -> s3File != null && (titleImage == null || (titleImage != null && !s3File.equals(titleImage))))
                            .map(attachment -> createAnhangFromS3File(attachment, OpenImmoDateiTyp.BILD, data))
                            .collect(Collectors.toList()));
        }

        if (propertyData.getDocuments() != null) {
            anhaenge.getAnhang().addAll(propertyData.getDocuments().stream().map(attachment -> {
                if (attachment.getType() == FileType.FLOOR_PLAN) {
                    return createAnhangFromS3File(attachment, OpenImmoDateiTyp.GRUNDRISS, data);
                }
                return createAnhangFromS3File(attachment, OpenImmoDateiTyp.DOKUMENTE, data);
            }).collect(Collectors.toList()));
        }

        anhaenge.getAnhang().add(createAnhang(data));

        return anhaenge;
    }

    protected abstract Objektart createObjektart(OpenImmoConverterData data);

    protected abstract Vermarktungsart createVermarktungsart(OpenImmoConverterData data);

    protected abstract Nutzungsart createNutzungsart(OpenImmoConverterData data);

    protected Anhang createAnhang(OpenImmoConverterData data) {
        Anhang anhang = new Anhang();

        anhang.setAnhangtitel(INTERESSENTENLINK);
        anhang.setLocation(OpenImmoDateiOrtTyp.REMOTE.name());
        anhang.setGruppe(OpenImmoDateiTyp.LINKS.name());

        anhang.setDaten(createDaten(data));

        return anhang;
    }

    protected Anhang createAnhangFromS3File(S3File attachment, OpenImmoDateiTyp openImmoDateiTyp,
            OpenImmoConverterData data) {
        Anhang anhang = new Anhang();

        anhang.setLocation(OpenImmoDateiOrtTyp.EXTERN.name());
        anhang.setFormat(attachment.getExtension());
        anhang.setGruppe(openImmoDateiTyp.name());

        anhang.setDaten(createDatenFromS3File(attachment));
        anhang.setCheck(createCheck(data));

        if (!StringUtils.isEmpty(attachment.getTitle())) {
            anhang.setAnhangtitel(attachment.getTitle());
        } else if (!StringUtils.isEmpty(attachment.getName())) {
            anhang.setAnhangtitel(attachment.getName());
        }

        return anhang;
    }

    protected Uebertragung createUebertragung() {
        Uebertragung uebertragung = new Uebertragung();

        uebertragung.setVersion(OPENIMMO_VERSION);
        uebertragung.setArt(TRANSMISSION);
        uebertragung.setSendersoftware(SENDERSOFTWARE);
        uebertragung.setSenderversion(SENDERVERSION);
        uebertragung.setUmfang(OpenImmoUmfangTyp.TEIL.key());

        return uebertragung;
    }

    protected Kueche createKueche(boolean isKitchenette) {
        Kueche kueche = new Kueche();

        kueche.setEBK(isKitchenette);

        return kueche;
    }

    protected Fahrstuhl createFahrstuhl(boolean isElevator) {
        Fahrstuhl fahrstuhl = new Fahrstuhl();

        fahrstuhl.setPERSONEN(isElevator);

        return fahrstuhl;
    }

    protected Moebliert createMoebliert(FurnishingType type) {
        Moebliert furnishing = new Moebliert();

        furnishing.setMoeb(OpenImmoConverterUtils.convertFurnishingType(type));

        return furnishing;
    }

    protected Zustand createZustand(BuildingConditionType buildingCondition) {
        Zustand zustand = new Zustand();

        if (buildingCondition == null) {
            return zustand;
        }

        OpenImmoZustandArtTyp zustandArtTyp;
        switch (buildingCondition) {
            case FIRST_TIME_USE:
                zustandArtTyp = OpenImmoZustandArtTyp.ERSTBEZUG;
                break;
            case FIRST_TIME_USE_AFTER_REFURBISHMENT:
                zustandArtTyp = OpenImmoZustandArtTyp.TEIL_VOLLRENOVIERT;
                break;
            case MINT_CONDITION:
                zustandArtTyp = OpenImmoZustandArtTyp.NEUWERTIG;
                break;
            case REFURBISHED:
                zustandArtTyp = OpenImmoZustandArtTyp.VOLL_SANIERT;
                break;
            case MODERNIZED:
                zustandArtTyp = OpenImmoZustandArtTyp.MODERNISIERT;
                break;
            case FULLY_RENOVATED:
                zustandArtTyp = OpenImmoZustandArtTyp.TEIL_VOLLRENOVIERT;
                break;
            case WELL_KEPT:
                zustandArtTyp = OpenImmoZustandArtTyp.GEPFLEGT;
                break;
            case NEED_OF_RENOVATION:
                zustandArtTyp = OpenImmoZustandArtTyp.TEIL_VOLLRENOVIERUNGSBED;
                break;
            case NEGOTIABLE:
                zustandArtTyp = OpenImmoZustandArtTyp.NACH_VEREINBARUNG;
                break;
            case RIPE_FOR_DEMOLITION:
                zustandArtTyp = OpenImmoZustandArtTyp.ABRISSOBJEKT;
                break;
            default:
                zustandArtTyp = null;
                break;
        }

        if (zustandArtTyp != null) {
            zustand.setZustandArt(zustandArtTyp.name());
        }

        return zustand;
    }

    protected Befeuerung createBefeuerung(HeaterType heater) {
        if (heater == null) {
            return null;
        }

        Befeuerung befeuerung = new Befeuerung();

        switch (heater) {
            case ALTERNATIVE:
                befeuerung.setALTERNATIV(true);
                break;
            case BLOCK:
                befeuerung.setBLOCK(true);
                break;
            case COAL:
                befeuerung.setKOHLE(true);
                break;
            case ELECTRO:
                befeuerung.setELEKTRO(true);
                break;
            case GAS:
                befeuerung.setGAS(true);
                break;
            case GROUND:
                befeuerung.setERDWAERME(true);
                break;
            case LIQUEFIED_GAS:
                befeuerung.setFLUESSIGGAS(true);
                break;
            case LONG_DISTANCE:
                befeuerung.setFERN(true);
                break;
            case OIL:
                befeuerung.setOEL(true);
                break;
            case PELLET:
                befeuerung.setPELLET(true);
                break;
            case WOOD:
                befeuerung.setHOLZ(true);
                break;
            case SOLAR:
                befeuerung.setSOLAR(true);
                break;
        }

        return befeuerung;
    }

    protected Boden convertGroundType(GroundType groundType) {
        Boden boden = new Boden();

        if (groundType == null) {
            return boden;
        }

        switch (groundType) {
            case TILE:
                boden.setFLIESEN(true);
                break;
            case STONE:
                boden.setSTEIN(true);
                break;
            case CARPET:
                boden.setTEPPICH(true);
                break;
            case PARQUET:
                boden.setPARKETT(true);
                break;
            case ENGINEERED:
                boden.setFERTIGPARKETT(true);
                break;
            case LAMINATE:
                boden.setLAMINAT(true);
                break;
            case BATTEN:
                boden.setDIELEN(true);
                break;
            case PLASTIC:
                boden.setKUNSTSTOFF(true);
                break;
            case ESTRICH:
                boden.setESTRICH(true);
                break;
            case DOUBLE_FLOOR:
                boden.setDOPPELBODEN(true);
                break;
            case LINOLEUM:
                boden.setLINOLEUM(true);
                break;
            case MARBLE:
                boden.setMARMOR(true);
                break;
            case TERRACOTTA:
                boden.setTERRAKOTTA(true);
                break;
            case GRANITE:
                boden.setGRANIT(true);
                break;
        }

        return boden;
    }

    protected Heizungsart createHeizungsart(HeaterFiringType heaterType) {
        if (heaterType == null) {
            return null;
        }

        Heizungsart heizungsart = new Heizungsart();

        switch (heaterType) {
            case CENTRAL:
                heizungsart.setZENTRAL(true);
                break;
            case ELECTRIC_HEATING:
                heizungsart.setOFEN(true);
                break;
            case FLOOR:
                heizungsart.setETAGE(true);
                break;
            case GROUND_FLOOR:
                heizungsart.setFUSSBODEN(true);
                break;
            case LONG_DISTANCE:
                heizungsart.setFERN(true);
                break;
            case OVEN:
                heizungsart.setOFEN(true);
                break;
        }

        return heizungsart;
    }

    protected List<Stellplatzart> createStellplatzarten(List<ParkingSpace> parkingSpaces) {
        if (parkingSpaces == null) {
            return null;
        }

        return parkingSpaces.stream()
                .map(this::createStellplatzart)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    protected Energiepass createEnergiepass(OpenImmoConverterData data) {
        EnergyCertificate energyCertificate = data.getProperty().getData().getEnergyCertificate();

        if (isInvalidEnergyCertificate(energyCertificate)) {
            return null;
        }

        Energiepass energiepass = new Energiepass();

        if (energyCertificate.getEnergyCertificateType() == EnergyCertificate.EnergyCertificateType.NO_AVAILABLE) {
            energiepass.setJahrgang(OpenImmoJahrgangTyp.OHNE.key());
            return energiepass;
        }

        energiepass.setBaujahr(String.valueOf(energyCertificate.getYearOfConstruction()));
        energiepass.setPrimaerenergietraeger(translateEnum(OpenImmoConverterUtils.convertPrimaerEnergieTraeger(energyCertificate.getPrimaryEnergyProvider())));
        energiepass.setJahrgang(OpenImmoConverterUtils.convertCreationDateType(energyCertificate.getCreationDate()).key());

        switch (energyCertificate.getEnergyCertificateType()) {
            case DEMAND_IDENTIFICATION:
                energiepass.setEpart(OpenImmoEpartTyp.BEDARF.name());
                energiepass.setEndenergiebedarf(energyCertificate.getDemandCertificate().getEndEnergyConsumption());
                if (energyCertificate.getCreationDate() == MAY_2014) {
                    energiepass.setWertklasse(energyCertificate.getDemandCertificate().getEnergyEfficiencyClass().getSymbol());
                }
                break;
            case USAGE_IDENTIFICATION:
                UsageCertificate usageCertificate = energyCertificate.getUsageCertificate();
                energiepass.setEpart(OpenImmoEpartTyp.VERBRAUCH.name());
                energiepass.setMitwarmwasser(usageCertificate.isIncludesHeatConsumption());
                energiepass.setEnergieverbrauchkennwert(usageCertificate.getEnergyConsumption());
                break;
        }

        return energiepass;
    }

    protected boolean isInvalidEnergyCertificate(EnergyCertificate energyCertificate) {
        return energyCertificate == null
                || energyCertificate.getCreationDate() == null
                || energyCertificate.getEnergyCertificateType() == null
                || (energyCertificate.getEnergyCertificateType() == DEMAND_IDENTIFICATION
                && energyCertificate.getDemandCertificate() == null)
                || (energyCertificate.getEnergyCertificateType()
                == EnergyCertificate.EnergyCertificateType.USAGE_IDENTIFICATION
                && energyCertificate.getUsageCertificate() == null);
    }

    protected Geo createGeo(OpenImmoConverterData data) {
        PropertyData propertyData = data.getProperty().getData();
        Address propertyAddress = propertyData.getAddress();

        Geo geo = new Geo();

        geo.setEtage(propertyData.getFloor());
        geo.setAnzahlEtagen(propertyData.getNumberOfFloors());
        geo.setPlz(propertyAddress.getZipCode());
        geo.setOrt(propertyAddress.getCity());
        geo.setStrasse(propertyAddress.getStreet());
        geo.setHausnummer(propertyAddress.getHouseNumber());

        geo.setLand(createLand(data.getCountryCode().name()));
        geo.setGeokoordinaten(createGeokoordinaten(propertyAddress.getCoordinates()));

        if (!StringUtils.isEmpty(propertyAddress.getRegion())) {
            geo.setBundesland(propertyAddress.getRegion());
        }

        return geo;
    }

    protected Freitexte createFreitexte(OpenImmoConverterData data) {
        PropertyData propertyData = data.getProperty().getData();

        Freitexte freitexte = new Freitexte();

        freitexte.setAusstattBeschr(propertyData.getFurnishingDescription());
        freitexte.setLage(propertyData.getObjectLocationText());
        freitexte.setObjekttitel(propertyData.getName());

        return freitexte;
    }

    protected void populateParkingPlace(Preise preise, ParkingSpace parkingSpace) {
        if (parkingSpace.getType() == null) {
            return;
        }

        Stellplatz parkingPlace = createParkingPlace(parkingSpace.getCount(), parkingSpace.getPrice());

        switch (parkingSpace.getType()) {
            case GARAGE:
                preise.setStpGarage(parkingPlace);
                break;
            case UNDERGROUND_CAR_PARK:
                preise.setStpTiefgarage(parkingPlace);
                break;
            case CARPORT:
                preise.setStpCarport(parkingPlace);
                break;
            case FREE_SPACE:
                preise.setStpFreiplatz(parkingPlace);
                break;
            case CAR_PARK:
                preise.setStpParkhaus(parkingPlace);
                break;
            case DUPLEX:
                preise.setStpDuplex(parkingPlace);
                break;
            default:
                StpSonstige other = createStpSonstige(parkingSpace);
                preise.getStpSonstige().add(other);
                break;
        }
    }

    protected ZustandAngaben createZustandAngaben(OpenImmoConverterData data) {
        ZustandAngaben zustandAngaben = new ZustandAngaben();

        zustandAngaben.setBaujahr(data.getProperty().getData().getConstructionYear());

        if (data.getProperty().getData().getLastRefurbishment() != null) {
            zustandAngaben.setLetztemodernisierung(data.getProperty().getData().getLastRefurbishment());
        }

        return zustandAngaben;
    }

    protected VerwaltungObjekt createVerwaltungObjekt(OpenImmoConverterData data) {
        VerwaltungObjekt verwaltungObjekt = new VerwaltungObjekt();

        verwaltungObjekt.setObjektadresseFreigeben(data.getProperty().getData().isShowAddress());

        AvailableFrom availableFrom = data.getProperty().getData().getAvailableFrom();
        if (availableFrom != null) {
            verwaltungObjekt.setVerfuegbarAb(availableFrom.dateAsString());
        }

        return verwaltungObjekt;
    }

    protected Stellplatz createParkingPlace(Integer count, Double price) {
        Stellplatz parkingPlace = new Stellplatz();

        parkingPlace.setAnzahl(count);
        parkingPlace.setStellplatzmiete(OpenImmoConverterUtils.convertNumberToBigDecimal(price, true));

        return parkingPlace;
    }

    private String createExternalId(OpenImmoConverterData data) {
        String externalId = data.getProperty().getExternalId();
        if (data.isUseExternalId() && !StringUtils.isEmpty(externalId)) {
            return externalId;
        } else {
            return data.getProperty().getId().toString();
        }
    }

    private void addNamesToKontaktpersonContent(Kontaktperson kontaktperson, String name, String firstName) {
        kontaktperson.setName(name);
        kontaktperson.setVorname(firstName);
    }

    private StpSonstige createStpSonstige(ParkingSpace parkingSpace) {
        StpSonstige other = new StpSonstige();

        other.setAnzahl(parkingSpace.getCount());
        other.setStellplatzmiete(OpenImmoConverterUtils.convertNumberToBigDecimal(parkingSpace.getPrice(), true));
        other.setPlatzart(parkingSpace.getType().name());

        return other;
    }

    private Stellplatzart createStellplatzart(ParkingSpace parkingSpace) {
        if (parkingSpace.getType() == null) {
            return null;
        }

        Stellplatzart stellplatzart = new Stellplatzart();
        switch (parkingSpace.getType()) {
            case GARAGE:
                stellplatzart.setGARAGE(true);
                break;
            case UNDERGROUND_CAR_PARK:
                stellplatzart.setTIEFGARAGE(true);
                break;
            case CARPORT:
                stellplatzart.setCARPORT(true);
                break;
            case FREE_SPACE:
                stellplatzart.setFREIPLATZ(true);
                break;
            case CAR_PARK:
                stellplatzart.setPARKHAUS(true);
                break;
            case DUPLEX:
                stellplatzart.setDUPLEX(true);
                break;
        }

        return stellplatzart;
    }


    private Geokoordinaten createGeokoordinaten(GeoCoordinates coordinates) {
        if (coordinates == null || !coordinates.isValid()) {
            return null;
        }

        Geokoordinaten geokoordinaten = new Geokoordinaten();

        geokoordinaten.setBreitengrad(coordinates.getLatitude().floatValue());
        geokoordinaten.setLaengengrad(coordinates.getLongitude().floatValue());

        return geokoordinaten;
    }

    private Land createLand(String countryName) {
        Land land = new Land();

        land.setIsoLand(countryName);

        return land;
    }

    private Aktion createAktion(OpenImmoConverterData data) {
        Aktion aktion = new Aktion();

        aktion.setAktionart(data.getAktion().name());

        return aktion;
    }

    private Daten createDaten(OpenImmoConverterData data) {
        Daten daten = new Daten();

        daten.setPfad(data.getApplicationLink().toString());

        return daten;
    }

    private Daten createDatenFromS3File(S3File attachment) {
        Daten daten = new Daten();

        daten.setPfad(AbstractS3.concatIdentifier(attachment.getType(), attachment.getIdentifier(),
                attachment.getExtension()));

        return daten;
    }

    private Check createCheck(OpenImmoConverterData data) {
        Check check = new Check();

        check.setCtype(OpenImmoCTypeTyp.DATETIME.name());
        check.setValue(DateUtil.instantToGregorianCalendar(data.getNow()));

        return check;
    }

    protected String translateEnum(Enum toTranslate) {
        return messageSource.resolveCodeString(PhraseAppKeyUtils.toKey(toTranslate), Locale.GERMAN);
    }
}
