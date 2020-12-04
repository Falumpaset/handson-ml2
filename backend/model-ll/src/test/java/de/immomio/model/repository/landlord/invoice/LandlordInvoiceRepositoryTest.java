package de.immomio.model.repository.landlord.invoice;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.invoice.LandlordInvoice;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.model.BaseDataJpaTest;
import de.immomio.model.repository.landlord.customer.LandlordCustomerRepository;
import de.immomio.model.repository.landlord.customer.user.LandlordUserRepository;
import de.immomio.utils.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.greghaskins.spectrum.Spectrum.it;
import static de.immomio.utils.TestComparatorHelper.compareInvoice;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LandlordInvoiceRepositoryTest extends BaseDataJpaTest {

    @Autowired
    private LandlordInvoiceRepository landlordInvoiceRepository;

    @Autowired
    private LandlordCustomerRepository landlordCustomerRepository;

    @Autowired
    private LandlordUserRepository userRepository;

    {
        describe("create invoice", () ->
                it("created successfully", () -> {
                    LandlordCustomer landlordCustomer = generateAndSaveLandlordCustomer();
                    LandlordInvoice landlordInvoice = TestHelper.generateInvoice(landlordCustomer, 1L);
                    LandlordInvoice savedInvoice = landlordInvoiceRepository.save(landlordInvoice);

                    compareInvoice(landlordInvoice, savedInvoice);
                })
        );

        describe("find invoice", () -> {
            it("find by ID", () -> {
                LandlordCustomer landlordCustomer = generateAndSaveLandlordCustomer();
                LandlordUser landlordUser = generateAndSaveLandlordUser(landlordCustomer);

                LandlordInvoice landlordInvoice1 = TestHelper.generateInvoice(landlordCustomer, 1L);
                LandlordInvoice savedInvoice1 = landlordInvoiceRepository.save(landlordInvoice1);

                LandlordInvoice landlordInvoice2 = TestHelper.generateInvoice(landlordCustomer, 2L);
                LandlordInvoice savedInvoice2 = landlordInvoiceRepository.save(landlordInvoice2);

                withAuthentication(landlordUser, () -> {
                    compareInvoice(landlordInvoiceRepository.findById(savedInvoice1.getId()).get(), savedInvoice1);
                    compareInvoice(landlordInvoiceRepository.findById(savedInvoice2.getId()).get(), savedInvoice2);
                });
            });

            it("find all", () -> {
                LandlordCustomer landlordCustomer1 = generateAndSaveLandlordCustomer();
                LandlordUser landlordUser1 = generateAndSaveLandlordUser(landlordCustomer1);

                LandlordInvoice landlordInvoice1 = TestHelper.generateInvoice(landlordCustomer1, 1L);
                LandlordInvoice savedInvoice1 = landlordInvoiceRepository.save(landlordInvoice1);

                LandlordInvoice landlordInvoice2 = TestHelper.generateInvoice(landlordCustomer1, 2L);
                LandlordInvoice savedInvoice2 = landlordInvoiceRepository.save(landlordInvoice2);

                Pageable pageable = PageRequest.of(INIT_PAGE, ITEMS_PER_PAGE);

                withAuthentication(landlordUser1, () -> {
                    List<LandlordInvoice> invoices = landlordInvoiceRepository.findAll(pageable).getContent();
                    assertEquals(2, invoices.size());

                    compareInvoice(invoices.get(0), savedInvoice2);
                    compareInvoice(invoices.get(1), savedInvoice1);
                });

                LandlordCustomer landlordCustomer2 = generateAndSaveLandlordCustomer();
                LandlordUser landlordUser2 = generateAndSaveLandlordUser(landlordCustomer2);

                LandlordInvoice landlordInvoice3 = TestHelper.generateInvoice(landlordCustomer2, 3L);
                LandlordInvoice savedInvoice3 = landlordInvoiceRepository.save(landlordInvoice3);

                withAuthentication(landlordUser2, () -> {
                    List<LandlordInvoice> invoices = landlordInvoiceRepository.findAll(pageable).getContent();
                    assertEquals(1, invoices.size());

                    compareInvoice(invoices.get(0), savedInvoice3);
                });
            });
        });

        describe("delete invoice", () -> {

            it("delete invoice by ID - success", () -> {
                LandlordCustomer landlordCustomer = generateAndSaveLandlordCustomer();
                LandlordUser landlordUser = generateAndSaveLandlordUser(landlordCustomer);

                LandlordInvoice landlordInvoice1 = TestHelper.generateInvoice(landlordCustomer, 1L);
                LandlordInvoice savedInvoice1 = landlordInvoiceRepository.save(landlordInvoice1);

                LandlordInvoice landlordInvoice2 = TestHelper.generateInvoice(landlordCustomer, 2L);
                LandlordInvoice savedInvoice2 = landlordInvoiceRepository.save(landlordInvoice2);

                withAuthentication(landlordUser, () -> {
                    compareInvoice(landlordInvoiceRepository.findById(savedInvoice1.getId()).get(), savedInvoice1);
                    compareInvoice(landlordInvoiceRepository.findById(savedInvoice2.getId()).get(), savedInvoice2);
                });

                landlordInvoiceRepository.deleteById(savedInvoice1.getId());

                withAuthentication(landlordUser, () -> {
                    assertTrue(landlordInvoiceRepository.findById(savedInvoice1.getId()).isEmpty());

                    compareInvoice(landlordInvoiceRepository.findById(savedInvoice2.getId()).get(), savedInvoice2);
                });
            });

            it("delete invoice - success", () -> {
                LandlordCustomer landlordCustomer = generateAndSaveLandlordCustomer();
                LandlordUser landlordUser = generateAndSaveLandlordUser(landlordCustomer);

                LandlordInvoice landlordInvoice1 = TestHelper.generateInvoice(landlordCustomer, 1L);
                LandlordInvoice savedInvoice1 = landlordInvoiceRepository.save(landlordInvoice1);

                LandlordInvoice landlordInvoice2 = TestHelper.generateInvoice(landlordCustomer, 2L);
                LandlordInvoice savedInvoice2 = landlordInvoiceRepository.save(landlordInvoice2);

                withAuthentication(landlordUser, () -> {
                    compareInvoice(landlordInvoiceRepository.findById(savedInvoice1.getId()).get(), savedInvoice1);
                    compareInvoice(landlordInvoiceRepository.findById(savedInvoice2.getId()).get(), savedInvoice2);
                });

                landlordInvoiceRepository.delete(savedInvoice1);

                withAuthentication(landlordUser, () -> {
                    assertTrue(landlordInvoiceRepository.findById(savedInvoice1.getId()).isEmpty());

                    compareInvoice(landlordInvoiceRepository.findById(savedInvoice2.getId()).get(), savedInvoice2);
                });
            });

            it("delete invoice - invoice not exists", () -> {
                LandlordCustomer landlordCustomer = generateAndSaveLandlordCustomer();
                LandlordUser landlordUser = generateAndSaveLandlordUser(landlordCustomer);

                LandlordInvoice landlordInvoice = TestHelper.generateInvoice(landlordCustomer, 1L);
                LandlordInvoice savedInvoice = landlordInvoiceRepository.save(landlordInvoice);

                Long notExistentId = savedInvoice.getId() + 100000;
                withAuthentication(landlordUser, () -> assertTrue(landlordInvoiceRepository.findById(notExistentId).isEmpty()));

                boolean exception = false;
                try {
                    landlordInvoiceRepository.deleteById(notExistentId);
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

    private LandlordUser generateAndSaveLandlordUser(LandlordCustomer landlordCustomer) {
        LandlordUser landlordUser = TestHelper.generateLandlordUser(TestHelper.generateEmail(), landlordCustomer);
        return userRepository.save(landlordUser);
    }
}
