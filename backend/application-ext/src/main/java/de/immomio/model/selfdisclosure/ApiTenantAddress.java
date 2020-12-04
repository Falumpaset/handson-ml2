package de.immomio.model.selfdisclosure;

import de.immomio.model.address.ApiPersonAddressAnswer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Niklas Lindemann
 */
@Getter
@Setter
@NoArgsConstructor
@Schema(name = "TenantAddressInformation")
public class ApiTenantAddress extends ApiPersonAddressAnswer {
    private static final long serialVersionUID = 197179382039225178L;

    @Schema(description = "the phone number")
    private String phone;

    public ApiTenantAddress(String city, String street, String houseNumber, String zipCode, String federalProvince, String email, String phone) {
        super(city, street, houseNumber, zipCode, federalProvince, email);
        this.phone = phone;
    }
}
