package com.denisballa.centralservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for thresholds.
 * This class is used to bind the properties defined in the application file
 * under the prefix "thresholds".
 */
@Data
@Component
@ConfigurationProperties(prefix = "thresholds")
public class ThresholdProperties {
    private int temperature;
    private int humidity;
}
