package de.rjst.ps.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.rjst.ps.GenericValidator;
import de.rjst.ps.TestInputObject;
import de.rjst.ps.api.model.Product;
import de.rjst.ps.container.ContainerTest;
import de.rjst.ps.container.datasource.CacheCleaner;
import de.rjst.ps.container.datasource.ProductControllerRestAssured;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContainerTest
class CacheIT {

    @Autowired
    private ProductControllerRestAssured unterTest;

    @Autowired
    private CacheCleaner cacheCleaner;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        cacheCleaner.run();
    }

    @Test
    @DisplayName("GET /products should return HTTP 200 OK and cached products equal to response products")
    void cachedAfterRequest() {
        final var cache = cacheManager.getCache(CacheConfig.PRODUCT_CACHE);
        assertThat(cache).isNotNull();
        assertThat(cache.get(CacheConfig.ALL_PRODUCT_KEY)).isNull();

        final var result = unterTest.getProducts();
        final var products = result.body().jsonPath().getList(".", Product.class);
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());

        final var cachedProducts = getProductsFromCache(cache);

        GenericValidator.assertThat(cachedProducts, products);
    }

    @Test
    @DisplayName("GET + POST /products should return HTTP 2x 200 OK and cache is evicted")
    void cacheEvictedAfterRequest() {
        final var cache = cacheManager.getCache(CacheConfig.PRODUCT_CACHE);

        final var firstCachedResponse = unterTest.getProducts();
        assertThat(firstCachedResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        final var createdResponse = unterTest.postProductsBody(TestInputObject.getProductDto());
        assertThat(createdResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        assertThat(cache.get(CacheConfig.ALL_PRODUCT_KEY)).isNull();
    }

    @Test
    @DisplayName("GET /products should return HTTP 200 OK and cache evicts after 10 seconds")
    void cacheEvictedAfterTimeout() {
        final var cache = cacheManager.getCache(CacheConfig.PRODUCT_CACHE);

        final var cachedResponse = unterTest.getProducts();
        assertThat(cachedResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(cache.get(CacheConfig.ALL_PRODUCT_KEY)).isNotNull();

        Awaitility.await()
                .pollInterval(250L, TimeUnit.MILLISECONDS)
                .atLeast(9L, TimeUnit.SECONDS)
                .atMost(15L, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    assertThat(cache.get(CacheConfig.ALL_PRODUCT_KEY)).isNull();
                });
    }


    private List<Product> getProductsFromCache(final Cache cache) {
        final var cachedObjects = (Collection<Object>) cache.get(CacheConfig.ALL_PRODUCT_KEY).get();
        return cachedObjects.stream()
                .map(e -> objectMapper.convertValue(e, Product.class))
                .toList();
    }

}
