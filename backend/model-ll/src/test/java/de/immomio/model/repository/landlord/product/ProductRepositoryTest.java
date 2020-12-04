package de.immomio.model.repository.landlord.product;

import de.immomio.data.landlord.entity.product.LandlordProduct;
import de.immomio.model.BaseDataJpaTest;
import de.immomio.utils.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import static com.greghaskins.spectrum.Spectrum.it;
import static de.immomio.utils.TestComparatorHelper.compareLandlordProduct;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ProductRepositoryTest extends BaseDataJpaTest {

    @Autowired
    private LandlordProductRepository landlordProductRepository;

    {
        describe("create product", () -> it("create product - success", () -> {
            LandlordProduct landlordProduct = TestHelper.generateLandlordProduct();
            LandlordProduct savedProduct = landlordProductRepository.save(landlordProduct);

            compareLandlordProduct(landlordProduct, savedProduct);
        }));

        describe("find product", () -> it("find product by ID", () -> {
            LandlordProduct landlordProduct1 = TestHelper.generateLandlordProduct();
            LandlordProduct savedProduct1 = landlordProductRepository.save(landlordProduct1);

            LandlordProduct LandlordProduct2 = TestHelper.generateLandlordProduct();
            LandlordProduct savedProduct2 = landlordProductRepository.save(LandlordProduct2);

            compareLandlordProduct(landlordProductRepository.findById(savedProduct1.getId()).get(), savedProduct1);
            compareLandlordProduct(landlordProductRepository.findById(savedProduct2.getId()).get(), savedProduct2);

            Long notExistentId = savedProduct1.getId() + 100000;
            assertFalse(landlordProductRepository.findById(notExistentId).isPresent());

        }));

        describe("delete product", () -> {

            it("delete product by ID - success", () -> {
                LandlordProduct landlordProduct1 = TestHelper.generateLandlordProduct();
                LandlordProduct savedProduct1 = landlordProductRepository.save(landlordProduct1);

                LandlordProduct LandlordProduct2 = TestHelper.generateLandlordProduct();
                LandlordProduct savedProduct2 = landlordProductRepository.save(LandlordProduct2);

                compareLandlordProduct(landlordProductRepository.findById(savedProduct1.getId()).get(), savedProduct1);
                compareLandlordProduct(landlordProductRepository.findById(savedProduct2.getId()).get(), savedProduct2);

                landlordProductRepository.deleteById(savedProduct1.getId());

                assertFalse(landlordProductRepository.findById(savedProduct1.getId()).isPresent());
                compareLandlordProduct(landlordProductRepository.findById(LandlordProduct2.getId()).get(), savedProduct2);
            });

            it("delete product - success", () -> {
                LandlordProduct landlordProduct1 = TestHelper.generateLandlordProduct();
                LandlordProduct savedProduct1 = landlordProductRepository.save(landlordProduct1);

                LandlordProduct LandlordProduct2 = TestHelper.generateLandlordProduct();
                LandlordProduct savedProduct2 = landlordProductRepository.save(LandlordProduct2);

                compareLandlordProduct(landlordProductRepository.findById(savedProduct1.getId()).get(), savedProduct1);
                compareLandlordProduct(landlordProductRepository.findById(savedProduct2.getId()).get(), savedProduct2);

                landlordProductRepository.delete(savedProduct1);

                assertFalse(landlordProductRepository.findById(savedProduct1.getId()).isPresent());
                compareLandlordProduct(landlordProductRepository.findById(LandlordProduct2.getId()).get(), savedProduct2);
            });

            it("delete product - product not exists", () -> {
                LandlordProduct landlordProduct = TestHelper.generateLandlordProduct();
                LandlordProduct savedProduct = landlordProductRepository.save(landlordProduct);
                Long notExistentId = savedProduct.getId() + 100000;

                assertNull(landlordProductRepository.findById(notExistentId));
                boolean exception = false;

                try {
                    landlordProductRepository.deleteById(notExistentId);
                } catch (EmptyResultDataAccessException ex) {
                    exception = true;
                } finally {
                    assertTrue(exception);
                }
            });

        });
    }
}

