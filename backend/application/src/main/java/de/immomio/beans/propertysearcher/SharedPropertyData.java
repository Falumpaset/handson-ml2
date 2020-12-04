package de.immomio.beans.propertysearcher;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.immomio.data.base.type.property.BuildingConditionType;
import de.immomio.data.base.type.property.FlatType;
import de.immomio.data.base.type.property.GroundType;
import de.immomio.data.base.type.property.HeaterFiringType;
import de.immomio.data.landlord.bean.customer.settings.theme.LandlordCustomerBranding;
import de.immomio.data.landlord.bean.customer.settings.theme.LandlordCustomerBrandingTheme;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.settings.LandlordCustomerSettings;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.data.shared.bean.common.ParkingSpace;
import de.immomio.data.shared.bean.common.S3File;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.BooleanUtils;

import java.util.List;

@Getter
@Setter
@JsonInclude
public class SharedPropertyData {

    private Long id;

    private String externalId;

    private String referenceId;

    private String name;

    private String description;

    private String furnishingDescription;

    private String shortDescription;

    private Address address;

    private String miscellaneous;

    private Integer floor;

    private boolean balcony;

    private GroundType ground;

    private HeaterFiringType heater;

    private Integer bathRooms;

    private boolean guestToilette;

    private boolean kitchenette;

    private BuildingConditionType buildingCondition;

    private boolean historicBuilding;

    private boolean barrierFree;

    private boolean garden;

    private boolean elevator;

    private Double basementSize;

    private Boolean basementAvailable;

    private Double size;

    private Double rooms;

    private FlatType flatType;

    private List<S3File> attachments;

    private Double bailment;

    private Double basePrice;

    private Double heatingCost;

    private Double serviceCharge;

    private List<ParkingSpace> parkingSpaces;

    private boolean heatingCostIncluded;

    private String customerLogo;

    private String customerName;

    private Long customerId;

    private LandlordCustomerBranding branding;

    private Double totalRentGross;

    private boolean allowContinueAsGuest;

    public SharedPropertyData(Property property) {
        PropertyData propertyData = property.getData();

        this.id = property.getId();
        this.externalId = property.getExternalId();
        this.referenceId = propertyData.getReferenceId();
        this.name = propertyData.getName();
        this.description = propertyData.getObjectDescription();
        this.furnishingDescription = propertyData.getFurnishingDescription();
        this.address = propertyData.getAddress();
        this.miscellaneous = propertyData.getObjectMiscellaneousText();
        this.floor = propertyData.getFloor();
        this.ground = propertyData.getGround();
        this.heater = propertyData.getHeater();
        this.bathRooms = propertyData.getBathRooms();
        this.guestToilette = propertyData.isGuestToilette();
        this.kitchenette = propertyData.isKitchenette();
        this.buildingCondition = propertyData.getBuildingCondition();
        this.historicBuilding = propertyData.isHistoricBuilding();
        this.barrierFree = propertyData.isBarrierFree();
        this.garden = propertyData.isGarden();
        this.elevator = propertyData.isElevator();
        this.basementSize = propertyData.getBasementSize();
        this.basementAvailable = propertyData.getBasementAvailable();
        this.size = propertyData.getSize();
        this.rooms = propertyData.getRooms();
        this.flatType = propertyData.getFlatType();
        this.attachments = propertyData.getAttachments();
        this.bailment = propertyData.getBailment();
        this.basePrice = propertyData.getBasePrice();
        this.parkingSpaces = propertyData.getParkingSpaces();
        this.heatingCost = propertyData.getHeatingCost();
        this.heatingCostIncluded = propertyData.isHeatingCostIncluded();
        this.serviceCharge = propertyData.getServiceCharge();
        this.customerLogo = property.getCustomerLogo();
        this.customerName = property.getCustomerName();
        this.totalRentGross = propertyData.getTotalRentGross();

        LandlordCustomer customer = property.getCustomer();
        this.customerId = customer.getId();

        LandlordCustomerSettings settings = customer.getCustomerSettings();
        if (settings != null) {
            this.allowContinueAsGuest = BooleanUtils.isTrue(settings.getAllowContinueAsGuest());
            this.branding = customer.getCustomerBranding();
        }
    }
}
