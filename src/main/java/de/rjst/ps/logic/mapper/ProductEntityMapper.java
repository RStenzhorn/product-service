package de.rjst.ps.logic.mapper;

import de.rjst.ps.api.model.ProductDto;
import de.rjst.ps.database.ProductEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ProductEntityMapper implements Function<ProductDto, ProductEntity> {

    @Override
    public ProductEntity apply(@NonNull final ProductDto productDto) {

        final var result = new ProductEntity();
        result.setId(null);
        result.setName(productDto.getName());
        result.setDescription(productDto.getDescription());
        result.setPrice(productDto.getPrice());

        return result;
    }
}
