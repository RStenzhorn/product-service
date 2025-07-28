package de.rjst.ps.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

@RequiredArgsConstructor
@Configuration
public class CacheConfig {

    private final CacheProperties cacheProperties;

    public static final String PRODUCT_CACHE = "productCache";
    public static final String ALL_PRODUCT_KEY = "allProducts";
    public static final String ALL_PRODUCT_KEY_EXPRESSION = "'allProducts'";

    @Bean
    public RedisSerializer<Object> redisSerializer(ObjectMapper objectMapper) {
        return new JsonRedisSerializer<>(objectMapper, Object.class);
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration(RedisSerializer<Object> redisSerializer) {
        final var redisProperties = cacheProperties.getRedis();
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(redisProperties.getTimeToLive())
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer));
    }

}
