package de.rjst.ps.adapter;

import de.rjst.ps.container.ContainerTest;
import de.rjst.ps.container.mock.ExchangeServiceMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ContainerTest
class ExchangeRestTemplateIT {

    @Autowired
    private ExchangeRestTemplate unterTest;

    @Autowired
    private ExchangeServiceMock exchangeServiceMock;

    @Test
    void getExchange_mockBadRequest_throwsBadRequestException() {
        exchangeServiceMock.getExchange(HttpStatus.BAD_REQUEST);

        assertThrows(HttpClientErrorException.BadRequest.class, () -> unterTest.getExchange());
    }

    @Test
    void getExchange_mockInternalServerError_throwsInternalServerException() {
        exchangeServiceMock.getExchange(HttpStatus.INTERNAL_SERVER_ERROR);

        assertThrows(HttpServerErrorException.InternalServerError.class, () -> unterTest.getExchange());
    }

    @Test
    void getExchange_mockOk_responseNotNull() {
        exchangeServiceMock.getExchange(HttpStatus.OK);

        final var exchange = unterTest.getExchange();

        assertThat(exchange).isNotNull();
    }
}
