package de.rjst.ps.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class ExchangeRestTemplate {

    private final RestTemplate restTemplate;
    private final ExchangeProperties exchangeProperties;

    public ExchangeResponse getExchange() {
        return restTemplate.getForObject(exchangeProperties.getUrl() + "/v1/latest", ExchangeResponse.class);
    }
}
