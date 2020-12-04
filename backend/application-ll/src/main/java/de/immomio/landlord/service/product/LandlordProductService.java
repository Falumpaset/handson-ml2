package de.immomio.landlord.service.product;

import de.immomio.data.landlord.bean.product.LandlordProductBean;
import de.immomio.model.repository.landlord.product.LandlordProductRepository;
import de.immomio.service.landlord.product.LandlordProductDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */
@Service
public class LandlordProductService {
    private LandlordProductDataConverter productDataConverter;

    private LandlordProductRepository productRepository;

    @Autowired
    public LandlordProductService(LandlordProductDataConverter productDataConverter,
            LandlordProductRepository productRepository) {
        this.productDataConverter = productDataConverter;
        this.productRepository = productRepository;
    }

    public List<LandlordProductBean> getAllProducts() {
        return productRepository.findAll().stream().map(productDataConverter::convertProductToProductBean).collect(Collectors.toList());
    }
}
