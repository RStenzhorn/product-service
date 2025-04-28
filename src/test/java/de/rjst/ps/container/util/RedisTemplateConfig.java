package de.rjst.ps.container.util;

import de.rjst.ps.api.model.Product;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Configuration
public class RedisTemplateConfig {

    @Bean
    public RedisTemplate<String, List<Product>> redisTemplate(final RedisConnectionFactory redisConnectionFactory) {
        final var template = new RedisTemplate<String, List<Product>>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

}
