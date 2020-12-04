package de.immomio.integration.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greghaskins.spectrum.Spectrum;
import com.greghaskins.spectrum.Variable;
import de.immomio.Application;
import de.immomio.config.CustomAPIConfiguration;
import de.immomio.utils.TokenHelper;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static com.greghaskins.spectrum.Spectrum.afterAll;
import static com.greghaskins.spectrum.Spectrum.beforeAll;
import static com.greghaskins.spectrum.Spectrum.it;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Johannes Hiemer.
 * @author Maik Kingma
 */
@RunWith(Spectrum.class)
@ActiveProfiles(value = {"test-api", "default", "DE"})
@Transactional
@ContextConfiguration(classes = {
        CustomAPIConfiguration.class
})
@SpringBootTest(classes = {Application.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class SpectrumMockMvcTest {

    protected static final String AUTHORIZATION_HEADER = "Authorization";

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    protected MockMvc mockMvc;

    @Value("${auth.token.name}")
    protected String tokenName;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    public void wireAndUnwire() {

        System.setProperty("DEPLOYMENT_LOCATION", "DE");

        // prepare the test instance before running any further methods so that
        // code between beforeAll etc. have a chance to use autowired dependencies

        try {
            // Use the official spring TestContext, see
            // https://docs.spring.io/spring/docs/current/spring-framework-reference/html/integration-testing.html
            TestContextManager testContextManager = new TestContextManager(this.getClass());
            final Method wire = this.getClass().getMethod("wireAndUnwire");
            testContextManager.prepareTestInstance(this); // this injects

            mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();

            // to spring's testing infrastructure, we pretend running a full test class with
            // a single
            // test method. Because we have a @Transactional() on this base class, spring
            // will rollback
            // the test TX after a full scenario
            beforeAll(() -> {
                testContextManager.beforeTestClass();
                testContextManager.beforeTestMethod(this, wire);
            });

            afterAll(() -> {
                testContextManager.afterTestMethod(this, wire, null);
                testContextManager.afterTestClass();
            });

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public String authorizationHeaderFor(String email)
            throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        return "Bearer " + TokenHelper.tokenFor(email);
    }

    public Variable<ResultActions> describeRequest(int expectedStatusCode, RequestSupplier f) {
        Variable<ResultActions> v = new Variable<>();

        beforeAll(() -> {
            ResultActions r = mockMvc.perform(f.get());

            try {
                r.andExpect(status().is(expectedStatusCode));
                v.set(r);
            } catch (AssertionError e) {
                r.andDo(print());
                throw e;
            }
        });

        it("returns status " + expectedStatusCode, () -> {
            // already checked by beforeAll
        });

        return v;
    }

    public interface RequestSupplier {
        MockHttpServletRequestBuilder get() throws Exception;
    }

}
