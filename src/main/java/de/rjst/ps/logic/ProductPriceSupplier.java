package de.rjst.ps.logic;

import de.rjst.ps.api.model.ProductPrice;
import de.rjst.ps.database.ProductEntity;
import de.rjst.ps.database.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class ProductPriceSupplier implements Function<Long, ProductPrice> {

    private final ProductRepository productRepository;
    private final CaculatePriceFunction caculatePriceFunction;

    @Override
    public ProductPrice apply(@NonNull final Long id) {
        final var optionalProduct = productRepository.findById(id);
        final var result = new ProductPrice();

        if (optionalProduct.isPresent()) {
            result.setId(id);
            final var productEntity = optionalProduct.get();
            result.setPrices(caculatePriceFunction.apply(productEntity.getPrice()));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not found");
        }

        return result;
    }
}
