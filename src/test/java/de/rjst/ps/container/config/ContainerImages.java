package de.rjst.ps.container.config;

import org.mockserver.client.MockServerClient;
import org.testcontainers.utility.DockerImageName;


public class ContainerImages {

    public static final DockerImageName MOCK_SERVER = DockerImageName
            .parse("mockserver/mockserver")
            .withTag("mockserver-" + MockServerClient.class.getPackage().getImplementationVersion());

    public static final DockerImageName POSTGRESQL = DockerImageName.parse("postgres:17.4");

    public static final DockerImageName REDIS = DockerImageName.parse("redis:7.4.3");

}
