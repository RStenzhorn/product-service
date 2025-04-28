package de.rjst.ps.api;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class ProductEndpoint {

    public static final String PRODUCTS = "/products";
    public static final String PRODUCT_PRICE = "/products/{id}/price";

}
