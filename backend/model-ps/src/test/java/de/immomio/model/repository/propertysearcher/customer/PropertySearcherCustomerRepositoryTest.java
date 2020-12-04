package de.immomio.model.repository.propertysearcher.customer;

import de.immomio.data.propertysearcher.entity.customer.PropertySearcherCustomer;
import de.immomio.model.BaseDataJpaTest;
import de.immomio.utils.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;

import static com.greghaskins.spectrum.Spectrum.it;
import static de.immomio.utils.TestComparatorHelper.comparePropertySearcherCustomer;
import static org.junit.Assert.assertTrue;

public class PropertySearcherCustomerRepositoryTest extends BaseDataJpaTest {

    @Autowired
    private PropertySearcherCustomerRepository customerRepository;

    {
        describe("create customer", () -> it("create customer - success", () -> {
            PropertySearcherCustomer propertySearcherCustomer = TestHelper.generateCustomer();
            PropertySearcherCustomer savedCustomer = customerRepository.save(propertySearcherCustomer);

            comparePropertySearcherCustomer(propertySearcherCustomer, savedCustomer);
        }));

        describe("find customer", () -> it("find customer by ID", () -> {
            PropertySearcherCustomer propertySearcherCustomer1 = TestHelper.generateCustomer();
            PropertySearcherCustomer savedCustomer1 = customerRepository.save(propertySearcherCustomer1);

            PropertySearcherCustomer propertySearcherCustomer2 = TestHelper.generateCustomer();
            PropertySearcherCustomer savedCustomer2 = customerRepository.save(propertySearcherCustomer2);

            comparePropertySearcherCustomer(customerRepository.findById(savedCustomer1.getId()).get(), savedCustomer1);
            comparePropertySearcherCustomer(customerRepository.findById(savedCustomer2.getId()).get(), savedCustomer2);

            Long notExistentId = savedCustomer1.getId() + 100000;
            assertTrue(customerRepository.findById(notExistentId).isEmpty());

        }));

        describe("delete customer", () -> {

            it("delete customer by ID - success", () -> {
                PropertySearcherCustomer propertySearcherCustomer1 = TestHelper.generateCustomer();
                PropertySearcherCustomer savedCustomer1 = customerRepository.save(propertySearcherCustomer1);

                PropertySearcherCustomer propertySearcherCustomer2 = TestHelper.generateCustomer();
                PropertySearcherCustomer savedCustomer2 = customerRepository.save(propertySearcherCustomer2);

                comparePropertySearcherCustomer(customerRepository.findById(savedCustomer1.getId()).get(), savedCustomer1);
                comparePropertySearcherCustomer(customerRepository.findById(savedCustomer2.getId()).get(), savedCustomer2);

                customerRepository.deleteById(savedCustomer1.getId());

                assertTrue(customerRepository.findById(savedCustomer1.getId()).isEmpty());
                comparePropertySearcherCustomer(customerRepository.findById(propertySearcherCustomer2.getId()).get(),
                        savedCustomer2);
            });

            it("delete customer - success", () -> {
                PropertySearcherCustomer propertySearcherCustomer1 = TestHelper.generateCustomer();
                PropertySearcherCustomer savedCustomer1 = customerRepository.save(propertySearcherCustomer1);

                PropertySearcherCustomer propertySearcherCustomer2 = TestHelper.generateCustomer();
                PropertySearcherCustomer savedCustomer2 = customerRepository.save(propertySearcherCustomer2);

                comparePropertySearcherCustomer(customerRepository.findById(savedCustomer1.getId()).get(), savedCustomer1);
                comparePropertySearcherCustomer(customerRepository.findById(savedCustomer2.getId()).get(), savedCustomer2);

                customerRepository.delete(savedCustomer1);

                assertTrue(customerRepository.findById(savedCustomer1.getId()).isEmpty());
                comparePropertySearcherCustomer(customerRepository.findById(propertySearcherCustomer2.getId()).get(),
                        savedCustomer2);
            });

        });
    }
}
