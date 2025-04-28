package de.rjst.ps.container.datasource;

import de.rjst.ps.api.ProductEndpoint;
import de.rjst.ps.api.model.ProductDto;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import static io.restassured.RestAssured.given;

@RequiredArgsConstructor
@Service
@Profile("container")
public class ProductControllerRestAssured {

    private static final String ID = "id";

    private final RequestSpecification requestSpecification;
    private final ResponseSpecification responseSpecification;

    public ExtractableResponse<Response> getProducts() {
        return given()
                .spec(requestSpecification)
                .get(ProductEndpoint.PRODUCTS)
                .then()
                .spec(responseSpecification)
                .extract();
    }

    public ExtractableResponse<Response> postProductsBody(final ProductDto productDto) {
        return given()
                .spec(requestSpecification)
                .body(productDto)
                .post(ProductEndpoint.PRODUCTS)
                .then()
                .spec(responseSpecification)
                .extract();
    }

    public ExtractableResponse<Response> getProductPrice(final Long id) {
        return given()
                .spec(requestSpecification)
                .pathParams(ID, id)
                .get(ProductEndpoint.PRODUCT_PRICE)
                .then()
                .spec(responseSpecification)
                .extract();
    }


}
