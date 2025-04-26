package de.rjst.ps.api.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class ProductPrice {

    private Long id;
    private Map<String, BigDecimal> prices;

}
