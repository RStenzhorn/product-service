package de.rjst.ps.adapter;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class ExchangeResponse {
    private String date;
    private BigDecimal amount;
    private Map<String, BigDecimal> rates = Map.of();
    private String base;
}
