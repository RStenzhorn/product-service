package de.rjst.ps.container.restassured;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.context.ConfigurableWebServerApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class RestAssuredSpecsConfig {

    private final ConfigurableWebServerApplicationContext context;

    @Bean
    public RequestSpecification requestSpecification() {
        final var webServer = context.getWebServer();
        final var port = webServer.getPort();

        final var builder = new RequestSpecBuilder();
        builder.setContentType(ContentType.JSON);
        builder.setAccept(ContentType.JSON);
        builder.setPort(port);
        builder.log(LogDetail.ALL);

        return builder.build();
    }

    @Bean
    public ResponseSpecification responseSpecification() {
        final var builder = new ResponseSpecBuilder();
        builder.log(LogDetail.ALL);
        return builder.build();
    }
}
