package de.immomio.model.repository.landlord.discount;

import de.immomio.data.landlord.entity.discount.LandlordDiscount;
import de.immomio.model.BaseDataJpaTest;
import de.immomio.utils.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.greghaskins.spectrum.Spectrum.it;
import static de.immomio.utils.TestComparatorHelper.compareLandlordDiscount;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DiscountRepositoryTest extends BaseDataJpaTest {

    @Autowired
    private LandlordDiscountRepository discountRepository;

    {
        describe("create discount", () -> it("create discount - success", () -> {
            LandlordDiscount landlordDiscount = TestHelper.generateLandlordDiscount();
            LandlordDiscount savedDiscount = discountRepository.save(landlordDiscount);

            compareLandlordDiscount(landlordDiscount, savedDiscount);
        }));

        describe("find discount", () -> {
            it("find discount by ID", () -> {
                LandlordDiscount landlordDiscount1 = TestHelper.generateLandlordDiscount();
                LandlordDiscount savedDiscount1 = discountRepository.save(landlordDiscount1);

                LandlordDiscount landlordDiscount2 = TestHelper.generateLandlordDiscount();
                LandlordDiscount savedDiscount2 = discountRepository.save(landlordDiscount2);

                compareLandlordDiscount(discountRepository.findById(savedDiscount1.getId()).get(), savedDiscount1);
                compareLandlordDiscount(discountRepository.findById(savedDiscount2.getId()).get(), savedDiscount2);

                Long notExistentId = savedDiscount1.getId() + 100000;
                assertFalse(discountRepository.findById(notExistentId).isPresent());
            });

            it("find all", () -> {
                LandlordDiscount landlordDiscount1 = TestHelper.generateLandlordDiscount();
                LandlordDiscount savedDiscount1 = discountRepository.save(landlordDiscount1);

                LandlordDiscount landlordDiscount2 = TestHelper.generateLandlordDiscount();
                LandlordDiscount savedDiscount2 = discountRepository.save(landlordDiscount2);

                Pageable pageable = PageRequest.of(INIT_PAGE, ITEMS_PER_PAGE);

                List<LandlordDiscount> discounts = discountRepository.findAll(pageable).getContent();
                assertEquals(2, discounts.size());

                compareLandlordDiscount(discounts.get(0), savedDiscount1);
                compareLandlordDiscount(discounts.get(1), savedDiscount2);

                pageable = PageRequest.of(INIT_PAGE, 1);

                discounts = discountRepository.findAll(pageable).getContent();
                assertEquals(1, discounts.size());
                compareLandlordDiscount(discounts.get(0), savedDiscount1);

                pageable = PageRequest.of(INIT_PAGE + 1, ITEMS_PER_PAGE);
                discounts = discountRepository.findAll(pageable).getContent();
                assertEquals(0, discounts.size());
            });
        });

        describe("delete discount", () -> {

            it("delete discount by ID - success", () -> {
                LandlordDiscount landlordDiscount1 = TestHelper.generateLandlordDiscount();
                LandlordDiscount savedDiscount1 = discountRepository.save(landlordDiscount1);

                LandlordDiscount landlordDiscount2 = TestHelper.generateLandlordDiscount();
                LandlordDiscount savedDiscount2 = discountRepository.save(landlordDiscount2);

                compareLandlordDiscount(discountRepository.findById(savedDiscount1.getId()).get(), savedDiscount1);
                compareLandlordDiscount(discountRepository.findById(savedDiscount2.getId()).get(), savedDiscount2);

                discountRepository.deleteById(savedDiscount1.getId());

                assertFalse(discountRepository.findById(savedDiscount1.getId()).isPresent());
                compareLandlordDiscount(discountRepository.findById(landlordDiscount2.getId()).get(), savedDiscount2);
            });

            it("delete discount - success", () -> {
                LandlordDiscount landlordDiscount1 = TestHelper.generateLandlordDiscount();
                LandlordDiscount savedDiscount1 = discountRepository.save(landlordDiscount1);

                LandlordDiscount landlordDiscount2 = TestHelper.generateLandlordDiscount();
                LandlordDiscount savedDiscount2 = discountRepository.save(landlordDiscount2);

                compareLandlordDiscount(discountRepository.findById(savedDiscount1.getId()).get(), savedDiscount1);
                compareLandlordDiscount(discountRepository.findById(savedDiscount2.getId()).get(), savedDiscount2);

                discountRepository.delete(savedDiscount1);

                assertFalse(discountRepository.findById(savedDiscount1.getId()).isPresent());
                compareLandlordDiscount(discountRepository.findById(landlordDiscount2.getId()).get(), savedDiscount2);
            });

            it("delete discount - discount not exists", () -> {
                LandlordDiscount landlordDiscount = TestHelper.generateLandlordDiscount();
                LandlordDiscount savedDiscount = discountRepository.save(landlordDiscount);
                Long notExistentId = savedDiscount.getId() + 100000;

                assertFalse(discountRepository.findById(notExistentId).isPresent());
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
