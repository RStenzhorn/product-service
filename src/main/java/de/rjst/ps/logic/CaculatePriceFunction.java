package de.rjst.ps.logic;

import de.rjst.ps.adapter.ExchangeResponse;
import de.rjst.ps.adapter.ExchangeService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Service
public class CaculatePriceFunction implements Function<BigDecimal, Map<String, BigDecimal>> {

    private static final String EUR = "EUR";

    private final ExchangeService exchangeService;

    @Override
    public Map<String, BigDecimal> apply(final BigDecimal basePrice) {
        final var result = new ConcurrentHashMap<String, BigDecimal>();
        result.put(EUR, basePrice);
        try {
            final var exchangeResponse = exchangeService.getExchange();
            final var exchangeRates = exchangeResponse.getRates();
            exchangeRates.forEach((key, value) ->
                    result.put(key, basePrice.multiply(value).setScale(2, RoundingMode.HALF_UP))
            );
        } catch (final FeignException ex) {
            log.error("Error while getting exchange rate", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while getting exchange rate");
        }

        return result;
    }
}
