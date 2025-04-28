package de.rjst.ps.api;

import com.github.dockerjava.api.DockerClient;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static de.rjst.ps.TestConstant.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContainerTest
class ProductControllerIT {

    @Autowired
    private ProductControllerRestAssured unterTest;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TableLockRepository tableLockRepository;

    @Autowired
    private CacheCleaner cacheCleaner;

    @Autowired
    private ExchangeServiceMock exchangeServiceMock;

    @Autowired
    private RedisContainer redisContainer;

    @BeforeEach
    void setUp() {
        cacheCleaner.run();
    }

    @Test
    @DisplayName("GET /products should return HTTP 200 OK with equal list size to database entries")
    void getProductsSuccessful() {
        final var result = unterTest.getProducts();

        final var products = result.body().jsonPath().getList(".", Product.class);

        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(products.size()).isEqualTo(productRepository.count());
    }

    @Transactional
    @Test
    @DisplayName("GET /products should return HTTP 500 reason transaction timeout")
    void getProductsTransactionalTimeout() {
        tableLockRepository.apply("public", "product");

        final var result = unterTest.getProducts();
        assertThat(result.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    @DisplayName("GET /products should return HTTP 500 reason redis down")
    void getProductsRedisDown() {
        final var dockerClient = redisContainer.getDockerClient();
        final var containerId = redisContainer.getContainerId();

        dockerClient.pauseContainerCmd(containerId).exec();

        final var result = unterTest.getProducts();
        final var response = result.body().as(ErrorResponse.class);

        assertThat(result.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getMessage()).isEqualTo("Redis command timed out");

        dockerClient.unpauseContainerCmd(containerId).exec();
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

    @Test
    @DisplayName("GET /product/{id} should return HTTP 200 with prices")
    void getProductPriceSuccessful_ExchangeOk() {
        exchangeServiceMock.getExchange(HttpStatus.OK);

        final var productEntity = productRepository.findAll().stream().findFirst().orElseThrow();
        final var response = unterTest.getProductPrice(productEntity.getId());
        final var prices = response.body().jsonPath().getMap("prices", String.class, BigDecimal.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(prices.size()).isEqualTo(31);
    }

    @Test
    @DisplayName("GET /product/{id} should return HTTP 400 with message not found")
    void getProductPriceNotFound() {
        final var response = unterTest.getProductPrice(MAX_LONG);
        final var products = response.body().as(ErrorResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(products.getMessage()).isEqualTo("Product not found");
    }

    @Test
    @DisplayName("GET /product/{id} should return HTTP 500 with error message")
    void getProductPriceSuccessful_ExchangeBadRequest() {
        exchangeServiceMock.getExchange(HttpStatus.BAD_REQUEST);

        final var productEntity = productRepository.findAll().stream().findFirst().orElseThrow();
        final var response = unterTest.getProductPrice(productEntity.getId());
        final var products = response.body().as(ErrorResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(products.getMessage()).isEqualTo("Error while getting exchange rate");
    }
}
