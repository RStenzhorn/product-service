package de.rjst.ps.logic;

import de.rjst.ps.api.model.Product;
import de.rjst.ps.api.model.ProductDto;
import de.rjst.ps.cache.CacheConfig;
import de.rjst.ps.container.ContainerTest;
import de.rjst.ps.container.restassured.ProductRestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheStatistics;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@ContainerTest
class ProductsSupplierIT {

    @Autowired
    private ProductRestAssured unterTest;

    @Autowired
    private CacheManager cacheManager;

    @Test
    void testCacheStatistics() {
        RedisCache cache = (RedisCache) cacheManager.getCache(CacheConfig.PRODUCT_CACHE);
        CacheStatistics statistics = cache.getStatistics();
        assertThat(statistics.getHits()).isEqualTo(0L);

        unterTest.getProducts();
        statistics = cache.getStatistics();
        assertThat(statistics.getHits()).isEqualTo(0L);

        unterTest.getProducts();
        statistics = cache.getStatistics();
        assertThat(statistics.getHits()).isEqualTo(1L);

    }
}

