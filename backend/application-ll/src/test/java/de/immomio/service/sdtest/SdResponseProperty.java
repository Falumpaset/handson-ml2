package de.immomio.service.sdtest;

import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.shared.entity.selfdisclosure.json.answertype.AddressAnswer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
@NoArgsConstructor
public class SdResponseProperty implements Serializable {
    private static final long serialVersionUID = 7757978980320716706L;
    private String id;
    private String name;
    private AddressAnswer address;
    private Integer floor;
    private Double rent;
    private Double heatingCost;
    private Double additionalCosts;
    private Double rooms;
    private Double size;


    public SdResponseProperty(PropertyData propertyData) {
        this.name = propertyData.getName();
        this.address = new AddressAnswer(propertyData.getAddress());
        this.floor = propertyData.getFloor();
        this.rent = propertyData.getBasePrice();
        this.heatingCost = propertyData.getHeatingCost();
        this.additionalCosts = propertyData.getServiceCharge();
        this.rooms = propertyData.getRooms();
        this.size = propertyData.getSize();
    }
}
