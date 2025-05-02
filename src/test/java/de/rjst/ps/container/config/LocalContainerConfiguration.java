package de.rjst.ps.container.config;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static de.rjst.ps.container.config.ContainerImages.POSTGRESQL;
import static de.rjst.ps.container.config.ContainerImages.REDIS;

@TestConfiguration(proxyBeanMethods = false)
public class LocalContainerConfiguration {

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgresContainer() {
        final var postgreSQLContainer = new PostgreSQLContainer<>(POSTGRESQL);
        postgreSQLContainer.withDatabaseName("products");
        postgreSQLContainer.withUsername("postgres");
        postgreSQLContainer.withPassword("password");
        postgreSQLContainer.setPortBindings(List.of("5432:5432"));
        postgreSQLContainer.withInitScript("init.sql");
        return postgreSQLContainer;
    }

    @Bean
    @ServiceConnection(name = "redis")
    public RedisContainer redisContainer() {
        final var redisContainer = new RedisContainer(REDIS);
        redisContainer.setPortBindings(List.of("6379:6379"));
        return redisContainer;
    }

}
