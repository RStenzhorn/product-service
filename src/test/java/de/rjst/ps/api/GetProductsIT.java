package de.rjst.ps.api;

import com.redis.testcontainers.RedisContainer;
import de.rjst.ps.api.model.ErrorResponse;
import de.rjst.ps.api.model.Product;
import de.rjst.ps.container.ContainerController;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContainerTest
public class GetProductsIT {

    @Autowired
    private ProductControllerRestAssured unterTest;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TableLockRepository tableLockRepository;

    @Autowired
    private CacheCleaner cacheCleaner;

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
        ContainerController.pause(redisContainer);

        final var result = unterTest.getProducts();
        final var response = result.body().as(ErrorResponse.class);

        assertThat(result.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getMessage()).isEqualTo("Redis command timed out");

        ContainerController.unpause(redisContainer);
    }
}
