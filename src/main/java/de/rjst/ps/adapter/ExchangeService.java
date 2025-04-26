package de.rjst.ps.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "exchange")
public interface ExchangeService {

    @GetMapping("/v1/latest")
    ExchangeResponse getExchange();

}
