package de.rjst.ps;

import org.assertj.core.api.AssertionsForClassTypes;

public class GenericValidator {

    public static <T> void assertThat(final T actual, final T expected) {
        AssertionsForClassTypes.assertThat(actual)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expected);
    }

}
