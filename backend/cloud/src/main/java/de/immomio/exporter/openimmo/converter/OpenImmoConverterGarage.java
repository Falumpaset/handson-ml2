package de.immomio.exporter.openimmo.converter;

import de.immomio.config.ApplicationMessageSource;
import de.immomio.data.base.type.property.GarageType;
import de.immomio.data.landlord.bean.property.data.GarageData;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.openimmo.Flaechen;
import de.immomio.openimmo.Freitexte;
import de.immomio.openimmo.Immobilie;
import de.immomio.openimmo.Nutzungsart;
import de.immomio.openimmo.Objektart;
import de.immomio.openimmo.Parken;
import de.immomio.openimmo.Preise;
import de.immomio.openimmo.Vermarktungsart;
import de.immomio.openimmo.VerwaltungObjekt;
import de.immomio.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Fabian Beck
 */

@Slf4j
@Service
public class OpenImmoConverterGarage extends AbstractOpenimmoConverter {

    @Autowired
    public OpenImmoConverterGarage(ApplicationMessageSource messageSource) {
        super(messageSource);
    }

    @Override
    public Immobilie createImmobilie(OpenImmoConverterData data) {
        Immobilie immobilie = super.createImmobilie(data);

        immobilie.setVerwaltungObjekt(createVerwaltungObjekt(data));
        immobilie.setFlaechen(createFlaechen(data));
        immobilie.setPreise(createPreise(data));

        return immobilie;
    }

    @Override
    protected Objektart createObjektart(OpenImmoConverterData data) {
        GarageData garageData = data.getProperty().getData().getGarageData();

        if (garageData == null) {
            log.error("GarageData was null");
            throw new IllegalArgumentException("GarageData was null");
        }

        Objektart objektart = new Objektart();

        objektart.getParken().add(createParken(garageData.getGarageType()));

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

        nutzungsart.setANLAGE(true);

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
    protected VerwaltungObjekt createVerwaltungObjekt(OpenImmoConverterData data) {
        VerwaltungObjekt verwaltungObjekt = super.createVerwaltungObjekt(data);
        GarageData garageData = data.getProperty().getData().getGarageData();

        verwaltungObjekt.setBisdatum(DateUtil.dateToGregorianCalendar(garageData.getFreeUntil()));

        return verwaltungObjekt;
    }

    private Flaechen createFlaechen(OpenImmoConverterData data) {
        PropertyData propertyData = data.getProperty().getData();

        Flaechen flaechen = new Flaechen();

        flaechen.setNutzflaeche(OpenImmoConverterUtils.convertNumberToBigDecimal(propertyData.getSize()));

        return flaechen;
    }

    private Preise createPreise(OpenImmoConverterData data) {
        PropertyData propertyData = data.getProperty().getData();

        Preise preise = new Preise();

        preise.setKaltmiete(OpenImmoConverterUtils.convertNumberToBigDecimal(propertyData.getBasePrice(), true));
        preise.setGesamtmietebrutto(
                OpenImmoConverterUtils.convertNumberToBigDecimal(propertyData.getTotalRentGross(), true));

        preise.setKaution(OpenImmoConverterUtils.convertNumberToBigDecimal(propertyData.getBailment(), true));

        return preise;
    }

    private Parken createParken(GarageType type) {
        Parken parken = new Parken();

        parken.setParkenTyp(OpenImmoConverterUtils.convertGarageType(type));

        return parken;
    }
}
