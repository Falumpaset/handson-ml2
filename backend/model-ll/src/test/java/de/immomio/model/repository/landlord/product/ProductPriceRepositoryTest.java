package de.immomio.model.repository.landlord.product;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.price.LandlordPrice;
import de.immomio.data.landlord.entity.product.productprice.LandlordProductPrice;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.model.BaseDataJpaTest;
import de.immomio.model.repository.landlord.price.LandlordPriceRepository;
import de.immomio.model.repository.landlord.product.price.LandlordProductPriceRepository;
import de.immomio.utils.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import static com.greghaskins.spectrum.Spectrum.it;
import static de.immomio.utils.TestComparatorHelper.compareLandlordProductPrice;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ProductPriceRepositoryTest extends BaseDataJpaTest {

    @Autowired
    private LandlordProductPriceRepository landlordProductPriceRepository;

    @Autowired
    private LandlordPriceRepository priceRepository;

    {
        describe("create product price", () -> it("create product price - success", () -> {
            LandlordPrice savedLandlordPrice = generateAndSaveLandlordPrice();
            LandlordProductPrice landlordProductPrice = TestHelper.generateLandlordProductPrice(savedLandlordPrice);
            LandlordProductPrice savedProductPrice = landlordProductPriceRepository.save(landlordProductPrice);

            compareLandlordProductPrice(landlordProductPrice, savedProductPrice);
        }));

        describe("find product price", () -> it("find product price by ID", () -> {
            LandlordPrice savedLandlordPrice = generateAndSaveLandlordPrice();
            LandlordCustomer landlordCustomer = TestHelper.generateCustomer();
            LandlordUser landlordUser = TestHelper.generateLandlordUser(landlordCustomer);
            LandlordProductPrice landlordProductPrice1 = TestHelper.generateLandlordProductPrice(savedLandlordPrice,
                    landlordCustomer);
            LandlordProductPrice savedProductPrice1 = landlordProductPriceRepository.save(landlordProductPrice1);

            LandlordProductPrice landlordProductPrice2 = TestHelper.generateLandlordProductPrice(savedLandlordPrice,
                    landlordCustomer);
            LandlordProductPrice savedProductPrice2 = landlordProductPriceRepository.save(landlordProductPrice2);

            withAuthentication(landlordUser, () -> {
                compareLandlordProductPrice(landlordProductPriceRepository.findById(landlordProductPrice1.getId()).get(),
                        savedProductPrice1);
                compareLandlordProductPrice(landlordProductPriceRepository.findById(savedProductPrice2.getId()).get(),
                        savedProductPrice2);

                Long notExistentId = landlordProductPrice1.getId() + 100000;
                assertTrue(landlordProductPriceRepository.findById(notExistentId).isEmpty());
            });
        }));

        describe("delete product price", () -> {

            it("delete product price by ID - success", () -> {
                LandlordCustomer landlordCustomer = TestHelper.generateCustomer();
                LandlordUser landlordUser = TestHelper.generateLandlordUser(landlordCustomer);

                LandlordPrice savedLandlordPrice = generateAndSaveLandlordPrice();
                LandlordProductPrice landlordProductPrice1 = TestHelper.generateLandlordProductPrice(
                        savedLandlordPrice);
                LandlordProductPrice savedProductPrice1 = landlordProductPriceRepository.save(landlordProductPrice1);

                LandlordProductPrice landlordProductPrice2 = TestHelper.generateLandlordProductPrice(
                        savedLandlordPrice);
                LandlordProductPrice savedProductPrice2 = landlordProductPriceRepository.save(landlordProductPrice2);

                withAuthentication(landlordUser, () -> {
                    compareLandlordProductPrice(landlordProductPriceRepository.findById(landlordProductPrice1.getId()).get(),
                            savedProductPrice1);
                    compareLandlordProductPrice(landlordProductPriceRepository.findById(savedProductPrice2.getId()).get(),
                            savedProductPrice2);

                    landlordProductPriceRepository.deleteById(landlordProductPrice1.getId());

                    assertTrue(landlordProductPriceRepository.findById(landlordProductPrice1.getId()).isEmpty());
                    compareLandlordProductPrice(landlordProductPriceRepository.findById(landlordProductPrice2.getId()).get(),
                            savedProductPrice2);
                });
            });

            it("delete product price - success", () -> {
                LandlordCustomer landlordCustomer = TestHelper.generateCustomer();
                LandlordUser landlordUser = TestHelper.generateLandlordUser(landlordCustomer);
                LandlordPrice savedLandlordPrice = generateAndSaveLandlordPrice();
                LandlordProductPrice landlordProductPrice1 = TestHelper.generateLandlordProductPrice(
                        savedLandlordPrice);
                LandlordProductPrice savedProductPrice1 = landlordProductPriceRepository.save(landlordProductPrice1);

                LandlordProductPrice landlordProductPrice2 = TestHelper.generateLandlordProductPrice(
                        savedLandlordPrice);
                LandlordProductPrice savedProductPrice2 = landlordProductPriceRepository.save(landlordProductPrice2);

                withAuthentication(landlordUser, () -> {
                    compareLandlordProductPrice(landlordProductPriceRepository.findById(landlordProductPrice1.getId()).get(),
                            savedProductPrice1);
                    compareLandlordProductPrice(landlordProductPriceRepository.findById(savedProductPrice2.getId()).get(),
                            savedProductPrice2);

                    landlordProductPriceRepository.delete(landlordProductPrice1);

                    assertNull(landlordProductPriceRepository.findById(landlordProductPrice1.getId()));
                    compareLandlordProductPrice(landlordProductPriceRepository.findById(landlordProductPrice2.getId()).get(),
                            savedProductPrice2);
                });
            });

            it("delete product price - product price not exists", () -> {
                LandlordCustomer landlordCustomer = TestHelper.generateCustomer();
                LandlordUser landlordUser = TestHelper.generateLandlordUser(landlordCustomer);
                LandlordPrice savedLandlordPrice = generateAndSaveLandlordPrice();
                LandlordProductPrice landlordProductPrice = TestHelper.generateLandlordProductPrice(savedLandlordPrice);
                LandlordProductPrice savedProductPrice = landlordProductPriceRepository.save(landlordProductPrice);
                Long notExistentId = savedProductPrice.getId() + 100000;

                withAuthentication(landlordUser, () -> {
                    assertTrue(landlordProductPriceRepository.findById(notExistentId).isEmpty());
                    boolean exception = false;

                    try {
                        landlordProductPriceRepository.deleteById(notExistentId);
                    } catch (EmptyResultDataAccessException ex) {
                        exception = true;
                    } finally {
                        assertTrue(exception);
                    }
                });
            });

        });

    }

    private LandlordPrice generateAndSaveLandlordPrice() {
        LandlordPrice landlordPrice = TestHelper.generateLandlordPrice();
        return priceRepository.save(landlordPrice);
    }

}
