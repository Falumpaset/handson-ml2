package de.immomio.exporter.immoscout;

import de.immomio.data.landlord.bean.property.data.PropertyData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Immoscout24Prices {

    private double basePrice;

    private double heatingCost;

    private double serviceCharge;

    private double parkingPrice;

    public Immoscout24Prices(PropertyData propertyData) {
        this.basePrice = getValue(propertyData.getBasePrice());
        this.heatingCost = getValue(propertyData.getHeatingCost());
        this.serviceCharge = getValue(propertyData.getServiceCharge());
        this.parkingPrice = getValue(propertyData.getTotalParkingPrice());
    }

    private double getValue(Double value) {
        return value == null ? 0 : value;
    }
}
