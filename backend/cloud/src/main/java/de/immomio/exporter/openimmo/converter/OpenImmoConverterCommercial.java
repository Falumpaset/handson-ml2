package de.immomio.exporter.openimmo.converter;

import de.immomio.config.ApplicationMessageSource;
import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.base.type.property.OfficeType;
import de.immomio.data.landlord.bean.property.data.CommercialData;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.openimmo.Ausstattung;
import de.immomio.openimmo.BueroPraxen;
import de.immomio.openimmo.Distanzen;
import de.immomio.openimmo.Flaechen;
import de.immomio.openimmo.Freitexte;
import de.immomio.openimmo.Immobilie;
import de.immomio.openimmo.Infrastruktur;
import de.immomio.openimmo.Nutzungsart;
import de.immomio.openimmo.Objektart;
import de.immomio.openimmo.Preise;
import de.immomio.openimmo.Vermarktungsart;
import de.immomio.openimmo.ZustandAngaben;
import de.immomio.openimmo.typen.OpenImmoDistanzenTyp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Fabian Beck
 */

@Slf4j
@Service
public class OpenImmoConverterCommercial extends AbstractOpenimmoConverter {

    @Autowired
    public OpenImmoConverterCommercial(ApplicationMessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public Immobilie createImmobilie(OpenImmoConverterData data) {
        Immobilie immobilie = super.createImmobilie(data);

        immobilie.setVerwaltungObjekt(createVerwaltungObjekt(data));
        immobilie.setFlaechen(createFlaechen(data));
        immobilie.setAusstattung(createAusstattung(data));
        immobilie.setPreise(createPreise(data));
        immobilie.setInfrastruktur(createInfrastruktur(data));

        return immobilie;
    }

    @Override
    protected Objektart createObjektart(OpenImmoConverterData data) {
        CommercialData commercialData = data.getProperty().getData().getCommercialData();

        if (commercialData == null) {
            log.error("CommercialData was null");
            throw new IllegalArgumentException("CommercialData was null");
        }

        Objektart objektart = new Objektart();

        objektart.getBueroPraxen().add(createBueroPraxen(commercialData.getOfficeType()));

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

        nutzungsart.setGEWERBE(true);

        return nutzungsart;
    }

    @Override
    protected Freitexte createFreitexte(OpenImmoConverterData data) {
        Freitexte freitexte = super.createFreitexte(data);
        PropertyData propertyData = data.getProperty().getData();

        freitexte.setObjektbeschreibung(propertyData.getObjectDescription());
        freitexte.setSonstigeAngaben(propertyData.getObjectMiscellaneousText());

        return freitexte;
    }

    @Override
    protected ZustandAngaben createZustandAngaben(OpenImmoConverterData data) {
        ZustandAngaben zustandAngaben = super.createZustandAngaben(data);

        zustandAngaben.setZustand(createZustand(data.getProperty().getData().getBuildingCondition()));
        zustandAngaben.getEnergiepass().add(createEnergiepass(data));

        return zustandAngaben;
    }

    private Flaechen createFlaechen(OpenImmoConverterData data) {
        PropertyData propertyData = data.getProperty().getData();

        Flaechen flaechen = new Flaechen();

        flaechen.setBueroflaeche(OpenImmoConverterUtils.convertNumberToBigDecimal(propertyData.getSize()));
        flaechen.setKellerflaeche(OpenImmoConverterUtils.convertNumberToBigDecimal(propertyData.getBasementSize()));
        flaechen.setAnzahlStellplaetze(
                OpenImmoConverterUtils.convertNumberToBigDecimal(propertyData.getTotalParkingSpaces()));

        return flaechen;
    }

    private Ausstattung createAusstattung(OpenImmoConverterData data) {
        PropertyData propertyData = data.getProperty().getData();

        Ausstattung ausstattung = new Ausstattung();

        ausstattung.setFahrstuhl(createFahrstuhl(propertyData.isElevator()));
        ausstattung.setBarrierefrei(propertyData.isBarrierFree());
        ausstattung.setBoden(convertGroundType(propertyData.getGround()));
        ausstattung.setKantineCafeteria(propertyData.getCommercialData().isHasCanteen());
        ausstattung.setKueche(createKueche(propertyData.isKitchenette()));
        ausstattung.setKlimatisiert(propertyData.getCommercialData().isAirConditioning());
        ausstattung.setRollstuhlgerecht(propertyData.isWheelchairAccessible());

        return ausstattung;
    }

    private Preise createPreise(OpenImmoConverterData data) {
        PropertyData propertyData = data.getProperty().getData();

        Preise preise = new Preise();

        preise.setNebenkosten(OpenImmoConverterUtils.convertNumberToBigDecimal(propertyData.getServiceCharge(), true));

        Double pricePerSquareMeter = propertyData.getCommercialData().getPricePerSquareMeter();
        if (pricePerSquareMeter != null && pricePerSquareMeter > 0) {
            preise.setMietpreisProQm(OpenImmoConverterUtils.convertNumberToBigDecimal(pricePerSquareMeter, true));
        } else {
            preise.setKaltmiete(OpenImmoConverterUtils.convertNumberToBigDecimal(propertyData.getBasePrice(), true));
        }

        if (propertyData.getParkingSpaces() != null && !propertyData.isParkingPriceIncludedToAdditionalCosts()) {
            if (data.getPortal() == Portal.EBAY) {
                preise.setStpGarage(
                        createParkingPlace(propertyData.getTotalParkingSpaces(), propertyData.getTotalParkingPrice()));
            } else {
                propertyData.getParkingSpaces().forEach(parkingSpace -> populateParkingPlace(preise, parkingSpace));
            }

        }

        preise.setGesamtmietebrutto(
                OpenImmoConverterUtils.convertNumberToBigDecimal(propertyData.getTotalRentGross(), true));
        preise.setKaution(OpenImmoConverterUtils.convertNumberToBigDecimal(propertyData.getBailment(), true));

        return preise;
    }

    private Infrastruktur createInfrastruktur(OpenImmoConverterData data) {
        CommercialData commercialData = data.getProperty().getData().getCommercialData();

        Infrastruktur infrastruktur = new Infrastruktur();

        addDistanzen(infrastruktur, OpenImmoDistanzenTyp.FERNBAHNHOF, commercialData.getDistanceToTrainStation());
        addDistanzen(infrastruktur, OpenImmoDistanzenTyp.AUTOBAHN, commercialData.getDistanceToHighway());
        addDistanzen(infrastruktur, OpenImmoDistanzenTyp.FLUGHAFEN, commercialData.getDistanceToAirport());
        addDistanzen(infrastruktur, OpenImmoDistanzenTyp.US_BAHN, commercialData.getDistanceToPublicTransport());

        return infrastruktur;
    }

    private void addDistanzen(Infrastruktur infrastruktur, OpenImmoDistanzenTyp type, Double distance) {
        if (distance != null) {
            infrastruktur.getDistanzen().add(createDistanzen(type, distance.floatValue()));
        }
    }

    private Distanzen createDistanzen(OpenImmoDistanzenTyp type, float distance) {
        Distanzen distanzen = new Distanzen();

        distanzen.setDistanzZu(type.name());
        distanzen.setValue(distance);

        return distanzen;
    }

    private BueroPraxen createBueroPraxen(OfficeType type) {
        BueroPraxen bueroPraxen = new BueroPraxen();

        bueroPraxen.setBueroTyp(OpenImmoConverterUtils.convertBueroTyp(type));

        return bueroPraxen;
    }
}
