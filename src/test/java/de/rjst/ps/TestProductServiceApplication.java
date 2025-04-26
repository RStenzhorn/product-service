package de.rjst.ps;

import de.rjst.ps.container.config.LocalContainerConfiguration;
import org.springframework.boot.SpringApplication;

public class TestProductServiceApplication {

    private static final String LOCAL = "local";

    public static void main(final String[] args) {
        SpringApplication.from(ProductServiceApplication::main)
                .with(LocalContainerConfiguration.class)
                .withAdditionalProfiles(LOCAL)
                .run(args);
    }

}
