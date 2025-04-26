package de.rjst.ps.logic;

import de.rjst.ps.api.model.Product;
import de.rjst.ps.database.ProductRepository;
import de.rjst.ps.logic.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

import static de.rjst.ps.cache.CacheConfig.PRODUCT_CACHE;

@RequiredArgsConstructor
@Service
public class ProductsSupplier implements Supplier<List<Product>> {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Cacheable(PRODUCT_CACHE)
    @Override
    public List<Product> get() {
        return productRepository.findAll()
                .stream()
                .map(productMapper)
                .toList();
    }
}
