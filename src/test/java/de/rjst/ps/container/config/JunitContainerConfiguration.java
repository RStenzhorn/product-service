package de.rjst.ps.container.config;

import com.redis.testcontainers.RedisContainer;
import org.mockserver.client.MockServerClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import static de.rjst.ps.container.config.ContainerImages.*;

@TestConfiguration(proxyBeanMethods = false)
public class JunitContainerConfiguration {

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

    @Bean
    public MockServerContainer mockServerContainer() {
        final var mockServerContainer = new MockServerContainer(MOCK_SERVER);
        mockServerContainer.start();
        return mockServerContainer;
    }

    @Bean
    public MockServerClient mockServerClient(final MockServerContainer mockServerContainer) {
        return new MockServerClient(mockServerContainer.getHost(), mockServerContainer.getServerPort());
    }

    @Bean
    public DynamicPropertyRegistrar dynamicPropertyRegistrar(final MockServerContainer mockServerContainer) {
        return registry -> {
            registry.add("spring.cloud.openfeign.client.config.exchange.url", mockServerContainer::getEndpoint);
        };
    }

}
