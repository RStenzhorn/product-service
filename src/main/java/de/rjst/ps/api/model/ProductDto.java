package de.rjst.ps.api.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {

    @NotBlank
    private String name;

    private String description;

    @Min(0L)
    @Max(10000L)
    private BigDecimal price;

}
