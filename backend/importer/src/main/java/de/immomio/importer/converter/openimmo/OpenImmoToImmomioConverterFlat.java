package de.immomio.importer.converter.openimmo;

import de.immomio.constants.exceptions.OpenImmoToImmomioConverterException;
import de.immomio.data.base.type.property.ObjectType;
import de.immomio.data.landlord.bean.property.BathroomEquipment;
import de.immomio.data.landlord.bean.property.PropertyPreviousTenantBean;
import de.immomio.data.landlord.bean.property.TemporaryLiving;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.openimmo.Ausstattung;
import de.immomio.openimmo.Bad;
import de.immomio.openimmo.Flaechen;
import de.immomio.openimmo.Immobilie;
import de.immomio.openimmo.Objektart;
import de.immomio.openimmo.Preise;
import de.immomio.openimmo.UserDefinedSimplefield;
import de.immomio.openimmo.VerwaltungObjekt;
import de.immomio.openimmo.ZustandAngaben;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static de.immomio.importer.converter.openimmo.OpenImmoToImmomioConverterUtils.bigDecimalToDouble;
import static de.immomio.importer.converter.openimmo.OpenImmoToImmomioConverterUtils.bigDecimalToInt;
import static de.immomio.importer.converter.openimmo.OpenImmoToImmomioConverterUtils.booleanToBoolean;
import static de.immomio.importer.converter.openimmo.OpenImmoToImmomioConverterUtils.convertBoden;
import static de.immomio.importer.converter.openimmo.OpenImmoToImmomioConverterUtils.convertElevator;
import static de.immomio.importer.converter.openimmo.OpenImmoToImmomioConverterUtils.convertFlatType;
import static de.immomio.importer.converter.openimmo.OpenImmoToImmomioConverterUtils.convertFurnishingType;
import static de.immomio.importer.converter.openimmo.OpenImmoToImmomioConverterUtils.convertHeaterType;
import static de.immomio.importer.converter.openimmo.OpenImmoToImmomioConverterUtils.convertHouseType;
import static de.immomio.importer.converter.openimmo.OpenImmoToImmomioConverterUtils.convertIntercomType;
import static de.immomio.importer.converter.openimmo.OpenImmoToImmomioConverterUtils.getValueFromSimpleField;

/**
 * @author Fabian Beck
 */

@Slf4j
@Service
public class OpenImmoToImmomioConverterFlat extends AbstractOpenImmoToImmomioConvert {

    private static final String WASCHMASCHINENANSCHLUSS = "WASCHMASCHINENANSCHLUSS";
    private static final String GEGENSPRECHANLAGE = "GEGENSPRECHANLAGE";

    private static final String SIMPLE_FIELDS_VORMIETER_VORNAME = "vormieter_vorname";
    private static final String SIMPLE_FIELDS_VORMIETER_NACHNAME = "vormieter_nachname";
    private static final String SIMPLE_FIELDS_VORMIETER_EMAIL = "vormieter_email";
    private static final String SIMPLE_FIELDS_VORMIETER_TELEFON = "vormieter_telefon";
    private static final Set<String> PREVIOUS_TENANT_FIELDS = Set.of(SIMPLE_FIELDS_VORMIETER_VORNAME,
            SIMPLE_FIELDS_VORMIETER_NACHNAME, SIMPLE_FIELDS_VORMIETER_EMAIL, SIMPLE_FIELDS_VORMIETER_TELEFON);

    private final OpenImmoToImmomioConverterEnergyCertificate converterEnergyCertificate;

    @Autowired
    public OpenImmoToImmomioConverterFlat(OpenImmoToImmomioConverterEnergyCertificate converterEnergyCertificate) {
        this.converterEnergyCertificate = converterEnergyCertificate;
    }

    @Override
    public void addImmobilie(Property property, Immobilie immobilie) throws OpenImmoToImmomioConverterException {
        super.addImmobilie(property, immobilie);

        converterEnergyCertificate.addEnergyCertificate(property, immobilie);

        addAusstattung(property, immobilie.getAusstattung());
        addPreis(property, immobilie.getPreise());
        addFlaechen(property, immobilie.getFlaechen());
        addUserDefinedSimplefields(property, immobilie.getUserDefinedSimplefield());

        addShortTermAccommodation(property, immobilie);
    }

    @Override
    protected void addObjektart(Property property, Objektart objektart) {
        PropertyData data = property.getData();

        if (objektart.getWohnung().size() == 1) {
            data.setFlatType(
                    convertFlatType(objektart.getWohnung().get(0).getWohnungtyp()));
            data.setObjectType(ObjectType.FLAT);
        } else if (objektart.getHaus().size() == 1) {
            data.setHouseType(
                    convertHouseType(objektart.getHaus().get(0).getHaustyp()));
            data.setObjectType(ObjectType.HOUSE);
        } else {
            log.warn("Objektart " + objektart + " for Flat missing");
            data.setObjectType(ObjectType.FLAT);
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
        data.setHistoricBuilding(
                booleanToBoolean(verwaltungObjekt.isDenkmalgeschuetzt()));
    }

    protected void addUserDefinedSimplefields(Property property, List<UserDefinedSimplefield> simplefields) {
        if(containsPreviousTenantFields(simplefields)) {
            property.getData().setPreviousTenant(createPreviousTenantFields(simplefields));
        }
    }

    protected void addShortTermAccommodation(Property property, Immobilie immobilie) {
        property.getData().setTemporaryLiving(createTemporaryLiving(immobilie.getVerwaltungObjekt()));
    }

    protected TemporaryLiving createTemporaryLiving(VerwaltungObjekt verwaltungObjekt) {
        if (verwaltungObjekt == null) {
            return null;
        }

        if (verwaltungObjekt.getAbdatum() == null || verwaltungObjekt.getBisdatum() == null) {
            return null;
        }

        TemporaryLiving temporaryLiving = new TemporaryLiving();
        temporaryLiving.setStart(verwaltungObjekt.getAbdatum().toGregorianCalendar().getTime());
        temporaryLiving.setEnd(verwaltungObjekt.getBisdatum().toGregorianCalendar().getTime());
        return temporaryLiving;
    }

    protected void addFlaechen(Property property, Flaechen flaechen) {
        if (flaechen == null) {
            return;
        }

        PropertyData data = property.getData();
        if (flaechen.getAnzahlBadezimmer() != null && flaechen.getAnzahlBadezimmer().compareTo(BigDecimal.ZERO) >= 0) {
            data.setBathRooms(bigDecimalToInt(flaechen.getAnzahlBadezimmer()));
        }

        if (flaechen.getAnzahlZimmer() != null && flaechen.getAnzahlZimmer().compareTo(BigDecimal.ZERO) >= 0) {
            data.setRooms(bigDecimalToDouble(flaechen.getAnzahlZimmer()));
        }

        if (flaechen.getWohnflaeche() != null && flaechen.getWohnflaeche().compareTo(BigDecimal.ZERO) >= 0) {
            data.setSize(bigDecimalToDouble(flaechen.getWohnflaeche()));
        }

        if (!data.isGuestToilette()) {
            data.setGuestToilette(bigDecimalToInt(flaechen.getAnzahlSepWc()) > 0);
        }
        // TODO: Lookup uploaded Flats
        // flat.getDetails().setParkingSpace(flaechen.getAnzahlStellplaetze().);
        data.setGarden(bigDecimalToInt(flaechen.getGartenflaeche()) > 0);
        data.setGardenArea(bigDecimalToDouble(flaechen.getGartenflaeche()));
        data.setNumberOfBalconies(bigDecimalToDouble(flaechen.getAnzahlBalkone()));
        data.setNumberOfLoggias(OpenImmoToImmomioConverterUtils.bigDecimalToDouble(flaechen.getAnzahlLogia()));
        data.setNumberOfTerraces(bigDecimalToDouble(flaechen.getAnzahlTerrassen()));
        data.setBalconyTerraceArea(
                bigDecimalToDouble(flaechen.getBalkonTerrasseFlaeche()));
        data.setNumberOfBedrooms(bigDecimalToDouble(flaechen.getAnzahlSchlafzimmer()));

        if (flaechen.getKellerflaeche() != null && flaechen.getKellerflaeche().compareTo(BigDecimal.ZERO) >= 0) {
            data.setBasementAvailable(true);
            data.setBasementSize(bigDecimalToDouble(flaechen.getKellerflaeche()));
        }

        if (property.getData().getObjectType() == ObjectType.HOUSE) {
            data.setLandArea(bigDecimalToDouble(flaechen.getGrundstuecksflaeche()));
        }
    }

    protected void addAusstattung(Property property, Ausstattung ausstattung) {
        if (ausstattung == null) {
            return;
        }

        PropertyData data = property.getData();

        data.setBarrierFree(booleanToBoolean(ausstattung.isBarrierefrei()));
        data.setGround(convertBoden(ausstattung.getBoden()));
        data.setHeater(convertHeaterType(ausstattung.getHeizungsart()));
        data.setElevator(convertElevator(ausstattung.getFahrstuhl()));
        data.setGuestToilette(booleanToBoolean(ausstattung.isGaestewc()));
        data.setFlatShare(booleanToBoolean(ausstattung.isWgGeeignet()));
        data.setFireplace(booleanToBoolean(ausstattung.isKamin()));
        data.setGardenUse(booleanToBoolean(ausstattung.isGartennutzung()));
        data.setWashDryRoom(booleanToBoolean(ausstattung.isWaschTrockenraum()));
        data.setStoreRoom(booleanToBoolean(ausstattung.isAbstellraum()));
        data.setBicycleRoom(booleanToBoolean(ausstattung.isFahrradraum()));
        data.setAttic(booleanToBoolean(ausstattung.isDachboden()));
        data.setSeniors(booleanToBoolean(ausstattung.isSeniorengerecht()));
        data.setTvSatCable(booleanToBoolean(ausstattung.isKabelSatTv()));
        data.setWashingMachineConnection(Boolean.parseBoolean(
                getValueFromSimpleField(ausstattung.getUserDefinedSimplefield(), WASCHMASCHINENANSCHLUSS)));
        data.setShutter(booleanToBoolean(ausstattung.isRolladen()));
        addKueche(property, ausstattung.getKueche());
        addKeller(property, ausstattung.getUnterkellert());
        data.setIntercomType(convertIntercomType(getValueFromSimpleField(ausstattung.getUserDefinedSimplefield(), GEGENSPRECHANLAGE)));
        data.setWheelchairAccessible(booleanToBoolean(ausstattung.isRollstuhlgerecht()));

        if (ausstattung.getMoebliert() != null) {
            data.setFurnishingType(
                    convertFurnishingType(ausstattung.getMoebliert().getMoeb()));
        }

        addBad(property, ausstattung.getBad());
    }



    protected void addBad(Property property, Bad bad) {
        if (bad == null) {
            return;
        }

        BathroomEquipment bathroomEquipment = new BathroomEquipment();
        bathroomEquipment.setShower(bad.isDUSCHE());
        bathroomEquipment.setBathtub(bad.isWANNE());
        bathroomEquipment.setWindow(bad.isFENSTER());
        bathroomEquipment.setBidet(bad.isBIDET());
        bathroomEquipment.setUrinal(bad.isPISSOIR());

        property.getData().setBathroomEquipment(bathroomEquipment);
    }

    protected void addPreis(Property property, Preise preise) {
        if (preise == null) {
            return;
        }

        PropertyData data = property.getData();

        if (booleanToBoolean(preise.isHeizkostenEnthalten())) {
            data.setHeatingCostIncluded(true);
        } else if (preise.getHeizkosten() != null) {
            data.setHeatingCost(bigDecimalToDouble(preise.getHeizkosten()));
        }

        if (preise.getKaution() != null && preise.getKaution().compareTo(BigDecimal.ZERO) >= 0) {
            data.setBailment(bigDecimalToDouble(preise.getKaution()));
        }

        if (preise.getNebenkosten() != null && preise.getNebenkosten().compareTo(BigDecimal.ZERO) >= 0) {
            data.setServiceCharge(bigDecimalToDouble(preise.getNebenkosten()));
        }

        addBasePrice(preise, data);

        addStellplaetze(property, preise);
    }

    private boolean containsPreviousTenantFields(List<UserDefinedSimplefield> simplefields) {
        return simplefields.stream().anyMatch(userDefinedSimplefield -> PREVIOUS_TENANT_FIELDS.contains(userDefinedSimplefield.getFeldname().toLowerCase()));
    }

    private PropertyPreviousTenantBean createPreviousTenantFields(List<UserDefinedSimplefield> simplefields) {
        PropertyPreviousTenantBean tenantBean = new PropertyPreviousTenantBean();
        setPreTenantValue(simplefields, SIMPLE_FIELDS_VORMIETER_VORNAME, tenantBean::setFirstname);
        setPreTenantValue(simplefields, SIMPLE_FIELDS_VORMIETER_NACHNAME, tenantBean::setName);
        setPreTenantValue(simplefields, SIMPLE_FIELDS_VORMIETER_EMAIL, tenantBean::setEmail);
        setPreTenantValue(simplefields, SIMPLE_FIELDS_VORMIETER_TELEFON, tenantBean::setPhone);

        return tenantBean;
    }

    private void setPreTenantValue(List<UserDefinedSimplefield> simplefields, String key, Consumer<String> consumer) {
        simplefields.stream()
                .filter(simplefield -> key.equalsIgnoreCase(simplefield.getFeldname()))
                .map(UserDefinedSimplefield::getContent)
                .findFirst()
                .ifPresent(consumer);
    }
}
