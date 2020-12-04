package de.immomio.model.repository.propertysearcher.discount;

import de.immomio.data.propertysearcher.entity.discount.PropertySearcherDiscount;
import de.immomio.model.BaseDataJpaTest;
import de.immomio.utils.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import static com.greghaskins.spectrum.Spectrum.it;
import static de.immomio.utils.TestComparatorHelper.comparePropertySearcherDiscount;
import static org.junit.Assert.assertTrue;

public class PropertySearcherDiscountRepositoryTest extends BaseDataJpaTest {

    @Autowired
    private PropertySearcherDiscountRepository discountRepository;

    {
        describe("create discount", () -> it("create discount - success", () -> {
            PropertySearcherDiscount propertySearcherDiscount = TestHelper.generatePropertySearcherDiscount();
            PropertySearcherDiscount savedDiscount = discountRepository.save(propertySearcherDiscount);

            comparePropertySearcherDiscount(propertySearcherDiscount, savedDiscount);
        }));

        describe("find discount", () -> it("find discount by ID", () -> {
            PropertySearcherDiscount propertySearcherDiscount1 = TestHelper.generatePropertySearcherDiscount();
            PropertySearcherDiscount savedDiscount1 = discountRepository.save(propertySearcherDiscount1);

            PropertySearcherDiscount propertySearcherDiscount2 = TestHelper.generatePropertySearcherDiscount();
            PropertySearcherDiscount savedDiscount2 = discountRepository.save(propertySearcherDiscount2);

            comparePropertySearcherDiscount(discountRepository.findById(savedDiscount1.getId()).get(), savedDiscount1);
            comparePropertySearcherDiscount(discountRepository.findById(savedDiscount2.getId()).get(), savedDiscount2);

            Long notExistentId = savedDiscount1.getId() + 100000;
            assertTrue(discountRepository.findById(notExistentId).isEmpty());

        }));

        describe("delete discount", () -> {

            it("delete discount by ID - success", () -> {
                PropertySearcherDiscount propertySearcherDiscount1 = TestHelper.generatePropertySearcherDiscount();
                PropertySearcherDiscount savedDiscount1 = discountRepository.save(propertySearcherDiscount1);

                PropertySearcherDiscount propertySearcherDiscount2 = TestHelper.generatePropertySearcherDiscount();
                PropertySearcherDiscount savedDiscount2 = discountRepository.save(propertySearcherDiscount2);

                comparePropertySearcherDiscount(discountRepository.findById(savedDiscount1.getId()).get(), savedDiscount1);
                comparePropertySearcherDiscount(discountRepository.findById(savedDiscount2.getId()).get(), savedDiscount2);

                discountRepository.deleteById(savedDiscount1.getId());

                assertTrue(discountRepository.findById(savedDiscount1.getId()).isEmpty());
                comparePropertySearcherDiscount(discountRepository.findById(propertySearcherDiscount2.getId()).get(),
                        savedDiscount2);
            });

            it("delete discount - success", () -> {
                PropertySearcherDiscount propertySearcherDiscount1 = TestHelper.generatePropertySearcherDiscount();
                PropertySearcherDiscount savedDiscount1 = discountRepository.save(propertySearcherDiscount1);

                PropertySearcherDiscount propertySearcherDiscount2 = TestHelper.generatePropertySearcherDiscount();
                PropertySearcherDiscount savedDiscount2 = discountRepository.save(propertySearcherDiscount2);

                comparePropertySearcherDiscount(discountRepository.findById(savedDiscount1.getId()).get(), savedDiscount1);
                comparePropertySearcherDiscount(discountRepository.findById(savedDiscount2.getId()).get(), savedDiscount2);

                discountRepository.delete(savedDiscount1);

                assertTrue(discountRepository.findById(savedDiscount1.getId()).isEmpty());
                comparePropertySearcherDiscount(discountRepository.findById(propertySearcherDiscount2.getId()).get(),
                        savedDiscount2);
            });

            it("delete discount - discount not exists", () -> {
                PropertySearcherDiscount propertySearcherDiscount = TestHelper.generatePropertySearcherDiscount();
                PropertySearcherDiscount savedDiscount = discountRepository.save(propertySearcherDiscount);
                Long notExistentId = savedDiscount.getId() + 100000;

                assertTrue(discountRepository.findById(notExistentId).isEmpty());
                boolean exception = false;

                try {
                    discountRepository.deleteById(notExistentId);
                } catch (EmptyResultDataAccessException ex) {
                    exception = true;
                } finally {
                    assertTrue(exception);
                }
            });

        });
    }

}
