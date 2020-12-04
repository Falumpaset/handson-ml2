package de.immomio.model.repository.landlord.price;

import de.immomio.data.landlord.entity.price.LandlordPrice;
import de.immomio.model.BaseDataJpaTest;
import de.immomio.utils.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import static com.greghaskins.spectrum.Spectrum.it;
import static de.immomio.utils.TestComparatorHelper.compareLandlordPrice;
import static org.junit.Assert.assertTrue;

public class PriceRepositoryTest extends BaseDataJpaTest {

    @Autowired
    private LandlordPriceRepository priceRepository;

    {
        describe("create price", () -> it("create price - success", () -> {
            LandlordPrice landlordPrice = TestHelper.generateLandlordPrice();
            LandlordPrice savedPrice = priceRepository.save(landlordPrice);

            compareLandlordPrice(landlordPrice, savedPrice);
        }));

        describe("find price", () -> it("find price by ID", () -> {
            LandlordPrice landlordPrice1 = TestHelper.generateLandlordPrice();
            LandlordPrice savedPrice1 = priceRepository.save(landlordPrice1);

            LandlordPrice landlordPrice2 = TestHelper.generateLandlordPrice();
            LandlordPrice savedPrice2 = priceRepository.save(landlordPrice2);

            compareLandlordPrice(priceRepository.findById(savedPrice1.getId()).get(), savedPrice1);
            compareLandlordPrice(priceRepository.findById(savedPrice2.getId()).get(), savedPrice2);

            Long notExistentId = savedPrice1.getId() + 100000;
            assertTrue(priceRepository.findById(notExistentId).isEmpty());

        }));

        describe("delete price", () -> {

            it("delete price by ID - success", () -> {
                LandlordPrice landlordPrice1 = TestHelper.generateLandlordPrice();
                LandlordPrice savedPrice1 = priceRepository.save(landlordPrice1);

                LandlordPrice landlordPrice2 = TestHelper.generateLandlordPrice();
                LandlordPrice savedPrice2 = priceRepository.save(landlordPrice2);

                compareLandlordPrice(priceRepository.findById(savedPrice1.getId()).get(), savedPrice1);
                compareLandlordPrice(priceRepository.findById(savedPrice2.getId()).get(), savedPrice2);

                priceRepository.deleteById(savedPrice1.getId());

                assertTrue(priceRepository.findById(savedPrice1.getId()).isEmpty());
                compareLandlordPrice(priceRepository.findById(landlordPrice2.getId()).get(), savedPrice2);
            });

            it("delete price - success", () -> {
                LandlordPrice landlordPrice1 = TestHelper.generateLandlordPrice();
                LandlordPrice savedPrice1 = priceRepository.save(landlordPrice1);

                LandlordPrice landlordPrice2 = TestHelper.generateLandlordPrice();
                LandlordPrice savedPrice2 = priceRepository.save(landlordPrice2);

                compareLandlordPrice(priceRepository.findById(savedPrice1.getId()).get(), savedPrice1);
                compareLandlordPrice(priceRepository.findById(savedPrice2.getId()).get(), savedPrice2);

                priceRepository.delete(savedPrice1);

                assertTrue(priceRepository.findById(savedPrice1.getId()).isEmpty());
                compareLandlordPrice(priceRepository.findById(landlordPrice2.getId()).get(), savedPrice2);
            });

            it("delete price - price not exists", () -> {
                LandlordPrice landlordPrice = TestHelper.generateLandlordPrice();
                LandlordPrice savedPrice = priceRepository.save(landlordPrice);
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
