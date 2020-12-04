package de.immomio.model.repository.propertysearcher.product;

import de.immomio.data.propertysearcher.entity.product.PropertySearcherProduct;
import de.immomio.model.BaseDataJpaTest;
import de.immomio.utils.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;

import static com.greghaskins.spectrum.Spectrum.it;
import static de.immomio.utils.TestComparatorHelper.comparePropertySearcherProduct;
import static org.junit.Assert.assertTrue;

public class PropertySearcherProductRepositoryTest extends BaseDataJpaTest {

    @Autowired
    private PropertySearcherProductRepository productRepository;

    {
        describe("create product", () -> it("create product - success", () -> {
            PropertySearcherProduct landlordProduct = TestHelper.generatePropertySearcherProduct();
            PropertySearcherProduct savedProduct = productRepository.save(landlordProduct);

            comparePropertySearcherProduct(landlordProduct, savedProduct);
        }));

        describe("find product", () -> it("find product by ID", () -> {
            PropertySearcherProduct landlordProduct1 = TestHelper.generatePropertySearcherProduct();
            PropertySearcherProduct savedProduct1 = productRepository.save(landlordProduct1);

            PropertySearcherProduct PropertySearcherProduct2 = TestHelper.generatePropertySearcherProduct();
            PropertySearcherProduct savedProduct2 = productRepository.save(PropertySearcherProduct2);

            comparePropertySearcherProduct(productRepository.findById(savedProduct1.getId()).get(), savedProduct1);
            comparePropertySearcherProduct(productRepository.findById(savedProduct2.getId()).get(), savedProduct2);
        }));

        describe("delete product", () -> {

            it("delete product by ID - success", () -> {
                PropertySearcherProduct landlordProduct1 = TestHelper.generatePropertySearcherProduct();
                PropertySearcherProduct savedProduct1 = productRepository.save(landlordProduct1);

                PropertySearcherProduct PropertySearcherProduct2 = TestHelper.generatePropertySearcherProduct();
                PropertySearcherProduct savedProduct2 = productRepository.save(PropertySearcherProduct2);

                comparePropertySearcherProduct(productRepository.findById(savedProduct1.getId()).get(), savedProduct1);
                comparePropertySearcherProduct(productRepository.findById(savedProduct2.getId()).get(), savedProduct2);

                productRepository.deleteById(savedProduct1.getId());

                assertTrue(productRepository.findById(savedProduct1.getId()).isEmpty());
                comparePropertySearcherProduct(productRepository.findById(PropertySearcherProduct2.getId()).get(),
                        savedProduct2);
            });

            it("delete product - success", () -> {
                PropertySearcherProduct landlordProduct1 = TestHelper.generatePropertySearcherProduct();
                PropertySearcherProduct savedProduct1 = productRepository.save(landlordProduct1);

                PropertySearcherProduct PropertySearcherProduct2 = TestHelper.generatePropertySearcherProduct();
                PropertySearcherProduct savedProduct2 = productRepository.save(PropertySearcherProduct2);

                comparePropertySearcherProduct(productRepository.findById(savedProduct1.getId()).get(), savedProduct1);
                comparePropertySearcherProduct(productRepository.findById(savedProduct2.getId()).get(), savedProduct2);

                productRepository.delete(savedProduct1);

                assertTrue(productRepository.findById(savedProduct1.getId()).isEmpty());
                comparePropertySearcherProduct(productRepository.findById(PropertySearcherProduct2.getId()).get(),
                        savedProduct2);
            });

        });
    }
}
