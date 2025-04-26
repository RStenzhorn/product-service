package de.rjst.ps.adapter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.cloud.openfeign.client.config.exchange")
public class ExchangeProperties {

    private String url;

}
