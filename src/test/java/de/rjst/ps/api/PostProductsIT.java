package de.rjst.ps.api;

import com.redis.testcontainers.RedisContainer;
import de.rjst.ps.GenericValidator;
import de.rjst.ps.TestInputObject;
import de.rjst.ps.TestOutputObject;
import de.rjst.ps.api.model.ErrorResponse;
import de.rjst.ps.api.model.Product;
import de.rjst.ps.container.ContainerTest;
import de.rjst.ps.container.datasource.CacheCleaner;
import de.rjst.ps.container.datasource.ProductControllerRestAssured;
import de.rjst.ps.container.datasource.TableLockRepository;
import de.rjst.ps.container.mock.ExchangeServiceMock;
import de.rjst.ps.database.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static de.rjst.ps.TestConstant.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContainerTest
public class PostProductsIT {

    @Autowired
    private ProductControllerRestAssured unterTest;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CacheCleaner cacheCleaner;

    @BeforeEach
    void setUp() {
        cacheCleaner.run();
    }


    @Test
    @DisplayName("POST /products should return HTTP 200 OK with created product")
    void createProductSuccessful() {
        final var request = TestInputObject.getProductDto();

        final var result = unterTest.postProductsBody(request);
        final var response = result.body().as(Product.class);

        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        GenericValidator.assertThat(response, TestOutputObject.getProduct());

        final var productEntity = productRepository.findById(response.getId()).orElseThrow();
        GenericValidator.assertThat(productEntity, TestOutputObject.getProductEntity());
    }

    @Test
    @DisplayName("POST /products should return HTTP 400 reason name null")
    void createProductMissingName() {
        final var request = TestInputObject.getProductDto();
        request.setName(null);

        final var result = unterTest.postProductsBody(request);
        final var response = result.body().as(ErrorResponse.class);

        assertThat(result.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getMessage()).isEqualTo("name: darf nicht leer sein");
    }

    @Test
    @DisplayName("POST /products should return HTTP 400 reason name blank")
    void createProductBlankName() {
        final var request = TestInputObject.getProductDto();
        request.setName(BLANK_STRING);

        final var result = unterTest.postProductsBody(request);
        final var response = result.body().as(ErrorResponse.class);

        assertThat(result.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getMessage()).isEqualTo("name: darf nicht leer sein");
    }

    @Test
    @DisplayName("POST /products should return HTTP 400 reason price null")
    void createProductMissingPrice() {
        final var request = TestInputObject.getProductDto();
        request.setPrice(null);

        final var result = unterTest.postProductsBody(request);
        final var response = result.body().as(ErrorResponse.class);

        assertThat(result.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getMessage()).isEqualTo("price: darf nicht null sein");
    }

    @Test
    @DisplayName("POST /products should return HTTP 400 reason price to high")
    void createProductPriceToHigh() {
        final var request = TestInputObject.getProductDto();
        request.setPrice(HIGH_PRICE);

        final var result = unterTest.postProductsBody(request);
        final var response = result.body().as(ErrorResponse.class);

        assertThat(result.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getMessage()).isEqualTo("price: muss kleiner oder gleich 10000 sein");
    }

    @Test
    @DisplayName("POST /products should return HTTP 400 reason price to low")
    void createProductPriceToLow() {
        final var request = TestInputObject.getProductDto();
        request.setPrice(LOW_PRICE);

        final var result = unterTest.postProductsBody(request);
        final var response = result.body().as(ErrorResponse.class);

        assertThat(result.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getMessage()).isEqualTo("price: muss größer 0 sein");
    }


}
