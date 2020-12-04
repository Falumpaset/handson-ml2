package de.immomio.model.repository.landlord.email;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.email.LandlordEmail;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.model.BaseDataJpaTest;
import de.immomio.model.repository.landlord.customer.LandlordCustomerRepository;
import de.immomio.model.repository.landlord.customer.user.LandlordUserRepository;
import de.immomio.utils.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.greghaskins.spectrum.Spectrum.it;
import static de.immomio.utils.TestComparatorHelper.compareLandlordEmail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class LandlordEmailRepositoryTest extends BaseDataJpaTest {

    @Autowired
    private LandlordEmailRepository landlordEmailRepository;

    @Autowired
    private LandlordCustomerRepository landlordCustomerRepository;

    @Autowired
    private LandlordUserRepository userRepository;

    private static final int INIT_PAGE = 0;

    private static final int ITEMS_PER_PAGE = 1000;

    {

        describe("create email", () -> it("create email - success", () -> {
            LandlordCustomer landlordCustomer = generateAndSaveLandlordCustomer();
            LandlordUser landlordUser = generateAndSaveLandlordUser(landlordCustomer);
            LandlordEmail landlordEmail = TestHelper.generateLandlordEmail(landlordUser, landlordCustomer);
            LandlordEmail savedEmail = landlordEmailRepository.save(landlordEmail);

            compareLandlordEmail(landlordEmail, savedEmail);
        }));

        describe("find email", () -> {
            it("find email by ID", () -> {
                LandlordCustomer landlordCustomer = generateAndSaveLandlordCustomer();
                LandlordUser landlordUser = generateAndSaveLandlordUser(landlordCustomer);

                LandlordEmail landlordEmail1 = TestHelper.generateLandlordEmail(landlordUser, landlordCustomer);
                LandlordEmail savedEmail1 = landlordEmailRepository.save(landlordEmail1);

                LandlordEmail landlordEmail2 = TestHelper.generateLandlordEmail(landlordUser, landlordCustomer);
                LandlordEmail savedEmail2 = landlordEmailRepository.save(landlordEmail2);

                withAuthentication(landlordUser, () -> {
                    compareLandlordEmail(landlordEmailRepository.findById(savedEmail1.getId()).get(), savedEmail1);
                    compareLandlordEmail(landlordEmailRepository.findById(savedEmail2.getId()).get(), savedEmail2);
                });
            });

            it("find all", () -> {
                LandlordCustomer landlordCustomer = generateAndSaveLandlordCustomer();
                LandlordUser landlordUser1 = generateAndSaveLandlordUser(landlordCustomer);

                LandlordEmail landlordEmail1 = TestHelper.generateLandlordEmail(landlordUser1, landlordCustomer);
                LandlordEmail savedEmail1 = landlordEmailRepository.save(landlordEmail1);

                LandlordEmail landlordEmail2 = TestHelper.generateLandlordEmail(landlordUser1, landlordCustomer);
                LandlordEmail savedEmail2 = landlordEmailRepository.save(landlordEmail2);

                Pageable pageable = PageRequest.of(INIT_PAGE, ITEMS_PER_PAGE);
                withAuthentication(landlordUser1, () -> {
                    List<LandlordEmail> emails = landlordEmailRepository.findAll(pageable).getContent();
                    assertEquals(2, emails.size());

                    compareLandlordEmail(emails.get(0), savedEmail2);
                    compareLandlordEmail(emails.get(1), savedEmail1);
                });

                LandlordUser landlordUser2 = generateAndSaveLandlordUser(landlordCustomer);
                LandlordEmail landlordEmail3 = TestHelper.generateLandlordEmail(landlordUser2, landlordCustomer);
                LandlordEmail savedEmail3 = landlordEmailRepository.save(landlordEmail3);

                withAuthentication(landlordUser2, () -> {
                    List<LandlordEmail> emails = landlordEmailRepository.findAll(pageable).getContent();
                    assertEquals(1, emails.size());

                    compareLandlordEmail(emails.get(0), savedEmail3);
                });
            });
        });

        describe("delete email", () -> {

            it("delete email by ID - success", () -> {
                LandlordCustomer landlordCustomer = generateAndSaveLandlordCustomer();
                LandlordUser landlordUser = generateAndSaveLandlordUser(landlordCustomer);

                LandlordEmail landlordEmail1 = TestHelper.generateLandlordEmail(landlordUser, landlordCustomer);
                LandlordEmail savedEmail1 = landlordEmailRepository.save(landlordEmail1);

                LandlordEmail landlordEmail2 = TestHelper.generateLandlordEmail(landlordUser, landlordCustomer);
                LandlordEmail savedEmail2 = landlordEmailRepository.save(landlordEmail2);

                withAuthentication(landlordUser, () -> {
                    compareLandlordEmail(landlordEmailRepository.findById(savedEmail1.getId()).get(), savedEmail1);
                    compareLandlordEmail(landlordEmailRepository.findById(savedEmail2.getId()).get(), savedEmail2);

                    landlordEmailRepository.deleteById(savedEmail1.getId());

                    assertFalse(landlordEmailRepository.findById(savedEmail1.getId()).isPresent());
                    compareLandlordEmail(landlordEmailRepository.findById(landlordEmail2.getId()).get(), savedEmail2);
                });
            });

            it("delete email - success", () -> {
                LandlordCustomer landlordCustomer = generateAndSaveLandlordCustomer();
                LandlordUser landlordUser = generateAndSaveLandlordUser(landlordCustomer);

                LandlordEmail landlordEmail1 = TestHelper.generateLandlordEmail(landlordUser, landlordCustomer);
                LandlordEmail savedEmail1 = landlordEmailRepository.save(landlordEmail1);

                LandlordEmail landlordEmail2 = TestHelper.generateLandlordEmail(landlordUser, landlordCustomer);
                LandlordEmail savedEmail2 = landlordEmailRepository.save(landlordEmail2);

                withAuthentication(landlordUser, () -> {
                    compareLandlordEmail(landlordEmailRepository.findById(savedEmail1.getId()).get(), savedEmail1);
                    compareLandlordEmail(landlordEmailRepository.findById(savedEmail2.getId()).get(), savedEmail2);

                    landlordEmailRepository.delete(savedEmail1);

                    assertFalse(landlordEmailRepository.findById(savedEmail1.getId()).isPresent());
                    compareLandlordEmail(landlordEmailRepository.findById(landlordEmail2.getId()).get(), savedEmail2);
                });
            });

        });

    }

    private LandlordCustomer generateAndSaveLandlordCustomer() {
        LandlordCustomer landlordCustomer = TestHelper.generateCustomer();
        return landlordCustomerRepository.save(landlordCustomer);
    }

    private LandlordUser generateAndSaveLandlordUser(LandlordCustomer landlordCustomer) {
        LandlordUser landlordUser = TestHelper.generateLandlordUser(TestHelper.generateEmail(), landlordCustomer);
        return userRepository.save(landlordUser);
    }
}
