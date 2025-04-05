package com.denisballa.centralservice.service;

import com.denisballa.centralservice.model.SensorMessage;
import com.denisballa.centralservice.config.ThresholdProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AlarmService {

    private final ThresholdProperties props;

    public AlarmService(ThresholdProperties props) {
        this.props = props;
    }

    public void process(SensorMessage message) {
        int value = message.getValue();
        boolean alarm = switch (message.getType()) {
            case "temperature" -> value > props.getTemperature();
            case "humidity" -> value > props.getHumidity();
            default -> false;
        };
        if (alarm) {
            log.warn("🚨 ALARM: {} reported value {} exceeds threshold!", message.getSensorId(), message.getValue());
        } else {
            log.info("✅ Normal: {} reported value {}", message.getSensorId(), message.getValue());
        }
    }

}
