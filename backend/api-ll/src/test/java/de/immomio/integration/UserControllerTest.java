package de.immomio.integration;

import de.immomio.beans.landlord.LandlordRegisterBean;
import de.immomio.common.ErrorCode;
import de.immomio.integration.base.BaseApiTest;
import de.immomio.util.TestHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import static com.greghaskins.spectrum.Spectrum.describe;
import static com.greghaskins.spectrum.Spectrum.it;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class UserControllerTest extends BaseApiTest {

    public static final String REGISTRATION_URL = "/users/register";

    private static final String UPDATE_EMAIL_URL = "/users/%s/email";

    private static final String RESET_PASSWORD_URL = "/users/reset-password";

    {
        wireAndUnwire();

        String email = TestHelper.generateEmail();

        describe("Register user", () -> {

            it("should return new user", () -> {
                LandlordRegisterBean data = TestHelper.createLandlordRegisterBean(email);

                sendPostRequest(REGISTRATION_URL, this.json(data))
                        .andExpect(status().is(HttpStatus.OK.value()));
            });

            it("should return error when duplicate", () -> {
                LandlordRegisterBean data = TestHelper.createLandlordRegisterBean(email);

                sendPostRequest(REGISTRATION_URL, this.json(data))
                        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                        .andExpect(jsonPath(DEFAULT_MSG_ERROR_KEY).value(ErrorCode.ERROR_EMAIL_ALREADY_EXISTS));
            });

            it("should return validation errors - confirm password ", () -> {
                LandlordRegisterBean data = TestHelper.createLandlordRegisterBean(TestHelper.generateEmail());
                data.setConfirmPassword(data.getPassword() + "1");

                sendPostRequest(REGISTRATION_URL, this.json(data))
                        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                        .andExpect(jsonPath(DEFAULT_MSG_ERROR_KEY).value(ErrorCode.ERROR_PASSWORD_NOT_EQUAL));
            });

            it("should return validation errors - no email ", () -> {
                LandlordRegisterBean data = TestHelper.createLandlordRegisterBean(TestHelper.generateEmail());
                data.setEmail(null);

                sendPostRequest(REGISTRATION_URL, this.json(data))
                        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                        .andExpect(jsonPath(DEFAULT_MSG_ERROR_KEY).value(ErrorCode.ERROR_NO_EMAIL));
            });

        });

        describe("Update email", () -> it("Update email - User not found", () -> {
            String url = String.format(UPDATE_EMAIL_URL, "123456789");
            String changeEmail = TestHelper.generateEmail();
            sendAuthorizedPutRequest(url, this.json(TestHelper.createChangeEmailBean(changeEmail)), email)
                    .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
        }));
    }
}
