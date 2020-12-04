package de.immomio.importer.converter.openimmo;

import de.immomio.data.landlord.bean.property.certificate.DemandCertificate;
import de.immomio.data.landlord.bean.property.certificate.EnergyCertificate;
import de.immomio.data.landlord.bean.property.certificate.UsageCertificate;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.openimmo.Ausstattung;
import de.immomio.openimmo.Befeuerung;
import de.immomio.openimmo.Energiepass;
import de.immomio.openimmo.Immobilie;
import de.immomio.openimmo.ZustandAngaben;
import de.immomio.openimmo.typen.OpenImmoEpartTyp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;

import static de.immomio.openimmo.constants.OpenImmoConstants.EPASS_WERTKLASSE;

/**
 * @author Fabian Beck
 */

@Slf4j
@Service
public class OpenImmoToImmomioConverterEnergyCertificate {
    public static final LocalDate CERTIFICATIOM_CREATIOM_DATE_BEFORE = LocalDate.of(2014, 5, 1);

    public void addEnergyCertificate(Property property, Immobilie immobilie) {
        property.getData().setEnergyCertificate(createEnergyCertificate(immobilie.getZustandAngaben()));

        addBefeuerung(property, immobilie.getAusstattung());
    }

    private EnergyCertificate createEnergyCertificate(ZustandAngaben zustandAngaben) {
        EnergyCertificate energyCertificate = new EnergyCertificate();

        if (zustandAngaben == null) {
            return energyCertificate;
        }

        if (zustandAngaben.getEnergiepass() != null && zustandAngaben.getEnergiepass().size() == 1) {
            Energiepass energiepass = zustandAngaben.getEnergiepass().get(0);

            energyCertificate.setYearOfConstruction(
                    getYearOfConstructionForEnergyCertificate(energiepass, zustandAngaben));
            energyCertificate.setCreationDate(evaluateCreationDate(energiepass));
            OpenImmoEpartTyp epartTyp = EnumUtils.getEnumIgnoreCase(OpenImmoEpartTyp.class, energiepass.getEpart());
            if (epartTyp != null) {
                switch (epartTyp) {
                    case KEINER:
                        energyCertificate.setEnergyCertificateType(
                                EnergyCertificate.EnergyCertificateType.NO_AVAILABLE);
                        break;
                    case BEDARF:
                        energyCertificate.setEnergyCertificateType(
                                EnergyCertificate.EnergyCertificateType.DEMAND_IDENTIFICATION);
                        energyCertificate.setDemandCertificate(createDemandCertificate(energiepass, zustandAngaben));
                        break;
                    case VERBRAUCH:
                        energyCertificate.setEnergyCertificateType(
                                EnergyCertificate.EnergyCertificateType.USAGE_IDENTIFICATION);
                        energyCertificate.setUsageCertificate(createUsageCertificate(energiepass, zustandAngaben));
                        break;
                }
            }
        }

        return energyCertificate;
    }

    private UsageCertificate createUsageCertificate(Energiepass energiepass, ZustandAngaben zustandAngaben) {
        UsageCertificate usageCertificate = new UsageCertificate();

        usageCertificate.setIncludesHeatConsumption(
                OpenImmoToImmomioConverterUtils.booleanToBoolean(energiepass.isMitwarmwasser()));

        Double evkw = OpenImmoToImmomioConverterUtils.parseDouble(energiepass.getEnergieverbrauchkennwert());
        if (evkw != null) {
            usageCertificate.setEnergyConsumptionParameter(evkw.toString());
            usageCertificate.setEnergyConsumption(evkw.toString());
        }

        String wertklasse = energiepass.getWertklasse();
        if (wertklasse == null || wertklasse.isEmpty()) {
            wertklasse = OpenImmoToImmomioConverterUtils.getValueFromSimpleField(
                    zustandAngaben.getUserDefinedSimplefield(), EPASS_WERTKLASSE);
        }
        usageCertificate.setEnergyEfficiencyClass(OpenImmoToImmomioConverterUtils.convertEnergyClassType(wertklasse));

        return usageCertificate;
    }

    private DemandCertificate createDemandCertificate(Energiepass energiepass, ZustandAngaben zustandAngaben) {
        DemandCertificate demandCertificate = new DemandCertificate();
        Double eec = OpenImmoToImmomioConverterUtils.parseDouble(energiepass.getEndenergiebedarf());

        if (eec != null) {
            demandCertificate.setEndEnergyConsumption(eec.toString());
        }

        String wertklasse = energiepass.getWertklasse();
        if (wertklasse == null || wertklasse.isEmpty()) {
            wertklasse = OpenImmoToImmomioConverterUtils.getValueFromSimpleField(
                    zustandAngaben.getUserDefinedSimplefield(), EPASS_WERTKLASSE);
        }

        demandCertificate.setEnergyEfficiencyClass(OpenImmoToImmomioConverterUtils.convertEnergyClassType(wertklasse));
        return demandCertificate;
    }

    private void addBefeuerung(Property property, Ausstattung ausstattung) {
        if (ausstattung == null) {
            return;
        }

        property.getData()
                .getEnergyCertificate()
                .setPrimaryEnergyProvider(
                        OpenImmoToImmomioConverterUtils.convertPrimaryEnergy(ausstattung.getBefeuerung()));
    }

    private EnergyCertificate.CertificateCreationDate evaluateCreationDate(Energiepass energiepass) {
        EnergyCertificate.CertificateCreationDate creationDate = OpenImmoToImmomioConverterUtils.convertJahrgang(
                energiepass.getJahrgang());
        if (creationDate != null) {
            return creationDate;
        }

        XMLGregorianCalendar ausstelldatum = energiepass.getAusstelldatum();
        if (ausstelldatum != null) {
            LocalDate certificateCreationDate = ausstelldatum.toGregorianCalendar().toZonedDateTime().toLocalDate();
            if (certificateCreationDate != null) {

                if (certificateCreationDate.isBefore(CERTIFICATIOM_CREATIOM_DATE_BEFORE)) {
                    return EnergyCertificate.CertificateCreationDate.APRIL_2014;
                } else {
                    return EnergyCertificate.CertificateCreationDate.MAY_2014;
                }
            }
        }

        String wertklasse = energiepass.getWertklasse();
        if (wertklasse != null && !wertklasse.isEmpty()) {
            return EnergyCertificate.CertificateCreationDate.MAY_2014;
        }

        return EnergyCertificate.CertificateCreationDate.APRIL_2014;
    }

    private Integer getYearOfConstructionForEnergyCertificate(Energiepass energiepass, ZustandAngaben zustandAngaben) {
        if (energiepass == null) {
            return null;
        }

        try {
            return Integer.valueOf(energiepass.getBaujahr());
        } catch (NumberFormatException ex) {
            log.info(ex.getMessage());
        }

        try {
            return Integer.valueOf(zustandAngaben.getBaujahr());
        } catch (NumberFormatException ex) {
            log.info(ex.getMessage());
        }

        log.info("Exception raised parsing YearOfConstruction -> " + energiepass.getBaujahr());
        return null;
    }
}
