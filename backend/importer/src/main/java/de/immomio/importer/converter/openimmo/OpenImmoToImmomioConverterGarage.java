package de.immomio.importer.converter.openimmo;

import de.immomio.constants.exceptions.OpenImmoToImmomioConverterException;
import de.immomio.data.base.type.property.ObjectType;
import de.immomio.data.landlord.bean.property.data.GarageData;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.openimmo.Flaechen;
import de.immomio.openimmo.Immobilie;
import de.immomio.openimmo.Objektart;
import de.immomio.openimmo.Preise;
import de.immomio.openimmo.VerwaltungObjekt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Fabian Beck
 */

@Slf4j
@Service
public class OpenImmoToImmomioConverterGarage extends AbstractOpenImmoToImmomioConvert {

    @Override
    public void addImmobilie(Property property, Immobilie immobilie) throws OpenImmoToImmomioConverterException {
        super.addImmobilie(property, immobilie);

        addPreis(property, immobilie.getPreise());
        addFlaechen(property, immobilie.getFlaechen());
    }

    @Override
    protected void addObjektart(Property property, Objektart objektart) throws OpenImmoToImmomioConverterException {
        PropertyData data = property.getData();

        if (objektart.getParken().size() == 1) {
            data.getGarageData()
                    .setGarageType(OpenImmoToImmomioConverterUtils.convertGarageType(
                            objektart.getParken().get(0).getParkenTyp()));
            data.setObjectType(ObjectType.GARAGE);
        } else {
            log.error("Objektart " + objektart + " for Garage missing");
            throw new OpenImmoToImmomioConverterException("Objektart " + objektart + " for Garage missing");
        }
    }

    @Override
    protected void addVerwaltungObjekt(Property property, VerwaltungObjekt verwaltungObjekt) {
        super.addVerwaltungObjekt(property, verwaltungObjekt);

        if (verwaltungObjekt == null) {
            return;
        }

        PropertyData data = property.getData();
        GarageData garageData = data.getGarageData();

        data.setAvailableFrom(convertStringToAvailableFrom(verwaltungObjekt, false));

        if (verwaltungObjekt.getBisdatum() != null) {
            garageData.setFreeUntil(verwaltungObjekt.getBisdatum().toGregorianCalendar().getTime());
        }
    }

    protected void addPreis(Property property, Preise preise) {
        if (preise == null) {
            return;
        }

        PropertyData data = property.getData();

        addBasePrice(preise, data);

        data.setBailment(OpenImmoToImmomioConverterUtils.bigDecimalToDouble(preise.getKaution()));
    }

    protected void addFlaechen(Property property, Flaechen flaechen) {
        if (flaechen == null) {
            return;
        }

        property.getData().setSize(OpenImmoToImmomioConverterUtils.bigDecimalToDouble(flaechen.getGesamtflaeche()));
    }

}
