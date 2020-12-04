package de.immomio.model.repository.landlord.customer.credential;

import de.immomio.data.landlord.entity.credential.Credential;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.model.BaseDataJpaTest;
import de.immomio.model.repository.landlord.customer.LandlordCustomerRepository;
import de.immomio.utils.TestComparatorHelper;
import de.immomio.utils.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;

import static com.greghaskins.spectrum.Spectrum.it;
import static org.junit.Assert.assertNull;

public class LandlordCustomerCredentialRepositoryTest extends BaseDataJpaTest {

    @Autowired
    private LandlordCustomerRepository customerRepository;

    @Autowired
    private LandlordCredentialRepository credentialRepository;

    {
        describe("create customer credential", () -> it("save credential", () -> {
            LandlordCustomer savedCustomer = customerRepository.save(TestHelper.generateCustomer());
            Credential credential = TestHelper.generateCredential(savedCustomer);
            Credential savedCredential = this.credentialRepository.save(credential);
            TestComparatorHelper.compareCredentials(credential, savedCredential);
        }));

        describe("find customer credential", () -> {

            it("find credential by ID", () -> {
                LandlordCustomer savedCustomer = customerRepository.save(TestHelper.generateCustomer());
                Credential credential = TestHelper.generateCredential(savedCustomer);
                Credential savedCredential = this.credentialRepository.save(credential);
                TestComparatorHelper.compareCredentials(savedCredential,
                        this.credentialRepository.getOne(savedCredential.getId()));
            });

            it("find by customer and portal2", () -> {
                LandlordCustomer savedCustomer = customerRepository.save(TestHelper.generateCustomer());
                Credential credential = TestHelper.generateCredential(savedCustomer);
                Credential savedCredential = this.credentialRepository.save(credential);

                TestComparatorHelper.compareCredentials(savedCredential,
                        this.credentialRepository.findByCustomerAndPortal(savedCredential.getCustomer(),
                                savedCredential.getPortal()));
            });

            it("no such credential", () -> assertNull(this.credentialRepository.customFindOne(-42L)));
        });

        describe("delete customer credential", () -> it("plain delete", () -> {
            LandlordCustomer savedCustomer = customerRepository.save(TestHelper.generateCustomer());
            Credential credential = TestHelper.generateCredential(savedCustomer);
            Credential savedCredential = this.credentialRepository.save(credential);
            final Long credentialId = savedCredential.getId();

            this.credentialRepository.customDelete(savedCredential);

            assertNull(this.credentialRepository.customFindOne(credentialId));
        }));

    }

}
