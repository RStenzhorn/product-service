package de.rjst.ps.api;

import de.rjst.ps.api.model.Product;
import de.rjst.ps.api.model.ProductDto;
import de.rjst.ps.api.model.ProductPrice;
import de.rjst.ps.logic.CreateProductFunction;
import de.rjst.ps.logic.ProductPriceSupplier;
import de.rjst.ps.logic.ProductsSupplier;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Products")
@RequiredArgsConstructor
@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ProductController {

    private final ProductsSupplier productsSupplier;
    private final CreateProductFunction createProductFunction;
    private final ProductPriceSupplier productPriceSupplier;

    @GetMapping(ProductEndpoint.PRODUCTS)
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getProducts() {
        return productsSupplier.get();
    }

    @PostMapping(ProductEndpoint.PRODUCTS)
    @ResponseStatus(HttpStatus.OK)
    public Product createProduct(@Valid @RequestBody final ProductDto productDto) {
        return createProductFunction.apply(productDto);
    }

    @GetMapping(ProductEndpoint.PRODUCT_PRICE)
    @ResponseStatus(HttpStatus.OK)
    public ProductPrice getProductPrice(@Min(1L) @PathVariable final Long id) {
        return productPriceSupplier.apply(id);
    }

}
