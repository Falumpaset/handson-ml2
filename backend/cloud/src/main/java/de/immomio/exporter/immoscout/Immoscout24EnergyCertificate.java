package de.immomio.exporter.immoscout;

import de.immobilienscout24.rest.schema.common._1.BuildingEnergyRatingType;
import de.immobilienscout24.rest.schema.common._1.EnergyPerformanceCertificate;
import de.immobilienscout24.rest.schema.common._1.EnergySourcesEnev2014;
import de.immobilienscout24.rest.schema.common._1.YesNotApplicableType;
import de.immobilienscout24.rest.schema.offer.realestates._1.ApartmentRent;
import de.immobilienscout24.rest.schema.offer.realestates._1.HouseRent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Immoscout24EnergyCertificate {

    private EnergyPerformanceCertificate energyCertificate;

    private Integer constructionYear;

    private BuildingEnergyRatingType buildingEnergyRatingType;

    private Double thermalCharacteristic;

    private YesNotApplicableType energyConsumptionContainsWarmWater;

    private EnergySourcesEnev2014 energySourcesEnev2014;

    public Immoscout24EnergyCertificate(ApartmentRent apartmentRent) {
        this.energyCertificate = apartmentRent.getEnergyCertificate();
        this.constructionYear = apartmentRent.getConstructionYear();
        this.buildingEnergyRatingType = apartmentRent.getBuildingEnergyRatingType();
        this.thermalCharacteristic = apartmentRent.getThermalCharacteristic();
        this.energyConsumptionContainsWarmWater = apartmentRent.getEnergyConsumptionContainsWarmWater();
        this.energySourcesEnev2014 = apartmentRent.getEnergySourcesEnev2014();
    }

    public Immoscout24EnergyCertificate(HouseRent houseRent) {
        this.energyCertificate = houseRent.getEnergyCertificate();
        this.constructionYear = houseRent.getConstructionYear();
        this.buildingEnergyRatingType = houseRent.getBuildingEnergyRatingType();
        this.thermalCharacteristic = houseRent.getThermalCharacteristic();
        this.energyConsumptionContainsWarmWater = houseRent.getEnergyConsumptionContainsWarmWater();
        this.energySourcesEnev2014 = houseRent.getEnergySourcesEnev2014();
    }
}
