package de.immomio.model;

import com.greghaskins.spectrum.Spectrum;
import com.greghaskins.spectrum.dsl.specification.Specification;
import de.immomio.data.base.entity.customer.user.AbstractUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.spel.spi.EvaluationContextExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;

import static org.junit.Assert.assertTrue;

@RunWith(Spectrum.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE,
        connection = EmbeddedDatabaseConnection.NONE)
@EnableJpaRepositories(basePackages = {
        "de.immomio.model.repository.landlord",
        "de.immomio.model.repository.propertysearcher",
        "de.immomio.model.repository.shared"
})
@EntityScan(basePackages = {"de.immomio.model"})
@Rollback
@Transactional
@Slf4j
public abstract class BaseDataJpaTest {

    @ClassRule
    public static final SpringClassRule classRule = new SpringClassRule();

    @Rule
    public SpringMethodRule methodRule = new SpringMethodRule();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    protected static final int INIT_PAGE = 0;

    protected static final int ITEMS_PER_PAGE = 1000;

    public void wireAndUnwire() {
        try {
            // Use the official spring TestContext, see
            // https://docs.spring.io/spring/docs/current/spring-framework-reference/html/integration-testing.html
            TestContextManager testContextManager = new TestContextManager(this.getClass());
            final Method wire = this.getClass().getMethod("wireAndUnwire");
            testContextManager.prepareTestInstance(this); // this injects

            // to spring's testing infrastructure, we pretend running a full test class with
            // a single test method. Because we have a @Transactional() on this base class, spring
            // will rollback the test TX after a full scenario
            Spectrum.beforeEach(() -> testContextManager.beforeTestMethod(this, wire));

            Spectrum.beforeAll(testContextManager::beforeTestClass);

            Spectrum.afterAll(() -> {
                testContextManager.afterTestMethod(this, wire, null);
                testContextManager.afterTestClass();
            });

            Spectrum.afterEach(() -> testContextManager.afterTestMethod(this, wire, null));

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    protected void describe(final String context, final com.greghaskins.spectrum.Block block) {
        Specification.describe(context, () -> {
            wireAndUnwire();
            block.run();
        });
    }

    protected void withAuthentication(final AbstractUser user, final com.greghaskins.spectrum.Block block) {
        UsernamePasswordAuthenticationToken tomAuth = new UsernamePasswordAuthenticationToken(user, "x");
        SecurityContextHolder.getContext().setAuthentication(tomAuth);

        try {
            block.run();
        } catch (Throwable throwable) {
            log.error(throwable.getMessage(), throwable);
        }
    }

    protected void assertTrueNotNullPropertyViolationType(Set<ConstraintViolation<?>> violations, String property) {
        assertTrue(violations.stream().anyMatch(violation -> checkNotNullPropertyViolationType(violation, property)));
    }

    protected void assertTrueNotEmptyPropertyViolationType(Set<ConstraintViolation<?>> violations, String property) {
        assertTrue(violations.stream().anyMatch(violation -> checkNotEmptyPropertyViolationType(violation, property)));
    }

    private boolean checkNotNullPropertyViolationType(ConstraintViolation<?> violation, String property) {
        return Objects.equals(violation.getPropertyPath().toString(), property) &&
                violation.getMessageTemplate().contains(NotNull.class.getName());
    }

    private boolean checkNotEmptyPropertyViolationType(ConstraintViolation<?> violation, String property) {
        return Objects.equals(violation.getPropertyPath().toString(), property) &&
                violation.getMessageTemplate().contains(NotEmpty.class.getName());
    }

    @SpringBootApplication
    static class Application {

        @Bean
        public EvaluationContextExtension securityExtension() {
            return new SecurityEvaluationContextExtension();
        }
    }

}
