package de.rjst.ps.database;

import de.rjst.ps.container.ContainerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContainerTest
class ProductRepositoryIT {

    @Autowired
    private ProductRepository underTest;

    @Test
    void findAll_returnsAllProducts() {
        final var products = underTest.findAll();

        assertThat(products.size()).isGreaterThan(0);
    }

}
