package de.immomio.importer.converter.openimmo;

import de.immomio.constants.exceptions.OpenImmoToImmomioConverterException;
import de.immomio.data.base.type.property.ObjectType;
import de.immomio.data.landlord.bean.property.data.CommercialData;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.openimmo.Ausstattung;
import de.immomio.openimmo.Distanzen;
import de.immomio.openimmo.Flaechen;
import de.immomio.openimmo.Immobilie;
import de.immomio.openimmo.Infrastruktur;
import de.immomio.openimmo.Objektart;
import de.immomio.openimmo.Preise;
import de.immomio.openimmo.VerwaltungObjekt;
import de.immomio.openimmo.ZustandAngaben;
import de.immomio.openimmo.typen.OpenImmoDistanzenTyp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author Fabian Beck
 */

@Slf4j
@Service
public class OpenImmoToImmomioConverterCommercial extends AbstractOpenImmoToImmomioConvert {

    private final OpenImmoToImmomioConverterEnergyCertificate converterEnergyCertificate;

    @Autowired
    public OpenImmoToImmomioConverterCommercial(
            OpenImmoToImmomioConverterEnergyCertificate converterEnergyCertificate) {
        this.converterEnergyCertificate = converterEnergyCertificate;
    }

    @Override
    public void addImmobilie(Property property, Immobilie immobilie) throws OpenImmoToImmomioConverterException {
        super.addImmobilie(property, immobilie);

        addAusstattung(property, immobilie.getAusstattung());
        addPreis(property, immobilie.getPreise());
        addFlaechen(property, immobilie.getFlaechen());
        addInfrastruktur(property, immobilie.getInfrastruktur());

        converterEnergyCertificate.addEnergyCertificate(property, immobilie);
    }

    @Override
    protected void addObjektart(Property property, Objektart objektart) throws OpenImmoToImmomioConverterException {
        PropertyData data = property.getData();

        if (objektart.getBueroPraxen().size() == 1) {
            data.getCommercialData()
                    .setOfficeType(OpenImmoToImmomioConverterUtils.convertCommercialType(
                            objektart.getBueroPraxen().get(0).getBueroTyp()));
            data.setObjectType(ObjectType.OFFICE);
        } else {
            log.error("Objektart " + objektart + " for Commercial missing");
            throw new OpenImmoToImmomioConverterException("Objektart " + objektart + " for Commercial missing");
        }
    }

    @Override
    protected void addZustandAngaben(Property property, ZustandAngaben zustandAngaben) {
        super.addZustandAngaben(property, zustandAngaben);

        if (zustandAngaben == null) {
            return;
        }

        PropertyData data = property.getData();

        if (zustandAngaben.getLetztemodernisierung() != null && !zustandAngaben.getLetztemodernisierung().isEmpty()) {
            data.setLastRefurbishment(zustandAngaben.getLetztemodernisierung());
        }
    }

    protected void addVerwaltungObjekt(Property property, VerwaltungObjekt verwaltungObjekt) {
        super.addVerwaltungObjekt(property, verwaltungObjekt);

        if (verwaltungObjekt == null) {
            return;
        }

        PropertyData data = property.getData();

        data.setAvailableFrom(convertStringToAvailableFrom(verwaltungObjekt, true));
    }

    protected void addAusstattung(Property property, Ausstattung ausstattung) {
        if (ausstattung == null) {
            return;
        }

        PropertyData data = property.getData();

        data.setElevator(OpenImmoToImmomioConverterUtils.convertElevator(ausstattung.getFahrstuhl()));
        data.setBarrierFree(OpenImmoToImmomioConverterUtils.booleanToBoolean(ausstattung.isBarrierefrei()));
        data.setGround(OpenImmoToImmomioConverterUtils.convertBoden(ausstattung.getBoden()));

        addKueche(property, ausstattung.getKueche());

        CommercialData commercialData = data.getCommercialData();

        commercialData.setHasCanteen(
                OpenImmoToImmomioConverterUtils.booleanToBoolean(ausstattung.isKantineCafeteria()));
        commercialData.setAirConditioning(
                OpenImmoToImmomioConverterUtils.booleanToBoolean(ausstattung.isKlimatisiert()));
    }

    protected void addPreis(Property property, Preise preise) {
        if (preise == null) {
            return;
        }

        PropertyData data = property.getData();

        if (preise.getKaution() != null && preise.getKaution().compareTo(BigDecimal.ZERO) >= 0) {
            data.setBailment(OpenImmoToImmomioConverterUtils.bigDecimalToDouble(preise.getKaution()));
        }

        if (preise.getNebenkosten() != null && preise.getNebenkosten().compareTo(BigDecimal.ZERO) >= 0) {
            data.setServiceCharge(OpenImmoToImmomioConverterUtils.bigDecimalToDouble(preise.getNebenkosten()));
        }

        addBasePrice(preise, data);

        if (preise.getMietpreisProQm() != null && preise.getMietpreisProQm().compareTo(BigDecimal.ZERO) > 0) {
            data.getCommercialData().setPricePerSquareMeter(OpenImmoToImmomioConverterUtils.bigDecimalToDouble(preise.getMietpreisProQm()));
        }

        addStellplaetze(property, preise);
    }

    protected void addFlaechen(Property property, Flaechen flaechen) {
        if (flaechen == null) {
            return;
        }

        PropertyData data = property.getData();
        if (flaechen.getBueroflaeche() != null && flaechen.getBueroflaeche().compareTo(BigDecimal.ZERO) >= 0) {
            data.
                    setSize(OpenImmoToImmomioConverterUtils.bigDecimalToDouble(flaechen.getBueroflaeche()));
        }

        if (flaechen.getKellerflaeche() != null && flaechen.getKellerflaeche().compareTo(BigDecimal.ZERO) >= 0) {
            data.setBasementAvailable(true);
            data.setBasementSize(OpenImmoToImmomioConverterUtils.bigDecimalToDouble(flaechen.getKellerflaeche()));
        }
    }

    private void addInfrastruktur(Property property, Infrastruktur infrastruktur) {
        CommercialData commercialData = property.getData().getCommercialData();

        if (infrastruktur != null && infrastruktur.getDistanzen() != null) {
            infrastruktur.getDistanzen().forEach(distanz -> addDistanzen(commercialData, distanz));
        }
    }

    private void addDistanzen(CommercialData commercialData, Distanzen distanzen) {
        Double value = (double) distanzen.getValue();
        switch (EnumUtils.getEnumIgnoreCase(OpenImmoDistanzenTyp.class, distanzen.getDistanzZu())) {
            case FERNBAHNHOF:
                commercialData.setDistanceToTrainStation(value);
                break;
            case AUTOBAHN:
                commercialData.setDistanceToHighway(value);
                break;
            case FLUGHAFEN:
                commercialData.setDistanceToAirport(value);
                break;
            case US_BAHN:
                commercialData.setDistanceToPublicTransport(value);
                break;
        }
    }
}
