package de.immomio.model.repository.landlord.property;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.prioset.Prioset;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.shared.bean.common.GeoCoordinates;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.model.BaseDataJpaTest;
import de.immomio.model.repository.landlord.customer.LandlordCustomerRepository;
import de.immomio.model.repository.landlord.customer.prioset.LandlordPriosetRepository;
import de.immomio.model.repository.landlord.customer.user.LandlordUserRepository;
import de.immomio.model.repository.shared.property.PropertyRepository;
import de.immomio.utils.TestComparatorHelper;
import de.immomio.utils.TestHelper;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static com.greghaskins.spectrum.Spectrum.it;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Niklas Lindemann
 */

public class PropertyRepositoryTest extends BaseDataJpaTest {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private LandlordCustomerRepository landlordCustomerRepository;

    @Autowired
    private LandlordPriosetRepository priosetRepository;

    @Autowired
    private LandlordUserRepository landlordUserRepository;

    private static final String ATTACHMENT_1 = "ATTACHMENT1";

    private static final String ATTACHMENT_2 = "ATTACHMENT2";

    {
        describe("create property", () -> it("is successful", () -> {
            LandlordCustomer savedCustomer = landlordCustomerRepository.save(TestHelper.generateCustomer());
            Property property = TestHelper.generateProperty(savedCustomer);
            Property saved = propertyRepository.save(property);
            TestComparatorHelper.compareProperty(saved, property);
            Assert.assertNotNull(saved);
        }));

        describe("find property by attachment", () -> {
            it("which ist used", () -> {
                LandlordCustomer savedCustomer = landlordCustomerRepository.save(TestHelper.generateCustomer());
                Property propertyUsesAttachment = saveProperty(ATTACHMENT_1, savedCustomer, "Testproperty");
                Property propertyNotUsesAttachment = saveProperty(ATTACHMENT_2, savedCustomer, "Testproperty2");
                Property propertyUsesAttachment2 = saveProperty(ATTACHMENT_1, savedCustomer, "Testproperty3");

                List<Property> allUsingAttachment = propertyRepository.findAllUsingAttachment(savedCustomer.getId(), ATTACHMENT_1);
                Assert.assertTrue(allUsingAttachment.containsAll(Arrays.asList(propertyUsesAttachment,propertyUsesAttachment2)));
                Assert.assertFalse(allUsingAttachment.contains(propertyNotUsesAttachment));
            });

            it("which is not used", () ->{
                LandlordCustomer savedCustomer = landlordCustomerRepository.save(TestHelper.generateCustomer());
                Property property1 = saveProperty(ATTACHMENT_1, savedCustomer, "Testproperty");
                Property property2 = saveProperty(ATTACHMENT_1, savedCustomer, "Testproperty2");
                Property property3 = saveProperty(ATTACHMENT_1, savedCustomer, "Testproperty3");

                List<Property> properties = propertyRepository.findAll();
                Assert.assertTrue(properties.containsAll(Arrays.asList(property1,property2, property3)));

                List<Property> allUsingAttachment = propertyRepository.findAllUsingAttachment(savedCustomer.getId(), ATTACHMENT_2);
                Assert.assertTrue(allUsingAttachment.isEmpty());
            });
        });

        describe("find property by users", () -> {
            it("users exist", () -> {
                LandlordCustomer customer = landlordCustomerRepository.save(TestHelper.generateCustomer());
                LandlordUser landlordUser1 = landlordUserRepository.save(TestHelper.generateLandlordUser(customer));
                LandlordUser landlordUser2 = landlordUserRepository.save(TestHelper.generateLandlordUser(customer));

                Property property1 = saveProperty(landlordUser1, ATTACHMENT_1, customer, "Testproperty");
                Property property2 = saveProperty(landlordUser2, ATTACHMENT_1, customer, "Testproperty2");
                Property property3 = saveProperty(landlordUser1, ATTACHMENT_1, customer, "Testproperty3");

                Pageable pageable = PageRequest.of(INIT_PAGE, ITEMS_PER_PAGE);

                withAuthentication(landlordUser1, () -> {
                    List<Property> properties = propertyRepository.findByUsers(new LandlordUser[]{landlordUser1},
                            pageable).getContent();
                    assertEquals(2, properties.size());

                    TestComparatorHelper.compareProperty(properties.get(0), property3);
                    TestComparatorHelper.compareProperty(properties.get(1), property1);
                });

                withAuthentication(landlordUser2, () -> {
                    List<Property> properties = propertyRepository.findByUsers(new LandlordUser[]{landlordUser2},
                            pageable).getContent();
                    assertEquals(1, properties.size());

                    TestComparatorHelper.compareProperty(properties.get(0), property2);
                });
            });

            it("users not exist", () -> {
                LandlordCustomer customer = landlordCustomerRepository.save(TestHelper.generateCustomer());

                LandlordUser landlordUser1 = landlordUserRepository.save(TestHelper.generateLandlordUser(customer));
                LandlordUser landlordUser2 = landlordUserRepository.save(TestHelper.generateLandlordUser(customer));
                saveProperty(landlordUser1, ATTACHMENT_1, customer, "Testproperty");

                Pageable pageable = PageRequest.of(INIT_PAGE, ITEMS_PER_PAGE);

                withAuthentication(landlordUser2, () -> {
                    List<Property> properties = propertyRepository.findByUsers(new LandlordUser[]{landlordUser2},
                            pageable).getContent();
                    assertTrue(properties.isEmpty());
                });
            });
        });

        describe("find property by customer", () -> {
            it("customer exists", () -> {
                LandlordCustomer customer1 = landlordCustomerRepository.save(TestHelper.generateCustomer());
                LandlordCustomer customer2 = landlordCustomerRepository.save(TestHelper.generateCustomer());

                LandlordUser landlordUser1 = landlordUserRepository.save(TestHelper.generateLandlordUser(customer1));
                LandlordUser landlordUser2 = landlordUserRepository.save(TestHelper.generateLandlordUser(customer2));

                Property property1 = saveProperty(landlordUser1, ATTACHMENT_1, customer1, "Testproperty");
                Property property2 = saveProperty(landlordUser2, ATTACHMENT_1, customer2, "Testproperty2");
                Property property3 = saveProperty(landlordUser1, ATTACHMENT_1, customer1, "Testproperty3");

                Pageable pageable = PageRequest.of(INIT_PAGE, ITEMS_PER_PAGE);

                List<Property> properties = propertyRepository.findByCustomer(customer1, pageable).getContent();
                assertEquals(2, properties.size());

                TestComparatorHelper.compareProperty(properties.get(0), property1);
                TestComparatorHelper.compareProperty(properties.get(1), property3);

                properties = propertyRepository.findByCustomer(customer2, pageable).getContent();
                assertEquals(1, properties.size());
                TestComparatorHelper.compareProperty(properties.get(0), property2);
            });

            it("customer does not exist", () -> {
                LandlordCustomer customer1 = landlordCustomerRepository.save(TestHelper.generateCustomer());
                LandlordCustomer customer2 = landlordCustomerRepository.save(TestHelper.generateCustomer());

                LandlordUser landlordUser1 = landlordUserRepository.save(TestHelper.generateLandlordUser(customer1));
                landlordUserRepository.save(TestHelper.generateLandlordUser(customer1));
                saveProperty(landlordUser1, ATTACHMENT_1, customer1, "Testproperty");

                Pageable pageable = PageRequest.of(INIT_PAGE, ITEMS_PER_PAGE);

                List<Property> properties = propertyRepository.findByCustomer(customer2, pageable).getContent();
                assertTrue(properties.isEmpty());
            });
        });

        describe("find by customerId and externalId", () -> {
            it("externalId not null", () -> {
                LandlordCustomer customer1 = landlordCustomerRepository.save(TestHelper.generateCustomer());
                LandlordCustomer customer2 = landlordCustomerRepository.save(TestHelper.generateCustomer());
                LandlordUser landlordUser1 = landlordUserRepository.save(TestHelper.generateLandlordUser(customer1));
                LandlordUser landlordUser2 = landlordUserRepository.save(TestHelper.generateLandlordUser(customer1));
                LandlordUser landlordUser3 = landlordUserRepository.save(TestHelper.generateLandlordUser(customer2));

                String externalId1 = "1";
                String externalId2 = "2";
                Property property1 = saveProperty(landlordUser1, ATTACHMENT_1, customer1, "Testproperty", externalId1);
                Property property2 = saveProperty(landlordUser2, ATTACHMENT_1, customer1, "Testproperty2", externalId2);
                Property property3 = saveProperty(landlordUser1, ATTACHMENT_1, customer1, "Testproperty3", externalId1);
                Property property4 = saveProperty(landlordUser3, ATTACHMENT_1, customer2, "Testproperty4", externalId1);

                withAuthentication(landlordUser1, () -> {
                    List<Property> properties = propertyRepository.findByCustomerIdAndExternalId(customer1.getId(),
                                                                                                 externalId1);
                    assertEquals(2, properties.size());

                    TestComparatorHelper.compareProperty(properties.get(0), property1);
                    TestComparatorHelper.compareProperty(properties.get(1), property3);
                });

                withAuthentication(landlordUser2, () -> {
                    List<Property> properties = propertyRepository.findByCustomerIdAndExternalId(customer1.getId(),
                                                                                                 externalId2);
                    assertEquals(1, properties.size());

                    TestComparatorHelper.compareProperty(properties.get(0), property2);
                });

                withAuthentication(landlordUser3, () -> {
                    List<Property> properties = propertyRepository.findByCustomerIdAndExternalId(customer2.getId(),
                                                                                                 externalId1);
                    assertEquals(1, properties.size());

                    TestComparatorHelper.compareProperty(properties.get(0), property4);
                });
            });

            it("customer does not exist", () -> {
                LandlordCustomer customer1 = landlordCustomerRepository.save(TestHelper.generateCustomer());
                LandlordCustomer customer2 = landlordCustomerRepository.save(TestHelper.generateCustomer());

                String externalId1 = "1";

                LandlordUser landlordUser1 = landlordUserRepository.save(TestHelper.generateLandlordUser(customer1));
                saveProperty(landlordUser1, ATTACHMENT_1, customer1, "Testproperty");

                List<Property> properties = propertyRepository.findByCustomerIdAndExternalId(customer2.getId(),
                                                                                             externalId1);
                assertTrue(properties.isEmpty());
            });

        });

        describe("count by customer", () -> {
            it("customer exists", () -> {
                LandlordCustomer customer1 = landlordCustomerRepository.save(TestHelper.generateCustomer());
                LandlordCustomer customer2 = landlordCustomerRepository.save(TestHelper.generateCustomer());

                LandlordUser landlordUser1 = landlordUserRepository.save(TestHelper.generateLandlordUser(customer1));
                LandlordUser landlordUser2 = landlordUserRepository.save(TestHelper.generateLandlordUser(customer2));

                saveProperty(landlordUser1, ATTACHMENT_1, customer1, "Testproperty");
                saveProperty(landlordUser2, ATTACHMENT_1, customer2, "Testproperty2");
                saveProperty(landlordUser1, ATTACHMENT_1, customer1, "Testproperty3");

                withAuthentication(landlordUser1, () -> {
                    Long count = propertyRepository.countByCustomer(customer1);
                    assertEquals(2, (long) count);
                });

                withAuthentication(landlordUser2, () -> {
                    Long count = propertyRepository.countByCustomer(customer2);
                    assertEquals(1, (long) count);
                });
            });

            it("customer does not exist", () -> {
                LandlordCustomer customer1 = landlordCustomerRepository.save(TestHelper.generateCustomer());
                LandlordCustomer customer2 = landlordCustomerRepository.save(TestHelper.generateCustomer());

                LandlordUser landlordUser1 = landlordUserRepository.save(TestHelper.generateLandlordUser(customer1));
                LandlordUser landlordUser2 = landlordUserRepository.save(TestHelper.generateLandlordUser(customer2));

                saveProperty(landlordUser1, ATTACHMENT_1, customer1, "Testproperty");

                withAuthentication(landlordUser2, () -> {
                    Long count = propertyRepository.countByCustomer(customer2);
                    assertEquals(0, (long) count);
                });
            });
        });
    }

    private Property saveProperty(String attachmentId, LandlordCustomer customer, String name) {
        LandlordUser savedUser = landlordUserRepository.save(TestHelper.generateLandlordUser(customer));
        return saveProperty(savedUser, attachmentId, customer, name);
    }

    private Property saveProperty(
            LandlordUser landlordUser,
            String attachmentId,
            LandlordCustomer customer,
            String name
    ) {
        return saveProperty(landlordUser, attachmentId, customer, name, null);
    }

    private Property saveProperty(
            LandlordUser landlordUser,
            String attachmentId,
            LandlordCustomer customer,
            String name,
            String externalId
    ) {
        Property property = TestHelper.generateProperty(customer);
        Prioset prioset = TestHelper.generatePrioset(customer);
        Prioset savedPrioset = priosetRepository.save(prioset);
        S3File s3File = TestHelper.generateS3File(attachmentId);

        property.getData().setName(name);
        property.getData().getAttachments().add(s3File);
        property.setPrioset(savedPrioset);
        property.setUser(landlordUser);
        property.setExternalId(externalId);

        return propertyRepository.save(property);
    }

    private GeoCoordinates getGeoCoordinates(Property property) {
        return property.getData().getAddress().getCoordinates();
    }

}
