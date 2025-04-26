package de.rjst.ps.container.mock;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.mockserver.client.MockServerClient;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@RequiredArgsConstructor
@Component
@Profile("container")
public class ExchangeServiceMock {

    private static final String PATH = "/v1/latest";
    private static final String RESPONSE_EXAMPLE_BODY_JSON = "/response/example_body.json";

    private final MockServerClient mockServerClient;

    public void getExchange(@NonNull final HttpStatus httpStatus) {
        final var requestDefinition = request()
                .withMethod(HttpMethod.GET.name())
                .withPath(PATH);

        mockServerClient.clear(requestDefinition);
        mockServerClient.when(
                requestDefinition
        ).respond(
                response()
                        .withStatusCode(httpStatus.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(getBody())
        );
    }

    @SneakyThrows
    @NonNull
    private static String getBody() {
        final InputStreamSource source = new ClassPathResource(RESPONSE_EXAMPLE_BODY_JSON);
        final var inputStream = source.getInputStream();
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

}
