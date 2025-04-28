package de.rjst.ps.database;

import de.rjst.ps.container.ContainerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContainerTest
class ProductRepositoryIT {

    @Autowired
    private ProductRepository underTest;

    @Test
    @DisplayName("findAll should return more then 0 products")
    void findAll() {
        final var products = underTest.findAll();

        assertThat(products.size()).isGreaterThan(0);
    }

}
