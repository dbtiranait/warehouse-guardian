package com.denisballa.centralservice.service;

import com.denisballa.centralservice.model.SensorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * LogService is responsible for logging sensor messages
 * in a consistent and readable format.
 */
@Slf4j
@Service
public class LogService {

    /**
     * Logs the sensor message in a specific format.
     *
     * @param message the sensor message to log
     */
    public void log(SensorMessage message) {
        String output = String.format("📥 SENSOR LOG | ID: %s | TYPE: %s | VALUE: %d",
                message.getSensorId(),
                message.getType().toUpperCase(),
                message.getValue());
        log.info(output);
    }

}
