package de.rjst.ps.api.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    @Digits(integer = 5, fraction = 2)
    @DecimalMin(value = "0", inclusive = false)
    @DecimalMax("10000")
    private BigDecimal price;

}
