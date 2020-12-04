package de.immomio.exporter.openimmo.converter;

import de.immomio.config.ApplicationMessageSource;
import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.base.type.property.FlatType;
import de.immomio.data.base.type.property.HouseType;
import de.immomio.data.landlord.bean.property.BathroomEquipment;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.openimmo.Ausstattung;
import de.immomio.openimmo.Bad;
import de.immomio.openimmo.Flaechen;
import de.immomio.openimmo.Freitexte;
import de.immomio.openimmo.Haus;
import de.immomio.openimmo.Immobilie;
import de.immomio.openimmo.Nutzungsart;
import de.immomio.openimmo.Objektart;
import de.immomio.openimmo.Preise;
import de.immomio.openimmo.Stellplatzart;
import de.immomio.openimmo.Unterkellert;
import de.immomio.openimmo.Vermarktungsart;
import de.immomio.openimmo.VerwaltungObjekt;
import de.immomio.openimmo.Wohnung;
import de.immomio.openimmo.ZustandAngaben;
import de.immomio.openimmo.typen.OpenImmoKellerTyp;
import de.immomio.util.DateUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static de.immomio.data.base.type.property.ObjectType.HOUSE;
import static de.immomio.openimmo.constants.OpenImmoConstants.KEINE;

/**
 * @author Fabian Beck
 */

@Service
public class OpenImmoConverterFlat extends AbstractOpenimmoConverter {

    @Autowired
    public OpenImmoConverterFlat(ApplicationMessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public Immobilie createImmobilie(OpenImmoConverterData data) {
        Immobilie immobilie = super.createImmobilie(data);

        immobilie.setFlaechen(createFlaechen(data));
        immobilie.setAusstattung(createAusstattung(data));
        immobilie.setPreise(createPreise(data));

        return immobilie;
    }

    @Override
    protected Objektart createObjektart(OpenImmoConverterData data) {
        PropertyData propertyData = data.getProperty().getData();

        if (propertyData.getObjectType() == null) {
            return null;
        }

        Objektart objektart = new Objektart();

        if (propertyData.getObjectType() == HOUSE) {
            objektart.getHaus().add(createHaus(propertyData.getHouseType()));
        } else {
            objektart.getWohnung().add(createWohnung(propertyData.getFlatType()));
        }

        return objektart;
    }

    @Override
    protected Vermarktungsart createVermarktungsart(OpenImmoConverterData data) {
        Vermarktungsart vermarktungsart = new Vermarktungsart();

        vermarktungsart.setMIETEPACHT(true);

        return vermarktungsart;
    }

    @Override
    protected Nutzungsart createNutzungsart(OpenImmoConverterData data) {
        Nutzungsart nutzungsart = new Nutzungsart();

        if (data.getProperty().getData().isShortTermAccommodation()) {
            nutzungsart.setWAZ(true);
        } else {
            nutzungsart.setWOHNEN(true);
        }

        return nutzungsart;
    }

    @Override
    protected Freitexte createFreitexte(OpenImmoConverterData data) {
        Freitexte freitexte = super.createFreitexte(data);
        PropertyData propertyData = data.getProperty().getData();

        if (data.getShortDescription() != null) {
            freitexte.setObjektbeschreibung(data.getShortDescription());
        } else {
            freitexte.setObjektbeschreibung(propertyData.getObjectDescription());
        }
        if (data.getMiscellaneousText() != null) {
            freitexte.setSonstigeAngaben(data.getMiscellaneousText());
        } else {
            freitexte.setSonstigeAngaben(propertyData.getObjectMiscellaneousText());
        }

        return freitexte;
    }

    @Override
    protected ZustandAngaben createZustandAngaben(OpenImmoConverterData data) {
        ZustandAngaben zustandAngaben = super.createZustandAngaben(data);

        zustandAngaben.setZustand(createZustand(data.getProperty().getData().getBuildingCondition()));
        zustandAngaben.getEnergiepass().add(createEnergiepass(data));

        return zustandAngaben;
    }

    @Override
    protected VerwaltungObjekt createVerwaltungObjekt(OpenImmoConverterData data) {
        VerwaltungObjekt verwaltungObjekt = super.createVerwaltungObjekt(data);

        PropertyData propertyData = data.getProperty().getData();

        verwaltungObjekt.setDenkmalgeschuetzt(propertyData.isHistoricBuilding());
        if (data.getProperty().getPrioset() != null && data.getProperty().getPrioset().getData() != null) {
            verwaltungObjekt.setWbsSozialwohnung(data.getProperty().getPrioset().getData().getWbs());
        }

        if (propertyData.getTemporaryLiving() != null) {
            verwaltungObjekt.setAbdatum(DateUtil.dateToGregorianCalendar(propertyData.getTemporaryLiving().getStart()));
            verwaltungObjekt.setBisdatum(DateUtil.dateToGregorianCalendar(propertyData.getTemporaryLiving().getEnd()));
        }

        return verwaltungObjekt;
    }

    private Flaechen createFlaechen(OpenImmoConverterData data) {
        PropertyData propertyData = data.getProperty().getData();

        Flaechen flaechen = new Flaechen();

        flaechen.setAnzahlBadezimmer(OpenImmoConverterUtils.convertNumberToBigDecimal(propertyData.getBathRooms()));
        flaechen.setAnzahlBalkone(
                OpenImmoConverterUtils.convertNumberToBigDecimal(propertyData.getNumberOfBalconies()));
        flaechen.setAnzahlLogia(OpenImmoConverterUtils.convertNumberToBigDecimal(propertyData.getNumberOfLoggias()));
        flaechen.setAnzahlZimmer(OpenImmoConverterUtils.convertNumberToBigDecimal(propertyData.getRooms()));
        flaechen.setAnzahlTerrassen(
                OpenImmoConverterUtils.convertNumberToBigDecimal(propertyData.getNumberOfTerraces()));
        flaechen.setBalkonTerrasseFlaeche(
                OpenImmoConverterUtils.convertNumberToBigDecimal(propertyData.getBalconyTerraceArea()));
        flaechen.setAnzahlSchlafzimmer(
                OpenImmoConverterUtils.convertNumberToBigDecimal(propertyData.getNumberOfBedrooms()));
        flaechen.setGartenflaeche(OpenImmoConverterUtils.convertNumberToBigDecimal(propertyData.getGardenArea()));
        flaechen.setGrundstuecksflaeche(OpenImmoConverterUtils.convertNumberToBigDecimal(propertyData.getLandArea()));
        flaechen.setKellerflaeche(OpenImmoConverterUtils.convertNumberToBigDecimal(propertyData.getBasementSize()));
        flaechen.setWohnflaeche(OpenImmoConverterUtils.convertNumberToBigDecimal(propertyData.getSize()));
        flaechen.setAnzahlStellplaetze(
                OpenImmoConverterUtils.convertNumberToBigDecimal(propertyData.getTotalParkingSpaces()));

        return flaechen;
    }

    private Ausstattung createAusstattung(OpenImmoConverterData data) {
        PropertyData propertyData = data.getProperty().getData();

        Ausstattung ausstattung = new Ausstattung();

        ausstattung.setBarrierefrei(propertyData.isBarrierFree());
        ausstattung.setGaestewc(propertyData.isGuestToilette());
        ausstattung.setWgGeeignet(propertyData.isFlatShare());
        ausstattung.setKamin(propertyData.isFireplace());
        ausstattung.setGartennutzung(propertyData.isGardenUse());
        ausstattung.setWaschTrockenraum(propertyData.isWashDryRoom());
        ausstattung.setAbstellraum(propertyData.isStoreRoom());
        ausstattung.setFahrradraum(propertyData.isBicycleRoom());
        ausstattung.setDachboden(propertyData.isAttic());
        ausstattung.setSeniorengerecht(propertyData.isSeniors());
        ausstattung.setKabelSatTv(propertyData.isTvSatCable());

        ausstattung.setHeizungsart(createHeizungsart(propertyData.getHeater()));
        ausstattung.setFahrstuhl(createFahrstuhl(propertyData.isElevator()));
        ausstattung.setKueche(createKueche(propertyData.isKitchenette()));
        ausstattung.setBoden(convertGroundType(propertyData.getGround()));
        ausstattung.setMoebliert(createMoebliert(propertyData.getFurnishingType()));
        ausstattung.setRolladen(propertyData.isShutter());
        ausstattung.setBad(createBad(data));
        ausstattung.setRollstuhlgerecht(propertyData.isWheelchairAccessible());
        ausstattung.setUnterkellert(createUnterkellert(data));

        List<Stellplatzart> stellplatzarten = createStellplatzarten(propertyData.getParkingSpaces());
        if (stellplatzarten != null) {
            ausstattung.getStellplatzart().addAll(stellplatzarten);
        }

        if (propertyData.getEnergyCertificate() != null) {
            ausstattung.setBefeuerung(createBefeuerung(propertyData.getEnergyCertificate().getPrimaryEnergyProvider()));
        }

        return ausstattung;
    }

    private Preise createPreise(OpenImmoConverterData data) {
        PropertyData propertyData = data.getProperty().getData();

        Preise preise = new Preise();

        preise.setNebenkosten(OpenImmoConverterUtils.convertNumberToBigDecimal(propertyData.getServiceCharge(), true));
        preise.setKaltmiete(OpenImmoConverterUtils.convertNumberToBigDecimal(propertyData.getBasePrice(), true));

        if (propertyData.getParkingSpaces() != null && !propertyData.isParkingPriceIncludedToAdditionalCosts()) {
            if (data.getPortal() == Portal.EBAY) {
                preise.setStpGarage(
                        createParkingPlace(propertyData.getTotalParkingSpaces(), propertyData.getTotalParkingPrice()));
            } else {
                propertyData.getParkingSpaces().forEach(parkingSpace -> populateParkingPlace(preise, parkingSpace));
            }

        }

        preise.setHeizkostenEnthalten(propertyData.isHeatingCostIncluded());
        if (!propertyData.isHeatingCostIncluded()) {
            preise.setHeizkosten(OpenImmoConverterUtils.convertNumberToBigDecimal(propertyData.getHeatingCost(), true));
        }

        preise.setGesamtmietebrutto(
                OpenImmoConverterUtils.convertNumberToBigDecimal(propertyData.getTotalRentGross(), true));
        preise.setKaution(OpenImmoConverterUtils.convertNumberToBigDecimal(propertyData.getBailment(), true));

        preise.setCourtageHinweis(KEINE); // TODO Warum ist das hier STD?

        return preise;
    }

    private Wohnung createWohnung(FlatType flatType) {
        Wohnung wohnung = new Wohnung();

        wohnung.setWohnungtyp(OpenImmoConverterUtils.convertFlatType(flatType).key());

        return wohnung;
    }

    private Haus createHaus(HouseType houseType) {
        Haus haus = new Haus();

        haus.setHaustyp(OpenImmoConverterUtils.convertHouseType(houseType).name());

        return haus;
    }

    private Bad createBad(OpenImmoConverterData data) {
        BathroomEquipment bathroomEquipment = data.getProperty().getData().getBathroomEquipment();

        if (bathroomEquipment == null) {
            return null;
        }

        Bad bad = new Bad();
        bad.setDUSCHE(bathroomEquipment.getShower());
        bad.setWANNE(bathroomEquipment.getBathtub());
        bad.setFENSTER(bathroomEquipment.getWindow());
        bad.setBIDET(bathroomEquipment.getBidet());
        bad.setPISSOIR(bathroomEquipment.getUrinal());

        return bad;
    }

    private Unterkellert createUnterkellert(OpenImmoConverterData data) {
        Unterkellert unterkellert = new Unterkellert();

        Boolean basementAvailable = data.getProperty().getData().getBasementAvailable();
        OpenImmoKellerTyp kellerTyp = BooleanUtils.isTrue(basementAvailable) ? OpenImmoKellerTyp.JA : OpenImmoKellerTyp.NEIN;
        unterkellert.setKeller(kellerTyp.name());

        return unterkellert;
    }
}
