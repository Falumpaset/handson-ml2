package de.immomio.integration.customer;

import com.greghaskins.spectrum.Variable;
import de.immomio.beans.landlord.LandlordRegisterBean;
import de.immomio.integration.UserControllerTest;
import de.immomio.integration.base.BaseApiTest;
import de.immomio.data.base.type.customer.CustomerLocation;
import de.immomio.util.TestHelper;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.ResultActions;

import static com.greghaskins.spectrum.Spectrum.describe;
import static com.greghaskins.spectrum.Spectrum.it;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class CustomerControllerTest extends BaseApiTest {

    private final String userEmail = TestHelper.generateEmail();

    {
        wireAndUnwire();

        describe("Register new user and customer", () -> it("should return new user", () -> {
            LandlordRegisterBean data = TestHelper.createLandlordRegisterBean(userEmail);

            sendPostRequest(UserControllerTest.REGISTRATION_URL, this.json(data))
                    .andExpect(status().is(HttpStatus.OK.value()));
        }));

        describe("Given an unregistered user",
                () -> describe("GET /customers", () -> describe("without authentication", () -> {
                    Variable<ResultActions> req = describeRequest(401, () -> get("/customers"));

                    it("message == \"No authorization header present.\"", () -> req.get()
                            .andExpect(status().reason(containsString("No authorization header present."))));
                })));

        describe("Given a customer with a user that is customer admin", () -> {
            final String customerSelf = "http://localhost/customers/";
            Variable<Long> customerId = new Variable<>();

            describe("GET /customers", () -> {
                describe("without authentication", () -> {
                    Variable<ResultActions> req = describeRequest(401, () -> get("/customers"));

                    it("message == \"No authorization header present.\"", () -> req.get()
                            .andExpect(status().reason(containsString("No authorization header present."))));
                });

                describe("with authentication", () -> {
                    Variable<ResultActions> req = describeRequest(200,
                            () -> get("/customers").header(AUTHORIZATION_HEADER, authorizationHeaderFor(userEmail))
                    );

                    it("response contains own customer", () -> {
                        req.get()
                                .andExpect(jsonPath("$._embedded.landlordCustomers[0].location",
                                        Matchers.equalTo(CustomerLocation.DE.name())));

                        //                        customerId.set(extractID(req.get(), "$._embedded.landlordCustomers[0].id"));
                    });
                });
            });

            describe("GET /customers/{id}", () -> {
                describe("without authentication", () -> {
                    Variable<ResultActions> req = describeRequest(401, () -> get(customerSelf));

                    it("returns a helpful error message", () -> req.get()
                            .andExpect(status().reason(containsString("No authorization header present."))));
                });

                describe("with authentication", () -> {
                    Variable<ResultActions> req = describeRequest(200,
                            () -> get(customerSelf).header(AUTHORIZATION_HEADER, authorizationHeaderFor(userEmail))
                    );

                    it("has no managedCustomers link",
                            () -> req.get().andExpect(jsonPath("$._links.managedCustomers").doesNotExist()));

                    it("has no registerManagedCustomer link",
                            () -> req.get().andExpect(jsonPath("$._links.registerManagedCustomer").doesNotExist()));
                });
            });
        });
    }

    private String updateCustomerSettings() {
        return "{"
                + "\"customerSettings\": \"/customerSettings/2\""
                + "}";
    }
}
