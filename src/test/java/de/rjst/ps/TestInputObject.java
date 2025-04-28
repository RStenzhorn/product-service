package de.rjst.ps;

import de.rjst.ps.api.model.ProductDto;
import lombok.NoArgsConstructor;

import static de.rjst.ps.TestConstant.ANY_STRING;
import static de.rjst.ps.TestConstant.OTHER_STRING;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class TestInputObject {

    public static ProductDto getProductDto() {
        final var result = new ProductDto();
        result.setName(ANY_STRING);
        result.setDescription(OTHER_STRING);
        result.setPrice(TestConstant.MAX_PRICE);
        return result;
    }

}
