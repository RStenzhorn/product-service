package de.rjst.ps.logic.mapper;

import de.rjst.ps.api.model.Product;
import de.rjst.ps.database.ProductEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ProductMapper implements Function<ProductEntity, Product> {

    @Override
    public Product apply(@NonNull final ProductEntity productEntity) {

        final var result = new Product();
        result.setId(productEntity.getId());
        result.setName(productEntity.getName());
        result.setDescription(productEntity.getDescription());
        result.setPrice(productEntity.getPrice());

        return result;
    }
}
