package de.immomio.importer.converter.openimmo;

import de.immomio.constants.exceptions.OpenImmoToImmomioConverterException;
import de.immomio.data.base.type.property.ObjectType;
import de.immomio.data.base.type.property.PropertyType;
import de.immomio.data.landlord.bean.property.data.CommercialData;
import de.immomio.data.landlord.bean.property.data.GarageData;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.openimmo.Immobilie;
import de.immomio.openimmo.Objektart;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Fabian Beck
 */

@Slf4j
@Service
public class OpenImmoToImmomioConverter {

    private final OpenImmoToImmomioConverterCommercial openImmoToImmomioConvertCommercial;
    private final OpenImmoToImmomioConverterFlat openImmoToImmomioConvertFlat;
    private final OpenImmoToImmomioConverterGarage openImmoToImmomioConvertGarage;

    @Autowired
    public OpenImmoToImmomioConverter(OpenImmoToImmomioConverterCommercial openImmoToImmomioConvertCommercial,
            OpenImmoToImmomioConverterFlat openImmoToImmomioConvertFlat,
            OpenImmoToImmomioConverterGarage openImmoToImmomioConvertGarage) {
        this.openImmoToImmomioConvertCommercial = openImmoToImmomioConvertCommercial;
        this.openImmoToImmomioConvertFlat = openImmoToImmomioConvertFlat;
        this.openImmoToImmomioConvertGarage = openImmoToImmomioConvertGarage;
    }

    public Property convert(Immobilie immobilie) throws OpenImmoToImmomioConverterException {
        ObjectType objectType = findObjectTypeForImmobilie(immobilie);

        Property property = new Property();
        property.setData(new PropertyData());

        switch (objectType) {
            case FLAT:
            case HOUSE:
                property.setType(PropertyType.FLAT);
                openImmoToImmomioConvertFlat.addImmobilie(property, immobilie);
                break;
            case OFFICE:
                property.setType(PropertyType.COMMERCIAL);
                property.getData().setCommercialData(new CommercialData());
                openImmoToImmomioConvertCommercial.addImmobilie(property, immobilie);
                break;
            case GARAGE:
                property.setType(PropertyType.GARAGE);
                property.getData().setGarageData(new GarageData());
                openImmoToImmomioConvertGarage.addImmobilie(property, immobilie);
                break;
            default:
                throw new OpenImmoToImmomioConverterException("ObjectType " + objectType + " not found");
        }

        return property;
    }

    private ObjectType findObjectTypeForImmobilie(Immobilie immobilie) throws OpenImmoToImmomioConverterException {
        Objektart objektart = immobilie.getObjektkategorie().getObjektart();
        Map<ObjectType, Integer> typeMap = new HashMap<>();
        typeMap.put(ObjectType.FLAT, objektart.getWohnung().size());
        typeMap.put(ObjectType.HOUSE, objektart.getHaus().size());
        typeMap.put(ObjectType.OFFICE, objektart.getBueroPraxen().size());
        typeMap.put(ObjectType.GARAGE, objektart.getParken().size());
        typeMap.put(ObjectType.ROOM, objektart.getZimmer().size());

        int objectTypesSum = typeMap.values().stream().mapToInt(Integer::intValue).sum();
        if (objectTypesSum == 0) {
            log.warn("no ObjectType found choosing Flat");
            return ObjectType.FLAT;
        } else if (objectTypesSum > 1) {
            log.error("no unique ObjectType found");
            throw new OpenImmoToImmomioConverterException("No unique ObjectType");
        }

        return typeMap.entrySet().stream().filter(entry -> entry.getValue() == 1).findFirst().get().getKey();
    }
}
