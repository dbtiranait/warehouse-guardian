package com.denisballa.centralservice.service;

import com.denisballa.centralservice.model.SensorMessage;
import com.denisballa.centralservice.config.ThresholdProperties;
import com.denisballa.centralservice.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * AlarmService is responsible for processing sensor messages and checking if they exceed the defined thresholds.
 * If a threshold is exceeded, it logs a warning message; otherwise, it logs an info message.
 */
@Slf4j
@Service
public class AlarmService {

    /**
     * The ThresholdProperties object contains the threshold values for temperature and humidity.
     */
    private final ThresholdProperties props;

    /**
     * Constructor for AlarmService.
     *
     * @param props The ThresholdProperties object containing the threshold values.
     */
    public AlarmService(ThresholdProperties props) {
        this.props = props;
    }

    /**
     * Processes a SensorMessage and checks if the reported value exceeds the defined thresholds.
     *
     * @param message The SensorMessage object containing the sensor ID, type, and value.
     */
    public void process(SensorMessage message) {
        if (Utils.exceedsThreshold(message.getType(), message.getValue(), props.getTemperature(), props.getHumidity())) {
            log.warn("🚨 ALARM: {} reported value {} exceeds threshold!", message.getSensorId(), message.getValue());
        } else {
            log.info("✅ Normal: {} reported value {}", message.getSensorId(), message.getValue());
        }
    }

}
