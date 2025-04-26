package de.rjst.ps.container.config;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

import static de.rjst.ps.container.config.ContainerImages.POSTGRESQL;
import static de.rjst.ps.container.config.ContainerImages.REDIS;

@TestConfiguration(proxyBeanMethods = false)
public class LocalContainerConfiguration {

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(POSTGRESQL)
                .withDatabaseName("products");
    }

    @Bean
    @ServiceConnection(name = "redis")
    public RedisContainer redisContainer() {
        return new RedisContainer(REDIS);
    }

}
