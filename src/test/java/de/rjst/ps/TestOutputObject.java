package de.rjst.ps;

import de.rjst.ps.api.model.Product;
import de.rjst.ps.api.model.ProductDto;
import de.rjst.ps.database.ProductEntity;
import lombok.NoArgsConstructor;

import static de.rjst.ps.TestConstant.ANY_STRING;
import static de.rjst.ps.TestConstant.OTHER_STRING;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class TestOutputObject {

    public static Product getProduct() {
        final var result = new Product();
        result.setName(ANY_STRING);
        result.setDescription(OTHER_STRING);
        result.setPrice(TestConstant.MAX_PRICE);
        return result;
    }

    public static ProductEntity getProductEntity() {
        final var result = new ProductEntity();
        result.setName(ANY_STRING);
        result.setDescription(OTHER_STRING);
        result.setPrice(TestConstant.MAX_PRICE);
        return result;
    }

}
