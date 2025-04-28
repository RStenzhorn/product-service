package de.rjst.ps.logic;

import de.rjst.ps.api.model.Product;
import de.rjst.ps.api.model.ProductDto;
import de.rjst.ps.database.ProductRepository;
import de.rjst.ps.logic.mapper.ProductEntityMapper;
import de.rjst.ps.logic.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.function.Function;

import static de.rjst.ps.cache.CacheConfig.ALL_PRODUCT_KEY_EXPRESSION;
import static de.rjst.ps.cache.CacheConfig.PRODUCT_CACHE;

@RequiredArgsConstructor
@Service
public class CreateProductFunction implements Function<ProductDto, Product> {

    private final ProductRepository productRepository;
    private final ProductEntityMapper productEntityMapper;
    private final ProductMapper productMapper;

    @CacheEvict(cacheNames = PRODUCT_CACHE, key = ALL_PRODUCT_KEY_EXPRESSION)
    @Override
    public Product apply(final ProductDto productDto) {
        final var productEntity = productEntityMapper.apply(productDto);
        final var saved = productRepository.save(productEntity);
        return productMapper.apply(saved);
    }
}
