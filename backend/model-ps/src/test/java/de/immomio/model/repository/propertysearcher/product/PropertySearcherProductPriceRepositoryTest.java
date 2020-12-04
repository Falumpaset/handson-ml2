package de.immomio.model.repository.propertysearcher.product;

import de.immomio.data.propertysearcher.entity.customer.PropertySearcherCustomer;
import de.immomio.data.propertysearcher.entity.price.PropertySearcherPrice;
import de.immomio.data.propertysearcher.entity.product.price.PropertySearcherProductPrice;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.model.BaseDataJpaTest;
import de.immomio.model.repository.propertysearcher.price.PropertySearcherPriceRepository;
import de.immomio.model.repository.propertysearcher.product.price.PropertySearcherProductPriceRepository;
import de.immomio.utils.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;

import static com.greghaskins.spectrum.Spectrum.it;
import static de.immomio.utils.TestComparatorHelper.comparePropertySearcherProductPrice;
import static org.junit.Assert.assertTrue;

public class PropertySearcherProductPriceRepositoryTest extends BaseDataJpaTest {

    @Autowired
    private PropertySearcherProductPriceRepository productPriceRepository;

    @Autowired
    private PropertySearcherPriceRepository priceRepository;

    {

        describe("create product price", () -> it("create product price - success", () -> {
            PropertySearcherPrice savedPropertySearcherPrice = generateAndSavePropertySearcherPrice();
            PropertySearcherProductPrice landlordProductPrice = TestHelper.generatePropertySearcherProductPrice(
                    savedPropertySearcherPrice);
            PropertySearcherProductPrice savedProductPrice = productPriceRepository.save(landlordProductPrice);

            comparePropertySearcherProductPrice(landlordProductPrice, savedProductPrice);
        }));

        describe("find product price", () -> it("find product price by ID", () -> {
            PropertySearcherCustomer customer = TestHelper.generateCustomer();
            PropertySearcherUser user = TestHelper.generatePropertySearcherUser(customer);

            PropertySearcherPrice savedPropertySearcherPrice = generateAndSavePropertySearcherPrice();
            PropertySearcherProductPrice landlordProductPrice1 = TestHelper.generatePropertySearcherProductPrice(
                    savedPropertySearcherPrice);
            PropertySearcherProductPrice savedProductPrice1 = productPriceRepository.save(landlordProductPrice1);

            PropertySearcherProductPrice landlordProductPrice2 = TestHelper.generatePropertySearcherProductPrice(
                    savedPropertySearcherPrice);
            PropertySearcherProductPrice savedProductPrice2 = productPriceRepository.save(landlordProductPrice2);

            withAuthentication(user, () -> {
                comparePropertySearcherProductPrice(productPriceRepository.findById(landlordProductPrice1.getId()).get(),
                        savedProductPrice1);
                comparePropertySearcherProductPrice(productPriceRepository.findById(savedProductPrice2.getId()).get(),
                        savedProductPrice2);
            });
        }));

        describe("delete product price", () -> {

            it("delete product price by ID - success", () -> {
                PropertySearcherCustomer customer = TestHelper.generateCustomer();
                PropertySearcherUser user = TestHelper.generatePropertySearcherUser(customer);
                PropertySearcherPrice savedPropertySearcherPrice = generateAndSavePropertySearcherPrice();
                PropertySearcherProductPrice landlordProductPrice1 = TestHelper.generatePropertySearcherProductPrice(
                        savedPropertySearcherPrice);
                PropertySearcherProductPrice savedProductPrice1 = productPriceRepository.save(landlordProductPrice1);

                PropertySearcherProductPrice landlordProductPrice2 = TestHelper.generatePropertySearcherProductPrice(
                        savedPropertySearcherPrice);
                PropertySearcherProductPrice savedProductPrice2 = productPriceRepository.save(landlordProductPrice2);

                withAuthentication(user, () -> {
                    comparePropertySearcherProductPrice(productPriceRepository.findById(landlordProductPrice1.getId()).get(),
                            savedProductPrice1);
                    comparePropertySearcherProductPrice(productPriceRepository.findById(savedProductPrice2.getId()).get(),
                            savedProductPrice2);

                    productPriceRepository.deleteById(landlordProductPrice1.getId());

                    assertTrue(productPriceRepository.findById(landlordProductPrice1.getId()).isEmpty());
                    comparePropertySearcherProductPrice(productPriceRepository.findById(landlordProductPrice2.getId()).get(),
                            savedProductPrice2);
                });
            });

            it("delete product price - success", () -> {
                PropertySearcherCustomer customer = TestHelper.generateCustomer();
                PropertySearcherUser user = TestHelper.generatePropertySearcherUser(customer);
                PropertySearcherPrice savedPropertySearcherPrice = generateAndSavePropertySearcherPrice();
                PropertySearcherProductPrice landlordProductPrice1 = TestHelper.generatePropertySearcherProductPrice(
                        savedPropertySearcherPrice);
                PropertySearcherProductPrice savedProductPrice1 = productPriceRepository.save(landlordProductPrice1);

                PropertySearcherProductPrice landlordProductPrice2 = TestHelper.generatePropertySearcherProductPrice(
                        savedPropertySearcherPrice);
                PropertySearcherProductPrice savedProductPrice2 = productPriceRepository.save(landlordProductPrice2);

                withAuthentication(user, () -> {
                    comparePropertySearcherProductPrice(productPriceRepository.findById(landlordProductPrice1.getId()).get(),
                            savedProductPrice1);
                    comparePropertySearcherProductPrice(productPriceRepository.findById(savedProductPrice2.getId()).get(),
                            savedProductPrice2);

                    productPriceRepository.delete(landlordProductPrice1);

                    assertTrue(productPriceRepository.findById(landlordProductPrice1.getId()).isEmpty());
                    comparePropertySearcherProductPrice(productPriceRepository.findById(landlordProductPrice2.getId()).get(),
                            savedProductPrice2);
                });
            });

        });

    }

    private PropertySearcherPrice generateAndSavePropertySearcherPrice() {
        PropertySearcherPrice landlordPrice = TestHelper.generatePropertySearcherPrice();
        return priceRepository.save(landlordPrice);
    }

}
