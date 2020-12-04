package de.immomio.model.repository.landlord.customer.user;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.model.BaseDataJpaTest;
import de.immomio.model.repository.landlord.customer.LandlordCustomerRepository;
import de.immomio.utils.TestHelper;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.TransactionSystemException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

import static com.greghaskins.spectrum.Spectrum.it;
import static de.immomio.utils.TestComparatorHelper.compareLandlordUsers;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LandlordUserRepositoryTest extends BaseDataJpaTest {

    private static final String CUSTOMER_FIELD = "customer";

    private static final String PROFILE_FIELD = "profile";

    private static final String USER_TYPE_FIELD = "usertype";

    private static final String EMAIL_FIELD = "email";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private LandlordUserRepository userRepository;

    @Autowired
    private LandlordCustomerRepository landlordCustomerRepository;

    {
        describe("create user", () -> {

            it("create user - validation failed", () -> {
                try {
                    LandlordUser landlordUser = TestHelper.generateLandlordUser();
                    userRepository.save(landlordUser);
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
                LandlordCustomer landlordCustomer = generateAndSaveLandlordCustomer();

                LandlordUser landlordUser = TestHelper.generateLandlordUser(TestHelper.generateEmail(),
                        landlordCustomer);
                LandlordUser savedUser = userRepository.save(landlordUser);

                compareLandlordUsers(landlordUser, savedUser);
            });

        });

        describe("find user", () -> {

            it("find user by ID", () -> {
                LandlordCustomer landlordCustomer = generateAndSaveLandlordCustomer();

                LandlordUser landlordUser1 = TestHelper.generateLandlordUser(TestHelper.generateEmail(),
                        landlordCustomer);
                LandlordUser savedUser1 = userRepository.save(landlordUser1);

                LandlordUser landlordUser2 = TestHelper.generateLandlordUser(TestHelper.generateEmail(),
                        landlordCustomer);
                LandlordUser savedUser2 = userRepository.save(landlordUser2);

                compareLandlordUsers(userRepository.findById(savedUser1.getId()).get(), savedUser1);
                compareLandlordUsers(userRepository.findById(savedUser2.getId()).get(), savedUser2);

                Long notExistentId = savedUser1.getId() + 100000;
                assertTrue(userRepository.findById(notExistentId).isEmpty());
            });

            it("find user by email", () -> {
                LandlordCustomer landlordCustomer = generateAndSaveLandlordCustomer();

                LandlordUser landlordUser1 = TestHelper.generateLandlordUser(TestHelper.generateEmail(),
                        landlordCustomer);
                LandlordUser savedUser1 = userRepository.save(landlordUser1);

                LandlordUser landlordUser2 = TestHelper.generateLandlordUser(TestHelper.generateEmail(),
                        landlordCustomer);
                LandlordUser savedUser2 = userRepository.save(landlordUser2);

                compareLandlordUsers(userRepository.findByEmail(savedUser1.getEmail()), savedUser1);
                compareLandlordUsers(userRepository.findByEmail(savedUser2.getEmail()), savedUser2);

                String notExistentEmail = savedUser1.getEmail() + "_not_exists";
                assertNull(userRepository.findByEmail(notExistentEmail));
            });

            it("find one", () -> {
                LandlordCustomer landlordCustomer = generateAndSaveLandlordCustomer();

                LandlordUser landlordUser1 = TestHelper.generateLandlordUser(TestHelper.generateEmail(),
                        landlordCustomer);
                LandlordUser savedUser1 = userRepository.save(landlordUser1);

                LandlordUser landlordUser2 = TestHelper.generateLandlordUser(TestHelper.generateEmail(),
                        landlordCustomer);
                LandlordUser savedUser2 = userRepository.save(landlordUser2);

                compareLandlordUsers(userRepository.findById(savedUser1.getId()).get(), savedUser1);
                compareLandlordUsers(userRepository.findById(savedUser2.getId()).get(), savedUser2);
            });

        });

        describe("delete user", () -> {

            it("delete user by ID - success", () -> {
                LandlordCustomer landlordCustomer = generateAndSaveLandlordCustomer();

                LandlordUser landlordUser1 = TestHelper.generateLandlordUser(TestHelper.generateEmail(),
                        landlordCustomer);
                LandlordUser savedUser1 = userRepository.save(landlordUser1);

                LandlordUser landlordUser2 = TestHelper.generateLandlordUser(TestHelper.generateEmail(),
                        landlordCustomer);
                LandlordUser savedUser2 = userRepository.save(landlordUser2);

                compareLandlordUsers(userRepository.findById(savedUser1.getId()).get(), savedUser1);
                compareLandlordUsers(userRepository.findById(savedUser2.getId()).get(), savedUser2);

                userRepository.deleteById(savedUser1.getId());

                assertTrue(userRepository.findById(landlordUser1.getId()).isEmpty());
                compareLandlordUsers(userRepository.findById(landlordUser2.getId()).get(), savedUser2);
            });

            it("delete user - success", () -> {
                LandlordCustomer landlordCustomer = generateAndSaveLandlordCustomer();

                LandlordUser landlordUser1 = TestHelper.generateLandlordUser(TestHelper.generateEmail(),
                        landlordCustomer);
                LandlordUser savedUser1 = userRepository.save(landlordUser1);

                LandlordUser landlordUser2 = TestHelper.generateLandlordUser(TestHelper.generateEmail(),
                        landlordCustomer);
                LandlordUser savedUser2 = userRepository.save(landlordUser2);

                compareLandlordUsers(userRepository.findById(landlordUser1.getId()).get(), savedUser1);
                compareLandlordUsers(userRepository.findById(landlordUser2.getId()).get(), savedUser2);

                userRepository.delete(savedUser1);

                assertTrue(userRepository.findById(savedUser1.getId()).isEmpty());
                compareLandlordUsers(userRepository.findById(landlordUser2.getId()).get(), savedUser2);
            });

            it("delete user - user not exists", () -> {
                LandlordCustomer landlordCustomer = generateAndSaveLandlordCustomer();

                LandlordUser landlordUser = TestHelper.generateLandlordUser(TestHelper.generateEmail(),
                        landlordCustomer);
                LandlordUser savedUser = userRepository.save(landlordUser);
                Long notExistentId = savedUser.getId() + 100000;

                assertTrue(userRepository.findById(notExistentId).isEmpty());
                boolean exception = false;

                try {
                    userRepository.deleteById(notExistentId);
                } catch (EmptyResultDataAccessException ex) {
                    exception = true;
                } finally {
                    assertTrue(exception);
                }
            });

        });
    }

    private LandlordCustomer generateAndSaveLandlordCustomer() {
        LandlordCustomer landlordCustomer = TestHelper.generateCustomer();
        return landlordCustomerRepository.save(landlordCustomer);
    }

}
