package de.immomio.integration.auth;

import com.greghaskins.spectrum.Variable;
import de.immomio.beans.landlord.LandlordRegisterBean;
import de.immomio.integration.UserControllerTest;
import de.immomio.integration.base.BaseApiTest;
import de.immomio.util.TestHelper;
import de.immomio.utils.TokenHelper;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.ResultActions;

import static com.greghaskins.spectrum.Spectrum.describe;
import static com.greghaskins.spectrum.Spectrum.it;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationTest extends BaseApiTest {

    private final String customersUri = "/customers";

    private final String invalidUserEmail = "invalidcustomer@meshcloud.io";

    {
        wireAndUnwire();

        String validUserEmail = TestHelper.generateEmail();

        describe("incorrect token", () -> {
            Variable<ResultActions> req = describeRequest(401, () -> get(customersUri)
                    .header(AUTHORIZATION_HEADER, "Bearer " + TokenHelper.tokenWithInvalidSignature()));

            it("message == \"Error verifying signature of token\"",
                    () -> req.get().andExpect(status().reason(containsString("Error verifying signature of token"))));
        });

        describe("correct token with unknown user", () -> {
            Variable<ResultActions> req = describeRequest(401, () -> get(customersUri)
                    .header(AUTHORIZATION_HEADER, authorizationHeaderFor(invalidUserEmail)));

            it("message == \"username could not be found\"",
                    () -> req.get().andExpect(status().reason(containsString("username could not be found"))));
        });

        describe("Register new user and customer", () -> it("should return new user", () -> {
            LandlordRegisterBean data = TestHelper.createLandlordRegisterBean(validUserEmail);

            sendPostRequest(UserControllerTest.REGISTRATION_URL, this.json(data))
                    .andExpect(status().is(HttpStatus.OK.value()));
        }));

        describe("correct token and correct user", () -> {

            Variable<ResultActions> req = describeRequest(200, () -> get(customersUri)
                    .header(AUTHORIZATION_HEADER, authorizationHeaderFor(validUserEmail)));

        });
    }
}
