package com.minvoice.demo.application.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "app")
@Validated
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AppProperties {
    private String invoiceFileNewPath;


}
