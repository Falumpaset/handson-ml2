package de.immomio.model.repository.propertysearcher.price;

import de.immomio.data.propertysearcher.entity.price.PropertySearcherPrice;
import de.immomio.model.BaseDataJpaTest;
import de.immomio.utils.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import static com.greghaskins.spectrum.Spectrum.it;
import static de.immomio.utils.TestComparatorHelper.comparePropertySearcherPrice;
import static org.junit.Assert.assertTrue;

public class PropertySearcherPriceRepositoryTest extends BaseDataJpaTest {

    @Autowired
    private PropertySearcherPriceRepository priceRepository;

    {
        describe("create price", () -> it("create price - success", () -> {
            PropertySearcherPrice landlordPrice = TestHelper.generatePropertySearcherPrice();
            PropertySearcherPrice savedPrice = priceRepository.save(landlordPrice);

            comparePropertySearcherPrice(landlordPrice, savedPrice);
        }));

        describe("find price", () -> it("find price by ID", () -> {
            PropertySearcherPrice landlordPrice1 = TestHelper.generatePropertySearcherPrice();
            PropertySearcherPrice savedPrice1 = priceRepository.save(landlordPrice1);

            PropertySearcherPrice landlordPrice2 = TestHelper.generatePropertySearcherPrice();
            PropertySearcherPrice savedPrice2 = priceRepository.save(landlordPrice2);

            comparePropertySearcherPrice(priceRepository.findById(savedPrice1.getId()).get(), savedPrice1);
            comparePropertySearcherPrice(priceRepository.findById(savedPrice2.getId()).get(), savedPrice2);

            Long notExistentId = savedPrice1.getId() + 100000;
            assertTrue(priceRepository.findById(notExistentId).isEmpty());

        }));

        describe("delete price", () -> {

            it("delete price by ID - success", () -> {
                PropertySearcherPrice landlordPrice1 = TestHelper.generatePropertySearcherPrice();
                PropertySearcherPrice savedPrice1 = priceRepository.save(landlordPrice1);

                PropertySearcherPrice landlordPrice2 = TestHelper.generatePropertySearcherPrice();
                PropertySearcherPrice savedPrice2 = priceRepository.save(landlordPrice2);

                comparePropertySearcherPrice(priceRepository.findById(savedPrice1.getId()).get(), savedPrice1);
                comparePropertySearcherPrice(priceRepository.findById(savedPrice2.getId()).get(), savedPrice2);

                priceRepository.deleteById(savedPrice1.getId());

                assertTrue(priceRepository.findById(savedPrice1.getId()).isEmpty());
                comparePropertySearcherPrice(priceRepository.findById(landlordPrice2.getId()).get(), savedPrice2);
            });

            it("delete price - success", () -> {
                PropertySearcherPrice landlordPrice1 = TestHelper.generatePropertySearcherPrice();
                PropertySearcherPrice savedPrice1 = priceRepository.save(landlordPrice1);

                PropertySearcherPrice landlordPrice2 = TestHelper.generatePropertySearcherPrice();
                PropertySearcherPrice savedPrice2 = priceRepository.save(landlordPrice2);

                comparePropertySearcherPrice(priceRepository.findById(savedPrice1.getId()).get(), savedPrice1);
                comparePropertySearcherPrice(priceRepository.findById(savedPrice2.getId()).get(), savedPrice2);

                priceRepository.delete(savedPrice1);

                assertTrue(priceRepository.findById(savedPrice1.getId()).isEmpty());
                comparePropertySearcherPrice(priceRepository.findById(landlordPrice2.getId()).get(), savedPrice2);
            });

            it("delete price - price not exists", () -> {
                PropertySearcherPrice landlordPrice = TestHelper.generatePropertySearcherPrice();
                PropertySearcherPrice savedPrice = priceRepository.save(landlordPrice);
                Long notExistentId = savedPrice.getId() + 100000;

                assertTrue(priceRepository.findById(notExistentId).isEmpty());
                boolean exception = false;

                try {
                    priceRepository.deleteById(notExistentId);
                } catch (EmptyResultDataAccessException ex) {
                    exception = true;
                } finally {
                    assertTrue(exception);
                }
            });

        });
    }

}
