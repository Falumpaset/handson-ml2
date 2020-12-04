package de.immomio.integration;

import com.greghaskins.spectrum.Variable;
import de.immomio.integration.base.BaseApiTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.RequestDispatcher;

import static com.greghaskins.spectrum.Spectrum.describe;
import static com.greghaskins.spectrum.Spectrum.it;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiRootTest extends BaseApiTest {

    {
        wireAndUnwire();

        describe("API Root", () -> {
            Variable<ResultActions> getRoot = describeRequest(200, () ->
                    get("/")
                            .contentType(MediaTypes.HAL_JSON)
                            .accept(MediaTypes.HAL_JSON)
            );

            it("is available", () -> getRoot.get().andExpect(status().isOk()));

            it("has a landlordAddonProducts link",
                    () -> getRoot.get().andExpect(jsonPath("$._links.landlordAddonProducts.href").exists()));

            it("has a landlordPermissionSchemeRights link",
                    () -> getRoot.get().andExpect(jsonPath("$._links.landlordPermissionSchemeRights.href").exists()));

            it("has a landlordProductBasketProducts link",
                    () -> getRoot.get().andExpect(jsonPath("$._links.landlordProductBasketProducts.href").exists()));

            it("has a landlordProductLimitations link",
                    () -> getRoot.get().andExpect(jsonPath("$._links.landlordProductLimitations.href").exists()));

            it("has a priosets link", () -> getRoot.get().andExpect(jsonPath("$._links.priosets.href").exists()));

            it("has a landlordCoupons link",
                    () -> getRoot.get().andExpect(jsonPath("$._links.landlordCoupons.href").exists()));

            it("has a importLogs link", () -> getRoot.get().andExpect(jsonPath("$._links.importLogs.href").exists()));

            it("has a landlordCustomerAddonProducts link",
                    () -> getRoot.get().andExpect(jsonPath("$._links.landlordCustomerAddonProducts.href").exists()));

            it("has a landlordRights link",
                    () -> getRoot.get().andExpect(jsonPath("$._links.landlordRights.href").exists()));

            it("has a landlordUsages link",
                    () -> getRoot.get().andExpect(jsonPath("$._links.landlordUsages.href").exists()));

            it("has a landlordUsers link",
                    () -> getRoot.get().andExpect(jsonPath("$._links.landlordUsers.href").exists()));

            it("has a invoices link", () -> getRoot.get().andExpect(jsonPath("$._links.invoices.href").exists()));

            it("has a credentials link", () -> getRoot.get().andExpect(jsonPath("$._links.credentials.href").exists()));

            it("has a landlordCouponUsages link",
                    () -> getRoot.get().andExpect(jsonPath("$._links.landlordCouponUsages.href").exists()));

            it("has a landlordProductAddons link",
                    () -> getRoot.get().andExpect(jsonPath("$._links.landlordProductAddons.href").exists()));

            it("has a properties link", () -> getRoot.get().andExpect(jsonPath("$._links.properties.href").exists()));

            it("has a landlordQuotas link",
                    () -> getRoot.get().andExpect(jsonPath("$._links.landlordQuotas.href").exists()));

            it("has a landlordPrices link",
                    () -> getRoot.get().andExpect(jsonPath("$._links.landlordPrices.href").exists()));

            it("has a landlordUsertypeRights link",
                    () -> getRoot.get().andExpect(jsonPath("$._links.landlordUsertypeRights.href").exists()));

            it("has a landlordProducts link",
                    () -> getRoot.get().andExpect(jsonPath("$._links.landlordProducts.href").exists()));

            it("has a propertyPortals link",
                    () -> getRoot.get().andExpect(jsonPath("$._links.propertyPortals.href").exists()));

            it("has a ftpAccesses link", () -> getRoot.get().andExpect(jsonPath("$._links.ftpAccesses.href").exists()));

            it("has a landlordEmails link",
                    () -> getRoot.get().andExpect(jsonPath("$._links.landlordEmails.href").exists()));

            it("has a landlordProductPermissionSchemes link",
                    () -> getRoot.get().andExpect(jsonPath("$._links.landlordProductPermissionSchemes.href").exists()));

            it("has a landlordProductBaskets link",
                    () -> getRoot.get().andExpect(jsonPath("$._links.landlordProductBaskets.href").exists()));

            it("has a landlordDiscounts link",
                    () -> getRoot.get().andExpect(jsonPath("$._links.landlordDiscounts.href").exists()));

            it("has a landlordProductBasketProductAddons link", () -> getRoot.get()
                    .andExpect(jsonPath("$._links.landlordProductBasketProductAddons.href").exists()));

            it("has a landlordCustomerProducts link",
                    () -> getRoot.get().andExpect(jsonPath("$._links.landlordCustomerProducts.href").exists()));

            it("has a landlordAddonProductPermissionSchemes link", () -> getRoot.get()
                    .andExpect(jsonPath("$._links.landlordAddonProductPermissionSchemes.href").exists()));

            it("has a landlordProductPrices link",
                    () -> getRoot.get().andExpect(jsonPath("$._links.landlordProductPrices.href").exists()));

            it("has a landlordPermissionSchemes link",
                    () -> getRoot.get().andExpect(jsonPath("$._links.landlordPermissionSchemes.href").exists()));

            it("has a landlordCustomers link",
                    () -> getRoot.get().andExpect(jsonPath("$._links.landlordCustomers.href").exists()));

            it("has a landlordAddonProductLimitations link",
                    () -> getRoot.get().andExpect(jsonPath("$._links.landlordAddonProductLimitations.href").exists()));

            it("has a landlordProductAddonPrices link",
                    () -> getRoot.get().andExpect(jsonPath("$._links.landlordProductAddonPrices.href").exists()));

            it("has a messageSources link",
                    () -> getRoot.get().andExpect(jsonPath("$._links.messageSources.href").exists()));

            it("has a profile link", () -> getRoot.get().andExpect(jsonPath("$._links.profile.href").exists()));

        });

        describe("API errors", () -> {
            Variable<ResultActions> getError = describeRequest(500, () ->
                    get("/error")
                            .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 500)
                            .requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/users")
                            .requestAttr(RequestDispatcher.ERROR_MESSAGE,
                                    "The user 'http://localhost:8080/users/123' does not exist")
            );

            it("return with status 500", () -> getError.get().andExpect(status().isInternalServerError()));
        });
    }

}
