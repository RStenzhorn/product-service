package de.rjst.ps.api;

import com.redis.testcontainers.RedisContainer;
import de.rjst.ps.api.model.ErrorResponse;
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

import java.math.BigDecimal;

import static de.rjst.ps.TestConstant.MAX_LONG;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContainerTest
public class GetProductPriceIT {


    @Autowired
    private ProductControllerRestAssured unterTest;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CacheCleaner cacheCleaner;

    @Autowired
    private ExchangeServiceMock exchangeServiceMock;


    @BeforeEach
    void setUp() {
        cacheCleaner.run();
    }


    @Test
    @DisplayName("GET /products/{id}/price should return HTTP 200 with prices")
    void getProductPriceSuccessful_ExchangeOk() {
        exchangeServiceMock.getExchange(HttpStatus.OK);

        final var productEntity = productRepository.findAll().stream().findFirst().orElseThrow();
        final var response = unterTest.getProductPrice(productEntity.getId());
        final var prices = response.body().jsonPath().getMap("prices", String.class, BigDecimal.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(prices.size()).isEqualTo(31);
    }

    @Test
    @DisplayName("GET /products/{id}/price should return HTTP 400 with message not found")
    void getProductPriceNotFound() {
        final var response = unterTest.getProductPrice(MAX_LONG);
        final var products = response.body().as(ErrorResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(products.getMessage()).isEqualTo("Product not found");
    }

    @Test
    @DisplayName("GET /products/{id}/price should return HTTP 500 with error message")
    void getProductPriceInternalServerError_ExchangeBadRequest() {
        exchangeServiceMock.getExchange(HttpStatus.BAD_REQUEST);

        final var productEntity = productRepository.findAll().stream().findFirst().orElseThrow();
        final var response = unterTest.getProductPrice(productEntity.getId());
        final var products = response.body().as(ErrorResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(products.getMessage()).isEqualTo("Error while getting exchange rate");
    }

}
