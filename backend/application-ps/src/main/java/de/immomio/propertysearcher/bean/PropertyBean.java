package de.immomio.propertysearcher.bean;

import de.immomio.data.shared.bean.common.Address;
import de.immomio.data.shared.bean.common.S3File;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
public class PropertyBean {

    private Long propertyId;

    private Address address;

    private String name;

    private S3File image;

    private String customerLogo;

    private String customerName;
}
