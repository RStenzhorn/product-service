package de.rjst.ps.container;

import lombok.experimental.UtilityClass;
import org.testcontainers.containers.ContainerState;
import org.testcontainers.containers.GenericContainer;

@UtilityClass
public class ContainerController {

    public static void pause(final ContainerState container) {
        final var dockerClient = container.getDockerClient();
        final var containerId = container.getContainerId();

        dockerClient.pauseContainerCmd(containerId).exec();
    }

    public static void unpause(final ContainerState container) {
        final var dockerClient = container.getDockerClient();
        final var containerId = container.getContainerId();

        dockerClient.unpauseContainerCmd(containerId).exec();
    }

}
