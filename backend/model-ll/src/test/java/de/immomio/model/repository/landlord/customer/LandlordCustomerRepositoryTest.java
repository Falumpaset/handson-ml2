package de.immomio.model.repository.landlord.customer;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.model.BaseDataJpaTest;
import de.immomio.utils.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import static com.greghaskins.spectrum.Spectrum.it;
import static de.immomio.utils.TestComparatorHelper.compareLandlordCustomer;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LandlordCustomerRepositoryTest extends BaseDataJpaTest {

    @Autowired
    private LandlordCustomerRepository landlordCustomerRepository;

    {
        describe("create customer", () -> it("create customer - success", () -> {
            LandlordCustomer landlordCustomer = TestHelper.generateCustomer();
            LandlordCustomer savedCustomer = landlordCustomerRepository.save(landlordCustomer);

            compareLandlordCustomer(landlordCustomer, savedCustomer);
        }));

        describe("find customer", () -> it("find customer by ID", () -> {
            LandlordCustomer landlordCustomer1 = TestHelper.generateCustomer();
            LandlordCustomer savedCustomer1 = landlordCustomerRepository.save(landlordCustomer1);

            LandlordCustomer landlordCustomer2 = TestHelper.generateCustomer();
            LandlordCustomer savedCustomer2 = landlordCustomerRepository.save(landlordCustomer2);

            compareLandlordCustomer(landlordCustomerRepository.findById(savedCustomer1.getId()).get(), savedCustomer1);
            compareLandlordCustomer(landlordCustomerRepository.findById(savedCustomer2.getId()).get(), savedCustomer2);

            Long notExistentId = savedCustomer1.getId() + 100000;
            assertTrue(landlordCustomerRepository.findById(notExistentId).isEmpty());

        }));

        describe("delete customer", () -> {

            it("delete customer by ID - success", () -> {
                LandlordCustomer landlordCustomer1 = TestHelper.generateCustomer();
                LandlordCustomer savedCustomer1 = landlordCustomerRepository.save(landlordCustomer1);

                LandlordCustomer landlordCustomer2 = TestHelper.generateCustomer();
                LandlordCustomer savedCustomer2 = landlordCustomerRepository.save(landlordCustomer2);

                compareLandlordCustomer(landlordCustomerRepository.findById(savedCustomer1.getId()).get(), savedCustomer1);
                compareLandlordCustomer(landlordCustomerRepository.findById(savedCustomer2.getId()).get(), savedCustomer2);

                landlordCustomerRepository.deleteById(savedCustomer1.getId());

                assertTrue(landlordCustomerRepository.findById(savedCustomer1.getId()).isEmpty());
                compareLandlordCustomer(landlordCustomerRepository.findById(landlordCustomer2.getId()).get(), savedCustomer2);
            });

            it("delete customer - success", () -> {
                LandlordCustomer landlordCustomer1 = TestHelper.generateCustomer();
                LandlordCustomer savedCustomer1 = landlordCustomerRepository.save(landlordCustomer1);

                LandlordCustomer landlordCustomer2 = TestHelper.generateCustomer();
                LandlordCustomer savedCustomer2 = landlordCustomerRepository.save(landlordCustomer2);

                compareLandlordCustomer(landlordCustomerRepository.findById(savedCustomer1.getId()).get(), savedCustomer1);
                compareLandlordCustomer(landlordCustomerRepository.findById(savedCustomer2.getId()).get(), savedCustomer2);

                landlordCustomerRepository.delete(savedCustomer1);

                assertTrue(landlordCustomerRepository.findById(savedCustomer1.getId()).isEmpty());
                compareLandlordCustomer(landlordCustomerRepository.findById(landlordCustomer2.getId()).get(), savedCustomer2);
            });

            it("delete customer - customer not exists", () -> {
                LandlordCustomer landlordCustomer = TestHelper.generateCustomer();
                LandlordCustomer savedCustomer = landlordCustomerRepository.save(landlordCustomer);
                Long notExistentId = savedCustomer.getId() + 100000;

                assertNull(landlordCustomerRepository.findById(notExistentId));
                boolean exception = false;

                try {
                    landlordCustomerRepository.deleteById(notExistentId);
                } catch (EmptyResultDataAccessException ex) {
                    exception = true;
                } finally {
                    assertTrue(exception);
                }
            });

        });
    }
}
