package de.rjst.ps.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@RequiredArgsConstructor
@Configuration
public class CacheConfig {

    private final CacheProperties cacheProperties;

    public static final String PRODUCT_CACHE = "productCache";

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        final var redisProperties = cacheProperties.getRedis();
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(redisProperties.getTimeToLive())
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new Class2JsonRedisSerializer<>(Object.class)));
    }

}
