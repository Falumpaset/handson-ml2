package de.immomio.model.repository.propertysearcher.user;

import de.immomio.data.propertysearcher.entity.customer.PropertySearcherCustomer;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.model.BaseDataJpaTest;
import de.immomio.model.repository.propertysearcher.customer.PropertySearcherCustomerRepository;
import de.immomio.utils.TestHelper;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionSystemException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

import static com.greghaskins.spectrum.Spectrum.it;
import static de.immomio.utils.TestComparatorHelper.comparePropertySearcherUsers;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PropertySearcherUserRepositoryTest extends BaseDataJpaTest {

    private static final String CUSTOMER_FIELD = "customer";

    private static final String PROFILE_FIELD = "profile";

    private static final String USER_TYPE_FIELD = "usertype";

    private static final String EMAIL_FIELD = "email";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private PropertySearcherUserRepository userRepository;

    @Autowired
    private PropertySearcherCustomerRepository customerRepository;

    {
        describe("create user", () -> {

            it("create user - validation failed", () -> {
                try {
                    PropertySearcherUser propertySearcherUser = TestHelper.generatePropertySearcherUser();
                    userRepository.save(propertySearcherUser);
                } catch (TransactionSystemException ex) {
                    Throwable cause = ex.getOriginalException().getCause();

                    assertTrue(cause instanceof ConstraintViolationException);

                    ConstraintViolationException violationException = ConstraintViolationException.class.cast(cause);
                    Set<ConstraintViolation<?>> violations = violationException.getConstraintViolations();

                    assertTrueNotNullPropertyViolationType(violations, CUSTOMER_FIELD);
                    assertTrueNotNullPropertyViolationType(violations, PROFILE_FIELD);
                    assertTrueNotNullPropertyViolationType(violations, USER_TYPE_FIELD);
                    assertTrueNotEmptyPropertyViolationType(violations, EMAIL_FIELD);
                }
            });

            it("create user - success", () -> {
                PropertySearcherCustomer propertySearcherCustomer = generateAndSavePropertySearcherCustomer();

                PropertySearcherUser propertySearcherUser = TestHelper.generatePropertySearcherUser(
                        TestHelper.generateEmail(), propertySearcherCustomer);
                PropertySearcherUser savedUser = userRepository.save(propertySearcherUser);

                comparePropertySearcherUsers(propertySearcherUser, savedUser);
            });

        });

        describe("find user", () -> {

            it("find user by ID", () -> {
                PropertySearcherCustomer propertySearcherCustomer1 = generateAndSavePropertySearcherCustomer();
                PropertySearcherUser propertySearcherUser1 = TestHelper.generatePropertySearcherUser(
                        TestHelper.generateEmail(), propertySearcherCustomer1);
                PropertySearcherUser savedUser1 = userRepository.save(propertySearcherUser1);

                PropertySearcherCustomer propertySearcherCustomer2 = generateAndSavePropertySearcherCustomer();
                PropertySearcherUser propertySearcherUser2 = TestHelper.generatePropertySearcherUser(
                        TestHelper.generateEmail(), propertySearcherCustomer2);
                PropertySearcherUser savedUser2 = userRepository.save(propertySearcherUser2);

                withAuthentication(savedUser1,
                        () -> comparePropertySearcherUsers(userRepository.findById(savedUser1.getId()).get(), savedUser1));
                withAuthentication(savedUser1,
                        () -> comparePropertySearcherUsers(userRepository.findById(savedUser2.getId()).get(), savedUser2));

                Long notExistentId = savedUser1.getId() + 100000;
                assertTrue(userRepository.findById(notExistentId).isEmpty());
            });

            it("find user by email", () -> {
                PropertySearcherCustomer propertySearcherCustomer1 = generateAndSavePropertySearcherCustomer();
                PropertySearcherUser propertySearcherUser1 = TestHelper.generatePropertySearcherUser(
                        TestHelper.generateEmail(), propertySearcherCustomer1);
                PropertySearcherUser savedUser1 = userRepository.save(propertySearcherUser1);

                PropertySearcherCustomer propertySearcherCustomer2 = generateAndSavePropertySearcherCustomer();
                PropertySearcherUser propertySearcherUser2 = TestHelper.generatePropertySearcherUser(
                        TestHelper.generateEmail(), propertySearcherCustomer2);
                PropertySearcherUser savedUser2 = userRepository.save(propertySearcherUser2);

                comparePropertySearcherUsers(userRepository.findByEmail(savedUser1.getEmail()), savedUser1);
                comparePropertySearcherUsers(userRepository.findByEmail(savedUser2.getEmail()), savedUser2);

                String notExistentEmail = savedUser1.getEmail() + "_not_exists";
                assertNull(userRepository.findByEmail(notExistentEmail));
            });

            it("find one", () -> {
                PropertySearcherCustomer propertySearcherCustomer1 = generateAndSavePropertySearcherCustomer();
                PropertySearcherUser propertySearcherUser1 = TestHelper.generatePropertySearcherUser(
                        TestHelper.generateEmail(), propertySearcherCustomer1);
                PropertySearcherUser savedUser1 = userRepository.save(propertySearcherUser1);

                PropertySearcherCustomer propertySearcherCustomer2 = generateAndSavePropertySearcherCustomer();
                PropertySearcherUser propertySearcherUser2 = TestHelper.generatePropertySearcherUser(
                        TestHelper.generateEmail(), propertySearcherCustomer2);
                PropertySearcherUser savedUser2 = userRepository.save(propertySearcherUser2);

                comparePropertySearcherUsers(userRepository.findById(savedUser1.getId()).get(), savedUser1);
                comparePropertySearcherUsers(userRepository.findById(savedUser2.getId()).get(), savedUser2);
            });

        });

        describe("delete user", () -> {

            it("delete user by ID - success", () -> {
                PropertySearcherCustomer propertySearcherCustomer1 = generateAndSavePropertySearcherCustomer();
                PropertySearcherUser propertySearcherUser1 = TestHelper.generatePropertySearcherUser(
                        TestHelper.generateEmail(), propertySearcherCustomer1);
                PropertySearcherUser savedUser1 = userRepository.save(propertySearcherUser1);

                PropertySearcherCustomer propertySearcherCustomer2 = generateAndSavePropertySearcherCustomer();
                PropertySearcherUser propertySearcherUser2 = TestHelper.generatePropertySearcherUser(
                        TestHelper.generateEmail(), propertySearcherCustomer2);
                PropertySearcherUser savedUser2 = userRepository.save(propertySearcherUser2);

                comparePropertySearcherUsers(userRepository.findById(savedUser1.getId()).get(), savedUser1);
                comparePropertySearcherUsers(userRepository.findById(savedUser2.getId()).get(), savedUser2);

                userRepository.deleteById(savedUser1.getId());

                assertTrue(userRepository.findById(propertySearcherUser1.getId()).isEmpty());
                comparePropertySearcherUsers(userRepository.findById(propertySearcherUser2.getId()).get(), savedUser2);
            });

            it("delete user - success", () -> {
                PropertySearcherCustomer propertySearcherCustomer1 = generateAndSavePropertySearcherCustomer();
                PropertySearcherUser propertySearcherUser1 = TestHelper.generatePropertySearcherUser(
                        TestHelper.generateEmail(), propertySearcherCustomer1);
                PropertySearcherUser savedUser1 = userRepository.save(propertySearcherUser1);

                PropertySearcherCustomer propertySearcherCustomer2 = generateAndSavePropertySearcherCustomer();
                PropertySearcherUser propertySearcherUser2 = TestHelper.generatePropertySearcherUser(
                        TestHelper.generateEmail(), propertySearcherCustomer2);
                PropertySearcherUser savedUser2 = userRepository.save(propertySearcherUser2);

                comparePropertySearcherUsers(userRepository.findById(propertySearcherUser1.getId()).get(), savedUser1);
                comparePropertySearcherUsers(userRepository.findById(propertySearcherUser2.getId()).get(), savedUser2);

                userRepository.delete(savedUser1);

                assertTrue(userRepository.findById(savedUser1.getId()).isEmpty());
                comparePropertySearcherUsers(userRepository.findById(propertySearcherUser2.getId()).get(), savedUser2);
            });

        });
    }

    private PropertySearcherCustomer generateAndSavePropertySearcherCustomer() {
        PropertySearcherCustomer propertySearcherCustomer = TestHelper.generateCustomer();
        return customerRepository.save(propertySearcherCustomer);
    }

}
